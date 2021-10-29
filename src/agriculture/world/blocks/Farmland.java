package agriculture.world.blocks;

import agriculture.AgriUtils;
import agriculture.content.PlantTypes;
import agriculture.entities.Plant;
import agriculture.entities.PlantType;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.scene.ui.layout.Table;
import arc.util.Nullable;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.meta.BuildVisibility;

public class Farmland extends Block {
    public TextureRegion[] baseRegions;
    public float dryRate = 0.01f;

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
    public void setBars() {
        super.setBars();
        bars.add("Water Level", b -> new Bar("Water Level", Blocks.water.mapColor,() -> ((FarmlandBuild) b).waterLevel / 100f));
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
            plant.update(waterLevel);
            addWater(-dryRate * Time.delta);
        }

        // DEBUG
        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            table.button("water", () -> {
                waterLevel = 100;
            });
            table.button("bush", () -> {
                setPlant(PlantTypes.bush);
            });
            table.button("spore", () -> {
                setPlant(PlantTypes.spore);
            });
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
            return true;
        }

        public void removePlant(){
            plant.type = null;
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
        public void onProximityUpdate() {
            super.onProximityUpdate();
            mask = 0;
            for(int i = 0; i < 8; i++){
                if(Vars.world.build(tileX() + Geometry.d8[i].x, tileY() + Geometry.d8[i].y) instanceof FarmlandBuild f) {
                    mask |= 1 << i;
                }
            }
        }
    }
}
