package signage;

/*
 * 최근작업날짜 : 2016-7-13
 * 작업자 : 김진수
 * 카메라 스트리밍 및 이미지 저장
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
	//카메라 동작을 멈추는 함수
	{
		
		this.capture.release();
		//종료버튼시 카메라 장치 연결 해제
		Platform.setImplicitExit(true);
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				CameraCV cv = new CameraCV();
				 cv.start(new Stage());
			}
			
		});
		
		//현재 프레임 종료

	}*/
	
}
