package agriculture.ui.elements;

import arc.func.Cons;
import arc.func.Floatp;
import arc.func.Prov;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import mindustry.ui.Bar;

public class ValueBar extends Table {
    public ValueBar(String name, Prov<String> text, Color color, Floatp val, float maxVal){
        this.name = name;
        table(t -> {
            t.labelWrap(text::get)
                .top().left()
                .width(80f)
                .get().setAlignment(Align.center);
            t.add(new Bar(name, color, () -> val.get() / maxVal)).align(Align.left).growX();
        }).growX();
    }
}
