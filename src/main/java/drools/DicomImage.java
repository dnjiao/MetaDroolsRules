package drools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class DicomImage {
	String patientName;
	String modality;
	boolean boolName;
	
	public DicomImage (String filename) {
		FileInputStream fstream = new FileInputStream(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		while ((strLine = br.readLine()) != null) {
			
		}
	}
	public void setPatientName(String str) {
		
	}
	
}