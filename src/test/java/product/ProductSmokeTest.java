package product;

import base.BaseTest;
import POJO.Category;
import POJO.Product;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Product Management")
@Feature("Product CRUD Operations")
public class ProductSmokeTest extends BaseTest {

    int productId;
    Product productObject;
    String productSlug;
    int categoryId;

    @Test(priority = 1)
    @Story("Get all products")
    @Description("Verify that all products are returned as a JSON array with valid properties")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Get All Products")
    public void getAllProductsTest() {
        Response response = RestAssured.given(Spec).contentType(ContentType.JSON).get("/products");
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.time() < 2000);
        List<Object> products = response.jsonPath().getList("");
        Assert.assertNotNull(products, "Response is not an array");
        Assert.assertFalse(products.isEmpty(), "Array is empty");
    }

    @Test(dependsOnMethods = "getAllProductsTest")
    @Story("Create a new product")
    @Description("Create a product with title, price, description, category, and images")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Create Product")
    public void createProductTest() {
        Response response = RestAssured.given(Spec).get("/categories");
        Assert.assertEquals(response.getStatusCode(), 200);
        List<Category> categories = response.jsonPath().getList("", Category.class);
        Assert.assertFalse(categories.isEmpty(), "No categories returned");

        Category firstCategory = categories.getFirst();
        categoryId = firstCategory.getId();
        Product product = new Product("Cat", 100, "Egyptian Cat", categoryId, List.of("cat1.png", "cat2.png"));

        response = RestAssured.given(Spec).contentType(ContentType.JSON).body(product).post("/products/");
        response.prettyPrint();
        Product responseProduct = response.as(Product.class);

        productId = responseProduct.getId();
        productObject = responseProduct;
        productSlug = responseProduct.getSlug();

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertTrue(response.time() < 3000);
        Assert.assertEquals(responseProduct.getTitle(), "Cat");
        Assert.assertEquals(responseProduct.getDescription(), "Egyptian Cat");
    }

    @Test(dependsOnMethods = "createProductTest")
    @Story("Get product by ID")
    @Description("Verify retrieving a product using its ID returns correct data")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Get Product by ID")
    public void getProductByIdTest() {
        Response response = RestAssured.given(Spec).contentType(ContentType.JSON).get("/products/" + productId);
        response.prettyPrint();
        Product responseProduct = response.as(Product.class);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.time() < 2000);
        Assert.assertEquals(responseProduct.getTitle(), "Cat");
        Assert.assertEquals(responseProduct.getDescription(), "Egyptian Cat");
    }

    @Test(dependsOnMethods = "createProductTest")
    @Story("Get product by Slug")
    @Description("Verify retrieving a product using its Slug returns correct data")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Get Product by Slug")
    public void getProductBySlugTest() {
        Response response = RestAssured.given(Spec).contentType(ContentType.JSON).get("/products/slug/" + productSlug);
        response.prettyPrint();
        Product responseProduct = response.as(Product.class);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.time() < 2000);
        Assert.assertEquals(responseProduct.getTitle(), "Cat");
        Assert.assertEquals(responseProduct.getDescription(), "Egyptian Cat");
    }

    @Test(dependsOnMethods = "createProductTest")
    @Story("Update product")
    @Description("Update the full product information")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Update Product")
    public void updateProductTest() {
        Product product = new Product();
        List<String> images = List.of(
                "https://placehold1.co/600x400",
                "https://placehold2.co/600x400"
        );
        product.fullyUpdateProduct("Change title", 250, "Changed Description", categoryId, images);

        Response response = RestAssured.given(Spec).contentType(ContentType.JSON)
                .body(product).put("/products/" + productId);
        response.prettyPrint();
        Product responseProduct = response.as(Product.class);

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(responseProduct.getTitle(), "Change title");
        Assert.assertEquals(responseProduct.getDescription(), "Changed Description");
        Assert.assertEquals(responseProduct.getPrice(), 250);
        Assert.assertEquals(responseProduct.getImages(), images);
    }

    @Test(dependsOnMethods = "updateProductTest")
    @Story("Delete product")
    @Description("Delete the created product using its ID")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Delete Product")
    public void deleteProductTest() {
        Response response = RestAssured.given(Spec).delete("/products/" + productId);
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertEquals(response.getBody().asString(), "true", "Response body should be 'true'");
    }
}
