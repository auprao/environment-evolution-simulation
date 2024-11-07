package com.company;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Iterator;

import static com.company.Animal.Direction.*;

public class Animal extends Circle {
    private int hunger; //40-60
    private int sight; //150
    private double speed;
    private int aggression; // 0-100

    private Food targetF;
    private Animal targetA;

    private Direction direction;
    enum Direction{
        UP, UPRIGHT, RIGHT, DOWNRIGHT, DOWN, DOWNLEFT, LEFT, UPLEFT;

        public static Direction getRandomDirection(){
            return values()[(int) (Math.random() * values().length)];
        }
        public static Direction turn(Direction currentDirection){
            return values()[
                    values()[(currentDirection.ordinal() + (Math.random() < 0.5 ? 1 : -1) + values().length) % values().length].ordinal()];
        //(int) ((((values()[currentDirection.ordinal()]) + (int) (Math.random() - 0.5)))
        }
    }

    public Animal(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        direction = Direction.getRandomDirection();
        setHungerFromRadius();
        hunger = hunger + sight;
        setColorFromRadius();
    //  speed = Math.pow((6/radius),2);
        speed = (6/radius);
        aggression = (int) (((Math.random() * radius * 30) + (Math.min(50, Math.max(0, Main.animals.size() - Main.foods.size()))))
                *(Main.globalAggression)/50.0)
        ;
        if (aggression > 50){
            switch (Main.animalColor){
                case 0 :
                    setStroke(Color.PINK);
                    break;
                case 1 :
                    setStroke(Color.WHITE);
                    break;
                case 2 :
                    setStroke(Color.CYAN);
                    break;
                case 3 :
                    setStroke(Color.RED);
                    break;
            }
        setStrokeWidth(Math.sqrt(aggression)/10);
        }
        sight = ((int) (Math.min(150, Math.random()*150) + Math.pow(radius,2)*2)
            * (Main.globalSight/150));
    }

    //final static int speed = 2;

