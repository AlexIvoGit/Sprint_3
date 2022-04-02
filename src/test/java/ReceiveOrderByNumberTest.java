import api.model.pojo.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Получение заказа")
public class ReceiveOrderByNumberTest extends BaseApiTest {
    private Order order;
    private int trackOrder;

    @Before
    public void createTestData() {
        order = new Order(List.of("BLACK"));
        trackOrder = orderClient.getTrackOrder(order);
    }


    @Test
    @DisplayName("Успешное получение заказа")
    public void successfulReceiveOrder() {
        Response response = orderClient.getOrder(String.valueOf(trackOrder));

        //Проверяем код ответа и получаем заказ
        Order receiveOrder = response.then()
                .statusCode(200)
                .extract().body().jsonPath().getObject("order", Order.class);

        assertThat(receiveOrder).as("Отправленный и полученный заказ не совпадают").
                usingRecursiveComparison().ignoringFields("deliveryDate").isEqualTo(this.order); //Сравниваем заказы
    }

    @Test
    @DisplayName("Получение заказа без номера")
    public void receiveOrderWithoutTrack() {
        Response response = orderClient.getOrder("");

        //Проверяем код ответа
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для поиска")); //Проверяем текст ошибки
    }

    @Test
    @DisplayName("Получение заказа с несуществующим номером")
    public void receiveOrderWithNotValidTrack() {
        Response response = orderClient.getOrder(String.valueOf(Integer.MAX_VALUE));

        //Проверяем код ответа
        response.then()
                .statusCode(404)
                .body("message", equalTo("Заказ не найден")); //Проверяем текст ошибки
    }

    @After
    public void deleteTestData() {
        orderClient.cancelOrder(trackOrder);
    }
}
