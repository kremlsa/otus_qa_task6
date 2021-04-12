package stepdefs;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.То;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.Assert;
import service.UserService;
import utils.Parser;

public class CreateSteps {

    UserService userService = new UserService();
    JSONObject user;
    Response response;
    String userName;
    JSONCompareMode compareMode;
    private final String PATH_TO_DATA = "src/test/testData/";

    @Step("API на сайте \"{string}\"")
    @Дано("API на сайте {string}")
    public void createContext(String url) {
        userService.createContext(url);
    }

    @Step(("данные пользователя \"{string}\""))
    @И("данные пользователя {string}")
    public void createUser(String path) {
        user = Parser.parseJSONFile(PATH_TO_DATA + path);
    }

    @Step("отправляется запрос на создание пользователя")
    @Когда("отправляется запрос на создание пользователя")
    public void sendRequest() {
       response =  userService.addUserRequest(user.toString());
    }

    @Step("возвращается ответ со статусом \"{string}\"")
    @То("возвращается ответ со статусом {string}")
    public void checkResponse(String responseCode) {
        Assert.assertEquals(Integer.valueOf(responseCode), response.jsonPath().get("code"));
    }

    @Step("\"{string}\" пользователя равен указанному в файле \"{string}\"")
    @И("{string} пользователя равен указанному в файле {string}")
    public void checkUserId(String attribute,  String filePath) {
        Integer userId = null;
        try {
            userId = user.getInt(attribute);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(userId.toString(), response.jsonPath().get("message"));
    }

    @Step("имя пользователя через атрибут \"{string}\"")
    @И("имя пользователя через атрибут {string}")
    public void setUserName(String attribute) {
        try {
            userName = user.getString(attribute);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(userName);
    }

    @Step("отправляется запрос на получение сведений о пользователе")
    @Когда("отправляется запрос на получение сведений о пользователе")
    public void getUserByName() {
        response = userService.getUserRequest(userName);
    }

    @Step("проверяем полное совпадение")
    @И("проверяем полное совпадение")
    public void strictMode() {
        compareMode = JSONCompareMode.STRICT;
    }

    @Step("проверяем частичное совпадение")
    @И("проверяем частичное совпадение")
    public void lenientMode() {
        compareMode = JSONCompareMode.LENIENT;
    }

    @Step("данные в файле и ответе с сайта совпадают")
    @То("данные в файле и ответе с сайта совпадают")
    public void checkDataInResponse() {
        String responseJsonString = response.body().asString();
        try {
            JSONAssert.assertEquals(user.toString(), responseJsonString, compareMode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
