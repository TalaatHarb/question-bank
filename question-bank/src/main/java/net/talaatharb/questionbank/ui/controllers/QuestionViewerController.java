package net.talaatharb.questionbank.ui.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.talaatharb.questionbank.dto.QuestionDto;
import net.talaatharb.questionbank.service.QuestionService;

@Slf4j
public class QuestionViewerController implements Initializable {

    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Label questionNumberLabel;
    
    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Label questionTextLabel;
    
    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private TextArea answerTextArea;
    
    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Button previousButton;
    
    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Button nextButton;
    
    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Button backToListButton;
    
    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Label statusLabel;
    
    private QuestionService questionService;
    private SceneManager sceneManager;
    private List<QuestionDto> questions;
    private int currentQuestionIndex = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Initializing Question Viewer Controller");
        setupEventHandlers();
    }

    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void loadQuestionBank(String questionBank) {
        this.currentQuestionIndex = 0;
        
        try {
            questions = questionService.getQuestions(questionBank);
            if (questions.isEmpty()) {
                statusLabel.setText("No questions found in this bank");
                clearQuestionDisplay();
            } else {
                statusLabel.setText(String.format("Loaded %d questions from %s", questions.size(), questionBank));
                displayCurrentQuestion();
            }
            log.info("Loaded question bank: {} with {} questions", questionBank, questions.size());
        } catch (Exception e) {
            log.error("Error loading question bank: {}", questionBank, e);
            statusLabel.setText("Error loading question bank");
            clearQuestionDisplay();
        }
    }

    private void setupEventHandlers() {
        previousButton.setOnAction(event -> showPreviousQuestion());
        nextButton.setOnAction(event -> showNextQuestion());
        backToListButton.setOnAction(event -> goBackToList());
    }

    public void showPreviousQuestion() {
        if (questions != null && !questions.isEmpty() && currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayCurrentQuestion();
        }
    }

    public void showNextQuestion() {
        if (questions != null && !questions.isEmpty() && currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayCurrentQuestion();
        }
    }

    private void displayCurrentQuestion() {
        if (questions != null && !questions.isEmpty() && currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            QuestionDto question = questions.get(currentQuestionIndex);
            
            questionNumberLabel.setText(String.format("Question %d of %d", currentQuestionIndex + 1, questions.size()));
            questionTextLabel.setText(question.getQuestion());
            answerTextArea.setText(question.getAnswer());
            
            // Update button states
            previousButton.setDisable(currentQuestionIndex == 0);
            nextButton.setDisable(currentQuestionIndex == questions.size() - 1);
            
            log.debug("Displaying question {} of {}", currentQuestionIndex + 1, questions.size());
        }
    }

    private void clearQuestionDisplay() {
        questionNumberLabel.setText("No questions");
        questionTextLabel.setText("");
        answerTextArea.setText("");
        previousButton.setDisable(true);
        nextButton.setDisable(true);
    }

    public void goBackToList() {
        if (sceneManager != null) {
            log.info("Returning to question bank list");
            sceneManager.switchToQuestionBankList();
        }
    }
} 