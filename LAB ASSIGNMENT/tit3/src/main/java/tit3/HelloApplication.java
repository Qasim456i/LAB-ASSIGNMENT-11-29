package tit3;

import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Label lb = new Label("Full name :");
        TextField tf = new TextField();
        tf.setPrefWidth(150);
        HBox h1 = new HBox( lb, tf);
        h1.setPadding(new Insets(5));
      
        

        Label lb2 = new Label("Your ID    :");
        TextField tf2 = new TextField();
        tf2.setPrefWidth(150);
        HBox h2 = new HBox( lb2, tf2);
        h2.setPadding(new Insets(5));

        TextArea resultArea = new TextArea();
        resultArea.setPrefHeight(100);  // Set height of the TextArea
        resultArea.setEditable(false);  // Make it non-editable
        resultArea.setWrapText(true); 



        Label genderLabel = new Label("Gender:");
        RadioButton rbMale = new RadioButton("Male");
        RadioButton rbFemale = new RadioButton("Female");
        RadioButton rbOther = new RadioButton("Other");

        // Grouping radio buttons
        ToggleGroup genderGroup = new ToggleGroup();
        rbMale.setToggleGroup(genderGroup);
        rbFemale.setToggleGroup(genderGroup);
        rbOther.setToggleGroup(genderGroup);
        VBox h3 = new VBox(10, genderLabel, rbMale, rbFemale, rbOther); 
        h3.setPadding(new Insets(5));
       
        Label provinceLabel = new Label("Home Province:");
        ComboBox<String> cityDropdown = new ComboBox<>();
        cityDropdown.getItems().addAll(
                 "Shakargarh",
                "Lahore", 
                "Karachi", 
                "Islamabad", 
                "Quetta", 
                "Peshawar"
        );
        cityDropdown.setPromptText("Select your city");
        HBox h4 = new HBox(10, provinceLabel, cityDropdown);
        h4.setPadding(new Insets(5));

        Label dateLabel = new Label("Date of Birth:");
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());  
        HBox h5 = new HBox(10, dateLabel, datePicker);
        h5.setPadding(new Insets(5));

        Label lb6=new Label("NEW RECORD");
        VBox fp=new VBox(lb6);
        Button b1 = new Button(" NEW ");
        b1.setOnAction(e -> {
            
            String fullName = tf.getText();
            String userId = tf2.getText();
            String gender = null;
        
            
            if (genderGroup.getSelectedToggle() != null) {
                gender = ((RadioButton) genderGroup.getSelectedToggle()).getText();
            } else {
               
                gender = "Not selected"; 
            }
        
            String province = cityDropdown.getValue();
            LocalDate dob = datePicker.getValue();
        
            // Create a record string
            String record = "Full Name: " + fullName + "\n"
                    + "ID: " + userId + "\n"
                    + "Gender: " + gender + "\n"
                    + "Home Province: " + province + "\n"
                    + "Date of Birth: " + dob + "\n\n";
        
            // Write data to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("records.txt", true))) {
                writer.write(record);
                writer.flush();
                System.out.println("Data written to file successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        
            // Reset form fields
            tf.clear();
            tf2.clear();
            genderGroup.selectToggle(null);
            cityDropdown.setValue(null);
            datePicker.setValue(LocalDate.now());
        });
        Button b2 = new Button("DELETE");
        Button b3 = new Button("RESTORE");
        Button b4 = new Button("FIND");

        
        b4.setOnAction(e -> {
            String searchId = tf2.getText();

            if (searchId.isEmpty()) {
                resultArea.setText("Please enter an ID to search.");
                return;
            }

            boolean found = false;
            StringBuilder recordBuilder = new StringBuilder();

           
            try (BufferedReader reader = new BufferedReader(new FileReader("records.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    
                    if (line.contains("ID: " + searchId)) {
                        found = true;
                        recordBuilder.append(line).append("\n");

                        
                        while ((line = reader.readLine()) != null && !line.isEmpty()) {
                            recordBuilder.append(line).append("\n");
                        }

                        break; 
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (found) {
                resultArea.setText("Record Found:\n" + recordBuilder.toString());
            } else {
                resultArea.setText("Record with ID: " + searchId + " not found.");
            }

            // Clear the search field after the search
            tf2.clear();
        });

        Button b5 = new Button("PREVIEW");
        Button b6 = new Button("CLOSE");

        VBox buttons = new VBox(10, b1, b2, b3, b4, b5, b6);


        GridPane gp = new GridPane();
        gp.setPadding(new Insets(10)); // Add padding around the grid
        gp.setVgap(5); // Set vertical spacing between rows
        gp.setHgap(10); // Set horizontal spacing between columns
        gp.setAlignment(Pos.TOP_LEFT);
        gp.add(h1, 0, 0);
        gp.add(h2, 0, 1);
        gp.add(h3, 0, 2);
        gp.add(h4, 0, 3);
        gp.add(h5, 0, 4);
        gp.add(fp, 2,0,2,4);
        gp.add(buttons, 3, 1, 3, 5);
        gp.add(resultArea, 0, 5, 4, 1);

        Scene sc = new Scene(gp, 600, 400);
        stage.setScene(sc);
        stage.setTitle("ENTRY FORM");
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}