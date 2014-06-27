package edu.tcu.gaduo.zk.view_model.xds_b_i;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.iod.module.sr.HierachicalSOPInstanceReference;
import org.dcm4che2.iod.module.sr.SOPInstanceReferenceAndMAC;
import org.dcm4che2.iod.module.sr.SeriesAndInstanceReference;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.Filedownload;


import edu.tcu.gaduo.ihe.security.CertificateDetails;
import edu.tcu.gaduo.ihe.security._interface.ICertificate;
import edu.tcu.gaduo.zk.model.dicom.DICOMEntry;
import edu.tcu.gaduo.zk.model.dicom.DICOMEntryTreeNode;

//import Gaduo.ZK.Model.DICOM.DICOMEntryCollection;

/**
 * 
 */

/**
 * @author Gaduo
 */
public class ImagingDocumentConsumerVM {
    public static Logger logger = Logger.getLogger(ImagingDocumentConsumerVM.class);
    private DefaultTreeModel<DICOMEntry> studyList;
    private String wadoUrl;
    private final String STUDY = "Study";
    private final String SERIES = "Series";
    private final String INSTANCE = "Instance";

    @Init
    public void init() {
        setWadoUrl("http://localhost:8000/mio/WADO.html");
        System.gc();   
    }

    public DefaultTreeModel<DICOMEntry> getstudyList() {
        return studyList;
    }

    public void setstudyList(DefaultTreeModel<DICOMEntry> studyList) {
        this.studyList = studyList;
    }

    @NotifyChange({"studyList"})
    @Command
    public void upload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
        UploadEvent upEvent = (UploadEvent) ctx.getTriggerEvent();
        Media media = upEvent.getMedia();
        byte[] array = null;
        if(!media.isBinary())
            array = media.getStringData().getBytes();
        else
            array = media.getByteData();

        DicomObject dcmObj;
        DicomInputStream din = null;
        try {
            din = new DicomInputStream(new ByteArrayInputStream(array));
            dcmObj = din.readDicomObject();
            parse(dcmObj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (din != null) {
                try {
                    din.close();
                } catch (IOException ignore) {
                }
            } else {
                logger.error("kos is null");
                return;
            }
        }
        
    }
    
    private void parse(DicomObject dcmObj) {
        try {
            DicomElement sq = dcmObj.get(0x0040a375);
            if (sq == null)
                return;
            HierachicalSOPInstanceReference[] hierachicalSOPInstanceReference = HierachicalSOPInstanceReference.toSOPInstanceReferenceMacros(sq);
            Set<DICOMEntryTreeNode> studies = new HashSet<DICOMEntryTreeNode>();
            for (HierachicalSOPInstanceReference study : hierachicalSOPInstanceReference) {
                String studyUID = study.getStudyInstanceUID();
                logger.trace(studyUID);
                Set<DICOMEntryTreeNode> serieses = new HashSet<DICOMEntryTreeNode>();
                SeriesAndInstanceReference[] seriesAndInstanceReference = study.getReferencedSeries();
                for (SeriesAndInstanceReference series : seriesAndInstanceReference) {
                    String seriesUID = series.getSeriesInstanceUID();
                    String RetrieveAETitle = series.getRetrieveAETitle();
                    String RetrieveLocationUID = series.getDicomObject().getString(0x0040e011); // AETITLE mapping
                    logger.trace(" |__\t" + seriesUID + "\t@" + RetrieveAETitle + "\t" + RetrieveLocationUID);
                    SOPInstanceReferenceAndMAC[] instanceReferenceAndMAC = series.getReferencedInstances();
                    Set<DICOMEntryTreeNode> instances = new HashSet<DICOMEntryTreeNode>();
                    for (SOPInstanceReferenceAndMAC instance : instanceReferenceAndMAC) {
                        String instanceUID = instance.getReferencedSOPInstanceUID();
                        instances.add(new DICOMEntryTreeNode(new DICOMEntry(this.INSTANCE, instanceUID)));
                        logger.trace("\t |__\t" + instanceUID);
                    }
                    serieses.add(new DICOMEntryTreeNode(new DICOMEntry(this.SERIES, seriesUID), instances));
                }
                studies.add(new DICOMEntryTreeNode(new DICOMEntry(this.STUDY, studyUID), serieses));
            }
            DICOMEntryTreeNode root = new DICOMEntryTreeNode(null, studies, true);
            setstudyList(new DefaultTreeModel<DICOMEntry>(root, true));
        } catch (java.lang.NullPointerException e) {
            logger.error(e.toString());
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public String getWadoUrl() {
        return wadoUrl;
    }

    public void setWadoUrl(String wadoUrl) {
        this.wadoUrl = wadoUrl;
    }

    @Command
    public void newWindows(@BindingParam("each") DICOMEntryTreeNode each) {
    	ICertificate cert = CertificateDetails.getInstance();
    	cert.setSSLCertificate();
        String mimeType = "image/jpeg";
        String request = this.wadoUrl + "?requestType=WADO&contentType=" + mimeType + "&studyUID=" + each.getParent().getParent().getData().getId()
                + "&seriesUID=" + each.getParent().getData().getId() + "&objectUID=" + each.getData().getId();
        logger.trace(request);
        Clients.evalJavaScript("window.open('" + request + "','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')");
    }
    @Command
    public void downloadDicom(@BindingParam("each") DICOMEntryTreeNode each) {
    	ICertificate cert = CertificateDetails.getInstance();
    	cert.setSSLCertificate();
        String mimeType = "application/dicom";
        String request = this.wadoUrl + "?requestType=WADO&contentType=" + mimeType + "&studyUID=" + each.getParent().getParent().getData().getId()
                + "&seriesUID=" + each.getParent().getData().getId() + "&objectUID=" + each.getData().getId();
        logger.trace(request);
        URL url;
        try {
            url = new URL(request);
            URLConnection connect = url.openConnection();
            InputStream is = connect.getInputStream();
            Filedownload.save(is, mimeType, each.getData().getId() + ".dcm");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
}
