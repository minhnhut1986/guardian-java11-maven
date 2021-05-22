package taptap.api.luckySpin.actions;

import io.restassured.response.Response;
import core.Log;
import core.api.API;
import core.api.APIBaseTest;
import core.api.constant.path.TapTapPath;

import java.util.List;

public class LoginAction extends APIBaseTest {

    protected final String LOGIN_URL;

    private final long maxResponseTime = 1000L;

    public LoginAction() {
        LOGIN_URL = luckySpinHost + TapTapPath.LOGIN_PATH;
    }

    private String login(String jsonBody) {
        Log.debug("Start Login");

        Response response = API.postJson(LOGIN_URL, jsonBody);

        int statusCode = response.statusCode();
        String result = response.body().asString();

        if( API.isStatusCode200(statusCode) && API.isResponseTimeLessThan(response, maxResponseTime) ) {
            Log.highlight("Login success: " + result);
        } else {
            Log.errorAndStop("Login fail: " + result);
        }

        return result;
    }

    public void loginSuccess(String jsonBody, String expectedResponseBody,
                              List<String> listResponseRequiredKey) {
        String result = login(jsonBody);

        API.validateAPIStructure(expectedResponseBody, result, listResponseRequiredKey);

        API.assertResponseAPIRequiredKeyOnly(expectedResponseBody, result, listResponseRequiredKey);
    }

    public String getAccessToken(String jsonBody) {
        String result = login(jsonBody);

        return API.getString(result, "accessToken");
    }
}
