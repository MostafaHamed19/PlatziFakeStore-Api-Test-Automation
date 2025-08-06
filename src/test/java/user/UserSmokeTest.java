package user;

import base.BaseTest;
import POJO.User;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("User Management")
@Feature("User CRUD Operations")
public class UserSmokeTest extends BaseTest {

    private int createdUserId;

    @Test(priority = 1)
    @DisplayName("Get All Users")
    @Description("Ensure all users are retrieved successfully with valid structure and data")
    @Severity(SeverityLevel.NORMAL)
    public void getAllUsers() {
        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .get("/users/");
        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.time() < 2000, "Response time should be < 2000ms");

        List<Object> users = response.jsonPath().getList("");
        Assert.assertNotNull(users, "Response is not an array");
        Assert.assertFalse(users.isEmpty(), "Users array is empty");
    }

    @Test(priority = 2)
    @DisplayName("Create New User")
    @Description("Verify adding a new user with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void createUser() {
        User user = new User(
                "H" + System.currentTimeMillis() + "@gmail.com",
                "H123456",
                "Mostafa",
                "hamed.png"
        );

        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .body(user)
                .post("/users/");
        response.prettyPrint();

        User responseUser = response.as(User.class);
        createdUserId = responseUser.getId();

        Assert.assertEquals(response.getStatusCode(), 201, "Status code should be 201");
        Assert.assertEquals(responseUser.getRole(), "customer", "Role should be 'customer'");
    }

    @Test(priority = 3)
    @DisplayName("Get User by ID")
    @Description("Verify retrieving a user by ID returns correct user details")
    @Severity(SeverityLevel.NORMAL)
    public void getUserById() {
        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .get("/users/" + createdUserId);
        response.prettyPrint();

        User responseUser = response.as(User.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.time() < 2000, "Response time should be < 2000ms");
        Assert.assertEquals(responseUser.getName(), "Mostafa", "Name should match");
        Assert.assertEquals(responseUser.getAvatar(), "hamed.png", "Avatar should match");
    }

    @Test(priority = 4)
    @DisplayName("Update Existing User")
    @Description("Verify updating an existing user's info works correctly")
    @Severity(SeverityLevel.CRITICAL)
    public void updateUser() {
        User updatedUser = new User();
        updatedUser.updateUser("test@gmail.com", "H654321", "Hamed", "Mo.png");

        Response response = RestAssured.given(Spec)
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .put("/users/" + createdUserId);
        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.time() < 2000, "Response time should be < 2000ms");

        Assert.assertEquals(response.jsonPath().getString("email"), updatedUser.getEmail(), "Email should match");
        Assert.assertEquals(response.jsonPath().getString("password"), updatedUser.getPassword(), "Password should match");
        Assert.assertEquals(response.jsonPath().getString("name"), updatedUser.getName(), "Name should match");
        Assert.assertEquals(response.jsonPath().getString("avatar"), updatedUser.getAvatar(), "Avatar should match");
    }

    @Test(priority = 5)
    @DisplayName("Delete User")
    @Description("Verify deleting the created user using its ID")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteUser() {
        Response response = RestAssured.given(Spec)
                .delete("/users/" + createdUserId);
        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.time() < 2000, "Response time should be < 2000ms");
        Assert.assertEquals(response.getBody().asString(), "true", "Response body should be 'true'");
    }
}
