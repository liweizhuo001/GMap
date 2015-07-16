package Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import SPT.Spt;

public class Test {
	public static void main (String[] args) 
	{
		/*double x[]={0,0.25,0.5,0.75,1,1.1,2,3};
		for(double k:x)
		{
			double y=1-Math.sin(Math.acos(k));
			System.out.print(y+"	");
		}*/
		/*ArrayList<String> old_SV=new ArrayList<String>();
		ArrayList<String> new_SV=new ArrayList<String>();
		old_SV.add("a");
		old_SV.add("b");
		old_SV.add("c");
		new_SV.add("a");
		new_SV.add("b");
		new_SV.add("a");
		boolean flag=IsConverge(old_SV,new_SV);
		System.out.println(flag);*/
		boolean recursiveFlag = true;
		boolean debugFlag = false;
		int rLevel = 1;
       // String inFile = "/export/home/lu/Development/LVG/Components/Applications/SynonymPermutationTool/data/Spt/test.data";
        String inFile = "dic/normTermSynonyms.data.2015";
    	String inTerm = "vagus X nerve right recurrent laryngeal branch";
    	Spt s=new Spt();
		HashSet<String> synonym = s.GetSynonymPermutationRecursive(inFile, inTerm, recursiveFlag, debugFlag, rLevel);
		System.out.println("----- Synonyms permutation results ------");
		for(String a:synonym)
		{	
			System.out.println(a);
		}
		System.out.println("--------------------------------------------------");
		System.out.println(inTerm+"'s  set of synonyms is "+synonym.size());
		System.out.println("--------------------------------------------------");
		System.out.println(synonym.contains("vault"));
		/*String temp="vagus X nerve right recurrent laryngeal branch";
		String parts[]=temp.split(" ");
		String new_temp=temp.replace(parts[0]+" ", "");
		System.out.println(new_temp);*/
		
	}
	public static boolean IsConverge(ArrayList<String> old_SV,ArrayList<String> new_SV)
	{
		boolean flag=false;
		for(int i=0;i<new_SV.size();i++)
		{
			if(old_SV.contains(new_SV.get(i)))
			{
				flag=true;
			}
			else
			{
				return false;
			}
		}
		return flag;
	}

}
