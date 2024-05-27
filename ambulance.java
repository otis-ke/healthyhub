import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AmbulanceRequestForm extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ambulance Request Form");

        // Create the form fields
        TextField nameField = new TextField();
        TextField phoneField = new TextField();
        TextField locationField = new TextField();
        TextField specificLocationField = new TextField();
        TextField ambulanceTypeField = new TextField();
        TextArea incidentDescriptionArea = new TextArea();
        TextArea patientConditionArea = new TextArea();

        // Create labels
        Label nameLabel = new Label("Name:");
        Label phoneLabel = new Label("Phone:");
        Label locationLabel = new Label("Location:");
        Label specificLocationLabel = new Label("Specific Location:");
        Label ambulanceTypeLabel = new Label("Ambulance Type:");
        Label incidentDescriptionLabel = new Label("Incident Description:");
        Label patientConditionLabel = new Label("Patient Condition:");

        // Create the submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String location = locationField.getText();
            String specificLocation = specificLocationField.getText();
            String ambulanceType = ambulanceTypeField.getText();
            String incidentDescription = incidentDescriptionArea.getText();
            String patientCondition = patientConditionArea.getText();

            if (name.isEmpty() || phone.isEmpty() || location.isEmpty() || specificLocation.isEmpty() ||
                    ambulanceType.isEmpty() || incidentDescription.isEmpty() || patientCondition.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all the details.");
            } else {
                if (insertDataToDatabase(name, phone, location, specificLocation, ambulanceType, incidentDescription, patientCondition)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Form submitted successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while submitting the form. Please try again.");
                }
            }
        });

        // Create a grid pane and add the form elements
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(phoneLabel, 0, 1);
        gridPane.add(phoneField, 1, 1);
        gridPane.add(locationLabel, 0, 2);
        gridPane.add(locationField, 1, 2);
        gridPane.add(specificLocationLabel, 0, 3);
        gridPane.add(specificLocationField, 1, 3);
        gridPane.add(ambulanceTypeLabel, 0, 4);
        gridPane.add(ambulanceTypeField, 1, 4);
        gridPane.add(incidentDescriptionLabel, 0, 5);
        gridPane.add(incidentDescriptionArea, 1, 5);
        gridPane.add(patientConditionLabel, 0, 6);
        gridPane.add(patientConditionArea, 1, 6);
        gridPane.add(submitButton, 1, 7);

        // Create a scene and add the grid pane to it
        Scene scene = new Scene(gridPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean insertDataToDatabase(String name, String phone, String location, String specificLocation, String ambulanceType, String incidentDescription, String patientCondition) {
        String url = "jdbc:mysql://localhost:3306/AmbulanceRequestsDB"; // Replace with your database URL
        String user = "yourusername"; // Replace with your database username
        String password = "yourpassword"; // Replace with your database password

        String sql = "INSERT INTO ambulance_requests (name, phone, location, specificLocation, ambulanceType, incidentDescription, patientCondition) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, location);
            pstmt.setString(4, specificLocation);
            pstmt.setString(5, ambulanceType);
            pstmt.setString(6, incidentDescription);
            pstmt.setString(7, patientCondition);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
