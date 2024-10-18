package org.alex.questionbank.entity;

import lombok.Data;

@Data
public class QuestionInfo {
    private String id;
    private String examId;
    private String content;
    private Integer type;
    private String explanation;
}
