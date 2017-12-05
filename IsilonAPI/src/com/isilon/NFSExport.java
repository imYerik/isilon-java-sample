package com.isilon;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

public class NFSExport {

	String statUrl = "/platform/2/protocols/nfs/";

	public HttpResponse createNfsExport(ConfigAPISession session, JSONArray pathArray, JSONArray roArray, JSONArray rwArray) {

		String url = "https://" + session.HOST + ":" + session.PORT + statUrl + "exports";
		try {
			HttpPost postRequest = new HttpPost(url);
			CloseableHttpClient client = HttpClients.createDefault();

			// HTTP Headers
			postRequest.setHeader("Cookie", session.isisessid);
			postRequest.setHeader("content-type","application/json");

		
			// HTTP Body
			JSONObject jsonBody = new JSONObject();
			jsonBody.put("paths", pathArray);			
			jsonBody.put("read_only_clients",roArray);
			jsonBody.put("read_write_clients",rwArray);
			
			postRequest.setEntity(new StringEntity(jsonBody.toString()));


			// Send HTTP POST Request
			HttpResponse response = client.execute(postRequest);

			return response;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
