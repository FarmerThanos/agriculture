package agriculture.ui.tables;

import agriculture.entities.Plant;
import agriculture.ui.elements.ValueBar;
import agriculture.ui.elements.WaterBar;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public class PlantInfoTable extends Table {
    public PlantInfoTable(Plant plant){
        super(Styles.black5);
        top().left();

        table(t -> {
            if(plant.type == null) return;
            t.top().left();
            t.row();
            t.labelWrap(() -> plant.type.item.emoji() + " " + plant.type.name).top().left().padBottom(5f);
            t.row();
            t.table(tt -> {
                tt.top().left();
                tt.add(new ValueBar(
                    "Health",
                    () -> Mathf.floor(plant.health) + "%",
                    Pal.health,
                    () -> plant.health,
                    100f
                )).top().left().growX().padTop(5f);
                tt.row();

                tt.add(new ValueBar(
                    "Maturity",
                    () -> Mathf.floor(plant.growth) + "%",
                    Pal.accent,
                    () -> plant.growth,
                    100f
                )).top().left().growX().padTop(5f);
                tt.row();

                tt.add(new ValueBar(
                    "Yield",
                    () -> plant.type.item.emoji() + Mathf.floor(plant.type.itemAmount * plant.growth / 100f),
                    plant.type.item.color,
                    () -> Mathf.floor(plant.type.itemAmount * plant.growth / 100f),
                    plant.type.itemAmount
                )).top().left().growX().padTop(5f);
            }).top().left().growX().padBottom(5f);
            t.row();
            t.labelWrap(() -> {
                if(plant.waterLevel < plant.type.minWater) return "[accent]Underwatered[]";
                if(plant.waterLevel > plant.type.maxWater) return "[accent]Overwatered[]";
                return "Ideal";
            }).center().padBottom(5f).get().setAlignment(Align.center);
            t.row();

            t.table(i -> {
                i.add(new WaterBar("water bar",
                    Pal.heal,
                    Pal.accent,
                    () -> plant.waterLevel,
                    plant.type.minWater,
                    plant.type.maxWater
                )).top().left().grow().padTop(5f);
            }).height(45f).growX();
            t.row();

            t.label(() -> plant.type.minWater + "% - " + plant.type.maxWater + "%").top().left();
        }).grow().pad(5f);

        visible(() -> plant.type != null);
    }
}
