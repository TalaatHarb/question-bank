package net.talaatharb.questionbank.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.talaatharb.questionbank.dto.QuestionDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    private static String dataFolder = "./data";

    /**
     * Lists all JSON files in the ./data folder
     * 
     * @return List of JSON file names (without path)
     * @throws IOException if there's an error reading the directory
     */
    public static List<String> listJsonFilesInDataFolder() throws IOException {
        Path dataPath = Paths.get(dataFolder);

        if (!Files.exists(dataPath)) {
            return List.of(); // Return empty list if directory doesn't exist
        }

        List<String> result = null;

        try (var list = Files.list(dataPath)) {
            result = list.filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".json"))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .toList();
        }

        return result;

    }

    /**
     * Lists all JSON files in the ./data folder with full paths
     * 
     * @return List of JSON file paths
     * @throws IOException if there's an error reading the directory
     */
    public static List<Path> listJsonFilePathsInDataFolder() throws IOException {
        Path dataPath = Paths.get(dataFolder);

        if (!Files.exists(dataPath)) {
            return List.of(); // Return empty list if directory doesn't exist
        }

        List<Path> result = null;

        try (var list = Files.list(dataPath)) {
            result = list.filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".json"))
                    .toList();
        }

        return result;

    }

    /**
     * Loads a JSON file from the ./data folder as a string
     * 
     * @param filename the name of the JSON file to load (e.g., "empty.json")
     * @return the contents of the JSON file as a string
     * @throws IOException              if the file doesn't exist or there's an
     *                                  error reading it
     * @throws IllegalArgumentException if the filename is null or empty
     */
    public static String loadJsonFileAsString(String filename) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        Path dataPath = Paths.get(dataFolder);
        Path filePath = dataPath.resolve(filename);

        if (!Files.exists(dataPath)) {
            throw new IOException("Data directory ./data does not exist");
        }

        if (!Files.exists(filePath)) {
            throw new IOException("File " + filename + " does not exist in ./data folder");
        }

        if (!Files.isRegularFile(filePath)) {
            throw new IOException("Path " + filename + " is not a regular file");
        }

        return Files.readString(filePath);
    }

    /**
     * Saves a list of questions to a JSON file in the ./data folder
     * 
     * @param filename  the name of the JSON file to save (e.g., "questions.json")
     * @param questions the list of questions to save
     * @throws IOException              if there's an error writing the file
     * @throws IllegalArgumentException if the filename is null or empty, or if
     *                                  questions is null
     */
    public static void saveQuestionsToJsonFile(String filename, List<QuestionDto> questions) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        if (questions == null) {
            throw new IllegalArgumentException("Questions list cannot be null");
        }

        Path dataPath = Paths.get(dataFolder);

        // Ensure the data directory exists
        if (!Files.exists(dataPath)) {
            Files.createDirectories(dataPath);
        }

        // Convert questions to JSON string
        String jsonContent = QuestionUtils.convertToJsonString(questions);

        // Write to file
        Path filePath = dataPath.resolve(filename);
        Files.writeString(filePath, jsonContent);
    }

    public static final String getDataFolder() {
        return dataFolder;
    }

    public static final void setDataFolder(String dataFolder) {
        FileUtils.dataFolder = dataFolder;
    }
}
