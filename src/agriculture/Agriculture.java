package agriculture;

import agriculture.content.AgricultureBlocks;
import mindustry.mod.*;

public class Agriculture extends Mod{

    public Agriculture(){

    }

    @Override
    public void loadContent(){
        new AgricultureBlocks().load();
    }

}
