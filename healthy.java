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

public class HealthCheckApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Health Check Form");

        // Create form fields
        TextField nameField = new TextField();
        TextField ageField = new TextField();
        TextField heightField = new TextField();
        TextField bloodPressureField = new TextField();
        TextField temperatureField = new TextField();
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        // Create labels
        Label nameLabel = new Label("Name:");
        Label ageLabel = new Label("Age:");
        Label heightLabel = new Label("Height (cm):");
        Label bloodPressureLabel = new Label("Blood Pressure:");
        Label temperatureLabel = new Label("Temperature (°C):");

        // Create submit button
        Button submitButton = new Button("Check Health");
        submitButton.setOnAction(event -> {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            int height = Integer.parseInt(heightField.getText());
            int bloodPressure = Integer.parseInt(bloodPressureField.getText());
            double temperature = Double.parseDouble(temperatureField.getText());

            String advice = generateHealthAdvice(bloodPressure, temperature, height);
            resultArea.setText("Health Advice for " + name + " (" + age + " years old):\n" + advice);

            saveToDatabase(name, age, height, bloodPressure, temperature, advice);
        });

        // Create grid pane and add form elements
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(ageLabel, 0, 1);
        gridPane.add(ageField, 1, 1);
        gridPane.add(heightLabel, 0, 2);
        gridPane.add(heightField, 1, 2);
        gridPane.add(bloodPressureLabel, 0, 3);
        gridPane.add(bloodPressureField, 1, 3);
        gridPane.add(temperatureLabel, 0, 4);
        gridPane.add(temperatureField, 1, 4);
        gridPane.add(submitButton, 1, 5);
        gridPane.add(resultArea, 1, 6);

        // Create scene and add grid pane to it
        Scene scene = new Scene(gridPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String generateHealthAdvice(int bloodPressure, double temperature, int height) {
        StringBuilder advice = new StringBuilder();

        // Blood Pressure
        if (bloodPressure < 90) {
            advice.append("Your blood pressure is low. Consider increasing your salt intake and drinking more water. ");
        } else if (bloodPressure > 140) {
            advice.append("Your blood pressure is high. Avoid salty foods and manage stress. ");
        } else {
            advice.append("Your blood pressure is normal. Keep up the good work! ");
        }

        // Temperature
        if (temperature < 36) {
            advice.append("Your temperature is below normal. Keep yourself warm and stay hydrated. ");
        } else if (temperature > 37.5) {
            advice.append("You have a fever. Rest and drink plenty of fluids. Consider seeing a doctor if it persists. ");
        } else {
            advice.append("Your temperature is normal. ");
        }

        // Height
        if (height < 150) {
            advice.append("Consider a balanced diet rich in vitamins and minerals to support your growth. ");
        } else if (height > 190) {
            advice.append("Ensure you maintain good posture and engage in regular exercise to support your height. ");
        } else {
            advice.append("Your height is within the normal range. Maintain a healthy lifestyle to support your growth. ");
        }

        return advice.toString();
    }

    private void saveToDatabase(String name, int age, int height, int bloodPressure, double temperature, String advice) {
        String url = "jdbc:mysql://localhost:3306/HealthDB"; // Replace with your database URL
        String user = "yourusername"; // Replace with your database username
        String password = "yourpassword"; // Replace with your database password

        String sql = "INSERT INTO health_data (name, age, height, bloodPressure, temperature, advice) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setInt(3, height);
            pstmt.setInt(4, bloodPressure);
            pstmt.setDouble(5, temperature);
            pstmt.setString(6, advice);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
