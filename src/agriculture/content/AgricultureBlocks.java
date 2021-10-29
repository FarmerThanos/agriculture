package agriculture.content;

import agriculture.world.blocks.Farmland;
import mindustry.ctype.ContentList;
import mindustry.world.Block;

public class AgricultureBlocks implements ContentList {
    public static Block farmland, plant;

    @Override
    public void load() {
        farmland = new Farmland("farmland");
    }
}
