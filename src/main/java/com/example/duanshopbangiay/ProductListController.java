package com.example.duanshopbangiay;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.Optional;

public class ProductListController {

    private Stage stage;
    private Scene scene;

    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, Void> actionsColumn;

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;

    private ObservableList<Product> productList;

    @FXML
    public void initialize() {
        productList = FXCollections.observableArrayList();
        loadProductsFromFile(); // Tải sản phẩm từ file khi khởi động

        // Kiểm tra nếu không có sản phẩm nào trong danh sách, thêm sản phẩm mặc định
        if (productList.isEmpty()) {
            addDefaultProducts();
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        actionsColumn.setCellFactory(getActionsCellFactory());
        productTableView.setItems(productList);
    }

    private void addDefaultProducts() {
        // Thêm sản phẩm mặc định vào danh sách
        productList.add(new Product(1, "Gucci", 500000, 10));
        productList.add(new Product(2, "Nike", 1000000, 5));
        productList.add(new Product(3, "Dior", 300000, 8));
        productList.add(new Product(4, "Adidas", 800000, 6));
        productList.add(new Product(5, "Converse", 90000000, 9));
    }

    private Callback<TableColumn<Product, Void>, TableCell<Product, Void>> getActionsCellFactory() {
        return col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button removeButton = new Button("Remove");
            private final HBox actionButtons = new HBox(editButton, removeButton);

            {
                editButton.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    editProduct(product);
                });

                removeButton.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    removeProduct(product);
                });

                actionButtons.setSpacing(10);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButtons);
                }
            }
        };
    }

    private void editProduct(Product product) {
        showEditProductDialog(product);
    }

    private void removeProduct(Product product) {
        showConfirmationDialog(product);
    }

    public void switchToDisplayLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("DisplayLogin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToOrderList(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("OrderList.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("OrderList");
        stage.setScene(scene);
        stage.show();
    }

    private void showEditProductDialog(Product product) {
        while (true) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Product");

            TextField idField = new TextField(String.valueOf(product.getId()));
            TextField nameField = new TextField(product.getName());
            TextField priceField = new TextField(String.valueOf(product.getPrice()));
            TextField quantityField = new TextField(String.valueOf(product.getQuantity()));

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.add(new Label("Product ID:"), 0, 0);
            grid.add(idField, 1, 0);
            grid.add(new Label("Product Name:"), 0, 1);
            grid.add(nameField, 1, 1);
            grid.add(new Label("Product Price:"), 0, 2);
            grid.add(priceField, 1, 2);
            grid.add(new Label("Product Quantity:"), 0, 3);
            grid.add(quantityField, 1, 3);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                break;
            }

            String idText = idField.getText();
            String name = nameField.getText();
            String priceText = priceField.getText();
            String quantityText = quantityField.getText();

            if (idText.isEmpty() || name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                showError("Invalid information", "Please fill in complete information for product ID, name, price, and quantity.");
                continue;
            }

            try {
                int newId = Integer.parseInt(idText);
                double newPrice = Double.parseDouble(priceText);
                int newQuantity = Integer.parseInt(quantityText);

                boolean isDuplicate = false;
                for (Product p : productList) {
                    if (p.getId() == newId && p != product) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (isDuplicate) {
                    showError("Duplicate ID", "Product ID already exists. Please enter a different ID.");
                    continue;
                }

                product.setId(newId);
                product.setName(name);
                product.setPrice(newPrice);
                product.setQuantity(newQuantity);
                productTableView.refresh();
                break;
            } catch (NumberFormatException e) {
                showError("Invalid information", "Please enter valid numbers for product ID, price, and quantity.");
            }
        }
    }

    private void showConfirmationDialog(Product product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm product deletion ?");
        alert.setHeaderText("Are you sure you want to delete the product " + product.getName() + " ?");
        alert.setContentText("Click Ok to confirm, or Cancel to cancel.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                productList.remove(product);
                saveProductsToFile(); // Lưu lại sản phẩm sau khi xóa
            }
        });
    }

    @FXML
    private void addProduct() {
        String idText = idField.getText();
        String name = nameField.getText();
        String priceText = priceField.getText();
        String quantityText = quantityField.getText();

        if (idText.isEmpty() || name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            showError("Error", "Please fill in all fields for product ID, name, price, and quantity.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            for (Product product : productList) {
                if (product.getId() == id) {
                    showError("Duplicate ID", "Product ID already exists. Please enter a different ID.");
                    return;
                }
            }

            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);
            Product newProduct = new Product(id, name, price, quantity);
            productList.add(newProduct);

            // Ghi sản phẩm mới vào file
            saveProductsToFile();

            nameField.clear();
            priceField.clear();
            quantityField.clear();
            idField.clear();
        } catch (NumberFormatException e) {
            if (!idText.matches("\\d+")) {
                showError("Invalid ID", "Please enter a valid numeric ID.");
            } else if (!priceText.matches("\\d+(\\.\\d+)?")) {
                showError("Invalid Price", "Please enter a valid numeric price.");
            } else if (!quantityText.matches("\\d+")) {
                showError("Invalid Quantity", "Please enter a valid numeric quantity.");
            } else {
                showError("Error", "Please enter valid numbers for product ID, price, and quantity.");
            }
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadProductsFromFile() {
        File file = new File("products.txt");
        if (!file.exists()) {
            return; // Không có file thì không làm gì
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int quantity = Integer.parseInt(parts[3]);
                    productList.add(new Product(id, name, price, quantity));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveProductsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("products.txt"))) {
            for (Product product : productList) {
                String line = product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getQuantity();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
