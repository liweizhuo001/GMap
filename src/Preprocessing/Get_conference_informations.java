package Preprocessing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


import Tools.Ontology_tools;
//import Tools.Ontology_tools;
import Tools.Pellet_tools;

public class Get_conference_informations {
	public static void main(String args[]) throws IOException
	{
		//定义文件的写操作
		BufferedWriter bfw_concepts= null;
		BufferedWriter bfw_properties= null;
		BufferedWriter bfw_objectproperties= null;
		BufferedWriter bfw_dataproperties= null;
		BufferedWriter bfw_relations= null;
		BufferedWriter bfw_objectrelations= null;
		BufferedWriter bfw_datatyperelations= null;
		BufferedWriter bfw_restrictions= null;
		BufferedWriter bfw_leaves= null;
		BufferedWriter bfw_instances= null;		
		BufferedWriter bfw_HasSub= null;
		BufferedWriter bfw_HasSub_Direct= null;
		BufferedWriter bfw_HasSuper= null;
		BufferedWriter bfw_HasSuper_Direct= null;
		BufferedWriter bfw_HasSibling= null;
		BufferedWriter bfw_Disjointwith= null;
	
		//设置相对路径
		/*String []Ontology={"cmt","Conference","confOf","edas","ekaw","iasted","sigkdd"};
		for (int x=0;x<Ontology.length;x++)
		{
			String Ontology_name = Ontology[x];*/
		String Ontology_name="iasted";
		String Store_Path="Intermediate_Data/"+Ontology_name;//存取本体的路径
		try{
			bfw_concepts=new BufferedWriter(new FileWriter(Store_Path+"/concepts.txt"));
			bfw_properties=new BufferedWriter(new FileWriter(Store_Path+"/properties.txt"));
			bfw_objectproperties=new BufferedWriter(new FileWriter(Store_Path+"/objectproperties.txt"));
			bfw_dataproperties=new BufferedWriter(new FileWriter(Store_Path+"/datatypeproperties.txt"));
			bfw_relations=new BufferedWriter(new FileWriter(Store_Path+"/relations.txt"));
			bfw_objectrelations=new BufferedWriter(new FileWriter(Store_Path+"/objectrelations.txt"));
			bfw_datatyperelations=new BufferedWriter(new FileWriter(Store_Path+"/datatyperelations.txt"));
			bfw_restrictions=new BufferedWriter(new FileWriter(Store_Path+"/restrictions.txt"));
			bfw_leaves=new BufferedWriter(new FileWriter(Store_Path+"/leaves.txt"));
			bfw_instances=new BufferedWriter(new FileWriter(Store_Path+"/instances.txt"));
			bfw_HasSub=new BufferedWriter(new FileWriter(Store_Path+"/Ontology_HasSub.txt"));
			bfw_HasSub_Direct=new BufferedWriter(new FileWriter(Store_Path+"/Ontology_HasSub_Direct.txt"));	
			bfw_HasSuper=new BufferedWriter(new FileWriter(Store_Path+"/Ontology_HasSuper.txt"));
			bfw_HasSuper_Direct=new BufferedWriter(new FileWriter(Store_Path+"/Ontology_HasSuper_Direct.txt"));	
			bfw_HasSibling=new BufferedWriter(new FileWriter(Store_Path+"/Ontology_HasSibling.txt"));	
			bfw_Disjointwith=new BufferedWriter(new FileWriter(Store_Path+"/Ontology_Disjoint.txt"));	
		}
		catch(IOException e){
			e.printStackTrace();		
		}
		
		//String ontPath ="Datasets/conference_ontologys/"+Ontology_name+".owl";//读取本体的路径
		String ontPath ="Datasets/conference_ontologys/"+Ontology_name+".owl";//读取本体的路径

		//Ontology_tools Onto=new Ontology_tools(); 
		Pellet_tools Onto=new Pellet_tools(); 


		//将本体文件进行导入
		Onto.readOnto(ontPath);
		
		/*ArrayList<String> ClassOffSpring=Onto.findConceptChildrenforObjcet("Conference_applicant");
		System.out.println(ClassOffSpring.size());*/

		//获取本体中所有的class
		ArrayList<String> Classes=Onto.GetAllConcept();
		
		//获取本体中所有的对象属性
		ArrayList<String> Properties=Onto.GetAllProperty();
		
		//获取本体中所有的数值属性
		ArrayList<String> ObjectProperties=Onto.GetObjectProperty();

		//获取本体中所有的属性
		ArrayList<String> DataProperties=Onto.GetDataProperty();
		
		//获取本体中所有的非父子关系
		//ArrayList<String> Relations=Onto.GetAllRelations();
		ArrayList<String> Relations=Onto.GetAll_Infered_Relations();
		
		//获取本体中所有的对象属性关系
		//ArrayList<String> ObjectRelations=Onto.GetObjectRelations();
		ArrayList<String> ObjectRelations=Onto.New_GetObjectRelations();

		//获取本体中所有的数值属性关系
		ArrayList<String> DatatypeRelations=Onto.GetDataPropertyRelations();
		
		//获取本体中所有的受限关系	
		//ArrayList<String> Restrictions=Onto.GetAllRestrictions();
		ArrayList<String> Restrictions=Onto.GetSomeRestrictions();
			
		//获取本体中的叶子结点
		ArrayList<String> Leaves=Onto.GetLeaves();
		
		//获取本体中的概念的实例
		ArrayList<String> Instances=Onto.GetConcept_Instances();

		//这里是获取本体中所有的子类关系
		ArrayList<String> Subclasses=Onto.GetSubclass();

		//这里仅获取本体中直接子类关系
		ArrayList<String> Subclasses_Direct=Onto.GetSubclass_Direct();
		
		//这里是获取本体中所有的父类关系
		ArrayList<String> Superclasses=Onto.GetSuperclass();

		//这里仅获取本体中直接父类关系
		ArrayList<String> Superclasses_Direct=Onto.GetSupclass_Direct();
		
		//这里仅获取本体中的等价类的表达式
		ArrayList<String> EquivalentClass=Onto.GetEquivalentClass();
		
		//这里仅获取本体中的兄弟姐妹关系
		ArrayList<String> Classes_Sibling=Onto.GetSibling(Subclasses_Direct);
					
		//这里仅获取本体中不相交关系
		ArrayList<String> Classes_Disjoint=Onto.GetDisjointwith();
		//System.out.println("+++++++++++++++++++++++++++=");
		
		//要增强的有Subclasses,Superclasses,ObjectRelations,Classes_Disjoint
		/*Subclasses=Onto.enhancedSubClasses(Subclasses,EquivalentClass);
		Superclasses=Onto.enhancedSuperClasses(Superclasses,EquivalentClass);
		Classes_Disjoint=Onto.enhancedClassesDisjoint(Classes_Disjoint,Subclasses,EquivalentClass);*/
		ObjectRelations=Onto.enhancedRelation(ObjectRelations,EquivalentClass);
		System.out.println("**********");
		ObjectRelations=Onto.enhancedRelation(ObjectRelations,Restrictions);
		//关系属性的加强
		
		
		//将其写入文件
		for(int j=0;j<Classes.size();j++)
		{
			bfw_concepts.append(Classes.get(j)+"\n");		//读取的时候会默认识别换行符的
		}
		
		for(int j=0;j<Properties.size();j++)
		{
			bfw_properties.append(Properties.get(j)+"\n");		//读取的时候会默认识别换行符的
		}
		
		for(int j=0;j<ObjectProperties.size();j++)
		{
			bfw_objectproperties.append(ObjectProperties.get(j)+"\n");		//读取的时候会默认识别换行符的
		}
		
		for(int j=0;j<DataProperties.size();j++)
		{
			bfw_dataproperties.append(DataProperties.get(j)+"\n");		//读取的时候会默认识别换行符的
		}
		
		for(int j=0;j<Relations.size();j++)
		{
			bfw_relations.append(Relations.get(j)+"\n");		//读取的时候会默认识别换行符的
		}
		
		for(int j=0;j<ObjectRelations.size();j++)
		{
			bfw_objectrelations.append(ObjectRelations.get(j)+"\n");		//读取的时候会默认识别换行符的
		}
		
		for(int j=0;j<DatatypeRelations.size();j++)
		{
			bfw_datatyperelations.append(DatatypeRelations.get(j)+"\n");		//读取的时候会默认识别换行符的
		}
		
		for(int j=0;j<Restrictions.size();j++)
		{
			//System.out.println(Restrictions.get(j));
			bfw_restrictions.append(Restrictions.get(j)+"\n");		//读取的时候会默认识别换行符的
		}
		
		for(int j=0;j<Leaves.size();j++)
		{
			bfw_leaves.append(Leaves.get(j)+"\n");		//读取的时候会默认识别换行符的
		}
		
		for(int j=0;j<Instances.size();j++)
		{
			String a=Instances.get(j);
			a=a.replace("--,", "--");
			bfw_instances.append(a+"\n");		//读取的时候会默认识别换行符的
		}
		
		
		for(int j=0;j<Subclasses.size();j++)
		{
			String a=Subclasses.get(j);
			a=a.replace("--,", "--");
			bfw_HasSub.append(a+"\n");		//读取的时候会默认识别换行符的
		}
		
		for(int j=0;j<Subclasses_Direct.size();j++)
		{
			String a=Subclasses_Direct.get(j);
			a=a.replace("--,", "--");
			bfw_HasSub_Direct.append(a+"\n");		//读取的时候会默认识别换行符的
		}
		
		for(int j=0;j<Superclasses.size();j++)
		{
			String a=Superclasses.get(j);
			a=a.replace("--,", "--");
			bfw_HasSuper.append(a+"\n");		//读取的时候会默认识别换行符的
		}
		//System.out.println(Superclasses.size());
		
		for(int j=0;j<Superclasses_Direct.size();j++)
		{
			String a=Superclasses_Direct.get(j);
			a=a.replace("--,", "--");
			bfw_HasSuper_Direct.append(a+"\n");		//读取的时候会默认识别换行符的
		}
		
		for(int j=0;j<Classes_Sibling.size();j++)
		{
			String a=Classes_Sibling.get(j);
			a=a.replace("--,", "--");
			bfw_HasSibling.append(a+"\n");		//读取的时候会默认识别换行符的
		}
			
		
		for(int j=0;j<Classes_Disjoint.size();j++)
		{
			String a=Classes_Disjoint.get(j);
			a=a.replace("--,", "--");
			bfw_Disjointwith.append(a+"\n");		//读取的时候会默认识别换行符的
		}
	
		
		//文件输入流关闭
		bfw_concepts.close();
		bfw_properties.close();
		bfw_objectproperties.close();
		bfw_dataproperties.close();
		bfw_relations.close();
		bfw_objectrelations.close();
		bfw_datatyperelations.close();
		bfw_restrictions.close();
		bfw_instances.close();
		bfw_leaves.close();
		bfw_HasSub.close();
		bfw_HasSub_Direct.close();	
		bfw_HasSuper.close();
		bfw_HasSuper_Direct.close();
		bfw_HasSibling.close();
		bfw_Disjointwith.close();
		System.out.println(Ontology_name+" has been done!");
		
		}
	//}//循环
}
