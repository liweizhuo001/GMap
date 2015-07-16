package Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import JWS.JWS;
import JWS.JiangAndConrath;
import JWS.Lin;
import SPT.Spt;

public class Sim_Tools {
	
	 private MaxentTagger tagger;
	 Morphology method=new Morphology();
	 private String dir = "WordNet";
	 private JWS  ws = new JWS(dir, "2.1");
	 
	// MaxentTagger tagger=new MaxentTagger("models/bidirectional-distsim-wsj-0-18.tagger");
	/* public Sim_Tools(String path) throws IOException, ClassNotFoundException
	 {
		 tagger=new MaxentTagger("models/bidirectional-distsim-wsj-0-18.tagger");
	 }*/	
	 
	/**
	 * This partition is used to calculate the Edit Distance Similarity
	 * @param object1s
	 * @param object2s
	 * @return editDistance
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void initialPOS() throws ClassNotFoundException, IOException
	{
		 tagger=new MaxentTagger("models/bidirectional-distsim-wsj-0-18.tagger");
		 //System.out.println(tagger.tagString("written"));
		 System.out.println("initial is successful!");
	}
	 
	public ArrayList<String> stringEquivSimClass(ArrayList<String> object1s, ArrayList<String> object2s) 
	{
		ArrayList<String> stringEquiv=new ArrayList<String>();
		for(int i=0;i<object1s.size();i++)
		{
			String str1=object1s.get(i).toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
			for(int j=0;j<object2s.size();j++)
			{
				//System.out.println(str2);
				String str2=object2s.get(j).toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
				if(str1.equals(str2))
				{
					stringEquiv.add(str1+","+str2+","+1);
				}
				else
				{
					stringEquiv.add(str1+","+str2+","+0);
				}
			}
		}		
		return stringEquiv;
	}
	
	public ArrayList<String> stringEquivSimClass2(ArrayList<String> classes1, ArrayList<String> classes2,ArrayList<String> object1s, ArrayList<String> object2s) 
	{
		ArrayList<String> stringEquiv=new ArrayList<String>();
		for(int i=0;i<object1s.size();i++)
		{
			String str1=tokeningWord(object1s.get(i));
			for(int j=0;j<object2s.size();j++)
			{
				//System.out.println(str2);
				String str2=tokeningWord(object2s.get(j));
				if(str1.equals(str2))
				{
					stringEquiv.add(classes1.get(i)+","+classes2.get(j)+","+1);
				}
				else
				{
					stringEquiv.add(classes1.get(i)+","+classes2.get(j)+","+0);
				}
			}
		}		
		return stringEquiv;
	}
	
	public ArrayList<String> editSimClass(ArrayList<String> object1s, ArrayList<String> object2s) 
	{
		ArrayList<String> editDistance=new ArrayList<String>();
		int number=0;
		for(int i=0;i<object1s.size();i++)
		{
			String str1=object1s.get(i).toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
			//String str1="extrahepatic bile duct epithelium".toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
			for(int j=0;j<object2s.size();j++)
			{
				//System.out.println(str2);
				String str2=object2s.get(j).toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
				//String str2="extrahepatic_bile_duct_epithelium".toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
				int ld = similarityOfDistance(str1, str2);		
				float sim=1 - (float) ld / Math.max(str1.length(), str2.length());
				//editDistance.add(object1s.get(i)+","+object2s.get(j)+","+sim);
				if(sim>0)
					number++;
			/*	if(sim==1)
					System.out.println(object1s.get(i)+","+object2s.get(j));*/
				editDistance.add(object1s.get(i)+","+object2s.get(j)+","+sim+","+ld);
			}
		}	
		System.out.println("编辑距离相似度不为0的个数为："+number);
		return editDistance;
	}
	
	public ArrayList<String> editSimClassWithLabel(ArrayList<String> object1s, ArrayList<String> object2s,ArrayList<String> label1,ArrayList<String> label2) 
	{
		TreeMap_Tools classLabel1=new TreeMap_Tools(label1);//这里转换成小写字母了
		TreeMap_Tools classLabel2=new TreeMap_Tools(label2);
		ArrayList<String> editDistance=new ArrayList<String>();
		for(int i=0;i<object1s.size();i++)
		{
			///String str1="dzqndbzq";
			String str1=object1s.get(i).toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
			ArrayList<String> strlabel1=classLabel1.GetKey_Value(str1);
			
			for(int j=0;j<object2s.size();j++)
			{
				//String str2="chapter";
				//System.out.println(str2);
				String str2=object2s.get(j).toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
				ArrayList<String> strlabel2=classLabel2.GetKey_Value(str2);			
				int ld = similarityOfDistance(str1, str2);		
				double sim=1 - (float) ld / Math.max(str1.length(), str2.length());
				if(strlabel1!=null&&strlabel2==null)
				{
					String label=strlabel1.get(0).replace("_", "").replace("-", "").replace(" ", "");
					int ld1 = similarityOfDistance (label,str2);	
					
					double sim1=1 - (float) ld1 / Math.max(label.length(), str2.length());
					sim=Math.max(sim, sim1);
				}
				else if(strlabel1==null&&strlabel2!=null)
				{
					String label=strlabel2.get(0).replace("_", "").replace("-", "").replace(" ", "");
					int ld2=similarityOfDistance(str1, label);
					double sim2=1 - (float) ld2 / Math.max(str1.length(), label.length());					
					sim=Math.max(sim, sim2);				
				}
				else if(strlabel1!=null&&strlabel2!=null)
				{
					String conceptLabel1=strlabel1.get(0).replace("_", "").replace("-", "").replace(" ", "");
					String conceptLabel2=strlabel2.get(0).replace("_", "").replace("-", "").replace(" ", "");
					
					int ld1 = similarityOfDistance(conceptLabel1, str2);		
					double sim1=1 - (float) ld1 / Math.max(conceptLabel1.length(), str2.length());
					
					sim=Math.max(sim, sim1);
					
					int ld2=similarityOfDistance(str1, conceptLabel2);
					double sim2=1 - (float) ld2 / Math.max(str1.length(), conceptLabel2.length());
					
					sim=Math.max(sim, sim2);
					
					int ld3=similarityOfDistance(conceptLabel1, conceptLabel2);
					double sim3=1 - (float) ld3 / Math.max(conceptLabel1.length(), conceptLabel2.length());
					
					sim=Math.max(sim, sim3);
				}
				//editDistance.add(object1s.get(i)+","+object2s.get(j)+","+sim);
				editDistance.add(object1s.get(i)+","+object2s.get(j)+","+sim+","+ld);
			}
		}		
		return editDistance;
	}

	public ArrayList<String> editSimProperty(ArrayList<String> object1s, ArrayList<String> object2s) 
	{
		ArrayList<String> editSimProperty=new ArrayList<String>();
		String flag1="0";
		String flag2="0";		
		for(int i=0;i<object1s.size();i++)
		{
			String concept1=object1s.get(i);
			//String concept1="writes";
			String stemObject1[]=fliterThings(concept1);
			String str1=stemObject1[0].replace(" ", "");
			String label1=stemObject1[1];
			if(!str1.equals(object1s.get(i).toLowerCase())&&label1.equals("1"))
				flag1="*";//已经发生了改变,且是被动
			else if(!str1.equals(object1s.get(i).toLowerCase())&&label1.equals("0"))
				flag1="1";//已经发生了改变，是主动
			else		  
				flag1="0";//没有发生任何改变
			for(int j=0;j<object2s.size();j++)
			{
				//System.out.println(str2);
				String concept2=object2s.get(j);
				//String concept2="is_written_by";
				String stemObject2[]=fliterThings(concept2);
				String str2=stemObject2[0].replace(" ","");
				String label2=stemObject2[1];
				if(!str2.equals(object2s.get(j).toLowerCase())&&label2.equals("1"))
					flag2="*";//已经发生了改变,且是被动
				else if(!str2.equals(object2s.get(j).toLowerCase())&&label2.equals("0"))
					flag2="1";//已经发生了改变,且是主动
				else
					flag2="0";//没有发生任何改变
				int ld = similarityOfDistance(str1, str2);		
				float sim=1 - (float) ld / Math.max(str1.length(), str2.length());
				editSimProperty.add(object1s.get(i)+","+object2s.get(j)+","+sim+","+flag1+","+flag2+","+str1+","+str2);
				/*if(flag1==true&&flag2==true&&sim>=0.6)
				{
					System.out.println(object1s.get(i)+","+object2s.get(j)+","+sim);
				}	*/			
				flag2="0";	
			}
			flag1="0";
		}		
		return editSimProperty;
	}
	
	public ArrayList<String> editSimPropertyWithLabel(ArrayList<String> object1s, ArrayList<String> object2s,ArrayList<String> labels1,ArrayList<String> labels2) 
	{
		ArrayList<String> editSimProperty=new ArrayList<String>();
		HashMap<String,String> propertyLabel1=changetoHashMap(labels1);
		HashMap<String,String> propertyLabel2=changetoHashMap(labels2);
		String flag1="0";
		String flag2="0";	
		String flag11="0";
		String flag22="0";
		for(int i=0;i<object1s.size();i++)
		{
			String concept1=object1s.get(i);
			//String concept1="writes";
			String stemObject1[]=fliterThings(concept1);
			String str1=stemObject1[0].replace(" ", "");
			String label1=stemObject1[1];
			if(!str1.equals(concept1.toLowerCase())&&label1.equals("1"))
				flag1="*";//已经发生了改变,且是被动
			else if(!str1.equals(concept1.toLowerCase())&&label1.equals("0"))
				flag1="1";//已经发生了改变，是主动
			else		  
				flag1="0";//没有发生任何改变
			String strlabel1=propertyLabel1.get(concept1);
			for(int j=0;j<object2s.size();j++)
			{
				//System.out.println(str2);
				String concept2=object2s.get(j);
				//String concept2="is_written_by";
				String stemObject2[]=fliterThings(concept2);
				String str2=stemObject2[0].replace(" ","");
				String label2=stemObject2[1];
				if(!str2.equals(concept2.toLowerCase())&&label2.equals("1"))
					flag2="*";//已经发生了改变,且是被动
				else if(!str2.equals(concept2.toLowerCase())&&label2.equals("0"))
					flag2="1";//已经发生了改变,且是主动
				else
					flag2="0";//没有发生任何改变
				String strlabel2=propertyLabel2.get(concept2);
				int ld = similarityOfDistance(str1, str2);		
				double sim=1 - (float) ld / Math.max(str1.length(), str2.length());
				
				if(strlabel1!=null&&strlabel2==null)
				{
					String stemObject11[]=fliterThings(strlabel1);
					String str11=stemObject11[0].replace(" ", "");
					String label11=stemObject11[1];
					if(!str11.equals(strlabel1.toLowerCase())&&label11.equals("1"))
						flag11="*";//已经发生了改变,且是被动
					else if(!str11.equals(strlabel1.toLowerCase())&&label11.equals("0"))
						flag11="1";//已经发生了改变，是主动
					else		  
						flag11="0";//没有发生任何改变
					
					String label=str11.replace("_", "").replace("-", "").replace(" ", "");
					
					int ld1 = similarityOfDistance(label, str2);			
					double sim1=1 - (float) ld1 / Math.max(label.length(), str2.length());
					
					//sim=Math.max(sim, sim1);
					if(sim>=sim1)
						editSimProperty.add(object1s.get(i)+","+object2s.get(j)+","+sim+","+flag1+","+flag2+","+str1+","+str2);
					else
						editSimProperty.add(object1s.get(i)+","+object2s.get(j)+","+sim1+","+flag11+","+flag2+","+str11+","+str2);
					
				}
				else if(strlabel1==null&&strlabel2!=null)
				{
					String stemObject22[]=fliterThings(strlabel2);
					String str22=stemObject22[0].replace(" ", "");
					String label22=stemObject22[1];
					if(!str22.equals(strlabel2.toLowerCase())&&label22.equals("1"))
						flag22="*";//已经发生了改变,且是被动
					else if(!str22.equals(strlabel2.toLowerCase())&&label22.equals("0"))
						flag22="1";//已经发生了改变，是主动
					else		  
						flag22="0";//没有发生任何改变
					
					String label=str22.replace("_", "").replace("-", "").replace(" ", "");
					
					int ld2=similarityOfDistance(str1,label);
					double sim2=1 - (float) ld2 / Math.max(str1.length(), label.length());
					if(sim>=sim2)
						editSimProperty.add(object1s.get(i)+","+object2s.get(j)+","+sim+","+flag1+","+flag2+","+str1+","+str2);
					else
						editSimProperty.add(object1s.get(i)+","+object2s.get(j)+","+sim2+","+flag1+","+flag22+","+str1+","+str22);
				}			
				else if(strlabel1!=null&&strlabel2!=null)
				{
					String stemObject11[]=fliterThings(strlabel1);
					String str11=stemObject11[0].replace(" ", "");
					String label11=stemObject11[1];
					if(!str11.equals(strlabel1.toLowerCase())&&label11.equals("1"))
						flag11="*";//已经发生了改变,且是被动
					else if(!str11.equals(strlabel1.toLowerCase())&&label11.equals("0"))
						flag11="1";//已经发生了改变，是主动
					else		  
						flag11="0";//没有发生任何改变
					String replacelabel1=str11.replace("_", "").replace("-", "").replace(" ", "");
							
					String stemObject22[]=fliterThings(strlabel2);
					String str22=stemObject22[0].replace(" ", "");
					String label22=stemObject22[1];
					if(!str22.equals(strlabel2.toLowerCase())&&label22.equals("1"))
						flag22="*";//已经发生了改变,且是被动
					else if(!str22.equals(strlabel2.toLowerCase())&&label22.equals("0"))
						flag22="1";//已经发生了改变，是主动
					else		  
						flag22="0";//没有发生任何改变
					
					String replacelabel2=str22.replace("_", "").replace("-", "").replace(" ", "");
					
				
					int ld1 = similarityOfDistance(replacelabel1, str2);		
					double sim1=1 - (float) ld1 / Math.max(replacelabel1.length(), str2.length());
					
					sim=Math.max(sim, sim1);
					
					int ld2=similarityOfDistance(str1, replacelabel2);
					double sim2=1 - (float) ld2 / Math.max(str1.length(), replacelabel2.length());
					
					sim=Math.max(sim, sim2);
					
					int ld3=similarityOfDistance(replacelabel1, replacelabel2);
					double sim3=1 - (float) ld3 / Math.max(replacelabel1.length(), replacelabel2.length());
					
					sim=Math.max(sim, sim3);
					double finalsim=Math.max(sim3, Math.max(sim2, Math.max(sim, sim1)));
					if(finalsim==sim1)
						editSimProperty.add(object1s.get(i)+","+object2s.get(j)+","+sim1+","+flag11+","+flag2+","+str11+","+str2);
					else if(finalsim==sim2)
						editSimProperty.add(object1s.get(i)+","+object2s.get(j)+","+sim2+","+flag1+","+flag22+","+str1+","+str22);
					else if(finalsim==sim3)
						editSimProperty.add(object1s.get(i)+","+object2s.get(j)+","+sim3+","+flag11+","+flag22+","+str11+","+str22);
					else
						editSimProperty.add(object1s.get(i)+","+object2s.get(j)+","+sim+","+flag1+","+flag2+","+str1+","+str2);
				}
				else	//默认其他情况不满足	
					editSimProperty.add(object1s.get(i)+","+object2s.get(j)+","+sim+","+flag1+","+flag2+","+str1+","+str2);
				/*if(flag1==true&&flag2==true&&sim>=0.6)
				{
					System.out.println(object1s.get(i)+","+object2s.get(j)+","+sim);
				}	*/			
				flag2="0";
				flag11="0";
				flag22="0";
			}
			flag1="0";
		}		
		return editSimProperty;
	}
	
	
	public String[] fliterThings(String word)
	{
		boolean flag=false;
		boolean tense=false;//默认是主动
		String fliterWord[]={"",""};
		word=word.replaceAll("---", "-").replaceAll("--", "-");
		word=word.replaceAll("___", "_").replaceAll("__", "_");
		//System.out.println("************************");
		//System.out.println(word);
		String tokensWord=tokeningWord(word);
		String tokens[]=tokensWord.split(" ");
		String []fliterWords={"have","be","a","an","the","with","on","in","at","of","by","to","from","for","through"};
		for(int i=0;i<tokens.length;i++)
		{
			String token=findStem(tokens[i].toLowerCase());
			String pos=findPOS(tokens[i].toLowerCase());
			for(int j=0;j<fliterWords.length;j++)
			{
				flag=false;
				//tense=false;
			/*	if(token.equals("be"))//被动的情况,主动的情况默认不需要考虑
				{
					tense=true;
					flag=true;
					break;
				}*/
				if(pos.equals("VBN"))//被动的情况,主动的情况默认不需要考虑
				{
					tense=true;
					//flag=true;
					break;
				}
				else if(fliterWords[j].equals(token)&&token.equals("of"))//这里是针对名词的那种被动
				{
					tense=true;
					flag=true;
					break;
				}
				else if(fliterWords[j].equals(token))
				{
					flag=true;
					break;
				}
			}
			if(flag==false)
				fliterWord[0]=fliterWord[0]+token+" ";	
		}
		fliterWord[0]=fliterWord[0].replaceAll("\\.","");
		/*word=word.replaceAll("_", "").replaceAll("\\.","").replaceAll("-", "");
		word=findStem(word);//词干还原*/
		 if(tense==true)
		 {
			 fliterWord[0]=fliterWord[0].trim();
			 fliterWord[1]="1";//表示被动
			 return fliterWord;
		 }
		 else
		 {
			 fliterWord[0]=fliterWord[0].trim();
			 fliterWord[1]="0";//表示主动
			 return fliterWord;
		 }	 
	}
	
	public String fliter(String str)
	{
		String flitered="";
		boolean flag=false;
		String part[]=str.split(" ");
		String []fliterWords={"the","of","or"};
		for(int i=0;i<part.length;i++)
		{
			flag=false;
			//System.out.println(part[i]);
			for(int j=0;j<fliterWords.length;j++)
			{
				if(part[i].equals(fliterWords[j]))
				{
					flag=true;
					break;
				}
			}
			if(flag==true)
			{
				continue;
			}
			else
			{
				flitered=flitered+part[i]+" ";
			}
			
		}
		return flitered.trim();
	}
	
	
	public int similarityOfDistance(String str1, String str2) {
		int n = str1.length();
		int m = str2.length();
		char ch1, ch2; // str1
		int temp;
		if (n == 0) {
			return m; // 判断其中字符串1为空的情况
		}
		if (m == 0) // 判断其中字符串2为空的情况
		{
			return n;
		}
		// 定义一个(n+1)*(m+1)的矩阵来计算编辑距离
		int[][] d = new int[n + 1][m + 1];
		// 初始化矩阵
		for (int i = 0; i <= n; i++) {
			d[i][0] = i;
		}
		for (int j = 0; j <= m; j++) {
			d[0][j] = j;
		}
		// 按照计算字符串们的算法依次填充矩阵中的值
		for (int i = 1; i < n + 1; i++) {
			ch1 = str1.charAt(i - 1); // 提取字符串的1第i个字母,因为i初始值为1，效果是一样的
			for (int j = 1; j <= m; j++) {
				ch2 = str2.charAt(j - 1); // 提取字符串的1第i个字母,因为i初始值为1，效果是一样的
				if (ch1 == ch2) {
					temp = 0;
				} else {
					temp = 1;
				}
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
						+ temp);
			}
		}
		return d[n][m];
	}

	private int min(int one, int two, int three) // 计算三者之间的最小值
	{
		int min = one;
		if (two < min) {
			min = two;
		}
		if (three < min) {
			min = three;
		}
		return min;
	}

	/**
	 * This partition is used to calculate the Semantic similarity based on WordNet2.1
	 * @param object1s
	 * @param object2s
	 * @return semanticSim
	 */
	 
	public ArrayList<String> semanticSimClass(ArrayList<String> object1s, ArrayList<String> object2s) 
	{
		ArrayList<String> semanticSim=new ArrayList<String>();
		ArrayList<String> objectWeight1=wordWeight(object1s);
		ArrayList<String> objectWeight2=wordWeight(object2s);	
		TreeMap_Tools tokensWeight1=new TreeMap_Tools(objectWeight1);
		TreeMap_Tools tokensWeight2=new TreeMap_Tools(objectWeight2);
		for(int i=0;i<object1s.size();i++)
		{
			String concept1=tokeningWord(object1s.get(i));
			concept1=fliter(concept1);
			ArrayList<String> subTokenweight1=tokensWeight1.GetKey_Value(concept1);
			String[] strs1 = getTokens(subTokenweight1);
			double pairWeight1[]=getTokensWeights(subTokenweight1);
			for(int j=0;j<object2s.size();j++)
			{
				String concept2=tokeningWord(object2s.get(j));
				concept2=fliter(concept2);
				ArrayList<String> subTokenweight2=tokensWeight2.GetKey_Value(concept2);
				String[] strs2 = getTokens(subTokenweight2);				
				double pairWeight2[]=getTokensWeights(subTokenweight2);
				//double similarity=0;
				if(strs1.length>strs2.length)	//统一处理成行比列短的情况	        
		        {
					double similarity=getSemanticSim(strs2,strs1,pairWeight2,pairWeight1);
					/*double offset=((float)strs1.length-(float)strs2.length)/strs1.length;
					offset=1+Math.pow(offset, 2);
					similarity=similarity*offset;*/
					//System.out.println(concept1+","+concept2+": "+similarity);
					if(similarity>1)
						similarity=similarity-0.0000001;
					semanticSim.add(concept1+","+concept2+", "+similarity+","+strs1.length+","+strs2.length);
		        }
		        else 	//统一处理成行比列短的情况	        
		        {
		        	double similarity=getSemanticSim(strs1,strs2,pairWeight1,pairWeight2);
		       /* 	double offset=((float)strs2.length-(float)strs1.length)/strs2.length;
					offset=1+Math.pow(offset, 2);
					similarity=similarity*offset;*/
		        	//System.out.println(concept1+","+concept2+": "+similarity);
		        	if(similarity>1)
						similarity=similarity-0.0000001;
		        	semanticSim.add(concept1+","+concept2+", "+similarity+","+strs1.length+","+strs2.length);
		        }
			/*	double offset=(float)strs1.length-(float)strs2.length
				similarity=similarity**/
				//semanticSim.add(concept1+","+concept2+", "+similarity+","+strs1.length+","+strs2.length);
			}
		}	
		return semanticSim;
	}
	
	public ArrayList<String> NewsemanticSimClass(ArrayList<String> object1s, ArrayList<String> object2s) 
	{
		Refine_Tools tools=new Refine_Tools();
		ArrayList<String> semanticSim=new ArrayList<String>();
		double maxSim=0;
		int number=0;
		for(int i=0;i<object1s.size();i++)
		{			
			String concept1=tokeningWord(object1s.get(i));
			concept1=fliter(concept1);
			String tokens1[]=concept1.split(" ");
			for(int j=0;j<object2s.size();j++)
			{
				String concept2=tokeningWord(object2s.get(j));
				concept2=fliter(concept2);
				String tokens2[]=concept2.split(" ");
				if(concept1.equals(concept2))
				{
					semanticSim.add(object1s.get(i)+","+object2s.get(j)+","+1+","+tokens1.length+","+tokens2.length);
					//System.out.println(object1s.get(i)+","+object2s.get(j));
					number++;
				}
				else
				{
					ArrayList<String> sim=new ArrayList<String>();
					for(int m=0;m<tokens1.length;m++)
					{
						for(int n=0;n<tokens2.length;n++)
						{
							//if(tokens1[m].equals(tokens2[n]))
							double sc=0;
							//sc= maxScoreOfLin(tokens1[m],tokens2[n]);
							if(tokens1[m].equals(tokens2[n]))//除了名词和动词，有些WordNet中也没有.
							  sc=1.0;
							sim.add(tokens1[m]+","+tokens2[n]+","+sc);
						}
					}
					ArrayList<String> greedymaps=tools.keepOneToOneAlignment(sim);
					for(int num=0;num<greedymaps.size();num++)
					{
						String parts[]=greedymaps.get(num).split(",");
						double value=Double.parseDouble(parts[2]);
						if(Double.parseDouble(parts[2])>1)
							value=value-0.0000001;
						maxSim=maxSim+value;
					}
					maxSim=maxSim/Math.max(tokens1.length, tokens2.length);//仍然有些粗糙
					if(maxSim>0)
						number++;
					semanticSim.add(object1s.get(i)+","+object2s.get(j)+","+maxSim+","+tokens1.length+","+tokens2.length);
				}
				maxSim=0;
			}
		}		
		System.out.println("语义相似度不为0的个数为："+number);
		return semanticSim;
		
		/*
		ArrayList<String> semanticSim=new ArrayList<String>();
		int num=0;
		for(int i=0;i<object1s.size();i++)
		{
			String str1=tokeningWord(object1s.get(i));
			for(int j=0;j<object2s.size();j++)
			{
				String str2=tokeningWord(object2s.get(j));
				if(str1.equals(str2))
				{
					semanticSim.add(object1s.get(i)+","+object2s.get(j)+","+1);
					System.out.println(object1s.get(i)+","+object2s.get(j));
					num++;
				}
				else
					semanticSim.add(object1s.get(i)+","+object2s.get(j)+","+0);
			}
		}
		System.out.println("语义相似度能找到的匹配对为"+num);
		
		return semanticSim;*/
	}
	
	public ArrayList<String> NewsemanticSimClass2(ArrayList<String> object1s, ArrayList<String> object2s) 
	{
		Refine_Tools tools=new Refine_Tools();
		ArrayList<String> semanticSim=new ArrayList<String>();
		double maxSim=0;
		int number=0;
		for(int i=0;i<object1s.size();i++)
		{			
			String concept1=tokeningWord(object1s.get(i));
			concept1=fliter(concept1);
			String subTokenweight1=wordWeight(concept1);
			//String[] strs1 = getTokens(subTokenweight1);
			double pairWeight1[]=getTokensWeights(subTokenweight1);
			String tokens1[]=concept1.split(" ");
			for(int j=0;j<object2s.size();j++)
			{
				String concept2=tokeningWord(object2s.get(j));
				concept2=fliter(concept2);
				String subTokenweight2=wordWeight(concept2);
				//String[] strs2 = getTokens(subTokenweight2);
				double pairWeight2[]=getTokensWeights(subTokenweight2);						
				String tokens2[]=concept2.split(" ");
				if(concept1.equals(concept2))
				{
					semanticSim.add(object1s.get(i)+","+object2s.get(j)+","+1+","+tokens1.length+","+tokens2.length);
					//System.out.println(object1s.get(i)+","+object2s.get(j));
					number++;
				}
				else
				{
					ArrayList<String> sim=new ArrayList<String>();
					for(int m=0;m<tokens1.length;m++)
					{
						for(int n=0;n<tokens2.length;n++)
						{
							double sc=0;							
							if(tokens1[m].equals(tokens2[n]))//除了名词和动词，有些WordNet中也没有.
								sc=Math.min(pairWeight1[m],pairWeight2[n]);		
						/*	else
								sc=Math.min(pairWeight1[m],pairWeight2[n])*maxScoreOfLin(tokens1[m],tokens2[n]);	*/
							if(sc==0)  //能缓解部分词义相似度为0造成的影响			
								sc=Math.min(pairWeight1[m],pairWeight2[n])*ISUB(tokens1[m],tokens2[n]);					
							sim.add(tokens1[m]+","+tokens2[n]+","+sc);
						}
					}
					ArrayList<String> greedymaps=tools.keepOneToOneAlignment(sim);
					for(int num=0;num<greedymaps.size();num++)
					{
						String parts[]=greedymaps.get(num).split(",");
						double value=Double.parseDouble(parts[2]);
						if(Double.parseDouble(parts[2])>1)
							value=value-0.0000001;
						maxSim=maxSim+value;
					}
					//maxSim=maxSim/Math.max(tokens1.length, tokens2.length);//仍然有些粗糙
					if(maxSim>0)
						number++;
					semanticSim.add(object1s.get(i)+","+object2s.get(j)+","+maxSim+","+tokens1.length+","+tokens2.length);
				}
				maxSim=0;
			}
		}		
		System.out.println("语义相似度不为0的个数为："+number);
		return semanticSim;
	}
	
	public ArrayList<String> NewsemanticSimClass3(ArrayList<String> object1s, ArrayList<String> object2s) //只是为了测试
	{
		Refine_Tools tools=new Refine_Tools();
		Spt s=new Spt();
		String inFile = "dic/normTermSynonyms.data.2015";
		ArrayList<String> semanticSim=new ArrayList<String>();
		double maxSim=0;
		int number=0;
		HashSet<String> synonyms1=new HashSet<String>();
		for(int i=0;i<object1s.size();i++)
		{	
			//String concept1="limb";
			String concept1=tokeningWord(object1s.get(i));
			concept1=fliter(concept1);
			String subTokenweight1=wordWeight(concept1);
			//String[] strs1 = getTokens(subTokenweight1);
			double pairWeight1[]=getTokensWeights(subTokenweight1);
			String tokens1[]=concept1.split(" ");
			synonyms1 = s.GetSynonymPermutationRecursive(inFile, concept1, true, false, 1);
		    System.out.println(concept1+" is finished, and the No is "+i);	
			for(int j=0;j<object2s.size();j++)
			{
				String concept2=tokeningWord(object2s.get(j));
				concept2=fliter(concept2);
				String subTokenweight2=wordWeight(concept2);
				//String[] strs2 = getTokens(subTokenweight2);
				double pairWeight2[]=getTokensWeights(subTokenweight2);						
				String tokens2[]=concept2.split(" ");
				if(concept1.equals(concept2))  //直接判断是否等价
				{
					semanticSim.add(object1s.get(i)+","+object2s.get(j)+","+1+","+tokens1.length+","+tokens2.length);
					//System.out.println(object1s.get(i)+","+object2s.get(j));
					number++;
				}
				else if(synonyms1.contains(concept2)) //判断语义是否等价
				{
					semanticSim.add(object1s.get(i)+","+object2s.get(j)+","+1+","+tokens1.length+","+tokens2.length);
					//System.out.println(object1s.get(i)+","+object2s.get(j));
					number++;
				}
				else
				{
					semanticSim.add(object1s.get(i)+","+object2s.get(j)+","+0+","+tokens1.length+","+tokens2.length);
				}	
			}
		}		
		System.out.println("语义相似度为1的个数为："+number);
		return semanticSim;
	}
	
	public ArrayList<String> NewsemanticSimClass4(ArrayList<String> object1s, ArrayList<String> object2s) 
	{
		Refine_Tools tools=new Refine_Tools();
		ArrayList<String> semanticSim=new ArrayList<String>();
		double maxSim=0;
		int number=0;	
		Spt s=new Spt();
		String inFile = "dic/normTermSynonyms.data.2015";
		HashSet<String> synonyms1=new HashSet<String>();
		for(int i=0;i<object1s.size();i++)
		{	
			//String concept1="limb";
			String concept1=tokeningWord(object1s.get(i));
			concept1=fliter(concept1);
			String subTokenweight1=wordWeight(concept1);
			//String[] strs1 = getTokens(subTokenweight1);
			double pairWeight1[]=getTokensWeights(subTokenweight1);
			String tokens1[]=concept1.split(" ");
			synonyms1 = s.GetSynonymPermutationRecursive(inFile, concept1, true, false, 1);
			System.out.println(concept1 +" is finished!" );
			for(int j=0;j<object2s.size();j++)
			{
				String concept2=tokeningWord(object2s.get(j));
				concept2=fliter(concept2);
				String subTokenweight2=wordWeight(concept2);
				//String[] strs2 = getTokens(subTokenweight2);
				double pairWeight2[]=getTokensWeights(subTokenweight2);						
				String tokens2[]=concept2.split(" ");
				if(concept1.equals(concept2))  //直接判断是否等价
				{
					semanticSim.add(object1s.get(i)+","+object2s.get(j)+","+1+","+tokens1.length+","+tokens2.length);
					//System.out.println(object1s.get(i)+","+object2s.get(j));
					number++;
				}
				else if(synonyms1.contains(concept2))
				{
					semanticSim.add(object1s.get(i)+","+object2s.get(j)+","+0.9+","+tokens1.length+","+tokens2.length);
					//System.out.println(object1s.get(i)+","+object2s.get(j));
					number++;
				}
				else
				{
					ArrayList<String> sim=new ArrayList<String>();
					for(int m=0;m<tokens1.length;m++)
					{
						for(int n=0;n<tokens2.length;n++)
						{
							double sc=0;							
							if(tokens1[m].equals(tokens2[n]))//除了名词和动词，有些WordNet中也没有.
								sc=Math.min(pairWeight1[m],pairWeight2[n]);	
							/*	else
							sc=Math.min(pairWeight1[m],pairWeight2[n])*maxScoreOfLin(tokens1[m],tokens2[n]);	*/
							/*else if(isSynonym(tokens1[m],tokens2[n])) //因为同义词并不像wordnet计算相似度。一般来说，它具有很高的可信度。
								sc=Math.min(pairWeight1[m],pairWeight2[n])*0.9;	*/
							if(sc==0)  //能缓解部分词义相似度为0造成的影响			
								sc=Math.min(pairWeight1[m],pairWeight2[n])*ISUB(tokens1[m],tokens2[n]);					
							sim.add(tokens1[m]+","+tokens2[n]+","+sc);
						}
					}
					ArrayList<String> greedymaps=tools.keepOneToOneAlignment(sim);
					for(int num=0;num<greedymaps.size();num++)
					{
						String parts[]=greedymaps.get(num).split(",");
						double value=Double.parseDouble(parts[2]);
						if(Double.parseDouble(parts[2])>1)
							value=1;
							//value=value-0.0000001;
						maxSim=maxSim+value;
					}
					//maxSim=maxSim/Math.max(tokens1.length, tokens2.length);//仍然有些粗糙
					if(maxSim>0)
						number++;
					semanticSim.add(object1s.get(i)+","+object2s.get(j)+","+maxSim+","+tokens1.length+","+tokens2.length);
				}
				maxSim=0;
			}
		}		
		System.out.println("语义相似度为1的个数为："+number);
		return semanticSim;
	}
	
	public boolean isSynonym(String st1, String st2)
	{
		Spt s=new Spt();
		String inFile = "dic/normTermSynonyms.data.2015";
		HashSet<String>  synonyms= s.GetSynonymPermutationRecursive(inFile, st1, true, false, 1);
		if(synonyms.contains(st2))
			return true;
		return false;
	}
	
    public  double ISUB(String st1, String st2)  //基于字符串的相似度计算，考虑了部分形变的情况
    {
    	String s1 = st1;
    	String s2 = st2;
    	int l1 = s1.length(), l2 = s2.length();
    	int L1 = l1, L2 = l2;
    	if ((L1 == 0) && (L2 == 0)) {
    		return 0;
    	}
    	if ((L1 == 0) || (L2 == 0)) {
    		return 1;
    	}
    	double common = 0;
    	int best = 2;
    	while (s1.length() > 0 && s2.length() > 0 && best != 0) {
    		best = 0;
    		l1 = s1.length();
    		l2 = s2.length();
    		int i = 0, j = 0;
    		int startS1 = 0, endS1 = 0;
    		int startS2 = 0, endS2 = 0;
    		int p = 0;
    		for (i = 0; (i < l1) && (l1 - i > best); i++) {
    			j = 0;
    			while (l2 - j > best) {
    				int k = i;
    				for (; (j < l2) && (s1.charAt(k) != s2.charAt(j)); j++) {
    				}
    				if (j != l2) {
    					p = j;
    					for (j++, k ++; (j < l2) && (k < l1)
    							&& (s1.charAt(k) == s2.charAt(j)); j++, k++) {
    					}
    					if (k - i > best) {
    						best = k - i;
    						startS1 = i;
    						endS1 = k;
    						startS2 = p;
    						endS2 = j;
    					}
    				}
    			}
    		}
    		char[] newString = new char[s1.length() - (endS1 - startS1)];
    		j = 0;
    		for (i = 0; i < s1.length(); i++) {
    			if (i >= startS1 && i < endS1) {
    				continue;
    			}
    			newString[j++] = s1.charAt(i);
    		}
    		s1 = new String(newString);
    		newString = new char[s2.length() - (endS2 - startS2)];
    		j = 0;
    		for (i = 0; i < s2.length(); i++) {
    			if (i >= startS2 && i < endS2) {
    				continue;
    			}
    			newString[j++] = s2.charAt(i);
    		}
    		s2 = new String(newString);
    		if (best > 2) {
    			common += best;
    		} else {
    			best = 0;
    		}
    	}
    	double commonality = 0;
    	double scaledCommon = (double) (2 * common) / (L1 + L2);
    	commonality = scaledCommon;
    	double winklerImprovement = winklerImprovement(st1, st2, commonality);
    	double dissimilarity = 0;
    	double rest1 = L1 - common;
    	double rest2 = L2 - common;
    	double unmatchedS1 = Math.max(rest1, 0);
    	double unmatchedS2 = Math.max(rest2, 0);
    	unmatchedS1 = rest1 / L1;
    	unmatchedS2 = rest2 / L2;
    	double suma = unmatchedS1 + unmatchedS2;
    	double product = unmatchedS1 * unmatchedS2;
    	double p = 0.6;
    	if ((suma - product) == 0) {
    		dissimilarity = 0;
    	} else {
    		dissimilarity = (product) / (p + (1 - p) * (suma - product));
    	}
    	//  System.out.println(commonality);
    	//  System.out.println(dissimilarity);
    	//  System.out.println(winklerImprovement);
    	if(commonality - dissimilarity + winklerImprovement>0)
    		return commonality - dissimilarity + winklerImprovement;
    	else
    		return 0;
    }

    private  double winklerImprovement(String s1, String s2, double commonality)
    {
    	int i, n = Math.min(s1.length(), s2.length());
    	for (i = 0; i < n; i++) {
    		if (s1.charAt(i) != s2.charAt(i)) {
    			break;
    		}
    	}
    	double commonPrefixLength = Math.min(4, i);
    	double winkler = commonPrefixLength * 0.1 * (1 - commonality);
    	return winkler;
    }
	
	public ArrayList<String> briefLabel(ArrayList<String> labels)
	{
		 ArrayList<String> briefLabels=new  ArrayList<String>();
		 for(int i=0;i<labels.size();i++)
		 {
			 String parts[]=labels.get(i).split("--");
			 int length=parts[1].split(" ").length;
			 if(length==1)
				 briefLabels.add(labels.get(i));
			 else
			 {
				 String comment=parts[1];
				 String words[]=comment.replace(".", "").replace("/", " ").replace("'", "").split(" ");	
				 String briefWord="";
				//System.out.println(words);
				boolean flag=false;
				for(int k=0;k<words.length;k++)
				{
					if(findPOS(words[k]).equals("NN")||findPOS(words[k]).equals("NNS"))
					{
						briefWord=briefWord+words[k]+" ";
						flag=true;
					}
				    //System.out.print(findPOS(words[i])+" "); 
				    
				   if(flag==true&&!findPOS(words[k]).equals("NNS")&&!findPOS(words[k]).equals("NN"))
					   break;
				}
				if(!briefWord.equals(""))
					briefLabels.add(parts[0]+"--"+briefWord);
				else
					briefLabels.add(parts[0]+"--"+parts[1]);
			 }		 
		 }		
		 return briefLabels;
	}

	public ArrayList<String> replaceLabel(ArrayList<String> object,ArrayList<String> label)
	{
		ArrayList<String> newObject=new ArrayList<String>();
		if(label==null)
			return object;
		else
		{
			//转换成HashMap来检索
			HashMap<String,String> classLabel=new HashMap<String,String>();
			for(int i=0;i<label.size();i++)
			{
				String part[]=label.get(i).split("--");
				classLabel.put(part[0], part[1]);
			}
			//将对应的进行替换
			for(int i=0;i<object.size();i++)
			{
				String replaceObject=classLabel.get(object.get(i));
				if(replaceObject==null)
					newObject.add(object.get(i));
				else
					newObject.add(replaceObject);
			}
			return newObject;
		}
	}
	
	public ArrayList<String> keepLabel(ArrayList<String> ClassLabel)
	{
		ArrayList<String> label=new ArrayList<String>();
		for(int i=0;i<ClassLabel.size();i++)
		{
			String part[]=ClassLabel.get(i).split("--");
			label.add(part[1]);//只将label保留
		}
		return label;
		
	}
	
	public HashMap<String,String> transformToHashMap(ArrayList<String> originalMap)
	{
		HashMap<String,String> standardMap=new HashMap<String,String>();
		for(int i=0;i<originalMap.size();i++)
		{
			String part[]=originalMap.get(i).split("--");
			standardMap.put(part[0].toLowerCase(),part[1].toLowerCase());
		}
		return standardMap;
	}
	
	public HashMap<String,String> changetoHashMap(ArrayList<String> ClassLabel)
	{
		HashMap<String,String> classLabelMap=new HashMap<String,String>();
		for(int i=0;i<ClassLabel.size();i++)
		{
			String part[]=ClassLabel.get(i).split("--");
			classLabelMap.put(part[0], part[1]);
		}
		return classLabelMap;
	}
	
	public ArrayList<String> semanticSimProperty(ArrayList<String> object1s, ArrayList<String> object2s) 
	{
		Refine_Tools tools=new Refine_Tools();
		ArrayList<String> semanticSimProperty=new ArrayList<String>();
		double maxSim=0;
		for(int i=0;i<object1s.size();i++)
		{
			String concept1=object1s.get(i);
			//String concept1="writes";
			String str1=fliterThings(concept1)[0];
			String tokens1[]=str1.split(" ");
			for(int j=0;j<object2s.size();j++)
			{
				String concept2=object2s.get(j);
				//String concept2="isWrittenBy";
				String str2=fliterThings(concept2)[0];
				String tokens2[]=str2.split(" ");
				ArrayList<String> sim=new ArrayList<String>();
				for(int m=0;m<tokens1.length;m++)
				{
					for(int n=0;n<tokens2.length;n++)
					{
						double sc= maxScoreOfLin(tokens1[m],tokens2[n]);
						/*if(tokens1[m].equals(tokens2[n]))//除了名词和动词，有些WordNet中也没有.
							sc=1.0;*/
						sim.add(tokens1[m]+","+tokens2[n]+","+sc);
					}
				}
				ArrayList<String> greedymaps=tools.keepOneToOneAlignment(sim);
				for(int num=0;num<greedymaps.size();num++)
				{
					String parts[]=greedymaps.get(num).split(",");
					double value=Double.parseDouble(parts[2]);
					if(Double.parseDouble(parts[2])>1)
						value=value-0.0000001;
					maxSim=maxSim+value;
				}
				maxSim=maxSim/Math.max(tokens1.length, tokens2.length);//仍然有些粗糙
				/*for(String s1 : tokens1)
				{
		            for(String s2: tokens2)
		            {
		                double sc= maxScoreOfLin(s1,s2);
		                sum+= sc;
		            }
				}*/
				//maxSim = sum /(tokens1.length * tokens2.length);
				if(maxSim>0.8)
				{
					System.out.println(object1s.get(i)+","+object2s.get(j)+","+maxSim);
				}
				semanticSimProperty.add(object1s.get(i)+","+object2s.get(j)+","+maxSim);
				maxSim=0;
			}
		}		
		return semanticSimProperty;
	}
	
	
	/*public double getSemanticSim(String []strs1,String []strs2,double weight1[],double weight2[])
	{

		double main_similarity=0;
		double max1=0;
		int index1=0;
		for(int m=0;m<weight1.length;m++)//找到长度较小的字符串对应的最大权值和下标
		{
			if(weight1[m]>max1)
			{
				max1=weight1[m];
				index1=m;
			}
		}
		double max2=0;
		int index2=0;
		for(int n=0;n<weight2.length;n++)//找到长度较小的字符串对应的最大权值和下标
		{
			if(weight2[n]>max2)
			{
				max2=weight2[n];
				index2=n;
			}
		}
		//计算核心词的相似度
		double wordnet_similarity=maxScoreOfLin(strs1[index1], strs2[index2]);
		main_similarity=Math.min(weight1[index1],weight2[index2])*wordnet_similarity;//感觉取最小值更合适一些
		if(strs1.length==1||strs2.length==1)//长度为1的话直接就输出核心词的语义相似度即可
		{
			return main_similarity;
		}
		else //如果长度不为1，则将核心词的相似度与辅助词的相似度进行相加
		{
			//将tokens和weight进行约简
			String new_strs1[]=new String[strs1.length-1];
			double new_weight1[]=new double[weight1.length-1];
			int k1=0;
			for(int i=0;i<strs1.length;i++)
			{
				if(i==index1)
					continue;
				new_strs1[k1]=strs1[i];
				new_weight1[k1]=weight1[i];
				k1++;
			}
			int k2=0;
			String new_strs2[]=new String[strs2.length-1];
			double new_weight2[]=new double[weight2.length-1];
			for(int i=0;i<strs2.length;i++)
			{
				if(i==index2)
					continue;
				new_strs2[k2]=strs2[i];
				new_weight2[k2]=weight2[i];
				k2++;
			}
			double left_similarity=findSemanticSim(new_strs1,new_strs2,new_weight1,new_weight2);
			double final_similarity=main_similarity+left_similarity;
			return final_similarity;
		}
	}*/ 
	
	public double getSemanticSim(String []strs1,String []strs2,double weight1[],double weight2[])
	{

		double main_similarity=0;
		//double max1=0;
		int index1=strs1.length-1;
		/*for(int m=0;m<weight1.length;m++)//找到长度较小的字符串对应的最大权值和下标
		{
			if(weight1[m]>max1)
			{
				max1=weight1[m];
				index1=m;
			}
		}*/
		//double max2=0;
		int index2=strs2.length-1;
		/*for(int n=0;n<weight2.length;n++)//找到长度较小的字符串对应的最大权值和下标
		{
			if(weight2[n]>max2)
			{
				max2=weight2[n];
				index2=n;
			}
		}*/
		//计算核心词的相似度
		double wordnet_similarity=maxScoreOfLin(strs1[index1], strs2[index2]);
		main_similarity=Math.min(weight1[index1],weight2[index2])*wordnet_similarity;//感觉取最小值更合适一些
		if(strs1.length==1||strs2.length==1)//长度为1的话直接就输出核心词的语义相似度即可
		{
			return main_similarity;
		}
		else //如果长度不为1，则将核心词的相似度与辅助词的相似度进行相加
		{
			//将tokens和weight进行约简
			String new_strs1[]=new String[strs1.length-1];
			double new_weight1[]=new double[weight1.length-1];
			int k1=0;
			for(int i=0;i<strs1.length-1;i++)
			{
				if(i==index1)
					continue;
				new_strs1[k1]=strs1[i];
				new_weight1[k1]=weight1[i];
				k1++;
			}
			int k2=0;
			String new_strs2[]=new String[strs2.length-1];
			double new_weight2[]=new double[weight2.length-1];
			for(int i=0;i<strs2.length-1;i++)
			{
				if(i==index2)
					continue;
				new_strs2[k2]=strs2[i];
				new_weight2[k2]=weight2[i];
				k2++;
			}
			double left_similarity=findSemanticSim(new_strs1,new_strs2,new_weight1,new_weight2);
			double final_similarity=main_similarity+left_similarity;
			/*double maxSim=0;
			ArrayList<String> sim=new ArrayList<String>();
			for(int m=0;m<strs1.length-1;m++)
			{
				for(int n=0;n<strs2.length-1;n++)
				{
					double sc= maxScoreOfLin(strs1[m],strs2[n])*Math.min(weight1[m], weight2[n]);
					if(strs1[m].equals(strs2[n]))//除了名词和动词，有些WordNet中也没有.
						sc=1.0*Math.min(weight1[m], weight2[n]);
					sim.add(strs1[m]+","+strs2[n]+","+sc);
				}
			}
			Refine_Tools tools=new Refine_Tools();
			ArrayList<String> greedymaps=tools.keepOneToOneAlignment(sim);
			for(int num=0;num<greedymaps.size();num++)
			{
				String parts[]=greedymaps.get(num).split(",");
				double value=Double.parseDouble(parts[2]);
				if(Double.parseDouble(parts[2])>1)
					value=value-0.0000001;
				maxSim=maxSim+value;
			}
			double final_similarity=main_similarity+maxSim;*/
			return final_similarity;
		}
	} 
		
	public double getSemanticSim2(String []strs1,String []strs2,double weight1[],double weight2[])
	{

		double main_similarity=0;
		//double max1=weight1[weight1.length-1];
		int index1=weight1.length-1;
		/*for(int m=0;m<weight1.length;m++)//找到长度较小的字符串对应的最大权值和下标
		{
			if(weight1[m]>max1)
			{
				max1=weight1[m];
				index1=m;
			}
		}*/
		//double max2=weight2[weight2.length-1];;
		int index2=weight2.length-1;
		/*for(int n=0;n<weight2.length;n++)//找到长度较小的字符串对应的最大权值和下标
		{
			if(weight2[n]>max2)
			{
				max2=weight2[n];
				index2=n;
			}
		}*/
		//计算核心词的相似度
		double wordnet_similarity=maxScoreOfLin(strs1[index1], strs2[index2]);
		main_similarity=Math.min(weight1[index1],weight2[index2])*wordnet_similarity;//感觉取最小值更合适一些
		if(strs1.length==1||strs2.length==1)//长度为1的话直接就输出核心词的语义相似度即可
		{
			return main_similarity;
		}
		else //如果长度不为1，则将核心词的相似度与辅助词的相似度进行相加
		{
			//将tokens和weight进行约简
			String new_strs1[]=new String[strs1.length-1];
			double new_weight1[]=new double[weight1.length-1];
			int k1=0;
			for(int i=0;i<strs1.length;i++)
			{
				if(i==index1)
					continue;
				new_strs1[k1]=strs1[i];
				new_weight1[k1]=weight1[i];
				k1++;
			}
			int k2=0;
			String new_strs2[]=new String[strs2.length-1];
			double new_weight2[]=new double[weight2.length-1];
			for(int i=0;i<strs2.length;i++)
			{
				if(i==index2)
					continue;
				new_strs2[k2]=strs2[i];
				new_weight2[k2]=weight2[i];
				k2++;
			}
			double left_similarity=getSemanticSim2(new_strs1,new_strs2,new_weight1,new_weight2);
			//double left_similarity=findSemanticSim(new_strs1,new_strs2,new_weight1,new_weight2);
			double final_similarity=main_similarity+left_similarity;
			return final_similarity;
		} 

	}

	public double findSemanticSim(String []strs1,String []strs2,double weight1[],double weight2[])
	{
		//基于wordnet计算相似度
		double similarity[][] = new double[strs1.length][strs2.length];
		//计算每个词中tokens归一化的权值
		for (int j = 0; j < strs1.length; j++) 
		{
			String s1 = strs1[j];
			for (int i = 0; i < strs2.length; i++) 
			{
				String s2 = strs2[i];
				similarity[j][i] = maxScoreOfLin(s1, s2);
			}
		}
		/*ArrayList<String> greedymaps=tools.keepOneToOneAlignment(sim);
		for(int num=0;num<greedymaps.size();num++)
		{
			String parts[]=greedymaps.get(num).split(",");
			double value=Double.parseDouble(parts[2]);
			if(Double.parseDouble(parts[2])>1)
				value=value-0.0000001;
			maxSim=maxSim+value;
		}*/
		
		double sim=-0.1;
		double sum=0;   	
		int pair[]=new int[strs1.length];//在短的tokens中每一行找到一个对应的标签号,这些标签号是不重复的
		for(int i=0;i<similarity[0].length;i++)//这里采用的是遍历的方法(或许以后要用到动态规划) 读取列
		{  
			//预先定义一个链表，并且存储第一行某列的标签与对应的相似度
			ArrayList<Integer> labeled_row=new ArrayList<Integer>();
			labeled_row.add(i);			
			//这里即使出现了助词，它与其他词基于wordnet的相似度为0,则不需要考虑,这里是基于TFIDF来考虑
			/*sum=similarity[0][i]*weight1[0]*weight2[i];//weight1对应短的字符串里的tokens,weight2对应长的字符串里的tokens
	    		for(int k=1;k<similarity.length;k++)//因为第一行与第一列已经间接遍历过，所以从第二行开始继续遍历
	    		{
	    			int label=FindsuitLabel(similarity[k],labeled_row);//这里找的基于语义上最匹配的对
	    			labeled_row.add(label);  
	    			sum=sum+similarity[k][label]*weight1[k]*weight2[label];
	    		}	*/
			//这里即使出现了助词，它与其他词基于wordnet的相似度为0,则不需要考虑,这里是基于依存关系来考虑
			sum=similarity[0][i]*Math.min(weight1[0],weight2[i]);//weight1对应短的字符串里的tokens,weight2对应长的字符串里的tokens
			for(int k=1;k<similarity.length;k++)//因为第一行与第一列已经间接遍历过，所以从第二行开始继续遍历
			{
				int label=findSuitLabel(similarity[k],labeled_row);//这里找的基于语义上最匹配的对
				labeled_row.add(label);  
				sum=sum+similarity[k][label]*Math.min(weight1[k],weight2[label]);
			}
			//Print(similarity,strs1,strs2,labeled_row,i,sum);
			if(sim<sum)
			{
				sim=sum; //获取相似度最大的情况   		
				for(int L=0;L<labeled_row.size();L++)
					pair[L]=labeled_row.get(L); //这里的pair会不断更新的
			}
		}
		double final_similarity=sim;
		/*double rate=GetMappingRate(weight1,weight2,pair); //这里纯粹将各个词的TFIDF作为一个比例
	    	double final_similarity=GetSimilarity(similarity, pair)/strs1.length*rate;*/

		//System.out.println("最佳的遍历顺序为:");   		
		//打印输出
		/*for(int m=0;m<similarity.length;m++)
			{
				System.out.println(strs1[m]+"<->"+strs2[pair[m]]);
			}   */ 
		//System.out.println();
		return final_similarity;
	}


	private double maxScoreOfLin(String str1,String str2){
		Lin lin = ws.getLin();    
		double sc = lin.max(str1, str2, "n");
		if(sc==0){
			sc = lin.max(str1, str2, "v");
		}
		if(str1.equals(str2))
			sc=1.0;
		else if(!str1.equals(str2)&&sc==1)
			sc=Math.max(sc-0.1, 0);
		return sc;
	}

	public int findSuitLabel(double []row,ArrayList<Integer> label)//通过已标记的标签来找每一行的最大值
	{
		int suit_label=0;
		double max_value=-0.1;
		for(int i=0;i<row.length;i++)
		{
			if(max_value<row[i]&&!label.contains(i))
			{
				max_value=row[i];
				suit_label=i;
			}
		}
		return suit_label;//其实找到的对应的标签，也就知道其最大值是多少。因为下标代表行数。
	}
	
	public String[] getTokens(ArrayList<String> tokens_value)
    {
    	String tokens[]=new String[tokens_value.size()];
    	for(int i=0;i<tokens_value.size();i++)
    	{
    		String concept_value[]=tokens_value.get(i).split("#");
    		tokens[i]=concept_value[0];//将值进行返回
    	}  	
    	return tokens;
    }
	
	public String[] getTokens(String tokens_value)
    {
		String tokens[]=tokens_value.split(",");
    	//String tokens[]=new String[tokens_value.size()];
    	for(int i=0;i<tokens.length;i++)
    	{
    		String concept_value[]=tokens[i].split("#");
    		tokens[i]=concept_value[0];//将值进行返回
    	}  	
    	return tokens;
    }
	
	public double[] getTokensWeights(ArrayList<String> tokens_value)
	{
		double weight[]=new double[tokens_value.size()];
		for(int i=0;i<tokens_value.size();i++)
		{
			String concept_value[]=tokens_value.get(i).split("#");
			weight[i]=Double.parseDouble(concept_value[1]);//将值进行返回
		}
		return weight;
	}
	
	public double[] getTokensWeights(String tokens_value)
	{
		String tokens[]=tokens_value.split(",");
		double weight[]=new double[tokens.length];
		for(int i=0;i<tokens.length;i++)
		{
			String concept_value[]=tokens[i].split("#");
			weight[i]=Double.parseDouble(concept_value[1]);//将值进行返回
		}
		return weight;
	}
	
	public  ArrayList<String> wordWeight(ArrayList<String> Classes) 
	  {
		  ArrayList<String> word=new ArrayList<String>();	
		  for(int i=0;i<Classes.size();i++)
		  {
			  String concept=tokeningWord(Classes.get(i));
			  concept=fliter(concept);
			 // String sent2 = "invited Speaker";
			  String tokens[]=concept.split(" ");
			  //默认认为keywords是概念的最后一个单词
			  String keyword=tokens[tokens.length-1];
			  double value=0;
			  String weighted_tokens="";
			  //如果考虑将词干融入进来就方便许多了
			  double n=tokens.length;//定义字符串长度
			  for(int k=0;k<tokens.length;k++)
			  {
				  if(tokens[k].equals(keyword))		 //keyword的赋值
				  {
					  value=1-Math.pow((n-1)/(n), 2);
					  weighted_tokens=weighted_tokens+findStem(tokens[k])+"#"+value+",";
				  }
				  else
				  {
					  value=(Math.pow((n-1)/(n), 2))/(n-1);  //一般组合词的个数只有2到3个
					  weighted_tokens=weighted_tokens+findStem(tokens[k])+"#"+value+",";
				  }
			  }
			  weighted_tokens=weighted_tokens+".";
			  weighted_tokens=weighted_tokens.replace(",.", "");
			  word.add(concept+"--"+weighted_tokens);				  	  	
		  }
		  return word;
	  }
	
	
	public String wordWeight(String word) 
	{	  
		/*String concept=tokeningWord(word);
			  concept=fliter(concept);*/
		String tokens[]=word.split(" ");//因为已经切词了
		//默认认为keywords是概念的最后一个单词
		String keyword=tokens[tokens.length-1];
		double value=0;
		String weighted_tokens="";
		//如果考虑将词干融入进来就方便许多了
		double n=tokens.length;//定义字符串长度
		for(int k=0;k<tokens.length;k++)
		{
			if(tokens[k].equals(keyword))		 //keyword的赋值
			{
				value=1-Math.pow((n-1)/n, 2);
				weighted_tokens=weighted_tokens+findStem(tokens[k])+"#"+value+",";
			}
			else
			{
				value=(Math.pow((n-1)/n, 2))/(n-1);  //一般组合词的个数只有2到3个
				weighted_tokens=weighted_tokens+findStem(tokens[k])+"#"+value+",";
			}
		}
		weighted_tokens=weighted_tokens+".";
		weighted_tokens=weighted_tokens.replace(",.", "");
		//String weightedWord=concept+"--"+weighted_tokens;
		return weighted_tokens;
	}
	  
	  public String findStem(String token)   // 词干还原
	  { 
		  //String tokens1="science";
		  //String tokens2="scientific";
		/*  Word s=new Word(); 	
		  s.setWord(token); 	
		  //System.out.println(s.value());   	
		  Morphology method=new Morphology();
		  Word k=method.stem(s);
		  //method.
		  String stem=k.value();*/
		  
		  
		  String stem=method.stem(token);
		  
		  return stem;
	  }
	  
	  public String findPOS(String token) 
	  {	  
		  String POS=tagger.tagString(token).replace(token+"/", "");//返回是written/VBN 
		  return POS.trim();
	  }
	  
	  /**
	   * 
	   * @param strs1
	   * @param strs2
	   * @param weight1
	   * @param weight2
	   * @return
	   */
	  public ArrayList<String> tfidfSim(ArrayList<String> Classes1, ArrayList<String> Classes2)
	  {
		  ArrayList<String> tfidfSimiliary=new ArrayList<String>();
		  ArrayList<String> Tokens1=new ArrayList<String>();
			//将本体1中的概念基于'_'进行切割。
			for(int i=0;i<Classes1.size();i++)
			{
				String concept1=Classes1.get(i);
				String []concept_token=tokeningWord(concept1).split(" ");
				//String []concept_token=concept1.split("_");
				for(int k=0;k<concept_token.length;k++)
				{
					Tokens1.add(concept_token[k]);
				}
			}
			
			ArrayList<String> Concept_tokens1=new ArrayList<String>();
			ArrayList<String> TF_IDF1=new ArrayList<String>();
			for(int i=0;i<Classes1.size();i++)
			{
				String concept1=Classes1.get(i);
				String []concept_token=tokeningWord(concept1).split(" ");
				//String []concept_token=concept1.split("_");
				//int tf[]=new int[concept_token.length];////tf其实没必要，如果将一个概念当做一个网页的话。
				double concept_token_idf[]=new double[concept_token.length];
				for(int j=0;j<concept_token.length;j++)
				{						
					for(int k=0;k<Tokens1.size();k++)
					{
						if(Tokens1.get(k).equals(concept_token[j]))//IDF 中的Dw加1
						{
							concept_token_idf[j]++;
						}
					}			
				}		
				//计算概念中每个tokens的权重
				double concept_token_weight[]=new double[concept_token.length];
				double Max_weight=0;
				for(int L=0;L<concept_token.length;L++)
				{		
					concept_token_weight[L]=Math.log(Classes1.size()/concept_token_idf[L]);
					if(Max_weight<concept_token_weight[L])
					{
						Max_weight=concept_token_weight[L];
					}
				}
				
				double normalized_concept_token_weight[]=new double[concept_token.length];
				
				//进行存储
				String cpt_tokens="";
				String cpt_weight="";
				for(int L=0;L<concept_token.length;L++)
				{		
					normalized_concept_token_weight[L]=concept_token_weight[L]/Max_weight;
					if(cpt_tokens.equals(""))
						cpt_tokens=concept_token[L];
					else
					{
						cpt_tokens=cpt_tokens+","+concept_token[L];
					}
					if(cpt_weight.equals(""))
						cpt_weight=cpt_weight+normalized_concept_token_weight[L];
					else
					{
						cpt_weight=cpt_weight+","+normalized_concept_token_weight[L];
					}		
				}
				
			//	System.out.print(cpt_tokens+" "+cpt_weight+"\n");
				//System.out.println(cpt_weight);
				Concept_tokens1.add(cpt_tokens);
				TF_IDF1.add(cpt_weight);		
			}
			
			System.out.println("Classes1 has been tokens");
			ArrayList<String> Tokens2 = new ArrayList<String>();
			// 将本体1中的概念基于'_'进行切割。
			for (int i = 0; i < Classes2.size(); i++) {
				String concept2 = Classes2.get(i);
				String []concept_token2=tokeningWord(concept2).split(" ");
				//String[] concept_token2 = concept2.split("_");
				for (int k = 0; k < concept_token2.length; k++) {
					Tokens2.add(concept_token2[k]);
				}
			}
			ArrayList<String> Concept_tokens2 = new ArrayList<String>();
			ArrayList<String> TF_IDF2 = new ArrayList<String>();

			for (int i = 0; i < Classes2.size(); i++) {
				String concept2 = Classes2.get(i);
				String []concept_token2=tokeningWord(concept2).split(" ");
				//String[] concept_token2 = concept2.split("_");
				// int[concept_token.length];////tf其实没必要，如果将一个概念当做一个网页的话。
				double concept_token_idf2[] = new double[concept_token2.length];
				for (int j = 0; j < concept_token2.length; j++) {
					for (int k = 0; k < Tokens2.size(); k++) {
						if (Tokens2.get(k).equals(concept_token2[j]))// IDF 中的Dw加1
						{
							concept_token_idf2[j]++;
						}
					}
				}
				// 计算概念中每个tokens的权重
				double concept_token_weight2[] = new double[concept_token2.length];
				double Max_weight2 = 0;
				for (int L = 0; L < concept_token2.length; L++) {
					concept_token_weight2[L] = Math.log(Classes2.size()/ concept_token_idf2[L]);
					if (Max_weight2 < concept_token_weight2[L]) {
						Max_weight2 = concept_token_weight2[L];
					}
				}

				double normalized_concept_token_weight2[] = new double[concept_token2.length];
				// 进行存储
				String cpt_tokens2 = "";
				String cpt_weight2 = "";
				for (int L = 0; L < concept_token2.length; L++) {
					normalized_concept_token_weight2[L] = concept_token_weight2[L]/ Max_weight2;
					if (cpt_tokens2.equals(""))
						cpt_tokens2 = concept_token2[L];
					else 
					{
						cpt_tokens2 = cpt_tokens2 + "," + concept_token2[L];
					}
					if (cpt_weight2.equals(""))
						cpt_weight2 = cpt_weight2+ normalized_concept_token_weight2[L];
					else 
					{
						cpt_weight2 = cpt_weight2 + ","+ normalized_concept_token_weight2[L];
					}

				}
				//	System.out.print(cpt_tokens2 + " " + cpt_weight2 + "\n");
				// System.out.println(cpt_weight);
				Concept_tokens2.add(cpt_tokens2);
				TF_IDF2.add(cpt_weight2);
			}
			System.out.println("Classes2 has been tokens");
			
			String concept1="";
			String concept2="";
			double TF_IDF_similarity=0;
			int sum=0;
			for(int i=0;i<Classes1.size();i++)
			{
				concept1=Classes1.get(i);
				for(int j=0;j<Classes2.size();j++)
				{
					concept2=Classes2.get(j);
					//因为每个概念与相应的其分解的tokens的标号是一致的
					TF_IDF_similarity=TFIDF_similarity(concept1,concept2,Concept_tokens1.get(i),TF_IDF1.get(i),Concept_tokens2.get(j),TF_IDF2.get(j));
					//bfw_TDIDF_Similarities.append(Classes1.get(i)+","+Classes2.get(j)+","+TF_IDF_similarity+"\n");
					if(TF_IDF_similarity>0)
						sum++;
					tfidfSimiliary.add(concept1+","+concept2+","+TF_IDF_similarity);		
				}
			}
			System.out.println("TFIDF不为0的个数为:"+sum);
		  return tfidfSimiliary;
	  }
	 
	  public static double TFIDF_similarity(String concept1,String concept2,String tokens1,String TF_IDF1,String tokens2,String TF_IDF2)
	  {
		  String[] concept_token1 = tokens1.split(",");
		  String[] concept_token2 = tokens2.split(",");
		  String[] tfidf1 = TF_IDF1.split(",");
		  String[] tfidf2 = TF_IDF2.split(",");
		  boolean[] flag1=new boolean[concept_token1.length];
		  boolean[] flag2=new boolean[concept_token2.length];

		  String commen_token="";
		  for(int i=0;i<concept_token1.length;i++)
		  {
			  String token1=concept_token1[i];
			  for(int j=0;j<concept_token2.length;j++)
			  {
				  String token2=concept_token2[j];
				  //如果子串匹配则，对应的标签又false变为true
				  if(token1.equals(token2))
				  {
					  flag1[i]=true;
					  flag2[j]=true;
					  commen_token=commen_token+token1+"  ";					
				  }
			  }
		  }

		  double sum1=0;
		  double common1=0;		
		  for(int i=0;i<tfidf1.length;i++)
		  {
			  if(flag1[i]==true)
			  {
				  common1=common1+Double.parseDouble(tfidf1[i]);
			  }
			  sum1=sum1+Double.parseDouble(tfidf1[i]);
		  }

		  double sum2=0;
		  double common2=0;		
		  for(int j=0;j<tfidf2.length;j++)
		  {
			  if(flag2[j]==true)
			  {
				  common2=common2+Double.parseDouble(tfidf2[j]);
			  }
			  sum2=sum2+Double.parseDouble(tfidf2[j]);
		  }

		  double similarity=(common1+common2)/(sum1+sum2);
		 /* if(similarity>0)
		  {
			  System.out.println(concept1+"与"+concept2+"共同的子串为"+commen_token);
			  System.out.println(concept1+"与"+concept2+"的相似度为"+similarity);
		  }	*/	
		  return similarity;
	  }	
	  
	  /**
	   * 
	   * @param object1s
	   * @param object2s
	   * @return
	   */
	  public ArrayList<String> instancesSim(ArrayList<String> classes1, ArrayList<String> classes2, ArrayList<String> conceptInstances1, ArrayList<String> conceptInstances2) 
	  {
		  ArrayList<String> instancesSim=new ArrayList<String>();
		  TreeMap_Tools classesInstances1=new TreeMap_Tools(conceptInstances1);	
		  TreeMap_Tools classesInstances2=new TreeMap_Tools(conceptInstances2);
		  ArrayList<String> concepts1=classesInstances1.GetKey();
		  System.out.println(concepts1.size());
		  ArrayList<String> concepts2=classesInstances2.GetKey();	
		  System.out.println(concepts2.size());
		  ArrayList<String> Instances_sim=new ArrayList<String>();

		  //Jaccard-sim(A,B)=p(a,b)/p(A U B) 用这个公式进行计算
		  //这里的Concept1，Concept2并不为空
		  for(int i=0;i<concepts1.size();i++)
		  {	
			  String concept1=concepts1.get(i);
			  //String concept1="city";
			  ArrayList<String> Instances1=classesInstances1.GetKey_Value(concept1);			
			  for(int j=0;j<concepts2.size();j++)
			  {	
				  double sim=0;
				  String concept2=concepts2.get(j);	
				  // String concept2="city";
				  ArrayList<String> Instances2=classesInstances2.GetKey_Value(concept2);
				  double num1=Instances1.size();
				  double num2=Instances2.size();
				  ArrayList<String> copy_Instances1=new ArrayList<String>();
				  //copy_Instances1=(ArrayList<String>) Instances1.clone(); 
				  copy_Instances1.addAll(Instances1); 
				  copy_Instances1.removeAll(Instances2);//获取A-B实例
				  double num3=Instances1.size()-copy_Instances1.size();
				  //System.out.println(concept1+"的实例大小为："+num1);//A的实例个数
				  //System.out.println(concept2+"的实例大小为："+num2);//B的实例个数
				  //System.out.println("两者公共的实例大小为："+(num1-num3));//A-B的实例个数
				  //sim=(num1-num3)/(num1+num2-(num1-num3));
				  //sim=num3/(num1+num2-num3);
				  sim=num3/Math.min(num1, num2);	//考虑到可能两个概念的实例个数差别比较大
				  //System.out.println(concept1+","+concept2+","+sim);
				  /*DecimalFormat format=new DecimalFormat("#.00"); //double转字符串
						Instances_sim.add(format.format(sim));*/
				  if(sim>0)
					  Instances_sim.add(concept1+","+concept2+"--"+sim);
			  }
		  }
		  TreeMap_Tools validate_Instances_sim=new TreeMap_Tools(Instances_sim);	
		  ArrayList<String> concept_pair = validate_Instances_sim.GetKey();

		  /*BufferedWriter bfw_Instances_Similarities = null;
			BufferedWriter bfw_Instances_Similarities_PairName = null;*/
		  //将所有的情况遍历一遍,主要是将概率值进行离散化
		  int I1=0,I2=0,I3=0;
		  int m=0;
		  for(int i=0;i<classes1.size();i++)
		  {
			  for(int j=0;j<classes2.size();j++)
			  {
				  String concept1=classes1.get(i).toLowerCase();
				  String concept2=classes2.get(j).toLowerCase();
				  String new_concept_pair=concept1+","+concept2;
				  if(concept_pair.contains(new_concept_pair))
				  {
					  double value=Double.parseDouble(validate_Instances_sim.GetKey_Value(new_concept_pair).get(0));
					 // System.out.println(new_concept_pair+","+value);
					  int numberInstances1=classesInstances1.GetKey_Value(concept1).size();
					  int numberInstances2=classesInstances2.GetKey_Value(concept2).size();
					  if(value>=0.5)
					  {
						  System.out.println(new_concept_pair+","+"1"+"对应的编号为:"+m);
						  instancesSim.add(new_concept_pair+","+"1");
					  	  I1++;
					  }
					  else if(value<0.5&&numberInstances1>=3&&numberInstances2>=3) //保证其个数有一定说服性
					  {
						  instancesSim.add(new_concept_pair+","+"0");
					  	  I2++;
					  }
					  else  
					  {
						  instancesSim.add(new_concept_pair+","+"*");
						  I3++;
					  }
				  }
				  else
				  {
					  instancesSim.add(new_concept_pair+","+"*");
					  //System.out.println(new_concept_pair+","+0.0);
					  I3++;
				  }
				  m++;
			  }
		  }
		  System.out.println("发现实例匹配的概念对为：	"+I1);
		  System.out.println("发现实例不匹配的概念对为：	"+I2);
		  System.out.println("发现实例匹配未知的概念对为：	"+I3);
		  return instancesSim;
	  }
	  
	  public ArrayList<String> instancesSim2(ArrayList<String> classes1, ArrayList<String> classes2, ArrayList<String> conceptInstances1, ArrayList<String> conceptInstances2) 
	  {
		  ArrayList<String> instancesSim=new ArrayList<String>();
		  TreeMap_Tools classesInstances1=new TreeMap_Tools(conceptInstances1);	
		  TreeMap_Tools classesInstances2=new TreeMap_Tools(conceptInstances2);
		  ArrayList<String> conceptSet1=classesInstances1.GetKey();
		  System.out.println(conceptSet1.size());
		  ArrayList<String> conceptSet2=classesInstances2.GetKey();	
		  System.out.println(conceptSet2.size());
		  ArrayList<String> Instances_sim=new ArrayList<String>();

		  //Jaccard-sim(A,B)=p(a,b)/p(A U B) 用这个公式进行计算
		  //这里的Concept1，Concept2并不为空
		  for(int i=0;i<conceptSet1.size();i++)
		  {	
			  String concept1=conceptSet1.get(i);
			  //String concept1="city";
			  ArrayList<String> Instances1=classesInstances1.GetKey_Value(concept1);			
			  for(int j=0;j<conceptSet2.size();j++)
			  {	
				  double sim=0;
				  String concept2=conceptSet2.get(j);	
				  // String concept2="city";
				  ArrayList<String> Instances2=classesInstances2.GetKey_Value(concept2);
				  double num1=Instances1.size();
				  double num2=Instances2.size();
				  ArrayList<String> copy_Instances1=new ArrayList<String>();
				  //copy_Instances1=(ArrayList<String>) Instances1.clone(); 
				  copy_Instances1.addAll(Instances1); 
				  copy_Instances1.removeAll(Instances2);//获取A-B实例
				  double num3=Instances1.size()-copy_Instances1.size();
				  //System.out.println(concept1+"的实例大小为："+num1);//A的实例个数
				  //System.out.println(concept2+"的实例大小为："+num2);//B的实例个数
				  //System.out.println("两者公共的实例大小为："+(num1-num3));//A-B的实例个数
				  //sim=(num1-num3)/(num1+num2-(num1-num3));
				  //sim=num3/(num1+num2-num3);
				  sim=num3/Math.sqrt(num1*num2);  //改成Ochiai coefficient
				//  sim=num3/Math.min(num1, num2);	//考虑到可能两个概念的实例个数差别比较大
				  //System.out.println(concept1+","+concept2+","+sim);
				  /*DecimalFormat format=new DecimalFormat("#.00"); //double转字符串
						Instances_sim.add(format.format(sim));*/
				  if(sim>0)
					  Instances_sim.add(concept1+","+concept2+"--"+sim);
			  }
		  }
		  TreeMap_Tools validate_Instances_sim=new TreeMap_Tools(Instances_sim);	
		  ArrayList<String> concept_pair = validate_Instances_sim.GetKey();

		  /*BufferedWriter bfw_Instances_Similarities = null;
			BufferedWriter bfw_Instances_Similarities_PairName = null;*/
		  //将所有的情况遍历一遍,主要是将概率值进行离散化
		  int I1=0,I2=0,I3=0;
		  int m=0;
		  for(int i=0;i<classes1.size();i++)
		  {
			  for(int j=0;j<classes2.size();j++)
			  {
				  String concept1=classes1.get(i).toLowerCase();
				  String concept2=classes2.get(j).toLowerCase();
				  String new_concept_pair=concept1+","+concept2;
				  if(concept_pair.contains(new_concept_pair))
				  {
					  double value=Double.parseDouble(validate_Instances_sim.GetKey_Value(new_concept_pair).get(0));
					 // System.out.println(new_concept_pair+","+value);
					  int numberInstances1=classesInstances1.GetKey_Value(concept1).size();
					  int numberInstances2=classesInstances2.GetKey_Value(concept2).size();
					  if(value>=0.4)  //matches
					  {
						  System.out.println(new_concept_pair+","+"1"+"对应的编号为:"+m);
						  instancesSim.add(new_concept_pair+","+"1");
					  	  I1++;
					  }
					  else if(0<value&&value<0.4) //overlap unknown
					  {
						  instancesSim.add(new_concept_pair+","+"*");
					  	  I3++;
					  }
					  else
					  {
						  instancesSim.add(new_concept_pair+","+"0");
						  I2++;
					  }
					/*  else if(value<0.4&&numberInstances1>=3&&numberInstances2>=3) //保证其个数有一定说服性
					  {
						  instancesSim.add(new_concept_pair+","+"0");
					  	  I2++;
					  }
					  else  
					  {
						  instancesSim.add(new_concept_pair+","+"*");
						  I3++;
					  }*/
				  }
				  else
				  {
					  instancesSim.add(new_concept_pair+","+"*");
					  //System.out.println(new_concept_pair+","+0.0);
					  I3++;
				  }
				  m++;
			  }
		  }
		  System.out.println("发现实例匹配的概念对为：	"+I1);
		  System.out.println("发现实例不匹配的概念对为：	"+I2);
		  System.out.println("发现实例匹配未知的概念对为：	"+I3);
		  return instancesSim;
	  }
	  
	  public ArrayList<String> ClassDisjoint(ArrayList<String> classes1, ArrayList<String> classes2)
	  {
		  ArrayList<String> classDisjoitnSim=new ArrayList<String>();
		  char Disjoint='*';
		  for(int i=0;i<classes1.size();i++)
		  {
			  for(int j=0;j<classes2.size();j++)
			  {
				  classDisjoitnSim.add(classes1.get(i)+","+classes2.get(j)+","+Disjoint);
			  }
		  }
		  return classDisjoitnSim;
	  }
	  
	  public ArrayList<String> initialClass(ArrayList<String> classes1, ArrayList<String> classes2)
	  {
		  ArrayList<String> classEditSim=new ArrayList<String>();
		  //char Disjoint='*';
		  for(int i=0;i<classes1.size();i++)
		  {
			  for(int j=0;j<classes2.size();j++)
			  {
				  classEditSim.add(classes1.get(i)+","+classes2.get(j)+","+0);
			  }
		  }
		  return classEditSim;
	  }
	  
	  
	  
	  /**
	   * this function is used to preprocess Strings
	   * @param str
	   * @return tokens
	   */
	  public String tokeningWord(String str)
	  {
	   		String s1=str;
	   		//s1="Registration_SIGMOD_Member";
	   		String ss = "";
	   		for(int i=0;i<s1.length()-1;i++){
	   			char aa=s1.charAt(i+1);
	   			char a=s1.charAt(i);
	   			if(Character.isUpperCase(a) && i==0)//如果首字母是大写则直接添加
	   			{
	   				ss=ss+String.valueOf(a);
	   			}
	   			else if(Character.isUpperCase(a) &&Character.isLowerCase(aa)&& i!=0)//如果非字母是大写则需要插入分隔符
	   			{
	   				ss=ss+" "+String.valueOf(a);
	   			}	
	   			else if((a=='-'||a=='_')||a=='.')//当出现字符"-","_" 而且后面aa是大写，则不做操作
	   			{
	   				//continue;
	   				ss=ss+" ";//等于间接将'_','-'进行了替换
	   			}
	   			else if(Character.isUpperCase(a)&&Character.isUpperCase(aa))
	   			{
	   				ss=ss+String.valueOf(a);
	   			}	
	   			else if(Character.isLowerCase(a)&&Character.isUpperCase(aa))//前面小写后面接大写
	   			{
	   				ss=ss+String.valueOf(a)+" ";
	   			}	
	   			else  //其实情况正常添加
	   			{             
	   				ss=ss+String.valueOf(a);
	   			}
	   		}
	   		ss=ss+s1.charAt(s1.length()-1);
	   		ss=ss.replace("  ", " ").trim();
	   		return ss.toLowerCase().replaceAll("_|-","");		
	   }
	
}
