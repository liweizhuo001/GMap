package Preprocessing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;





import Tools.Ontology_tools;
//import Tools.Ontology_tools;
import Tools.Pellet_tools;

public class Get_benchmark_informations {
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
		/*String []Ontology={"101","103","104","201-2","201-4","201-6","201-8","201","202-2","202-4","202-6","202-8","202",
						   "203","204","205","206","207","208","209","210","221","222","223","225","228","230","231","233",
						   "239","240","241","246","247","248-2","248-4","248-6","248-8","248","250-2","250-4","250-6",
						   "250-8","250","251-2","251-4","251-6","251-8","251","252-2","252-4","252-6","252-8","252","254-2",
						   "254-4","254-6","254-8","254","257-2","257-4","257-6","257-8","257","260-2","260-4","260-6",
						   "260-8","260","261-2","261-4","261-6","261-8","261","262-2","262-4","262-6","262-8","262","265",
						   "266","301","302","303","304"};*/
		/*String []Ontology={"224","232","237","238","249-2","249-4","249-6","249-8","249","253-2","253-4","253-6",
							"253-8","253","258-2","258-4","258-6","258-8","258","259-2","259-4","259-6",
							"259-8","259"};				   
		for (int x=0;x<Ontology.length;x++)
		{
			String Ontology_name = Ontology[x]+"onto";*/
		String Ontology_name="101onto";
		//完全不能解析: 224 232 237 238 249-2 249-4 249-6 249-8 249 253-2 253-4 253-6 253-8 253 258-2 258-4 258-4
		//258-8 258 259-2 259-4 259-6 259-8 259
		//部分解析问题: 204 
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
		String ontPath ="Datasets/benchmarks/"+Ontology_name+".rdf";//读取本体的路径

		//Ontology_tools Onto1=new Ontology_tools(); 
		Pellet_tools Onto=new Pellet_tools(); 


		//将本体文件进行导入
		Onto.readOnto(ontPath);

		//获取本体中所有的class
		ArrayList<String> Classes=Onto.GetAllConcept();//204解析的时候会有Bug
		
		//获取本体中所有的属性
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
		ArrayList<String> Restrictions=Onto.GetAllRestrictions();
		
		//ArrayList<String> Restrictions=Onto.GetSomeRestrictions();
			
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
		
		//这里仅获取本体中的兄弟姐妹关系
		ArrayList<String> Classes_Sibling=Onto.GetSibling(Subclasses_Direct);
			
		//这里仅获取本体中不相交关系
		ArrayList<String> Classes_Disjoint=Onto.GetDisjointwith();
		//System.out.println("+++++++++++++++++++++++++++=");
		

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
		
		//}	//循环
	}
}
