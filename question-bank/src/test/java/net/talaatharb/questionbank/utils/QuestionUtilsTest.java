package net.talaatharb.questionbank.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.talaatharb.questionbank.dto.QuestionDto;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionUtilsTest {

    @Test
    void testConvertToQuestionDtoList_WithValidJson() throws IOException {
        // Given
        String json = "[{\"question\":\"What is Java?\",\"answer\":\"A programming language\",\"category\":\"Programming\"}]";
        
        // When
        List<QuestionDto> result = QuestionUtils.convertToQuestionDtoList(json);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        QuestionDto question = result.get(0);
        assertEquals("What is Java?", question.getQuestion());
        assertEquals("A programming language", question.getAnswer());
        assertEquals("Programming", question.getCategory());
    }

    @Test
    void testConvertToQuestionDtoList_WithEmptyArray() throws IOException {
        // Given
        String json = "[]";
        
        // When
        List<QuestionDto> result = QuestionUtils.convertToQuestionDtoList(json);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testConvertToQuestionDtoList_WithMultipleQuestions() throws IOException {
        // Given
        String json = "[" +
                "{\"question\":\"What is Java?\",\"answer\":\"A programming language\",\"category\":\"Programming\"}," +
                "{\"question\":\"What is Spring?\",\"answer\":\"A framework\",\"category\":\"Framework\"}" +
                "]";
        
        // When
        List<QuestionDto> result = QuestionUtils.convertToQuestionDtoList(json);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        QuestionDto firstQuestion = result.get(0);
        assertEquals("What is Java?", firstQuestion.getQuestion());
        assertEquals("A programming language", firstQuestion.getAnswer());
        assertEquals("Programming", firstQuestion.getCategory());
        
        QuestionDto secondQuestion = result.get(1);
        assertEquals("What is Spring?", secondQuestion.getQuestion());
        assertEquals("A framework", secondQuestion.getAnswer());
        assertEquals("Framework", secondQuestion.getCategory());
    }

    @Test
    void testConvertToQuestionDtoList_WithInvalidJson() {
        // Given
        String invalidJson = "{invalid json}";
        
        // When & Then
        assertThrows(IOException.class, () -> {
            QuestionUtils.convertToQuestionDtoList(invalidJson);
        });
    }

    @Test
    void testConvertToQuestionDtoList_WithNullJson() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            QuestionUtils.convertToQuestionDtoList(null);
        });
    }

    @Test
    void testConvertToQuestionDtoList_WithEmptyString() {
        // When & Then
        assertThrows(IOException.class, () -> {
            QuestionUtils.convertToQuestionDtoList("");
        });
    }

    @Test
    void testConvertToJsonString_WithValidQuestions() throws JsonProcessingException {
        // Given
        QuestionDto question1 = new QuestionDto();
        question1.setQuestion("What is Java?");
        question1.setAnswer("A programming language");
        question1.setCategory("Programming");
        
        QuestionDto question2 = new QuestionDto();
        question2.setQuestion("What is Spring?");
        question2.setAnswer("A framework");
        question2.setCategory("Framework");
        
        List<QuestionDto> questions = List.of(question1, question2);
        
        // When
        String result = QuestionUtils.convertToJsonString(questions);
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("What is Java?"));
        assertTrue(result.contains("A programming language"));
        assertTrue(result.contains("Programming"));
        assertTrue(result.contains("What is Spring?"));
        assertTrue(result.contains("A framework"));
        assertTrue(result.contains("Framework"));
    }

    @Test
    void testConvertToJsonString_WithEmptyList() throws JsonProcessingException {
        // Given
        List<QuestionDto> questions = List.of();
        
        // When
        String result = QuestionUtils.convertToJsonString(questions);
        
        // Then
        assertNotNull(result);
        assertEquals("[]", result.replaceAll("\\s+", ""));
    }

    @Test
    void testConvertToJsonString_WithSingleQuestion() throws JsonProcessingException {
        // Given
        QuestionDto question = new QuestionDto();
        question.setQuestion("What is Java?");
        question.setAnswer("A programming language");
        question.setCategory("Programming");
        
        List<QuestionDto> questions = List.of(question);
        
        // When
        String result = QuestionUtils.convertToJsonString(questions);
        
        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("["));
        assertTrue(result.endsWith("]"));
        assertTrue(result.contains("What is Java?"));
        assertTrue(result.contains("A programming language"));
        assertTrue(result.contains("Programming"));
    }

    @Test
    void testRoundTripConversion() throws IOException, JsonProcessingException {
        // Given
        QuestionDto question1 = new QuestionDto();
        question1.setQuestion("What is Java?");
        question1.setAnswer("A programming language");
        question1.setCategory("Programming");
        
        QuestionDto question2 = new QuestionDto();
        question2.setQuestion("What is Spring?");
        question2.setAnswer("A framework");
        question2.setCategory("Framework");
        
        List<QuestionDto> originalQuestions = List.of(question1, question2);
        
        // When - Convert to JSON and back
        String jsonString = QuestionUtils.convertToJsonString(originalQuestions);
        List<QuestionDto> convertedQuestions = QuestionUtils.convertToQuestionDtoList(jsonString);
        
        // Then
        assertNotNull(convertedQuestions);
        assertEquals(originalQuestions.size(), convertedQuestions.size());
        
        // Verify first question
        QuestionDto convertedQuestion1 = convertedQuestions.get(0);
        assertEquals(originalQuestions.get(0).getQuestion(), convertedQuestion1.getQuestion());
        assertEquals(originalQuestions.get(0).getAnswer(), convertedQuestion1.getAnswer());
        assertEquals(originalQuestions.get(0).getCategory(), convertedQuestion1.getCategory());
        
        // Verify second question
        QuestionDto convertedQuestion2 = convertedQuestions.get(1);
        assertEquals(originalQuestions.get(1).getQuestion(), convertedQuestion2.getQuestion());
        assertEquals(originalQuestions.get(1).getAnswer(), convertedQuestion2.getAnswer());
        assertEquals(originalQuestions.get(1).getCategory(), convertedQuestion2.getCategory());
    }
} 