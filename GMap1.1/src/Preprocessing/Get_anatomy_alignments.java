package Preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Tools.Ontology_tools;

public class Get_anatomy_alignments {
	public static void main(String args[]) throws IOException
	{		
		BufferedWriter bfw_class= null;	
		BufferedWriter bfw_property= null;
		
		String Ontology_name1="mouse";
		String Ontology_name2="human";
		String Store_Class_Path="Results/Anatomy_alignments/"+Ontology_name1+"-"+Ontology_name2+"_class.txt";
		String Store_Property_Path="Results/Anatomy_alignments/"+Ontology_name1+"-"+Ontology_name2+"_property.txt";
		try{		
			/*bfw_class=new BufferedWriter(new FileWriter("Results/First_line_matcher/cmt-conference_class.txt"));
			bfw_property=new BufferedWriter(new FileWriter("Results/First_line_matcher/cmt-conference_property.txt"));*/
			bfw_class=new BufferedWriter(new FileWriter(Store_Class_Path));
			bfw_property=new BufferedWriter(new FileWriter(Store_Property_Path));		
		}catch(IOException e){
			e.printStackTrace();		
		}
		
		
		String Read_Path="Intermediate_Data/"+Ontology_name1;//��ȡ�����·��
		
		BufferedReader  Ontology_Concept= new BufferedReader (new FileReader(new File(Read_Path+"/concepts.txt")));
		ArrayList<String> Classes1=new ArrayList<String>();
		String lineTxt = null;
		  while ((lineTxt = Ontology_Concept.readLine()) != null) {
				String line = lineTxt.trim(); //ȥ���ַ�����λ�Ŀո񣬱�����ո���ɵĴ���						
				Classes1.add(line);				
			}
		  /*	System.out.println(Classes1.size());
			for(int j=0;j<Classes1.size();j++)
			{
				System.out.println(Classes1.get(j));
			}*/
		
		Ontology_tools Match=new Ontology_tools();
		
		String reference_Path="Datasets/anatomy/"+Ontology_name1+"-"+Ontology_name2+".rdf";;//��ȡ�����·��
		
		ArrayList<String> initial_match=Match.get_reference(reference_Path); 
	//	ArrayList<String> initial_match=Match.get_reference("Datasets/conference_reference_alignments/cmt-conference.rdf"); 
		//ArrayList<String> initial_match=Match.get_reference("Data/conference_alignments_subset/YAM++-cmt-conference.rdf"); 
		
		//��URL���й���
		System.out.println("###########################");
		for(int i=0;i<initial_match.size();i++)
		{
			System.out.println(initial_match.get(i));
			//bfw_class.append(initial_match.get(i)+"\n");

		}
		System.out.println("Reference����ƥ�䳤��Ϊ��"+initial_match.size());
		
		
		//������ƥ��Ժ����ƥ��Խ�ȥ����
		ArrayList<String> class_match=Match.keep_class(initial_match,Classes1);
		System.out.println("###########################");		
		for(int j=0;j<class_match.size();j++)
		{
			System.out.println(class_match.get(j));
			bfw_class.append(class_match.get(j)+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}			
		System.out.println("�����ƥ�����Ϊ��"+class_match.size());
		ArrayList<String> property_match=Match.filter_class(initial_match,class_match);
		for(int L=0;L<property_match.size();L++)
		{
			System.out.println(property_match.get(L));
			bfw_property.append(property_match.get(L)+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
		System.out.println("���Ե�ƥ�����Ϊ��"+property_match.size());
		bfw_class.close();
		bfw_property.close();
		System.out.println("done");
		
	}
}
