import io.restassured.RestAssured;
import org.junit.BeforeClass;
import util.ScooterCourier;

public abstract class BaseApiTest {
    protected final ScooterCourier scooterCourier = new ScooterCourier();

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}
