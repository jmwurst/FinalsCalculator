package jmwurst.FinalsCalculator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

/*
Definitions:

about alert
input error alert
duplicate entry alert
file output specifications
main scene
    menu bar
        file menu option
        edit menu option
        help menu option
    list view
    list operation controls
        name field
        numerical fields
            current grade field
            final weight field
        button fields
            add button
            update button
            remove button
    list selection specification
 */

public class FinalsCalculator extends Application {

    private ObservableList<Class> classes;
    private int curIndex = -1;
    private BooleanProperty noneSelected = new SimpleBooleanProperty(true);

    public void start(Stage stage) {

        //begin about alert
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("Finals Calculator - About");
        about.setHeaderText("About This Program");
        about.setContentText("Justin Wurst - 2018");
        //end about alert

        //begin input error alert
        Alert inputError = new Alert(Alert.AlertType.ERROR);
        inputError.setTitle("Finals Calculator - Error");
        inputError.setHeaderText("Error - Invalid Data Entry");
        inputError.setContentText("One or more of the numerical values entered are not valid input.");
        //end input error alert

        //begin duplicate entry alert
        Alert duplicateEntry = new Alert(Alert.AlertType.CONFIRMATION);
        duplicateEntry.setTitle("Finals Calculator - Warning");
        duplicateEntry.setHeaderText("Warning - Duplicate Data");
        duplicateEntry.setContentText("The data entered is a duplicate of a class that has already " +
                "been entered. Do you wish to add this data?");
        ButtonType DEyes = new ButtonType("Yes");
        ButtonType DEno = new ButtonType("No");
        ButtonType DEcancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        duplicateEntry.getButtonTypes().setAll(DEyes, DEno, DEcancel);
        //end duplicate entry alert

        //begin file output specifications
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save .CSV");
        fileChooser.setInitialFileName("finals.csv");
        //end file output specifications

        //begin main scene
        classes = FXCollections.observableArrayList();
        Group root = new Group();
        Scene scene = new Scene(root, 560, 400);

        //begin menu bar
        MenuBar bar = new MenuBar();

        //begin file menu option
        Menu file = new Menu("File");
        MenuItem output = new MenuItem("Save as CSV...");
        output.setOnAction(t -> {
            String out = CSVOutput();
            File csvOut = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    FileWriter fw = new FileWriter(csvOut);
                    fw.write(out);
                    fw.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        MenuItem quit = new MenuItem("Quit...");
        quit.setOnAction(t -> Platform.exit());
        file.getItems().addAll(output, quit);
        //end file menu option

        //begin edit menu option
        Menu edit = new Menu("Edit");
        MenuItem clearcl = new MenuItem("Clear...");
        clearcl.setOnAction(t -> {
            classes.clear();
            noneSelected.setValue(true);
        });
        edit.getItems().addAll(clearcl);
        //end edit menu option

        //begin help menu option
        Menu help = new Menu("Help");
        MenuItem aboutpr = new MenuItem("About this program...");
        aboutpr.setOnAction(t -> about.showAndWait());
        help.getItems().addAll(aboutpr);
        //end help menu option

        bar.getMenus().addAll(file, edit, help);
        bar.prefWidthProperty().bind(stage.widthProperty());
        //end menu bar

        //begin list view
        ListView<Class> listView = new ListView<>(classes);
        listView.prefWidthProperty().bind(stage.widthProperty().multiply(0.35));
        listView.prefHeightProperty().bind(stage.heightProperty());
        //end list view

        //begin list operation controls
        VBox innerData = new VBox();

        //begin name field
        Text name = new Text(0, 0, "Class name:");
        TextField nameField = new TextField();
        //end name field

        //begin numerical fields
        HBox fields = new HBox();

        //begin current grade field
        VBox cg = new VBox();
        Text curGr = new Text(0, 0, "Current grade:");
        TextField curGrField = new TextField();
        cg.getChildren().addAll(curGr, curGrField);
        VBox.setMargin(curGr, new Insets(0,2,4,4));
        VBox.setMargin(curGrField, new Insets(0,2,16,4));
        cg.prefWidthProperty().bind(innerData.widthProperty().multiply(0.5));
        //end current grade field

        //begin final weight field
        VBox fp = new VBox();
        Text finPr = new Text(0, 0, "Weight of final (%):");
        TextField finPrField = new TextField();
        fp.getChildren().addAll(finPr, finPrField);
        VBox.setMargin(finPr, new Insets(0,4,4,2));
        VBox.setMargin(finPrField, new Insets(0,4,16,2));
        fp.prefWidthProperty().bind(innerData.widthProperty().multiply(0.5));
        fields.getChildren().addAll(cg, fp);
        //end final weight field
        //end numerical fields

        //begin button fields
        HBox modifiers = new HBox();

        //begin add button
        Button addcl = new Button();
        addcl.setText("Add");
        addcl.prefWidthProperty().bind(modifiers.widthProperty().multiply(1.0/3.0));
        addcl.disableProperty().bind(Bindings.isEmpty(nameField.textProperty()));
        addcl.disableProperty().bind(Bindings.isEmpty(curGrField.textProperty()));
        addcl.disableProperty().bind(Bindings.isEmpty(finPrField.textProperty()));
        addcl.setOnAction(el -> {
            try {
                Class newClass = new Class(nameField.getText(), Double.parseDouble(curGrField.getText()), Double.parseDouble(finPrField.getText()));
                if (classes.contains(newClass)) {
                    Optional<ButtonType> result = duplicateEntry.showAndWait();
                    if (result.get() != DEyes){
                        return;
                    }
                }
                classes.add(newClass);
                noneSelected.set(true);
                nameField.clear();
                curGrField.clear();
                finPrField.clear();
            } catch(Exception e) {
                inputError.showAndWait();
            }
        });
        //end add button

        //begin update button
        Button updatecl = new Button();
        updatecl.setText("Update");
        updatecl.prefWidthProperty().bind(modifiers.widthProperty().multiply(1.0/3.0));
        updatecl.disableProperty().bind(noneSelected);
        updatecl.setOnAction(el -> {
            try {
                Class newClass = new Class(nameField.getText(), Double.parseDouble(curGrField.getText()), Double.parseDouble(finPrField.getText()));
                classes.add(curIndex, newClass);
                classes.remove(curIndex + 1);
                noneSelected.set(true);
            } catch(Exception e) {
                inputError.showAndWait();
            }
        });
        //end update button

        //begin remove button
        Button removecl = new Button();
        removecl.setText("Remove");
        removecl.prefWidthProperty().bind(modifiers.widthProperty().multiply(1.0/3.0));
        removecl.disableProperty().bind(noneSelected);
        removecl.setOnAction(el -> {
           classes.remove(listView.getSelectionModel().getSelectedIndex());
           if (curIndex != 0) {
               curIndex--;
           }
           if (classes.size() == 0) {
               noneSelected.set(true);
           }
        });
        //end remove button

        modifiers.getChildren().addAll(addcl, updatecl, removecl);
        HBox.setMargin(addcl, new Insets(0, 8, 2, 8));
        HBox.setMargin(updatecl, new Insets(0, 8, 2, 8));
        HBox.setMargin(removecl, new Insets(0, 8, 2, 8));
        //end button fields

        innerData.getChildren().addAll(name, nameField, fields, modifiers);
        VBox.setMargin(name, new Insets(12,4,4,4));
        VBox.setMargin(nameField, new Insets(0,4,12,4));
        VBox.setMargin(modifiers, new Insets(0,4,16,4));
        innerData.prefWidthProperty().bind(stage.widthProperty().multiply(0.65));
        //end list operation controls

        //begin list selection specifications
        listView.getSelectionModel().selectedItemProperty().addListener(cl -> {
            if (classes.size() != 0) {
                Class cur = listView.getSelectionModel().getSelectedItem();
                curIndex = listView.getSelectionModel().getSelectedIndex();
                noneSelected.set(false);
                nameField.setText(cur.name);
                curGrField.setText(String.format("%.2f", cur.currentAvg));
                finPrField.setText(String.format("%.2f", 100 * cur.finalWeight));
            }
        });
        //end list selection specifications

        HBox hbox = new HBox();
        hbox.getChildren().addAll(listView, innerData);

        VBox outer = new VBox();
        outer.getChildren().addAll(bar, hbox);
        outer.prefHeightProperty().bind(stage.heightProperty());

        root.getChildren().add(outer);
        stage.setScene(scene);
        stage.setTitle("Finals Calculator");
        stage.show();
        //end main scene
    }

    public String CSVOutput() {
        String out = ",Current Grade,Best Possible Final Grade,For A,For B,For C,For D,For F\n";
        for (Class c : classes) {
            out += c.name + ",";
            out += String.format("%.2f,", c.currentAvg);
            double max = (c.currentAvg * (1 - c.finalWeight)) + (100 * c.finalWeight);
            out += String.format("%.2f,", max);
            boolean firstLower = true;
            for (int i = 0; i < 5; i++) {
                double lowerLimit = 90 - (i * 10);
                double reqdScore = (lowerLimit - (c.currentAvg * (1 - c.finalWeight))) / c.finalWeight;
                if (reqdScore > 100.0 || reqdScore < 0) {
                    if (firstLower) {
                        out += "0";
                        firstLower = false;
                    } else {
                        out += "n/a";
                    }
                } else {
                    out += String.format("%.2f", reqdScore);
                }
                if (i != 4) {
                    out += ",";
                }
            }
            out += "\n";
        }
        return out;
    }
}
