package taptap.api.luckySpin.actions;

import core.Log;
import core.api.API;
import core.api.APIBaseTest;
import core.api.constant.path.TapTapPath;
import core.api.constant.response.PlayAndSpinResponse;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;

public class PlayAndSpinAction extends APIBaseTest {

    protected final String PLAY_AND_SPIN_URL;

    private final long maxResponseTime = 1000L;

    public PlayAndSpinAction() {
        PLAY_AND_SPIN_URL = luckySpinHost + TapTapPath.PLAY_AND_SPIN_PATH;
    }

    private String playAndSpin(String jsonBody, String accessToken) {
        Log.debug("Start play and spin");

        String url = PLAY_AND_SPIN_URL + "?access_token=" + accessToken;

        Response response = API.postJson(url, jsonBody);

        int statusCode = response.statusCode();
        String result = response.body().asString();

        if (result.contains("Đã hết lượt quay số")) {
            Log.highlight("Status code: " + statusCode);
            Log.highlight("Play and spin success: " + result);
        } else if( API.isStatusCode200(statusCode) && API.isResponseTimeLessThan(response, maxResponseTime) ) {
            Log.highlight("Play and spin success: " + result);
        } else {
            Log.errorAndStop("Play and spin fail: " + result);
        }

        return result;
    }

    public boolean playAndSpinSuccess(String jsonBody, String accessToken, String expectedResponseBody,
                             List<String> listResponseRequiredKey) {
        String actualResponseBody = playAndSpin(jsonBody, accessToken);

        API.validateAPIStructure(expectedResponseBody, actualResponseBody, listResponseRequiredKey);

        if (actualResponseBody.contains("Đã hết lượt quay số")) {
            Log.info("There is no turn to play ! Please come back in tomorrow");
            return false;
        } else {

            int rewardId = API.getInt(actualResponseBody, PlayAndSpinResponse.KEY_REWARD_ID);
            int rewardIndex = API.getInt(actualResponseBody, PlayAndSpinResponse.KEY_REWARD_INDEX);

            Assert.assertTrue(PlayAndSpinResponse.LIST_VALUE_REWARD_ID.contains(rewardId));
            Assert.assertTrue(PlayAndSpinResponse.LIST_VALUE_REWARD_INDEX.contains(rewardIndex));
        }

        return true;
    }
}
