package Preprocessing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//import Tools.Ontology_tools;
import Tools.Pellet_tools;

public class Get_I3CON_informations {
	public static void main(String args[]) throws IOException
	{
		//�����ļ���д����
		BufferedWriter bfw_concepts= null;
		BufferedWriter bfw_properties= null;
		BufferedWriter bfw_relations= null;
		BufferedWriter bfw_restrictions= null;
		BufferedWriter bfw_leaves= null;
		BufferedWriter bfw_instances= null;		
		BufferedWriter bfw_HasSub= null;
		BufferedWriter bfw_HasSub_Direct= null;
		BufferedWriter bfw_HasSuper= null;
		BufferedWriter bfw_HasSuper_Direct= null;
		BufferedWriter bfw_Disjointwith= null;
	
		//�������·��
	/*	String []Ontology={"animals","hotel","network","people+pets","russia","Weapons","Wine"};
		for (int x=0;x<Ontology.length;x++)
		{
			String Ontology_name = Ontology[x];*/
		String Ontology_name="people+petsA";
		//String Ontology_name="people+pets";
		String Store_Path="Intermediate_Data/"+Ontology_name;//��ȡ�����·��
		try{
			bfw_concepts=new BufferedWriter(new FileWriter(Store_Path+"/concepts.txt"));
			bfw_properties=new BufferedWriter(new FileWriter(Store_Path+"/properties.txt"));
			bfw_relations=new BufferedWriter(new FileWriter(Store_Path+"/relations.txt"));
			bfw_restrictions=new BufferedWriter(new FileWriter(Store_Path+"/restrictions.txt"));
			bfw_leaves=new BufferedWriter(new FileWriter(Store_Path+"/leaves.txt"));
			bfw_instances=new BufferedWriter(new FileWriter(Store_Path+"/instances.txt"));
			bfw_HasSub=new BufferedWriter(new FileWriter(Store_Path+"/Ontology_HasSub.txt"));
			bfw_HasSub_Direct=new BufferedWriter(new FileWriter(Store_Path+"/Ontology_HasSub_Direct.txt"));	
			bfw_HasSuper=new BufferedWriter(new FileWriter(Store_Path+"/Ontology_HasSuper.txt"));
			bfw_HasSuper_Direct=new BufferedWriter(new FileWriter(Store_Path+"/Ontology_HasSuper_Direct.txt"));	
			bfw_Disjointwith=new BufferedWriter(new FileWriter(Store_Path+"/Ontology_Disjoint.txt"));	
		}
		catch(IOException e){
			e.printStackTrace();		
		}
		
		String ontPath ="Datasets/I3CON_ontologys/"+Ontology_name+".owl";//��ȡ�����·��
		//String ontPath ="Datasets/"+Ontology_name+".owl";//��ȡ�����·��

		//Ontology_tools Onto=new Ontology_tools(); 
		Pellet_tools Onto=new Pellet_tools(); 


		//�������ļ����е���
		Onto.readOnto(ontPath);

		//��ȡ���������е�class
		ArrayList<String> Classes=Onto.GetAllConcept();
		
		//��ȡ���������е�����
		ArrayList<String> Properties=Onto.GetAllProperty();
		
		//��ȡ���������еķǸ��ӹ�ϵ
		ArrayList<String> Relations=Onto.GetAllRelations();
		//ArrayList<String> Relations=Onto.GetAll_Infered_Relations();
		
		//��ȡ���������е����޹�ϵ	
		ArrayList<String> Restrictions=Onto.GetAllRestrictions();
			
		//��ȡ�����е�Ҷ�ӽ��
		ArrayList<String> Leaves=Onto.GetLeaves();
		
		//��ȡ�����еĸ����ʵ��
		ArrayList<String> Instances=Onto.GetConcept_Instances();

		//�����ǻ�ȡ���������е������ϵ
		ArrayList<String> Subclasses=Onto.GetSubclass();

		//�������ȡ������ֱ�������ϵ
		ArrayList<String> Subclasses_Direct=Onto.GetSubclass_Direct();
		
		//�����ǻ�ȡ���������еĸ����ϵ
		ArrayList<String> Superclasses=Onto.GetSuperclass();

		//�������ȡ������ֱ�Ӹ����ϵ
		ArrayList<String> Superclasses_Direct=Onto.GetSupclass_Direct();
			
		//�������ȡ�����в��ཻ��ϵ
		ArrayList<String> Classes_Disjoint=Onto.GetDisjointwith();
		
		ArrayList<String> EquivalentProperty=Onto.GetEquivalentProperty();
		//System.out.println("+++++++++++++++++++++++++++=");
		

		//����д���ļ�
		for(int j=0;j<Classes.size();j++)
		{
			bfw_concepts.append(Classes.get(j)+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
		
		for(int j=0;j<Properties.size();j++)
		{
			bfw_properties.append(Properties.get(j)+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
		
		for(int j=0;j<Relations.size();j++)
		{
			bfw_relations.append(Relations.get(j)+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
		
		for(int j=0;j<Restrictions.size();j++)
		{
			//System.out.println(Restrictions.get(j));
			bfw_restrictions.append(Restrictions.get(j)+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
		
		for(int j=0;j<Leaves.size();j++)
		{
			bfw_leaves.append(Leaves.get(j)+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
		
		for(int j=0;j<Instances.size();j++)
		{
			String a=Instances.get(j);
			a=a.replace("--,", "--");
			bfw_instances.append(a+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
		
		
		for(int j=0;j<Subclasses.size();j++)
		{
			String a=Subclasses.get(j);
			a=a.replace("--,", "--");
			bfw_HasSub.append(a+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
		
		for(int j=0;j<Subclasses_Direct.size();j++)
		{
			String a=Subclasses_Direct.get(j);
			a=a.replace("--,", "--");
			bfw_HasSub_Direct.append(a+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
		
		for(int j=0;j<Superclasses.size();j++)
		{
			String a=Superclasses.get(j);
			a=a.replace("--,", "--");
			bfw_HasSuper.append(a+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
		//System.out.println(Superclasses.size());
		
		for(int j=0;j<Superclasses_Direct.size();j++)
		{
			String a=Superclasses_Direct.get(j);
			a=a.replace("--,", "--");
			bfw_HasSuper_Direct.append(a+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
			
		
		for(int j=0;j<Classes_Disjoint.size();j++)
		{
			String a=Classes_Disjoint.get(j);
			a=a.replace("--,", "--");
			bfw_Disjointwith.append(a+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
	
		
		//�ļ��������ر�
		bfw_concepts.close();
		bfw_properties.close();
		bfw_relations.close();
		bfw_restrictions.close();
		bfw_instances.close();
		bfw_leaves.close();
		bfw_HasSub.close();
		bfw_HasSub_Direct.close();	
		bfw_HasSuper.close();
		bfw_HasSuper_Direct.close();
		bfw_Disjointwith.close();
		System.out.println(Ontology_name+" has been done!");
		
		}
		
		
	//}//ѭ��
}
