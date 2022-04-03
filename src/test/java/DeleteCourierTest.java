import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Удаление курьера")
public class DeleteCourierTest extends BaseApiTest {
    private String id;

    @Before
    @DisplayName("Подготовка тестовых данных")
    public void createTestData() {
        Map<String, String> generatedDataCourier = courierClient.getMapGeneratedDataCourier();
        courierClient.createCourier(generatedDataCourier);
        generatedDataCourier.remove("firstName");
        id = String.valueOf(courierClient.getIdCourier(generatedDataCourier));
    }

    @Test
    @DisplayName("Удаление курьера с валидными данными")
    public void deleteCourierWithValidDataTest() {
        //Отправляем запрос на удаление
        Response response = courierClient.deleteCourier(id);

        //Проверяем код ответа
        response.then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Удаление курьера без указания ID")
    public void deleteCourierWithoutIdTest() {
        //Отправляем запрос на удаление
        Response response = courierClient.deleteCourier("");

        //Проверяем код ответа
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Удаление курьера с указанием несуществующего ID")
    public void deleteCourierWithDefunctIdTest() {
        //Отправляем запрос на удаление
        Response response = courierClient.deleteCourier(String.valueOf(Integer.MAX_VALUE));

        //Проверяем код ответа
        response.then()
                .statusCode(404)
                .body("message", equalTo("Курьера с таким id нет"));
    }

    @After
    @DisplayName("Удаление тестовых данных")
    public void deleteTestData() {
        //Получаем ID и удаляем курьера
        courierClient.deleteCourier(id);
    }
}
