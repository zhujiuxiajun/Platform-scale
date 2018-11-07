package com.zhujiu.scale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RESTFulGetUtil {
	private static final String targetURL = "http://114.115.142.224:8080/nbim/api/outif/scale/lastflowid";
	private static final String ERROR_IO = "ERRORIO";
	private static final String ERROR_URL = "ERRORURL";

	public static void main(String[] args) {

		try {
			URL restServiceURL = new URL(targetURL);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Accept", "application/json");
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException(
						"HTTP GET Request Failed with Error code : " + httpConnection.getResponseCode());
			}
			String charset = "UTF-8";
			Pattern pattern = Pattern.compile("charset=\\S*");
			Matcher matcher = pattern.matcher(httpConnection.getContentType());
			if (matcher.find()) {
				charset = matcher.group().replace("charset=", "");
			}
			BufferedReader responseBuffer = new BufferedReader(
					new InputStreamReader((httpConnection.getInputStream()), charset));
			String output;
			System.out.println("Output from Server:  \n");
			while ((output = responseBuffer.readLine()) != null) {
				System.out.println(output);
			}
			httpConnection.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get·½·¨£¬json×Ö·û´®
	 * @param targetURL
	 * @return
	 */
	public static String doGet(String targetURL) {
		StringBuffer sb = new StringBuffer();
		try {
			URL restServiceURL = new URL(targetURL);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Accept", "application/json");
			if (httpConnection.getResponseCode() != 200) {
				//throw new RuntimeException("HTTP GET Request Failed with Error code : " + httpConnection.getResponseCode());
				return ERROR_URL;
			}
			String charset = "UTF-8";
			Pattern pattern = Pattern.compile("charset=\\S*");
			Matcher matcher = pattern.matcher(httpConnection.getContentType());
			if (matcher.find()) {
				charset = matcher.group().replace("charset=", "");
			}
			BufferedReader responseBuffer = new BufferedReader(
					new InputStreamReader((httpConnection.getInputStream()), charset));
			String line;
			while ((line = responseBuffer.readLine()) != null) {
				sb.append(line + "\n");
			}
			responseBuffer.close();
			httpConnection.disconnect();
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			return ERROR_URL;
		} catch (IOException e) {
			//e.printStackTrace();
			return ERROR_IO;
		}
		return sb.toString().replace("\n", "");
	}
}
