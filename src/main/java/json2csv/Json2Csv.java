package json2csv;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.opencsv.CSVWriter;

public class Json2Csv {

    
    public static void main(String[] args) throws Exception {
        InputStream fis = new FileInputStream(Constants.JSON_FILE);
        FileWriter outputfile = new FileWriter(Constants.CSV_FILE);

        JsonReader reader = Json.createReader(fis);
        CSVWriter writer = new CSVWriter(outputfile);
        
        JsonArray jsonArray = reader.readArray();
        
        reader.close();
        writer.writeNext(Constants.HEADER);

        for (JsonValue jsonValue : jsonArray) {
            JsonObject jsonObject = jsonValue.asJsonObject();

            String column1 = jsonObject.getString(Constants.ROOT_ELEMENT);
            if(jsonObject.containsKey("distinctValues"))
            {
                JsonArray column2 =jsonObject.getJsonObject("distinctValues").getJsonArray("buckets");
                for (JsonValue distinctValue : column2) {
                    String col2value = distinctValue.asJsonObject().getString("key");
                    int count = distinctValue.asJsonObject().getInt(Constants.COUNT_FIELD);
                    String[] data={column1,col2value,String.valueOf(count)};
                    writer.writeNext(data);
                
                }
            }
            else {
                int column2 = jsonObject.getInt(Constants.COUNT_FIELD);
                String[] data={column1,String.valueOf(column2)};
                writer.writeNext(data);
                
            }
            

            
        }
        writer.close();
    }
}