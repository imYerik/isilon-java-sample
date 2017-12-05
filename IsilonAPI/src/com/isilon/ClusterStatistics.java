package com.isilon;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ClusterStatistics {

	String statUrl = "/platform/1/statistics/";

	public HttpResponse getClusterStatistics(ConfigAPISession session) {

		String url = "https://" + session.HOST + ":" + session.PORT + statUrl + "keys";
		try {
			HttpGet getRequest = new HttpGet(url);
			CloseableHttpClient client = HttpClients.createDefault();

			// HTTP Headers
			getRequest.setHeader("Cookie", session.isisessid);

			// Send HTTP GET Request
			HttpResponse response = client.execute(getRequest);

			return response;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
