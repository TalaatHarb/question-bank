package net.talaatharb.questionbank.ui.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import net.talaatharb.questionbank.dto.QuestionDto;
import net.talaatharb.questionbank.service.QuestionService;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
class QuestionViewerControllerTest extends ApplicationTest {

    @Mock
    private QuestionService questionService;

    @Mock
    private SceneManager sceneManager;

    private QuestionViewerController controller;

    @Override
    public void start(Stage stage) {
        controller = new QuestionViewerController();
        controller.setQuestionService(questionService);
        controller.setSceneManager(sceneManager);
        controller.setStatusLabel(new Label());
        controller.setQuestionNumberLabel(new Label());
        controller.setPreviousButton(new Button());
        controller.setNextButton(new Button());
        controller.setQuestionTextLabel(new Label());
        controller.setAnswerTextArea(new TextArea());
        controller.setBackToListButton(new Button());
    }

    @BeforeEach
    void setUp() {
        reset(questionService, sceneManager);
    }

    @Test
    void testLoadQuestionBank_WithValidQuestions() {
        // Given
        String questionBank = "test-bank.json";
        List<QuestionDto> mockQuestions = Arrays.asList(
            createMockQuestion("What is Java?", "A programming language", "Programming"),
            createMockQuestion("What is Spring?", "A framework", "Framework")
        );
        when(questionService.getQuestions(questionBank)).thenReturn(mockQuestions);

        // When
        controller.loadQuestionBank(questionBank);

        // Then
        verify(questionService).getQuestions(questionBank);
    }

    @Test
    void testLoadQuestionBank_WithEmptyQuestions() {
        // Given
        String questionBank = "empty-bank.json";
        when(questionService.getQuestions(questionBank)).thenReturn(List.of());

        // When
        controller.loadQuestionBank(questionBank);

        // Then
        verify(questionService).getQuestions(questionBank);
    }

    @Test
    void testLoadQuestionBank_WithException() {
        // Given
        String questionBank = "invalid-bank.json";
        when(questionService.getQuestions(questionBank)).thenThrow(new RuntimeException("Test exception"));

        // When
        controller.loadQuestionBank(questionBank);

        // Then
        verify(questionService).getQuestions(questionBank);
        // Exception should be handled gracefully
    }

    @Test
    void testShowPreviousQuestion_WithValidIndex() {
        // Given
        setupControllerWithQuestions();
        controller.loadQuestionBank("test-bank.json");
        
        // Move to second question first
        controller.showNextQuestion();

        // When
        controller.showPreviousQuestion();

        // Then
        // Verify navigation worked (in a real scenario, we'd check the displayed question)
        assertDoesNotThrow(() -> controller.showPreviousQuestion());
    }

    @Test
    void testShowNextQuestion_WithValidIndex() {
        // Given
        setupControllerWithQuestions();
        controller.loadQuestionBank("test-bank.json");

        // When
        controller.showNextQuestion();

        // Then
        // Verify navigation worked
        assertDoesNotThrow(() -> controller.showNextQuestion());
    }

    @Test
    void testGoBackToList() {
        // Given
        controller.setSceneManager(sceneManager);

        // When
        controller.goBackToList();

        // Then
        verify(sceneManager).switchToQuestionBankList();
    }

    @Test
    void testSetQuestionService() {
        // Given
        QuestionService mockService = mock(QuestionService.class);

        // When & Then
        assertDoesNotThrow(() -> controller.setQuestionService(mockService));
    }

    @Test
    void testSetSceneManager() {
        // Given
        SceneManager mockSceneManager = mock(SceneManager.class);

        // When & Then
        assertDoesNotThrow(() -> controller.setSceneManager(mockSceneManager));
    }

    @Test
    void testInitialize() {
        // Given

        // When & Then
        assertDoesNotThrow(() -> controller.initialize(null, null));
    }

    // Helper methods
    private QuestionDto createMockQuestion(String question, String answer, String category) {
        QuestionDto dto = new QuestionDto();
        dto.setQuestion(question);
        dto.setAnswer(answer);
        dto.setCategory(category);
        return dto;
    }

    private void setupControllerWithQuestions() {
        List<QuestionDto> mockQuestions = Arrays.asList(
            createMockQuestion("Question 1", "Answer 1", "Category 1"),
            createMockQuestion("Question 2", "Answer 2", "Category 2"),
            createMockQuestion("Question 3", "Answer 3", "Category 3")
        );
        when(questionService.getQuestions(anyString())).thenReturn(mockQuestions);
    }
} 