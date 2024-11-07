package com.company;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.time.chrono.JapaneseChronology;

public class Food extends Circle {
    public Food(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        setColorFromRadius();
    }

    public void setColorFromRadius(){
        double r2 = Math.pow(this.getRadius(),2);
        if (Main.foodColor == 1){
            Color green = Color.rgb((int)(120 - r2 * 3), (int)(50 + r2*4),0);
            this.setFill(green);
        }
        else if (Main.foodColor == 0){
            Color purple = Color.rgb((int) (r2 * 5), 0, (int) (250 - r2 * 5));
            this.setFill(purple);
        }
        else if (Main.foodColor == 2){
            Color blue = Color.rgb(0,(int) (150 - r2 * 3), (int)(50 + r2*4));
            this.setFill(blue);
        }
        else {
            Color gray = Color.rgb((int) (r2*r2/5.1),(int) (r2*r2/5.1),(int) (r2*r2/5.1));
            this.setFill(gray);
        }
    }

}

