package edu.tcu.gaduo.ihe.iti.atna_transaction.rfc5424;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

import edu.tcu.gaduo.ihe.constants.atna.RFC3881;

public class ParticipantObjectNameType extends General {

	protected String value;
	
	public ParticipantObjectNameType(String value){
		this.value = value;
	}
	
	@Override
	public OMElement buildRFC3881() {
    	OMNamespace namespace = null;
		OMElement root = axiom.createOMElement(RFC3881.ParticipantObjectName.getTag(), namespace);
		if(value != null){
			root.setText(value);
		}
		return root;
	}

	
	
}
