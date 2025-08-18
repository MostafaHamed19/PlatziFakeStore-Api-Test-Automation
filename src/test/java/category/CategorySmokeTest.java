package category;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import POJO.Category;
import io.qameta.allure.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Category Module")
@Feature("Smoke Test for Category CRUD Operations")
public class CategorySmokeTest extends BaseTest {

    private int categoryId;
    private Category originalCategory;
    private String categorySlug;

    @Test(priority = 1)
    @Story("Get All Categories")
    @Description("Verify that all categories are returned as a JSON array with valid properties")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Get All Categories")
    public void getAllCategories() {
        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .get("/categories");

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.time() < 2000, "Response time should be < 2s");

        List<Object> categories = response.jsonPath().getList("");
        Assert.assertNotNull(categories, "Response is not an array");
        Assert.assertFalse(categories.isEmpty(), "Categories list is empty");
    }

    @Test(dependsOnMethods = "getAllCategories")
    @Story("Create New Category")
    @Description("Verify that a new category can be created successfully")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Create Category")
    public void createCategory() {
        originalCategory = new Category("category " + System.currentTimeMillis(), "https://placeimg.com/640/480/any");

        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .body(originalCategory)
                .post("/categories/");

        response.prettyPrint();

        Category responseCategory = response.as(Category.class);
        categoryId = responseCategory.getId();
        categorySlug = responseCategory.getSlug();

        Assert.assertEquals(response.getStatusCode(), 201, "Status code should be 201");
        Assert.assertTrue(response.time() < 2000, "Response time should be < 2s");
        Assert.assertNotNull(responseCategory.getName(), "Category name is null");
        Assert.assertNotNull(responseCategory.getSlug(), "Category slug is null");
        Assert.assertNotNull(responseCategory.getImage(), "Category image is null");
    }

    @Test(dependsOnMethods = "createCategory")
    @Story("Get Category by ID")
    @Description("Verify that a category can be retrieved by its ID")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Get Category By ID")
    public void getCategoryById() {
        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .get("/categories/" + categoryId);

        response.prettyPrint();

        Category responseCategory = response.as(Category.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.time() < 2000, "Response time should be < 2s");
        Assert.assertNotNull(responseCategory.getName(), "Name is null");
        Assert.assertNotNull(responseCategory.getSlug(), "Slug is null");
        Assert.assertNotNull(responseCategory.getImage(), "Image is null");
    }

    @Test(dependsOnMethods = "createCategory")
    @Story("Get Category by Slug")
    @Description("Verify that a category can be retrieved by its slug")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Get Category By Slug")
    public void getCategoryBySlug() {
        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .get("/categories/slug/" + categorySlug);

        response.prettyPrint();

        Category responseCategory = response.as(Category.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.time() < 2000, "Response time should be < 2s");
        Assert.assertNotNull(responseCategory.getName(), "Name is null");
        Assert.assertEquals(responseCategory.getSlug(), categorySlug, "Slug doesn't match");
        Assert.assertNotNull(responseCategory.getImage(), "Image is null");
    }

    @Test(dependsOnMethods = "createCategory")
    @Story("Update Category")
    @Description("Verify that a category can be updated successfully")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Update Category")
    public void updateCategory() {
        Category updatedCategory = new Category();
        updatedCategory.updateCategory("Updated Category", "UpdatedImage.png");

        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .body(updatedCategory)
                .put("/categories/" + categoryId);

        response.prettyPrint();

        Category responseCategory = response.as(Category.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.time() < 2000, "Response time should be < 2s");
        Assert.assertEquals(responseCategory.getName(), "Updated Category", "Name wasn't updated");
        Assert.assertEquals(responseCategory.getImage(), "UpdatedImage.png", "Image wasn't updated");
    }

    @Test(dependsOnMethods = "updateCategory")
    @Story("Delete Category")
    @Description("Verify that a category can be deleted successfully")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete Category")
    public void deleteCategory() {
        Response response = RestAssured.given(Spec)
                .delete("/categories/" + categoryId);

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.time() < 2000, "Response time should be < 2s");
        Assert.assertEquals(response.getBody().asString(), "true", "Category was not deleted");
    }
}
