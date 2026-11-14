// End of file - Name: [khalil.alabsi] - ID: [1320225638]
package app;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StoreController {

    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private TextField urlField;
    @FXML private ComboBox<String> currencyBox;
    @FXML private ImageView productImage;
    @FXML private DatePicker datePicker;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> codeCol;
    @FXML private TableColumn<Product, String> nameCol;
    @FXML private TableColumn<Product, Double> priceCol;
    @FXML private TableColumn<Product, Integer> quantityCol;
    @FXML private TableColumn<Product, String> currencyCol;
    @FXML private TableColumn<Product, String> dateCol;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    // متغيرات لحفظ بيانات المستخدم
    private String username;
    private boolean isAdmin;
    private int currentUserId;

    @FXML
    public void initialize() {
        // تعبئة ComboBox بالعملات
        currencyBox.setItems(FXCollections.observableArrayList("Dinar", "Dollar", "Euro"));

        // ربط أعمدة الجدول مع خصائص الكلاس Product
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        currencyCol.setCellValueFactory(new PropertyValueFactory<>("currency"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        // عند النقر على صف في الجدول، يظهر التفاصيل
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showProductDetails(newSelection);
            }
        });

        // تحميل المنتجات من قاعدة البيانات
        loadProducts();
    }

    /**
     * عرض تفاصيل المنتج في الحقول عند النقر عليه
     */
    private void showProductDetails(Product product) {
        codeField.setText(product.getCode());
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
        currencyBox.setValue(product.getCurrency());
        datePicker.setValue(java.time.LocalDate.parse(product.getDate()));
        urlField.setText(product.getImageUrl());

        // تحميل الصورة من URL
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            try {
                Image image = new Image(product.getImageUrl(), 100, 100, true, true);
                productImage.setImage(image);
            } catch (Exception e) {
                productImage.setImage(null);
            }
        } else {
            productImage.setImage(null);
        }
    }

    /**
     * إضافة منتج جديد
     */
    @FXML
    void handleAdd(ActionEvent event) {
        if (isInputValid()) {
            try {
                Connection conn = DBConnection.getConnection();

                String sql = "INSERT INTO products (code, name, price, quantity, currency, date, image_url, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, codeField.getText());
                ps.setString(2, nameField.getText());
                ps.setDouble(3, Double.parseDouble(priceField.getText()));
                ps.setInt(4, Integer.parseInt(quantityField.getText()));
                ps.setString(5, currencyBox.getValue());
                ps.setString(6, datePicker.getValue().toString());
                ps.setString(7, urlField.getText());
                ps.setInt(8, currentUserId);

                ps.executeUpdate();
                conn.close();

                showAlert(Alert.AlertType.INFORMATION, "Product added successfully!");
                clearFields();
                loadProducts();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error saving product.");
            }
        }
    }

    /**
     * تعديل المنتج المحدد
     */
    @FXML
    void handleUpdate(ActionEvent event) {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a product to update.");
            return;
        }

        if (isInputValid()) {
            try {
                Connection conn = DBConnection.getConnection();

                String sql = "UPDATE products SET name=?, price=?, quantity=?, currency=?, date=?, image_url=? WHERE code=?";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, nameField.getText());
                ps.setDouble(2, Double.parseDouble(priceField.getText()));
                ps.setInt(3, Integer.parseInt(quantityField.getText()));
                ps.setString(4, currencyBox.getValue());
                ps.setString(5, datePicker.getValue().toString());
                ps.setString(6, urlField.getText());
                ps.setString(7, codeField.getText());

                ps.executeUpdate();
                conn.close();

                selected.setName(nameField.getText());
                selected.setPrice(Double.parseDouble(priceField.getText()));
                selected.setQuantity(Integer.parseInt(quantityField.getText()));
                selected.setCurrency(currencyBox.getValue());
                selected.setDate(datePicker.getValue().toString());
                selected.setImageUrl(urlField.getText());

                productTable.refresh();
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Product updated successfully!");

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error updating product.");
            }
        }
    }

    /**
     * حذف المنتج المحدد
     */
    @FXML
    void handleDelete(ActionEvent event) {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a product to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("You are about to delete: " + selected.getName());
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DBConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE code = ?");
                    ps.setString(1, selected.getCode());
                    ps.executeUpdate();
                    conn.close();
                    productList.remove(selected);
                    clearFields();
                    showAlert(Alert.AlertType.INFORMATION, "Product deleted.");
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error deleting product.");
                }
            }
        });
    }

    /**
     * تسجيل الخروج والعودة لشاشة الدخول
     */
    @FXML
    void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("eStore Login");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Cannot load login screen.");
        }
    }

    /**
     * تحقق من صحة المدخلات
     */
    private boolean isInputValid() {
        StringBuilder errors = new StringBuilder();

        if (codeField.getText().isEmpty()) errors.append("- Code is required.\n");
        if (nameField.getText().isEmpty()) errors.append("- Name is required.\n");
        if (priceField.getText().isEmpty()) errors.append("- Price is required.\n");
        if (quantityField.getText().isEmpty()) errors.append("- Quantity is required.\n");
        if (currencyBox.getValue() == null) errors.append("- Currency is required.\n");
        if (datePicker.getValue() == null) errors.append("- Date is required.\n");

        try {
            Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            errors.append("- Price must be a number.\n");
        }

        try {
            Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            errors.append("- Quantity must be a number.\n");
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, errors.toString());
            return false;
        }

        return true;
    }

    /**
     * مسح الحقول
     */
    private void clearFields() {
        codeField.clear();
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        currencyBox.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        urlField.clear();
        productImage.setImage(null);
    }

    /**
     * عرض رسالة تنبيه
     */
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Store");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * تعيين بيانات المستخدم بعد الدخول
     */
    public void setUser(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
        this.currentUserId = getCurrentUserId(username);
        showWelcomeDialog();
        loadProducts();
    }

    /**
     * رسالة ترحيب بعد الدخول
     */
    private void showWelcomeDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Welcome");
        alert.setHeaderText("Hello, " + username + "!");
        alert.setContentText(
            "You are logged in as " + (isAdmin ? "Admin" : "User") + ".\n" +
            "You can now manage your products."
        );
        alert.showAndWait();
    }

    /**
     * تحميل المنتجات من قاعدة البيانات
     */
    private void loadProducts() {
        productList.clear();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = isAdmin ? 
                "SELECT * FROM products" : 
                "SELECT * FROM products WHERE user_id = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            if (!isAdmin) {
                ps.setInt(1, currentUserId);
            }
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("currency"),
                    rs.getString("date"),
                    rs.getString("image_url")
                );
                productList.add(product);
            }
            
            productTable.setItems(productList);
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * الحصول على ID المستخدم من قاعدة البيانات
     */
    private int getCurrentUserId(String username) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT id FROM users WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
// End of file - Name: [khalil.alabsi] - ID: [1320225638]