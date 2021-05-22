package taptap.api.luckySpin.test;

import core.Config;
import core.api.APIBaseTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import taptap.api.luckySpin.actions.LoginAction;
import taptap.api.luckySpin.actions.PlayAndSpinAction;
import taptap.api.luckySpin.jsonData.JsonDataPlayAndSpin;

public class PlayAndSpin extends APIBaseTest {

    JsonDataPlayAndSpin jsonData;

    LoginAction loginAction;
    PlayAndSpinAction playAndSpinAction;

    int maxSpinTurn = 3;

    @BeforeTest
    public void initTest() {
        loginAction = new LoginAction();
        playAndSpinAction = new PlayAndSpinAction();
    }

    @AfterMethod
    public void deleteNewCollection() {
        if (Config.isCleanDataTest) {
        }
    }

    @Test
    public void playAndSpinSuccess() {

        jsonData = new JsonDataPlayAndSpin("/data/json/api/play-and-spin-success.json");

        String accessToken = loginAction.getAccessToken(jsonData.getLoginRequestJsonBody());

        boolean isHaveTurn;
        for (int i=0; i<maxSpinTurn ; i++) {

            isHaveTurn = playAndSpinAction.playAndSpinSuccess(
                jsonData.getActualRequestJsonBody(),
                accessToken,
                jsonData.getExpectedResponseBody(),
                jsonData.getExpectedResponseRequiredKey()
            );

            if (!isHaveTurn) break;
        }
    }
}
