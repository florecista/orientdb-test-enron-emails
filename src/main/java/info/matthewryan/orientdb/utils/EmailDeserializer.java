package info.matthewryan.orientdb.utils;


import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class EmailDeserializer implements JsonDeserializer<Email> {
    private final static String FROM_KEY = "FROM";
    private final static String TO_KEY = "TO";
    private final static String CC_KEY = "CC";
    private final static String CONTENT_TYPE_KEY = "CONTENT_TYPE_KEY";
    private final static String SUBJECT_KEY = "SUBJECT";
    private final static String IS_TREE_KEY = "IS_TREE";

    private Map<String, String> propertyMap;

    public EmailDeserializer(Map<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }

    @Override
    public Email deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        Email email = new Email();

        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        final JsonElement jsonFrom = jsonObject.get(propertyMap.get(FROM_KEY));
        final String from = jsonFrom.getAsString();
        email.setFrom(from);

        JsonElement toObject = jsonObject.get(propertyMap.get(TO_KEY));
        if(toObject != null) {
            final JsonArray jsonToArray = toObject.getAsJsonArray();
            final String[] recipients = new String[jsonToArray.size()];
            for (int i = 0; i < recipients.length; i++) {
                final JsonElement jsonRecipient = jsonToArray.get(i);
                recipients[i] = jsonRecipient.getAsString();
            }
            email.setTo(recipients);
        }

        JsonElement ccObject = jsonObject.get(propertyMap.get(CC_KEY));
        if(ccObject != null) {
            final JsonArray jsonCcArray = ccObject.getAsJsonArray();
            final String[] ccRecipients = new String[jsonCcArray.size()];
            for (int i = 0; i < ccRecipients.length; i++) {
                final JsonElement ccRecipient = jsonCcArray.get(i);
                ccRecipients[i] = ccRecipient.getAsString();
            }
            email.setTo(ccRecipients);
        }

        JsonElement contentTypeObject = jsonObject.get(propertyMap.get(CONTENT_TYPE_KEY));
        if(contentTypeObject != null) {
            final String contentTypeString = contentTypeObject.getAsString();
            email.setContentType(contentTypeString);
        }

        final String subjectString = jsonObject.get(propertyMap.get(SUBJECT_KEY)).getAsString();
        email.setSubject(subjectString);

        email.setContentType(getContent(jsonObject, Boolean.parseBoolean(propertyMap.get(IS_TREE_KEY))));

        return email;
    }

    private String getContent(JsonObject jsonObject, boolean isTree) {
        if(isTree) {
            final JsonArray jsonPartsObject = jsonObject.getAsJsonArray("parts");
            for(final JsonElement contentElement : jsonPartsObject) {
                return ((JsonObject)contentElement).get("content").toString();
            }
        }
        else {
            JsonElement textObject = jsonObject.get("text");
            if(textObject != null) {
                final String textObjectString = textObject.getAsString();
                return textObjectString;
            }
        }

        return "";
    }
}