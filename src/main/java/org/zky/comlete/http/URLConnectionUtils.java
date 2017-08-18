package org.zky.comlete.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionUtils {

	private static int connectTimeout;

	private static int readTimeout;

	public static int getConnectTimeout() {
		return connectTimeout;
	}

	public static void setConnectTimeout(int connectTimeout) {
		URLConnectionUtils.connectTimeout = connectTimeout;
	}

	public static int getReadTimeout() {
		return readTimeout;
	}

	public static void setReadTimeout(int readTimeout) {
		URLConnectionUtils.readTimeout = readTimeout;
	}

	public static String get(String url, String auth) {
		return service(url, auth, null, false);
	}

	public static String service(String url, String auth, String data, boolean method) {
		OutputStream out = null;
		InputStream in = null;
		BufferedReader reader = null;
		try {
			URL uri = new URL(url);
			URLConnection urlConnection = uri.openConnection();
			HttpURLConnection httpUrlConn = (HttpURLConnection) urlConnection;
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
			if (auth != null) {
				// httpUrlConn.setRequestProperty("Authorization", "Basic
				// "+Base64Utils.getBASE64(auth.getBytes()));
			}
			httpUrlConn.setRequestMethod(method ? "POST" : "GET");
			httpUrlConn.connect();

			if (method) {
				out = httpUrlConn.getOutputStream();
				out.write(data.toString().getBytes());
				out.flush();
			}

			in = httpUrlConn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			closeConnection(out, in, reader);
		}
	}

	private static void closeConnection(OutputStream out, InputStream in, BufferedReader reader) {
		try {
			if (reader != null) {
				reader.close();
			}
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String arg[]) throws Exception {
		String agentUrl = "http://10.0.10.114:5000/";
		String formData = "corpus=zhao kun yang&query=zhao jun";
		while(true){
			String str = URLConnectionUtils.service(agentUrl, null, formData, true);
			System.out.println(str);
		}
		
		
	}
}
