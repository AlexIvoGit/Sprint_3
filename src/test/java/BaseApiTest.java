import api.client.CourierClient;
import api.client.OrderClient;
import io.restassured.RestAssured;
import org.junit.BeforeClass;

public abstract class BaseApiTest {
    protected final CourierClient courierClient = new CourierClient();
    protected final OrderClient orderClient = new OrderClient();

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}
