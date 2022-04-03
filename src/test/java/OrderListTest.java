import api.model.pojo.Orders;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Список заказов")
public class OrderListTest extends BaseApiTest {

    @Test
    @DisplayName("Получение списка заказов")
    public void getOrdersTest() {
        Orders allOrders = orderClient.getAllOrders();
        assertThat(allOrders.getOrders().size()).as("Полученный список заказов пустой").isNotZero();
    }
}
