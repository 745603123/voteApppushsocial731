package com.xl.voteapp.upload;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.xl.voteapp.common.Contanst;
import com.xl.voteapp.util.ErrorCode;

import android.widget.Toast;

/*import com.wangjialin.internet.userInformation.post.R;*/

public class UploadVotingInformation {
	public static boolean save(String i_id) throws Exception{
	//	String path = "http://192.168.191.1:8080/votefinalserver150310/AddVote";
		String path = Contanst.URLPATH+"AddVote";
		Map<String, String> params = new HashMap<String, String>();
		params.put("I_id", i_id);
		return sendPOSTRequest(path, params, "UTF-8");
	}

	/**
	 * 发送POST请求
	 * @param path 请求路径
	 * @param params 请求参数
	 * @return
	 */
	private static boolean sendPOSTRequest(String path, Map<String, String> params, String encoding) throws Exception{
		//  title=liming&length=30
		StringBuilder sb = new StringBuilder();
		if(params!=null && !params.isEmpty()){
			for(Map.Entry<String, String> entry : params.entrySet()){
				sb.append(entry.getKey()).append("=");
				sb.append(URLEncoder.encode(entry.getValue(), encoding));
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		byte[] data = sb.toString().getBytes();
		
		HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);//允许对外传输数据
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", data.length+"");
		OutputStream outStream = conn.getOutputStream();
		outStream.write(data);
		outStream.flush();
		if(conn.getResponseCode() == 200){
			DataInputStream inputStream = new DataInputStream(conn.getInputStream());
		    String DC=inputStream.readUTF();
			if(DC.equals(ErrorCode.VOTING_SUCCESS))		
			return true;
			if(DC.equals(ErrorCode.VOTING_FAIL))
			return false;
		}
		 return false;
	}
}
