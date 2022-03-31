import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Удаление курьера")
public class DeleteCourierTest extends BaseApiTest{

    private final String ENDPOINT = "/api/v1/courier/{id}";

    @Test
    @DisplayName("Удаление курьера с валидными данными")
    public void deleteCourierWithValidDataTest(){
        //Создаем курьера и получаем его ID
        List<String> courierData = scooterCourier.registerNewCourierAndReturnLoginPasswordFirstName();
        String login = courierData.get(0);
        String password = courierData.get(1);
        String idCourier = scooterCourier.getIdCourier(login, password);

        //Отправляем запрос на удаление
        given()
                .delete(ENDPOINT, idCourier) // отправка DELETE-запроса
                .then().assertThat().body("ok", equalTo(true))
                .statusCode(200); // проверка, что сервер вернул код 200
    }

    @Test
    @DisplayName("Удаление курьера без указания ID")
    public void deleteCourierWithoutIdTest(){
        //Отправляем запрос на удаление
        given()
                .delete(ENDPOINT, "") // отправка DELETE-запроса
                .then().assertThat().body("message", equalTo("Недостаточно данных для удаления курьера"))
                .statusCode(400); // проверка, что сервер вернул код 400
    }

    @Test
    @DisplayName("Удаление курьера с указанием несуществующего ID")
    public void deleteCourierWithDefunctIdTest(){
        //Отправляем запрос на удаление
        given()
                .delete(ENDPOINT, "2100483647") // отправка DELETE-запроса
                .then().assertThat().body("message", equalTo("Курьера с таким id нет"))
                .statusCode(404); // проверка, что сервер вернул код 404
    }
}
