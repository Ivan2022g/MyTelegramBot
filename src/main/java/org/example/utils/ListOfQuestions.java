package org.example.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.CardQuestion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry
 */
public class ListOfQuestions {
    private static List<CardQuestion> lvl1;
    private static List<CardQuestion> lvl2;
    private static List<CardQuestion> lvl3;
    static {
        lvl1 = parseList("lvl1.json");
        lvl2 = parseList("lvl2.json");
        lvl3 = parseList("lvl3.json");
    }

    private static List<CardQuestion> parseList(String fileName) {
        List<CardQuestion> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        File settingsFile = new File("src/main/java/org/example/questions/" + fileName);
        TypeReference<ArrayList<CardQuestion>> typeRef = new TypeReference<>() {};
        try {
            list = mapper.readValue(settingsFile, typeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static List<CardQuestion> getQuestionList(DifficultyLvl lvl) {
        switch (lvl) {
            case LVL1 -> {
                return lvl1;
            }
            case LVL2 -> {
                return lvl2;
            }
            case LVL3 -> {
                return lvl3;
            }
            default -> {
                return null;
            }
        }
    }
}
