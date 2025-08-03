package net.talaatharb.questionbank.ui.controllers;

/**
 * Interface for managing scene transitions in the Question Bank application
 */
public interface SceneManager {
    
    /**
     * Switch to the question bank list scene
     */
    void switchToQuestionBankList();
    
    /**
     * Switch to the question viewer scene with the specified question bank
     * 
     * @param questionBank the name of the question bank to display
     */
    void switchToQuestionViewer(String questionBank);
    
    /**
     * Switch to the question editor scene with the specified question bank
     * 
     * @param questionBank the name of the question bank to edit
     */
    void switchToQuestionEditor(String questionBank);
} 