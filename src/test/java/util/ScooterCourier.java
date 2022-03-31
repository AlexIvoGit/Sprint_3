package util;

// импортируем Response

import io.restassured.response.Response;
// импортируем библиотеку генерации строк
import org.apache.commons.lang3.RandomStringUtils;
// импортируем список
import java.util.ArrayList;
import java.util.List;
// дополнительный статический импорт нужен, чтобы использовать given(), get() и then()
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ScooterCourier {

    private final String BASE_URI = "https://qa-scooter.praktikum-services.ru";

    /*
    метод регистрации нового курьера
    возвращает список из логина, пароля и имени курьера
    если регистрация не удалась, возвращаем null
    */
    public List<String> registerNewCourierAndReturnLoginPasswordFirstName() {
        //Получаем сгенерированные данные для создания курьера
        List<String> listDataCourierForCreate = getListDataCourierForCreate();

        String login = listDataCourierForCreate.get(0);
        String password = listDataCourierForCreate.get(1);
        String firstName = listDataCourierForCreate.get(2);

        // собираем в строку тело запроса на регистрацию, подставляя в него логин, пароль и имя курьера
        String registerRequestBody = getBodyCourierForCreate(login, password, firstName);

        // отправляем запрос на регистрацию курьера и сохраняем ответ в переменную response класса Response
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post(BASE_URI + "/api/v1/courier");

        // если регистрация прошла успешно (код ответа 201), возвращаем список с данными курьера
        if (response.statusCode() == 201) {
            return listDataCourierForCreate;
        }
        // если регистрация не успешна, ничего не возвращаем
        return null;
    }

    public String getIdCourier(String login, String password) {
        int id = 0;
        String body = getBodyCourierForLogin(login, password);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(BASE_URI + "/api/v1/courier/login");
        if (response.statusCode() == 200) {
            id = response.then().extract().body().path("id");
        }
        return String.valueOf(id);
    }

    public void deleteCourier(String id) {
        given()
                .delete(BASE_URI + "/api/v1/courier/{id}", id);
    }

    public String getBodyCourierForCreate(String login, String password, String firstName) {
        return "{\"login\":\"" + login + "\","
                + "\"password\":\"" + password + "\","
                + "\"firstName\":\"" + firstName + "\"}";
    }

    public String getBodyCourierForLogin(String login, String password) {
        return "{\n" +
                "    \"login\": \"" + login + "\",\n" +
                "    \"password\": \"" + password + "\"\n" +
                "}";
    }


    public List<String> getListDataCourierForCreate() {
        // с помощью библиотеки RandomStringUtils генерируем логин
        // метод randomAlphabetic генерирует строку, состоящую только из букв, в качестве параметра передаём длину строки
        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем пароль
        String courierPassword = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем имя курьера
        String courierFirstName = RandomStringUtils.randomAlphabetic(10);
        return List.of(courierLogin, courierPassword, courierFirstName);
    }

}
