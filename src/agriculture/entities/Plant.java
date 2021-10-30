package agriculture.entities;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.type.ItemStack;

public class Plant {
    public float x, y;

    public float health;
    public float growth;
    public float waterLevel;
    public boolean alive;

    public PlantType type;

    public Plant(PlantType type, float x, float y){
        this.type = type;
        this.x = x;
        this.y = y;

        reset();
    }

    public void reset(){
        this.health = 100f;
        this.growth = 0f;
        this.alive = true;
    }

    public void update(float waterLevel){
        if(type == null)return;
        this.waterLevel = waterLevel;

        if(alive){
            if(waterValid()){
                type.update(this);
                health += 0.5f * Time.delta;
            }else{
                health -= 0.005f * Time.delta;
            }
        }

        health = Mathf.clamp(health, -0.1f, 100f);
        growth = Mathf.clamp(growth, 0f, 100f);

        if(health < 0f) die();
    }

    public void draw(){
        if(type == null)return;
        type.draw(this);
        Draw.reset();
    }

    public void die(){
        alive = false;
        type.die(this);
    }

    public ItemStack harvest(){
        return new ItemStack(type.item, (int) (type.itemAmount * growth / 100f));
    }

    public boolean waterValid(){
        return type.maxWater > waterLevel && waterLevel > type.minWater;
    }
}
