package cz.cellar.meteoapp.model;

import android.graphics.Color;

public class Pressure {
    private float value;
    private int status;
    private int color;

    public Pressure(float value) {
        this.value = value;
    }

    public Pressure() {
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


   public void countStats(float value){
       this.color=Color.rgb(Double.valueOf(Math.abs(value-1100)/4.5).intValue(), 255, Double.valueOf(Math.abs(value-1100)/4.5).intValue() );
       if (value >= 1010 && value <= 1015) {
           this.status=1;
       } else if (value > 1015 && value <= 1030) {
           this.status=2;
       } else if (value > 1030) {
           this.status=3;
       } else if (value < 1010 && value >= 995) {
           this.status=2;
       } else if (value < 995) {
           this.status=3;
       }else{
           this.color=Color.rgb(255, 255, 255);
       }

   }
}
