package com.isilon;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class SmartQuotas {

	String quotaUrl = "/platform/1/quota/";

	public HttpResponse getQuotas(ConfigAPISession session) {

		String url = "https://" + session.HOST + ":" + session.PORT + quotaUrl + "quotas-summary";
		try {
			HttpGet getRequest = new HttpGet(url);
			CloseableHttpClient client = HttpClients.createDefault();

			// 构造HTTP Headers
			getRequest.setHeader("Cookie", session.isisessid);

			// 发送HTTP GET请求
			HttpResponse response = client.execute(getRequest);

			return response;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
