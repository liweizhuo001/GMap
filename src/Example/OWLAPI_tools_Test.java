package Example;

import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import Tools.OWLAPI_tools;

public class OWLAPI_tools_Test {
	public  static  void main(String args[]) throws OWLOntologyCreationException
	{
			//String path="Datasets/filled_conference_ontologys/Conference.owl";
			
			String Ontology_name="animalsB";
			String ontPath ="Datasets/I3CON_ontologys/"+Ontology_name+".owl";//��ȡ�����·��	
			OWLAPI_tools onto1=new OWLAPI_tools();
			onto1.readOnto(ontPath);
			onto1.Isconsistent();
				
			//onto1.GetConcept_Children("Chairman");
			//�������еĸ���
			 ArrayList<String> classes=onto1.GetAllConcept();
		    System.out.println("*************************");
			for(String concept:	classes)
			{
				System.out.println(concept);
			}
			System.out.print(classes.size());
			
			//�������е�����
			ArrayList<String> OB_Properties=onto1.GetObjectProperty();	
			System.out.println("*************************");			
			for(String op:	OB_Properties)
			{
				System.out.println(op);
			}
			
			//����ABOX�ı���
			//onto1.GetConceptInstance();
			//onto1.GetConceptInstance("Regular_author");
			//onto1.Is_concept_Instance("Review","41review1");
			
		/*	ArrayList<String> Relation=onto1.GetRelationInstance();	
			for(String relation:	Relation)
			{
				System.out.println(relation);
			}*/
			//ArrayList<String> Relation=onto1.GetRelationInstance("PengWang", "writePaper");	
			/*ArrayList<String> Relation=onto1.GetRelationInstance("PengWang", "writePaper");	
			for(String relation:	Relation)
			{
				System.out.println(relation);
			}*/
			
			//��Ѱ����ʵ����URL
			/*String URL=onto1.find_Instances_url();
			System.out.println(URL);*/
	}
}
