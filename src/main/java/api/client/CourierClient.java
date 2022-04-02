package api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private final String JSON = "application/json";
    private final String ENDPOINT = "api/v1/courier";

    @Step("Создание курьера")
    public Response createCourier(Map<String, String> param) {
        return given()
                .header("Content-type", JSON)
                .and()
                .body(param)
                .when()
                .post(ENDPOINT);
    }

    @Step("Логин курьера")
    public Response loginCourier(Map<String, String> param) {
        return given()
                .header("Content-type", JSON)
                .and()
                .body(param)
                .when()
                .post(ENDPOINT + "/login");
    }

    @Step("Удаление курьера")
    public Response deleteCourier(String idCourier) {
        return given()
                .delete(ENDPOINT + "/{idCourier}", idCourier);
    }

    @Step("Генерация тестовых данных для создания курьера")
    public Map<String, String> getMapGeneratedDataCourier() {
        HashMap<String, String> generatedData = new HashMap<>();
        generatedData.put("login", RandomStringUtils.randomAlphabetic(10));
        generatedData.put("password", RandomStringUtils.randomAlphabetic(10));
        generatedData.put("firstName", RandomStringUtils.randomAlphabetic(10));
        return generatedData;
    }

    @Step("Получение ID курьера")
    public int getIdCourier(Map<String, String> registrationData) {
        HashMap<String, String> loginData = new HashMap<>(registrationData);
        loginData.remove("firstName");
        Response response = loginCourier(loginData);
        int id = 0;
        if (response.statusCode() == 200) {
            id = response.then().extract().body().path("id");
        }
        return id;
    }


}
