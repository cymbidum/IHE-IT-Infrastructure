package edu.tcu.gaduo.ihe.iti.atna_transaction.rfc3881;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

import edu.tcu.gaduo.ihe.constants.atna.RFC3881;

@XmlType(name="ParticipantObjectNameType")
@XmlAccessorType (XmlAccessType.FIELD)
public class ParticipantObjectNameType extends General {
	@XmlValue
	protected String value;
	
	public ParticipantObjectNameType(){
		
	}
	
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
