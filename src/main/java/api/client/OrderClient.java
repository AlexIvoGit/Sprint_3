package api.client;

import api.model.pojo.Order;
import api.model.pojo.Orders;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private final String JSON = "application/json";
    private final String ENDPOINT = "/api/v1/orders";


    @Step("Создать заказ")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", JSON)
                .and()
                .body(order)
                .when()
                .post(ENDPOINT);
    }

    @Step("Получить заказ по номеру")
    public Response getOrder(String trackOrder) {
        return given()
                .queryParam("t", trackOrder)
                .get(ENDPOINT + "/track");

    }

    @Step("Принять заказ")
    public Response acceptOrder(int trackOrder, String courierId) {
        return given()
                .queryParam("courierId", courierId)
                .put(ENDPOINT + "/accept/{trackOrder}", trackOrder);
    }

    @Step("Отмена заказа")
    public Response cancelOrder(int track) {
        return given()
                .queryParam("track", String.valueOf(track))
                .put(ENDPOINT + "/cancel");// отправка PUT-запроса на отмену заказа
    }


    @Step("Отправка заказа")
    public Response sendOrder(Order order) {
        return given()
                .header("Content-type", JSON)
                .and()
                .body(order)
                .when()
                .post(ENDPOINT);
    }

    @Step("Получение трек номера заказа")
    public int getTrackOrder(Order order) {
        return sendOrder(order).then()
                .extract().body().path("track");
    }

    @Step("Получение всех заказов")
    public Orders getAllOrders() {
        return given()
                .get(ENDPOINT)
                .body().as(Orders.class);
    }
}
