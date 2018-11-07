package com.zhujiu.scale;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RESTFulPostUtil {
	// private static final String targetURL =
	// "http://localhost:8080/JerseyJSONExample/rest/jsonServices/send";
	private static final String targetURL = "http://122.112.210.4:8080/nbim/api/system/organ";
	private static final String ERROR_IO = "ERRORIO";
	private static final String ERROR_URL = "ERRORURL";

	public static void main(String[] args) {

		try {
			/*
			 * URL targetUrl = new URL(targetURL); HttpURLConnection
			 * httpConnection = (HttpURLConnection) targetUrl.openConnection();
			 * httpConnection.setDoOutput(true);
			 * httpConnection.setRequestMethod("POST");
			 * httpConnection.setRequestProperty("Content-Type",
			 * "application/json; charset=utf-8");
			 * 
			 * //httpConnection.addRequestProperty("Content-Type",
			 * "application/x-www-form-urlencoded; charset=UTF-8");
			 * 
			 * 
			 * httpConnection.setDoOutput(true);
			 * httpConnection.setDoInput(true);
			 * httpConnection.setRequestMethod("POST");
			 */

			URL url = new URL(targetURL);
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setRequestMethod("POST"); // 设置请求方法
			httpConnection.setRequestProperty("Charsert", "UTF-8"); // 设置请求编码
			httpConnection.setUseCaches(false);
			httpConnection.setInstanceFollowRedirects(true);
			httpConnection.setRequestProperty("Content-Type", "application/json");

			String input = "{\"name\":\"中文\",\"notes\":\"Marco222\"}";

			// POST请求
			DataOutputStream out = new DataOutputStream(httpConnection.getOutputStream()); // 关键的一步

			PrintWriter pout = new PrintWriter(new OutputStreamWriter(httpConnection.getOutputStream(), "utf-8"));
			pout.println(input);
			pout.flush();
			pout.close();

			/*
			 * out.writeBytes(input); out.flush(); out.close();
			 */

			// String input = "{\"name\":\"Liam222\",\"notes\":\"Marco222\"}";

			/*
			 * OutputStream outputStream = httpConnection.getOutputStream();
			 * 
			 * 
			 * outputStream.write(input.getBytes()); outputStream.flush();
			 */
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}
			BufferedReader responseBuffer = new BufferedReader(
					new InputStreamReader((httpConnection.getInputStream())));
			String output;
			System.out.println("Output from Server:\n");
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

	public static String doPost(String targetURL, String data) {
		StringBuffer sb = new StringBuffer();
		try {
			/*
			 * URL targetUrl = new URL(targetURL); HttpURLConnection
			 * httpConnection = (HttpURLConnection) targetUrl.openConnection();
			 * httpConnection.setDoOutput(true);
			 * httpConnection.setRequestMethod("POST");
			 * httpConnection.setRequestProperty("Content-Type",
			 * "application/json"); OutputStream outputStream =
			 * httpConnection.getOutputStream();
			 * outputStream.write(data.getBytes()); outputStream.flush();
			 */

			URL url = new URL(targetURL);
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setRequestMethod("POST"); // 设置请求方法
			httpConnection.setRequestProperty("Charsert", "UTF-8"); // 设置请求编码
			httpConnection.setUseCaches(false);
			httpConnection.setInstanceFollowRedirects(true);
			httpConnection.setRequestProperty("Content-Type", "application/json");

			// String input = "{\"name\":\"中文\",\"notes\":\"Marco222\"}";

			// POST请求
			PrintWriter pout = new PrintWriter(new OutputStreamWriter(httpConnection.getOutputStream(), "utf-8"));
			pout.println(data);
			pout.flush();
			pout.close();

			if (httpConnection.getResponseCode() != 200) {
				//throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
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
			// e.printStackTrace();
			return ERROR_URL;
		} catch (IOException e) {
			// e.printStackTrace();
			return ERROR_IO;
		}
		String result = sb.toString().replace("\n", "");
		if ("TRUE".equals(result.toUpperCase())) {
			return "OK";
		} else {
			return "ERROR";
		}
	}

}
