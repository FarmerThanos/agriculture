package agriculture.world.blocks;

import agriculture.AgriUtils;
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
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

public class Farmland extends Block {
    public TextureRegion[] baseRegions;
    public float dryRate = 0.001f;

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

        @Override
        public void updateTile() {
            super.updateTile();
            addWater(-dryRate * Time.delta);
        }

        // DEBUG
        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            table.button("water", () -> {
                waterLevel = 100;
            });
        }

        public float addWater(float water){
            float w = waterLevel;
            waterLevel = Mathf.clamp(waterLevel + water, 0f, 100f);
            return waterLevel - w;
        }

        @Override
        public void draw() {
            drawSoil();
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
