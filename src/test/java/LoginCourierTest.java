import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Логин курьера")
public class LoginCourierTest extends BaseApiTest{
    private final String ENDPOINT = "/api/v1/courier/login";
    private List<String> courierData = scooterCourier.registerNewCourierAndReturnLoginPasswordFirstName();
    private String login = courierData.get(0);
    private String password = courierData.get(1);


    @Test
    @DisplayName("Авторизация курьера")
    public void authorizationCourier() {
        //Составляем тело запроса
        String body = scooterCourier.getBodyCourierForLogin(login, password);

        //Отправляем запрос на авторизацию
        given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(ENDPOINT)
                .then().assertThat().body("id", notNullValue())//Проверяем, что ID вернулось
                .and().statusCode(200); //Проверяем код ответа
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    public void authorizationCourierWithoutLogin() {
        //Составляем тело запроса
        String body = "{\n" +
                "    \"password\": \"" + password + "\"\n" +
                "}";

        //Отправляем запрос на авторизацию
        given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(ENDPOINT)
                .then().assertThat().body("message", equalTo("Недостаточно данных для входа"))//Проверяем текст ошибки
                .and().statusCode(400); //Проверяем код ответа
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    public void authorizationCourierWithoutPassword() {
        //Составляем тело запроса
        String body = "{\n" +
                "    \"login\": \"" + login + "\"\n" +
                "}";

        //Отправляем запрос на авторизацию
        given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(ENDPOINT)
                .then().assertThat().body("message", equalTo("Недостаточно данных для входа"))//Проверяем текст ошибки
                .and().statusCode(400); //Проверяем код ответа
    }

    @Test
    @DisplayName("Авторизация курьера с невалидными данными")
    public void authorizationCourierNotValidData() {
        //Составляем тело запроса
        String body = scooterCourier.getBodyCourierForLogin("n", "1254");

        //Отправляем запрос на авторизацию
        given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(ENDPOINT)
                .then().assertThat().body("message", equalTo("Учетная запись не найдена"))//Проверяем текст ошибки
                .and().statusCode(404); //Проверяем код ответа
    }

    @After
    public void tearDown() {
        //Удаляем созданного курьера
        scooterCourier.deleteCourier(scooterCourier.getIdCourier(login, password));
    }

}
