package agriculture.world.blocks;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

public class Waterer extends Block{
    public float range, amount;
    public float rotateSpeed = 360f;
    public TextureRegion topRegion;
    public Waterer(String name, float range, float amount){
        super(name);
        this.range = range;
        this.amount = amount;

        solid = true;
        update = true;

        hasLiquids = true;
        hasPower = true;

        buildVisibility = BuildVisibility.shown;
        loopSound = Sounds.spray;
    }

    @Override
    public void load() {
        super.load();
        topRegion = Core.atlas.find(name + "-top", "power-node");
    }

    public class WatererBuild extends Building {
        public float topRotation, topRotationSpeed = 0f;
        @Override
        public void updateTile() {
            super.updateTile();
            if(cons.valid() && enabled){
                Vars.indexer.eachBlock(this, range, b -> b instanceof PlantBlock.PlantBuild, b -> {
                    ((PlantBlock.PlantBuild) b).addWater(((amount / 60f) * (1 - Mathf.curve(Mathf.dst(x, y, b.x, b.y), 0, range)/3)) * Time.delta);
                });

                if(Mathf.chanceDelta(0.5f)){
                    Tmp.v2.setZero().trns(Mathf.range(0, 360), range * Mathf.random()).add(this);
                    Fx.fire.at(Tmp.v2);
                }
                topRotationSpeed = Mathf.lerpDelta(topRotationSpeed, rotateSpeed / 60f, 0.05f);
            }else{
                topRotationSpeed = Mathf.lerpDelta(topRotationSpeed, 0, 0.05f);
            }
            topRotation += topRotationSpeed * Time.delta;
            topRotation %= 360f;
        }

        @Override
        public boolean shouldActiveSound() {
            return cons.valid() && enabled;
        }

        @Override
        public void draw() {
            super.draw();
            Drawf.spinSprite(topRegion, x, y, topRotation);
        }

        @Override
        public void drawSelect() {
            Drawf.dashCircle(x, y, range, team.color);
        }
    }
}
