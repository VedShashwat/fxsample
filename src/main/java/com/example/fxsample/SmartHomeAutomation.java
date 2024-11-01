package com.example.fxsample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SmartHomeAutomation extends Application {
    
    private static final String[] ROOMS = {"Living Room", "Bedroom", "Kitchen"};
    private static final String[] LEVELS = {"Low", "Medium", "High"};
    

    private RoomControl[] roomControls;
    
    @Override
    public void start(Stage primaryStage) {
        GridPane mainGrid = new GridPane();
        mainGrid.setPadding(new Insets(20));
        mainGrid.setHgap(20);
        mainGrid.setVgap(20);
        
        roomControls = new RoomControl[ROOMS.length];
        

        for (int i = 0; i < ROOMS.length; i++) {
            RoomControl roomControl = new RoomControl(ROOMS[i]);
            roomControls[i] = roomControl;
            mainGrid.add(roomControl.getLayout(), i, 0);
        }
        
        Scene scene = new Scene(mainGrid);
        primaryStage.setTitle("Smart Home Automation System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    

    private class RoomControl {
        private String roomName;
        private ToggleButton powerButton;
        private ToggleGroup levelGroup;
        private RadioButton[] levelButtons;
        private Label stateLabel;
        private VBox layout;
        

        private ToggleButton fanPowerButton;
        private ToggleGroup fanSpeedGroup;
        private RadioButton[] fanSpeedButtons;
        private Label fanStateLabel;
        
        public RoomControl(String roomName) {
            this.roomName = roomName;
            initializeControls();
            setupLayout();
            setupListeners();
        }
        
        private void initializeControls() {

            powerButton = new ToggleButton("Turn On");
            levelGroup = new ToggleGroup();
            levelButtons = new RadioButton[LEVELS.length];
            
            for (int i = 0; i < LEVELS.length; i++) {
                levelButtons[i] = new RadioButton(LEVELS[i]);
                levelButtons[i].setToggleGroup(levelGroup);
                levelButtons[i].setDisable(true);
            }
            
            stateLabel = new Label("Light is Off");
            

            fanPowerButton = new ToggleButton("Fan: Turn On");
            fanSpeedGroup = new ToggleGroup();
            fanSpeedButtons = new RadioButton[LEVELS.length];
            
            for (int i = 0; i < LEVELS.length; i++) {
                fanSpeedButtons[i] = new RadioButton(LEVELS[i]);
                fanSpeedButtons[i].setToggleGroup(fanSpeedGroup);
                fanSpeedButtons[i].setDisable(true);
            }
            
            fanStateLabel = new Label("Fan is Off");
        }
        
        private void setupLayout() {
            layout = new VBox(10);
            layout.setPadding(new Insets(10));
            layout.setStyle("-fx-border-color: #ac6363; -fx-border-radius: 5;");
            
            Label roomLabel = new Label(roomName);
            roomLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
            

            VBox lightControls = new VBox(5);
            lightControls.getChildren().add(new Label("Light Controls:"));
            lightControls.getChildren().add(powerButton);
            
            HBox brightnessBox = new HBox(10);
            brightnessBox.getChildren().addAll(levelButtons);
            brightnessBox.setAlignment(Pos.CENTER);
            
            lightControls.getChildren().add(brightnessBox);
            lightControls.getChildren().add(stateLabel);
            

            VBox fanControls = new VBox(5);
            fanControls.getChildren().add(new Label("Fan Controls:"));
            fanControls.getChildren().add(fanPowerButton);
            
            HBox speedBox = new HBox(10);
            speedBox.getChildren().addAll(fanSpeedButtons);
            speedBox.setAlignment(Pos.CENTER);
            
            fanControls.getChildren().add(speedBox);
            fanControls.getChildren().add(fanStateLabel);
            
            layout.getChildren().addAll(
                roomLabel,
                new Separator(),
                lightControls,
                new Separator(),
                fanControls
            );
        }
        
        private void setupListeners() {

            powerButton.setOnAction(e -> {
                boolean isOn = powerButton.isSelected();
                powerButton.setText(isOn ? "Turn Off" : "Turn On");
                
                for (RadioButton btn : levelButtons) {
                    btn.setDisable(!isOn);
                }
                
                if (!isOn) {
                    levelGroup.selectToggle(null);
                    stateLabel.setText("Light is Off");
                } else {
                    stateLabel.setText("Light is On");
                }
            });
            
            levelGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && powerButton.isSelected()) {
                    RadioButton selected = (RadioButton) newVal;
                    stateLabel.setText("Light is On, Brightness: " + selected.getText());
                }
            });
            

            fanPowerButton.setOnAction(e -> {
                boolean isOn = fanPowerButton.isSelected();
                fanPowerButton.setText("Fan: " + (isOn ? "Turn Off" : "Turn On"));
                
                for (RadioButton btn : fanSpeedButtons) {
                    btn.setDisable(!isOn);
                }
                
                if (!isOn) {
                    fanSpeedGroup.selectToggle(null);
                    fanStateLabel.setText("Fan is Off");
                } else {
                    fanStateLabel.setText("Fan is On");
                }
            });
            
            fanSpeedGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && fanPowerButton.isSelected()) {
                    RadioButton selected = (RadioButton) newVal;
                    fanStateLabel.setText("Fan is On, Speed: " + selected.getText());
                }
            });
        }
        
        public VBox getLayout() {
            return layout;
        }
    }
}
