package info.matthewryan.orientdb.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {
    public static List<String[]> loadMemoryFromJSONOneFile(InputStream importFile) {
        List<String[]> result = new ArrayList<>();

        JSONUtils util = new JSONUtils();
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put("FROM", "sender");
        propertyMap.put("TO", "recipients");
        propertyMap.put("CC", "cc");
        propertyMap.put("CONTENT_TYPE_KEY", "ctype");
        propertyMap.put("SUBJECT", "subject");
        propertyMap.put("IS_TREE", "false");
        List<Email> emails = util.parseEnronEmails(importFile, propertyMap);
        for (Email email : emails) {
            String[] record = new String[2];
            record[0] = email.getFrom();
            if(email.getTo().length > 0) {
                String[] toS = email.getTo();
                record[1] = toS[0];
                result.add(record);
            }
        }

        return result;
    }
}
