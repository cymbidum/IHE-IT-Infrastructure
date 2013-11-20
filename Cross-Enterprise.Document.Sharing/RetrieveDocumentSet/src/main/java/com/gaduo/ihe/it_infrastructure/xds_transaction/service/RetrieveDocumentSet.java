package com.gaduo.ihe.it_infrastructure.xds_transaction.service;

import java.util.List;
import java.util.Set;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gaduo.ihe.it_infrastructure.xds_transaction.XDSTransaction;
import com.gaduo.ihe.it_infrastructure.xds_transaction.dao.DocumentRequest;
import com.gaduo.ihe.utility.Common;
import com.gaduo.ihe.utility.webservice.nonblock.NonBlockCallBack;
import com.gaduo.webservice.ServiceConsumer;

public class RetrieveDocumentSet extends XDSTransaction {
	public static Logger logger = Logger.getLogger(RetrieveDocumentSet.class);
	private String filename;
	private String repositoryUrl;

	public RetrieveDocumentSet() {
		super();
		c = new Common();
	}

	public OMElement RetrieveGenerator(Set<DocumentRequest> documentIdList) {
		if (documentIdList == null)
			return null;
		filename = c.createTime();
		// -------submit ITI - 43 -------------------
		if (!this.repositoryUrl.equals("")) {
			RetrieveGenerator r = new RetrieveGenerator();
			request = r.execution(documentIdList);
			logger.info(request);
			if (request != null) {
				response = send(request);
				if (response != null) {
					logger.info(response);
					return response;
				}
			}
		}
		gc();
		logger.error("Response is null");
		return null;
	}
	
	public OMElement RetrieveGenerator(List<DocumentRequest> documentIdList) {
		if (documentIdList == null)
			return null;
		filename = c.createTime();
		// -------submit ITI - 43 -------------------
		if (!this.repositoryUrl.equals("")) {
			RetrieveGenerator r = new RetrieveGenerator();
			request = r.execution(documentIdList);
			logger.info(request);
			if (request != null) {
				response = send(request);
				if (response != null) {
					logger.info(response);
					return response;
				}
			}
		}
		gc();
		logger.error("Response is null");
		return null;
	}

	public OMElement RetrieveGenerator(Set<String> documentIdList,
			String repositoryUrl, String repositoryId, String homeCommunityId) {
		if (documentIdList == null)
			return null;
		filename = c.createTime();
		this.repositoryUrl = repositoryUrl;
		// -------submit ITI - 43 -------------------
		if (!this.repositoryUrl.equals("")) {
			RetrieveGenerator r = new RetrieveGenerator();
			request = r
					.execution(documentIdList, repositoryId, homeCommunityId);
			logger.info(request);
			if (request != null) {
				response = send(request);
				if (response != null) {
					logger.info(response);
					return response;
				}
			}
		}
		gc();
		logger.error("Response is null");
		return null;
	}

	@Override
	public OMElement send(OMElement request) {
		// ISoap soap = new Soap(companyRepository.getRepositoryEndpoint(),
		// "http://www.w3.org/2005/08/addressing",
		// "urn:ihe:iti:2007:RetrieveDocumentSet", true);
		// MessageContext context = soap.send(element);
		c.saveLog(filename, "Request_ITI-43", request);
		ServiceConsumer ServiceConsumer = new ServiceConsumer(repositoryUrl,
				request, "http://www.w3.org/2005/08/addressing",
				"urn:ihe:iti:2007:RetrieveDocumentSet", true, true);
		NonBlockCallBack callback = ServiceConsumer.getCallback();
		setContext(callback.getContext());
		SOAPEnvelope envelope = (context != null) ? context.getEnvelope()
				: null;
		SOAPBody body = (envelope != null) ? envelope.getBody() : null;
		OMElement response = (body != null) ? body.getFirstElement() : null;
		c.saveLog(filename, "Response_ITI-43", response);
		gc();
		return response;
	}

	public String extractExtension(String mimeType) {
		String extension = null;
		Node node = null;
		if (Common.codes != null) {
			node = Common.codes
					.QueryNode("Codes/CodeType[@name='mimeType']/Code[@code='"
							+ mimeType + "']");
			if (node != null) {
				if (node != null) {
					NamedNodeMap attrs = node.getAttributes();
					if (attrs != null) {
						extension = attrs.getNamedItem("ext").getNodeValue();
						return extension;
					}
				}
			}
		}
		if (Common.web != null) {
			node = Common.web
					.QueryNode("web-app/mime-mapping/mime-type[text()='"
							+ mimeType + "']");
			if (node != null) {
				node = node.getParentNode();
				NodeList nodes = node.getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++) {
					node = nodes.item(i);
					if (node.getNodeName().equalsIgnoreCase("extension")) {
						return node.getTextContent();
					}
				}
			}
		}
		return null;
	}

	public String getRepositoryUrl() {
		return repositoryUrl;
	}

	public void setRepositoryUrl(String repositoryUrl) {
		this.repositoryUrl = repositoryUrl;
	}

}
