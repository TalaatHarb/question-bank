package net.talaatharb.questionbank.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class QuestionDto {
    private UUID id;
    private String question;
    private String answer;
    private String category;
    private String source;
    private String explanation;
    private String tags;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
    private String status;
    private String version;
    private String language;
    private String questionAudioFileName;
    private String answerAudioFileName;
}
