package application;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient extends Thread{
		@Override
		public void run() {

			String serverIp = "1.255.57.236";
			Socket socket = null;
			
			//���� ����
			try {
				socket = new Socket(serverIp,7777);
				System.out.println("/////////������ ����Ǿ����ϴ�///////////");
				
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                outstream.writeUTF("transmission");
                outstream.flush();
				
				FileSender fs = new FileSender(socket);
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
	class FileSender extends Thread{
		Socket socket;
		DataOutputStream dos;
		FileInputStream fis;
		BufferedInputStream bis;
		
		public FileSender(Socket socket){
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
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		}
		
		

}
