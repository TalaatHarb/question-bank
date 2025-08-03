package net.talaatharb.questionbank;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.talaatharb.questionbank.ui.controllers.MainUiController;

public class JavafxApplication extends Application {

	public static final String CSS_FILE = "ui/theme.css";
	private static final int HEIGHT = 600;
	private static final String MAIN_FXML = "ui/MainWindow.fxml";
	private static final String ICON_FILE = "ui/logo.jpg";
	private static final String TITLE = "Question Bank Manager";
	private static final int WIDTH = 800;

	@Override
	public void start(Stage primaryStage) throws Exception {
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MAIN_FXML));
		final Parent root = fxmlLoader.load();

		// Get the controller and set the primary stage
		MainUiController controller = fxmlLoader.getController();
		controller.setPrimaryStage(primaryStage);

		final Image icon = new Image(getClass().getResourceAsStream(ICON_FILE));
		final Scene scene = new Scene(root, WIDTH, HEIGHT);
		scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.setTitle(TITLE);
		primaryStage.getIcons().add(icon);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		Platform.exit();
	}

}