    public boolean move() {

        Bounds animalBounds = this.localToScene(this.getBoundsInLocal());

        if (aggression > 50 && targetF == null) {

            for (int i = 0; i < Main.animals.size(); i++) {

                if (Main.animals.get(i) == this) {
                    break;
                }

                Bounds preyBounds = Main.animals.get(i).localToScene(Main.animals.get(i).getBoundsInLocal());

                if (animalBounds.intersects(preyBounds)) {
                    hunger = hunger - (int) (Main.animals.get(i).getRadius() * 10);
                    Main.root.getChildren().remove(Main.animals.get(i));
                    Main.animals.remove(i);
                    targetA = null;

                    for (int j = 0; j < Main.animals.size(); j++) {
                        if (j%2==0){
                        Main.animals.get(j).setAggression(Main.animals.get(j).getAggression() + 1);
                        }
                    }

                    return false;
                }

                if (preyBounds.intersects(this.getCenterX() - (sight / 2), this.getCenterY() - (sight / 2), sight, sight)
                        && hunger > 0 && !(Main.animals.contains(targetA))) {
                    targetA = Main.animals.get(i);
                    //System.out.println("found");
                }
                if (preyBounds.intersects(this.getCenterX() - (sight / 2), this.getCenterY() - (sight / 2), sight, sight)
                        && hunger > 0) {
                    double foodDistance = Math.sqrt(
                            Math.pow(this.getCenterX() - Main.animals.get(i).getCenterX(), 2)
                                    + Math.pow(this.getCenterY() - Main.animals.get(i).getCenterY(), 2)
                    );
                    double targetDistance = Math.sqrt(
                            Math.pow(this.getCenterX() - targetA.getCenterX(), 2)
                                    + Math.pow(this.getCenterY() - targetA.getCenterY(), 2)
                    );

                    if (foodDistance < targetDistance) {
                        targetA = Main.animals.get(i);
                        //System.out.println("found new");
                    }
                }
            }
        }
        //else{
        for (int i = 0; i < Main.foods.size(); i++) {

            Bounds foodBounds = Main.foods.get(i).localToScene(Main.foods.get(i).getBoundsInLocal());

            if (animalBounds.intersects(foodBounds)) {
                //    System.out.println("eat");
                //    System.out.println(Main.foods.size());
                hunger = hunger - (int) (Main.foods.get(i).getRadius() * 10);
                Main.root.getChildren().remove(Main.foods.get(i));
                Main.foods.remove(i);
                targetF = null;
                return false;
            }

            if (foodBounds.intersects(this.getCenterX() - (sight / 2), this.getCenterY() - (sight / 2), sight, sight)
                    && hunger > 0 && !(Main.foods.contains(targetF))) {
                targetF = Main.foods.get(i);
                //System.out.println("found");
            }
            if (foodBounds.intersects(this.getCenterX() - (sight / 2), this.getCenterY() - (sight / 2), sight, sight)
                    && hunger > 0) {
                double foodDistance = Math.sqrt(
                        Math.pow(this.getCenterX() - Main.foods.get(i).getCenterX(), 2)
                                + Math.pow(this.getCenterY() - Main.foods.get(i).getCenterY(), 2)
                );
                double targetDistance = Math.sqrt(
                        Math.pow(this.getCenterX() - targetF.getCenterX(), 2)
                                + Math.pow(this.getCenterY() - targetF.getCenterY(), 2)
                );

                if (foodDistance < targetDistance) {
                    targetF = Main.foods.get(i);
                    //System.out.println("found new");
                }
            }
        }
        //}

            if (hunger > 0) {
                /*
                double newX = ((this.getCenterX() + (Math.random() * 10) - 5) + Main.width )% Main.width;
                double newY = ((this.getCenterY() + (Math.random() * 10) - 5) + Main.height)% Main.height;

                this.setCenterX(newX);
                this.setCenterY(newY);
*/
                double newX = this.getCenterX();
                double newY = this.getCenterY();

                if (Main.foods.contains(targetF)){
                    //System.out.println("going to food");
                    if (targetF.getCenterX() > this.getCenterX()){
                        //go left
                        newX = ((this.getCenterX() + (Math.random() * speed)) + Main.width) % Main.width;
                    }
                    else {
                        //go right
                        newX = ((this.getCenterX() + (Math.random() * speed) - speed) + Main.width) % Main.width;
                    }
                    if (targetF.getCenterY() < this.getCenterY()){
                        //go up
                        newY = ((this.getCenterY() + (Math.random() * speed) - speed) + Main.height) % Main.height;
                    }
                    else {
                        //go down
                        newY = ((this.getCenterY() + (Math.random() * speed)) + Main.height) % Main.height;
                    }

                    this.setCenterX(newX);
                    this.setCenterY(newY);

                    return false;
                }

                else if (Main.animals.contains(targetA)){
                    //System.out.println("going to food");
                    if (targetA.getCenterX() > this.getCenterX()){
                        //go left
                        newX = ((this.getCenterX() + (Math.random() * speed)) + Main.width) % Main.width;
                    }
                    else {
                        //go right
                        newX = ((this.getCenterX() + (Math.random() * speed) - speed) + Main.width) % Main.width;
                    }
                    if (targetA.getCenterY() < this.getCenterY()){
                        //go up
                        newY = ((this.getCenterY() + (Math.random() * speed) - speed) + Main.height) % Main.height;
                    }
                    else {
                        //go down
                        newY = ((this.getCenterY() + (Math.random() * speed)) + Main.height) % Main.height;
                    }

                    this.setCenterX(newX);
                    this.setCenterY(newY);

                    return false;
                }

                if (Math.random() < 0.02) {
                    this.direction = Direction.turn(this.direction);
                }

                if (this.direction == UP){
                    newY = ((this.getCenterY() + (Math.random() * speed) - speed) + Main.height) % Main.height;
                }
                else if (this.direction == DOWN){
                    newY = ((this.getCenterY() + (Math.random() * speed)) + Main.height) % Main.height;
                }
                else if (this.direction == LEFT){
                    newX = ((this.getCenterX() + (Math.random() * speed)) + Main.width) % Main.width;
                }
                else if (this.direction == RIGHT){
                    newX = ((this.getCenterX() + (Math.random() * speed) - speed) + Main.width) % Main.width;
                }

                else if (this.direction == UPRIGHT){
                    newY = ((this.getCenterY() + ((Math.random() * speed) - speed)/2) + Main.height) % Main.height;
                    newX = ((this.getCenterX() + ((Math.random() * speed) - speed)/2) + Main.width) % Main.width;
                }
                else if (this.direction == DOWNRIGHT){
                    newY = ((this.getCenterY() + (Math.random() * speed)) + Main.height) % Main.height;
                    newX = ((this.getCenterX() + ((Math.random() * speed) - speed)/2) + Main.width) % Main.width;
                }
                else if (this.direction == DOWNLEFT){
                    newY = ((this.getCenterY() + (Math.random() * speed)) + Main.height) % Main.height;
                    newX = ((this.getCenterX() + (Math.random() * speed)) + Main.width) % Main.width;
                }
                else if (this.direction == UPLEFT){
                    newY = ((this.getCenterY() + ((Math.random() * speed) - speed)/2) + Main.height) % Main.height;
                    newX = ((this.getCenterX() + (Math.random() * speed)) + Main.width) % Main.width;
                }
                //    newX = ((this.getCenterX() + (Math.random() * 10) - 5) + Main.width) % Main.width;
                //    newY = ((this.getCenterY() + (Math.random() * 10) - 5) + Main.height) % Main.height;

                this.setCenterX(newX);
                this.setCenterY(newY);

            }
            else if (this.getCenterX() < Main.borderW || this.getCenterX() > (Main.width - Main.borderW)){
            //    System.out.println("home");
                Main.survivors.add(this);
                Main.root.getChildren().remove(this);
                Main.animals.remove(this);

                if (Main.animals.size() == 0){
                    //Main.endCycle(Main.timeline);
                    //System.out.println("end");;
                    //Main.precycle++;
                    Main.endCycle();
                }

            //    System.out.println(Main.animals.size());
            //    System.out.println(Main.survivors.size());
             //   return false;
            }
            else if (this.getCenterX() > (Main.width / 2)) {
                this.setCenterX(((this.getCenterX() + (Math.random() * speed)) + Main.width) % Main.width);
            }
            else if (this.getCenterX() < (Main.width / 2)) {
                this.setCenterX(((this.getCenterX() + (Math.random() * speed) - speed) + Main.width) % Main.width);
            }

        return false;
    }

