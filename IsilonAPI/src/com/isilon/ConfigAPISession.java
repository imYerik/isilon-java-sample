package com.isilon;

import java.io.IOException;
import java.util.Base64;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConfigAPISession {

	String HOST;
	String USER;
	String PASSWORD;
	String PORT;
	String isisessid;

	public ConfigAPISession(String host, String port, String user, String password) {
		HOST = host;
		USER = user;
		PASSWORD = password;
		PORT = port;
		isisessid = null;
		String certificatesTrustStorePath = new String("C:\\Program Files\\Java\\jre1.8.0_151\\lib\\security\\cacerts");
	    System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);
	    System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
	}



	public void createSession() {
//		构造HTTP请求，生成sessionid

		String url = "https://"+ HOST + ":"+ PORT + "/session/1/session";
		try {
			HttpPost postRequest = new HttpPost(url);
			CloseableHttpClient client = HttpClients.createDefault();	
			
//			构造HTTP Headers
			String encoding = Base64.getEncoder().encodeToString((USER + ":" + PASSWORD).getBytes("utf-8"));
			postRequest.setHeader("Authorization", "Basic "+encoding);		
			postRequest.setHeader("content-type","application/json");
			
//			构造HTTP JsonBody
			/*
			 * {
			 *     "username":"root",
			 *     "password":"Password1!",
			 *     "services":["platform","namespace"]
			 * }
			 * */
			JSONArray serviceArray = new JSONArray();
			serviceArray.put("platform");
			serviceArray.put("namespace");
			JSONObject jsonBody = new JSONObject();
			jsonBody.put("username", USER);
			jsonBody.put("password",PASSWORD);
			jsonBody.put("services", serviceArray);		
//			System.out.println("JsonBody:"+ jsonBody);		
			postRequest.setEntity(new StringEntity(jsonBody.toString()));
			
//			发送HTTP POST请求
			HttpResponse response = client.execute(postRequest);
			this.isisessid = extractSessionId(response);
			System.out.println("Session ID:" + isisessid);

//			输出HTTP Response Body 
			String responseAsString = EntityUtils.toString(response.getEntity());
			System.out.println("Session Infomation: " + responseAsString);		

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void closeSession() {
		
	}

	private String extractSessionId(HttpResponse response) {
		
		
		/*从HTTP响应Header消息中截取isisessid字段值
		 * 
		 * HTTP/1.1 201 Created 
		 * [
		 * Date: Mon, 04 Dec 2017 14:48:51 GMT, 
		 * Server: Apache/2.2.31 (FreeBSD) mod_ssl/2.2.31 OpenSSL/1.0.2j-fips mod_fastcgi/2.4.6, 
		 * Set-Cookie: isisessid=0b0c6e7e-4a0a-4660-b522-7bf083f0cb4c; path=/; HttpOnly; Secure, 
		 * Content-Length: 104, 
		 * Keep-Alive: timeout=15,max=500, 
		 * Connection: Keep-Alive, 
		 * Content-Type: application/json
		 * ]
		 * */
		
		Header [] setcookie = response.getHeaders("Set-Cookie");
		
		String []a = setcookie[0].toString().split(":");
		String []b = a[1].split(";");
		String sessionid = b[0];
				
		return sessionid;
	}

}
