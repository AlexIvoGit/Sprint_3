import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Order;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Принять заказ")
public class AcceptOrderTest extends BaseApiTest {
    private List<String> courierData;
    private Order order;
    private String idCourier;
    private int trackOrder;
    private final String ENDPOINT = "/api/v1/orders/accept/{trackOrder}";

    @Before
    public void createTestData() {
        courierData = scooterCourier.registerNewCourierAndReturnLoginPasswordFirstName();
        String loginCourier = courierData.get(0);
        String passwordCourier = courierData.get(1);
        order = new Order(List.of("BLACK"));
        idCourier = scooterCourier.getIdCourier(loginCourier, passwordCourier);
        trackOrder = order.sendOrder(order);
    }

    @Test
    @DisplayName("Принять валидный заказ, валидным курьером")
    public void acceptValidOrderWithValidCourier() {
        given()
                .queryParam("courierId", Integer.parseInt(idCourier))
                .put(ENDPOINT, trackOrder)
                .then().assertThat()
                .body("ok", equalTo(true)) //Проверяем ответ
                .statusCode(200); //Проверяем статус код
    }

    @Test
    @DisplayName("Принять валидный заказ, невалидным курьером")
    public void acceptValidOrderWithNotValidCourier() {
        given()
                .queryParam("courierId", 764437228)
                .put(ENDPOINT, trackOrder)
                .then().assertThat()
                .body("message", equalTo("Курьера с таким id не существует")) //Проверяем ответ
                .statusCode(404); //Проверяем статус код
    }

    @Test
    @DisplayName("Принять невалидный заказ, валидным курьером")
    public void acceptNotValidOrderWithValidCourier() {
        given()
                .queryParam("courierId", Integer.parseInt(idCourier))
                .put(ENDPOINT, 54433434)
                .then().assertThat()
                .body("message", equalTo("Заказа с таким id не существует")) //Проверяем ответ
                .statusCode(404); //Проверяем статус код
    }

    @Test
    @DisplayName("Принять валидный заказ без указания курьера")
    public void acceptValidOrderWithoutCourier() {
        given()
                .put(ENDPOINT, trackOrder)
                .then().assertThat()
                .body("message", equalTo("Недостаточно данных для поиска")) //Проверяем ответ
                .statusCode(400); //Проверяем статус код
    }

    @After
    public void deleteTestData() {
        order.cancelOrder(trackOrder);
        scooterCourier.deleteCourier(idCourier);
    }

}
