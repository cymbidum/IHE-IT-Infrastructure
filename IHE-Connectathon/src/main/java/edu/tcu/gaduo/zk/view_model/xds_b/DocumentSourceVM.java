/**
 * 
 */
package edu.tcu.gaduo.zk.view_model.xds_b;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.axiom.om.OMElement;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;

import edu.tcu.gaduo.ihe.iti.xds_transaction.service.ProvideAndRegisterDocumentSet;
import edu.tcu.gaduo.ihe.security.CertificateDetails;
import edu.tcu.gaduo.ihe.security._interface.ICertificate;
import edu.tcu.gaduo.ihe.utility.ws.nonblock.Response_ITI_41;
import edu.tcu.gaduo.ihe.utility.ws.nonblock._interface.IResponse;
import edu.tcu.gaduo.ihe.utility.xml.XMLPath;
import edu.tcu.gaduo.zk.model.CompanyInfomation;
import edu.tcu.gaduo.zk.model.MetadataGenerator;
import edu.tcu.gaduo.zk.model.attachment.AttachmentEntry;
import edu.tcu.gaduo.zk.model.attachment.AttachmentEntryTreeNode;
import edu.tcu.gaduo.zk.model.code.Code;
import edu.tcu.gaduo.zk.model.code.Codes;
import edu.tcu.gaduo.zk.model.code.CodesImpl;
import edu.tcu.gaduo.zk.view_model.CompanyInfoVM;

/**
 * @author Gaduo
 */
public class DocumentSourceVM {
	public static Logger logger = Logger.getLogger(DocumentSourceVM.class);

	private MetadataGenerator metadata;
	private String endpoint;
	private IResponse ITI_41;
	private Codes contentTypeCodeList;
	private Codes classCodeList;
	private Codes confidentialityCodeList;
	private Codes formatCodeList;
	private Codes healthcareFacilityTypeCodeList;
	private Codes practiceSettingCodeList;
	private Codes typeCodeList;
	private Codes eventCodeListList;
	private Codes folderCodeListList;

	private Set<AttachmentEntry> docSet;
	private TreeSet<?> folderSet;
	private List<CompanyInfomation> companyInfomations;

	public DocumentSourceVM() {
		System.gc();
	}

	@Init
	public void init() throws ParseException {
		BasicConfigurator.configure();
		getCodeList();
		getData();
		CompanyInfoVM c = new CompanyInfoVM();
		c.init();
		this.setCompanyInfomations(c.getCompanyInfomations());
		System.gc();
	}

	@NotifyChange({ "metadata" })
	private void getData() throws ParseException {
		metadata = new MetadataGenerator();
		metadata.setOperations("12049");
		metadata.setPid3("c6002e5679534ea^^^&1.3.6.1.4.1.21367.2005.3.7&ISO");
		metadata.setPid5("王大尾");
		metadata.setPid7(new SimpleDateFormat("yyyyMMdd").parse("19990801"));
		metadata.setPid8("M");
		metadata.setPid11("花蓮市中央路三段701號");
		metadata.setPid13("0912345678");
		metadata.setAuthorPerson("黃柏榮醫師");
		metadata.setAuthorInstitution("台北醫學大學附設醫院");
		metadata.setAuthorRole("主治醫師");
		metadata.setAuthorSpecialty("乳房專科醫師");
		metadata.setSourceID("1.3.6.1.4.1.21367.2010.1.2");
		metadata.setContentTypeCode(contentTypeCodeList.get(0));

		metadata.setClassCode(classCodeList.get(0));
		metadata.setConfidentialityCode(confidentialityCodeList.get(0));
		metadata.setFormatCode(formatCodeList.get(0));
		metadata.setHealthcareFacilityTypeCode(healthcareFacilityTypeCodeList
				.get(0));
		metadata.setPracticeSettingCode(practiceSettingCodeList.get(0));
		metadata.setTypeCode(typeCodeList.get(0));
		metadata.setEventCodeList(eventCodeListList.get(0));
		metadata.setFolderCodeList(folderCodeListList.get(0));
	}

