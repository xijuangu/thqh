package org.example.processserver.Utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;

/**
 * @author PY.Lu
 * @date 2024/10/28
 * @Description
 */
public class CSVFileData {
    public static Integer rowCount(String filePath){
        Integer rowCount = 0;
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            while (csvReader.readNext()!= null){
                rowCount++;
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return rowCount;
    }

}
