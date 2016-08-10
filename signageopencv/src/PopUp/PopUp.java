package PopUp;
	
/*
 * �ֱ��۾���¥ : 2016-7-13
 * �۾��� : ������
 * ī�޶� �Կ� ������
 */


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


public class PopUp extends Application {
	
	@Override
	public void start(Stage primaryStage) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopUp/PopUp.fxml"));
			Pane rootElement = (Pane) loader.load();
			Scene scene = new Scene(rootElement);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("��������");
			primaryStage.setScene(scene);
			primaryStage.show();
			

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
