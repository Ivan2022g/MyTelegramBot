package org.example.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Dmitry
 */
public final class SettingsReader {
   private final String settingsFileName = "Settings.json";
    private static final String botUsername;
    private static final String botToken;
    static {
        ObjectMapper mapper = new ObjectMapper();
        File settingsFile = new File("src/main/java/org/example/Settings.json");
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<>() {};

        HashMap<String,Object> o;
        try {
            o = mapper.readValue(settingsFile, typeRef);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        botUsername = o.get("BotUsername").toString().trim();
        botToken = o.get("BotToken").toString().trim();
    }

    public static String getBotToken() {
        return botToken;
    }

    public static String getBotUsername() {
        return botUsername;
    }
}
