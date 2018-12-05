package jmwurst.FinalsCalculator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
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
file saving alert
duplicate entry alert
file output specifications
main scene
    menu bar
        file menu option
        edit menu option
        help menu option
    editor tab
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
                clear button
        list selection specification
    data display tab
 */

public class FinalsCalculator extends Application {

    private ObservableList<Class> classes = FXCollections.observableArrayList();
    private ListView<Class> listView = new ListView<>(classes);
    private BooleanProperty noneSelected = new SimpleBooleanProperty(true);
    private TextField nameField;
    private TextField curGrField;
    private TextField finPrField;
    private int curIndex = -1;
    private BooleanBinding notUpdated = Bindings.createBooleanBinding(() ->
               classes.size() != 0 && curIndex != -1 && !noneSelected.get()
                       && classes.get(curIndex)
                            .equals(new Class(nameField.getText(),
                                    Double.parseDouble(curGrField.getText()),
                                    Double.parseDouble(finPrField.getText()))),

                classes);

    public void start(Stage stage) {

        //begin about alert
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("Finals Calculator - About");
        about.setHeaderText("About This Program");
        about.setContentText("Justin Wurst\n2018");
        //end about alert

        //begin input error alert
        Alert inputError = new Alert(Alert.AlertType.ERROR);
        inputError.setTitle("Finals Calculator - Error");
        inputError.setHeaderText("Error - Invalid Data Entry");
        inputError.setContentText("One or more of the numerical values entered are not valid input.");
        //end input error alert

        //begin file saving alert
        Alert savingError = new Alert(Alert.AlertType.ERROR);
        savingError.setTitle("Finals Calculator - Error");
        savingError.setHeaderText("Error - File Saving");
        savingError.setContentText("The file was unable to be saved.");
        //end file saving alert

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
        Group root = new Group();
        Scene scene = new Scene(root, 560, 400);
        BorderPane borderPane = new BorderPane();
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.tabMinWidthProperty().bind(stage.widthProperty().multiply(0.3));
        TableView<Class> table = new TableView<>();
        table.prefWidthProperty().bind(stage.widthProperty());

        //begin menu bar
        MenuBar bar = new MenuBar();

        //begin file menu option
        Menu file = new Menu("File");
        MenuItem output = new MenuItem("Save as CSV...");
        output.setOnAction(t -> {
            String out = CSVOutput();
            File csvOut = fileChooser.showSaveDialog(stage);
            try {
                FileWriter fw = new FileWriter(csvOut);
                fw.write(out);
                fw.close();
            } catch (Exception e) {
                savingError.showAndWait();
            }
        });
        MenuItem quit = new MenuItem("Quit...");
        quit.setOnAction(t -> Platform.exit());
        file.getItems().addAll(output, quit);
        //end file menu option

        //begin edit menu option
        Menu edit = new Menu("Edit");
        MenuItem clear = new MenuItem("Clear list...");
        clear.setOnAction(t -> {
            classes.clear();
            noneSelected.setValue(true);
        });
        edit.getItems().addAll(clear);
        //end edit menu option

        //begin help menu option
        Menu help = new Menu("Help");
        MenuItem aboutpr = new MenuItem("About this program...");
        aboutpr.setOnAction(t -> about.showAndWait());
        help.getItems().addAll(aboutpr);
        //end help menu option

        bar.getMenus().addAll(file, edit, help);
        bar.prefWidthProperty().bind(stage.widthProperty());
        bar.prefWidth(50);
        //end menu bar

        //begin editor tab
        Tab editor = new Tab();
        editor.setText("Editor");

        //begin list view
        listView.prefWidthProperty().bind(stage.widthProperty().multiply(0.35));
        listView.prefHeightProperty().bind(stage.heightProperty());
        //end list view

        //begin list operation controls
        VBox innerData = new VBox();

        //begin name field
        Text name = new Text(0, 0, "Class name:");
        nameField = new TextField();
        //end name field

        //begin numerical fields
        HBox fields = new HBox();

        //begin current grade field
        VBox cg = new VBox();
        Text curGr = new Text(0, 0, "Current grade:");
        curGrField = new TextField();
        cg.getChildren().addAll(curGr, curGrField);
        VBox.setMargin(curGr, new Insets(0,2,4,4));
        VBox.setMargin(curGrField, new Insets(0,2,16,4));
        cg.prefWidthProperty().bind(innerData.widthProperty().multiply(0.5));
        //end current grade field

        //begin final weight field
        VBox fp = new VBox();
        Text finPr = new Text(0, 0, "Weight of final (%):");
        finPrField = new TextField();
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
        addcl.prefWidthProperty().bind(modifiers.widthProperty().multiply(0.25));
        addcl.disableProperty().bind(Bindings.or(Bindings.isEmpty(nameField.textProperty()),
                Bindings.or(Bindings.isEmpty(curGrField.textProperty()),
                        Bindings.isEmpty(finPrField.textProperty()))));
        addcl.setOnAction(el -> {
            try {
                Class newClass = new Class(nameField.getText(), Double.parseDouble(curGrField.getText()), Double.parseDouble(finPrField.getText()));
                if (classes.contains(newClass)) {
                    Optional<ButtonType> result = duplicateEntry.showAndWait();
                    if (!result.isPresent() || result.get() != DEyes){
                        return;
                    }
                }
                classes.add(newClass);
                nameField.clear();
                curGrField.clear();
                finPrField.clear();
            } catch(Exception e) {
                inputError.showAndWait();
            } finally {
                noneSelected.set(true);
            }
        });
        //end add button

        //begin update button
        Button updatecl = new Button();
        updatecl.setText("Update");
        updatecl.prefWidthProperty().bind(modifiers.widthProperty().multiply(0.25));
        updatecl.disableProperty().bind(Bindings.or(Bindings.isEmpty(nameField.textProperty()),
                Bindings.or(Bindings.isEmpty(curGrField.textProperty()),
                        Bindings.or(Bindings.isEmpty(finPrField.textProperty()),
                                Bindings.or(Bindings.isEmpty(classes),
                                        Bindings.or(noneSelected,
                                                notUpdated))))));
        updatecl.setOnAction(el -> {
            try {
                Class newClass = new Class(nameField.getText(), Double.parseDouble(curGrField.getText()), Double.parseDouble(finPrField.getText()));
                classes.remove(curIndex);
                if (classes.size() == 0) {
                    classes.add(newClass);
                } else {
                    classes.add(curIndex, newClass);
                }
                noneSelected.set(true);
            } catch(Exception e) {
                inputError.showAndWait();
            }
        });
        //end update button

        //begin remove button
        Button removecl = new Button();
        removecl.setText("Remove");
        removecl.prefWidthProperty().bind(modifiers.widthProperty().multiply(0.25));
        removecl.disableProperty().bind(noneSelected);
        removecl.setOnAction(el -> {
           classes.remove(curIndex);
           noneSelected.set(true);
        });
        //end remove button

        //begin clear button
        Button clearcl = new Button();
        clearcl.setText("Clear");
        clearcl.prefWidthProperty().bind(modifiers.widthProperty().multiply(0.25));
        clearcl.disableProperty().bind(Bindings.and(Bindings.isEmpty(nameField.textProperty()),
                Bindings.and(Bindings.isEmpty(curGrField.textProperty()),
                        Bindings.isEmpty(finPrField.textProperty()))));
        clearcl.setOnAction(el -> {
            nameField.clear();
            curGrField.clear();
            finPrField.clear();
        });
        //end clear button

        modifiers.getChildren().addAll(addcl, updatecl, removecl, clearcl);
        HBox.setMargin(addcl, new Insets(0, 8, 2, 8));
        HBox.setMargin(updatecl, new Insets(0, 8, 2, 8));
        HBox.setMargin(removecl, new Insets(0, 8, 2, 8));
        HBox.setMargin(clearcl, new Insets(0, 8, 2, 8));
        //end button fields

        innerData.getChildren().addAll(name, nameField, fields, modifiers);
        VBox.setMargin(name, new Insets(12,4,4,4));
        VBox.setMargin(nameField, new Insets(0,4,12,4));
        VBox.setMargin(modifiers, new Insets(0,4,16,4));
        innerData.prefWidthProperty().bind(stage.widthProperty().multiply(0.65));
        //end list operation controls

        //begin list selection specifications
        listView.setOnMouseClicked(eh -> {
            curIndex = listView.getSelectionModel().getSelectedIndex();
            if (classes.size() != 0 && curIndex < classes.size()) {
                noneSelected.set(false);
                Class cur = classes.get(curIndex);
                nameField.setText(cur.getName());
                curGrField.setText(String.format("%.2f", cur.getCurrentAvg()));
                finPrField.setText(String.format("%.2f", 100 * cur.getFinalWeight()));
            } else {
                noneSelected.set(true);
            }
        });
        //end list selection specifications

        HBox hbox = new HBox();
        hbox.getChildren().addAll(listView, innerData);

        editor.setContent(hbox);
        //end editor tab

        //begin data display tab
        Tab dataDisp = new Tab();
        dataDisp.setText("Data Display");

        TableColumn nameCol = new TableColumn("Name");
        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(1.0/6.0));
        nameCol.setCellValueFactory(new PropertyValueFactory<Class, String>("name"));

