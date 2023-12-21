package gov.state.ds.blobservice.service;

import org.slf4j.LoggerFactory;


public class SprintTest {

	protected static final org.slf4j.Logger LOG = LoggerFactory.getLogger(SprintTest.class);	
		public static void main(String[] args) throws Exception {

			RequestService rs = new AEMRequestServiceImpl();
			//System.out.println(rs.uploadFile("F:\\chris\\video\\aemt1.mp4",null,null));
			//rs.addContainer("newcontainer", null, null);
			System.out.println(rs.listContainer("uploadtest"));
			LOG.info(rs.listContainer("uploadtest"));

		}
}
