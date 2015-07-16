package Example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import Tools.Pellet_tools;

public class Pellet_tools_Test {
	public static void main(String args[]) 
	{
		//String Ontology_name="math_final_edition2";
		String Ontology_name="cmt";
		//String Ontology_name="confOf";
		String ontPath ="Datasets/conference_ontologys/"+Ontology_name+".owl";//��ȡ�����·��

		Pellet_tools Onto=new Pellet_tools(); 

		//�������ļ����е���
		Onto.readOnto(ontPath);	
					
		/*ArrayList<String> Concept=Onto.GetAllConcept();
		System.out.println("�������£�");
		for(int i=0;i<Concept.size();i++)
		{
			System.out.println(Concept.get(i));
		}
		System.out.println("����ĸ���Ϊ��"+Concept.size());	
		*/
		/*ArrayList<String> Property=Onto.GetAllProperty();
		System.out.println("�������£�");
		for(int i=0;i<Property.size();i++)
		{
			System.out.println(Property.get(i));
		}
		System.out.println("���Եĸ���Ϊ��"+Property.size());	*/
		
		/*ArrayList<String> Relation=Onto.GetAll_Infered_Relations();
		//ArrayList<String> Relation=Onto.GetAllRelations();
		System.out.println("��ϵ���£�");
		for(int i=0;i<Relation.size();i++)
		{
			System.out.println(Relation.get(i));
		}
		System.out.println("��ϵ�ĸ���Ϊ��"+Relation.size());*/
		
		
		/*	ArrayList<String> Children=Onto.GetSubclass();
		System.out.println("���ӹ�ϵ���£�");
		for(int i=0;i<Children.size();i++)
		{
			System.out.println(Children.get(i));
		}
		System.out.println("���ӹ�ϵ�Ĵ�СΪ��"+Children.size());	*/
		
		/*ArrayList<String> Sub_Direct=Onto.GetSubclass_Direct();
		System.out.println("ֱ�ӹ�ϵ���£�");
		for(int i=0;i<Sub_Direct.size();i++)
		{
			System.out.println(Sub_Direct.get(i));
		}
		System.out.println("ֱ�ӹ�ϵ�ĸ���Ϊ��"+Sub_Direct.size());*/
		
		ArrayList<String> Super=Onto.GetSuperclass();
		System.out.println("�Ӹ���ϵ���£�");
		for(int i=0;i<Super.size();i++)
		{
			System.out.println(Super.get(i));
		}
		System.out.println("�Ӹ���ϵ�ĸ���Ϊ��"+Super.size());
		
		
	/*	ArrayList<String> Dis=Onto.GetDisjointwith();
		System.out.println("���ཻ��ϵ���£�");
		for(int i=0;i<Dis.size();i++)
		{
			System.out.println(Dis.get(i));
		}
		System.out.println("���ཻ��ϵ�ĸ���Ϊ��"+Dis.size());*/
		
	/*	ArrayList<String> leaf=Onto.GetLeaves();
		System.out.println("Ҷ�ӽ�����£�");
		for(int i=0;i<leaf.size();i++)
		{
			System.out.println(leaf.get(i));
		}
		System.out.println("Ҷ�ӵĸ���Ϊ��"+leaf.size());*/
		
		
		//String concept="Contribution";			
		String concept="Reviewer";		
		/*ArrayList<String> Restrictions=Onto.FindConcept_superclasses(concept);
		System.out.println(concept+"�������������£�");
		for(int i=0;i<Restrictions.size();i++)
		{
			System.out.println(Restrictions.get(i));
		}
		System.out.println("���޵ĸ���Ϊ��"+Restrictions.size());*/
		
	/*		ArrayList<String> Restrictions=Onto.GetAllRestrictions();
		System.out.println("���е������������£�");
		for(int i=0;i<Restrictions.size();i++)
		{
			System.out.println(Restrictions.get(i));
		}
		System.out.println("���޵ĸ���Ϊ��"+Restrictions.size());*/
		
	/*	ArrayList<String> Children=Onto.FindConcept_children(concept);
		for(int i=0;i<Children.size();i++)
		{
			System.out.println(Children.get(i));
		}
		System.out.println("���޵ĸ���Ϊ��"+Children.size());*/

	}
}
