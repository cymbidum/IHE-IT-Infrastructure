package edu.tcu.gaduo.ihe.iti.xds_transaction.template;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.axiom.om.OMElement;

import edu.tcu.gaduo.ihe.constants.EbXML;
import edu.tcu.gaduo.ihe.constants.Namespace;
import edu.tcu.gaduo.ihe.constants.ProvideAndRegistryDocumentSet_B_UUIDs;
import edu.tcu.gaduo.ihe.constants.SubmissionSetConstants;
import edu.tcu.gaduo.ihe.utility.xml.XMLPath;

@XmlRootElement(name = "MetadataGenerator")
@XmlAccessorType (XmlAccessType.FIELD)
public class MetadataType extends General {
	@XmlElement(name="RepositoryUrl")
	private String repositoryUrl;
	
	@XmlElement(name="SourcePatientId")
	protected String sourcePatientId;
	@XmlElement(name="PatientInfo")
	protected PatientInfoType patientInfo;
	
	@XmlElement(name="Authors")
	protected AuthorsType authors;
	@XmlElement(name="ContentTypeCode")
	protected String contentTypeCode;
	@XmlElement(name="Documents")
	protected DocumentsType documents;
	@XmlElement(name="Folders")
	protected FoldersType folders;

	@XmlTransient
	private final String objectType = ProvideAndRegistryDocumentSet_B_UUIDs.SUBMISSON_SET_OBJECT;
	@XmlTransient
	private ClassLoader loader;
	
	public static Set<String> ObjectRef;
	public static XMLPath codes;
	public static XMLPath web;
	
	private static String SourceID;
	private static String bootTimestamp;
	private static String IP;
	private static int count;
	
	private static MetadataType instance = null; 
	private MetadataType() {
		super();
		setId(createUUID());
		MetadataType.ObjectRef = new HashSet<String>();
		MetadataType.bootTimestamp = createTime();
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			MetadataType.IP = localHost.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		MetadataType.count = 0;
		
		loader = getClass().getClassLoader();
		Properties prop = new Properties();
		InputStream is = loader.getResourceAsStream("ProvideAndRegisterDocumentSet.properties");
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String sourceId = prop.getProperty("sourceId");
		MetadataType.SourceID = sourceId;
		
		repositoryUrl = prop.getProperty("repository.endpoint");
		
		authors = new AuthorsType();
		documents = new DocumentsType();
		folders = new FoldersType();
		
		InputStream codesXml = loader.getResourceAsStream("codes.xml");
		InputStream webXml = loader.getResourceAsStream("web.xml");
		MetadataType.codes = new XMLPath(codesXml);
		MetadataType.web = new XMLPath(webXml);
	}
	
	public synchronized static MetadataType getInstance(){
		instance = new MetadataType();
		return instance;
	}
	/*----------------------------------------------*/
	
	public String getRepositoryUrl(){
		return repositoryUrl;
	}
	
	public String getSourcePatientId(){
		return sourcePatientId;
	}
	
	public PatientInfoType getPatientInfo(){
		return patientInfo;
	}
	
	public DocumentsType getDocuments(){
		return documents;
	}
	
	public FoldersType getFolders(){
		return folders;
	}
	
	/*----------------------------------------------*/
	public void setRepositoryUrl(String repositoryUrl) {
		this.repositoryUrl = repositoryUrl;
	}
	
	public void setPatientInfo(PatientInfoType patientInfo) {
		this.patientInfo = patientInfo;
	}
	
	public void setContentTypeCode(String contentTypeCode) {
		this.contentTypeCode = contentTypeCode;
	}
	
	public void setSourcePatientId(String sourcePatientId) {
		this.sourcePatientId = sourcePatientId;
	}
	
	public void addAuthor(AuthorType author){
		this.authors.addAuthor(author);
	}
	
	public void addDocument(DocumentType document){
		this.documents.addDocument(document);
	}
	
	public void addFolder(FolderType folder){
		this.folders.addFolder(folder);
	}
	
