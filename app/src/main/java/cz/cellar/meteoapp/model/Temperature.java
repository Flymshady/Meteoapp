package cz.cellar.meteoapp.model;

import android.graphics.Color;

public class Temperature {
    private float value;
    private int color;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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
            this.status=2;
        }
        else if(value>0 && value<=4){
            this.status=2;
        }
        else if(value>4 && value<=10){
            this.status=1;
        }
        else if(value>10 && value<=20){
            this.status=1;
        }
        else if(value>20 && value<=30){
            this.status=1;
        }
        else if(value>30 && value<=40){
            this.status=2;
        }
        else if(value>40){
            this.status=3;
        }
        else if(value<0 && value>=-10){
            this.status=2;
        }
        else if(value<-10 && value>=-20){
            this.status=2;
        }
        else if(value<-20){
            this.status=3;
        }
        else{
            this.status=2;
            this.color=Color.rgb(255, 255, 255);
        }
    }
}
