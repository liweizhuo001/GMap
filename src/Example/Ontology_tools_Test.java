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
		String ontPath ="Datasets/I3CON_ontologys/"+Ontology_name+".owl";//读取本体的路径		
		
		/*String Ontology_name="new_cmt";
		String ontPath ="Datasets/Inferenced_conference_ontology/"+Ontology_name+".owl";//读取本体的路径
*/		

		//将本体文件进行导入
		Ontology_tools Onto=new Ontology_tools(); 
		Onto.readOnto(ontPath);
		
		
		ArrayList<String> Concept=Onto.GetAllConcept();
		System.out.println("概念如下：");
		for(int i=0;i<Concept.size();i++)
		{
			System.out.println(Concept.get(i));
		}
		System.out.println("概念的个数为："+Concept.size());
		ArrayList<String> Property=Onto.GetAllProperty();
		System.out.println("属性如下：");
		for(int i=0;i<Property.size();i++)
		{
			System.out.println(Property.get(i));
		}
		System.out.println("属性的个数为："+Property.size());
		
		ArrayList<String> Relation=Onto.GetAllRelations();
		System.out.println("关系如下：");
		for(int i=0;i<Relation.size();i++)
		{
			System.out.println(Relation.get(i));
		}
		System.out.println("关系的个数为："+Relation.size());
		
		//查找某一个概念的superclass
		//String concept="Contribution";
		/*String concept="Social_event";
		
		ArrayList<String> Restrictions=Onto.FindConcept_superclasses(concept);
		System.out.println(concept+"的领域受限如下：");
		for(int i=0;i<Restrictions.size();i++)
		{
			System.out.println(Restrictions.get(i));
		}
		System.out.println("受限的个数为："+Restrictions.size());*/
		
		
		/*ArrayList<String> Children=Onto.GetSubclass();
		System.out.println("父子关系如下：");
		for(int i=0;i<Children.size();i++)
		{
			System.out.println(Children.get(i));
		}
		System.out.println("父子关系的大小为："+Children.size());
		*/
		
		
	}
}
