package application;

/*
 * �ֱ��۾���¥ : 2016-7-13
 * �۾��� : ������
 * ī�޶� ��Ʈ���� �� �̹��� ����
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

	//���° �����ΰ�?
	
	@FXML
	private Button button;
	@FXML
	private Button button2;
	//JAVAFX ��ư
	@FXML
	private Button send_btn;
	@FXML
	private ImageView currentFrame;
	//JAVAFX �̹�����
	private ScheduledExecutorService timer;
	//�ֱ������� �̹����� �����ֱ� ���� timer
	private VideoCapture capture = new VideoCapture();
	//opneCV���� ����ϴ� ��ġ ���� ��ü
	private boolean cameraActive = false;
	//ī�޶��� ���� �۵��� ���� ����
	
	@FXML
	protected void startCamera(ActionEvent event)
	//ī�޶� �۵��ϴ� �Լ�, FXML�� ��ư�� �׼� �Լ�
	{
		fileNumListener fileNum = new fileNumListener();
		fileNum.start();
		
		if (!this.cameraActive)//ī�޶� �����ִٸ�
		{
			this.capture.open(0); //1�� ī�޶�� ����// ��Ʈ�� ���� ī�޶� 0��, USBī�޶� 1�� 
			
			if (this.capture.isOpened()) //ī�޶� ���°� ����Ǿ��ִٸ� true����
			{
				this.cameraActive = true;
				
				Runnable frameGrabber = new Runnable() {	
				//�̹����� �ֱ������� �����ֱ� ���� ������

					@Override
					public void run() {						
					//�̹��� ó�� run()
						// TODO Auto-generated method stub
						Image imageToShow = grabFrame();
						//���� �̹����� ������ ����
						
						currentFrame.setImage(imageToShow);
						//FXML�� ȭ�鿡 ���� �̹����� set
						
					}
				};
				this.timer = Executors.newSingleThreadScheduledExecutor();
				//�̹��� ó���� �ֱ������� ��������ִ� �۾� ������ ����.
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				//�̹����� �ֱ������� ��������ִ� �Լ�
				//�̹����� ó�� ��������, 33ms�� �ֱ��� ����
				this.send_btn.setText("���������....");
				this.button.setText("���� ���");
				
				//��ư�� �ؽ�Ʈ�� �ٲ���
			}	
			else
			{
				System.err.println("Impossible to open the camera connection...");
				//���� �޼��� ���
			}
		}
		else
		{
			
			this.cameraActive = false;
			//ī�޶� �۵��ɶ�
			this.send_btn.setText("����");
			this.button.setText("ī�޶� ����");
			
			//�ؽ�Ʈ�� �ٲ��ش�
			
			try
			{
				
				this.timer.shutdown();
				//timer�� ������ ������ ���� ����
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
				//timer�� ������¿� �� �� ���� ��ٸ���.
				
			}
			catch (InterruptedException e)
			{
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
			
			this.capture.release();
			
			fName = "upload"+Integer.toString(fNumber)+".png";
			//ī�޶� ��ġ ���� ����
			File file = new File("C:\\Users\\korea\\"+fName);
			//ī�޶� �����Ǵ� ��� ���� ���¿� ����� �̹���
			Image image = new Image(file.toURI().toString());
			//�̹��� ������ ���� ���� file ��ü�� URI�� �߰�
			currentFrame.setImage(image);
			//���� ����� �̹����� ȭ�鿡 ���
			
			//���⼭ ���ʹ� ����
			
			//Ǫ�� �޽��� ����
			PictureTransmission picturetransmission = new PictureTransmission();
			picturetransmission.start();
			
			//�ȵ���̵� Ŭ���̾�Ʈ�� ��������
			SocketServer socketServer = new SocketServer();
			socketServer.start();
			
			//�̸����� ������ ��������
			SocketClient2 socketClient2 = new SocketClient2();
			socketClient2.start();
			
			//���� ���� ������ ���� ����
			System.out.println(fName);
		}
	}
	
	@FXML
	protected void startPop(ActionEvent event)
	//ī�޶� ������ ���ߴ� �Լ�
	{
		if(this.send_btn.getText().equals("����"))
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
	//ī�޶� ������ ���ߴ� �Լ�
	{
		
		this.capture.release();
		
		Stage stage = (Stage) button2.getScene().getWindow();
		stage.close();
		
	}


	protected Image grabFrame() {
	//�̹����� �������� �Լ�
		Image imageToShow = null;
		//�̹����� ������ �ִ� ����
		Mat frame = new Mat();
		/*
		 * openCV���� ����ϴ� Ŭ����
		 * MatŬ������ ��Ʈ������ ����.
		 * 2���� �����͸� ������ �ִµ� 1���� ��Ʈ���� ����� ���� �̹��� �ȼ������� ����Ǵ� ������ ��Ʈ������ ���� �����͸� ������ ����.
		 * ��Ʈ���� ������� ��Ʈ������ ũ��� ����� ���, ��Ʈ������ ����� �ּҸ� ������ ����.
		 * ���� �����ϸ� �̹��� ���� ������ �ִٰ� �����ص� ��.
		 */
		if (this.capture.isOpened())
			//��ġ�� ����Ǿ� �ִ� ����
		{
			try
			{
				this.capture.read(frame);
				//���� �̹����� ��ġ�� �̹����� �ؼ��ϰ� �о���� ���� �������� ����
				if (!frame.empty())
					//���� �������� ������� �ʴٸ�
				{
					
					imageToShow = mat2Image(frame);
					//�̹��� ���� ����
				}
			}
			catch (Exception e)
			{
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		return imageToShow;
		//��ġ���� ���� �̹��� ����
	}


	private Image mat2Image(Mat frame) {
		//Mat������ �̹����� Image���� �������� ��ȯ��Ű�� �Լ�
		MatOfByte buffer = new MatOfByte();
		
		Imgcodecs.imencode(".png", frame, buffer);
		//�޸� ���� ���۷κ��� �̹����� �д´�. Mat�� �̹����� png���·� ��ȯ��Ű�� �۾�
		//Imgcodecs.imwrite("C:\\Users\\korea\\upload.png", frame);

		fName = "upload"+Integer.toString(fNumber)+".png";
		Imgcodecs.imwrite("C:\\Users\\korea\\"+fName,frame);
		//�� ��ȯ�� ������ �����Ѵ�.
		return new Image(new ByteArrayInputStream(buffer.toArray()));
		//Image�� ��ȯ�ؼ� ����
	}
	
}
