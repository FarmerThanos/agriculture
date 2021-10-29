package agriculture.content;

import agriculture.entities.Plant;
import agriculture.entities.PlantType;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.struct.IntMap;
import mindustry.content.Blocks;
import mindustry.content.Items;

public class Plants {
    public static IntMap<PlantType> ids = new IntMap<>();
    public static int id;
    public static void register(PlantType type){
        ids.put(id, type);
        type.id = id;
        id++;
    }

    public static PlantType

    spore = new PlantType("spore", Items.sporePod, 15){
        @Override
        public void draw(Plant plant) {
            Draw.color(plant.waterValid() ? Color.green : Color.yellow);
            Draw.rect(Blocks.sporeCluster.region, plant.x, plant.y);
        }
    };
}
