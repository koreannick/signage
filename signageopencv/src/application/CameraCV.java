package application;
	
/*
 * �ֱ��۾���¥ : 2016-7-13
 * �۾��� : ������
 * ī�޶� �Կ� ������
 */

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class CameraCV extends Application {

	
	@Override
	public void start(Stage primaryStage) {
		//ī�޶� �Կ��� ���� ������ ���� �Լ�

		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/CameraCV.fxml"));
			//FXML�� ����
			BorderPane rootElement = (BorderPane) loader.load();
			Scene scene = new Scene(rootElement, 800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Signage Camera");
			primaryStage.setScene(scene);
			primaryStage.show();
			

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
