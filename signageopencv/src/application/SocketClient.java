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
			
			//서버 연결
			try {
				socket = new Socket(serverIp,7777);
				System.out.println("/////////서버에 연결되었습니다///////////");
				
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
			
			//데이터 전송용 스트림 생성
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

				
				System.out.println("파일 전송을 시작합니다.");
				
				//파일 이름 전송
				//String fName = "upload.png";
				String fName = CameraCVController.fName;
				dos.writeUTF(fName);
				System.out.printf("파일이름(%s)을 전송하였습니다. ",fName);
				
				//파일 내용을 읽으면서 전솔
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
				System.out.println("파일 전송작업을 완료했습니다.");
				System.out.println("보낸 파일의 사이즈 : " + f.length());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		}
		
		

}
