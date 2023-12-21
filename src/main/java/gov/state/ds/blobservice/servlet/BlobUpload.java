package gov.state.ds.blobservice.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

import gov.state.ds.blobservice.service.*;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
/**
 * Servlet implementation class BlobUpload
 */
@WebServlet("/BlobUpload")
public class BlobUpload extends HttpServlet {
	protected static final org.slf4j.Logger LOG = LoggerFactory.getLogger(BlobUpload.class);	
	
	private static final long serialVersionUID = 1L;
//	protected static final Logger LOG = LogFactory.getLogger(CustomQueryBuilderImpl.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlobUpload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		
		//check that each request is coming from the configured server
		System.out.println(request.getServerName());
		LOG.info("In doGet");
		String tran = request.getParameter("type");
	    Map<String, String> headers = new HashMap<>();
	    headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
	    RequestService httpPostForm = new AzureRequestServiceImpl("https://login.microsoftonline.us/a3757a17-78a8-4dd9-9161-e20eace536cd/oauth2/token", "utf-8", headers);
	    
	    // Add form field
	    httpPostForm.addFormField("grant_type", "client_credentials");
	    httpPostForm.addFormField("client_id", "d3c9f180-1ded-4f2a-9fed-b0d431b2314c");
	    httpPostForm.addFormField("client_secret", "Ip.2v9B8TU-NZ6LKarJ_S581~wZ407jQAK");
	    httpPostForm.addFormField("resource", "https://imsstorage.blob.core.usgovcloudapi.net/");
	    String access_token = httpPostForm.getAccessToken();
		
		if ("upload".equals(tran)) {
		response.getWriter().append("Served at: ").append(request.getContextPath()); {
			String azurename = request.getParameter("azurename");
			String filepath = request.getParameter("filepath");
			httpPostForm = new AzureRequestServiceImpl("https://imsstorage.blob.core.usgovcloudapi.net/videofiles/"+azurename, "utf-8", headers);	
			httpPostForm.uploadFile(filepath, null, access_token);

			 response.setContentType("text/html");
			 PrintWriter out = response.getWriter();
			 out.println("<html>");
			 out.println("<head>");
			 out.println("<title>Azure Upload</title>");
			 out.println("</head>");
			 out.println("<body>");
			 out.println("<h1>Uploaded file</h1>");
			 out.println("");
			 out.println("</body>");
			 out.println("</html>");
			 }
		}
		else {
			response.getWriter().append("Download at: ").append(request.getContextPath()); {
				String azurename = request.getParameter("azurename");
				String path = request.getParameter("path");
				
			    httpPostForm = new AzureRequestServiceImpl("https://imsstorage.blob.core.usgovcloudapi.net/videofiles/"+azurename, null, headers);
			    httpPostForm.downloadFile("C:\\apache-tomcat-9.0.82\\webapps\\AzureWebDemo", null, access_token);

				
				 response.setContentType("text/html");
				 PrintWriter out = response.getWriter();
				 out.println("<html>");
				 out.println("<head>");
				 out.println("<title>Azure Download</title>");
				 out.println("</head>");
				 out.println("<body>");
				 out.println("<h1>File Downloaded</h1>");
				 out.println("<video width=\"320\" height=\"240\" controls>");
				 out.println("<source src="+azurename+" type=\"video/mp4\">");
				 out.println("</video>");
				 out.println("</body>");
				 out.println("</html>");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
