package application;

import java.util.List;
import java.util.ArrayList;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class PictureTransmission extends Thread{
	
	//���� Ǫ�ø޽���

    	/*
    	//COSTAMP GCM API KEY
        Sender sender = new Sender("AIzaSyAlIF12oXLOCfhLr_gpron52t2_PcRDjvw");
        //��� ��ū��
        String regId = "cIwnfBAcC4E:APA91bF0UoC-1zAmUF4xwJsR_L3WM7GXZX8oepgEJrwmAxAbhUMn7ipdWA8ntmjC-skAZOsiv3T2HUT-qoVcqK7Qx4MJtDqMahSH5acxXLRK5o5_aCE5tHWvaUtcR-eMmxxDGX12X_z-";
        */
        //COSTAMP GCM API KEY
        @Override
	public void run() {
		// TODO Auto-generated method stub
        	
		super.run();

		Sender sender = new Sender("AIzaSyAlIF12oXLOCfhLr_gpron52t2_PcRDjvw");
        //��� ��ū��
        String regId = "eMY8JIKUcEQ:APA91bFir_RIimLu_tgKdaNytyuoQIbNFrlk5GAIdcSFw-CEvwPVAEk6RtdeSNQiffE2fEPj5garQd7emjTLsVLuK5skOBIusrXaLOEx_51d3YM7AgUCWSlVjmyABXH4ejUZSBZNwdnb";
        
        //Ǫ�̸޽��� �޽��� & �ѱ��� �ȵ� ��
        Message message = new Message.Builder().addData("result", "Receive Picture").build();
        
        List<String> list = new ArrayList<String>();
        list.add(regId);
        try{
            MulticastResult multiResult = sender.send(message, list, 4);
            if (multiResult != null) {
                List<Result> resultList = multiResult.getResults();
                for (Result result : resultList) {
                	
                	//msgID �� �߸� ���ۿϷ�
                    System.out.println(result.getMessageId());
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
	}
	
}