package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class fileNumListener extends Thread{
    @Override
    public void run() {

        //���� ������ & ��Ʈ
        Socket socket = null;
        try {
            socket = new Socket("1.255.57.236",8878);
            //���̴����� reject ��ȯ
            ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
            outstream.writeUTF("request");
            outstream.flush();
            ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
            //���� ���� �ҷ�����
            CameraCVController.fNumber = instream.readInt();
            outstream.close();
            instream.close();
            socket.close();
            //mThumbIds�� fileLength������ �����Ҵ�
           
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}