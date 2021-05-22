package taptap.api.luckySpin.jsonData;

import core.JsonDataReader;

import java.util.List;

public class JsonDataPlayAndSpin {

    JsonDataReader dataReader;

    public JsonDataPlayAndSpin(String jsonPath) {
        dataReader = new JsonDataReader(jsonPath);
    }

    public String getLoginRequestJsonBody() {
        return dataReader.getJsonString("login.request.body");
    }

    public String getActualRequestJsonBody() {
        return dataReader.getJsonString("request.actuality.body");
    }

    public String getExpectedRequestJsonBody() {
        return dataReader.getJsonString("request.expectation.body");
    }

    public List<String> getListRequestRequiredKey() {
        return dataReader.getListString("request.required_key");
    }

    public String getExpectedResponseBody() {
        return dataReader.getJsonString("response.body");
    }

    public List<String> getExpectedResponseRequiredKey() {
        return dataReader.getListString("response.required_key");
    }
}
