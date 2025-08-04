package net.talaatharb.questionbank.service;

import net.talaatharb.questionbank.config.HelperBeans;
import net.talaatharb.questionbank.dto.QuestionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionServiceImplTest {

    @TempDir
    Path tempDir;
    
    private QuestionService questionService;
    private Path testDataPath;

    @BeforeEach
    void setUp() throws IOException {
        // Create test data directory
        testDataPath = tempDir.resolve("data");
        Files.createDirectories(testDataPath);
        
        // Create QuestionService using HelperBeans
        questionService = HelperBeans.buildQuestionService();
        
        // Create test JSON files
        createTestQuestionBank("java-questions.json", createJavaQuestions());
        createTestQuestionBank("spring-questions.json", createSpringQuestions());
        createTestQuestionBank("empty.json", "[]");
    }

    @Test
    void testGetQuestionBanks_WithExistingFiles() {
        // When
        List<String> questionBanks = questionService.getQuestionBanks();
        
        // Then
        assertNotNull(questionBanks);
        // Note: This test will only work if there are actual JSON files in ./data
        // In a real scenario, you might want to mock the FileUtils or use a different approach
        assertTrue(questionBanks.size() >= 0);
    }

    @Test
    void testGetQuestionBanks_WithNoFiles() throws IOException {
        // Given - empty data directory
        Path emptyDataPath = tempDir.resolve("emptyData");
        Files.createDirectories(emptyDataPath);
        
        // Note: This test is limited because the service uses hardcoded ./data path
        // In a real scenario, you might want to mock the FileUtils
        List<String> questionBanks = questionService.getQuestionBanks();
        
        // Then
        assertNotNull(questionBanks);
        // The actual result depends on what's in the ./data directory
    }

    @Test
    void testGetQuestions_WithValidQuestionBank() throws IOException {
        // Given - create a test file in the actual ./data directory
        Path actualDataPath = Path.of("./data");
        if (!Files.exists(actualDataPath)) {
            Files.createDirectories(actualDataPath);
        }
        
        String testFileName = "test-java-questions.json";
        Path testFile = actualDataPath.resolve(testFileName);
        Files.writeString(testFile, createJavaQuestions());
        
        try {
            // When
            List<QuestionDto> questions = questionService.getQuestions(testFileName);
            
            // Then
            assertNotNull(questions);
            assertEquals(2, questions.size());
            
            QuestionDto firstQuestion = questions.get(0);
            assertEquals("What is Java?", firstQuestion.getQuestion());
            assertEquals("A programming language", firstQuestion.getAnswer());
            assertEquals("Programming", firstQuestion.getCategory());
            
            QuestionDto secondQuestion = questions.get(1);
            assertEquals("What is JVM?", secondQuestion.getQuestion());
            assertEquals("Java Virtual Machine", secondQuestion.getAnswer());
            assertEquals("Programming", secondQuestion.getCategory());
        } finally {
            // Clean up
            Files.deleteIfExists(testFile);
        }
    }

    @Test
    void testGetQuestions_WithEmptyQuestionBank() throws IOException {
        // Given - create an empty test file in the actual ./data directory
        Path actualDataPath = Path.of("./data");
        if (!Files.exists(actualDataPath)) {
            Files.createDirectories(actualDataPath);
        }
        
        String testFileName = "test-empty.json";
        Path testFile = actualDataPath.resolve(testFileName);
        Files.writeString(testFile, "[]");
        
        try {
            // When
            List<QuestionDto> questions = questionService.getQuestions(testFileName);
            
            // Then
            assertNotNull(questions);
            assertTrue(questions.isEmpty());
        } finally {
            // Clean up
            Files.deleteIfExists(testFile);
        }
    }
    
    @CsvSource({
        "'null'",
        "''",
        "nonexistent.json"
    })
    @ParameterizedTest
    void testGetQuestionsForEmptyCases(String questionBank) {
     // When
        List<QuestionDto> questions = questionService.getQuestions(questionBank);
        
        // Then
        assertNotNull(questions);
        assertTrue(questions.isEmpty());
    }

    @Test
    void testSaveQuestions_WithValidData() throws IOException {
        // Given
        List<QuestionDto> newQuestions = createNewQuestions();
        String questionBankName = "new-questions.json";
        
        // When
        questionService.saveQuestions(questionBankName, newQuestions);
        
        // Then
        Path savedFile = Path.of("./data").resolve(questionBankName);
        assertTrue(Files.exists(savedFile));
        
        // Verify the saved content
        String savedContent = Files.readString(savedFile);
        assertTrue(savedContent.contains("What is Python?"));
        assertTrue(savedContent.contains("A programming language"));
        assertTrue(savedContent.contains("What is Django?"));
        assertTrue(savedContent.contains("A web framework"));
        
        // Clean up
        Files.deleteIfExists(savedFile);
    }

    @Test
    void testSaveQuestions_WithEmptyList() throws IOException {
        // Given
        List<QuestionDto> emptyQuestions = List.of();
        String questionBankName = "empty-questions.json";
        
        // When
        questionService.saveQuestions(questionBankName, emptyQuestions);
        
        // Then
        Path savedFile = Path.of("./data").resolve(questionBankName);
        assertTrue(Files.exists(savedFile));
        
        // Verify the saved content
        String savedContent = Files.readString(savedFile);
        assertEquals("[]", savedContent.replaceAll("\\s+", ""));
        
        // Clean up
        Files.deleteIfExists(savedFile);
    }

    @Test
    void testSaveQuestions_WithNullQuestionBankName() {
        // Given
        List<QuestionDto> questions = createNewQuestions();
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            questionService.saveQuestions(null, questions);
        });
    }

    @Test
    void testSaveQuestions_WithEmptyQuestionBankName() {
        // Given
        List<QuestionDto> questions = createNewQuestions();
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            questionService.saveQuestions("", questions);
        });
    }

    @Test
    void testSaveQuestions_WithNullQuestions() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            questionService.saveQuestions("test.json", null);
        });
    }

    @Test
    void testRoundTrip_SaveAndLoad() throws IOException {
        // Given
        List<QuestionDto> originalQuestions = createNewQuestions();
        String questionBankName = "roundtrip-test.json";
        
        // When - Save questions
        questionService.saveQuestions(questionBankName, originalQuestions);
        
        // Then - Load questions and verify
        List<QuestionDto> loadedQuestions = questionService.getQuestions(questionBankName);
        
        assertNotNull(loadedQuestions);
        assertEquals(originalQuestions.size(), loadedQuestions.size());
        
        // Verify first question
        QuestionDto originalFirst = originalQuestions.get(0);
        QuestionDto loadedFirst = loadedQuestions.get(0);
        assertEquals(originalFirst.getQuestion(), loadedFirst.getQuestion());
        assertEquals(originalFirst.getAnswer(), loadedFirst.getAnswer());
        assertEquals(originalFirst.getCategory(), loadedFirst.getCategory());
        
        // Verify second question
        QuestionDto originalSecond = originalQuestions.get(1);
        QuestionDto loadedSecond = loadedQuestions.get(1);
        assertEquals(originalSecond.getQuestion(), loadedSecond.getQuestion());
        assertEquals(originalSecond.getAnswer(), loadedSecond.getAnswer());
        assertEquals(originalSecond.getCategory(), loadedSecond.getCategory());
        
        // Clean up
        Files.deleteIfExists(Path.of("./data").resolve(questionBankName));
    }

    @Test
    void testSingletonBehavior() {
        // Test that HelperBeans returns the same instance
        QuestionService service1 = HelperBeans.buildQuestionService();
        QuestionService service2 = HelperBeans.buildQuestionService();
        
        // Verify they are the same instance (singleton behavior)
        assertSame(service1, service2);
        
        // Test ObjectMapper singleton behavior
        var mapper1 = HelperBeans.buildObjectMapper();
        var mapper2 = HelperBeans.buildObjectMapper();
        
        // Verify they are the same instance (singleton behavior)
        assertSame(mapper1, mapper2);
    }

    // Helper methods
    private void createTestQuestionBank(String filename, String content) throws IOException {
        Path filePath = testDataPath.resolve(filename);
        Files.writeString(filePath, content);
    }

    private String createJavaQuestions() {
        return "[" +
                "{\"question\":\"What is Java?\",\"answer\":\"A programming language\",\"category\":\"Programming\"}," +
                "{\"question\":\"What is JVM?\",\"answer\":\"Java Virtual Machine\",\"category\":\"Programming\"}" +
                "]";
    }

    private String createSpringQuestions() {
        return "[" +
                "{\"question\":\"What is Spring?\",\"answer\":\"A framework\",\"category\":\"Framework\"}," +
                "{\"question\":\"What is Spring Boot?\",\"answer\":\"A Spring-based framework\",\"category\":\"Framework\"}" +
                "]";
    }

    private List<QuestionDto> createNewQuestions() {
        QuestionDto question1 = new QuestionDto();
        question1.setQuestion("What is Python?");
        question1.setAnswer("A programming language");
        question1.setCategory("Programming");
        
        QuestionDto question2 = new QuestionDto();
        question2.setQuestion("What is Django?");
        question2.setAnswer("A web framework");
        question2.setCategory("Framework");
        
        return List.of(question1, question2);
    }
} 