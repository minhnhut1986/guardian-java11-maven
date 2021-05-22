package core.api;

import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import core.Common;
import core.Config;
import core.Log;
import core.PropertyValues;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.SSLConfig.sslConfig;

public class API {

    public static Response postJson(String url, String jsonBody) {
        Log.debug("Send POST with Json to URL : " + url);
        Log.debug("JsonBody : " + jsonBody);
        Response response = given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(url);

        return response;
    }

    public static Response postWithoutBody(String url) {
        Log.debug("Send POST to URL : " + url);
        Response response = given()
                .contentType("application/json")
                .post(url);

        return response;
    }

    public static Response uploadFile(String url, String filePath) {
        Log.debug("Upload file to URL : " + url);
        Log.debug("File path : " + filePath);
        Response response = given()
                .contentType("multipart/form-data")
                .multiPart("file", new File(filePath))
                .when()
                .post(url);

        return response;
    }

    public static Response putJson(String url, String jsonBody) {
        Log.debug("Send PUT with Json to URL : " + url);
        Log.debug("JsonBody: " + jsonBody);
        Response response = given()
                .contentType("application/json")
                .body(jsonBody)
                .put(url);

        return response;
    }

    public static Response putJson(String url) {
        Log.debug("Send PUT to URL : " + url);
        Response response = given()
                .contentType("application/json")
                .put(url);

        return response;
    }

    public static Response deleteJson(String url, String jsonBody) {
        Log.debug("Send DELETE with Json to URL : " + url);
        Log.debug("JsonBody: " + jsonBody);
        Response response = given()
                .contentType("application/json")
                .body(jsonBody)
                .delete(url);

        return response;
    }

    public static Response delete(String url) {
        Log.debug("Send DELETE to URL : " + url);
        Response response = given()
                .contentType("application/json")
                .delete(url);

        return response;
    }

    public static Response get(String url) {
        Log.debug("Send GET to URL : " + url);
        Response response = given()
                .contentType("application/json")
                .get(url);

        return response;
    }

    public static List<String> getListString(String responseBody, String searchName) {
        JsonPath jsonPath = new JsonPath(responseBody);
        List<String> listString = jsonPath.getList(searchName, String.class);
        for (int i = 0; i < listString.size(); i++) {
//            Log.debug("Value of " + searchName + " [" + i + "] : " + listString.get(i));
        }
        return listString;
    }

    public static List<Integer> getListInt(String responseBody, String searchName) {
        JsonPath jsonPath = new JsonPath(responseBody);
        List<Integer> listInt = jsonPath.getList(searchName);
        for (int i = 0; i < listInt.size(); i++) {
//            Log.debug("Value of " + searchName + " [" + i + "] : " + listInt.get(i));
        }
        return listInt;
    }

    public static String getString(String responseBody, String searchName) {
        JsonPath jsonPath = new JsonPath(responseBody);
        String temp = jsonPath.getString(searchName);
        Log.debug("Value of " + searchName + " : " + temp);
        return temp;
    }

    public static Boolean getBoolean(String responseBody, String searchName) {
        JsonPath jsonPath = new JsonPath(responseBody);
        Boolean temp = jsonPath.getBoolean(searchName);
        Log.debug("Value of " + searchName + " : " + temp);
        return temp;
    }

    public static Integer getInt(String responseBody, String searchName) {
        JsonPath jsonPath = new JsonPath(responseBody);
        Integer temp = jsonPath.getInt(searchName);
        Log.debug("Value of " + searchName + " : " + temp);
        return temp;
    }

    public static Long getLong(String responseBody, String searchName) {
        JsonPath jsonPath = new JsonPath(responseBody);
        Long temp = jsonPath.getLong(searchName);
        Log.debug("Value of " + searchName + " : " + temp);
        return temp;
    }

