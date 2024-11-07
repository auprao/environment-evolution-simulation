
package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class WelcomeController {

    @FXML
    private ColorPicker bgPicker;

    @FXML
    private Text aCountText;

    @FXML
    private Text fCountText;

    @FXML
    private Text aColorText;

    @FXML
    private Text fColorText;

    @FXML
    private Slider fCountSlider;

    @FXML
    private Slider aCountSlider;

    @FXML
    private Slider aggressionSlider;

    @FXML
    private Slider sightSlider;

    @FXML
    private Button startButton;

    @FXML
    private Text bgColorText;

    @FXML
    private Text aggressionText;

    @FXML
    private Text sightText;

    @FXML
    private ComboBox<String> fColorBox;

    @FXML
    private ComboBox<String> aColorBox;

    @FXML
    private Button resetButton;

    @FXML
    private Slider aSizeSlider;

    @FXML
    private Text aSizeText;

    @FXML
    private Text cycleLengthText;

    @FXML
    private Slider cycleLengthSlider;

    @FXML
    void resetChoices(ActionEvent event) {
        bgPicker.setValue(Color.BLACK);

        aCountSlider.setValue(50);
        fCountSlider.setValue(50);

        aColorBox.setValue("Pink");
        fColorBox.setValue("Green");

        sightSlider.setValue(150);
        aggressionSlider.setValue(40);

        aSizeSlider.setValue(50);
        cycleLengthSlider.setValue(15);
    }

    @FXML
    void startSimulation(ActionEvent event) {
        Main.bg.setFill(bgPicker.getValue());

        Main.foodCount = (int) (fCountSlider.getValue());
        Main.animalCount = (int) (aCountSlider.getValue());

        Main.animalColor = aColorBox.getSelectionModel().getSelectedIndex();
        Main.foodColor = fColorBox.getSelectionModel().getSelectedIndex();

        Main.globalAggression = (int) (aggressionSlider.getValue());
        Main.globalSight = (int) (sightSlider.getValue());

        Main.globalSize = (aSizeSlider.getValue() / 50);

        Main.cycleLength = (int) (cycleLengthSlider.getValue());

        Main.generate();
    }
}
