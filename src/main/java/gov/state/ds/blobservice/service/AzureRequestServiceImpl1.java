package gov.state.ds.blobservice.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
//import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.*;

public class AzureRequestServiceImpl1 {
//    private HttpsURLConnection httpConn;
    private HttpURLConnection httpConn;
    public Map<String, Object> queryParams;
    private String charset;
    private static final int BUFFER_SIZE = 4096;
    private static final String USER_AGENT = "Mozilla/5.0";
    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @param headers
     * @param queryParams
     * @throws IOException
     */
    public AzureRequestServiceImpl1(String requestURL, String charset, Map<String, String> headers, Map<String, Object> queryParams) throws IOException {
        this.charset = charset;
        if (queryParams == null) {
            this.queryParams = new HashMap<>();
        } else {
            this.queryParams = queryParams;
        }
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);    // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        if (headers != null && headers.size() > 0) {
            Iterator<String> it = headers.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String value = headers.get(key);
                httpConn.setRequestProperty(key, value);
            }
        }
    }



    public AzureRequestServiceImpl1(String requestURL, String charset, Map<String, String> headers) throws IOException {
        this(requestURL, charset, headers, null);
    }

    public AzureRequestServiceImpl1(String requestURL, String charset) throws IOException {
        this(requestURL, charset, null, null);
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, Object value) {
        queryParams.put(name, value);
    }

    /**
     * Adds a header to the request
     *
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        httpConn.setRequestProperty(key, value);
    }

    /**
     * Convert the request fields to a byte array
     *
     * @param params
     * @return
     */
    private static byte[] getParamsByte(Map<String, Object> params) {
        byte[] result = null;
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(encodeParam(param.getKey()));
            postData.append('=');
            postData.append(encodeParam(String.valueOf(param.getValue())));
        }
        try {
            result = postData.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    
    private static String ffmpegIt() throws IOException {
    	//ffmpeg -i samplevideo.mp4 -f dash -vcodec copy -acodec copy output.mpd
    	//String[] cmd={"C:\\ffmpeg\\bin\\ffmpeg","-i", "concat:C:\\journalism\\videos\\vid1.ts|C:\\journalism\\videos\\vid2.ts", "-c", "copy", "C:\\journalism\\videos\\output.mp4"};
    	String[] cmd={"C:\\chris\\videos\\ffmpeg", "-i", "C:\\chris\\videos\\test1\\samplevideo.mp4", "-f", "dash", "-vcodec", "copy", "-acodec", "copy", "C:\\chris\\videos\\test1\\output.mpd"};
    	Runtime.getRuntime().exec(cmd);
    	
    	return "";
    }
    
    /**
     * URL-encoding keys and values
     *
     * @param data
     * @return
     */
    private static String encodeParam(String data) {
        String result = "";
        try {
            result = URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return String as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String getAccessToken() throws IOException {
        String response = "";
        byte[] postDataBytes = this.getParamsByte(queryParams);
        httpConn.getOutputStream().write(postDataBytes);
        // Check the http status
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = httpConn.getInputStream().read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            response = result.toString(this.charset);
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
        
	    JSONObject jobj = new JSONObject(response);
	    String access_token = jobj.getString("access_token");
        return access_token;
    }

    public static void downloadFile(String fileURL, String saveDir, Map<String, Object> queryParams, String access_token )
            throws IOException {

        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
	    httpConn.setRequestProperty("Authorization", "Bearer "+access_token.toString());
	    httpConn.setRequestProperty("x-ms-version", "2017-11-09");
        
        int responseCode = httpConn.getResponseCode();
 
        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();
 
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
 
            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);
 
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;
             
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
 
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();
 
            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }
    
    public static void uploadFile(String fileURL, String uploadFile, Map<String, Object> queryParams, String access_token )
            throws IOException {

        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
	    File file = new File(uploadFile);
	    System.out.println(uploadFile+" "+file.length());
        httpConn.setDoInput(true);
	    httpConn.setDoOutput(true);
        httpConn.setRequestMethod("PUT");
	    httpConn.setRequestProperty("Authorization", "Bearer "+access_token.toString());
	    httpConn.setRequestProperty("x-ms-version", "2017-11-09");
	    httpConn.setRequestProperty("x-ms-blob-type", "BlockBlob");
	    httpConn.setRequestProperty("Content-Length", "0");
	    httpConn.setRequestProperty("Content-Type", "video/mp4");

		BufferedOutputStream bos = new BufferedOutputStream(httpConn.getOutputStream());
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		int read;
		byte[] input = new byte[4096];
		while ( -1 != ( read = bis.read( input ) ) ) {
		    bos.write( input, 0, read );
		}
		bis.close();
		bos.close();
		System.out.println("AzureRequestServiceImpl1 "+httpConn.getResponseMessage());
        httpConn.disconnect();
        
    }

    public void addContainer(String fileURL, Map<String, Object> queryParams, String access_token )
            throws IOException {

        String charset = "UTF-8";
        String param1 = "container";

        String query = String.format("restype=%s", 
             URLEncoder.encode(param1, charset));
        System.out.println(fileURL+"?"+query);
        URL url = new URL(fileURL+"?"+query);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setDoInput(true);
	    httpConn.setDoOutput(true);
        httpConn.setRequestMethod("PUT");
	    httpConn.setRequestProperty("Authorization", "Bearer "+access_token.toString());
	    httpConn.setRequestProperty("x-ms-version", "2017-11-09");
	    httpConn.setRequestProperty("Content-Length", "0");
	    httpConn.setFixedLengthStreamingMode(0);
	    System.out.println(httpConn.getResponseMessage());
        httpConn.disconnect();
        
    }
    
	public static void main(String[] args) throws IOException {
		// Headers
	    Map<String, String> headers = new HashMap<>();
	    headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
	    AzureRequestServiceImpl1 httpPostForm = new AzureRequestServiceImpl1("https://login.microsoftonline.us/a3757a17-78a8-4dd9-9161-e20eace536cd/oauth2/token", "utf-8", headers);
	    
	    // Add form field
	    httpPostForm.addFormField("grant_type", "client_credentials");
	    httpPostForm.addFormField("client_id", "d3c9f180-1ded-4f2a-9fed-b0d431b2314c");
	    httpPostForm.addFormField("client_secret", "Ip.2v9B8TU-NZ6LKarJ_S581~wZ407jQAK");
	    httpPostForm.addFormField("resource", "https://imsstorage.blob.core.usgovcloudapi.net/");
	    // Result
	    String response = httpPostForm.getAccessToken();
	    System.out.println(response);
	    
//	    JSONObject jobj = new JSONObject(response);
//	    String access_token = jobj.getString("access_token");
//	    System.out.println(access_token);
	    
	    //ffmpegIt();
	    httpPostForm = new AzureRequestServiceImpl1("https://imsstorage.blob.core.usgovcloudapi.net/videofiles/samplevideo.mp4", null, headers);
	    //httpPostForm.addFormField("Authorization", "Bearer "+access_token.toString());
	    //httpPostForm.addFormField("x-ms-version", "2017-11-09");
	  //  downloadFile("https://imsstorage.blob.core.usgovcloudapi.net/videofiles/jump4.mp4", "C:\\chris\\videos", httpPostForm.queryParams, response);
	 //  uploadFile("https://imsstorage.blob.core.usgovcloudapi.net/videofilespub/test/jump1.mp4", "C:\\chris\\videos\\jump1.mp4", httpPostForm.queryParams, response);

	    //httpPostForm.download(access_token);
	    
	}

}