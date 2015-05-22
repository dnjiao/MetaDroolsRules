package drools;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;

import org.drools.compiler.compiler.PackageBuilder;
import org.drools.compiler.compiler.PackageBuilderErrors;
import org.drools.core.StatefulSession;
import org.drools.core.RuleBase;
import org.drools.core.RuleBaseFactory;

public class ApplyDicomRules {
	private static RuleBase rbase = RuleBaseFactory.newRuleBase();
    private static PackageBuilder pbuilder = new PackageBuilder();
    private static StatefulSession sessionObject;
    private static String DRL_FILE = "dicomphi.drl";
 
	public static void main(String[] args) {
		initializeDrools();
		if (args.length != 1) {
			System.out.println("ERROR: must provide one argument.");
			System.exit(1);
		}
		else {
			File folder = new File(args[0]);
			if (folder.isDirectory()) {
				File[] files = folder.listFiles();
				long startTime = System.currentTimeMillis();
				long initTime = 0;
				long fireTime = 0;
				int fileCount = 0;
				for (File f : files) {
					if (f.getName().endsWith(".dcm")) {
						fileCount ++;
						DicomImage dcm = new DicomImage(f);
						long initStart = System.currentTimeMillis();
						initializeObject(dcm);
						long initEnd = System.currentTimeMillis();
						initTime += initEnd - initStart;
						long fireStart = System.currentTimeMillis();
						runRules();
						long fireEnd = System.currentTimeMillis();
						fireTime += fireEnd - fireStart;
						finalizeSession();
					}
				}
				long endTime = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				System.out.println("Rule applied to total " + fileCount + " files in " + totalTime + "ms");
				System.out.println("Rule initialization took " + initTime + "ms");
				System.out.println("Rule firing took " + fireTime + "ms");
			}
		}
	}
        
    private static void initializeDrools() {
        // Read the DRL File and add to package builder
        try {
            Reader reader = new InputStreamReader(ApplyDicomRules.class.getResourceAsStream(DRL_FILE));
            pbuilder.addPackageFromDrl(reader);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
 
        // Check for any errors
        PackageBuilderErrors errors = pbuilder.getErrors();
 
        if (errors.getErrors().length > 0) {
            System.out.println("Some errors exists in packageBuilder");
            for (int i = 0; i < errors.getErrors().length; i++) {
                System.out.println(errors.getErrors()[i]);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
 
        // Add package to rule base
        try {
            rbase.addPackage(pbuilder.getPackage());
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
 
    // Method to fire all rules
    private static void runRules() {
        sessionObject.fireAllRules();
    }
    
    // Method to free memory
    private static void finalizeSession() {
    	sessionObject.dispose();
    }
 
    // Method to insert message object in session
    private static void initializeObject(DicomImage di) {
        sessionObject = rbase.newStatefulSession();
        sessionObject.insert(di);
        
    }
}