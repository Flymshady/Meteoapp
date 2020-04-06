package cz.cellar.meteoapp.model;

import android.graphics.Color;

public class Pressure {
    private float value;
    private String info;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void countStats(float value) {
        if (value >= 1010 && value <= 1015) {
            this.info = ("Normální atmosferický tlak");
            this.color = Color.rgb(26, 188, 156 );
        } else if (value > 1015 && value <= 1030) {
            this.info = ("Vyšší atmosferický tlak, možné ohrožení");
            this.color = Color.rgb(20, 143, 119 );
        } else if (value > 1030) {
            this.info = ("Vysoký atmosferický tlak, hrozí nebezpečí");
            this.color = Color.rgb(17, 120, 100);
        } else if (value < 1010 && value >= 995) {
            this.info = ("Nižší atmosferický tlak, možné ohrožení");
            this.color = Color.rgb(118, 215, 196);
        } else if (value < 995) {
            this.info = ("Nízký atmosferický tlak, hrozí nebezpečí");
            this.color = Color.rgb(232, 248, 245);
        }
    }
}
