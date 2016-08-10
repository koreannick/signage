package application;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
		
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		Socket socket =null;
		
		
		try {
			
			//������ ���� ���� �� ���
			serverSocket = new ServerSocket(5533);
			//����Ǹ� ��ſ� ���� ����
			socket = serverSocket.accept();
			System.out.println("///////////Ŭ���̾�Ʈ�� ����Ǿ����ϴ�.//////////////");

			ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
			String inStr = instream.readUTF();
			if(inStr.equals("accept")){
				//���� �۽� �۾� ����
				FileSendered fs = new FileSendered(socket);
				fs.start();
				serverSocket.close();
				
			}
			else if(inStr.equals("reject")){
				System.out.println("������ ��� �Ǿ����ϴ�");
				
				instream.close();
				serverSocket.close();
				socket.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

class FileSendered extends Thread{
	Socket socket;
	DataOutputStream dos;
	FileInputStream fis;
	BufferedInputStream bis;
	
	public FileSendered(Socket socket){
		this.socket = socket;
		
		//������ ���ۿ� ��Ʈ�� ����
		try {
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		try {

			System.out.println("���� ������ �����մϴ�.");
			
			//���� �̸� ����
			//String fName = "upload.png";
			String fName = CameraCVController.fName;
			dos.writeUTF(fName);
			System.out.printf("�����̸�(%s)�� �����Ͽ����ϴ�. ",fName);
			
			//���� ������ �����鼭 ����
			File f = new File("C:\\Users\\korea\\"+CameraCVController.fName);
			fis = new FileInputStream(f);
			bis = new BufferedInputStream(fis);

			int len;
			int size = 4096;
			byte[] data = new byte[size];
			
			while((len = bis.read(data)) != -1){
				dos.write(data, 0, len);
			}
			dos.flush();
			dos.close();
			bis.close();
			fis.close();
			System.out.println("���� �����۾��� �Ϸ��߽��ϴ�.");
			System.out.println("���� ������ ������ : " + f.length());
			
			//�����λ�������
			
			SocketClient socketClient = new SocketClient();
			socketClient.start();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	

}
