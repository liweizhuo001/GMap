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
		String ontPath ="Datasets/conference_ontologys/"+Ontology_name+".owl";//读取本体的路径

		Pellet_tools Onto=new Pellet_tools(); 

		//将本体文件进行导入
		Onto.readOnto(ontPath);	
					
		/*ArrayList<String> Concept=Onto.GetAllConcept();
		System.out.println("概念如下：");
		for(int i=0;i<Concept.size();i++)
		{
			System.out.println(Concept.get(i));
		}
		System.out.println("概念的个数为："+Concept.size());	
		*/
		/*ArrayList<String> Property=Onto.GetAllProperty();
		System.out.println("属性如下：");
		for(int i=0;i<Property.size();i++)
		{
			System.out.println(Property.get(i));
		}
		System.out.println("属性的个数为："+Property.size());	*/
		
		/*ArrayList<String> Relation=Onto.GetAll_Infered_Relations();
		//ArrayList<String> Relation=Onto.GetAllRelations();
		System.out.println("关系如下：");
		for(int i=0;i<Relation.size();i++)
		{
			System.out.println(Relation.get(i));
		}
		System.out.println("关系的个数为："+Relation.size());*/
		
		
		/*	ArrayList<String> Children=Onto.GetSubclass();
		System.out.println("父子关系如下：");
		for(int i=0;i<Children.size();i++)
		{
			System.out.println(Children.get(i));
		}
		System.out.println("父子关系的大小为："+Children.size());	*/
		
		/*ArrayList<String> Sub_Direct=Onto.GetSubclass_Direct();
		System.out.println("直接关系如下：");
		for(int i=0;i<Sub_Direct.size();i++)
		{
			System.out.println(Sub_Direct.get(i));
		}
		System.out.println("直接关系的个数为："+Sub_Direct.size());*/
		
		ArrayList<String> Super=Onto.GetSuperclass();
		System.out.println("子父关系如下：");
		for(int i=0;i<Super.size();i++)
		{
			System.out.println(Super.get(i));
		}
		System.out.println("子父关系的个数为："+Super.size());
		
		
	/*	ArrayList<String> Dis=Onto.GetDisjointwith();
		System.out.println("不相交关系如下：");
		for(int i=0;i<Dis.size();i++)
		{
			System.out.println(Dis.get(i));
		}
		System.out.println("不相交关系的个数为："+Dis.size());*/
		
	/*	ArrayList<String> leaf=Onto.GetLeaves();
		System.out.println("叶子结点如下：");
		for(int i=0;i<leaf.size();i++)
		{
			System.out.println(leaf.get(i));
		}
		System.out.println("叶子的个数为："+leaf.size());*/
		
		
		//String concept="Contribution";			
		String concept="Reviewer";		
		/*ArrayList<String> Restrictions=Onto.FindConcept_superclasses(concept);
		System.out.println(concept+"的领域受限如下：");
		for(int i=0;i<Restrictions.size();i++)
		{
			System.out.println(Restrictions.get(i));
		}
		System.out.println("受限的个数为："+Restrictions.size());*/
		
	/*		ArrayList<String> Restrictions=Onto.GetAllRestrictions();
		System.out.println("所有的领域受限如下：");
		for(int i=0;i<Restrictions.size();i++)
		{
			System.out.println(Restrictions.get(i));
		}
		System.out.println("受限的个数为："+Restrictions.size());*/
		
	/*	ArrayList<String> Children=Onto.FindConcept_children(concept);
		for(int i=0;i<Children.size();i++)
		{
			System.out.println(Children.get(i));
		}
		System.out.println("受限的个数为："+Children.size());*/

	}
}
