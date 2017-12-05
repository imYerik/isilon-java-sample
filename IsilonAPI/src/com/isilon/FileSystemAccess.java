package com.isilon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public class FileSystemAccess {

	String namespaceUrl = "/namespace/";

	public HttpResponse createAccessPoint(ConfigAPISession session, String accessPoint, String ifsPath) {

		String url = "https://" + session.HOST + ":" + session.PORT + namespaceUrl + "/" + accessPoint;

		try {
			HttpPut putRequest = new HttpPut(url);
			CloseableHttpClient client = HttpClients.createDefault();

			// HTTP Headers
			putRequest.setHeader("Cookie", session.isisessid);

			// HTTP Body
			/* {"path" : "/ifs/gaozy/api"} */
			JSONObject jsonBody = new JSONObject();
			jsonBody.put("path", ifsPath);
			putRequest.setEntity(new StringEntity(jsonBody.toString()));

			// Send HTTP PUT Request
			HttpResponse response = client.execute(putRequest);
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public HttpResponse createDirectory(ConfigAPISession session, String accessPoint, String containerPath) {

		String url = "https://" + session.HOST + ":" + session.PORT + namespaceUrl + "/" + accessPoint + "/"
				+ containerPath + "/";

		try {
			HttpPut putRequest = new HttpPut(url);
			CloseableHttpClient client = HttpClients.createDefault();

			// HTTP Headers
			putRequest.setHeader("Cookie", session.isisessid);
			putRequest.setHeader("x-isi-ifs-target-type", "container");

			// Sent HTTP PUT Request
			HttpResponse response = client.execute(putRequest);
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public HttpResponse putFile(ConfigAPISession session, String accessPoint, String containerPath, String objectName,
			File localFile) {

		String url = "https://" + session.HOST + ":" + session.PORT + namespaceUrl + "/" + accessPoint + "/"
				+ containerPath + "/" + objectName;

		try {
			HttpPut putRequest = new HttpPut(url);
			CloseableHttpClient client = HttpClients.createDefault();

			// File input stream
			InputStream stream = new FileInputStream(localFile);
			InputStreamEntity entity = new InputStreamEntity(stream, localFile.length());
			putRequest.setEntity(entity);

			// HTTP Headers
			putRequest.setHeader("Cookie", session.isisessid);
			putRequest.setHeader("x-isi-ifs-target-type", "object");
			putRequest.setHeader("Content-Type", "image/png");

			// Send HTTP PUT Request
			HttpResponse response = client.execute(putRequest);

			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public HttpResponse getFile(ConfigAPISession session, String accessPoint, String containerPath, String objectName,
			File localFile) {

		String url = "https://" + session.HOST + ":" + session.PORT + namespaceUrl + "/" + accessPoint + "/"
				+ containerPath + "/" + objectName;

		try {
			HttpGet getRequest = new HttpGet(url);
			CloseableHttpClient client = HttpClients.createDefault();

			// HTTP Headers
			getRequest.setHeader("Cookie", session.isisessid);

			// Send HTTP GET Request
			HttpResponse response = client.execute(getRequest);

			// Get HTTP Response Content, Write filestream to a local file
			InputStream is = response.getEntity().getContent();
			FileOutputStream fos = new FileOutputStream(localFile);
			int read = 0;
			byte[] buffer = new byte[32768];
			while ((read = is.read(buffer)) > 0) {
				fos.write(buffer, 0, read);
			}

			fos.close();
			is.close();

			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public HttpResponse deleteFile(ConfigAPISession session, String accessPoint, String containerPath,
			String objectName) {

		String url = "https://" + session.HOST + ":" + session.PORT + namespaceUrl + "/" + accessPoint + "/"
				+ containerPath + "/" + objectName;

		try {
			HttpDelete deleteRequest = new HttpDelete(url);
			CloseableHttpClient client = HttpClients.createDefault();

			// HTTP Headers
			deleteRequest.setHeader("Cookie", session.isisessid);

			// HTTP GET Request
			HttpResponse response = client.execute(deleteRequest);

			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public HttpResponse listAccessPoint(ConfigAPISession session) {

		String url = "https://" + session.HOST + ":" + session.PORT + namespaceUrl + "/";
		try {
			HttpGet getRequest = new HttpGet(url);
			CloseableHttpClient client = HttpClients.createDefault();

			// HTTP Headers
			getRequest.setHeader("Cookie", session.isisessid);

			// Send HTTP PUT Request
			HttpResponse response = client.execute(getRequest);

			return response;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public HttpResponse listDirectory(ConfigAPISession session, String accessPoint, String containerPath) {

		String url = "https://" + session.HOST + ":" + session.PORT + namespaceUrl + "/" + accessPoint + "/"
				+ containerPath;
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
