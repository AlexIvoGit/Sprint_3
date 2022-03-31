import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;
import pojo.Orders;

import static io.restassured.RestAssured.given;

@DisplayName("Список заказов")
public class OrderListTest extends BaseApiTest{
    private final String ENDPOINT = "/api/v1/orders";


    @Test
    @DisplayName("Получение списка заказов")
    public void getOrdersTest() {
        Orders orders = given()
                .get(ENDPOINT)
                .body().as(Orders.class);

        Assert.assertTrue("Список заказов пуст", orders.getOrders().size() > 0);
        int i = 0;
    }
}
