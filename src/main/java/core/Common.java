package core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Common {

    public static List<String> convertListIntToListString(List<Integer> oldListInt) {
        List<String> newListString = new ArrayList<>(oldListInt.size());
        for (Integer myInt : oldListInt) {
            newListString.add(String.valueOf(myInt));
        }

        return newListString;
    }

    public static List<Integer> convertListStringToListInt(List<String> oldListString) {
        List<Integer> newListInt = new ArrayList<>(oldListString.size());
        for (String myString : oldListString) {
            newListInt.add(Integer.valueOf(myString));
        }

        return newListInt;
    }

    public static List<String> getListFromCSVData(String data) {
        List<String> newList;

        if (data.contains(",")) {
            String[] arrTemp = data.split(",");
            newList = Arrays.asList(arrTemp);
        } else {
            newList = new ArrayList<>();
            newList.add(data);
        }

        return newList;
    }

    public static List<String> getListFromCSVData(String data, boolean isRemoveBlank) {
        List<String> newList;

        if (isRemoveBlank) {
            data = data.replaceAll(" ", "");
        }

        if (data.contains(",")) {
            String[] arrTemp = data.split(",");
            newList = Arrays.asList(arrTemp);
        } else {
            newList = new ArrayList<>();
            newList.add(data);
        }

        return newList;
    }

    public static String checkDataFilePathFromSysEnv(String filePath) {
        String systemDataFilePath;

        systemDataFilePath = System.getProperty("data.path");
        Log.info("System data file path : " + systemDataFilePath);
        if (systemDataFilePath != null) {
            return systemDataFilePath;
        } else {
            return filePath;
        }
    }

    public static boolean compareTwoListString(List<String> list1, List<String> list2) {

        boolean isEqual = true;

        if (list1.size() != list2.size()) {
            Log.debug("Size of list 1 : " + list1.size() + " | Size of list 2 : " + list2.size());
            return false;
        } else if (list2.size() == 0 || list1.size() == 0) {
            Log.debug("Size of list 1 : " + list1.size() + " | Size of list 2 : " + list2.size());
            return true;
        }

        for (int i = 0; i < list2.size(); i++) {
            if (list1.get(i) == null) {
                if (list2.get(i) != null) {
                    Log.debug("[" + i + "] => List 1 : " + list1.get(i) + " | List 2 va : " + list2.get(i));
                    isEqual = false;
                }
            } else if (!list1.get(i).equalsIgnoreCase(list2.get(i))) {
                Log.debug("[" + i + "] => List 1 : " + list1.get(i) + " | List 2 : " + list2.get(i));
                isEqual = false;
            }
        }

        return isEqual;
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }

    public static List<Object> convertIteratorToList(Iterator iterator) {
        List<Object> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        return actualList;
    }

    public static boolean compareTwoObjectType(Object object, Object expectedObject) {

        // Not compare Object type = null
        if (object.getClass().toString().toLowerCase().contains("null") ||
                expectedObject.getClass().toString().toLowerCase().contains("null")) {
            return true;
        }

        if (object.getClass().toString().equalsIgnoreCase(expectedObject.getClass().toString())) {
            return true;
        } else {
            Log.error("Actual object class : " + object.getClass());
            Log.error("Expected object class : " + expectedObject.getClass());
            return false;
        }
    }

    static boolean isStopFindKey = false;

    public static boolean findJsonKey(JSONObject json, String key) {
        if (isStopFindKey != false) isStopFindKey = false;

        findJsonKeyRecursive(json, key);

        return isStopFindKey;
    }

    private static boolean findJsonKeyRecursive(JSONObject json, String key) {

        boolean exists = json.has(key);
        Iterator<?> keys;
        String nextKeys;

        if (isStopFindKey) return true;

        if (!exists) {
            keys = json.keys();
            while (keys.hasNext()) {
                nextKeys = (String) keys.next();

                try {
                    if (json.get(nextKeys) instanceof JSONObject) {

                        if (checkJsonKeyHasNestedJson(json, nextKeys)) {
                            findJsonKeyRecursive(json.getJSONObject(nextKeys), key);
                        }

                    } else if (json.get(nextKeys) instanceof JSONArray) {
                        JSONArray jsonArray = json.getJSONArray(nextKeys);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String jsonArrayString = jsonArray.get(i).toString();
                            if (isJSONValid(jsonArrayString) == 1) {
                                JSONObject innerJson = new JSONObject(jsonArrayString);
                                findJsonKeyRecursive(innerJson, key);
                            }

                        }
                    }

                } catch (Exception ex) {
                    Log.errorAndStop("Have error when finding key: " + ex);
                }
            }

        } else {
            isStopFindKey = true;
            return true;
        }

        return false;
    }

