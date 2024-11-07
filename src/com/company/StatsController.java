package com.company;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.Time;

public class StatsController {

    @FXML
    private LineChart<Number, Number> aCountChart;

    @FXML
    private LineChart<Number, Number> aggressionChart;

    @FXML
    private LineChart<Number, Number> sizeChart;

    @FXML
    private LineChart<Number, Number> sightCountChart;

    @FXML
    private Text aCountChartText;

    @FXML
    private Button backButton;

    @FXML
    private Text sightCountChartText;

    @FXML
    private Text aggressionChartText;

    @FXML
    private Text sizeChartText;

    @FXML
    private Text titleText;

    XYChart.Series<Number, Number> sizeSeries = new Series<>();
    XYChart.Series<Number, Number> aggressionSeries = new Series<>();
    XYChart.Series<Number, Number> aCountSeries = new Series<>();
    XYChart.Series<Number, Number> fCountSeries = new Series<>();
    XYChart.Series<Number, Number> sightCountSeries = new Series<>();

    @FXML
    void goBack(ActionEvent event) {
        Main.stage.close();
        Main.timeline.stop();
        Main.openSettings(Main.welcomeScene);
    }

    @FXML
    void initialize(){

        sightCountChart.getXAxis().setLabel("Cycles");
        sightCountChart.getYAxis().setLabel("Sight");


        aCountChart.getXAxis().setLabel("Cycles");
        aCountChart.getYAxis().setLabel("Animals & Food");


        aggressionChart.getXAxis().setLabel("Cycles");
        aggressionChart.getYAxis().setLabel("Aggression");


        sizeChart.getXAxis().setLabel("Cycles");
        sizeChart.getYAxis().setLabel("Size");

        sizeChart.getData().addAll(sizeSeries);
        aggressionChart.getData().addAll(aggressionSeries);
        aCountChart.getData().addAll(aCountSeries, fCountSeries);
        sightCountChart.getData().addAll(sightCountSeries);

        aCountSeries.setName("Animal Count");
        fCountSeries.setName("Food Count");

        sizeChart.legendVisibleProperty().setValue(false);
        aggressionChart.legendVisibleProperty().setValue(false);
        aCountChart.legendVisibleProperty().setValue(false);
        sightCountChart.legendVisibleProperty().setValue(false);

        aCountChart.setLegendVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            if (Main.reset == true){
                resetCharts();
                Main.reset = false;
            }
            else if (Main.cycleEnd){
            updateCharts();
            Main.cycleEnd = false;
            }

        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(false);

        timeline.play();

    }

    public void resetCharts(){
        sizeSeries.getData().clear();
        aggressionSeries.getData().clear();
        aCountSeries.getData().clear();
        fCountSeries.getData().clear();
        sightCountSeries.getData().clear();
        updateCharts();
    }

    public void updateCharts(){
        sizeSeries.getData().add(new XYChart.Data<>(Main.currentCycle, (int) (((Main.getAverageRadius()) - 3.9) * 50)));
        aggressionSeries.getData().add(new XYChart.Data<>(Main.currentCycle, Main.getAverageAggression()));
        sightCountSeries.getData().add(new XYChart.Data<>(Main.currentCycle, (int) Main.getAverageSight()));
        aCountSeries.getData().add(new XYChart.Data<>(Main.currentCycle, Main.animals.size()));
        fCountSeries.getData().add(new XYChart.Data<>(Main.currentCycle, Main.foods.size()));
    }
}
