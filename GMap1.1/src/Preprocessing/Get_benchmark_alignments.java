package Preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Tools.Ontology_tools;

public class Get_benchmark_alignments {
	public static void main(String args[]) throws IOException
	{		
		BufferedWriter bfw_class= null;	
		BufferedWriter bfw_property= null;
		/*String []Ontology1={"101","103","104","201-2","201-4","201-6","201-8","201","202-2","202-4","202-6","202-8","202",
						   "203","204","205","206","207","208","209","210","221","222","223","225","228","230","231","233",
						   "239","240","241","246","247","248-2","248-4","248-6","248-8","248","250-2","250-4","250-6",
						   "250-8","250","251-2","251-4","251-6","251-8","251","252-2","252-4","252-6","252-8","252","254-2",
						   "254-4","254-6","254-8","254","257-2","257-4","257-6","257-8","257","260-2","260-4","260-6",
						   "260-8","260","261-2","261-4","261-6","261-8","261","262-2","262-4","262-6","262-8","262","265",
						   "266","301","302","303","304"};*/
		/*String []Ontology1={"224","232","236","237","238","249-2","249-4","249-6","249-8","249","253-2","253-4","253-6",
							"253-8","253","258-2","258-4","258-6","258-8","258","259-2","259-4","259-6","259-8","259"};	
		String []Ontology2={"101"};
		for (int x=0;x<Ontology1.length;x++)
		{
			String Ontology_name1 = Ontology1[x];
			for(int y=0;y<Ontology2.length;y++)
			{			
				String Ontology_name2 = Ontology2[y];*/
		String Ontology_name1="101";
		String Ontology_name2="101";
		String Store_Class_Path="Results/Benchmark_alignments/"+Ontology_name1+"onto"+"-"+Ontology_name2+"onto"+"_class.txt";
		String Store_Property_Path="Results/Benchmark_alignments/"+Ontology_name1+"onto"+"-"+Ontology_name2+"onto"+"_property.txt";
		try{		
			/*bfw_class=new BufferedWriter(new FileWriter("Results/First_line_matcher/cmt-conference_class.txt"));
			bfw_property=new BufferedWriter(new FileWriter("Results/First_line_matcher/cmt-conference_property.txt"));*/
			bfw_class=new BufferedWriter(new FileWriter(Store_Class_Path));
			bfw_property=new BufferedWriter(new FileWriter(Store_Property_Path));		
		}catch(IOException e){
			e.printStackTrace();		
		}
		
		
		String Read_Path="Intermediate_Data/"+Ontology_name2+"onto";//��ȡ�����·��
		
		BufferedReader  Ontology_Concept= new BufferedReader (new FileReader(new File(Read_Path+"/concepts.txt")));
		ArrayList<String> Classes1=new ArrayList<String>();
		String lineTxt = null;
		  while ((lineTxt = Ontology_Concept.readLine()) != null) {
				String line = lineTxt.trim(); //ȥ���ַ�����λ�Ŀո񣬱�����ո���ɵĴ���						
				Classes1.add(line);				
			}
		
		Ontology_tools Match=new Ontology_tools();
		
		String reference_Path="Datasets/benchmarks_alignments/"+Ontology_name1+"refalign.rdf";;//��ȡ�����·��
		
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
			//System.out.println(class_match.get(j));
			bfw_class.append(class_match.get(j)+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}			
		System.out.println("�����ƥ�����Ϊ��"+class_match.size());
		ArrayList<String> property_match=Match.filter_class(initial_match,class_match);
		for(int L=0;L<property_match.size();L++)
		{
			//System.out.println(property_match.get(L));
			bfw_property.append(property_match.get(L)+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}
		System.out.println("���Ե�ƥ�����Ϊ��"+property_match.size());
		bfw_class.close();
		bfw_property.close();
		System.out.println(Ontology_name1+"and"+Ontology_name2+" have been done");
		/*//ѭ��ģʽ
			}
		}*/
	}
}