        TableColumn currentAvgCol = new TableColumn("Current Average");
        currentAvgCol.prefWidthProperty().bind(table.widthProperty().multiply(1.0/6.0));
        currentAvgCol.setCellValueFactory(new PropertyValueFactory<Class, Double>("currentAvg"));

        TableColumn maxCol = new TableColumn("Highest Possible");
        maxCol.prefWidthProperty().bind(table.widthProperty().multiply(1.0/6.0));
        maxCol.setCellValueFactory(new PropertyValueFactory<Class, Double>("max"));

        TableColumn finalsCols = new TableColumn("Final exam scores needed to achieve:");

        TableColumn aCol = new TableColumn("A");
        aCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        aCol.setCellValueFactory(new PropertyValueFactory<Class, Double>("needA"));

        TableColumn bCol = new TableColumn("B");
        bCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        bCol.setCellValueFactory(new PropertyValueFactory<Class, Double>("needB"));

        TableColumn cCol = new TableColumn("C");
        cCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        cCol.setCellValueFactory(new PropertyValueFactory<Class, Double>("needC"));

        TableColumn dCol = new TableColumn("D");
        dCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        dCol.setCellValueFactory(new PropertyValueFactory<Class, Double>("needD"));

        TableColumn fCol = new TableColumn("F");
        fCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        fCol.setCellValueFactory(new PropertyValueFactory<Class, Double>("needF"));