//    public static boolean compareTotalJsonKeys(JSONObject json, JSONObject expectedJson) {
//
//    }

    static Boolean jsonStructureEqual = true;

    public static boolean compareJsonStructure(String expectedJsonString, String actualJsonString,
                                               List<String> listRequiredKey, boolean onlyCheckRequiredKey) {
        if (jsonStructureEqual != true) jsonStructureEqual = true;

        int resultExpectedJsonValid = isJSONValid(expectedJsonString);
        int resultActualJsonValid = isJSONValid(actualJsonString);
//        Log.debug("resultExpectedJsonValid : " + resultExpectedJsonValid);
//        Log.debug("resultActualJsonValid : " + resultActualJsonValid);

        if (resultExpectedJsonValid == -1 || resultActualJsonValid == -1) {
            Log.error("There are invalid json!");
            return false;
        }

        if (resultExpectedJsonValid == 0 && resultActualJsonValid == 0) {
            Log.debug("Two json are empty");
            return true;
        }

        if (resultExpectedJsonValid == resultActualJsonValid) {

            JSONObject expectedJson = new JSONObject(expectedJsonString);
            JSONObject actualJson = new JSONObject(actualJsonString);

            if (onlyCheckRequiredKey) {
                compareJsonStructureRecursive(expectedJson, actualJson, listRequiredKey);
            } else {
                compareJsonStructureRecursive(expectedJson, actualJson);
            }

        } else {
            return false;
        }

        return jsonStructureEqual;
    }

    private static void compareJsonStructureRecursive(JSONObject expectedJson, JSONObject actualJson) {

        Iterator<?> keys;
        String expectedKey, jsonArrayStringExpected, jsonArrayStringActual;
        JSONArray jsonArray, jsonArrayActual;
        Object objValue, objActualValue;

        keys = expectedJson.keys();

        while (keys.hasNext()) {
            expectedKey = (String) keys.next();
//            Log.debug("Start compare key : " + expectedKey);

            if (findJsonKey(actualJson, expectedKey)) {
                try {
                    objValue = expectedJson.get(expectedKey);
                    objActualValue = actualJson.get(expectedKey);

//                    Log.debug("Value of key '" + expectedKey + "' => " +
//                            "[expectedJson : " + objValue + ", actualJson : " + objActualValue + "]");

                    if (objValue instanceof JSONObject) {

                        if (objActualValue instanceof JSONObject) {

                            compareJsonStructureRecursive(
                                    expectedJson.getJSONObject(expectedKey),
                                    actualJson.getJSONObject(expectedKey)
                            );

                        } else {
                            Log.error("Value of key '" + expectedKey + "' is not a JSONObject in actual json");
                            jsonStructureEqual = false;
                        }

                    } else if (objValue instanceof JSONArray) {

                        if (objActualValue instanceof JSONArray) {

//                            Log.debug("Start get json array");
                            jsonArray = expectedJson.getJSONArray(expectedKey);
                            jsonArrayActual = actualJson.getJSONArray(expectedKey);
//                            Log.debug("Get json array success: " + jsonArray.toString());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonArrayStringExpected = jsonArray.get(i).toString();
                                jsonArrayStringActual = jsonArrayActual.get(i).toString();

                                int resultJsonValidOfJAStringExpected = isJSONValid(jsonArrayStringExpected);
                                int resultJsonValidOfJAStringActual = isJSONValid(jsonArrayStringActual);

                                if (resultJsonValidOfJAStringExpected != resultJsonValidOfJAStringActual) {
                                    jsonStructureEqual = false;
                                    break;
                                }

                                if (resultJsonValidOfJAStringExpected == 1 && resultJsonValidOfJAStringActual == 1) {

                                    JSONObject innerExpectedJson = new JSONObject(jsonArrayStringExpected);
                                    JSONObject innerActualJson = new JSONObject(jsonArrayStringActual);

                                    compareJsonStructureRecursive(innerExpectedJson, innerActualJson);

                                } else {

                                    objValue = jsonArray.get(i);
                                    objActualValue = jsonArrayActual.get(i);

                                    if (!compareTwoObjectType(objValue, objActualValue)) {
                                        Log.error("Type of two object of key '" + expectedKey + "' are not same! Expected value '" + objValue + "'");
                                        jsonStructureEqual = false;
                                        break;
                                    }

                                    break;

                                    //Compare value
//                                    if (!jsonArray.get(i).equals(jsonArrayActual.get(i))) {
//                                        Log.error("Value of key '" + expectedKey + "' => " +
//                                                "[expectedJson : " + objValue + ", actualJson : " + objActualValue + "]");
//                                        jsonStructureEqual = false;
//                                    }
                                }
                            }
                        } else if (!objActualValue.getClass().toString().toLowerCase().contains("null")) {
                            Log.error("Value of key '" + expectedKey + "' is not a JSONArray in actual json");
                            jsonStructureEqual = false;
                        }

                    } else {

                        if (!compareTwoObjectType(objValue, objActualValue)) {
                            Log.error("Type of two object of key '" + expectedKey + "' are not same! Expected value '" + objValue + "'");
                            jsonStructureEqual = false;
                            break;
                        }

                        //Compare value
//                        if (!objValue.equals(objActualValue)) {
//                            Log.error("Value of key '" + expectedKey + "' => " +
//                                    "[expectedJson : " + objValue + ", actualJson : " + objActualValue + "]");
//                            jsonStructureEqual = false;
//                        }
                    }

                } catch (Exception ex) {
                    Log.errorAndStop("Have error when comparing structure two json : " + ex);
                }

            } else {
                Log.error("Key '" + expectedKey + "' NOT FOUND in actual json");
                jsonStructureEqual = false;
            }

//            Log.highlight("COMPARE KEY '" + expectedKey + "' SUCCESS!");

        }
    }

    private static void compareJsonStructureRecursive(JSONObject expectedJson, JSONObject actualJson,
                                                      List<String> listRequiredKey) {

        Iterator<?> keys;
        String expectedKey, jsonArrayStringExpected, jsonArrayStringActual;
        JSONArray jsonArray, jsonArrayActual;
        Object objValue, objActualValue;

        keys = expectedJson.keys();

        while (keys.hasNext()) {
            expectedKey = (String) keys.next();
//            Log.debug("Start compare key : " + expectedKey);

            if (listRequiredKey.contains(expectedKey)) {
                if (findJsonKey(actualJson, expectedKey)) {
                    try {
                        objValue = expectedJson.get(expectedKey);
                        objActualValue = actualJson.get(expectedKey);

//                    Log.debug("Value of key '" + expectedKey + "' => " +
//                            "[expectedJson : " + objValue + ", actualJson : " + objActualValue + "]");

                        if (objValue instanceof JSONObject) {

                            if (objActualValue instanceof JSONObject) {

                                compareJsonStructureRecursive(
                                        expectedJson.getJSONObject(expectedKey),
                                        actualJson.getJSONObject(expectedKey),
                                        listRequiredKey
                                );

                            } else {
                                Log.error("Value of key '" + expectedKey + "' is not a JSONObject in actual json");
                                jsonStructureEqual = false;
                            }

                        } else if (objValue instanceof JSONArray) {

                            if (objActualValue instanceof JSONArray) {

//                            Log.debug("Start get json array");
                                jsonArray = expectedJson.getJSONArray(expectedKey);
                                jsonArrayActual = actualJson.getJSONArray(expectedKey);
//                            Log.debug("Get json array success: " + jsonArray.toString());

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonArrayStringExpected = jsonArray.get(i).toString();
                                    jsonArrayStringActual = jsonArrayActual.get(i).toString();

                                    int resultJsonValidOfJAStringExpected = isJSONValid(jsonArrayStringExpected);
                                    int resultJsonValidOfJAStringActual = isJSONValid(jsonArrayStringActual);

                                    if (resultJsonValidOfJAStringExpected != resultJsonValidOfJAStringActual) {
                                        jsonStructureEqual = false;
                                        break;
                                    }

                                    if (resultJsonValidOfJAStringExpected == 1 && resultJsonValidOfJAStringActual == 1) {

                                        JSONObject innerExpectedJson = new JSONObject(jsonArrayStringExpected);
                                        JSONObject innerActualJson = new JSONObject(jsonArrayStringActual);

                                        compareJsonStructureRecursive(innerExpectedJson, innerActualJson, listRequiredKey);

                                    } else {

                                        objValue = jsonArray.get(i);
                                        objActualValue = jsonArrayActual.get(i);

                                        if (!compareTwoObjectType(objValue, objActualValue)) {
                                            Log.error("Type of two object of key '" + expectedKey + "' are not same! Expected value '" + objValue + "'");
                                            jsonStructureEqual = false;
                                            break;
                                        }

                                        break;

                                        //Compare value
//                                    if (!jsonArray.get(i).equals(jsonArrayActual.get(i))) {
//                                        Log.error("Value of key '" + expectedKey + "' => " +
//                                                "[expectedJson : " + objValue + ", actualJson : " + objActualValue + "]");
//                                        jsonStructureEqual = false;
//                                    }
                                    }
                                }
                            } else if (!objActualValue.getClass().toString().toLowerCase().contains("null")) {
                                Log.error("Value of key '" + expectedKey + "' is not a JSONArray in actual json");
                                jsonStructureEqual = false;
                            }

                        } else {

                            if (!compareTwoObjectType(objValue, objActualValue)) {
                                Log.error("Type of two object of key '" + expectedKey + "' are not same! Expected value '" + objValue + "'");
                                jsonStructureEqual = false;
                                break;
                            }

                            //Compare value
//                        if (!objValue.equals(objActualValue)) {
//                            Log.error("Value of key '" + expectedKey + "' => " +
//                                    "[expectedJson : " + objValue + ", actualJson : " + objActualValue + "]");
//                            jsonStructureEqual = false;
//                        }
                        }

                    } catch (Exception ex) {
                        Log.errorAndStop("Have error when comparing structure two json : " + ex);
                    }

                } else {
                    Log.error("Key '" + expectedKey + "' NOT FOUND in actual json");
                    jsonStructureEqual = false;
                }
            }

//            Log.highlight("COMPARE KEY '" + expectedKey + "' SUCCESS!");

        }
    }

    static Boolean jsonEqual = true;

    public static boolean compareJson(String expectedJsonString, String actualJsonString,
                                      List<String> listRequiredKey, boolean onlyCheckRequiredKey) {
        if (jsonEqual != true) jsonEqual = true;

        int resultExpectedJsonValid = isJSONValid(expectedJsonString);
        int resultActualJsonValid = isJSONValid(actualJsonString);
//        Log.debug("resultExpectedJsonValid : " + resultExpectedJsonValid);
//        Log.debug("resultActualJsonValid : " + resultActualJsonValid);

        if (resultExpectedJsonValid == -1 || resultActualJsonValid == -1) {
            Log.error("There are invalid json!");
            return false;
        }

        if (resultExpectedJsonValid == 0 && resultActualJsonValid == 0) {
            Log.debug("Two json are empty");
            return true;
        }

        if (resultExpectedJsonValid == resultActualJsonValid) {

            JSONObject expectedJson = new JSONObject(expectedJsonString);
            JSONObject actualJson = new JSONObject(actualJsonString);

            if (onlyCheckRequiredKey) {
                compareJsonRecursive(expectedJson, actualJson, listRequiredKey);
            } else {
                compareJsonRecursive(expectedJson, actualJson);
            }

        } else {
            return false;
        }

        return jsonEqual;
    }

    private static void compareJsonRecursive(JSONObject expectedJson, JSONObject actualJson) {

        Iterator<?> keys;
        String expectedKey, jsonArrayStringExpected, jsonArrayStringActual;
        JSONArray jsonArray, jsonArrayActual;
        Object objValue, objActualValue;

        keys = expectedJson.keys();

        while (keys.hasNext()) {
            expectedKey = (String) keys.next();
//            Log.debug("Start compare key : " + expectedKey);

            if (findJsonKey(actualJson, expectedKey)) {
                try {
                    objValue = expectedJson.get(expectedKey);
                    objActualValue = actualJson.get(expectedKey);

//                    Log.debug("Value of key '" + expectedKey + "' => " +
//                            "[expectedJson : " + objValue + ", actualJson : " + objActualValue + "]");

                    if (objValue instanceof JSONObject) {

                        if (objActualValue instanceof JSONObject) {

                            compareJsonRecursive(
                                    expectedJson.getJSONObject(expectedKey),
                                    actualJson.getJSONObject(expectedKey)
                            );

                        } else {
                            Log.error("Value of key '" + expectedKey + "' is not a JSONObject in actual json");
                            jsonEqual = false;
                        }

                    } else if (objValue instanceof JSONArray) {

                        if (objActualValue instanceof JSONArray) {

//                            Log.debug("Start get json array");
                            jsonArray = expectedJson.getJSONArray(expectedKey);
                            jsonArrayActual = actualJson.getJSONArray(expectedKey);
//                            Log.debug("Get json array success: " + jsonArray.toString());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonArrayStringExpected = jsonArray.get(i).toString();
                                jsonArrayStringActual = jsonArrayActual.get(i).toString();

                                int resultJsonValidOfJAStringExpected = isJSONValid(jsonArrayStringExpected);
                                int resultJsonValidOfJAStringActual = isJSONValid(jsonArrayStringActual);

                                if (resultJsonValidOfJAStringExpected != resultJsonValidOfJAStringActual) {
                                    jsonEqual = false;
                                    break;
                                }

                                if (resultJsonValidOfJAStringExpected == 1 && resultJsonValidOfJAStringActual == 1) {

                                    JSONObject innerExpectedJson = new JSONObject(jsonArrayStringExpected);
                                    JSONObject innerActualJson = new JSONObject(jsonArrayStringActual);

                                    compareJsonRecursive(innerExpectedJson, innerActualJson);

                                } else {

                                    objValue = jsonArray.get(i);
                                    objActualValue = jsonArrayActual.get(i);

                                    if (!objValue.equals(objActualValue)) {
                                        Log.error("Value of key '" + expectedKey + "' => " +
                                                "[expectedJson : " + objValue + ", actualJson : " + objActualValue + "]");
                                        jsonEqual = false;
                                        break;
                                    }

                                    break;

                                }
                            }
                        } else if (!objActualValue.getClass().toString().toLowerCase().contains("null")) {
                            Log.error("Value of key '" + expectedKey + "' is not a JSONArray in actual json");
                            jsonEqual = false;
                        }

                    } else {

                        if (!objValue.equals(objActualValue)) {
                            Log.error("Value of key '" + expectedKey + "' => " +
                                    "[expectedJson : " + objValue + ", actualJson : " + objActualValue + "]");
                            jsonEqual = false;
                            break;
                        }
                    }

                } catch (Exception ex) {
                    Log.errorAndStop("Have error when comparing structure two json : " + ex);
                }

            } else {
                Log.error("Key '" + expectedKey + "' NOT FOUND in actual json");
                jsonEqual = false;
            }

//            Log.highlight("COMPARE KEY '" + expectedKey + "' SUCCESS!");

        }
    }

    private static void compareJsonRecursive(JSONObject expectedJson, JSONObject actualJson,
                                             List<String> listRequiredKey) {

        Iterator<?> keys;
        String expectedKey, jsonArrayStringExpected, jsonArrayStringActual;
        JSONArray jsonArray, jsonArrayActual;
        Object objValue, objActualValue;

        keys = expectedJson.keys();

        while (keys.hasNext()) {
            expectedKey = (String) keys.next();
//            Log.debug("Start compare key : " + expectedKey);

            if (listRequiredKey.contains(expectedKey)) {
                if (findJsonKey(actualJson, expectedKey)) {
                    try {
                        objValue = expectedJson.get(expectedKey);
                        objActualValue = actualJson.get(expectedKey);

//                    Log.debug("Value of key '" + expectedKey + "' => " +
//                            "[expectedJson : " + objValue + ", actualJson : " + objActualValue + "]");

                        if (objValue instanceof JSONObject) {

                            if (objActualValue instanceof JSONObject) {

                                compareJsonRecursive(
                                        expectedJson.getJSONObject(expectedKey),
                                        actualJson.getJSONObject(expectedKey),
                                        listRequiredKey
                                );

                            } else {
                                Log.error("Value of key '" + expectedKey + "' is not a JSONObject in actual json");
                                jsonEqual = false;
                            }

                        } else if (objValue instanceof JSONArray) {

                            if (objActualValue instanceof JSONArray) {

//                            Log.debug("Start get json array");
                                jsonArray = expectedJson.getJSONArray(expectedKey);
                                jsonArrayActual = actualJson.getJSONArray(expectedKey);
//                            Log.debug("Get json array success: " + jsonArray.toString());

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonArrayStringExpected = jsonArray.get(i).toString();
                                    jsonArrayStringActual = jsonArrayActual.get(i).toString();

                                    int resultJsonValidOfJAStringExpected = isJSONValid(jsonArrayStringExpected);
                                    int resultJsonValidOfJAStringActual = isJSONValid(jsonArrayStringActual);

                                    if (resultJsonValidOfJAStringExpected != resultJsonValidOfJAStringActual) {
                                        jsonEqual = false;
                                        break;
                                    }

                                    if (resultJsonValidOfJAStringExpected == 1 && resultJsonValidOfJAStringActual == 1) {

                                        JSONObject innerExpectedJson = new JSONObject(jsonArrayStringExpected);
                                        JSONObject innerActualJson = new JSONObject(jsonArrayStringActual);

                                        compareJsonRecursive(innerExpectedJson, innerActualJson, listRequiredKey);

                                    } else {

                                        objValue = jsonArray.get(i);
                                        objActualValue = jsonArrayActual.get(i);

                                        if (!objValue.equals(objActualValue)) {
                                            Log.error("Value of key '" + expectedKey + "' => " +
                                                    "[expectedJson : " + objValue + ", actualJson : " + objActualValue + "]");
                                            jsonEqual = false;
                                            break;
                                        }

                                        break;

                                    }
                                }
                            } else if (!objActualValue.getClass().toString().toLowerCase().contains("null")) {
                                Log.error("Value of key '" + expectedKey + "' is not a JSONArray in actual json");
                                jsonEqual = false;
                            }

                        } else {

                            if (!objValue.equals(objActualValue)) {
                                Log.error("Value of key '" + expectedKey + "' => " +
                                        "[expectedJson : " + objValue + ", actualJson : " + objActualValue + "]");
                                jsonEqual = false;
                                break;
                            }
                        }

                    } catch (Exception ex) {
                        Log.errorAndStop("Have error when comparing structure two json : " + ex);
                    }

                } else {
                    Log.error("Key '" + expectedKey + "' NOT FOUND in actual json");
                    jsonEqual = false;
                }
            }

//            Log.highlight("COMPARE KEY '" + expectedKey + "' SUCCESS!");

        }
    }

    public static void getJsonKey(JSONObject json, String key) {

        boolean exists = json.has(key);
        Iterator<?> keys;
        String nextKeys;

        if (!exists) {
            keys = json.keys();
            while (keys.hasNext()) {
                nextKeys = (String) keys.next();
                try {
                    if (json.get(nextKeys) instanceof JSONObject) {

                        getJsonKey(json.getJSONObject(nextKeys), key);

                    } else if (json.get(nextKeys) instanceof JSONArray) {
                        JSONArray jsonArray = json.getJSONArray(nextKeys);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String jsonArrayString = jsonArray.get(i).toString();
                            JSONObject innerJson = new JSONObject(jsonArrayString);

                            getJsonKey(innerJson, key);

                        }
                    }

                } catch (Exception e) {
                    Log.errorAndStop("Have error when getting key!");
                }
            }

        } else {
            Log.debug("Get value of key '" + key + "' : " + json.get(key));
        }
    }

    // Result : -1 = invalid | 0 = empty | 1 = valid
    public static int isJSONValid(String jsonString) {
        int result = -1;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.isEmpty())
                result = 0;
            else
                result = 1;

        } catch (JSONException ex) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                if (jsonArray.isEmpty())
                    result = 0;
                else
                    result = 1;

            } catch (JSONException ex1) {
//                Log.info("Json is not valid : " + ex1 );
            }
        }

        return result;
    }

    public static boolean checkJsonKeyHasNestedJson(JSONObject json, String key) {
        String result = "";
        try {
            result = json.getJSONObject(key).toString();
        } catch (JSONException ex) {
            try {
                result = json.getJSONArray(key).toString();
            } catch (JSONException ex1) {
                Log.debug("Result check Json key nested : false");
                return false;
            }
        }

//        Log.debug("Result check Json key nested : " + isJSONValid(result));
        if (isJSONValid(result) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static String addPropertyToJsonString(String jsonBody, String properTy, int value) {

        JsonObject jsonObject = new JsonParser().parse(jsonBody).getAsJsonObject();
        jsonObject.addProperty(properTy, value);

        return jsonObject.toString();
    }

    public static String addPropertyToJsonString(String jsonBody, String properTy, String value) {

        JsonObject jsonObject = new JsonParser().parse(jsonBody).getAsJsonObject();
        jsonObject.addProperty(properTy, value);

        return jsonObject.toString();
    }

    public static List<String> subListString(List<String> listString, int endIndex) {

        List<String> newListString = new ArrayList<>();
        for (int index = 0; index < endIndex; index++) {

            if (index == listString.size()) break;

            newListString.add(listString.get(index));
        }

        return newListString;
    }

    public static List<Integer> subListInteger(List<Integer> listInteger, int endIndex) {

        List<Integer> newListInteger = new ArrayList<>();
        for (int index = 0; index < endIndex; index++) {

            if (index == listInteger.size()) break;

            newListInteger.add(listInteger.get(index));
        }

        return newListInteger;
    }
}
