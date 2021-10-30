package agriculture.world.blocks;

import agriculture.AgriUtils;
import agriculture.content.Plants;
import agriculture.entities.Plant;
import agriculture.entities.PlantType;
import agriculture.ui.elements.ValueBar;
import agriculture.ui.tables.PlantInfoTable;
import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Weathers;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

public class Farmland extends Block {
    public TextureRegion[] baseRegions;
    public float dryRate = 0.02f;

    public Farmland(String name){
        super(name);
        update = true;
        updateInUnits = false;

        solid = false;
        hasShadow = false;

        category = Category.crafting;
        buildVisibility = BuildVisibility.shown;
        configurable = true;
    }

    @Override
    public void load() {
        super.load();
        baseRegions = AgriUtils.getRegions(Core.atlas.find(name + "-tilemap"), 12, 4);
    }

    public class FarmlandBuild extends Building {
        public int mask = 0;
        public float waterLevel = 0;
        public Plant plant;

        @Override
        public void created() {
            super.created();
            plant = new Plant(null, x, y);
        }

        @Override
        public void updateTile() {
            super.updateTile();

            // For some reason, OnProximityUpdate() doesn't generate the mask properly.
            mask = 0;
            for(int i = 0; i < 8; i++){
                if(Vars.world.build(tileX() + Geometry.d8[i].x, tileY() + Geometry.d8[i].y) instanceof FarmlandBuild) {
                    mask |= 1 << i;
                }
            }

            plant.update(waterLevel);

            if(Weathers.rain.isActive()){
                addWater((dryRate * 2) * Time.delta);
            }else{
                addWater(-dryRate * Time.delta);
            }
        }

        @Override
        public void display(Table table) {
            super.display(table);
            table.row();
            table.table(t -> {
                t.top().left();
                t.add(new ValueBar("Water Level", () -> Mathf.floor(waterLevel) + "%", Blocks.water.mapColor, () -> waterLevel, 100f)).top().left().growX();
                t.row();
                t.add(new PlantInfoTable(plant)).grow().padTop(5f);
            }).growX().padTop(5f);
        }

        // DEBUG
        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            table.button("water", () -> {
                waterLevel = 100;
            });
            table.button("spore", () -> {
                setPlant(Plants.spore);
            });
            table.button("remove", this::removePlant);
        }

        public float addWater(float water){
            float w = waterLevel;
            waterLevel = Mathf.clamp(waterLevel + water, 0f, 100f);
            return waterLevel - w;
        }

        public boolean plantValid(){
            return plant != null && plant.type != null;
        }

        public boolean setPlant(PlantType type){
            if(plant.type != null)return false;
            plant.type = type;
            plant.reset();
            return true;
        }

        public void removePlant(){
            plant.type = null;
            plant.reset();
        }

        public ItemStack harvest(){
            ItemStack harvest = plant.type == null ? null : plant.harvest();
            removePlant();
            return harvest;
        }

        @Override
        public void draw() {
            drawSoil();
            plant.draw();
            Draw.rect(baseRegions[AgriUtils.tiles[mask]], x, y);
        }

        public void drawSoil(){
            Draw.rect(Blocks.dirt.region, x, y);
            Draw.alpha(waterLevel / 100f);
            Draw.rect(Blocks.mud.region, x, y);
            Draw.alpha(1);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            if(revision >= 2){
                waterLevel = read.f();
                int id = read.i();
                if(id != -1) setPlant(Plants.ids.get(id));
            }
            if(revision >= 3){
                plant.health = read.f();
                plant.growth = read.f();
                plant.alive = read.bool();
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.f(waterLevel);
            write.i(plant.type == null ? -1 : plant.type.id);

            write.f(plant.health);
            write.f(plant.growth);
            write.bool(plant.alive);
        }

        @Override
        public byte version() {
            return 3;
        }
    }
}
