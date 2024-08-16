package com.example.duanshopbangiay;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
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
    private TableColumn<Product, String> colorColumn; // Thêm cột cho màu sắc
    @FXML
    private TableColumn<Product, String> imageColumn; // Thêm cột cho hình ảnh
    @FXML
    private TableColumn<Product, Void> actionsColumn;

    private ObservableList<Product> productList;

    @FXML
    private void initialize() {
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
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color")); // Đặt giá trị cho cột màu sắc

        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath")); // Đặt giá trị cho cột hình ảnh

        imageColumn.setCellFactory(col -> new TableCell<Product, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    InputStream imageStream = getClass().getResourceAsStream(imagePath);
                    if (imageStream == null) {
                        System.err.println("Tệp hình ảnh không được tìm thấy: " + imagePath);
                        // Cung cấp hình ảnh thay thế nếu cần
                        imageStream = getClass().getResourceAsStream("/img/placeholder.png"); // Hình ảnh thay thế
                    }
                    if (imageStream != null) {
                        Image image = new Image(imageStream);
                        imageView.setImage(image);
                    }
                    imageView.setFitWidth(50); // Đặt kích thước hình ảnh
                    imageView.setFitHeight(50);
                    setGraphic(imageView);
                }
            }
        });

        actionsColumn.setCellFactory(getActionsCellFactory());
        productTableView.setItems(productList);
    }


    private void addDefaultProducts() {
        productList.add(new Product(1, "Jordan 1 pine", 500, 10, "32CD32", "/img/Green-remove-preview.png"));
        productList.add(new Product(2, "Jordan 1 ocean", 100, 5, "00008B", "/img/MidN-remove-preview.png"));
        productList.add(new Product(3, "Jordan 1 happy", 300, 8, "D2691E", "/img/Orange-remove-preview.png"));
        productList.add(new Product(4, "Jordan 1 rose", 800, 6, "FF6699", "/img/Pink-remove-preview.png"));
        productList.add(new Product(5, "Jordan 1 R&W", 900, 9, "B22222", "/img/Red-remove-preview.png"));
        productList.add(new Product(6, "Jordan 1 SSJ", 600, 9, "FFB605", "/img/YellowOriginal-remove-preview.png"));
    }

    @FXML
    public void addProduct() {
        while (true) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Product");

            TextField idField = new TextField();
            TextField nameField = new TextField();
            TextField priceField = new TextField();
            TextField quantityField = new TextField();
            TextField colorField = new TextField(); // Thêm trường nhập cho màu sắc
            TextField imageField = new TextField(); // Thêm trường nhập cho đường dẫn hình ảnh
            Button uploadImageButton = new Button("Upload Image");

            uploadImageButton.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
                File file = fileChooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
                if (file != null) {
                    imageField.setText(file.getAbsolutePath());
                }
            });

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
            grid.add(new Label("Product Color:"), 0, 4); // Thêm nhãn cho màu sắc
            grid.add(colorField, 1, 4); // Thêm trường nhập cho màu sắc
            grid.add(new Label("Image Path:"), 0, 5);
            grid.add(imageField, 1, 5);
            grid.add(uploadImageButton, 1, 6); // Nút tải ảnh lên

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
            String color = colorField.getText(); // Lấy màu sắc
            String imagePath = imageField.getText(); // Lấy đường dẫn hình ảnh

            if (idText.isEmpty() || name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty() || color.isEmpty() || imagePath.isEmpty()) {
                showError("Invalid information", "Please fill in complete information for product ID, name, price, quantity, color, and image path.");
                continue;
            }

            try {
                int id = Integer.parseInt(idText);
                double price = Double.parseDouble(priceText);
                int quantity = Integer.parseInt(quantityText);

                boolean idExists = productList.stream().anyMatch(p -> p.getId() == id);
                if (idExists) {
                    showError("Duplicate ID", "A product with this ID already exists.");
                    continue;
                }

                Product newProduct = new Product(id, name, price, quantity, color, imagePath);
                productList.add(newProduct);
                saveProductsToFile(); // Lưu sản phẩm vào file
                productTableView.refresh();
                break;
            } catch (NumberFormatException e) {
                showError("Invalid data", "Please enter valid numeric values for price and quantity.");
            }
        }
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
            TextField colorField = new TextField(product.getColor()); // Thêm trường nhập cho màu sắc
            TextField imageField = new TextField(product.getImagePath()); // Thêm trường nhập cho đường dẫn hình ảnh
            Button uploadImageButton = new Button("Upload Image");

            uploadImageButton.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
                File file = fileChooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
                if (file != null) {
                    imageField.setText(file.getAbsolutePath());
                }
            });

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
            grid.add(new Label("Product Color:"), 0, 4); // Thêm nhãn cho màu sắc
            grid.add(colorField, 1, 4); // Thêm trường nhập cho màu sắc
            grid.add(new Label("Image Path:"), 0, 5);
            grid.add(imageField, 1, 5);
            grid.add(uploadImageButton, 1, 6); // Nút tải ảnh lên

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
            String color = colorField.getText(); // Lấy màu sắc
            String imagePath = imageField.getText(); // Lấy đường dẫn hình ảnh

            if (idText.isEmpty() || name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty() || color.isEmpty() || imagePath.isEmpty()) {
                showError("Invalid information", "Please fill in complete information for product ID, name, price, quantity, color, and image path.");
                continue;
            }

            try {
                int id = Integer.parseInt(idText);
                double price = Double.parseDouble(priceText);
                int quantity = Integer.parseInt(quantityText);

                boolean idExists = productList.stream().anyMatch(p -> p.getId() == id && p != product);
                if (idExists) {
                    showError("Duplicate ID", "A product with this ID already exists.");
                    continue;
                }

                product.setId(id);
                product.setName(name);
                product.setPrice(price);
                product.setQuantity(quantity);
                product.setColor(color); // Cập nhật màu sắc
                product.setImagePath(imagePath); // Cập nhật đường dẫn hình ảnh
                saveProductsToFile(); // Lưu sản phẩm vào file
                productTableView.refresh();
                break;
            } catch (NumberFormatException e) {
                showError("Invalid data", "Please enter valid numeric values for price and quantity.");
            }
        }
    }

    private void showConfirmationDialog(Product product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Product");
        alert.setHeaderText("Are you sure you want to remove this product?");
        alert.setContentText("Product Name: " + product.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            productList.remove(product);
            saveProductsToFile(); // Lưu sản phẩm vào file sau khi xóa
            productTableView.refresh();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadProductsFromFile() {
        File file = new File("products.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 6) {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[1];
                        double price = Double.parseDouble(parts[2]);
                        int quantity = Integer.parseInt(parts[3]);
                        String color = parts[4]; // Đọc màu sắc
                        String imagePath = parts[5]; // Đọc đường dẫn hình ảnh

                        // Đảm bảo đường dẫn hình ảnh là tương đối từ thư mục resources/img
                        if (!imagePath.startsWith("img/")) {
                            imagePath = "img/" + imagePath;
                        }

                        // Tạo đường dẫn tài nguyên để JavaFX có thể truy cập
                        String resourcePath = "/" + imagePath;

                        // Thêm sản phẩm vào danh sách với đường dẫn tài nguyên
                        productList.add(new Product(id, name, price, quantity, color, resourcePath));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveProductsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"))) {
            for (Product product : productList) {
                writer.write(product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getQuantity() + "," + product.getColor() + "," + product.getImagePath());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}