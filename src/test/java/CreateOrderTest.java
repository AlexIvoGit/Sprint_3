import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.Order;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
@DisplayName("Создание заказа")
public class CreateOrderTest extends BaseApiTest{
    private final String ENDPOINT = "/api/v1/orders";
    private final Order order;


    public CreateOrderTest(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                {new Order(List.of("BLACK"))},
                {new Order(List.of("GREY"))},
                {new Order( List.of("BLACK", "GREY"))},
                {new Order(List.of())}};
        }


    @Test
    @DisplayName("Создание заказа с различными параметрами цвета")
    public void createOrderWithParamTest(){
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ENDPOINT);
        int track = response.then().assertThat()
                .body("track", notNullValue()) //Проверяем, что тело ответа содержит track
                .and()
                .statusCode(201)//Проверяем код ответа
                .extract().body().path("track"); //Сохраняем трек заказа

        order.cancelOrder(track); //Отменяем заказ
    }
}
