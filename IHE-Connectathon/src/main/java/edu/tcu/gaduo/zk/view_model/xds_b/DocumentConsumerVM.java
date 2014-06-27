/**
 * 
 */
package edu.tcu.gaduo.zk.view_model.xds_b;

import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;


import edu.tcu.gaduo.ihe.iti.xds_transaction.service.RegistryStoredQuery;
import edu.tcu.gaduo.ihe.security.CertificateDetails;
import edu.tcu.gaduo.ihe.security._interface.ICertificate;
import edu.tcu.gaduo.ihe.utility.ws.nonblock.Response_ITI_18;
import edu.tcu.gaduo.ihe.utility.ws.nonblock._interface.IResponse;
import edu.tcu.gaduo.zk.model.CompanyInfomation;
import edu.tcu.gaduo.zk.model.QueryGenerator;
import edu.tcu.gaduo.zk.model.KeyValue.KeyValue;
import edu.tcu.gaduo.zk.model.KeyValue.KeyValues;
import edu.tcu.gaduo.zk.model.KeyValue.KeyValuesImpl;
import edu.tcu.gaduo.zk.view_model.CompanyInfoVM;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.FindDocuments;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.FindFolders;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.FindSubmissionSets;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.GetAll;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.GetAssociations;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.GetDocuments;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.GetDocumentsAndAssociations;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.GetFolderAndContents;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.GetFolders;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.GetFoldersForDocument;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.GetRelatedDocuments;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.GetSubmissionSets;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.GetSubmissionsetAndContents;
import edu.tcu.gaduo.zk.view_model.xds_b.iti_18.IParameter;

/**
 * @author Gaduo
 */
public class DocumentConsumerVM {
	public static Logger logger = Logger.getLogger(DocumentConsumerVM.class);
	public static int total = 0;
	public static int currentNO;

	private QueryGenerator query;
	private String endpoint;
	private KeyValues queryTypeList;
	private String view;
	private IResponse ITI_18;
	private List<CompanyInfomation> companyInfomations;

	private double timestamp;
	
	@Init
	public void init() {
		getUUID();
		getData();
		CompanyInfoVM c = new CompanyInfoVM();
		c.init();
		this.setCompanyInfomations(c.getCompanyInfomations());
		System.gc();

		// ---Debug
		// ITI_18 = new Response_ITI_18();
		// ((Response_ITI_18)ITI_18).setList(new TreeSet<String>());
		// for(int i = 0; i < 15; i++){
		// ((Response_ITI_18)ITI_18).addItemToList(i +"_abcdefg");
		// }
		// ---Debug
	}

	private void getData() {
		query = new QueryGenerator();
		query.setQueryType(queryTypeList.get(0));
		query.setReturnType("ObjectRef");
		query.setParameter(new FindDocuments());
		setView("XDS.b/ITI-18/" + query.getQueryType().getKey() + ".zul");
	}

	private void getUUID() {
		this.queryTypeList = new KeyValuesImpl();
		this.queryTypeList.add(new KeyValue("FindDocuments",
				"urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d"));
		this.queryTypeList.add(new KeyValue("FindFolders",
				"urn:uuid:958f3006-baad-4929-a4de-ff1114824431"));
		this.queryTypeList.add(new KeyValue("FindSubmissionSets",
				"urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9"));
		this.queryTypeList.add(new KeyValue("GetAll",
				"urn:uuid:10b545ea-725c-446d-9b95-8aeb444eddf3"));
		this.queryTypeList.add(new KeyValue("GetDocuments",
				"urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4"));
		this.queryTypeList.add(new KeyValue("GetDocumentsAndAssociations",
				"urn:uuid:bab9529a-4a10-40b3-a01f-f68a615d247a"));
		this.queryTypeList.add(new KeyValue("GetFolders",
				"urn:uuid:5737b14c-8a1a-4539-b659-e03a34a5e1e4"));
		this.queryTypeList.add(new KeyValue("GetFolderAndContents",
				"urn:uuid:b909a503-523d-4517-8acf-8e5834dfc4c7"));
		this.queryTypeList.add(new KeyValue("GetFoldersForDocument",
				"urn:uuid:10cae35a-c7f9-4cf5-b61e-fc3278ffb578"));
		this.queryTypeList.add(new KeyValue("GetSubmissionSets",
				"urn:uuid:51224314-5390-4169-9b91-b1980040715a"));
		this.queryTypeList.add(new KeyValue("GetSubmissionsetAndContents",
				"urn:uuid:e8e3cb2c-e39c-46b9-99e4-c12f57260b83"));
		this.queryTypeList.add(new KeyValue("GetAssociations",
				"urn:uuid:a7ae438b-4bc2-4642-93e9-be891f7bb155"));
		this.queryTypeList.add(new KeyValue("GetRelatedDocuments",
				"urn:uuid:d90e5407-b356-4d91-a89f-873917b4b0e6"));
	}

