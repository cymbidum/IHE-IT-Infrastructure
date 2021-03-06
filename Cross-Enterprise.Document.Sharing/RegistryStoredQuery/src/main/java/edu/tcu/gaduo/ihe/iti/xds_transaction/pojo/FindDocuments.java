package edu.tcu.gaduo.ihe.iti.xds_transaction.pojo;

import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMElement;

import edu.tcu.gaduo.ihe.constants.StoredQueryConstants;
import edu.tcu.gaduo.ihe.iti.xds_transaction.template.ParameterType;
import edu.tcu.gaduo.ihe.iti.xds_transaction.template.ValueType;


public class FindDocuments extends StoredQuery {
	
	public FindDocuments(String queryUUID, List<ParameterType> parameters) {
		super(queryUUID);
		
		Iterator<ParameterType> pList = parameters.iterator();
		while (pList.hasNext()) {
			ParameterType p = pList.next();
			String name = p.getName();
			OMElement slot = null;
			if (name != null) {
				if(super.isContainParameter(name)){
					List<ValueType> vList = p.getValues();
					slot = this.addSlot(name, vList);
					if (slot != null) {
						addParameters(slot);
					}
				}
			}
		}
	}
	
	protected void setParameterSet(){
		this.ParameterSet.put(StoredQueryConstants.DE_PATIENT_ID, "R,-");/*PatientUID*/
		this.ParameterSet.put(StoredQueryConstants.DE_CLASS_CODE, "O,M");/*XDSDocumentEntryClassCode*/
		this.ParameterSet.put(StoredQueryConstants.DE_CLASS_CODE_SCHEME, "O,M");
		this.ParameterSet.put(StoredQueryConstants.DE_PRAC_SETTING_CODE, "O,M");/*XDSDocumentEntryPracticeSettingCode*/
		this.ParameterSet.put(StoredQueryConstants.DE_PRAC_SETTING_CODE_SCHEME, "O,M");
		this.ParameterSet.put(StoredQueryConstants.DE_CREATION_TIME_FROM, "O,-");/*XDSDocumentEntryCreationTimeFrom */
		this.ParameterSet.put(StoredQueryConstants.DE_CREATION_TIME_TO, "O,-");/*XDSDocumentEntryCreationTimeTo */
		this.ParameterSet.put(StoredQueryConstants.DE_SERVICE_START_TIME_FROM, "O,-");/*XDSDocumentEntryServiceStartTimeFrom */
		this.ParameterSet.put(StoredQueryConstants.DE_SERVICE_START_TIME_TO, "O,-");/*XDSDocumentEntryServiceStartTimeTo */
		this.ParameterSet.put(StoredQueryConstants.DE_SERVICE_STOP_TIME_FROM, "O,-");/*XDSDocumentEntryServiceStopTimeFrom */
		this.ParameterSet.put(StoredQueryConstants.DE_SERVICE_STOP_TIME_TO, "O,-");/*XDSDocumentEntryServiceStopTimeTo */
		this.ParameterSet.put(StoredQueryConstants.DE_HC_FACILITY_CODE, "O,M");/*XDSDocumentEntryHealthcareFacilityTypeCode*/
		this.ParameterSet.put(StoredQueryConstants.DE_HC_FACILITY_CODE_SCHEME, "O,M");
		this.ParameterSet.put(StoredQueryConstants.DE_EVENT_CODE, "O,M");/*XDSDocumentEntryEventCodeList*/
		this.ParameterSet.put(StoredQueryConstants.DE_EVENT_CODE_SCHEME, "O,M");
		this.ParameterSet.put(StoredQueryConstants.DE_CONF_CODE, "O,M");/*XDSDocumentEntryConfidentialityCode*/
		this.ParameterSet.put(StoredQueryConstants.DE_CONF_CODE_SCHEME, "O,M");
		this.ParameterSet.put(StoredQueryConstants.DE_FORMAT_CODE, "O,M");/*XDSDocumentEntryFormatCode*/
		this.ParameterSet.put(StoredQueryConstants.DE_STATUS, "R,M");/*XDSDocumentEntryStatus */
	}
}
