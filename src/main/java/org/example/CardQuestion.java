package org.example;

/**
 * @author Dmitry
 */
public class CardQuestion {
    private String question;
    private String action;

    public CardQuestion() {
    }

    public CardQuestion(final String question, final String action) {
        this.question = question;
        this.action = action;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }
}
