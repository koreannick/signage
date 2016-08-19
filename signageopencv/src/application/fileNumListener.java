package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class fileNumListener extends Thread{
    @Override
    public void run() {

        //소켓 아이피 & 포트
        Socket socket = null;
        try {
            socket = new Socket("1.255.57.236",8878);
            //사이니지에 reject 반환
            ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
            outstream.writeUTF("request");
            outstream.flush();
            ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
            //파일 갯수 불러오기
            CameraCVController.fNumber = instream.readInt();
            outstream.close();
            instream.close();
            socket.close();
            //mThumbIds를 fileLength에따라 동적할당
           
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}