package com.company;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends Application {

    static boolean cycleEnd;
    static boolean reset = false;
    static double globalSize;
    static int globalSight;
    static int globalAggression;
    static int width = 1000;
    static int height = 1000;
    static int borderW = width/6;
    final static int borderH = 0;

    //static Color bgColor = Color.WHITE;

    static double cycleLength; // seconds
    static int currentCycle = 0;
    //static int precycle = 0;

    static int foodCount;
    static int animalCount;

    static int c = 0;

    static AnchorPane root = new AnchorPane();
    static AnchorPane welcomeRoot = new AnchorPane();
    static AnchorPane statsRoot = new AnchorPane();

    static Scene welcomeScene;
    static Scene statsScene;
    static Scene scene = new Scene(root, width, height);

    static Timeline timeline;

    static int animalColor;
    static int foodColor;
    // 0 - pink purple
    // 1 - green
    // 2 - blue
    // 3 - monochrome

    static Stage stage = new Stage();
    static Stage statsStage = new Stage();
    static Rectangle bg = new Rectangle(width, height, Color.WHITE);

    static List<Food> foods = new ArrayList<>();
    static List<Animal> animals = new ArrayList<>();
    static List<Animal> survivors = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        welcomeRoot = FXMLLoader.load(getClass().getResource("welcomeLayout.fxml"));
        welcomeScene = new Scene(welcomeRoot);

        statsRoot = FXMLLoader.load(getClass().getResource("statsLayout.fxml"));
        statsScene = new Scene(statsRoot);

        stage = primaryStage;
        stage.setX(600);
        stage.setY(250);
        openSettings(welcomeScene);
        root.getChildren().addAll(bg);

    /*    timeline.setOnFinished(event -> {
            if (precycle > 0){
            precycle--;
            }
            else {
                endCycle();
            }
        }); */
        //fixed by no timeline finishes and added c counter. = more calculations
    }

    public static void openSettings(Scene scene) {
        statsStage.close();

        stage.setScene(scene);
        stage.setTitle("Simulation Setup");
        stage.setX(600);
        stage.setY(250);
        stage.show();
    }

    public static void generate() {

            timeline = new Timeline(new KeyFrame(Duration.millis(5), event -> {
                for (int i = 0; i < animals.size(); i++) {
                    boolean removed = animals.get(i).move();
                    if (removed) {
                        i--;
                    }
                }
                c++;
                if (c == 200 * cycleLength) {
                    endCycle();
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.setAutoReverse(false);

            resetSimulation();

            timeline.play();

            statsStage.setScene(statsScene);
            statsStage.setX(1100);
            statsStage.setY(200);
            statsStage.setTitle("Statistics");
            statsStage.show();

            stage.setScene(scene);
            stage.setTitle("Simulation");
            stage.setX(50);
            stage.setY(0);

            for (int i = 0; i < Main.foodCount; i++) Main.createFood();
            for (int i = 0; i < Main.animalCount; i++) Main.createAnimal();

            /*

            // color refresh
            for (int i = 0; i < Main.foodCount; i++) foods.get(i).setColorFromRadius();
            for (int i = 0; i < Main.animalCount; i++) animals.get(i).setColorFromRadius();

             */
            /*
            Button button = new Button("back");
            button.setOnAction(event -> {
                stage.close();
                timeline.stop();
                openSettings(welcomeScene);
            });

            root.getChildren().add(button);

             */
        }

    private static void resetSimulation() {
        c = 0;
        currentCycle = 0;
        for (int i = 0; i < animals.size(); i++) {
            root.getChildren().remove(animals.get(i));
        }
        animals.clear();

        for (int i = 0; i < survivors.size(); i++) {
            root.getChildren().remove(survivors.get(i));
        }
        survivors.clear();

        for (int i = 0; i < foods.size(); i++) {
            root.getChildren().remove(foods.get(i));
        }
        foods.clear();

        reset = true;
    }

    static public void endCycle() {
        ageFoods();
        for (int i = 0; i < animals.size(); i++) {
            root.getChildren().remove(animals.get(i));
            createFood();
        }
        animals.clear();

        for (int i = 0; i < survivors.size(); i++) {
            if (i % 2 == 0) createFood();
            Animal animal = survivors.get(i);
            animal.setHungerFromRadius();
            animals.add(animal);
            root.getChildren().add(animal);
        }
        survivors.clear();
        procreate(animals);

        c = 0;

        currentCycle++;
        cycleEnd = true;

        timeline.play();
    }

    static private void procreate(List<Animal> parents) {
        for (int i = 0; i < parents.size()/2; i++) {
            int m = (int) (Math.random()*parents.size());
            int f = (int) (Math.random()*parents.size());
            createAnimal(
                    Math.max(4,((parents.get(f).getRadius() + parents.get(m).getRadius()) / 2)
                            + (Math.random() - 0.5)),
                    (int) (((parents.get(f).getAggression() + parents.get(m).getAggression()) / 2) + (Math.random() - 0.5)*5.0),
                    (int) (((parents.get(f).getSight() + parents.get(m).getSight()) / 2) +  ((Math.random()-0.5) * 10.0)));
        }
    }

    static public double getAverageRadius(){
        double sum = 0;
        for (int i = 0; i < animals.size(); i++) {
            sum = sum + animals.get(i).getRadius();
        }
        return sum/animals.size();
    }

    static public int getAverageAggression(){
        int sum = 0;
        for (int i = 0; i < animals.size(); i++) {
            sum = sum + animals.get(i).getAggression();
        }
        return sum/animals.size();
    }

    static public double getAverageSight(){
        double sum = 0;
        for (int i = 0; i < animals.size(); i++) {
            sum = sum + animals.get(i).getSight();
        }
        return sum/animals.size();
    }

    static private void ageFoods() {
        for (int i = 0; i < foods.size(); i++) {
            foods.get(i).setRadius(foods.get(i).getRadius()-0.5);
            foods.get(i).setColorFromRadius();
            if (foods.get(i).getRadius() < 2) {
                root.getChildren().remove(foods.get(i));
                foods.remove(i);
                i--;
                createFood();
            }
        }

    }

    public static void createFood(){
        int newWidth =  (int) (Math.random()*width + borderW);
        int newHeight = (int) (Math.random()*height + borderH);
            while (newWidth > width - borderW){
                newWidth =  (int) (Math.random()*width + borderW);
            }
        while (newHeight > height - borderH){
            newHeight =  (int) (Math.random()*height + borderH);
        }

        //Color green = Color.rgb((int)(Math.random()*50), (int)(Math.random()*200+50), 0);

        double radius = ((Math.random()*3)+3);

        Food food = new Food(newWidth, newHeight, radius);
        foods.add(food);
        root.getChildren().add(food);
    }

    public static void createFood(double radius){
        int newWidth =  (int) (Math.random()*width + borderW);
        int newHeight = (int) (Math.random()*height + borderH);
        while (newWidth > width - borderW){
            newWidth =  (int) (Math.random()*width + borderW);
        }
        while (newHeight > height - borderH){
            newHeight =  (int) (Math.random()*height + borderH);
        }

        //Color green = Color.rgb((int)(Math.random()*50), (int)(Math.random()*200+50), 0);

        Food food = new Food(newWidth, newHeight, radius);
        foods.add(food);
        root.getChildren().add(food);
    }

    public static void createAnimal(){
        int newWidth = width/20;
        if (Math.random()> 0.5) {newWidth =  (int) (Math.random()*width/6);}
        else{newWidth =  (int) (Math.random()*width/6 + width*5/6);}

        int newHeight = (int) (Math.random()*height);

        double radius = Math.max(4,Math.min(6,((Math.random()*2)+4)*globalSize));

        Animal animal = new Animal(newWidth, newHeight,radius);
        animals.add(animal);
        root.getChildren().add(animal);
    }

    public static void createAnimal(double radius, int aggression, int sight){
        int newWidth = width/20;
        if (Math.random()> 0.5) {newWidth =  (int) (Math.random()*width/6);}
        else{newWidth =  (int) (Math.random()*width/6 + width*5/6);}

        int newHeight = (int) (Math.random()*height);

        Animal animal = new Animal(newWidth, newHeight,radius);
        animal.setAggression(aggression);
        animal.setSight(sight);
        animals.add(animal);
        root.getChildren().add(animal);
    }
}
