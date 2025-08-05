package net.talaatharb.questionbank.service;

import lombok.extern.slf4j.Slf4j;
import net.talaatharb.questionbank.dto.QuestionDto;
import net.talaatharb.questionbank.utils.FileUtils;
import net.talaatharb.questionbank.utils.QuestionUtils;

import java.io.IOException;
import java.util.List;

@Slf4j
public class QuestionServiceImpl implements QuestionService {

    @Override
    public List<String> getQuestionBanks() {
        try {
            log.debug("Retrieving list of question banks");
            List<String> jsonFiles = FileUtils.listJsonFilesInDataFolder();
            log.debug("Found {} question banks: {}", jsonFiles.size(), jsonFiles);
            return jsonFiles;
        } catch (IOException e) {
            log.trace("Error retrieving question banks", e);
            return List.of();
        }
    }

    @Override
    public List<QuestionDto> getQuestions(String questionBank) {
        if (questionBank == null || questionBank.trim().isEmpty()) {
            log.warn("Question bank name is null or empty");
            return List.of();
        }

        try {
            log.debug("Loading questions from question bank: {}", questionBank);
            
            // Load the JSON file content using FileUtils
            String jsonContent = FileUtils.loadJsonFileAsString(questionBank);
            
            // Convert JSON to QuestionDto list
            List<QuestionDto> questions = QuestionUtils.convertToQuestionDtoList(jsonContent);
            
            log.debug("Successfully loaded {} questions from question bank: {}", 
                    questions.size(), questionBank);
            
            return questions;
        } catch (IOException e) {
            log.trace("Error loading questions from question bank: {}", questionBank, e);
            return List.of();
        }
    }

    @Override
    public void saveQuestions(String questionBank, List<QuestionDto> questions) {
        if (questionBank == null || questionBank.trim().isEmpty()) {
            log.error("Cannot save questions: question bank name is null or empty");
            throw new IllegalArgumentException("Question bank name cannot be null or empty");
        }

        if (questions == null) {
            log.error("Cannot save questions: questions list is null");
            throw new IllegalArgumentException("Questions list cannot be null");
        }

        try {
            log.debug("Saving {} questions to question bank: {}", questions.size(), questionBank);
            
            // Use FileUtils to save questions to JSON file
            FileUtils.saveQuestionsToJsonFile(questionBank, questions);
            
            log.debug("Successfully saved {} questions to question bank: {}", 
                    questions.size(), questionBank);
                    
        } catch (IOException e) {
            log.error("Error saving questions to question bank: {}", questionBank, e);
            throw new RuntimeException("Failed to save questions to question bank: " + questionBank, e);
        }
    }
} 