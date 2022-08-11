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

    static String[] header={ "Position", "Activity", "Count" };
    static String rootElement="key";

    public static void main(String[] args) throws Exception {
        InputStream fis = new FileInputStream(Constants.JSON_FILE);
        FileWriter outputfile = new FileWriter(Constants.CSV_FILE);

        JsonReader reader = Json.createReader(fis);
        CSVWriter writer = new CSVWriter(outputfile);
        
        JsonArray positionArray = reader.readArray();
        
        reader.close();
        writer.writeNext(header);

        for (JsonValue positionValue : positionArray) {
            JsonObject position = positionValue.asJsonObject();

            String positionName = position.getString(rootElement);
            JsonArray activityArray =position.getJsonObject("distinctActivities").getJsonArray("buckets");

            for (JsonValue jsonValue : activityArray) {
                String activity = jsonValue.asJsonObject().getString("key");
                int count = jsonValue.asJsonObject().getInt("doc_count");
                String[] data={positionName,activity,String.valueOf(count)};
                writer.writeNext(data);
            
            }
        }
        writer.close();
    }
}