package signage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class signage extends  Application{

	public static void main(String[] args) {
		


		// TODO Auto-generated method stub
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/signage/signage.fxml"));
		Pane rootElement = (Pane) loader.load();
		Scene scene = new Scene(rootElement);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle("Signage");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