	/*----------------------------------------------*/
	@Override
	public boolean validate() {
		if(MetadataType.SourceID == null || MetadataType.SourceID.equals(""))
			return false;
		return true;
	}

	@Override
	public OMElement buildEbXML() {
		if(!validate()){
			logger.info("validate error");
			return null;
		}
		OMElement root = axiom.createOMElement(EbXML.RegistryPackage, Namespace.RIM3);
		root.addAttribute("id", this.getId(), null);
		root.addAttribute("objectType", objectType, null);
		MetadataType.ObjectRef.add(objectType);
		// --submissionTime
		String submissionTime = createTime();
		if (submissionTime != null) {
			OMElement slot = this.addSlot(SubmissionSetConstants.SUBMISSION_TIME, new String[] { submissionTime });
			root.addChild(slot);
		}
		// ---------------------Author
		Iterator<AuthorType> iterator = authors.list.iterator();
		while(iterator.hasNext()){
			AuthorType author = iterator.next();
			author.setEntryUUID(this.getId());
			OMElement element = author.buildEbXML();
			if(element != null)
				root.addChild(element);
		}
		// ---ContentTypeCode
		if (contentTypeCode != null) {
			contentTypeCode = contentTypeCode.trim();
			OMElement element = buildClassification("contentTypeCode", contentTypeCode, SubmissionSetConstants.CODING_SCHEME, this.getId(), ProvideAndRegistryDocumentSet_B_UUIDs.SUBMISSION_SET_CONTENT_TYPE_CODE);
			if(element != null)
				root.addChild(element);
		}
		
		// ---------------------ExternalIdentifier
		OMElement name;
		name = addNameOrDescription(SubmissionSetConstants.PATIENT_ID, EbXML.Name);
		OMElement externalIdentifier01 = addExternalIdentifier(ProvideAndRegistryDocumentSet_B_UUIDs.SUBMISSION_SET_PATIENT_IDENTIFICATION_SCHEME, this.getId(), sourcePatientId, name);
		root.addChild(externalIdentifier01);
		
		String uniqueId = MetadataType.getUniqueId();
		name = addNameOrDescription(SubmissionSetConstants.UNIQUE_ID, EbXML.Name);
		OMElement externalIdentifier02 = addExternalIdentifier(ProvideAndRegistryDocumentSet_B_UUIDs.SUBMISSION_SET_UNIQUE_IDENTIFICATION_SCHEME, this.getId(), uniqueId, name);
		root.addChild(externalIdentifier02);
		
		name = addNameOrDescription(SubmissionSetConstants.SOURCE_ID, EbXML.Name);
		OMElement externalIdentifier03 = addExternalIdentifier(ProvideAndRegistryDocumentSet_B_UUIDs.SUBMISSION_SET_SOURCE_IDENTIFICATION_SCHEME, this.getId(), MetadataType.SourceID, name);
		root.addChild(externalIdentifier03);
		return root;
	}
	
	
	/**
	 * 建置 classfication 
	 * @return
	 */
	public OMElement buildEbXMLClassification(){
		/* classification */
		OMElement classification = axiom.createOMElement(EbXML.Classification, Namespace.RIM3);
		classification.addAttribute("id", createUUID(), null);
		classification.addAttribute("classificationNode", objectType, null);
		classification.addAttribute("classifiedObject", getId(), null);
		classification.addAttribute("nodeRepresentation", "", null);
		/* classification */
		return classification;
	}
	
	
	/**
	 * 將 MetadataType 轉成 OMElement 
	 * @return OMElement Object
	 */
	public OMElement marshal(){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(MetadataType.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.marshal(this, os);
			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray()) ;
			OMElement element = axiom.resourcesToOMElement(is);
			return element;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static synchronized String getUniqueId(){
		MetadataType.count++;
		String oid = MetadataType.IP + "." + MetadataType.bootTimestamp + "." + Thread.currentThread().getId() + "." + MetadataType.count;
		return oid;
	}
}
