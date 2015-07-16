package Preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Tools.Ontology_tools;

public class Get_I3CON_alignments {
     //������Ϊ�˴������� ��֪��TreeMap�ṹ������Ҫ��ȡ�����ĸ������õ��ķ���
	public static void main (String[] args) throws IOException
	{
		String Ontology_name="WineAB";
		BufferedWriter bfw_class= null;	
		BufferedWriter bfw_property= null;
		try
		{
			bfw_class=new BufferedWriter(new FileWriter("Results/I3CON_alignments/"+Ontology_name+"_class.txt"));
			bfw_property=new BufferedWriter(new FileWriter("Results/I3CON_alignments/"+Ontology_name+"_property.txt"));
		}
		catch(IOException e)
		{
			e.printStackTrace();		
		}
			
		BufferedReader  Alignment_Reader = new BufferedReader (new FileReader(new File("Datasets/I3CON_alignments/"+Ontology_name+".n3")));
		String lineTxt = null;
		ArrayList<String> alignments=new ArrayList<String>();
		int i=1;
		boolean flag=false;
		int num1=0,num2=0;
		String map1="",map2="";
		while ((lineTxt = Alignment_Reader.readLine()) != null)
		{
			String line = lineTxt.trim(); //ȥ���ַ�����λ�Ŀո񣬱�����ո���ɵĴ���		
			if(line.contains("Alignment"+i))
			{
				flag=true;//��ʾ��������alignment�Ľ��
				num1=-1;
				num2=-1;
				i++;	
			}
			num1++;
			num2++;
			if(num1==2&&flag==true)
			{
				map1=line.replace("ao:elementA a:", "").replace(" ;", "");			
			}
			if(num2==3&&flag==true)
			{
				map2=line.replace("ao:elementB b:", "").replace(" ;", "");
				alignments.add(map1+"--"+map2);
				flag=false;
			}
		}
		System.out.println("alignments�ĸ���Ϊ��"+alignments.size());
		for(String a:alignments)
		{			
			System.out.println(a);
		}

		
		String Read_Path1="Intermediate_Data/"+Ontology_name.replace("AB", "A");//��ȡ�����·��
		String Read_Path2="Intermediate_Data/"+Ontology_name.replace("AB", "B");//��ȡ�����·��

		BufferedReader  Ontology_Concept1= new BufferedReader (new FileReader(new File(Read_Path1+"/concepts.txt")));
		ArrayList<String> Classes1=new ArrayList<String>();
		lineTxt = null;
		while ((lineTxt = Ontology_Concept1.readLine()) != null) {
			String line = lineTxt.trim(); //ȥ���ַ�����λ�Ŀո񣬱�����ո���ɵĴ���						
			Classes1.add(line);				
		}
		
		BufferedReader  Ontology_Concept2= new BufferedReader (new FileReader(new File(Read_Path2+"/concepts.txt")));
		ArrayList<String> Classes2=new ArrayList<String>();
		lineTxt = null;
		while ((lineTxt = Ontology_Concept2.readLine()) != null) {
			String line = lineTxt.trim(); //ȥ���ַ�����λ�Ŀո񣬱�����ո���ɵĴ���						
			Classes2.add(line);				
		}
		
		BufferedReader  Ontology_Property= new BufferedReader (new FileReader(new File(Read_Path1+"/properties.txt")));
		ArrayList<String> Property1=new ArrayList<String>();
		lineTxt = null;
		while ((lineTxt = Ontology_Property.readLine()) != null) {
			String line = lineTxt.trim(); //ȥ���ַ�����λ�Ŀո񣬱�����ո���ɵĴ���						
			Property1.add(line);				
		}
		
		
		Ontology_tools Match=new Ontology_tools();
		ArrayList<String> class_match=Match.new_keep_class(alignments,Classes1,Classes2);
		System.out.println("###########################");		
		for(int j=0;j<class_match.size();j++)
		{
			System.out.println(class_match.get(j));
			bfw_class.append(class_match.get(j)+"\n");		//��ȡ��ʱ���Ĭ��ʶ���з���
		}			
		System.out.println("�����ƥ�����Ϊ��"+class_match.size());
		ArrayList<String> property_match=Match.keep_property(alignments,Property1);
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
