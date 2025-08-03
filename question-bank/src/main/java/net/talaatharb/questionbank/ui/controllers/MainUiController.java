package net.talaatharb.questionbank.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.talaatharb.questionbank.config.HelperBeans;
import net.talaatharb.questionbank.service.QuestionService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class MainUiController implements Initializable, SceneManager {

    @FXML
    private StackPane mainContainer;
    
    private Stage primaryStage;
    private QuestionService questionService;
    
    // Current scene
    private String currentQuestionBank;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Initializing UI application Main window controller...");
        
        // Initialize services
        questionService = HelperBeans.buildQuestionService();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        log.info("Primary stage set, starting with question bank list scene");
        
        // Start with the question bank list scene
        switchToQuestionBankList();
    }

    @Override
    public void switchToQuestionBankList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/talaatharb/questionbank/ui/QuestionBankList.fxml"));
            Parent root = loader.load();
            QuestionBankListController controller = loader.getController();
            controller.setQuestionService(questionService);
            controller.setSceneManager(this);
            
            // Clear the main container and add the new content
            mainContainer.getChildren().clear();
            mainContainer.getChildren().add(root);
            
            if (primaryStage != null) {
                primaryStage.setTitle("Question Bank Manager - Question Banks");
            }
            
            log.info("Switched to question bank list scene");
        } catch (IOException e) {
            log.error("Error switching to question bank list scene", e);
        }
    }

    @Override
    public void switchToQuestionViewer(String questionBank) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/talaatharb/questionbank/ui/QuestionViewer.fxml"));
            Parent root = loader.load();
            QuestionViewerController controller = loader.getController();
            controller.setQuestionService(questionService);
            controller.setSceneManager(this);
            controller.loadQuestionBank(questionBank);
            
            // Clear the main container and add the new content
            mainContainer.getChildren().clear();
            mainContainer.getChildren().add(root);
            currentQuestionBank = questionBank;
            
            if (primaryStage != null) {
                primaryStage.setTitle("Question Bank Manager - Viewer: " + questionBank);
            }
            
            log.info("Switched to question viewer scene for bank: {}", questionBank);
        } catch (IOException e) {
            log.error("Error switching to question viewer scene", e);
        }
    }

    @Override
    public void switchToQuestionEditor(String questionBank) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/talaatharb/questionbank/ui/QuestionEditor.fxml"));
            Parent root = loader.load();
            QuestionEditorController controller = loader.getController();
            controller.setQuestionService(questionService);
            controller.setSceneManager(this);
            controller.loadQuestionBank(questionBank);
            
            // Clear the main container and add the new content
            mainContainer.getChildren().clear();
            mainContainer.getChildren().add(root);
            currentQuestionBank = questionBank;
            
            if (primaryStage != null) {
                primaryStage.setTitle("Question Bank Manager - Editor: " + questionBank);
            }
            
            log.info("Switched to question editor scene for bank: {}", questionBank);
        } catch (IOException e) {
            log.error("Error switching to question editor scene", e);
        }
    }
}
