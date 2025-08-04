package net.talaatharb.questionbank.ui.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.talaatharb.questionbank.service.QuestionService;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
class MainUiControllerTest extends ApplicationTest {

    @Mock
    private QuestionService questionService;

    private MainUiController controller;

    @Override
    public void start(Stage stage) {
        controller = new MainUiController();
        controller.setMainContainer(new AnchorPane());
    }

    @BeforeEach
    void setUp() {
        reset(questionService);
    }

    @Test
    void testInitialize() {
        // Given
        MainUiController newController = new MainUiController();

        // When & Then
        assertDoesNotThrow(() -> newController.initialize(null, null));
    }

    @Test
    void testSetPrimaryStage() {
        // Given
        Stage mockStage = mock(Stage.class);

        // When & Then
        assertDoesNotThrow(() -> controller.setPrimaryStage(mockStage));
    }

    @Test
    void testSwitchToQuestionBankList() {
        // Given
        controller.setPrimaryStage(mock(Stage.class));

        // When & Then
        assertDoesNotThrow(() -> controller.switchToQuestionBankList());
    }

    @Test
    void testSwitchToQuestionViewer() {
        // Given
        controller.setPrimaryStage(mock(Stage.class));
        String questionBank = "test-bank.json";

        // When & Then
        assertDoesNotThrow(() -> controller.switchToQuestionViewer(questionBank));
    }

    @Test
    void testSwitchToQuestionEditor() {
        // Given
        controller.setPrimaryStage(mock(Stage.class));
        String questionBank = "test-bank.json";

        // When & Then
        assertDoesNotThrow(() -> controller.switchToQuestionEditor(questionBank));
    }

    @Test
    void testSwitchToQuestionBankList_WithNullStage() {
        // Given
        controller.setPrimaryStage(null);

        // When & Then
        assertDoesNotThrow(() -> controller.switchToQuestionBankList());
    }

    @Test
    void testSwitchToQuestionViewer_WithNullStage() {
        // Given
        controller.setPrimaryStage(null);
        String questionBank = "test-bank.json";

        // When & Then
        assertDoesNotThrow(() -> controller.switchToQuestionViewer(questionBank));
    }

    @Test
    void testSwitchToQuestionEditor_WithNullStage() {
        // Given
        controller.setPrimaryStage(null);
        String questionBank = "test-bank.json";

        // When & Then
        assertDoesNotThrow(() -> controller.switchToQuestionEditor(questionBank));
    }

    @Test
    void testSceneManagerInterfaceImplementation() {
        // Given
        controller = new MainUiController();

        // When & Then
        assertTrue(controller instanceof SceneManager);
    }

    @Test
    void testControllerInitialization() {
        // Given
        controller = new MainUiController();

        // When & Then
        assertNotNull(controller);
        assertDoesNotThrow(() -> controller.initialize(null, null));
    }
} 