    public static void restAssuredConfig(Boolean allowAllCert) {
        if (allowAllCert) {
            //Allow all https certificates
            RestAssured.config = new RestAssuredConfig().encoderConfig(
                    encoderConfig().defaultContentCharset("UTF-8")).sslConfig(sslConfig().allowAllHostnames()).
                    sslConfig(sslConfig().relaxedHTTPSValidation());
        }
    }

    public static boolean isStatusCode200(int statusCode) {

        Log.debug("Status code: " + statusCode);
        if(statusCode == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isResponseTimeLessThan(Response response, long maxResponseTime) {

        if (response.time() < maxResponseTime) {
            return true;
        } else {
            Log.error("Response time more than " + maxResponseTime + " milliSecond");
            return false;
        }
    }

    public static String getLuckySpinHostAPI(PropertyValues propertyValues) {
        return propertyValues.getProperty(Config.globalEnvironment + "." + "lucky.spin.host");
    }

    public static String getQcReportHostAPI(PropertyValues propertyValues) {
        return propertyValues.getProperty(Config.globalEnvironment + "." + "qc.report.host");
    }


    public static void compareJsonString(String expectedJsonString, String actualJsonString) {
        JsonParser jsonParser = new JsonParser();
        Assert.assertEquals(jsonParser.parse(expectedJsonString), jsonParser.parse(actualJsonString));
    }

    public static boolean compareResponseBodyAndDataInDB(Map<String, List<String>> mapDataInDB,
                                                   String jsonResponseBody, List<String> listResponsePath) {
        boolean isEqual = true;
        List<String> listDataInResponse, listDataInDB;
        String responseKey, responsePath;

        Log.info("Compare response body and data in DB");
        for (Map.Entry<String, List<String>> entry : mapDataInDB.entrySet()) {
            responseKey = entry.getKey();
            responsePath = getResponsePath(listResponsePath, responseKey);
            listDataInResponse = API.getListString(jsonResponseBody, responsePath);
            Log.debug("Get list data in response: " + listDataInResponse);
            listDataInDB = entry.getValue();
            Log.debug("Get list data in database: " + listDataInDB);

            Log.debug("Compare field : " + responseKey);
            for (int i = 0; i < listDataInDB.size(); i++) {
                if(!Common.compareTwoListString(listDataInResponse, listDataInDB)) {
                    isEqual = false;
                    break;
                }
            }
        }

        return isEqual;
    }

    private static String getResponsePath(List<String> listResponsePath, String expectedResponseKey) {
        for (String responsePath : listResponsePath) {
            if (responsePath.equals(expectedResponseKey)) {
                Log.debug("Get response path : " + responsePath);
                return responsePath;
            }
        }
        return null;
    }

    private static boolean validateRequestJsonBody(String expectedRequestJsonBody, String actualRequestJsonBody) {
        boolean isEqual = Common.compareJsonStructure(
                expectedRequestJsonBody,
                actualRequestJsonBody,
                new ArrayList<>(),
                false);
        if (!isEqual) {
            Log.error("Request json structure is changed");
            return false;
        } else {
            Log.highlight("Request json structure is same");
            return true;
        }
    }

    private static boolean validateRequestJsonBodyRequiredKeyOnly(String expectedRequestJsonBody,
                                                                 String actualRequestJsonBody,
                                                                 List<String> listRequiredKey) {
        if (listRequiredKey.size() == 0){
            Log.highlight("There are no request required key");
            return true;
        }

        boolean isEqual = Common.compareJsonStructure(
                expectedRequestJsonBody,
                actualRequestJsonBody,
                listRequiredKey,
                true);
        if (!isEqual) {
            Log.error("Request json structure of required key is changed");
            return false;
        } else {
            Log.highlight("Request json structure of required key is same");
            return true;
        }
    }

    private static boolean validateResponseJsonBody(String expectedResponseJsonBody, String actualResponseJsonBody) {
        boolean isEqual = Common.compareJsonStructure(
                expectedResponseJsonBody,
                actualResponseJsonBody,
                new ArrayList<>(),
                false);
        if (!isEqual) {
            Log.error("Response body json structure not same expectation");
            return false;
        } else {
            Log.highlight("Response body json structure is same");
            return true;
        }
    }

    private static boolean validateResponseJsonBodyRequiredKeyOnly(String expectedResponseJsonBody, String actualResponseJsonBody,
                                                                  List<String> listRequiredKey) {
        if (listRequiredKey.size() == 0){
            Log.highlight("There are no response required key");
            return true;
        }

        boolean isEqual = Common.compareJsonStructure(
                expectedResponseJsonBody,
                actualResponseJsonBody,
                listRequiredKey,
                true);
        if (!isEqual) {
            Log.error("Response body json structure of required key not same expectation");
            return false;
        } else {
            Log.highlight("Response body json structure of required key is same");
            return true;
        }
    }

    public static void validateAPIStructure(String expectedResponseBody, String actualResponseBody, List<String> listResponseRequiredKey) {
        boolean isValidateFail = false;

        if (!API.validateResponseJsonBody(expectedResponseBody, actualResponseBody)) {
            isValidateFail = true;
        }

        if (!API.validateResponseJsonBodyRequiredKeyOnly(expectedResponseBody, actualResponseBody, listResponseRequiredKey)) {
            isValidateFail = true;
        }

        if (isValidateFail) {
            Log.errorAndStop("Validate api structure error!");
        } else {
            Log.highlight("Structure of two apis are equal!");
        }
    }

    public static void validateAPIStructure(String expectedRequestJsonBody, String actualRequestJsonBody, List<String> listRequestRequiredKey,
                                            String expectedResponseBody, String actualResponseBody, List<String> listResponseRequiredKey) {
        boolean isValidateFail = false;

        if (!validateRequestJsonBody(expectedRequestJsonBody, actualRequestJsonBody)) {
            isValidateFail = true;
        }

        if (!validateRequestJsonBodyRequiredKeyOnly(expectedRequestJsonBody, actualRequestJsonBody, listRequestRequiredKey)) {
            isValidateFail = true;
        }

        if (!validateResponseJsonBody(expectedResponseBody, actualResponseBody)) {
            isValidateFail = true;
        }

        if (!validateResponseJsonBodyRequiredKeyOnly(expectedResponseBody, actualResponseBody, listResponseRequiredKey)) {
            isValidateFail = true;
        }

        if (isValidateFail) {
            Log.errorAndStop("Validate api structure error!");
        } else {
            Log.highlight("Structure of two apis are equal!");
        }
    }

    private static boolean compareValueOfRequestJsonBody(String expectedRequestJsonBody, String actualRequestJsonBody) {
        boolean isEqual = Common.compareJson(
                expectedRequestJsonBody,
                actualRequestJsonBody,
                new ArrayList<>(),
                false);
        if (!isEqual) {
            Log.error("Request json is changed");
            return false;
        } else {
            Log.highlight("Request json is equal");
            return true;
        }
    }

    private static boolean compareValueOfRequestJsonBodyRequiredKeyOnly(String expectedRequestJsonBody,
                                                                 String actualRequestJsonBody,
                                                                 List<String> listRequiredKey) {
        if (listRequiredKey.size() == 0){
            Log.highlight("There are no request required key");
            return true;
        }

        boolean isEqual = Common.compareJson(
                expectedRequestJsonBody,
                actualRequestJsonBody,
                listRequiredKey,
                true);
        if (!isEqual) {
            Log.error("Request json of required key is changed");
            return false;
        } else {
            Log.highlight("Request json of required key is same");
            return true;
        }
    }

    private static boolean compareValueOfResponseJsonBody(String expectedResponseJsonBody, String actualResponseJsonBody) {
        boolean isEqual = Common.compareJson(
                expectedResponseJsonBody,
                actualResponseJsonBody,
                new ArrayList<>(),
                false);
        if (!isEqual) {
            Log.error("Response body json not same expectation");
            return false;
        } else {
            Log.highlight("Response body json is same");
            return true;
        }
    }

    private static boolean compareValueOfResponseJsonBodyRequiredKeyOnly(String expectedResponseJsonBody,
                                                                         String actualResponseJsonBody,
                                                                        List<String> listRequiredKey) {
        if (listRequiredKey.size() == 0){
            Log.highlight("There are no response required key");
            return true;
        }

        boolean isEqual = Common.compareJson(
                expectedResponseJsonBody,
                actualResponseJsonBody,
                listRequiredKey,
                true);
        if (!isEqual) {
            Log.error("Response body json of required key not same expectation");
            return false;
        } else {
            Log.highlight("Response body json of required key is same");
            return true;
        }
    }

    public static void assertAPI(String expectedResponseBody, String actualResponseBody, List<String> listResponseRequiredKey) {
        boolean isEqual = true;

        if (!API.compareValueOfResponseJsonBody(expectedResponseBody, actualResponseBody)) {
            isEqual = false;
        }

        if (!API.compareValueOfResponseJsonBodyRequiredKeyOnly(expectedResponseBody, actualResponseBody, listResponseRequiredKey)) {
            isEqual = false;
        }

        if (isEqual) {
            Log.highlight("Two apis are equal!");
        } else {
            Log.errorAndStop("Two apis are not equal!");
        }
    }

    public static void assertResponseAPIRequiredKeyOnly(String expectedResponseBody, String actualResponseBody,
                                                        List<String> listResponseRequiredKey) {

        API.compareValueOfResponseJsonBody(expectedResponseBody, actualResponseBody);

        if (API.compareValueOfResponseJsonBodyRequiredKeyOnly(expectedResponseBody, actualResponseBody, listResponseRequiredKey)) {
            Log.highlight("Two apis are equal!");
        } else {
            Log.errorAndStop("Two apis are not equal!");
        }
    }

    public static void assertAPI(String expectedRequestJsonBody, String actualRequestJsonBody, List<String> listRequestRequiredKey,
                                   String expectedResponseBody, String actualResponseBody, List<String> listResponseRequiredKey) {
        boolean isEqual = true;

        if (!compareValueOfRequestJsonBody(expectedRequestJsonBody, actualRequestJsonBody)) {
            isEqual = false;
        }

        if (!compareValueOfRequestJsonBodyRequiredKeyOnly(expectedRequestJsonBody, actualRequestJsonBody, listRequestRequiredKey)) {
            isEqual = false;
        }

        if (!compareValueOfResponseJsonBody(expectedResponseBody, actualResponseBody)) {
            isEqual = false;
        }

        if (!compareValueOfResponseJsonBodyRequiredKeyOnly(expectedResponseBody, actualResponseBody, listResponseRequiredKey)) {
            isEqual = false;
        }

        if (isEqual) {
            Log.highlight("Two apis are equal!");
        } else {
            Log.errorAndStop("Two apis are not equal!");
        }
    }

    public static void assertResponseAPIRequiredKeyOnly(String expectedRequestJsonBody, String actualRequestJsonBody, List<String> listRequestRequiredKey,
                                 String expectedResponseBody, String actualResponseBody, List<String> listResponseRequiredKey) {
        boolean isEqual = true;

        if (!compareValueOfRequestJsonBody(expectedRequestJsonBody, actualRequestJsonBody)) {
            isEqual = false;
        }

        if (!compareValueOfRequestJsonBodyRequiredKeyOnly(expectedRequestJsonBody, actualRequestJsonBody, listRequestRequiredKey)) {
            isEqual = false;
        }

        if (!compareValueOfResponseJsonBodyRequiredKeyOnly(expectedResponseBody, actualResponseBody, listResponseRequiredKey)) {
            isEqual = false;
        }

        if (isEqual) {
            Log.highlight("Two apis are equal!");
        } else {
            Log.errorAndStop("Two apis are not equal!");
        }
    }
}