	@Command
	@NotifyChange({ "ITI_18", "view", "total", "timestamp" })
	public void submit() {
		timestamp = System.currentTimeMillis();
		
		DocumentConsumerVM.total = 0;
		if (query.getCompanyRegistry() == null) {
			logger.warn("Choice Registry .");
			return;
		}
		boolean isBuild = query.build();
		logger.info("RSQ is build : " + isBuild);
		if (isBuild) {
			// setView("");
			ICertificate cert = CertificateDetails.getInstance();
			cert.setCertificate();
			ITI_18 = new Response_ITI_18();
			ITI_18.clean();
			RegistryStoredQuery rsq = new RegistryStoredQuery();
			OMElement response = rsq.QueryGenerator(query.getQuery());
			ITI_18.parser(rsq.getContext());
			DocumentConsumerVM.total = ((Response_ITI_18) ITI_18).getList() .size();
			logger.info(DocumentConsumerVM.total + "");
		}
		IParameter p;
		if (query != null) {
			p = query.getParameter();
			if (p != null) {
				List<OMElement> plist = p.getParameters();
				if (plist != null) {
					plist.clear();
				}
			}
		}
		System.gc();
		timestamp = System.currentTimeMillis() - timestamp;
		timestamp /= 1000;
		
	}

	@Command
	@NotifyChange({ "view", "query" })
	public void SelectQueryType() { // 選擇 Query 型態
		String key = query.getQueryType().getKey();
		logger.info(key);
		if (key.equals("FindDocuments")) {
			query.setParameter(new FindDocuments());
		}
		if (key.equals("FindFolders")) {
			query.setParameter(new FindFolders());
		}
		if (key.equals("FindSubmissionSets")) {
			query.setParameter(new FindSubmissionSets());
		}
		if (key.equals("GetAll")) {
			query.setParameter(new GetAll());
		}
		if (key.equals("GetDocuments")) {
			query.setParameter(new GetDocuments());
		}
		if (key.equals("GetDocumentsAndAssociations")) {
			query.setParameter(new GetDocumentsAndAssociations());
		}
		if (key.equals("GetFolders")) {
			query.setParameter(new GetFolders());
		}
		if (key.equals("GetFolderAndContents")) {
			query.setParameter(new GetFolderAndContents());
		}
		if (key.equals("GetFoldersForDocument")) {
			query.setParameter(new GetFoldersForDocument());
		}
		if (key.equals("GetSubmissionSets")) {
			query.setParameter(new GetSubmissionSets());
		}
		if (key.equals("GetSubmissionsetAndContents")) {
			query.setParameter(new GetSubmissionsetAndContents());
		}
		if (key.equals("GetAssociations")) {
			query.setParameter(new GetAssociations());
		}
		if (key.equals("GetRelatedDocuments")) {
			query.setParameter(new GetRelatedDocuments());
		}
		setView("XDS.b/ITI-18/" + key + ".zul");
	}

	public IResponse getITI_18() {
		return this.ITI_18;
	}

	public void setITI_18(IResponse ITI_18) {
		this.ITI_18 = ITI_18;
	}

	public List<KeyValue> getQueryTypeList() {
		return this.queryTypeList.findAll();
	}

	public void setQueryTypeList(KeyValues queryTypeList) {
		this.queryTypeList = queryTypeList;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public QueryGenerator getQuery() {
		return query;
	}

	public void setQuery(QueryGenerator query) {
		this.query = query;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public List<CompanyInfomation> getCompanyInfomations() {
		return companyInfomations;
	}

	public void setCompanyInfomations(List<CompanyInfomation> companyInfomations) {
		this.companyInfomations = companyInfomations;
	}

	public int getTotal() {
		return total;
	}

	public int getCurrentNO() {
		return currentNO;
	}

	public double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}
}
