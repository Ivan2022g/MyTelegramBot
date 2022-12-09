package org.example.utils;

/**
 * @author Dmitry
 */
public enum DifficultyLvl {
    LVL1("Уровень 1"),
    LVL2("Уровень 2"),
    LVL3("Уровень 3");

    private final String text;
    DifficultyLvl(final String s) {
        text = s;
    }

    public String getText() {
        return text;
    }
}
