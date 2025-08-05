package net.talaatharb.questionbank.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.talaatharb.questionbank.dto.QuestionDto;
import net.talaatharb.questionbank.service.QuestionService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
public class QuestionEditorController implements Initializable {

    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Label questionNumberLabel;

    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private TextField questionTextField;

    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private TextArea answerTextArea;

    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private TextField categoryTextField;

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
    private Button saveButton;

    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Button addQuestionButton;

    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Button deleteQuestionButton;

    @Getter(value = AccessLevel.PACKAGE)
    @Setter(value = AccessLevel.PACKAGE)
    @FXML
    private Button saveBankButton;

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
    private String currentQuestionBank;
    private int currentQuestionIndex = 0;
    private boolean hasUnsavedChanges = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Initializing Question Editor Controller");
        setupEventHandlers();
    }

    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void loadQuestionBank(String questionBank) {
        this.currentQuestionBank = questionBank;
        this.currentQuestionIndex = 0;
        this.hasUnsavedChanges = false;

        try {
            questions = new ArrayList<>(questionService.getQuestions(questionBank));
            if (questions.isEmpty()) {
                statusLabel.setText("No questions found in this bank");
                clearQuestionDisplay();
            } else {
                statusLabel.setText(String.format("Loaded %d questions from %s", questions.size(), questionBank));
                displayCurrentQuestion();
            }
            log.debug("Loaded question bank for editing: {} with {} questions", questionBank, questions.size());
        } catch (Exception e) {
            log.debug("Error loading question bank for editing: {}", questionBank, e);
            statusLabel.setText("Error loading question bank");
            clearQuestionDisplay();
        }
    }

    private void setupEventHandlers() {
        previousButton.setOnAction(event -> showPreviousQuestion());
        nextButton.setOnAction(event -> showNextQuestion());
        saveButton.setOnAction(event -> saveCurrentQuestion());
        addQuestionButton.setOnAction(event -> addNewQuestion());
        deleteQuestionButton.setOnAction(event -> deleteCurrentQuestion());
        saveBankButton.setOnAction(event -> saveQuestionBank());
        backToListButton.setOnAction(event -> goBackToList());

        // Track changes
        questionTextField.textProperty().addListener((observable, oldValue, newValue) -> markAsChanged());
        answerTextArea.textProperty().addListener((observable, oldValue, newValue) -> markAsChanged());
        categoryTextField.textProperty().addListener((observable, oldValue, newValue) -> markAsChanged());
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
        if (questions != null && !questions.isEmpty() && currentQuestionIndex >= 0
                && currentQuestionIndex < questions.size()) {
            QuestionDto question = questions.get(currentQuestionIndex);

            questionNumberLabel.setText(String.format("Question %d of %d", currentQuestionIndex + 1, questions.size()));
            questionTextField.setText(question.getQuestion());
            answerTextArea.setText(question.getAnswer());
            categoryTextField.setText(question.getCategory());

            // Update button states
            previousButton.setDisable(currentQuestionIndex == 0);
            nextButton.setDisable(currentQuestionIndex == questions.size() - 1);
            deleteQuestionButton.setDisable(false);

            hasUnsavedChanges = false;
            updateSaveButtonState();

            log.debug("Displaying question {} of {} for editing", currentQuestionIndex + 1, questions.size());
        }
    }

    private void clearQuestionDisplay() {
        questionNumberLabel.setText("No questions");
        questionTextField.setText("");
        answerTextArea.setText("");
        categoryTextField.setText("");
        previousButton.setDisable(true);
        nextButton.setDisable(true);
        deleteQuestionButton.setDisable(true);
        updateSaveButtonState();
    }

    public void saveCurrentQuestion() {
        if (questions != null && currentQuestionIndex >= 0) {
            QuestionDto question = null;
            if(currentQuestionIndex < questions.size()) {
                question = questions.get(currentQuestionIndex);
            } else {
                question = new QuestionDto();
                questions.add(question);
                currentQuestionIndex = questions.size() - 1;
            }
            question.setQuestion(questionTextField.getText());
            question.setAnswer(answerTextArea.getText());
            question.setCategory(categoryTextField.getText());

            // Save the question bank to persist changes
            saveQuestionBank();

            hasUnsavedChanges = false;
            updateSaveButtonState();
            statusLabel.setText("Question saved to file");

            log.debug("Saved question {} of {} to file", currentQuestionIndex + 1, questions.size());
        }
    }

    public void addNewQuestion() {
        QuestionDto newQuestion = new QuestionDto();
        newQuestion.setQuestion("New Question");
        newQuestion.setAnswer("New Answer");
        newQuestion.setCategory("General");

        questions.add(newQuestion);
        currentQuestionIndex = questions.size() - 1;

        statusLabel.setText(String.format("Added new question. Total: %d", questions.size()));
        displayCurrentQuestion();

        log.debug("Added new question to bank: {}", currentQuestionBank);
    }

    public void deleteCurrentQuestion() {
        if (questions != null && !questions.isEmpty() && currentQuestionIndex >= 0
                && currentQuestionIndex < questions.size()) {
            questions.remove(currentQuestionIndex);

            if (questions.isEmpty()) {
                clearQuestionDisplay();
                statusLabel.setText("No questions remaining");
            } else {
                if (currentQuestionIndex >= questions.size()) {
                    currentQuestionIndex = questions.size() - 1;
                }
                displayCurrentQuestion();
                statusLabel.setText(String.format("Question deleted. Total: %d", questions.size()));
            }

            hasUnsavedChanges = true;
            updateSaveButtonState();

            log.debug("Deleted question {} from bank: {}", currentQuestionIndex + 1, currentQuestionBank);
        }
    }

    private void markAsChanged() {
        hasUnsavedChanges = true;
        updateSaveButtonState();
    }

    private void updateSaveButtonState() {
        saveButton.setDisable(!hasUnsavedChanges);
    }

    public void saveQuestionBank() {
        if (questionService != null && currentQuestionBank != null && questions != null) {
            try {
                questionService.saveQuestions(currentQuestionBank, questions);
                hasUnsavedChanges = false;
                updateSaveButtonState();
                statusLabel.setText("Question bank saved successfully");
                log.debug("Saved question bank: {} with {} questions", currentQuestionBank, questions.size());
            } catch (Exception e) {
                log.debug("Error saving question bank: {}", currentQuestionBank, e);
                statusLabel.setText("Error saving question bank");
            }
        }
    }

    public void goBackToList() {
        if (hasUnsavedChanges) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Unsaved Changes");
            alert.setHeaderText("You have unsaved changes");
            alert.setContentText("Are you sure you want to go back to the list?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                log.warn("Unsaved changes detected when going back to list");
            } else {
                return;
            }
        }

        if (sceneManager != null) {
            log.debug("Returning to question bank list");
            sceneManager.switchToQuestionBankList();
        }
    }

    // Getter for testing
    public List<QuestionDto> getQuestions() {
        return questions;
    }
}