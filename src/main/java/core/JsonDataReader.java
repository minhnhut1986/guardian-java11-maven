package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.path.json.JsonPath;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JsonDataReader {

    JsonPath jsonPath = null;
    String filePath;

    public JsonDataReader(String filePathJsonData) {
        filePath = System.getProperty("user.dir") + filePathJsonData;
        try {
            jsonPath = new JsonPath(new File(filePath));
            Log.debug("Set data reader success ! Data file path : " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public List<String> getListString(String searchName) {
        String searchKey = Config.globalEnvironment + "." + searchName;
        try {
            List<String> listString = jsonPath.getList(searchKey);
//        for (int i = 0; i < listString.size(); i++) {
//            Log.debug("Get item " + (i + 1) + ": " + listString.get(i));
//        }
            return listString;
        } catch (Exception ex) {
            Log.errorAndStop("Have error when get value of " + searchKey + " in json file.\n"
                    + "If you did not set environment or json path, please set environment or json path first !\n"
                    + "Error : " + ex);
            return null;
        }
    }

    public List<Integer> getListInt(String searchName) {
        String searchKey = Config.globalEnvironment + "." + searchName;
        try {
            List<Integer> listInt = jsonPath.getList(searchKey);
//        for (int i = 0; i < listString.size(); i++) {
//            Log.debug("Get item " + (i + 1) + ": " + listString.get(i));
//        }
            return listInt;
        } catch (Exception ex) {
            Log.errorAndStop("Have error when get value of " + searchKey + " in json file.\n"
                    + "If you did not set environment or json path, please set environment or json path first !\n"
                    + "Error : " + ex);
            return null;
        }
    }

    public String getString(String searchName) {
        String searchKey = Config.globalEnvironment + "." + searchName;
        try {
            String result = jsonPath.getString(searchKey);
            Log.debug("Value of " + searchKey + " : " + result);
            return result;
        } catch (Exception ex) {
            Log.errorAndStop("Have error when get value of " + searchKey + " in json file.\n"
                    + "If you did not set environment or json path, please set environment or json path first !\n"
                    + "Error : " + ex);
            return null;
        }
    }

    public Boolean getBoolean(String searchName) {
        String searchKey = Config.globalEnvironment + "." + searchName;
        try {
            Boolean result = jsonPath.getBoolean(searchKey);
            Log.debug("Value of " + searchKey + " : " + result);
            return result;
        } catch (Exception ex) {
            Log.errorAndStop("Have error when get value of " + searchKey + " in json file.\n"
                    + "If you did not set environment or json path, please set environment or json path first !\n"
                    + "Error : " + ex);
            return null;
        }
    }

    public Integer getInt(String searchName) {
        String searchKey = Config.globalEnvironment + "." + searchName;
        try {
            Integer result = jsonPath.getInt(searchKey);
            Log.debug("Value of " + searchKey + " : " + result);
            return result;
        } catch (Exception ex) {
            Log.errorAndStop("Have error when get value of " + searchKey + " in json file.\n"
                    + "If you did not set environment or json path, please set environment or json path first !\n"
                    + "Error : " + ex);
            return null;
        }
    }

    public Long getLong(String searchName) {
        String searchKey = Config.globalEnvironment + "." + searchName;
        try {
            Long result = jsonPath.getLong(searchKey);
            Log.debug("Value of " + searchKey + " : " + result);
            return result;
        } catch (Exception ex) {
            Log.errorAndStop("Have error when get value of " + searchKey + " in json file.\n"
                    + "If you did not set environment or json path, please set environment or json path first !\n"
                    + "Error : " + ex);
            return null;
        }
    }

    public String getJsonString(String searchName) {
        String searchKey = Config.globalEnvironment + "." + searchName;
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            String result = gson.toJson(jsonPath.getJsonObject(searchKey), Map.class);
            Log.debug("Value of " + searchKey + " : " + result);
            return result;
        } catch (Exception ex) {
            Log.errorAndStop("Have error when get value of " + searchKey + " in json file.\n"
                    + "If you did not set environment or json path, please set environment or json path first !\n"
                    + "Error : " + ex);
            return null;
        }
    }
}
