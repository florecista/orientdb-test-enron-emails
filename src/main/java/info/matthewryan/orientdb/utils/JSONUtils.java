package info.matthewryan.orientdb.utils;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONUtils {
    public List<Email> parseEnronEmails(String file, Map<String, String> propertyMap) {
        List<Email> emails = new ArrayList<>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Email.class, new EmailDeserializer(propertyMap));
        Gson gson = gsonBuilder.create();

        try {
            InputStream is = this.getClass().getResourceAsStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                JsonElement element = new JsonParser().parse(line);
                JsonObject jsonObject = element.getAsJsonObject();
                Email email = gson.fromJson(jsonObject, Email.class);
                emails.add(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emails;
    }

    public List<Email> parseEnronEmails(InputStream is, Map<String, String> propertyMap) {
        List<Email> emails = new ArrayList<>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Email.class, new EmailDeserializer(propertyMap));
        Gson gson = gsonBuilder.create();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                JsonElement element = new JsonParser().parse(line);
                JsonObject jsonObject = element.getAsJsonObject();
                Email email = gson.fromJson(jsonObject, Email.class);
                emails.add(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emails;
    }
}
