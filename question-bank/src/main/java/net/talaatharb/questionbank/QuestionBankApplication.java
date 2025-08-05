package net.talaatharb.questionbank;

import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuestionBankApplication{
	public static void main(String[] args) {
		log.debug("UI Application Starting");
		Application.launch(JavafxApplication.class, args);
	}
}