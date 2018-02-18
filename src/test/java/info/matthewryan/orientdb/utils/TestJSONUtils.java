package info.matthewryan.orientdb.utils;


import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestJSONUtils {
    @Test
    public void test1() {
        JSONUtils util = new JSONUtils();
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put("FROM", "From");
        propertyMap.put("TO", "To");
        propertyMap.put("CC", "Cc");
        propertyMap.put("CONTENT_TYPE_KEY", "Content-Type");
        propertyMap.put("SUBJECT", "Subject");
        propertyMap.put("IS_TREE", "true");
        List<Email> emails1 = util.parseEnronEmails("/enron2001-06.json", propertyMap);
        assertEquals(emails1.size(), 1650);
        List<Email> emails2 = util.parseEnronEmails("/enron2001-11.json", propertyMap);
        assertEquals(emails2.size(), 9219);
        emails1.addAll(emails2);
        assertEquals(emails1.size(), 10869);
        System.out.println("Number of emails : " + emails1.size());
    }

    @Test
    public void test2() {
        JSONUtils util = new JSONUtils();
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put("FROM", "sender");
        propertyMap.put("TO", "recipients");
        propertyMap.put("CC", "cc");
        propertyMap.put("CONTENT_TYPE_KEY", "ctype");
        propertyMap.put("SUBJECT", "subject");
        propertyMap.put("IS_TREE", "false");
        List<Email> emails = util.parseEnronEmails("/enron.json", propertyMap);
        assertEquals(emails.size(), 5929);
        System.out.println("Number of emails : " + emails.size());
    }
}
