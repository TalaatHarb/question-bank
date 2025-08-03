package net.talaatharb.questionbank.ui.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
class SceneManagerTest extends ApplicationTest {

    @Mock
    private SceneManager mockSceneManager;

    @Override
    public void start(Stage stage) {
        // Setup for JavaFX tests
    }

    @Test
    void testSceneManagerInterfaceContract() {
        // Given
        SceneManager sceneManager = new MainUiController();

        // When & Then
        assertDoesNotThrow(() -> sceneManager.switchToQuestionBankList());
        assertDoesNotThrow(() -> sceneManager.switchToQuestionViewer("test-bank.json"));
        assertDoesNotThrow(() -> sceneManager.switchToQuestionEditor("test-bank.json"));
    }

    @Test
    void testSceneManagerMethods_WithMock() {
        // Given
        String testBank = "test-bank.json";

        // When
        mockSceneManager.switchToQuestionBankList();
        mockSceneManager.switchToQuestionViewer(testBank);
        mockSceneManager.switchToQuestionEditor(testBank);

        // Then
        verify(mockSceneManager).switchToQuestionBankList();
        verify(mockSceneManager).switchToQuestionViewer(testBank);
        verify(mockSceneManager).switchToQuestionEditor(testBank);
    }

    @Test
    void testSceneManagerInterfaceMethods() {
        // Given
        SceneManager sceneManager = new MainUiController();

        // When & Then
        assertDoesNotThrow(() -> {
            sceneManager.switchToQuestionBankList();
            sceneManager.switchToQuestionViewer("bank1.json");
            sceneManager.switchToQuestionEditor("bank2.json");
        });
    }

    @Test
    void testSceneManagerWithNullParameters() {
        // Given
        SceneManager sceneManager = new MainUiController();

        // When & Then
        assertDoesNotThrow(() -> {
            sceneManager.switchToQuestionViewer(null);
            sceneManager.switchToQuestionEditor(null);
        });
    }

    @Test
    void testSceneManagerWithEmptyParameters() {
        // Given
        SceneManager sceneManager = new MainUiController();

        // When & Then
        assertDoesNotThrow(() -> {
            sceneManager.switchToQuestionViewer("");
            sceneManager.switchToQuestionEditor("");
        });
    }

    @Test
    void testSceneManagerInterfaceImplementation() {
        // Given
        MainUiController controller = new MainUiController();

        // When & Then
        assertTrue(controller instanceof SceneManager);
        assertNotNull(controller);
    }
} 