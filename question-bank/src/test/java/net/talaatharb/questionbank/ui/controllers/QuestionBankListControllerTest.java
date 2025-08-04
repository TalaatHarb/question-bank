package net.talaatharb.questionbank.ui.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.talaatharb.questionbank.service.QuestionService;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
class QuestionBankListControllerTest extends ApplicationTest {

    @Mock
    private QuestionService questionService;

    @Mock
    private SceneManager sceneManager;

    private QuestionBankListController controller;

    @Override
    public void start(Stage stage) {
        controller = new QuestionBankListController();
        controller.setStatusLabel(new Label());
        controller.setCreateNewBankButton(new Button());
        controller.setEditBankButton(new Button());
        controller.setOpenBankButton(new Button());
        controller.setQuestionBankListView(new ListView<>());
        controller.setRefreshButton(new Button());
        controller.setQuestionService(questionService);
        controller.setSceneManager(sceneManager);
    }

    @BeforeEach
    void setUp() {
        // Reset mocks
        reset(questionService, sceneManager);
    }

    @Test
    void testSetQuestionService_ShouldRefreshQuestionBanks() {
        // Given
        List<String> mockBanks = Arrays.asList("bank1.json", "bank2.json");
        when(questionService.getQuestionBanks()).thenReturn(mockBanks);

        // When
        controller.setQuestionService(questionService);

        // Then
        verify(questionService).getQuestionBanks();
    }

    @Test
    void testRefreshQuestionBanks_WithValidBanks() {
        // Given
        List<String> mockBanks = Arrays.asList("java-questions.json", "spring-questions.json");
        when(questionService.getQuestionBanks()).thenReturn(mockBanks);

        // When
        controller.refreshQuestionBanks();

        // Then
        verify(questionService).getQuestionBanks();
        // Note: We can't easily test the ListView content without JavaFX thread
        // but we can verify the service was called
    }

    @Test
    void testRefreshQuestionBanks_WithEmptyList() {
        // Given
        when(questionService.getQuestionBanks()).thenReturn(List.of());

        // When
        controller.refreshQuestionBanks();

        // Then
        verify(questionService).getQuestionBanks();
    }

    @Test
    void testRefreshQuestionBanks_WithException() {
        // Given
        when(questionService.getQuestionBanks()).thenThrow(new RuntimeException("Test exception"));

        // When
        controller.refreshQuestionBanks();

        // Then
        verify(questionService).getQuestionBanks();
        // Exception should be handled gracefully
    }

    @Test
    void testSetSceneManager() {
        // Given
        SceneManager mockSceneManager = mock(SceneManager.class);

        // When
        controller.setSceneManager(mockSceneManager);

        // Then
        // Verify the scene manager was set (we can't easily access private fields)
        // but the method should complete without exception
        assertDoesNotThrow(() -> controller.setSceneManager(mockSceneManager));
    }

    @Test
    void testInitialize() {
        // Given

        // When & Then
        assertDoesNotThrow(() -> controller.initialize(null, null));
    }
} 