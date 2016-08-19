package application;

/*
 * 최근작업날짜 : 2016-7-13
 * 작업자 : 김진수
 * 카메라 스트리밍 및 이미지 저장
 */

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import PopUp.PopUp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CameraCVController {

	static int fNumber;	
	static String fName;

	//몇번째 사진인가?
	
	@FXML
	private Button button;
	@FXML
	private Button button2;
	//JAVAFX 버튼
	@FXML
	private Button send_btn;
	@FXML
	private ImageView currentFrame;
	//JAVAFX 이미지뷰
	private ScheduledExecutorService timer;
	//주기적으로 이미지를 보여주기 위한 timer
	private VideoCapture capture = new VideoCapture();
	//opneCV에서 사용하는 장치 연결 객체
	private boolean cameraActive = false;
	//카메라의 유무 작동을 위한 변수
	
	@FXML
	protected void startCamera(ActionEvent event)
	//카메라가 작동하는 함수, FXML의 버튼의 액션 함수
	{
		fileNumListener fileNum = new fileNumListener();
		fileNum.start();
		
		if (!this.cameraActive)//카메라가 꺼져있다면
		{
			this.capture.open(0); //1번 카메라와 연결// 노트북 내장 카메라가 0번, USB카메라가 1번 
			
			if (this.capture.isOpened()) //카메라 상태가 연결되어있다면 true리턴
			{
				this.cameraActive = true;
				
				Runnable frameGrabber = new Runnable() {	
				//이미지를 주기적으로 보여주기 위한 스레드

					@Override
					public void run() {						
					//이미지 처리 run()
						// TODO Auto-generated method stub
						Image imageToShow = grabFrame();
						//현재 이미지를 가지는 변수
						
						currentFrame.setImage(imageToShow);
						//FXML의 화면에 현재 이미지를 set
						
					}
				};
				this.timer = Executors.newSingleThreadScheduledExecutor();
				//이미지 처리를 주기적으로 실행시켜주는 작업 실행자 선언.
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				//이미지를 주기적으로 실행시켜주는 함수
				//이미지를 처음 가져오고, 33ms로 주기적 실행
				this.send_btn.setText("사진찍는중....");
				this.button.setText("사진 찍기");
				
				//버튼의 텍스트를 바꿔줌
			}	
			else
			{
				System.err.println("Impossible to open the camera connection...");
				//에러 메세지 출력
			}
		}
		else
		{
			
			this.cameraActive = false;
			//카메라가 작동될때
			this.send_btn.setText("전송");
			this.button.setText("카메라 시작");
			
			//텍스트를 바꿔준다
			
			try
			{
				
				this.timer.shutdown();
				//timer를 안전한 절차에 따른 종료
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
				//timer가 종료상태에 들어갈 때 까지 기다린다.
				
			}
			catch (InterruptedException e)
			{
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
			
			this.capture.release();
			
			fName = "upload"+Integer.toString(fNumber)+".png";
			//카메라 장치 연결 해제
			File file = new File("C:\\Users\\korea\\"+fName);
			//카메라가 해제되는 경우 현재 상태에 저장된 이미지
			Image image = new Image(file.toURI().toString());
			//이미지 변수에 위에 만든 file 객체의 URI를 추가
			currentFrame.setImage(image);
			//현재 저장된 이미지를 화면에 출력
			
			//여기서 부터는 전송
			
			//푸싱 메시지 전송
			PictureTransmission picturetransmission = new PictureTransmission();
			picturetransmission.start();
			
			//안드로이드 클라이언트에 사진전송
			SocketServer socketServer = new SocketServer();
			socketServer.start();
			
			//미리보기 서버에 사진전송
			SocketClient2 socketClient2 = new SocketClient2();
			socketClient2.start();
			
			//다음 사진 저장을 위한 변수
			System.out.println(fName);
		}
	}
	
	@FXML
	protected void startPop(ActionEvent event)
	//카메라 동작을 멈추는 함수
	{
		if(this.send_btn.getText().equals("전송"))
		{
			Platform.runLater(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					PopUp pu = new PopUp();
					pu.start(new Stage());
				}
				
			});
		}
	
			
	}
	
	@FXML
	protected void stopCamera(ActionEvent event)
	//카메라 동작을 멈추는 함수
	{
		
		this.capture.release();
		
		Stage stage = (Stage) button2.getScene().getWindow();
		stage.close();
		
	}


	protected Image grabFrame() {
	//이미지를 가져오는 함수
		Image imageToShow = null;
		//이미지를 리턴해 주는 변수
		Mat frame = new Mat();
		/*
		 * openCV에서 사용하는 클래스
		 * Mat클래스는 매트릭스를 뜻함.
		 * 2개의 데이터를 가지고 있는데 1개는 매트릭스 헤더와 실제 이미지 픽셀값들이 저장되는 데이터 매트릭스에 대한 포인터를 가지고 있음.
		 * 매트릭스 헤더에는 매트릭스의 크기와 저장된 방법, 매트릭스가 저장된 주소를 가지고 있음.
		 * 쉽게 생각하면 이미지 값을 가지고 있다고 생각해도 됨.
		 */
		if (this.capture.isOpened())
			//장치가 연결되어 있는 상태
		{
			try
			{
				this.capture.read(frame);
				//현재 이미지가 장치가 이미지를 해석하고 읽어온후 다음 프레임을 리턴
				if (!frame.empty())
					//현재 프레임이 비어있지 않다면
				{
					
					imageToShow = mat2Image(frame);
					//이미지 값을 저장
				}
			}
			catch (Exception e)
			{
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		return imageToShow;
		//장치에서 읽은 이미지 리턴
	}


	private Image mat2Image(Mat frame) {
		//Mat형식의 이미지를 Image변수 형식으로 변환시키는 함수
		MatOfByte buffer = new MatOfByte();
		
		Imgcodecs.imencode(".png", frame, buffer);
		//메모리 안의 버퍼로부터 이미지를 읽는다. Mat의 이미지를 png형태로 변환시키는 작업
		//Imgcodecs.imwrite("C:\\Users\\korea\\upload.png", frame);

		fName = "upload"+Integer.toString(fNumber)+".png";
		Imgcodecs.imwrite("C:\\Users\\korea\\"+fName,frame);
		//그 변환된 내용을 저장한다.
		return new Image(new ByteArrayInputStream(buffer.toArray()));
		//Image로 변환해서 리턴
	}
	
}
