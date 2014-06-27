/**
 * 
 */
package edu.tcu.gaduo.ihe.utility.ws.nonblock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;

import edu.tcu.gaduo.ihe.utility.ws.nonblock._interface.IResponse;

/**
 * @author Gaduo
 */
public class Response_ITI_43 implements IResponse {
	public static Logger logger = Logger.getLogger(Response_ITI_43.class);
    private OMElement response;
    private String status;
    private String codeContext;
    private String errorCode;
    private String severity;
    private String location;
    private List<RetrieveResult> retrieveResultlist;

    /**
     * @param msgContext
     */
    public void parser(MessageContext msgContext) {
        if (msgContext != null) {
            parser(msgContext.getEnvelope());
            
            synchronized (this) {
                this.notify();
            }
        }else {
            this.setCodeContext("NO MessageContext");
        }
    }
    
    @SuppressWarnings("unchecked")
    public void parser(SOAPEnvelope envelope) {
    	try{
    		response = envelope.getBody().getFirstElement().getFirstElement();
    	}catch(java.lang.NullPointerException e){
    		logger.error(e.toString());
    	}
        logger.trace(response);
        if (response != null) {
            OMElement errorList = response.getFirstElement();
            this.setStatus(response.getAttributeValue(new QName("status")));
            if (status.equals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure")) {
                if (errorList != null) {
                    Iterator<OMElement> elements = errorList.getChildElements();
                    while (elements.hasNext()) {
                        OMElement element = elements.next();
                        if (element != null) {
                            this.setCodeContext(setValue(element, "codeContext"));
                            this.setErrorCode(setValue(element, "errorCode"));
                            this.setLocation(setValue(element, "location"));
                            this.setSeverity(setValue(element, "severity"));
                        }
                    }
                }
            }
            if (status.equals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success")) {
                retrieveResultlist = new ArrayList<RetrieveResult>();
                response = envelope.getBody().getFirstElement();
                Iterator<OMElement> elements = response.getChildElements();
                while (elements.hasNext()) {
                    OMElement element = elements.next();
                    if(element.getLocalName().equalsIgnoreCase("DocumentResponse")) {
                        Iterator<OMElement> documents = element.getChildElements();
                        RetrieveResult rr = new RetrieveResult();
                        while(documents.hasNext()) {
                            OMElement document = documents.next();
                            if(document.getLocalName().equalsIgnoreCase("RepositoryUniqueId")) {
                                rr.setRepositoryUniqueId(document.getText());
                            }
                            if(document.getLocalName().equalsIgnoreCase("DocumentUniqueId")) {
                                rr.setDocumentUniqueId(document.getText());
                            }
                            if(document.getLocalName().equalsIgnoreCase("mimeType")) {
                                rr.setMimeType(document.getText());
                            }
                            if(document.getLocalName().equalsIgnoreCase("Document")) {
                                rr.setDocument(document.getText());
                            }
                        }
                        retrieveResultlist.add(rr);
                    }
                    
                }
            }
        }else {
            this.setCodeContext("NO SOAP Body");
        }
    }


    private String setValue(OMElement element, String type) {
        try {
            return element.getAttributeValue(new QName(type)).trim();
        } catch (java.lang.NullPointerException e) {
            return "";
        }
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCodeContext() {
        return codeContext;
    }

    public void setCodeContext(String codeContext) {
        this.codeContext = codeContext;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OMElement getResponse() {
        return this.response;
    }

    public void setResponse(OMElement response) {
        this.response = response;
    }

    public String getSeverity() {
        return this.severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<RetrieveResult> getRetrieveResultList() {
        return this.retrieveResultlist;
    }

    public void setRetrieveResultList(List<RetrieveResult> list) {
        this.retrieveResultlist = list;
    }

    public String toString() {
        logger.trace(getCodeContext());
        logger.trace(getStatus());
        logger.trace(getErrorCode());
        logger.trace(getLocation());
        logger.trace(getSeverity());
        logger.trace(getResponse());
        return null;
    }

	public boolean clean() {
    	errorCode = "";
    	codeContext = "";
    	status = "";
    	severity = "";
    	location = "";
    	return true;
	}

}
