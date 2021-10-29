package agriculture;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.GlyphLayout;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Align;
import arc.util.pooling.Pools;
import mindustry.Vars;
import mindustry.ui.Fonts;

public class AgriUtils {
    public static int[] tiles = new int[]{
        39, 36, 39, 36, 27, 16, 27, 24, 39, 36, 39, 36, 27, 16, 27, 24,
        38, 37, 38, 37, 17, 41, 17, 43, 38, 37, 38, 37, 26, 21, 26, 25,
        39, 36, 39, 36, 27, 16, 27, 24, 39, 36, 39, 36, 27, 16, 27, 24,
        38, 37, 38, 37, 17, 41, 17, 43, 38, 37, 38, 37, 26, 21, 26, 25,
        3,  4,  3,  4, 15, 40, 15, 20,  3,  4,  3,  4, 15, 40, 15, 20,
        5, 28,  5, 28, 29, 10, 29, 23,  5, 28,  5, 28, 31, 11, 31, 32,
        3,  4,  3,  4, 15, 40, 15, 20,  3,  4,  3,  4, 15, 40, 15, 20,
        2, 30,  2, 30,  9, 46,  9, 22,  2, 30,  2, 30, 14, 44, 14,  6,
        39, 36, 39, 36, 27, 16, 27, 24, 39, 36, 39, 36, 27, 16, 27, 24,
        38, 37, 38, 37, 17, 41, 17, 43, 38, 37, 38, 37, 26, 21, 26, 25,
        39, 36, 39, 36, 27, 16, 27, 24, 39, 36, 39, 36, 27, 16, 27, 24,
        38, 37, 38, 37, 17, 41, 17, 43, 38, 37, 38, 37, 26, 21, 26, 25,
        3,  0,  3,  0, 15, 42, 15, 12,  3,  0,  3,  0, 15, 42, 15, 12,
        5,  8,  5,  8, 29, 35, 29, 33,  5,  8,  5,  8, 31, 34, 31,  7,
        3,  0,  3,  0, 15, 42, 15, 12,  3,  0,  3,  0, 15, 42, 15, 12,
        2,  1,  2,  1,  9, 45,  9, 19,  2,  1,  2,  1, 14, 18, 14, 13
    };

    /**
     * @author Xelo
     * Gets multiple regions inside a {@link TextureRegion}. The size for each region has to be 32.
     * @param w The amount of regions horizontally.
     * @param h The amount of regions vertically.
     */
    public static TextureRegion[] getRegions(TextureRegion region, int w, int h){
        int size = w * h;
        TextureRegion[] regions = new TextureRegion[size];

        float tileW = (region.u2 - region.u) / w;
        float tileH = (region.v2 - region.v) / h;

        for(int i = 0; i < size; i++){
            float tileX = ((float)(i % w)) / w;
            float tileY = ((float)(i / w)) / h;
            TextureRegion reg = new TextureRegion(region);

            //start coordinate
            reg.u = Mathf.map(tileX, 0f, 1f, reg.u, reg.u2) + tileW * 0.02f;
            reg.v = Mathf.map(tileY, 0f, 1f, reg.v, reg.v2) + tileH * 0.02f;
            //end coordinate
            reg.u2 = reg.u + tileW * 0.96f;
            reg.v2 = reg.v + tileH * 0.96f;

            reg.width = reg.height = 32;

            regions[i] = reg;
        }
        return regions;
    }

    public static void drawText(String text, float x, float y, Color color){
        if(Vars.renderer.pixelator.enabled()) return;

        Font font = Fonts.def;
        boolean ints = font.usesIntegerPositions();
        font.setUseIntegerPositions(false);
        font.getData().setScale(1f);

        font.setColor(color);
        font.draw(text, x, y, Align.left);

        font.setUseIntegerPositions(ints);
        font.setColor(Color.white);
        font.getData().setScale(1f);
        Draw.reset();
    }
}
