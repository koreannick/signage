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
			
			//리스너 소켓 생성 후 대기
			serverSocket = new ServerSocket(5533);
			//연결되면 통신용 소켓 생성
			socket = serverSocket.accept();
			System.out.println("///////////클라이언트와 연결되었습니다.//////////////");

			ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
			String inStr = instream.readUTF();
			if(inStr.equals("accept")){
				//파일 송신 작업 시작
				FileSendered fs = new FileSendered(socket);
				fs.start();
				serverSocket.close();
				
			}
			else if(inStr.equals("reject")){
				System.out.println("전송이 취소 되었습니다");
				
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
			
			//서버로사진전송
			
			SocketClient socketClient = new SocketClient();
			socketClient.start();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	

}
