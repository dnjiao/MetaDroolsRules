package drools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class DicomImage {
	String fileName;
	String patientName;
	String modality;
	boolean boolName = false;
	
	public DicomImage (File file) {
		this.fileName = file.getName();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(file.getAbsolutePath());
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (strLine.startsWith("Patient_Name")) {
					this.patientName = strLine.split("\t")[1];
					this.boolName = true;
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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