package net.talaatharb.questionbank.service;

import java.io.IOException;
import java.util.List;

import net.talaatharb.questionbank.dto.QuestionDto;

public interface QuestionService {
    
    List<String> getQuestionBanks();

    List<QuestionDto> getQuestions(String questionBank);

    void saveQuestions(String questionBank, List<QuestionDto> questions) throws IOException;
}
