package net.talaatharb.questionbank.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.talaatharb.questionbank.dto.QuestionDto;
import net.talaatharb.questionbank.service.QuestionService;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
public class QuestionBankListController implements Initializable {

    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private ListView<String> questionBankListView;
    
    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Button openBankButton;
    
    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Button editBankButton;
    
    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Button refreshButton;
    
    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Button createNewBankButton;
    
    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Label statusLabel;
    
    private QuestionService questionService;
    private SceneManager sceneManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Initializing Question Bank List Controller");
        setupEventHandlers();
    }

    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
        refreshQuestionBanks();
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private void setupEventHandlers() {
        openBankButton.setOnAction(event -> openSelectedBank());
        editBankButton.setOnAction(event -> editSelectedBank());
        refreshButton.setOnAction(event -> refreshQuestionBanks());
        createNewBankButton.setOnAction(event -> createNewQuestionBank());
        
        // Enable/disable buttons based on selection
        questionBankListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                boolean hasSelection = newValue != null;
                openBankButton.setDisable(!hasSelection);
                editBankButton.setDisable(!hasSelection);
            }
        );
    }

    public void openSelectedBank() {
        String selectedBank = questionBankListView.getSelectionModel().getSelectedItem();
        if (selectedBank != null && sceneManager != null) {
            log.info("Opening question bank: {}", selectedBank);
            sceneManager.switchToQuestionViewer(selectedBank);
        }
    }

    public void editSelectedBank() {
        String selectedBank = questionBankListView.getSelectionModel().getSelectedItem();
        if (selectedBank != null && sceneManager != null) {
            log.info("Editing question bank: {}", selectedBank);
            sceneManager.switchToQuestionEditor(selectedBank);
        }
    }

    public void createNewQuestionBank() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Question Bank");
        dialog.setHeaderText("Enter the name for the new question bank");
        dialog.setContentText("Question Bank Name:");
        
        // Set default value with .json extension
        dialog.getEditor().setText("new-question-bank.json");
        
        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String newBankName = result.get().trim();
            
            // Ensure the filename ends with .json
            if (!newBankName.toLowerCase().endsWith(".json")) {
                newBankName += ".json";
            }
            
            try {
                // Create an empty question bank
                List<QuestionDto> emptyQuestions = List.of();
                questionService.saveQuestions(newBankName, emptyQuestions);
                
                log.info("Created new question bank: {}", newBankName);
                statusLabel.setText("Created new question bank: " + newBankName);
                
                // Refresh the list to show the new bank
                refreshQuestionBanks();
                
                // Optionally, open the editor for the new bank
                if (sceneManager != null) {
                    sceneManager.switchToQuestionEditor(newBankName);
                }
                
            } catch (Exception e) {
                log.error("Error creating new question bank: {}", newBankName, e);
                statusLabel.setText("Error creating question bank: " + e.getMessage());
            }
        }
    }

    public void refreshQuestionBanks() {
        if (questionService != null) {
            try {
                List<String> questionBanks = questionService.getQuestionBanks();
                ObservableList<String> observableList = FXCollections.observableArrayList(questionBanks);
                questionBankListView.setItems(observableList);
                
                statusLabel.setText(String.format("Found %d question banks", questionBanks.size()));
                log.info("Refreshed question banks, found {} banks", questionBanks.size());
            } catch (Exception e) {
                log.error("Error refreshing question banks", e);
                statusLabel.setText("Error loading question banks");
            }
        }
    }
} 