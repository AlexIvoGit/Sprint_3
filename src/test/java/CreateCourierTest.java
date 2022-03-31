import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Создание курьера")
public class CreateCourierTest extends BaseApiTest{

    private final String ENDPOINT = "api/v1/courier";

    @Test
    @DisplayName("Создание нового курьера")
    public void createNewCourierTest() {
        //Генерируем данные для создания курьера
        List<String> generatedDataCourier = scooterCourier.getListDataCourierForCreate();
        String login = generatedDataCourier.get(0);
        String password = generatedDataCourier.get(1);
        String firstName = generatedDataCourier.get(2);

        //Получаем тело запроса
        String body = scooterCourier.getBodyCourierForCreate(login, password, firstName);

        //Отправляем запрос
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(ENDPOINT);
        response.then().assertThat()
                .body("ok", equalTo(true)) //Успешный запрос возвращает ok: true;
                .and()
                .statusCode(201); //Сравниваем код ответа;
        //Курьер создан;

        // Удаляем созданного курьера
        scooterCourier.deleteCourier(scooterCourier.getIdCourier(login, password));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void createDoubleCourierTest() {

        //Создаем курьера
        List<String> listDataCourier = scooterCourier.registerNewCourierAndReturnLoginPasswordFirstName();
        String login = listDataCourier.get(0);
        String password = listDataCourier.get(1);
        String firstName = listDataCourier.get(2);

        //Получаем тело запроса
        String body = scooterCourier.getBodyCourierForCreate(login, password, firstName);

        try {
            //Отправляем повторный запрос на создание этого же курьера
            Response response = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(body)
                    .when()
                    .post(ENDPOINT);
            response.then().assertThat()
                    .body("message", equalTo("Этот логин уже используется")) //Сравниваем текст ошибки
                    .and()
                    .statusCode(409); //Сравниваем код ответа;
        } finally {
            //Если при выполнении запроса получаем ошибку, то в любом случае удалаяем ранее созданного курьера
            scooterCourier.deleteCourier(scooterCourier.getIdCourier(login, password));
        }
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void createCourierWithoutLoginParamTest() {
        //Подготавливаем тело без логина
        String body = "{\n" +
                "    \"password\": \"1234\",\n" +
                "    \"firstName\": \"saske\"\n" +
                "}";

        //Отправляем запрос на создание курьера
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(ENDPOINT);
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи")) //Сравниваем текст ошибки
                .and()
                .statusCode(400); //Сравниваем код ответа;
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCourierWithoutPasswordParamTest() {
        //Подготавливаем тело без пароля
        String body = "{\n" +
                "    \"login\": \"ninja\",\n" +
                "    \"firstName\": \"saske\"\n" +
                "}";

        //Отправляем запрос на создание курьера
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(ENDPOINT);
        response.then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи")) //Сравниваем текст ошибки
                .and()
                .statusCode(400); //Сравниваем код ответа;
    }
}