	private void getCodeList() {
		XMLPath codes = new XMLPath(getClass().getClassLoader()
				.getResourceAsStream("codes.xml"));

		setContentTypeCodeList(new CodesImpl(codes, "contentTypeCode"));
		setClassCodeList(new CodesImpl(codes, "classCode"));
		setConfidentialityCodeList(new CodesImpl(codes, "confidentialityCode"));
		setFormatCodeList(new CodesImpl(codes, "formatCode"));
		setHealthcareFacilityTypeCodeList(new CodesImpl(codes,
				"healthcareFacilityTypeCode"));
		setPracticeSettingCodeList(new CodesImpl(codes, "practiceSettingCode"));
		setTypeCodeList(new CodesImpl(codes, "typeCode"));
		setEventCodeListList(new CodesImpl(codes, "eventCodeList"));
		setFolderCodeListList(new CodesImpl(codes, "folderCodeList"));
	}

	@SuppressWarnings("unused")
	@Command
	@NotifyChange({ "ITI_41" })
	public void submit() {
		ITI_41 = new Response_ITI_41();
		ITI_41.clean();
		ProvideAndRegisterDocumentSet pnr = new ProvideAndRegisterDocumentSet();
		if (pnr == null && metadata == null)
			return;
		if (metadata.getCompanyRepository() == null) {
			logger.warn("Choice Repository .");
			return;
		}
		ICertificate cert = CertificateDetails.getInstance();
		cert.setCertificate();
		metadata.build();
		OMElement response = pnr.MetadataGenerator(metadata.getSource());
		// edu.tcu.gaduo.webservice.NonBlockCallBack callback = pnr.getCallback();
		// ServiceConsumer ServiceConsumer = new ServiceConsumer(endpoint,
		// metadata.getSource(),
		// null, "urn:Provide:and:Register:Document:Set:b", true, true);
		// NonBlockCallBack callback = ServiceConsumer.getCallback();
		ITI_41.parser(pnr.getContext());

		System.gc();
	}

	@NotifyChange({ "metadata" })
	@Command
	public void upload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws UnsupportedEncodingException {
		UploadEvent upEvent = (UploadEvent) ctx.getTriggerEvent();
		Media media = upEvent.getMedia();
		AttachmentEntry document = new AttachmentEntry(media.getName(),
				media.getName());
		document.setType(AttachmentEntry.DOCUMENT);
		byte[] array = null;
		if (!media.isBinary()) {
			array = removeUTF8BOM(media.getStringData()).getBytes("UTF-8");
		} else {
			array = media.getByteData();
		}
		document.setContent(new String(Base64.encodeBase64(array)));
		metadata.addDocumentItem(document);
	}

	private String removeUTF8BOM(String s) {
		if (s.startsWith("\uFEFF")) {
			s = s.substring(1);
		}
		return s;
	}

	@SuppressWarnings("unchecked")
	@NotifyChange({ "metadata" })
	@Command
	public void docAddFolder() {
		LinkedHashSet<?> selection = (LinkedHashSet<?>) metadata
				.getFolderList().getSelection();
		if (!selection.isEmpty() && docSet != null) {
			Iterator<?> Iterator = selection.iterator();
			while (Iterator.hasNext()) {
				AttachmentEntryTreeNode<AttachmentEntry> node = (AttachmentEntryTreeNode<AttachmentEntry>) Iterator
						.next();
				Iterator<AttachmentEntry> iterator = docSet.iterator();
				while (iterator.hasNext()) {
					AttachmentEntry doc = iterator.next();
					AttachmentEntryTreeNode<AttachmentEntry> treeNode = new AttachmentEntryTreeNode<AttachmentEntry>(
							doc);
					if (!node.isLeaf()) {
						logger.trace(node);
						logger.trace(metadata.getDocumentList());
						node.add(treeNode);
						metadata.getDocumentList().remove(doc);
					}
				}
			}
			docSet.clear();
		}
	}

	@SuppressWarnings("unchecked")
	@NotifyChange({ "metadata" })
	@Command
	public void docRemoveFromFolder() {
		if (folderSet != null) {
			Iterator<?> iterator = folderSet.iterator();
			while (iterator.hasNext()) {
				AttachmentEntryTreeNode<AttachmentEntry> node = (AttachmentEntryTreeNode<AttachmentEntry>) iterator
						.next();
				AttachmentEntry attachmentEntry = node.getAttachmentEntry();
				if (attachmentEntry.getType() == AttachmentEntry.DOCUMENT) {
					metadata.getDocumentList().add(attachmentEntry);
					metadata.getFolderList().removeOpenObject(attachmentEntry);
				}
			}
		}
	}

	@NotifyChange({ "metadata" })
	@Command
	public void removeDocumentItem(@BindingParam("each") int i) {
		metadata.removeDocumentItem(i);
	}

