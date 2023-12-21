package gov.state.ds.blobservice.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;


/**
 * @author millerc3
 *
 */
public class AEMRequestServiceImpl implements RequestService {
	
	//Chris - get these values from properties
    public String GRANT_TYPE = "refresh_token";
    public String CLIENT_ID = "p9na0lmrb50l26lvl7pns611jl-umujzwaj";
    public String CLIENT_SECRET = "3omk6hv4rk3jia27bt9n2fhtdv";
    public String REDIRECT_URI = "http://localhost:8080/servlets/index.html";
    public String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJwOW5hMGxtcmI1MGwyNmx2bDdwbnM2MTFqbC11bXVqendhaiIsImlzcyI6IkFkb2JlIEdyYW5pdGUiLCJzdWIiOiJhZG1pbiIsImV4cCI6MTczMDU2NjU4OCwiaWF0IjoxNjk5MDMwNTg4LCJzY29wZSI6InZlbmRvci14X193cml0ZS1kYW0sb2ZmbGluZV9hY2Nlc3MiLCJjdHkiOiJydCJ9.a-vXUqr9jE3_usMZ0uzXJBJPCu-Q2G1ZHAFE8K0QpTo";
    public String TOKEN_URL = "http://dsd19dsdlyap02/oauth/token";
    public String CSRF_URL = "http://dsd19dsdlyap02/libs/granite/csrf/token.json";	
    public String FILE_UPLOAD_URL = "http://dsd19dsdlyap02/api/assets/newcontainer/";
    public String CREATE_FOLDER_URL = "http://dsd19dsdlyap02/api/assets/";
    public String LIST_FOLDER_URL = "http://dsd19dsdlyap02/api/assets/";

    /**
    * getAccessToken is required for all requests. This
    * should be put in the header and set Authorization and the value as Bearer thistoken.
    */
    @Override
	public String getAccessToken() throws IOException {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grant_type", GRANT_TYPE);
		map.add("client_id",CLIENT_ID);
		map.add("client_secret",CLIENT_SECRET);
		map.add("redirect_uri", REDIRECT_URI);
		map.add("refresh_token", REFRESH_TOKEN);

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

		ResponseEntity<byte[]> response =
		    restTemplate.exchange(TOKEN_URL,
		                          HttpMethod.POST,
		                          entity,
		                          byte[].class);
		
		String accessTokenResultBody = new String(response.getBody(),Charset.forName("UTF-8"));
		JSONObject accessToken = new JSONObject(accessTokenResultBody);

		return accessToken.getString("access_token");
	}

    /**
    * getCSRFToken is required for POST and PUT requests. This
    * should be put in the header and set CSRF-Token. This might
    * only be required for js.
    */
	private String getCSRFToken() throws IOException {

		// create an instance of RestTemplate
		RestTemplate restTemplate = new RestTemplate();
		// create headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer "+this.getAccessToken());
		// build the request
		HttpEntity<Void> request = new HttpEntity<>(headers);
		// make an HTTP GET request with headers
		ResponseEntity<byte[]> response = restTemplate.exchange(
		        CSRF_URL,
		        HttpMethod.GET,
		        request,
		        byte[].class,
		        11
		);
		
		String csrfTokenResultBody = new String(response.getBody(),Charset.forName("UTF-8"));
		JSONObject csrfToken = new JSONObject(csrfTokenResultBody);

		return csrfToken.getString("token");
    }

	@Override
	public void addFormField(String name, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadFile(String saveDir, Map<String, Object> queryParams, String access_token) throws IOException {
		// TODO Auto-generated method stub

	}

	/*
	 * POST /api/assets/myFolder/myAsset.png -H"Content-Type: image/png" --data-binary "@myPicture.png"
	 */
	@Override
	public boolean uploadFile(String file, Map<String, Object> queryParams, String access_token) throws IOException {

	   	RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		httpHeaders.set("Authorization", "Bearer "+this.getAccessToken());
		httpHeaders.set("CSRF-Token", this.getCSRFToken());
        
	    File uploadFile = new File(file);
        final FileSystemResource fileSystemResource = new FileSystemResource(uploadFile);
        final MultiValueMap<String, Object> fileUploadMap = new LinkedMultiValueMap<>();
        fileUploadMap.set("file", fileSystemResource);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(fileUploadMap, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(FILE_UPLOAD_URL+uploadFile.getName(), httpEntity, String.class);
        return responseEntity.getStatusCode().equals(HttpStatus.CREATED);


	}

	/*
	 * POST /api/assets/myFolder -H"Content-Type: application/json" -d '{"class":"assetFolder","properties":{"jcr:title":"My Folder"}}'
	 */
	@Override
	public void addContainer(String name, Map<String, Object> queryParams, String access_token) throws IOException {
		
		//{"class":"assetFolder","properties":{"jcr:title":"My Folder"}}'
		
		RestTemplate restTemplate = new RestTemplate();
		// create request body
		JSONObject request = new JSONObject();
		JSONObject folder = new JSONObject();
		folder.put("jcr:title", name);
		request.put("class", "assetFolder");
		request.put("properties", folder);

		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer "+this.getAccessToken());
		HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

		// send request and parse result
		ResponseEntity<String> folderResponse = restTemplate.exchange(
				CREATE_FOLDER_URL+name, 
				HttpMethod.POST, 
				entity, 
				String.class);
		
		if (folderResponse.getStatusCode() == HttpStatus.CREATED) {
		  System.out.println(folderResponse);
		} else  {
			
		  // nono... bad credentials
		}
	
	}

	/*
	 * GET /api/assets/myFolder.json
	 */
	@Override
	public String listContainer(String containerName) throws IOException {

		// create an instance of RestTemplate
		RestTemplate restTemplate = new RestTemplate();
		// create headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer "+this.getAccessToken());
		// build the request
		HttpEntity<Void> request = new HttpEntity<>(headers);
		// make an HTTP GET request with headers
		ResponseEntity<byte[]> response = restTemplate.exchange(
		        LIST_FOLDER_URL+containerName,
		        HttpMethod.GET,
		        request,
		        byte[].class,
		        11
		);
		
		String csrfTokenResultBody = new String(response.getBody(),Charset.forName("UTF-8"));
		JSONObject csrfToken = new JSONObject(csrfTokenResultBody);

		return csrfToken.toString();
	
	}

}
