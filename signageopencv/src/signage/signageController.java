package signage;

/*
 * �ֱ��۾���¥ : 2016-7-13
 * �۾��� : ������
 * ī�޶� ��Ʈ���� �� �̹��� ����
 */

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import application.CameraCV;
import javafx.stage.Stage;

public class signageController {
	Stage stage;
	String[] args;
	
	@FXML
	private Button Camera_btn;
	
	
	@FXML
	public void Camera(ActionEvent event)
	{
		Platform.setImplicitExit(true);
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				CameraCV CV = new CameraCV();
				CV.start(new Stage());
			}
			
		});

	}
	
	/*public void restart()
	//ī�޶� ������ ���ߴ� �Լ�
	{
		
		this.capture.release();
		//�����ư�� ī�޶� ��ġ ���� ����
		Platform.setImplicitExit(true);
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				CameraCV cv = new CameraCV();
				 cv.start(new Stage());
			}
			
		});
		
		//���� ������ ����

	}*/
	
}
