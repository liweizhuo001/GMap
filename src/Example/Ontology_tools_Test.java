package Example;

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Tools.Ontology_tools;

public class Ontology_tools_Test {
	public static void main(String args[]) 
	{
		//String Ontology_name="math_final_edition2";
		String Ontology_name="WineB";
		String ontPath ="Datasets/I3CON_ontologys/"+Ontology_name+".owl";//��ȡ�����·��		
		
		/*String Ontology_name="new_cmt";
		String ontPath ="Datasets/Inferenced_conference_ontology/"+Ontology_name+".owl";//��ȡ�����·��
*/		

		//�������ļ����е���
		Ontology_tools Onto=new Ontology_tools(); 
		Onto.readOnto(ontPath);
		
		
		ArrayList<String> Concept=Onto.GetAllConcept();
		System.out.println("�������£�");
		for(int i=0;i<Concept.size();i++)
		{
			System.out.println(Concept.get(i));
		}
		System.out.println("����ĸ���Ϊ��"+Concept.size());
		ArrayList<String> Property=Onto.GetAllProperty();
		System.out.println("�������£�");
		for(int i=0;i<Property.size();i++)
		{
			System.out.println(Property.get(i));
		}
		System.out.println("���Եĸ���Ϊ��"+Property.size());
		
		ArrayList<String> Relation=Onto.GetAllRelations();
		System.out.println("��ϵ���£�");
		for(int i=0;i<Relation.size();i++)
		{
			System.out.println(Relation.get(i));
		}
		System.out.println("��ϵ�ĸ���Ϊ��"+Relation.size());
		
		//����ĳһ�������superclass
		//String concept="Contribution";
		/*String concept="Social_event";
		
		ArrayList<String> Restrictions=Onto.FindConcept_superclasses(concept);
		System.out.println(concept+"�������������£�");
		for(int i=0;i<Restrictions.size();i++)
		{
			System.out.println(Restrictions.get(i));
		}
		System.out.println("���޵ĸ���Ϊ��"+Restrictions.size());*/
		
		
		/*ArrayList<String> Children=Onto.GetSubclass();
		System.out.println("���ӹ�ϵ���£�");
		for(int i=0;i<Children.size();i++)
		{
			System.out.println(Children.get(i));
		}
		System.out.println("���ӹ�ϵ�Ĵ�СΪ��"+Children.size());
		*/
		
		
	}
}
