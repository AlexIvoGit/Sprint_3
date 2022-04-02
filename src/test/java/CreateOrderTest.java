import api.model.pojo.Order;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
@DisplayName("Создание заказа")
public class CreateOrderTest extends BaseApiTest {
    private final Order order;
    private int track = 0;


    public CreateOrderTest(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {new Order(List.of("BLACK"))},
                {new Order(List.of("GREY"))},
                {new Order(List.of("BLACK", "GREY"))},
                {new Order(List.of())}};
    }


    @Test
    @DisplayName("Создание заказа с различными параметрами цвета")
    public void createOrderWithParamTest() {
        track = orderClient.sendOrder(order).
                then()
                .statusCode(201)//Проверяем код ответа
                .body("track", notNullValue()) //Проверяем, что тело ответа содержит track
                .extract().body().path("track");
    }

    @After
    @DisplayName("Удаление тестовых данных")
    public void deleteTestData() {
        if (track != 0) {
            orderClient.cancelOrder(track);
        }
    }
}
