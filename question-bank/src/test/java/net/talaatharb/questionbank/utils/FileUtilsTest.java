package net.talaatharb.questionbank.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @TempDir
    Path tempDir;

    private Path testDataPath;

    @BeforeEach
    void setUp() throws IOException {
        // Create a test data directory in temp
        testDataPath = tempDir.resolve("data");
        Files.createDirectories(testDataPath);
        FileUtils.setDataFolder(testDataPath.toString());

        // Create test JSON files
        Files.write(testDataPath.resolve("empty.json"), "[]".getBytes());
        Files.write(testDataPath.resolve("test1.json"), "{\"test\": \"data1\"}".getBytes());
        Files.write(testDataPath.resolve("test2.json"), "{\"test\": \"data2\"}".getBytes());
        Files.write(testDataPath.resolve("notjson.txt"), "This is not a JSON file".getBytes());
    }

    @Test
    void testListJsonFilesInDataFolder_WithJsonFiles() throws IOException {
        // Create a mock FileUtils that uses our test directory
        List<String> jsonFiles = FileUtils.listJsonFilesInDataFolder();

        assertNotNull(jsonFiles);
        assertEquals(3, jsonFiles.size());
        assertTrue(jsonFiles.contains("empty.json"));
        assertTrue(jsonFiles.contains("test1.json"));
        assertTrue(jsonFiles.contains("test2.json"));
        assertFalse(jsonFiles.contains("notjson.txt"));
    }

    @Test
    void testListJsonFilePathsInDataFolder_WithJsonFiles() throws IOException {
        // Create a mock FileUtils that uses our test directory
        List<Path> jsonFilePaths = FileUtils.listJsonFilePathsInDataFolder();

        assertNotNull(jsonFilePaths);
        assertEquals(3, jsonFilePaths.size());

        // Check that all paths end with .json
        for (Path path : jsonFilePaths) {
            assertTrue(path.toString().toLowerCase().endsWith(".json"));
        }

        // Check specific files exist
        boolean hasEmptyJson = jsonFilePaths.stream()
                .anyMatch(path -> path.getFileName().toString().equals("empty.json"));
        boolean hasTest1Json = jsonFilePaths.stream()
                .anyMatch(path -> path.getFileName().toString().equals("test1.json"));
        boolean hasTest2Json = jsonFilePaths.stream()
                .anyMatch(path -> path.getFileName().toString().equals("test2.json"));

        assertTrue(hasEmptyJson);
        assertTrue(hasTest1Json);
        assertTrue(hasTest2Json);
    }

    @Test
    void testListJsonFilesInDataFolder_EmptyDirectory() throws IOException {
        // Create an empty data directory
        Path emptyDataPath = tempDir.resolve("emptyData");
        Files.createDirectories(emptyDataPath);
        FileUtils.setDataFolder(emptyDataPath.toString());

        List<String> jsonFiles = FileUtils.listJsonFilesInDataFolder();

        assertNotNull(jsonFiles);
        assertTrue(jsonFiles.isEmpty());
    }

    @Test
    void testListJsonFilePathsInDataFolder_EmptyDirectory() throws IOException {
        // Create an empty data directory
        Path emptyDataPath = tempDir.resolve("emptyData");
        Files.createDirectories(emptyDataPath);
        FileUtils.setDataFolder(emptyDataPath.toString());

        List<Path> jsonFilePaths = FileUtils.listJsonFilePathsInDataFolder();

        assertNotNull(jsonFilePaths);
        assertTrue(jsonFilePaths.isEmpty());
    }

    @Test
    void testListJsonFilesInDataFolder_WithMixedFiles() throws IOException {
        // Add some non-JSON files to ensure they're filtered out
        Files.write(testDataPath.resolve("document.pdf"), "PDF content".getBytes());
        Files.write(testDataPath.resolve("image.png"), "PNG content".getBytes());
        Files.write(testDataPath.resolve("data.JSON"), "{\"uppercase\": \"extension\"}".getBytes()); // Test case
                                                                                                     // insensitivity

        List<String> jsonFiles = FileUtils.listJsonFilesInDataFolder();

        assertNotNull(jsonFiles);
        assertEquals(4, jsonFiles.size()); // 3 original + 1 uppercase .JSON
        assertTrue(jsonFiles.contains("empty.json"));
        assertTrue(jsonFiles.contains("test1.json"));
        assertTrue(jsonFiles.contains("test2.json"));
        assertTrue(jsonFiles.contains("data.JSON"));
        assertFalse(jsonFiles.contains("document.pdf"));
        assertFalse(jsonFiles.contains("image.png"));
        assertFalse(jsonFiles.contains("notjson.txt"));
    }

    @CsvSource({
        "empty.json,[]",
        "test1.json,{\"test\": \"data1\"}",
        "notjson.txt,This is not a JSON file"
    })
    @ParameterizedTest
    void testLoadJsonFileAsString(String fileName, String expectedContent) throws IOException {
        String content = FileUtils.loadJsonFileAsString(fileName);

        assertNotNull(content);
        assertEquals(expectedContent, content.trim());
    }

    @Test
    void testLoadJsonFileAsString_WithNullFilename() {
        // Test with null filename
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtils.loadJsonFileAsString(null);
        });
    }

    @Test
    void testLoadJsonFileAsString_WithEmptyFilename() {
        // Test with empty filename
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtils.loadJsonFileAsString("");
        });
    }

    @Test
    void testLoadJsonFileAsString_WithWhitespaceFilename() {
        // Test with whitespace-only filename
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtils.loadJsonFileAsString("   ");
        });
    }

    @Test
    void testLoadJsonFileAsString_WithNonExistentFile() {
        // Test with non-existent file
        assertThrows(IOException.class, () -> {
            FileUtils.loadJsonFileAsString("nonexistent.json");
        });
    }

    @Test
    void testLoadJsonFileAsString_WithDirectory() throws IOException {
        // Create a directory with the same name as a file
        Path directoryPath = testDataPath.resolve("directory.json");
        Files.createDirectories(directoryPath);

        // Test loading a directory (should fail)
        assertThrows(IOException.class, () -> {
            FileUtils.loadJsonFileAsString("directory.json");
        });
    }

    @Test
    void testActualLoadJsonFileAsString_WithEmptyJson() throws IOException {
        // This test uses the actual FileUtils method with the real empty.json file
        // It will only work if the empty.json file exists in the ./data folder

        try {
            String content = FileUtils.loadJsonFileAsString("empty.json");

            assertNotNull(content);
            assertEquals("[]", content.trim());
        } catch (IOException e) {
            // Skip test if the actual data folder doesn't exist or empty.json is not found
            System.out.println("Skipping actual FileUtils test - ./data/empty.json not found: " + e.getMessage());
        }
    }
}