        finalsCols.getColumns().addAll(aCol, bCol, cCol, dCol, fCol);

        table.setItems(classes);
        table.getColumns().addAll(nameCol, currentAvgCol, maxCol, finalsCols);
        dataDisp.setContent(table);
        //end data display tab

        tabPane.getTabs().addAll(editor, dataDisp);
        borderPane.setCenter(tabPane);
        VBox all = new VBox();
        all.getChildren().addAll(bar, borderPane);
        root.getChildren().addAll(all);
        stage.setScene(scene);
        stage.setTitle("Finals Calculator");
        stage.setWidth(675);
        stage.setHeight(350);
        stage.show();
        //end main scene
    }

    private String CSVOutput() {
        StringBuilder out = new StringBuilder();
        out.append(",Current Grade,Best Possible Final Grade,For A,For B,For C,For D,For F\n");
        for (Class c : classes) {
            out.append(c.getName()).append(",");
            out.append(c.getCurrentAvg());
            out.append(c.getMax());
            boolean firstLower = true;
            for (int i = 0; i < 5; i++) {
                double lowerLimit = 90 - (i * 10);
                double reqdScore = (lowerLimit - (c.getCurrentAvg() * (1 - c.getFinalWeight())))
                                    / c.getFinalWeight();
                if (reqdScore > 100.0 || reqdScore < 0) {
                    if (firstLower) {
                        out.append("0");
                        firstLower = false;
                    } else {
                        out.append("n/a");
                    }
                } else {
                    out.append(String.format("%.2f", reqdScore));
                }
                if (i != 4) {
                    out.append(",");
                }
            }
            out.append("\n");
        }
        return out.toString();
    }
}
