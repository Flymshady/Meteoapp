package cz.cellar.meteoapp.model;

import android.graphics.Color;

public class Temperature {
    private float value;
    private String info;
    private int color;

    public Temperature(float value) {
        this.value = value;
    }

    public Temperature() {
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

    public void countStats(float value){
        if (value==0){
            this.info=("Bod mrazu, námraza na silnicích");
            this.color=Color.WHITE;
        }
        else if(value>0 && value<=4){
            this.info=("Možná námraza na silnicích");
            this.color=Color.rgb(254, 249, 231);
        }
        else if(value>4 && value<=10){
            this.info=("Chladno, není vyžadována zvláštní pozornost");
            this.color=Color.rgb(249, 231, 159);
        }
        else if(value>10 && value<=20){
            this.info=("Jarní teplota, není vyžadována zvláštní pozornost");
            this.color=Color.rgb(254, 249, 231);
        }
        else if(value>20 && value<=30){
            this.info=("Letní teplota, není vyžadována zvláštní pozornost");
            this.color=Color.rgb(245, 176, 65 );
        }
        else if(value>30 && value<=40){
            this.info=("Vysoká teplota, možné ohrožení");
            this.color=Color.rgb(230, 126, 34);
        }
        else if(value>40){
            this.info=("Příliš vysoká teplota, hrozní nebezpečí");
            this.color=Color.rgb(186, 74, 0 );
        }
        else if(value<0 && value>=-10){
            this.info=("Teplota pod bodem mrazu");
            this.color=Color.rgb(133,193,233);
        }
        else if(value<-10 && value>=-20){
            this.info=("Příliš nízká teplota, možné ohrožení");
            this.color=Color.rgb(52, 152, 219 );
        }
        else if(value<-20 && value>=-30){
            this.info=("Příliš nízká teplota, hrozní nebezpečí");
            this.color=Color.rgb(33, 97, 140 );
        }
    }
}
