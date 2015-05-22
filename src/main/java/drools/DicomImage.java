package drools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;

public class DicomImage {
	String fileName;
	String patientName;
	String modality;
	boolean boolName = false;
	DicomObject dicom;
	
	DicomImage (File file) {
		DicomInputStream din;
		try {
			din = new DicomInputStream(file);
			this.dicom = din.readDicomObject();
			this.modality = this.dicom.getString(0x00080060);
			if (this.dicom.contains(0x00100010)) {
				this.boolName = true;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		this.fileName = file.getName();
//		FileInputStream fstream;
//		try {
//			fstream = new FileInputStream(file.getAbsolutePath());
//			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//			String strLine;
//			while ((strLine = br.readLine()) != null) {
//				if (strLine.startsWith("Patient_Name")) {
//					this.patientName = strLine.split("\t")[1];
//					this.boolName = true;
//				}
//			}
//			br.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	public String getFileName() {
		return fileName;
	}
	public String getPatientName() {
		return patientName;
	}
	public boolean getBoolName() {
		return boolName;
	}
	
}