	@NotifyChange({ "metadata" })
	@Command
	public void removeFolderItem(@BindingParam("each") int i) {
		metadata.removeFolderItem(i);
	}

	@NotifyChange({ "metadata" })
	@Command
	public void addFolder() {
		int size = metadata.getRoot().size();
		AttachmentEntry attachmentEntry = new AttachmentEntry("Folder_" + size,
				"Folder_" + size);
		attachmentEntry.setType(AttachmentEntry.FOLDER);
		metadata.addFolderItem(attachmentEntry);
	}

	@NotifyChange({ "metadata" })
	@Command
	public void clear() {
		int opt = Integer.valueOf(metadata.getOperations());
		switch (opt) {
		case 12049:
			metadata.setExistingDocument("");
			metadata.setExistingFolder("");
			break;
		case 11971:
			metadata.setExistingDocument("");
			metadata.getRoot().clear();
			break;
		case 11973:
			metadata.getDocumentList().clear();
			metadata.getRoot().clear();
			break;
		case 11974:
		case 11975:
		case 11976:
		case 11977:
			metadata.setExistingFolder("");
			metadata.getRoot().clear();
			break;
		}
	}

	// -----------------

	public MetadataGenerator getMetadata() {
		return this.metadata;
	}

	public void setMetadata(MetadataGenerator metadata) {
		this.metadata = metadata;
	}

	public IResponse getITI_41() {
		return this.ITI_41;
	}

	public void setITI_41(IResponse ITI_41) {
		this.ITI_41 = ITI_41;
	}

	// Codes
	public List<Code> getClassCodeList() {
		return this.classCodeList.findAll();
	}

	public void setClassCodeList(CodesImpl classCodeList) {
		this.classCodeList = classCodeList;
	}

	public List<Code> getConfidentialityCodeList() {
		return this.confidentialityCodeList.findAll();
	}

	public void setConfidentialityCodeList(CodesImpl confidentialityCodeList) {
		this.confidentialityCodeList = confidentialityCodeList;
	}

	public List<Code> getFormatCodeList() {
		return this.formatCodeList.findAll();
	}

	public void setFormatCodeList(CodesImpl formatCodeList) {
		this.formatCodeList = formatCodeList;
	}

	public List<Code> getHealthcareFacilityTypeCodeList() {
		return this.healthcareFacilityTypeCodeList.findAll();
	}

	public void setHealthcareFacilityTypeCodeList(
			CodesImpl healthcareFacilityTypeCodeList) {
		this.healthcareFacilityTypeCodeList = healthcareFacilityTypeCodeList;
	}

	public List<Code> getPracticeSettingCodeList() {
		return this.practiceSettingCodeList.findAll();
	}

	public void setPracticeSettingCodeList(CodesImpl practiceSettingCodeList) {
		this.practiceSettingCodeList = practiceSettingCodeList;
	}

	public List<Code> getContentTypeCodeList() {
		return this.contentTypeCodeList.findAll();
	}

	public void setContentTypeCodeList(CodesImpl codesList) {
		this.contentTypeCodeList = codesList;
	}

	public List<Code> getEventCodeListList() {
		return eventCodeListList.findAll();
	}

	public void setEventCodeListList(CodesImpl eventCodeListList) {
		this.eventCodeListList = eventCodeListList;
	}

	public List<Code> getFolderCodeListList() {
		return folderCodeListList.findAll();
	}

	public void setFolderCodeListList(CodesImpl folderCodeListList) {
		this.folderCodeListList = folderCodeListList;
	}

	public List<Code> getTypeCodeList() {
		return typeCodeList.findAll();
	}

	public void setTypeCodeList(CodesImpl typeCodeList) {
		this.typeCodeList = typeCodeList;
	}

	// Codes

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public Set<?> getDocSet() {
		return docSet;
	}

	public void setDocSet(Set<AttachmentEntry> docSet) {
		this.docSet = docSet;
	}

	public TreeSet<?> getFolderSet() {
		return folderSet;
	}

	public void setFolderSet(TreeSet<?> folderSet) {
		this.folderSet = folderSet;
	}

	public List<CompanyInfomation> getCompanyInfomations() {
		return companyInfomations;
	}

	public void setCompanyInfomations(List<CompanyInfomation> companyInfomations) {
		this.companyInfomations = companyInfomations;
	}
}