    public void setColorFromRadius() {
        double r2 = Math.pow(this.getRadius(), 2);
        if (Main.animalColor == 1) {
            Color green = Color.rgb((int)(120 - r2 * 3), (int)(50 + r2*4),0);
            this.setFill(green);
        }
        else if (Main.animalColor == 0){
            Color purple = Color.rgb((int) (r2 * 5), 0, (int) (250 - r2 * 5));
            this.setFill(purple);
        }
        else if (Main.animalColor == 2){
            Color blue = Color.rgb(0,(int) (150 - r2 * 3), (int)(50 + r2*4));
            this.setFill(blue);
        }
        else {
            Color gray = Color.rgb((int) (r2*r2/5.2),(int) (r2*r2/5.2),(int) (r2*r2/5.2));
            this.setFill(gray);
        }
    }

    public void setHungerFromRadius() {
        int hunger = (int) (this.getRadius() * 10);
        this.setHunger(hunger);
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getAggression() {
        return aggression;
    }

    public void setAggression(int aggression) {
        this.aggression = aggression;
    }

    public int getSight() {
        return sight;
    }

    public void setSight(int sight) {
        this.sight = sight;
    }
}
    // get coral
/*
    public void move(){

            if (hunger > 0) {
                this.setCenterX((this.getCenterX() + (Math.random() * 10) - 5) % Main.width);
                this.setCenterY((this.getCenterY() + (Math.random() * 10) - 5) % Main.height);
            }

    }

*/