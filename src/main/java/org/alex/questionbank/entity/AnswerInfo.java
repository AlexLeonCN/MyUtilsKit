package org.alex.questionbank.entity;

import lombok.Data;

@Data
public class AnswerInfo {
    private String id;
    private String questionId;
    private String content;
    private Boolean isCorrect;
}
