package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Controller {

    @FXML
    private TextField nameField, emailField, phoneField, locationField, deliveryTimeField, patientNameField, descriptionField;
    @FXML
    private TextField medicineField, quantityField;
    @FXML
    private TextField cardNumberField, cardNameField, expiryDateField, cvvField;
    @FXML
    private ChoiceBox<String> paymentMethodChoiceBox;
    @FXML
    private TextArea cartArea, orderSummaryArea;

    private Cart cart = new Cart();

    @FXML
    private void addToCart() {
        String medicine = medicineField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        if (!medicine.isEmpty() && quantity > 0) {
            cart.addItem(medicine, quantity);
            updateCartArea();
        }
    }

    @FXML
    private void checkout() {
        // Show the checkout form (if using a new window/pane)
    }

    @FXML
    private void completeOrder() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String location = locationField.getText();
        String deliveryTime = deliveryTimeField.getText();
        String patientName = patientNameField.getText();
        String description = descriptionField.getText();
        String paymentMethod = paymentMethodChoiceBox.getValue();
        String cardNumber = cardNumberField.getText();
        String cardName = cardNameField.getText();
        String expiryDate = expiryDateField.getText();
        String cvv = cvvField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || location.isEmpty() ||
                deliveryTime.isEmpty() || patientName.isEmpty() || description.isEmpty() ||
                paymentMethod.isEmpty() || cardNumber.isEmpty() || cardName.isEmpty() ||
                expiryDate.isEmpty() || cvv.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all the details.");
            return;
        }

        // Save order to the database
        try {
            saveOrderToDatabase(name, email, phone, location, deliveryTime, patientName, description,
                    paymentMethod, cardNumber, cardName, expiryDate, cvv);
            showAlert(Alert.AlertType.INFORMATION, "Order Success", "Order completed successfully!");
            updateOrderSummary();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save order: " + e.getMessage());
        }
    }

    private void updateCartArea() {
        cartArea.clear();
        cart.getItems().forEach(item -> cartArea.appendText(item + "\n"));
    }

    private void updateOrderSummary() {
        orderSummaryArea.clear();
        orderSummaryArea.appendText("Order Summary\n");
        orderSummaryArea.appendText("Name: " + nameField.getText() + "\n");
        orderSummaryArea.appendText("Email: " + emailField.getText() + "\n");
        orderSummaryArea.appendText("Phone: " + phoneField.getText() + "\n");
        orderSummaryArea.appendText("Location: " + locationField.getText() + "\n");
        orderSummaryArea.appendText("Delivery Time: " + deliveryTimeField.getText() + "\n");
        orderSummaryArea.appendText("Patient Name: " + patientNameField.getText() + "\n");
        orderSummaryArea.appendText("Description: " + descriptionField.getText() + "\n");
        orderSummaryArea.appendText("Medicines Ordered:\n");
        cart.getItems().forEach(item -> orderSummaryArea.appendText(item + "\n"));
        orderSummaryArea.appendText("Payment Method: " + paymentMethodChoiceBox.getValue() + "\n");
        orderSummaryArea.appendText("Payment Successful!\n");
    }

    private void saveOrderToDatabase(String name, String email, String phone, String location, String deliveryTime,
                                     String patientName, String description, String paymentMethod,
                                     String cardNumber, String cardName, String expiryDate, String cvv) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/pharmacy_orders";
        String user = "your_username";
        String password = "your_password";

        Connection conn = DriverManager.getConnection(url, user, password);
        try {
            // Save order details
            String orderQuery = "INSERT INTO orders (name, email, phone, location, delivery_time, patient_name, description, " +
                    "payment_method, card_number, card_name, expiry_date, cvv) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement orderStmt = conn.prepareStatement(orderQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            orderStmt.setString(1, name);
            orderStmt.setString(2, email);
            orderStmt.setString(3, phone);
            orderStmt.setString(4, location);
            orderStmt.setString(5, deliveryTime);
            orderStmt.setString(6, patientName);
            orderStmt.setString(7, description);
            orderStmt.setString(8, paymentMethod);
            orderStmt.setString(9, cardNumber);
            orderStmt.setString(10, cardName);
            orderStmt.setString(11, expiryDate);
            orderStmt.setString(12, cvv);
            orderStmt.executeUpdate();

            // Get generated order ID
            int orderId = 0;
            var rs = orderStmt.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            // Save cart items
            String cartQuery = "INSERT INTO cart_items (order_id, medicine, quantity) VALUES (?, ?, ?)";
            PreparedStatement cartStmt = conn.prepareStatement(cartQuery);
            for (CartItem item : cart.getItems()) {
                cartStmt.setInt(1, orderId);
                cartStmt.setString(2, item.getMedicine());
                cartStmt.setInt(3, item.getQuantity());
                cartStmt.executeUpdate();
            }
        } finally {
            conn.close();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
