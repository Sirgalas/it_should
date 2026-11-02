package ru.sergalas.whats_need.enums;

public enum QuestionEnum {
    ENRICHED_QUESTION("ENRICHED_QUESTION"),
    ORIGINAL_QUESTION("ORIGINAL_QUESTION"),
    EXPANSION_RATIO("EXPANSION_RATIO");

    private String value;
    QuestionEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
