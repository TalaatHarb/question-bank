package net.talaatharb.questionbank.utils;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.talaatharb.questionbank.config.HelperBeans;
import net.talaatharb.questionbank.dto.QuestionDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionUtils {

    public static List<QuestionDto> convertToQuestionDtoList(String json) throws IOException {
        var objectMapper = HelperBeans.buildObjectMapper();
        
        return objectMapper.readValue(json, new TypeReference<List<QuestionDto>>() {});
    }
    
    /**
     * Converts a list of QuestionDto objects to a JSON string
     * 
     * @param questions the list of questions to convert
     * @return JSON string representation of the questions
     * @throws JsonProcessingException if there's an error during JSON serialization
     */
    public static String convertToJsonString(List<QuestionDto> questions) throws JsonProcessingException {
        var objectMapper = HelperBeans.buildObjectMapper();
        
        return objectMapper.writeValueAsString(questions);
    }
}
