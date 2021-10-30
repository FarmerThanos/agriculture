package agriculture.world.blocks;

import agriculture.content.Plants;
import agriculture.entities.Plant;
import agriculture.entities.PlantType;
import agriculture.ui.elements.ValueBar;
import agriculture.ui.tables.PlantInfoTable;
import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Blocks;
import mindustry.content.Weathers;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

public class PlantBlock extends Block {
    public float dryRate = 0.02f;
    public TextureRegion soilRegion, wetSoilRegion;

    public PlantBlock(String name){
        super(name);
        update = true;
        updateInUnits = false;

        category = Category.crafting;
        buildVisibility = BuildVisibility.shown;
        configurable = true;

        config(Integer.class, (PlantBuild b, Integer i) -> {
            if(i == -1){
                b.plant.type = null;
                b.plant.reset();
            }else if(b.plant.type == null){
                b.plant.type = Plants.ids.get(i);
                b.plant.reset();
            }
        });

        config(Float.class, (PlantBuild b, Float f) -> {
            b.waterLevel = f;
        });
    }

    @Override
    public void load() {
        super.load();
        soilRegion = Core.atlas.find(name + "-soil", "dirt1");
        wetSoilRegion = Core.atlas.find(name + "-soil-wet", "mud1");
    }

    public class PlantBuild extends Building {
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
            plant.update(waterLevel);

            if(Weathers.rain.isActive()){
                addWater((dryRate * 2) * Time.delta);
            }else{
                addWater((-dryRate * (plant.type == null ? 1f : plant.type.absorbSpeed)) * Time.delta);
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
                configure(waterLevel);
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

        public void setPlant(PlantType type){
            configure(type.id);
        }

        public void removePlant(){
            configure(-1);
        }

        public ItemStack harvest(){
            ItemStack harvest = plant.type == null ? null : plant.harvest();
            removePlant();
            return harvest;
        }

        @Override
        public void draw() {
            drawSoil();
            super.draw();
            plant.draw();
        }

        public void drawSoil(){
            Draw.rect(soilRegion, x, y);
            Draw.alpha(waterLevel / 100f);
            Draw.rect(wetSoilRegion, x, y);
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
