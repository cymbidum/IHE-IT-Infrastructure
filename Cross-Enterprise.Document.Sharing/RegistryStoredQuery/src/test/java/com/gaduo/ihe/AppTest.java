package com.gaduo.ihe;

import junit.framework.TestCase;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

import com.gaduo.ihe.it_infrastructure.xds_transaction.service.RegistryStoredQuery;
import com.gaduo.ihe.security.Certificate;
import com.gaduo.ihe.utility.AxiomUtil;
import com.gaduo.ihe.utility.LoadTesDatatUtil;

public class AppTest extends TestCase {
	public static Logger logger = Logger.getLogger(AppTest.class);
	LoadTesDatatUtil load;

	public AppTest(String testName) {
		super(testName);
	}

	protected void setUp() {
		load = new LoadTesDatatUtil();
	}

	public void testApp() {
		String uuid01 = "urn:uuid:152ac4cd-e29b-4aff-b880-ac895f21ff46";
		String uuid02 = "urn:uuid:2460a90d-a287-4eee-8797-564b44b4aede";

		AxiomUtil axiom = new AxiomUtil();
		Certificate cert = new Certificate();
		// cert.setCertificate();
		cert.setCertificate("openxds_2010/OpenXDS_2010_Keystore.p12",
				"password", "openxds_2010/OpenXDS_2010_Truststore.jks",
				"password");
		OMElement source = load.loadTestDataToOMElement("query_template.xml");

		RegistryStoredQuery rsq = new RegistryStoredQuery();
		OMElement response = rsq.QueryGenerator(source);
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
