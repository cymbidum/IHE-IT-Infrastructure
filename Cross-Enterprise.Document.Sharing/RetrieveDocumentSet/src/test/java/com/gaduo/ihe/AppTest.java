package com.gaduo.ihe;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

import com.gaduo.ihe.it_infrastructure.xds_transaction.dao.DocumentRequest;
import com.gaduo.ihe.it_infrastructure.xds_transaction.service.RetrieveDocumentSet;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	public static Logger logger = Logger.getLogger(AppTest.class);

	public AppTest(String testName) {
		super(testName);
	}

	protected void setUp() {
	}

	public void test01(){
		 RetrieveDocumentSet rds = new RetrieveDocumentSet();
		 rds.setRepositoryUrl("http://203.64.84.112:8020/axis2/services/xdsrepositoryb?wsdl");
		 List<DocumentRequest> documentIdList = new ArrayList<DocumentRequest>();
		 
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 
		 OMElement response = rds.RetrieveGenerator(documentIdList);
		 logger.info(response);
	}
	
	public void test05(){
		 RetrieveDocumentSet rds = new RetrieveDocumentSet();
		 rds.setRepositoryUrl("http://203.64.84.112:8020/axis2/services/xdsrepositoryb?wsdl");
		 List<DocumentRequest> documentIdList = new ArrayList<DocumentRequest>();
		 
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 
		 OMElement response = rds.RetrieveGenerator(documentIdList);
		 logger.info(response);
	}
	
	public void test10(){
		 RetrieveDocumentSet rds = new RetrieveDocumentSet();
		 rds.setRepositoryUrl("http://203.64.84.112:8020/axis2/services/xdsrepositoryb?wsdl");
		 List<DocumentRequest> documentIdList = new ArrayList<DocumentRequest>();
		 
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 
		 OMElement response = rds.RetrieveGenerator(documentIdList);
		 logger.info(response);
	}
	
	public void test20(){
		 RetrieveDocumentSet rds = new RetrieveDocumentSet();
		 rds.setRepositoryUrl("http://203.64.84.112:8020/axis2/services/xdsrepositoryb?wsdl");
		 List<DocumentRequest> documentIdList = new ArrayList<DocumentRequest>();
		 
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 documentIdList.add(new DocumentRequest("1.3.6.1.4.1.21367.2010.1.2.1125.112", "1.3.6.1.4.1.21367.2010.1.2.203.64.84.119.20131029063802.0", ""));
		 
		 OMElement response = rds.RetrieveGenerator(documentIdList);
		 logger.info(response);
	}

}
