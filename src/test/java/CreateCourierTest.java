import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Создание курьера")
public class CreateCourierTest extends BaseApiTest {
    private Map<String, String> generatedDataCourier;

    @Before
    @DisplayName("Подготовка тестовых данных")
    public void createTestData() {
        generatedDataCourier = courierClient.getMapGeneratedDataCourier();
    }

    @Test
    @DisplayName("Создание нового курьера")
    public void createNewCourierTest() {

        //Создаем курьера и получаем ответ
        Response response = courierClient.createCourier(generatedDataCourier);

        //Сравниваем код ответа;
        response.then()
                .statusCode(201)
                .body("ok", equalTo(true)); //Успешный запрос возвращает ok: true;
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void createDoubleCourierTest() {

        //Создаем курьера
        courierClient.createCourier(generatedDataCourier);

        //Повторно отправляем запрос на создание этого же курьера, получаем ответ
        Response response = courierClient.createCourier(generatedDataCourier);

        //Сравниваем код ответа;
        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется")); //Сравниваем текст ошибки

    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void createCourierWithoutLoginParamTest() {

        Map<String, String> dataCourier = new HashMap<>(generatedDataCourier);
        //Создаем курьера предварительно удалив логин из тела и получаем ответ
        dataCourier.remove("login");
        Response response = courierClient.createCourier(dataCourier);

        //Сравниваем код ответа;
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи")); //Сравниваем текст ошибки
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCourierWithoutPasswordParamTest() {

        Map<String, String> dataCourier = new HashMap<>(generatedDataCourier);

        //Создаем курьера предварительно удалив пароль из тела и получаем ответ
        dataCourier.remove("password");
        Response response = courierClient.createCourier(dataCourier);

        //Сравниваем код ответа;
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи")); //Сравниваем текст ошибки
    }

    @After
    @DisplayName("Удаление тестовых данных")
    public void deleteTestData() {
        //Получаем ID и удаляем курьера
        int id = courierClient.getIdCourier(generatedDataCourier);
        if(id != 0){
            courierClient.deleteCourier(String.valueOf(id));
        }
    }
}
