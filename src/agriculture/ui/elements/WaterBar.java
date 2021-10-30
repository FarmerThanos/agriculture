package agriculture.ui.elements;

import arc.Core;
import arc.func.Floatp;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import mindustry.gen.Tex;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;

public class WaterBar extends Table {
    public Color colorValid, colorInvalid;
    public Floatp value;
    public float maxVal, minVal;

    public WaterBar(String name, Color colorValid, Color colorInvalid, Floatp value, float minVal, float maxVal){
        this.name = name;
        this.colorValid = colorValid;
        this.colorInvalid = colorInvalid;
        this.value = value;
        this.maxVal = maxVal;
        this.minVal = minVal;
    }

    @Override
    public void draw() {
        clipBegin();
        float val;
        try{
            val = value.get() / 100f;
        }catch(Exception ignored){
            val = 0f;
        }

        Draw.colorl(0.1f);
        Fill.rects(x, y, width, height - 10f, 0);
        Draw.color(colorInvalid);
        Fill.rects(x, y, width * val, height - 10f, 0);
        Draw.color(colorValid);
        Fill.rects(x, y, Math.min((width * (maxVal / 100f)), width * val), height - 10f, 0);
        Draw.color(colorInvalid);
        Fill.rects(x, y, Math.min((width * (minVal / 100f)), width * val), height - 10f, 0);

        Lines.stroke(2.5f, Color.white);
        Lines.line(x + width * (minVal / 100f), y, x + width * (minVal / 100f), y + height);
        Lines.line(x + width * (maxVal / 100f), y, x + width * (maxVal / 100f), y + height);
        Drawf.tri(x + width * val, y + height, 10f, 10f, -90f);

        Draw.reset();
        clipEnd();
    }
}
