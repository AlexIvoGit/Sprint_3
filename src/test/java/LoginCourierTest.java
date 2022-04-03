import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Логин курьера")
public class LoginCourierTest extends BaseApiTest {
    private Map<String, String> generatedDataCourier;

    @Before
    @DisplayName("Подготовка тестовых данных")
    public void createTestData() {
        generatedDataCourier = courierClient.getMapGeneratedDataCourier();
        courierClient.createCourier(generatedDataCourier);
    }


    @Test
    @DisplayName("Авторизация курьера")
    public void authorizationCourier() {
        //Отправляем запрос на логин курьера
        Response response = courierClient.loginCourier(generatedDataCourier);

        //Проверяем код ответа
        response.then()
                .statusCode(200)
                .body("id", notNullValue()); //Проверяем, что ID вернулось

    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    public void authorizationCourierWithoutLogin() {
        Map<String, String> dataCourier = new HashMap<>(generatedDataCourier);

        //Удаляем из сгенерированных данных login
        dataCourier.remove("login");

        //Отправляем запрос на логин курьера
        Response response = courierClient.loginCourier(dataCourier);

        //Проверяем код ответа
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа")); //Проверяем текст ошибки
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    public void authorizationCourierWithoutPassword() {
        Map<String, String> dataCourier = new HashMap<>(generatedDataCourier);

        //Удаляем из сгенерированных данных password
        dataCourier.remove("password");

        //Отправляем запрос на логин курьера
        Response response = courierClient.loginCourier(dataCourier);

        //Проверяем код ответа
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа")); //Проверяем текст ошибки
    }

    @Test
    @DisplayName("Авторизация курьера с невалидными данными")
    public void authorizationCourierNotValidData() {
        Map<String, String> dataCourier = new HashMap<>(generatedDataCourier);

        //Изменяем пароль
        dataCourier.put("password", "09876432627");

        //Отправляем запрос на логин курьера
        Response response = courierClient.loginCourier(dataCourier);

        //Проверяем код ответа
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));//Проверяем текст ошибки
    }

    @After
    @DisplayName("Удаление тестовых данных")
    public void deleteTestData() {
        //Получаем ID и удаляем курьера
        int id = courierClient.getIdCourier(generatedDataCourier);
        if (id != 0) {
            courierClient.deleteCourier(String.valueOf(id));
        }
    }
}
