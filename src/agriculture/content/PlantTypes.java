package agriculture.content;

import agriculture.entities.Plant;
import agriculture.entities.PlantType;
import arc.graphics.g2d.Draw;
import arc.struct.IntMap;
import mindustry.content.Blocks;
import mindustry.content.Items;

public class PlantTypes {
    public static IntMap<PlantType> ids = new IntMap<>();
    public static int id;
    public static void register(PlantType type){
        ids.put(id, type);
        id++;
    }

    public static PlantType

    spore = new PlantType("spore", Items.sporePod, 15){
        @Override
        public void draw(Plant plant) {
            Draw.rect(Blocks.sporeCluster.region, plant.x, plant.y);
        }
    },

    bush = new PlantType("bush", Items.coal, 15){
        @Override
        public void update(Plant plant) {
            super.update(plant);
            plant.health -= 1f;
        }

        @Override
        public void draw(Plant plant) {
            if(!plant.alive){
                Draw.rect(Blocks.oreCoal.region, plant.x, plant.y);
                return;
            }
            Draw.rect(Blocks.pine.region, plant.x, plant.y);
        }
    }
    ;
}
