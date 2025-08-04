package net.talaatharb.questionbank.ui.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
        var sceneManager = new MainUiController();
        sceneManager.setMainContainer(new AnchorPane());

        // When & Then
        assertDoesNotThrow(sceneManager::switchToQuestionBankList);
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
        var sceneManager = new MainUiController();
        sceneManager.setMainContainer(new AnchorPane());

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
        var sceneManager = new MainUiController();
        sceneManager.setMainContainer(new AnchorPane());

        // When & Then
        assertDoesNotThrow(() -> {
            sceneManager.switchToQuestionViewer(null);
            sceneManager.switchToQuestionEditor(null);
        });
    }

    @Test
    void testSceneManagerWithEmptyParameters() {
        // Given
        var sceneManager = new MainUiController();
        sceneManager.setMainContainer(new AnchorPane());

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