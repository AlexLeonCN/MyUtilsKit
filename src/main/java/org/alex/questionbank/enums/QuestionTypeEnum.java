package org.alex.questionbank.enums;

public enum QuestionTypeEnum {
    TRUE_OR_FALSE(0, "TrueOrFalse", "TRUE_OR_FALSE"),
    SINGLE_CHOICE(1, "SingleChoice", "SINGLE_CHOICE"),
    MULTIPLE_CHOICE(2, "MultipleChoice", "MULTIPLE_CHOICE");

    private Integer code;
    private String name;
    private String value;

    QuestionTypeEnum(Integer code, String name, String value) {
        this.code = code;
        this.name = name;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
