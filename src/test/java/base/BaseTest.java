package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected RequestSpecification Spec;
    @BeforeMethod
    public void setUp(){
        Spec = new RequestSpecBuilder().
                setBaseUri("https://api.escuelajs.co/api/v1").
                build();
    }
}
