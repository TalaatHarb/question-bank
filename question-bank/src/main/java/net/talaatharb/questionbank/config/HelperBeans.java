package net.talaatharb.questionbank.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.talaatharb.questionbank.service.QuestionService;
import net.talaatharb.questionbank.service.QuestionServiceImpl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HelperBeans {

	private static ObjectMapper objectMapper;
	private static QuestionService questionService;

	public static final ObjectMapper buildObjectMapper() {
		if (objectMapper == null) {
			log.info("Creating new ObjectMapper bean");
			objectMapper = JsonMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS) // ignore case
					.enable(SerializationFeature.INDENT_OUTPUT) // pretty format for json
					.addModule(new JavaTimeModule()) // time module
					.build();
		} else {
			log.debug("Reusing existing ObjectMapper bean");
		}
		return objectMapper;
	}

	/**
	 * Creates a QuestionService bean instance (singleton)
	 * 
	 * @return QuestionService implementation
	 */
	public static final QuestionService buildQuestionService() {
		if (questionService == null) {
			log.info("Creating new QuestionService bean");
			questionService = new QuestionServiceImpl();
		} else {
			log.debug("Reusing existing QuestionService bean");
		}
		return questionService;
	}
}
