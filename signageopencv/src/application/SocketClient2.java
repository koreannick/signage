package application;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient2 extends Thread{
	@Override
	public void run() {

		String serverIp = "1.255.57.236";
		Socket socket = null;
		
		//���� ����
		try {
			socket = new Socket(serverIp,7777);
			

            ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
            outstream.writeUTF("preview");
            outstream.flush();
            
			System.out.println("/////////�̸����� �̹����� ���۵Ǿ����ϴ�.///////////");
			
			
			FileSender2 fs = new FileSender2(socket);
			fs.start();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}


class FileSender2 extends Thread{
	Socket socket;
	DataOutputStream dos;
	FileInputStream fis;
	BufferedInputStream bis;
	
	public FileSender2(Socket socket){
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
			String fName = "upload.png";
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
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	

}
