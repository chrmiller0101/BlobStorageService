package gov.state.ds.blobservice.service;

import java.io.IOException;
import java.util.Map;

public interface RequestService {
	
	public String getAccessToken() throws IOException;
	public void addFormField(String name, Object value);
	public void downloadFile(String saveDir, Map<String, Object> queryParams, String access_token ) throws IOException;
	public boolean uploadFile(String file, Map<String, Object> queryParams, String access_token ) throws IOException;
	public void addContainer(String fileURL, Map<String, Object> queryParams, String access_token ) throws IOException;
	public String listContainer(String containerName) throws IOException;

}
