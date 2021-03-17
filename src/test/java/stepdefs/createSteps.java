package stepdefs;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.То;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import service.UserService;
import utils.Parser;

public class createSteps {

    UserService userService = new UserService();
    JSONObject user;
    Response response;
    String userName;
    JSONCompareMode compareMode;

    @Дано("API на сайте {string}")
    public void createContext(String url) {
        userService.createContext(url);
    }

    @И("данные пользователя {string}")
    public void createUser(String path) {
        user = Parser.parseJSONFile("src/test/resources/" + path);
    }

    @Когда("отправляется запрос на создание пользователя")
    public void sendRequest() {
       response =  userService.addUserRequest(user.toString());
    }

    @То("возвращается ответ со статусом {string}")
    public void checkResponse(String responseCode) {
        System.out.println(response.jsonPath().get("code"));
        Assert.assertEquals(Integer.valueOf(responseCode), response.jsonPath().get("code"));
    }

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

    @И("имя пользователя через атрибут {string}")
    public void setUserName(String attribute) {
        try {
            userName = user.getString(attribute);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(userName);
    }

    @Когда("отправляется запрос на получение сведений о пользователе")
    public void getUserByName() {
        response = userService.getUserRequest(userName);
    }

    @И("проверяем полное совпадение")
    public void strictMode() {
        compareMode = JSONCompareMode.STRICT;
    }

    @И("проверяем частичное совпадение")
    public void lenientMode() {
        compareMode = JSONCompareMode.LENIENT;
    }

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
