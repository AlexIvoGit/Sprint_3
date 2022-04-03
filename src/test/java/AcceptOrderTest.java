import api.model.pojo.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Принять заказ")
public class AcceptOrderTest extends BaseApiTest {
    private String idCourier;
    private int trackOrder;

    @Before
    public void createTestData() {
        Map<String, String> generatedDataCourier = courierClient.getMapGeneratedDataCourier();
        courierClient.createCourier(generatedDataCourier);

        Order order = new Order(List.of("BLACK"));
        idCourier = String.valueOf(courierClient.getIdCourier(generatedDataCourier));
        trackOrder = orderClient.getTrackOrder(order);
    }

    @Test
    @DisplayName("Принять валидный заказ, валидным курьером")
    public void acceptValidOrderWithValidCourier() {
        Response response = orderClient.acceptOrder(trackOrder, idCourier);

        response.then().log().all()
                .statusCode(200)
                .body("ok", equalTo(true));//Проверяем ответ
    }

    @Test
    @DisplayName("Принять валидный заказ, невалидным курьером")
    public void acceptValidOrderWithNotValidCourier() {
        Response response = orderClient.acceptOrder(trackOrder, String.valueOf(Integer.MAX_VALUE));

        //Проверяем статус код
        response.then()
                .statusCode(404)
                .body("message", equalTo("Курьера с таким id не существует")); //Проверяем ответ
    }

    @Test
    @DisplayName("Принять невалидный заказ, валидным курьером")
    public void acceptNotValidOrderWithValidCourier() {
        Response response = orderClient.acceptOrder(Integer.MAX_VALUE, idCourier);

        //Проверяем статус код
        response.then()
                .statusCode(404)
                .body("message", equalTo("Заказа с таким id не существует")); //Проверяем ответ
    }

    @Test
    @DisplayName("Принять валидный заказ без указания курьера")
    public void acceptValidOrderWithoutCourier() {
        Response response = orderClient.acceptOrder(trackOrder, "");

        //Проверяем статус код
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для поиска")); //Проверяем ответ
    }


    @After
    @DisplayName("Удаление тестовых данных")
    public void deleteTestData() {
        orderClient.cancelOrder(trackOrder);
        courierClient.deleteCourier(String.valueOf(idCourier));
    }

}
