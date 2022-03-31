import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import pojo.Order;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Получение заказа")
public class ReceiveOrderByNumberTest extends BaseApiTest {
    private final String ENDPOINT = "/api/v1/orders/track";

    @Test
    @DisplayName("Успешное получение заказа")
    public void successfulReceiveOrder() {
        //Создаем заказ и получаем track
        Order order  = new Order(List.of("BLACK"));
        int trackOrder = order.sendOrder(order);


        Response response = given()
                .queryParam("t", trackOrder)
                .get(ENDPOINT);
        response
                .then().assertThat().statusCode(200);//Проверяем код ответа

        Order receiveOrder = response.then().extract().body().jsonPath().getObject("order", Order.class);
        assertThat(receiveOrder).as("Отправленный и полученный заказ не совпадают").
                usingRecursiveComparison().ignoringFields("deliveryDate").isEqualTo(order); //Сравниваем заказы


        order.cancelOrder(trackOrder); //Удаляем заказ
    }

    @Test
    @DisplayName("Получение заказа без номера")
    public void receiveOrderWithoutTrack() {
        given()
                .get(ENDPOINT)
                .then().assertThat().body("message", equalTo("Недостаточно данных для поиска"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("Получение заказа с несуществующим номером")
    public void receiveOrderWithNotValidTrack() {
        given()
                .queryParam("t", 76677322)
                .get(ENDPOINT)
                .then().assertThat().body("message", equalTo("Заказ не найден"))
                .and().statusCode(404);
    }
}
