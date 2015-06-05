package drools;

import java.io.File;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class ApplyDicomRules {
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("ERROR: must provide two arguments.");
			System.exit(1);
		}
		else {
			try {
				KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
				
				File folder = new File(args[0]);
				long initStart = System.currentTimeMillis();
				
				// add rule files to knowledgebuilder
				String filename;
				for (int i = 1; i < 10; i++) {
		    		filename = "rule" + Integer.toString(i) + "0000.drl";
		    		System.out.println("Adding rule file " + Integer.toString(i) + "\n");
		    		kbuilder.add(ResourceFactory.newClassPathResource(filename), ResourceType.DRL);
				}
				KnowledgeBuilderErrors errors = kbuilder.getErrors();
		        if (errors.size() > 0) {
		        	for (KnowledgeBuilderError error: errors) {
		        		System.err.println(error);
		            }
		            throw new IllegalArgumentException("Could not parse knowledge.");
		        }
		        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		        long initEnd = System.currentTimeMillis();
				long initTime = initEnd - initStart;
				
				// iterate all files in directory and apply all rules to each of them
				StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
				if (folder.isDirectory()) {
					File[] files = folder.listFiles();
					long startTime = System.currentTimeMillis();
					long fireTime = 0;
					int fileCount = 0;
					for (File f : files) {
						if (f.getName().endsWith(".dcm")) {
							fileCount ++;
							DicomImage dcm = new DicomImage(f);
							ksession.insert(dcm);
							
							long fireStart = System.currentTimeMillis();
							ksession.fireAllRules();
							long fireEnd = System.currentTimeMillis();
							fireTime += fireEnd - fireStart;
							ksession.dispose();
						}
						if (fileCount == Integer.parseInt(args[1])) {
							break;
						}
					}
					long endTime = System.currentTimeMillis();
					long totalTime = endTime - startTime;
					System.out.println("Rule applied to total " + fileCount + " files in " + totalTime + "ms");
					System.out.println("Rule initialization took " + initTime + "ms");
					System.out.println("Rule firing took " + fireTime + "ms");
				} 
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}