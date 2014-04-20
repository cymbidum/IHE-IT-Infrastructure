package edu.tcu.gaduo.ihe.iti.atna_transaction.rfc5424;

import java.io.InputStream;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import edu.tcu.gaduo.ihe.constants.atna.CodeType;
import edu.tcu.gaduo.ihe.constants.atna.RFC3881;
import edu.tcu.gaduo.ihe.utility.xml.XMLPath;

public class CodedValueType extends General {
	public static Logger logger = Logger.getLogger(CodedValueType.class);
	protected RFC3881 tag;
	
    protected String csd_code;
    protected String displayName;
    protected String originalText;
    protected String codeSystem;
    protected String codeSystemName;
    
    private XMLPath xmlPath;
    
    public CodedValueType(RFC3881 tag, String code){

    	this.tag = tag;
    	this.csd_code =code;  
    }
    
    public CodedValueType(RFC3881 tag, String code, CodeType codeType){
    	this.tag = tag;
    	this.csd_code =code;    	
		String query = "Codes/CodeType[@name='" + codeType.toString() + "']/Code[@code='" + code + "']";
    	Class<? extends CodedValueType> clazz = this.getClass();
    	ClassLoader loader = clazz.getClassLoader();
    	InputStream is = loader.getResourceAsStream("atnacodes.xml");
    	xmlPath = new XMLPath(is);
    	Node node = xmlPath.QueryNode(query);
    	NamedNodeMap attrs = node.getAttributes();
    	originalText = attrs.getNamedItem("display").getNodeValue();
    	codeSystemName = attrs.getNamedItem("codingScheme").getNodeValue();
    	
    }
    
    
    @Override
	public OMElement buildRFC3881() {
    	OMNamespace namespace = null;
    	OMElement element = axiom.createOMElement(tag.getTag(), namespace);
    	if(csd_code != null)
    		element.addAttribute("csd-code", csd_code, null);
    	if(originalText != null)
    		element.addAttribute("originalText", originalText, null);
    	if(codeSystemName != null)
    		element.addAttribute("codeSystemName", codeSystemName, null);
    	logger.info(element);
    	
		return element;
    }

}
