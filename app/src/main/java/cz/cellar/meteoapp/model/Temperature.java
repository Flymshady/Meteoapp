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

        if(value>=0 && value<30){
            this.color=Color.rgb(255, 255, Double.valueOf(Math.abs(value-30)*8.5).intValue());
         }else if(value>=30 && value<=100){
            this.color=Color.rgb(255, Double.valueOf(Math.abs(((value-30)*3.6)-255)).intValue(), 0);
        }else if(value<0){
            this.color=Color.rgb(Double.valueOf(Math.abs((Math.abs(value)/1.1)-255)).intValue(), Double.valueOf(Math.abs((Math.abs(value)/1.1)-255)).intValue(), 255);
        }

        if (value==0){
            this.info=("Bod mrazu, námraza na silnicích");
        }
        else if(value>0 && value<=4){
            this.info=("Možná námraza na silnicích");
        }
        else if(value>4 && value<=10){
            this.info=("Chladno, není vyžadována zvláštní pozornost");
        }
        else if(value>10 && value<=20){
            this.info=("Jarní teplota, není vyžadována zvláštní pozornost");
        }
        else if(value>20 && value<=30){
            this.info=("Letní teplota, není vyžadována zvláštní pozornost");
        }
        else if(value>30 && value<=40){
            this.info=("Vysoká teplota, možné ohrožení");
        }
        else if(value>40){
            this.info=("Příliš vysoká teplota, hrozí nebezpečí");
        }
        else if(value<0 && value>=-10){
            this.info=("Teplota pod bodem mrazu");
        }
        else if(value<-10 && value>=-20){
            this.info=("Příliš nízká teplota, možné ohrožení");
        }
        else if(value<-20){
            this.info=("Příliš nízká teplota, hrozí nebezpečí");
        }
        else{
            this.info=("Došlo k chybě");
            this.color=Color.rgb(255, 255, 255);
        }
    }
}
