package com.isilon;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

public class IsilonMain {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		// TODO Auto-generated method stub

		// Isilon Cluster API Endpoint
		String HOST = "api.isilon.com";     //This domain name should be the same as FQDN in HTTPS certificate.
		String USER = "root";
		String PASSWORD = "Password1!";
		String PORT = "8080";
		
		try {

			// Create a session Cookie, Session Cookie will be used in following samples.
			ConfigAPISession session = new ConfigAPISession(HOST, PORT, USER, PASSWORD);
			session.createSession();

			
			/* System Configuration Sample
			 * 
			 * Get Cluster Staticstics
			 * Get SmartQuotas summary
			 * Create a NFS export
			 * */
			// Get ClusterStaticstics
			ClusterStatistics stat = new ClusterStatistics();
			HttpResponse statResp = stat.getClusterStatistics(session);
//			System.out.println("Cluster Statitistics: " + EntityUtils.toString(statResp.getEntity()));

			// Get SmartQuotas summary
			SmartQuotas quota = new SmartQuotas();
			HttpResponse quotaResp = quota.getQuotas(session);
//			System.out.println("Quotas Summary: " + EntityUtils.toString(quotaResp.getEntity()));

			// Create NFS export	
			JSONArray pathArray = new JSONArray();
			pathArray.put("/ifs/gaozy/nfs1");
			pathArray.put("/ifs/gaozy/nfs2");
			JSONArray roArray = new JSONArray();
			roArray.put("192.168.10.0");
			JSONArray rwArray = new JSONArray();
			rwArray.put("192.168.10.11");
		
			NFSExport nfsExp = new NFSExport();
			HttpResponse createNfsResp = nfsExp.createNfsExport(session, pathArray,roArray,rwArray);
			System.out.println("Create NFS Export: " + createNfsResp.getStatusLine());
		
			
			/*  FileSystemAccess Samples
			 *  Create AccessPoint
			 *  List AccessPoint
			 *  Create Directory
			 *  Put File
			 *  Get File
			 *   Delete File
			 */
			
			FileSystemAccess fsa = new FileSystemAccess();
			String accessPoint = "gaozy";
			String ifsPath = "/ifs/gaozy/api";

			//			Create AccessPoint & List
			HttpResponse createApResp = fsa.createAccessPoint(session, accessPoint, ifsPath);	
			System.out.println("Create AccessPoint: " + createApResp.getStatusLine());
			

			//			List All AccessPoint Under user root
			HttpResponse fsaListResp = fsa.listAccessPoint(session);
			fsa.listAccessPoint(session);
			System.out.println("FileSystemAccessPoint: " + EntityUtils.toString(fsaListResp.getEntity()));

			
			//			Create Directory
			String containerPath = "directory02";
			HttpResponse createDirResp = fsa.createDirectory(session, accessPoint, containerPath);
			System.out.println("Create Directory: " + createDirResp.getStatusLine());
			
			
			//			Put a File From local
			File localFile = new File("C:\\test.png");
			String objectName = "test1.png";
			HttpResponse putFileResp = fsa.putFile(session, accessPoint, containerPath, objectName, localFile);
			System.out.println("Put File: " + putFileResp.getStatusLine());

			//			List Directory Contents
			HttpResponse listDirResp = fsa.listDirectory(session, accessPoint, containerPath);
			System.out.println("List Directory: " + EntityUtils.toString(listDirResp.getEntity()));

			//			Get a File to local
			File localFile2 = new File("C:\\Users\\gaoz2\\Downloads\\download-test.png");
			HttpResponse getFileResp = fsa.getFile(session, accessPoint, containerPath, objectName, localFile2);
			System.out.println("Get File: " + getFileResp.getStatusLine());
			

			//			Delete a File	
			HttpResponse deleteFileResp = fsa.deleteFile(session, accessPoint, containerPath, objectName);
			System.out.println("Delete File: " + deleteFileResp.getStatusLine());
					

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
