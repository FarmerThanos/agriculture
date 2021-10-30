package agriculture.world.blocks;

import agriculture.AgriUtils;
import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Geometry;
import mindustry.Vars;

public class Farmland extends PlantBlock{
    public TextureRegion[] baseRegions;
    public Farmland(String name){
        super(name);

        solid = false;
        hasShadow = false;
    }

    @Override
    public void load() {
        super.load();
        baseRegions = AgriUtils.getRegions(Core.atlas.find(name + "-tilemap"), 12, 4);
    }

    public class FarmlandBuild extends PlantBuild {
        public int mask;
        @Override
        public void draw() {
            mask = 0;
            for(int i = 0; i < 8; i++){
                if(Vars.world.build(tileX() + Geometry.d8[i].x, tileY() + Geometry.d8[i].y) instanceof FarmlandBuild){
                    mask |= 1 << i;
                }
            }
            drawSoil();
            Draw.rect(baseRegions[AgriUtils.tiles[mask]], x, y);
            plant.draw();
        }
    }
}
