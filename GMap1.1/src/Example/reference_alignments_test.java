package Example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Tools.Ontology_tools;

public class reference_alignments_test {
	public static void main(String args[]) throws IOException
	{				
		Ontology_tools Match=new Ontology_tools();
		String reference_Path="result/russiaA-russiaA.rdf";//读取本体的路径
	//	String reference_Path="Datasets/conference_reference_alignments/animalsAB.rdf";//读取本体的路径
	//	String reference_Path="Datasets/conference_reference_alignments/cmt-conference-2009.rdf";//读取本体的路径
	//	String reference_Path="Datasets/conference_alignments_of_systems/YAM++-ekaw-iasted.rdf";//读取本体的路径
		
		ArrayList<String> initial_match=Match.get_reference(reference_Path); 
	//	ArrayList<String> initial_match=Match.get_reference("Datasets/conference_reference_alignments/cmt-conference.rdf"); 
		//ArrayList<String> initial_match=Match.get_reference("Data/conference_alignments_subset/YAM++-cmt-conference.rdf"); 
		
		//将URL进行过滤
		System.out.println("###########################");
		for(int i=0;i<initial_match.size();i++)
		{
			System.out.println(initial_match.get(i));
			//bfw_class.append(initial_match.get(i)+"\n");

		}
		System.out.println("Reference的总匹配长度为："+initial_match.size());
		
		System.out.println("done");
	}
}
