package com.example.duanshopbangiay;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserShopController implements Initializable {
    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;
    @FXML
    private ComboBox<Integer> quantityComboBox;

    @FXML
    private ComboBox<String> sizeComboBox;

    @FXML
    private VBox chosenProductCard;

    @FXML
    private ImageView productImg;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label productPriceLable;

    @FXML
    private GridPane grid;

    @FXML
    private ImageView cartIcon; // Thêm ImageView cho giỏ hàng

    private List<Product> products = new ArrayList<>();

    private Cart cart = new Cart(); // Thêm đối tượng Cart

    private Image image;

    private MyListener myListener;

    private Stage cartViewStage;

    private FXMLLoader cartViewLoader;


    private List<Product> loadProductsFromFile() {
        List<Product> products = new ArrayList<>();
        String filePathProduct = "products.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePathProduct))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 7) {
                    Product product = new Product();
                    product.setId(Integer.parseInt(fields[0]));
                    product.setName(fields[1]);
                    product.setPrice(Double.parseDouble(fields[2]));
                    product.setQuantity(Integer.parseInt(fields[3]));
                    product.setColor(fields[4]);
                    product.setSize(fields[5]);
                    product.setImagePath(fields[6]);
                    products.add(product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    private void setChosenProduct(Product product) {
        productNameLabel.setText(product.getName());
        productPriceLable.setText(UserShopApplication.CURRENCY + product.getPrice());
        image = new Image(getClass().getResourceAsStream(product.getImagePath()));
        productImg.setImage(image);
        chosenProductCard.setStyle("-fx-background-color: #" + product.getColor() + ";\n" +
                "    -fx-background-radius: 30;");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        products.addAll(loadProductsFromFile());
        int maxQuantity = 10;
        String[] availableSizes = {"36", "37", "38", "39", "40", "41", "42"};

        for (int i = 1; i <= maxQuantity; i++) {
            quantityComboBox.getItems().add(i);
        }
        sizeComboBox.getItems().addAll(availableSizes);

        searchButton.setOnAction(event -> searchProduct());
        Button addToCartButton = (Button) chosenProductCard.lookup("#addToCart");
        addToCartButton.setOnAction(event -> handleAddToCart());

        if (products.size() > 0) {
            setChosenProduct(products.get(0));
            myListener = new MyListener() {
                @Override
                public void onClickListener(Product product) {
                    setChosenProduct(product);
                }
            };
        }
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < products.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();

                itemController.setData(products.get(i), myListener);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Thiết lập sự kiện cho giỏ hàng
        cartIcon.setOnMouseClicked(event -> showCartView());
    }

    private void showCartView() {
        try {
            if (cartViewLoader == null) {
                cartViewLoader = new FXMLLoader(getClass().getResource("CartView.fxml"));
                AnchorPane cartView = cartViewLoader.load();
                CartViewController controller = cartViewLoader.getController();
                controller.setCart(cart);
                controller.loadCartItemsFromFile();// Đọc dữ liệu từ tệp khi mở
                controller.updateTotalAmount();

                cartViewStage = new Stage();
                cartViewStage.setScene(new Scene(cartView));
                cartViewStage.setTitle("Giỏ hàng");

                // Đăng ký sự kiện khi cửa sổ đóng
                cartViewStage.setOnHiding(event -> {
                    CartViewController cartController = cartViewLoader.getController();
                    if (cartController != null) {
                        cartController.saveCartToFile(); // Lưu dữ liệu khi cửa sổ đóng
                        controller.updateTotalAmount();
                    }
                });

            } else {
                CartViewController controller = cartViewLoader.getController();
                if (controller != null) {
                    controller.loadCartItemsFromFile(); // Đọc dữ liệu từ tệp khi mở
                }
            }

            cartViewStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onQuantityChange() {
        Integer selectedQuantity = quantityComboBox.getValue();
        // Xử lý thay đổi số lượng nếu cần
    }

    @FXML
    private void onSizeChange() {
        String selectedSize = sizeComboBox.getValue();
        // Xử lý thay đổi kích cỡ nếu cần
    }

    private void searchProduct() {
        String searchText = searchTextField.getText().trim().toLowerCase();
        // Tìm kiếm sản phẩm trong danh sách
        Product foundProduct = products.stream()
                .filter(p -> p.getName().toLowerCase().contains(searchText))
                .findFirst()
                .orElse(null);

        if (foundProduct != null) {
            // Hiển thị sản phẩm
            showProduct(foundProduct);
            chosenProductCard.setStyle("-fx-background-color: #" + foundProduct.getColor() + ";\n" +
                    "    -fx-background-radius: 30;");
        } else {
            // Xóa sản phẩm hiển thị nếu không tìm thấy
            clearProductDisplay();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Không tìm thấy ");
            alert.setHeaderText(null);
            alert.setContentText("Không tìm thấy sản phẩm nào với từ khóa: " + searchText);
            alert.showAndWait();
            // hiển thị lại sản phẩm
            showProduct(products.get(0));
            chosenProductCard.setStyle("-fx-background-color: #" + products.get(0).getColor() + ";\n" +
                    "    -fx-background-radius: 30;");
        }

    }

    private void showProduct(Product product) {
        productNameLabel.setText(product.getName());
        productPriceLable.setText("$ " + product.getPrice());
        productImg.setImage(new Image(getClass().getResource(product.getImagePath()).toExternalForm()));

        // Cập nhật ComboBox cho số lượng và kích cỡ
        quantityComboBox.getItems().clear();
        for (int i = 1; i <= product.getQuantity(); i++) {
            quantityComboBox.getItems().add(i);
        }

        sizeComboBox.getItems().clear();
        sizeComboBox.getItems().add(product.getSize());

        // Hiển thị VBox sản phẩm
        chosenProductCard.setVisible(true);
    }

    private void clearProductDisplay() {
        productNameLabel.setText("");
        productPriceLable.setText("");
        productImg.setImage(null);

        // Ẩn VBox sản phẩm
        chosenProductCard.setVisible(false);
    }

    public void addToCart(Product product) {
        cart.addProduct(product);
        saveCartToFile();
        // Cập nhật lại giao diện giỏ hàng nếu đang mở
        if (cartViewStage != null && cartViewStage.isShowing()) {
            CartViewController cartViewController = cartViewLoader.getController();
            cartViewController.setCart(cart);
        }
    }
    private void saveCartToFile() {
        String filePath = "Cart.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Product product : cart.getProducts()) {
                writer.write(product.getId() + "," +
                        product.getName() + "," +
                        product.getPrice() + "," +
                        product.getQuantity() + "," +
                        product.getColor() + "," +
                        product.getSize() + "," +
                        product.getImagePath());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddToCart() {
        // Lấy thông tin sản phẩm hiện tại
        Product selectedProduct = products.stream()
                .filter(p -> p.getName().equals(productNameLabel.getText()))
                .findFirst()
                .orElse(null);

        if (selectedProduct != null) {
            // Lấy thông tin từ ComboBox
            Integer selectedQuantity = quantityComboBox.getValue();
            String selectedSize = sizeComboBox.getValue();

            int defaultQuantity = 1;
            String defaultSize = "36";

            // Sử dụng giá trị mặc định nếu người dùng không chọn gì
            if (selectedQuantity == null) {
                selectedQuantity = defaultQuantity;
            }

            if (selectedSize == null) {
                selectedSize = defaultSize;
            }

            if (selectedQuantity > selectedProduct.getQuantity()) {
                // Kiểm tra nếu số lượng chọn lớn hơn số lượng có sẵn
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Số lượng chọn không hợp lệ. Vui lòng chọn số lượng nhỏ hơn hoặc bằng số lượng có sẵn.");
                alert.showAndWait();
                return;
            }

            // Tạo một sản phẩm mới với thông tin đã chọn
            Product productToAdd = new Product(
                    selectedProduct.getId(),
                    selectedProduct.getName(),
                    selectedProduct.getPrice(),
                    selectedQuantity,
                    selectedProduct.getColor(),
                    selectedSize,
                    selectedProduct.getImagePath()
            );

            // Thêm sản phẩm vào giỏ hàng
            addToCart(productToAdd);

            // Hiển thị thông báo xác nhận
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText(null);
            alert.setContentText("Sản phẩm đã được thêm vào giỏ hàng.");
            alert.showAndWait();
        } else {
            // Hiển thị thông báo lỗi nếu không tìm thấy sản phẩm
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Sản phẩm không tồn tại.");
            alert.showAndWait();
        }
    }
}
