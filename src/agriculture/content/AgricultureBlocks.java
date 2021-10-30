package agriculture.content;

import agriculture.world.blocks.*;
import mindustry.content.Liquids;
import mindustry.ctype.ContentList;
import mindustry.world.Block;

public class AgricultureBlocks implements ContentList {
    public static Block farmland, waterer;

    @Override
    public void load() {
        farmland = new Farmland("farmland");
        waterer = new Waterer("waterer", 80f, 1f){{
            consumes.power(1f);
            consumes.liquid(Liquids.water, 1f);
        }};
    }
}
