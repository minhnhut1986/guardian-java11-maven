package core;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.List;

public class CSVDataReader {

    public static Object[][] getDataFromCSVFile(String path) throws Exception {
        List<String[]> data = readAllCSVFile(path);
        return convertListArrayTo2DimensionalArray(data);
    }

    public static List<String[]> readAllCSVFile(String path) throws Exception {
        Reader reader = new FileReader(path);

        CSVReader csvReader = new CSVReader(reader);
        List<String[]> result = csvReader.readAll();

        reader.close();
        csvReader.close();
        return result;
    }

    public static Object[][] convertListArrayTo2DimensionalArray(List<String[]> data) {
        Object[][] tempData = new Object[data.size() - 1][data.get(0).length];
        for (int rowNumber = 1; rowNumber < data.size(); rowNumber++) {
            for (int columnNumber = 0; columnNumber < data.get(rowNumber).length; columnNumber++) {
                tempData[rowNumber - 1][columnNumber] = data.get(rowNumber)[columnNumber];
            }
        }
        return tempData;
    }

    public static void writeData(String filePath, List<String[]> listData)
    {
        File file = new File(filePath);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputFile);

            writer.writeAll(listData);

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
