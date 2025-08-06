package e2e;

import base.BaseTest;
import POJO.*;

import io.qameta.allure.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.util.List;

@Epic("E2E Testing")
@Feature("Full User-Category-Product Flow")
public class EndToEndTest extends BaseTest {

    private int userId;
    private String userEmail;
    private String userPassword;
    private String accessToken;
    private String refreshToken;

    private int categoryId;
    private Category category;

    private int productId;

    @Test(priority = 1)
    @Story("User Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Create New User")
    @Description("Create a user using POJO and verify successful creation")
    public void createUser() {
        User user = new User("H" + System.currentTimeMillis() + "@gmail.com", "H123456", "Mostafa", "hamed.png");

        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .body(user)
                .post("/users/");

        User responseUser = response.as(User.class);

        userId = responseUser.getId();
        userEmail = user.getEmail();
        userPassword = user.getPassword();

        Assert.assertEquals(response.getStatusCode(), 201, "User creation should return 201");
        Assert.assertEquals(responseUser.getRole(), "customer", "User role should be 'customer'");
    }

    @Test(priority = 2)
    @Story("User Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Login with Created User")
    @Description("Login with the previously created user and get access/refresh tokens")
    public void loginUser() {
        RequestLogin login = new RequestLogin(userEmail, userPassword);

        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .body(login)
                .post("/auth/login");

        ResponseLogin loginResponse = response.as(ResponseLogin.class);
        accessToken = loginResponse.getAccess_token();
        refreshToken = loginResponse.getRefresh_token();

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertNotNull(accessToken, "Access token should not be null");
        Assert.assertNotNull(refreshToken, "Refresh token should not be null");
    }

    @Test(priority = 3)
    @Story("User Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Access User Profile")
    @Description("Get the user profile using the access token")
    public void getUserProfile() {
        Response response = RestAssured.given(Spec)
                .header("Authorization", "Bearer " + accessToken)
                .get("/auth/profile");

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("id"), userId, "Profile ID should match created user ID");
    }

    @Test(priority = 4)
    @Story("User Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Update User")
    @Description("Update created user's details")
    public void updateUser() {
        User user = new User();
        user.updateUser("test@gmail.com", "H654321", "Hamed", "Mo.png");

        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .body(user)
                .put("/users/" + userId);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("email"), user.getEmail());
        Assert.assertEquals(response.jsonPath().getString("name"), user.getName());
        Assert.assertEquals(response.jsonPath().getString("avatar"), user.getAvatar());
    }

    @Test(priority = 5)
    @Story("Category Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Create Category")
    @Description("Create a new product category and verify creation")
    public void createCategory() {
        category = new Category("Animals", "pet.jpg");

        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .body(category)
                .post("/categories/");

        Category responseCategory = response.as(Category.class);
        categoryId = responseCategory.getId();

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertNotNull(responseCategory.getName());
        Assert.assertNotNull(responseCategory.getSlug());
        Assert.assertNotNull(responseCategory.getImage());
    }

    @Test(priority = 6)
    @Story("Product Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Create Product")
    @Description("Create a product and validate the response")
    public void createProduct() {
        Product product = new Product("Cat", 100, "Egyptian Cat", categoryId, List.of("cat1.png", "cat2.png"));

        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .body(product)
                .post("/products/");

        Product responseProduct = response.as(Product.class);
        productId = responseProduct.getId();

        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @Test(priority = 7)
    @Story("Product Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Get Product")
    @Description("Fetch product details by ID")
    public void getProduct() {
        Response response = RestAssured.given(Spec).get("/products/" + productId);
        Product responseProduct = response.as(Product.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(responseProduct.getId(), productId);
        Assert.assertNotNull(responseProduct.getTitle());
        Assert.assertNotNull(responseProduct.getCategory());
    }

    @Test(priority = 8)
    @Story("Product Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Partially Update Product")
    @Description("Update only title and price of product")
    public void partiallyUpdateProduct() {
        Product product = new Product();
        product.partialUpdateProduct("Change title", 250);

        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .body(product)
                .put("/products/" + productId);

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 9)
    @Story("Product Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Fully Update Product")
    @Description("Update title, price, description, category, and images of product")
    public void fullyUpdateProduct() {
        Product product = new Product();
        List<String> images = List.of(
                "https://placehold1.co/600x400",
                "https://placehold2.co/600x400"
        );

        product.fullyUpdateProduct("Change title", 250, "Changed Description", categoryId, images);

        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .body(product)
                .put("/products/" + productId);

        Product responseProduct = response.as(Product.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(responseProduct.getTitle(), "Change title");
        Assert.assertEquals(responseProduct.getDescription(), "Changed Description");
        Assert.assertEquals(responseProduct.getPrice(), 250);
        Assert.assertEquals(responseProduct.getImages(), images);
    }

    @Test(priority = 10)
    @Story("Product Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Filter Product by Category")
    @Description("Get products filtered by category ID")
    public void filterProductsByCategoryId() {
        Response response = RestAssured.given(Spec)
                .queryParam("categoryId", categoryId)
                .get("/products/");

        Assert.assertEquals(response.getStatusCode(), 200);

        List<Product> products = response.jsonPath().getList("", Product.class);
        for (Product product : products) {
            Assert.assertEquals(product.getCategory().getId(), categoryId);
        }
    }

    @Test(priority = 11, dependsOnMethods = "createProduct")
    @Story("Product Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete Product")
    @Description("Delete created product by ID")
    public void deleteProduct() {
        Response response = RestAssured.given(Spec).delete("/products/" + productId);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().asString(), "true");
    }

    @Test(priority = 12, dependsOnMethods = "deleteProduct")
    @Story("Product Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Verify Deleted Product")
    @Description("Try to get deleted product and verify it no longer exists")
    public void verifyProductDeleted() {
        Response response = RestAssured.given(Spec).get("/products/" + productId);

        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("name"), "EntityNotFoundError");
    }

    @Test(priority = 13, dependsOnMethods = "deleteProduct")
    @Story("Category Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete Category")
    @Description("Delete created category by ID")
    public void deleteCategory() {
        Response response = RestAssured.given(Spec).delete("/categories/" + categoryId);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().asString(), "true");
    }

    @Test(priority = 14)
    @Story("User Management")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete User")
    @Description("Delete the created user")
    public void deleteUser() {
        Response response = RestAssured.given(Spec).delete("/users/" + userId);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().asString(), "true");
    }
}
