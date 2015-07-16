package Actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import SPNs.SPNProcessing;
import Tools.HeuristicRule_Tools;
import Tools.Pellet_tools;
import Tools.Refine_Tools;
import Tools.Sim_Tools;
import Tools.TreeMap_Tools;

public class NormalizedAnatomy {
	static Sim_Tools tool=new Sim_Tools();
	static BufferedWriter bfw_Result= null;
	//static BufferedWriter bfw_middle= null;
	public static void main(String args[]) throws IOException, ClassNotFoundException
	{

		String resultPath="Results/anatomy(1).txt";		
		try
		{			
			bfw_Result=new BufferedWriter(new FileWriter(resultPath));	
		}
		catch(IOException e)
		{
			e.printStackTrace();		
		}
		tool.initialPOS();


		String dictionary1="anatomy";
		String Read_path1="dic/"+dictionary1+".txt";
		BufferedReader Result1 = new BufferedReader(new FileReader(new File(Read_path1)));
		ArrayList<String> dic1= new ArrayList<String>();
		String lineTxt = null;
		while ((lineTxt = Result1.readLine()) != null) {
			String line = lineTxt.trim(); // 去掉字符串首位的空格，避免其空格造成的错误
			//line=line.toLowerCase();//全部变成小写
			dic1.add(line);
		}
		HashMap<String, String> anatomy=transformToHashMap(dic1);

		long tic1 = 0 ;
		long toc1= 0;
		long tic = 0 ;
		long toc= 0;
		tic1=System.currentTimeMillis();


		tic = System.currentTimeMillis();
		String ontologyName1="mouse";
		String ontologyName2="human";

		String readPath1="Datasets/anatomy/"+ontologyName1+".owl";
		String readPath2="Datasets/anatomy/"+ontologyName2+".owl";

		String classAlignmentPath="Results/Anatomy_alignments/"+ontologyName1+"-"+ontologyName2+"_class.txt";	
		String propertyAlignmentPath="Results/Anatomy_alignments/"+ontologyName1+"-"+ontologyName2+"_property.txt";

		Pellet_tools onto1=new Pellet_tools(); 
		Pellet_tools onto2=new Pellet_tools(); 
		//Loading
		onto1.readOnto(readPath1);
		onto2.readOnto(readPath2);

		TreeMap_Tools resultClassMap=new TreeMap_Tools(classAlignmentPath);
		TreeMap_Tools resultPropertyMap=new TreeMap_Tools(propertyAlignmentPath);

		/**
		 * acquire ontology information
		 */
		ArrayList<String> classes1=onto1.GetAllConcept();
		ArrayList<String> classlabel1=onto1.GetAllConceptLabel();
		ArrayList<String> objectProperties1=onto1.GetObjectProperty();
		ArrayList<String> objectPropertieslabel1=onto1.GetObjectPropertyLabel();
		ArrayList<String> dataProperties1=onto1.GetDataProperty();
		ArrayList<String> dataPropertieslabel1=onto1.GetDataPropertyLabel();
		ArrayList<String> objectRelations1=onto1.New_GetObjectRelations();
		ArrayList<String> instances1=filterCommand(onto1.GetConcept_Instances());
		//ArrayList<String> objectRelations1=onto1.GetObjectRelations();

		ArrayList<String> EquivalentClass1=onto1.GetEquivalentClass();
		ArrayList<String> Restrictions1=onto1.GetSomeRestrictions();
		/*subclasses1=onto1.enhancedSubClasses(subclasses1,EquivalentClass1);
		superclasses1=onto1.enhancedSuperClasses(superclasses1,EquivalentClass1);
		disjoint1=onto1.enhancedClassesDisjoint(disjoint1,subclasses1,EquivalentClass1);*/
		objectRelations1=onto1.enhancedRelation(objectRelations1,EquivalentClass1);
		System.out.println("**********");
		objectRelations1=onto1.enhancedRelation(objectRelations1,Restrictions1);


		ArrayList<String> classes2=onto2.GetAllConcept();
		ArrayList<String> classlabel2=onto2.GetAllConceptLabel();
		ArrayList<String> objectProperties2=onto2.GetObjectProperty();
		ArrayList<String> objectPropertieslabel2=onto2.GetObjectPropertyLabel();
		ArrayList<String> dataProperties2=onto2.GetDataProperty();
		ArrayList<String> dataPropertieslabel2=onto2.GetDataPropertyLabel();
		ArrayList<String> objectRelations2=onto2.New_GetObjectRelations();
		//ArrayList<String> objectRelations2=onto2.GetObjectRelations();
		ArrayList<String> instances2=filterCommand(onto2.GetConcept_Instances());
		ArrayList<String> EquivalentClass2=onto2.GetEquivalentClass();
		ArrayList<String> Restrictions2=onto2.GetSomeRestrictions();
		/*subclasses2=onto2.enhancedSubClasses(subclasses2,EquivalentClass2);
		superclasses2=onto2.enhancedSuperClasses(superclasses2,EquivalentClass2);
		disjoint2=onto2.enhancedClassesDisjoint(disjoint2,subclasses2,EquivalentClass2);*/
		objectRelations2=onto2.enhancedRelation(objectRelations2,EquivalentClass2);
		System.out.println("**********");
		objectRelations2=onto2.enhancedRelation(objectRelations2,Restrictions2);

		/**
		 * calculate the similarities by ontology information
		 */
		/**
		 * Instances
		 */
		toc = System.currentTimeMillis();
		System.out.println(toc-tic);
		//父子中只有一个儿子且不为空的情况，可能导致相似度是一样的（仍需改进空间）
		tic = System.currentTimeMillis();
		System.out.println(toc-tic);
		ArrayList<String> InstanceSim=tool.instancesSim(classes1,classes2,instances1, instances2);
		System.out.println("*************************************");
		System.out.println(InstanceSim.size());
		toc = System.currentTimeMillis();
		System.out.println(toc-tic);


		/**
		 * concepts
		 */
		//类只是一个编号，而真正的名字是label

		/*toc = System.currentTimeMillis();
		System.out.println(toc-tic);*/
		ArrayList<String> editSimClass=new ArrayList<String>();
		ArrayList<String> semanticSimClass=new ArrayList<String>();
		ArrayList<String> tfidfSim=new ArrayList<String>();
		HashMap<String,String> classLabels1=new HashMap<String,String>();
		HashMap<String,String> classLabels2=new HashMap<String,String>();

		System.out.println(InstanceSim.get(0));
		boolean labelflag=classes1.size()==classlabel1.size()&&classes2.size()==classlabel2.size();//只有在医学本体中才会出现
		if(labelflag==true)
		{		
			classLabels1=tool.transformToHashMap(classlabel1);//存储一份对应形式
			classLabels2=tool.transformToHashMap(classlabel2);//存储一份对应形式
			
			classlabel1=tool.keepLabel(classlabel1);
			classlabel2=tool.keepLabel(classlabel2);
			
			classlabel1=Normalize(classlabel1,anatomy);
			classlabel2=Normalize(classlabel2,anatomy);
			
			int m=0;
			tic = System.currentTimeMillis();
			for(int i=0;i<classlabel1.size();i++)
			{
				String concept1=classlabel1.get(i).toLowerCase().toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
				for(int j=0;j<classlabel2.size();j++)
				{
					String concept2=classlabel2.get(j).toLowerCase().toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
					if(concept1.equals(concept2))
					{
						editSimClass.add(concept1+","+concept2+","+1);
						m++;
					}
					else
						editSimClass.add(concept1+","+concept2+","+0);
				}
			}
			System.out.println("相等的个数为:"+m);
			//editSimClass=tool.editSimClass(classlabel1, classlabel2);
			//newEditSimClass=tool.editSimClass(classlabel1, classlabel2);
			System.out.println("*************************************");
			System.out.println(editSimClass.size());
			
			toc = System.currentTimeMillis();
			System.out.println("编辑距离相似度计算消耗的时间为："+(toc-tic));
			bfw_Result.append("编辑距离相似度计算消耗的时间为："+(toc-tic)/1000+"s"+"\n");
			//这里要将class--label进行一步处理,只保留label
			
			tic = System.currentTimeMillis();
			semanticSimClass=tool.NewsemanticSimClass3(classlabel1, classlabel2);//这里也词典就利用上面的判断条件引入
			System.out.println("*************************************");
			System.out.println(semanticSimClass.size());
			toc = System.currentTimeMillis();
			System.out.println("语义相似度计算消耗的时间为："+(toc-tic));
			bfw_Result.append("语义相似度计算消耗的时间为："+(toc-tic)/1000+"s"+"\n");
		}
		else
		{	
			classlabel1=tool.briefLabel(classlabel1);
			classlabel2=tool.briefLabel(classlabel2);
			tic = System.currentTimeMillis();
			editSimClass=tool.editSimClassWithLabel(classes1, classes2,classlabel1,classlabel2);
			//editSimClass=tool.editSimClass(classlabel1,classlabel2);
			System.out.println("*************************************");
			System.out.println(editSimClass.size());
			toc = System.currentTimeMillis();
			System.out.println(toc-tic);

			//从时间开销来考虑的话，下面2者的计算，只能用label替换来计算
			classlabel1=tool.replaceLabel(classes1,classlabel1);
			classlabel2=tool.replaceLabel(classes2,classlabel2);
			tic = System.currentTimeMillis();
			semanticSimClass=tool.semanticSimClass(classlabel1,classlabel2);//这里也词典就利用上面的判断条件引入
			System.out.println("*************************************");
			System.out.println(semanticSimClass.size());
			toc = System.currentTimeMillis();
			System.out.println(toc-tic);

			tic = System.currentTimeMillis();
			tfidfSim=tool.tfidfSim(classlabel1,classlabel2);
			System.out.println("*************************************");
			System.out.println(tfidfSim.size());
			toc = System.currentTimeMillis();
			System.out.println(toc-tic);
		}

		/**
		 * ObjectProperties
		 */
		ArrayList<String> editSimObjectProperty=new ArrayList<String>();
		ArrayList<String> SemanticSimObjectProperty=new ArrayList<String>();
		boolean objectproperty_lableflag=false;
		objectproperty_lableflag=objectProperties1.size()==objectPropertieslabel1.size()&&objectProperties2.size()==objectPropertieslabel2.size();//只有在医学本体中才会出现
		ArrayList<String> objectPropertyPair=new ArrayList<String>();
		for(int i=0;i<objectProperties1.size();i++)
		{
			String objectproperty1=objectProperties1.get(i);
			for(int j=0;j<objectProperties2.size();j++)
			{
				String objectproperty2=objectProperties2.get(j);
				objectPropertyPair.add(objectproperty1+","+objectproperty2);
			}
		}
		if(objectproperty_lableflag==true)
		{
			//只用label来进行计算,一般这种情况只出现在医学本体中
			objectPropertieslabel1=tool.keepLabel(objectPropertieslabel1);
			objectPropertieslabel2=tool.keepLabel(objectPropertieslabel2);

			editSimObjectProperty=tool.editSimProperty(objectPropertieslabel1, objectPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(editSimObjectProperty.size());

			SemanticSimObjectProperty=tool.semanticSimProperty(objectPropertieslabel1, objectPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(SemanticSimObjectProperty.size());
		}
		else
		{
			//editSimClass=tool.editSimClassWithLabel(classes1, classes2,classlabel1,classlabel2);
			//将label替换来计算编辑距离(分开算代价太高)
			objectPropertieslabel1=tool.briefLabel(objectPropertieslabel1);
			objectPropertieslabel2=tool.briefLabel(objectPropertieslabel2);
			
			editSimObjectProperty=tool.editSimPropertyWithLabel(objectProperties1, objectProperties2,objectPropertieslabel1,objectPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(editSimObjectProperty.size());

			objectPropertieslabel1=tool.replaceLabel(objectProperties1,objectPropertieslabel1);
			objectPropertieslabel2=tool.replaceLabel(objectProperties2,objectPropertieslabel2);

			SemanticSimObjectProperty=tool.semanticSimProperty(objectPropertieslabel1, objectPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(SemanticSimObjectProperty.size());
		}

		/**
		 * datatypeProperties
		 */
		ArrayList<String> editSimDatatypeProperty=new ArrayList<String>();
		ArrayList<String> SemanticSimDatatypeProperty=new ArrayList<String>();	
		boolean dataproperty_lableflag=false;
		dataproperty_lableflag=dataProperties1.size()==dataPropertieslabel1.size()&&dataProperties2.size()==dataPropertieslabel2.size();//只有在医学本体中才会出现
		ArrayList<String> dataPropertyPair=new ArrayList<String>();
		for(int i=0;i<dataProperties1.size();i++)
		{
			String dataProperty1=dataProperties1.get(i);
			for(int j=0;j<dataProperties2.size();j++)
			{
				String dataProperty2=dataProperties2.get(j);
				dataPropertyPair.add(dataProperty1+","+dataProperty2);
			}
		}
		if(dataproperty_lableflag==true)
		{
			dataPropertieslabel1=tool.keepLabel(dataPropertieslabel1);
			dataPropertieslabel2=tool.keepLabel(dataPropertieslabel2);
			editSimDatatypeProperty=tool.editSimProperty(dataPropertieslabel1, dataPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(editSimDatatypeProperty.size());

			SemanticSimDatatypeProperty=tool.semanticSimProperty(dataPropertieslabel1, dataPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(SemanticSimDatatypeProperty.size());
		}
		else
		{
			//将label替换来计算编辑距离(分开算代价太高)
			dataPropertieslabel1=tool.briefLabel(dataPropertieslabel1);
			dataPropertieslabel2=tool.briefLabel(dataPropertieslabel2);
			
			editSimDatatypeProperty=tool.editSimPropertyWithLabel(dataProperties1,dataProperties2,dataPropertieslabel1, dataPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(editSimDatatypeProperty.size());

			dataPropertieslabel1=tool.replaceLabel(dataProperties1,dataPropertieslabel1);
			dataPropertieslabel2=tool.replaceLabel(dataProperties2,dataPropertieslabel2);
			SemanticSimDatatypeProperty=tool.semanticSimProperty(dataPropertieslabel1, dataPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(SemanticSimDatatypeProperty.size());
		}

		//以后肯定是循环模式(而且应该是ArrayList的格式进行写入)
		/**
		 * statistic the number of each pair that satisfys heuristic rules by ontology information
		 */

		ArrayList<String> classesMap=new ArrayList<String>();
		ArrayList<String> propertiesMap=new ArrayList<String>();
		
	/*	for(int i=0;i<editSimClass.size();i++)
		{
			String parts[]=editSimClass.get(i).split(",");
			String pair_names[]=InstanceSim.get(i).split(",");
			if(Double.parseDouble(parts[2])==1)
				classesMap.add(pair_names[0]+","+pair_names[1]+","+Double.parseDouble(parts[2]));
		}*/
		for(int i=0;i<InstanceSim.size();i++)
		{
			double editValue=getTripleValue(editSimClass.get(i));
			double SemanticValue=getTripleValue(semanticSimClass.get(i));
			String pair_names[]=InstanceSim.get(i).split(",");
			if(editValue==1||SemanticValue==1)
				classesMap.add(pair_names[0]+","+pair_names[1]+","+1);
			//if(editValue==1)
				
		}
		
		
		Refine_Tools refineTools=new Refine_Tools();
		ArrayList<String> temp=refineTools.keepOneToOneAlignment(classesMap);
		classesMap.clear();
		for(int i=0;i<temp.size();i++)
		{
			String parts[]=temp.get(i).split(",");
			if(Double.parseDouble(parts[2])==1)
				classesMap.add(temp.get(i));
		}

		System.out.println("最终的概念匹配对为：");
		for(int i=0;i<classesMap.size();i++)
		{
			System.out.println(classesMap.get(i));	
		}
		System.out.println("找到概念匹配对的个数为："+classesMap.size());

		System.out.println("最终的属性匹配对为：");
		for(int i=0;i<propertiesMap.size();i++)
		{
			System.out.println(propertiesMap.get(i));
		}
		System.out.println("找到属性匹配对的个数为："+propertiesMap.size());

		/*ArrayList<String> classAlignments=changeToAlignments(classesMap);
		ArrayList<String> propertyAlignments=changeToAlignments(propertiesMap);*/

		System.out.println("利用该系统计算 "+ontologyName1+" 与 "+ontologyName2+" 的结果如下：");
		bfw_Result.append("利用该系统计算 "+ontologyName1+" 与 "+ontologyName2+" 的结果如下："+"\n");
		//printResult(classAlignments,propertyAlignments,resultClassMap,resultPropertyMap);
		if(labelflag==true)
		{
			//只是为打印医学本体提供方法
			printAnatomyResult(classesMap,propertiesMap,classLabels1,classLabels2,resultClassMap,resultPropertyMap);
		}	
		else
		{
			printResult(classesMap,propertiesMap,resultClassMap,resultPropertyMap);
		}
		System.out.println(ontologyName1+" "+ontologyName2+" has been done!");
		bfw_Result.append("\n\n");
		
		//}//1层循环I3CON

		/* }//2层循环Conference、Benchmark
		}*/
		toc1=System.currentTimeMillis();
		System.out.println("总消耗时间为："+(toc1-tic1)/1000+"s");
		bfw_Result.append("总消耗时间为："+(toc1-tic1)/1000+"s"+"\n");
		bfw_Result.close();	
		//bfw_middle.close();
	}

	/**
	 * these function are just for preproccess
	 */
	public static ArrayList<String> filterCommand(ArrayList<String> maps)
	{
		ArrayList<String> fiteredMaps=new ArrayList<String>();
		for(int i=0;i<maps.size();i++)
		{
			String a=maps.get(i);
			fiteredMaps.add(a.replace("--,", "--"));
		}
		return fiteredMaps;
	}

	public static ArrayList<String> changeToLowerCase(ArrayList<String> things)
	{
		ArrayList<String> lowCaseArrayList=new ArrayList<String>();
		for(int i=0;i<things.size();i++)
		{
			lowCaseArrayList.add(things.get(i).toLowerCase());
		}
		return lowCaseArrayList;
	}

	public static int satisfiedNum(TreeMap_Tools rule,String pair)
	{
		ArrayList<String> value=rule.GetKey_Value(pair.toLowerCase());
		if(value!=null)
		{
			return (int) Double.parseDouble(value.get(0));
		}
		return 0;
	}

	public static double getTripleValue(String triple)
	{
		String part[]=triple.split(",");
		double value=Double.parseDouble(part[2]);
		return value;
	}

	public static int getEditValue(String triple)
	{
		String part[]=triple.split(",");
		int value=Integer.parseInt(part[3]);
		return value;
	}

	public static boolean unChange(ArrayList<String> classesMap,ArrayList<String> propertiesMap,ArrayList<String> oldClassesMap, ArrayList<String> oldPropertiesMap)
	{
		boolean classUnChange=false;
		boolean propertyUnChange=false;
		if(oldClassesMap.size()!=classesMap.size())
		{
			return false;
		}
		if(oldPropertiesMap.size()!=propertiesMap.size())
		{
			return false;
		}			
		for(int i=0;i<classesMap.size();i++)
		{
			if(oldClassesMap.contains(classesMap.get(i)))
				classUnChange=true;
			else
			{
				classUnChange=false;
				break;
			}
		}
		if(propertiesMap.size()==0)
			propertyUnChange=true;
		for(int j=0;j<propertiesMap.size();j++)
		{
			if(oldPropertiesMap.contains(propertiesMap.get(j)))
				propertyUnChange=true;
			else
			{
				propertyUnChange=false;
				break;
			}
		}
		boolean change=classUnChange&&propertyUnChange;
		return change;
	}

	/*public static ArrayList<String> complementMaps(ArrayList<String> maps,ArrayList<String> hiddenmaps,boolean flag)
	{
		Refine_Tools tools=new Refine_Tools();
		ArrayList<String> preciseMaps=new ArrayList<String>();	
		if(flag==false)//这种情况的maps是为空的
		{
			for(int i=0;i<hiddenmaps.size();i++)				
			{
				maps.add(hiddenmaps.get(i));
			}
			preciseMaps=tools.keepOneToOneAlignment(maps);//针对Class的时候还是需要的。极有可能出现1对多的情况
			maps.clear();
			for(int i=0;i<preciseMaps.size();i++)
			{
				String parts[]=preciseMaps.get(i).split(",");
				if(Double.parseDouble(parts[2])>0)
					maps.add(preciseMaps.get(i));
			}
			return maps;	//注意，这里返回的格式仍需要修改
		}
		else	//默认缺省时添加的情况
		{
			//要求1对1的匹配,更相信通过模型计算出来的结果。其他的结果只能作为辅助
			preciseMaps=tools.keepOneToOneAlignment(hiddenmaps);
			for(int i=0;i<preciseMaps.size();i++)
			{
				boolean index=false;
				String parts1[]=preciseMaps.get(i).split(",");
				for(int j=0;j<maps.size();j++)
				{
					String parts2[]=maps.get(j).split(",");
					if(parts2[0].equals(parts1[0])||parts2[1].equals(parts1[1])||Double.parseDouble(parts1[2])==0)//1对1的限制
					{
						index=true;
						break;
					}
				}
				if(index==false)
				{
					System.out.println(parts1[0]+"	"+parts1[1]);
					String concept1=parts1[0];
					String concept2=parts1[1];
					int length1=tool.tokeningWord(concept1).split(" ").length;
					int length2=tool.tokeningWord(concept2).split(" ").length;
					if(length1==length2&&length1==1&&Double.parseDouble(parts1[2])>0.7)//仅名字相同才给予考虑，因为有很多WordNet计算会存在一定错误，导致召回率很低
						maps.add(preciseMaps.get(i));
					if((length1==1&&length2>1)||(length1==1&&length2>1))	//单个词与组合词的情况。
						maps.add(preciseMaps.get(i));
					if(length1>1&&length2>1&&Double.parseDouble(parts1[2])>0.6)	//组合词的token均超过2个。
						maps.add(preciseMaps.get(i));
				}
					//maps.add(parts1[0]+"--"+parts1[1]);
			}
			return maps;
		}	
	}*/

	public static ArrayList<String> complementMaps(ArrayList<String> maps,ArrayList<String> hiddenmaps,String type)
	{
		Refine_Tools tools=new Refine_Tools();
		ArrayList<String> preciseMaps=new ArrayList<String>();	
		if(type.equals("class")&&maps.size()==0)//Class为空时的补充
		{
			for(int i=0;i<hiddenmaps.size();i++)				
			{
				maps.add(hiddenmaps.get(i));
			}
			preciseMaps=tools.keepOneToOneAlignment(maps);//针对Class的时候还是需要的。极有可能出现1对多的情况
			maps.clear();
			for(int i=0;i<preciseMaps.size();i++)
			{
				String parts[]=preciseMaps.get(i).split(",");
				if(Double.parseDouble(parts[2])>0)
					maps.add(preciseMaps.get(i));
			}
			return maps;	//注意，这里返回的格式仍需要修改
		}
		else if(type.equals("class")&&maps.size()!=0)//Class不为空时的补充
		{
			preciseMaps=tools.keepOneToOneAlignment(hiddenmaps);
			for(int i=0;i<preciseMaps.size();i++)
			{
				boolean index=false;
				String parts1[]=preciseMaps.get(i).split(",");
				for(int j=0;j<maps.size();j++)
				{
					String parts2[]=maps.get(j).split(",");
					if(parts2[0].equals(parts1[0])||parts2[1].equals(parts1[1])||Double.parseDouble(parts1[2])==0)//1对1的限制
					{
						index=true;
						break;
					}
				}
				if(index==false)
				{
					System.out.println(parts1[0]+"	"+parts1[1]);
					String concept1=parts1[0];
					String concept2=parts1[1];
					int length1=tool.tokeningWord(concept1).split(" ").length;
					int length2=tool.tokeningWord(concept2).split(" ").length;
					if(length1==length2&&length1==1&&Double.parseDouble(parts1[2])>0.7)//仅名字相同才给予考虑，因为有很多WordNet计算会存在一定错误，导致召回率很低
						maps.add(preciseMaps.get(i));
					if((length1==1&&length2>1)||(length1>1&&length2==1))	//单个词与组合词的情况。
						maps.add(preciseMaps.get(i));
					if(length1>1&&length2>1&&Double.parseDouble(parts1[2])>0.6)	//组合词的token均超过2个。
						maps.add(preciseMaps.get(i));
				}
			}
			return maps;
		}
		else if((type.equals("property")&&maps.size()!=0))//属性不为空的情况
		{
			preciseMaps=tools.keepOneToOneAlignment(hiddenmaps);
			for(int i=0;i<preciseMaps.size();i++)
			{
				boolean index=false;
				String parts1[]=preciseMaps.get(i).split(",");
				for(int j=0;j<maps.size();j++)
				{
					String parts2[]=maps.get(j).split(",");
					if(parts2[0].equals(parts1[0])||parts2[1].equals(parts1[1])||Double.parseDouble(parts1[2])==0)//1对1的限制
					{
						index=true;
						break;
					}
				}
				if(index==false)//不冲突的情况添加
					maps.add(preciseMaps.get(i));
			}
			return maps;
		}
		else
			//else if(type.equals("property"))//因为最初肯定为空,后续的过程必须在循环中预先做一次精炼
		{
			for(int i=0;i<hiddenmaps.size();i++)				
			{
				maps.add(hiddenmaps.get(i));
			}
			return maps;	//注意，这里返回的格式仍需要修改
		}
	}

	public static ArrayList<String> addMaps(ArrayList<String> maps,ArrayList<String> newMaps)//前面是老的，后面是新的
	{
		//Sim_Tools tools=new Sim_Tools();
		ArrayList<String> preciseMaps=new ArrayList<String>();	

		for(int i=0;i<newMaps.size();i++)
		{
			preciseMaps.add(newMaps.get(i));
		}
		ArrayList<String> alignments=changeToAlignments(preciseMaps);
		for(int i=0;i<maps.size();i++)//添加新的Maps要考虑值的大小,新的比原来的值要大
		{
			String parts[]=maps.get(i).split(",");
			String conceptPair=parts[0]+"--"+parts[1];
			if(alignments.contains(conceptPair))//因为新的值一定比前者大，所以可以pass掉
			{
				continue;
			}
			else
				preciseMaps.add(maps.get(i));
		}
		return preciseMaps;	
	}

	public static ArrayList<String> refineClass(ArrayList<String> maps,ArrayList<String>Classes1,ArrayList<String> Classes2)//方便将一些最初不太可靠，但又没有在SPN与Noisy-OR中的加强的匹配对过滤掉
	{
		Refine_Tools tools=new Refine_Tools();
		ArrayList<String> lowerCaseClasses1=changeToLowerCase(Classes1);
		ArrayList<String> lowerCaseClasses2=changeToLowerCase(Classes2);
		ArrayList<String> preciseMaps=new ArrayList<String>();
		for(int i=0;i<maps.size();i++)
		{
			String parts[]=maps.get(i).split(",");	
			//因为最终的匹配都转换成了小写，必须还原之后才知道其真实的长度
			int index1=tools.find_index(lowerCaseClasses1, parts[0]);
			int index2=tools.find_index(lowerCaseClasses2, parts[1]);
			int length1=tool.tokeningWord(Classes1.get(index1)).split(" ").length;
			int length2=tool.tokeningWord(Classes2.get(index2)).split(" ").length;
			/*if(length1==length2&&length1==1&&Double.parseDouble(parts[2])>0.7)//仅名字相同才给予考虑，因为有很多WordNet计算会存在一定错误，导致召回率很低
				preciseMaps.add(maps.get(i));
			else if((length1==1&&length2>1)||(length1>1&&length2==1)&&Double.parseDouble(parts[2])>=0.6)
				preciseMaps.add(maps.get(i));
			else if(length1>1&&length2>1&&Double.parseDouble(parts[2])>0.6)	//组合词的token均超过2个。
				preciseMaps.add(maps.get(i));*/
			/*if((length1==1&&length2>1)||(length1>1&&length2==1))	//单个词与组合词的情况。
				preciseMaps.add(maps.get(i));
			if(length1>1&&length2>1&&Double.parseDouble(parts[2])>0.6)	//组合词的token均超过2个。
				preciseMaps.add(maps.get(i));*/
			if(length1==length2&&length1==1&&Double.parseDouble(parts[2])>0.7)//仅名字相同才给予考虑，因为有很多WordNet计算会存在一定错误，导致召回率很低
				preciseMaps.add(maps.get(i));
			else if(length1==length2&&length1!=1&&Double.parseDouble(parts[2])>0.6)//仅名字相同才给予考虑，因为有很多WordNet计算会存在一定错误，导致召回率很低
				preciseMaps.add(maps.get(i));
			else if(length1!=length2&&Double.parseDouble(parts[2])>=0.6)
				preciseMaps.add(maps.get(i));
		}
		return preciseMaps;
	}

	/*public static ArrayList<String> refineProperty(ArrayList<String> maps)//方便将一些最初不太可靠，但又没有在SPN与Noisy-OR中的加强的匹配对过滤掉
	{
		ArrayList<String> preciseMaps=new ArrayList<String>();
		for(int i=0;i<maps.size();i++)
		{
			String parts[]=maps.get(i).split(",");
			int length1=tool.tokeningWord(parts[0]).split(" ").length;
			int length2=tool.tokeningWord(parts[1]).split(" ").length;
			if(length1==length2&&length1==1&&Double.parseDouble(parts[2])>0.7)//仅名字相同才给予考虑，因为有很多WordNet计算会存在一定错误，导致召回率很低
				preciseMaps.add(maps.get(i));
			if((length1==1&&length2>1)||(length1>1&&length2==1))	//单个词与组合词的情况。
				preciseMaps.add(maps.get(i));
			if(length1>1&&length2>1&&Double.parseDouble(parts[2])>0.6)	//组合词的token均超过2个。
				preciseMaps.add(maps.get(i));
		}
		return preciseMaps;
	}*/

	public static ArrayList<String> changeToAlignments(ArrayList<String> maps)
	{
		ArrayList<String> alignments=new ArrayList<String>();
		for(int i=0;i<maps.size();i++)
		{
			String parts[]=maps.get(i).split(",");
			alignments.add(parts[0]+"--"+parts[1]);
		}
		return alignments;
	}

	public static ArrayList<String> enhancedMap(ArrayList<String> Map,ArrayList<String> Equivalent)
	{
		for(int i=0;i<Equivalent.size();i++)
		{
			String equivalent[]=Equivalent.get(i).split(",");
			if(equivalent.length==3&&equivalent[2].equals("Equal"))  //parts[0]是parts[1]的儿子
			{
				int index =findIndex(Map,equivalent[0]);
				if(index!=-1)
				{
					String newMap=Map.get(index).replaceFirst(equivalent[0], equivalent[1]);//针对属性
					String newMap2=Map.get(index).replaceFirst(equivalent[0].toLowerCase(), equivalent[1].toLowerCase());//针对类
					if(!Map.contains(newMap))
						Map.add(newMap);
					else if(!Map.contains(newMap2))
					{
						Map.add(newMap2);
					}
				}
			}
		}
		return Map;
	}

	public static int findIndex(ArrayList<String> Map,String index)
	{
		//ArrayList<String> Triples=new ArrayList<String>();
		for(int i=0;i<Map.size();i++)
		{
			String parts[]=Map.get(i).split(",");
			if(index.toLowerCase().equals(parts[0].toLowerCase()))
			{
				return i;
			}
		}
		return -1;
	}

	public static HashMap<String,String> transformToHashMap(ArrayList<String> originalMap)
	{
		HashMap<String,String> standardMap=new HashMap<String,String>();
		for(int i=0;i<originalMap.size();i++)
		{
			String part[]=originalMap.get(i).split("--");
			standardMap.put(part[0],part[1]);
			//standardMap.add();
		}
		return standardMap;
	}

	public static ArrayList<String> Normalize(ArrayList<String> object,HashMap<String,String> dic)
	{
		ArrayList<String> normalizedThings=new ArrayList<String>();
		int num=0;
		for(int i=0;i<object.size();i++)
		{
			//String part[]=object.get(i).split("--");
			String normalized=dic.get(object.get(i));
			num++;
			if(normalized!=null)
			{
				String parts[]=normalized.split(" ");
				String pos= tool.findPOS(parts[0]);
				if(pos.equals("CD")||pos.equals("NNP"))//考虑到首字母缩写的问题
				{
					String abbr_letter = parts[1].charAt(0)+parts[0];
					normalized=normalized.replace(parts[0], abbr_letter).replace(parts[1]+" ", "");
				}
				normalizedThings.add(normalized);
				num++;
			}
			else
				normalizedThings.add(normalized);
			//standardMap.add();
		}
		//String candidate_num[]={"1","2","3","4","5","6","7","8","9"};
		System.out.println("规范化概念的个数为:"+num);
		return normalizedThings;
	}

	public  static void printResult(ArrayList<String> classesMap,ArrayList<String> propertiesMap,TreeMap_Tools resultClassMap,TreeMap_Tools resultPropertyMap) throws IOException
	{		
		double alignmentClassNumbers=resultClassMap.getNumberOfMap();
		double alignmentPropertyNumbers=resultPropertyMap.getNumberOfMap();

		double findClassNumbers=classesMap.size();
		double findPropertyNumbers=propertiesMap.size();

		double rightClassNumber=0,rightPropertyNumber=0;
		double R,P,F1;

		System.out.println("**************************************");
		bfw_Result.append("**************************************"+"\n");
		ArrayList<String> notFoundClassMaps=new ArrayList<String>();//O2的概念
		for(int i=0;i<classesMap.size();i++)
		{
			//String part[]=classesMap.get(i).split("--");
			String part[]=classesMap.get(i).split(",");
			String concept1=part[0].toLowerCase();
			String concept2=part[1].toLowerCase();
			//可能给出的reference alignment是反的
			if(resultClassMap.has_relation(concept1, concept2))
			{
				System.out.println(classesMap.get(i)+" (Right)");
				bfw_Result.append(classesMap.get(i)+" (Right)"+"\n");
				rightClassNumber++;
				resultClassMap.remove(concept1, concept2);
			}
			else if(resultClassMap.has_relation(concept2,concept1))
			{
				System.out.println(classesMap.get(i)+" (Right)");
				bfw_Result.append(classesMap.get(i)+" (Right)"+"\n");
				rightClassNumber++;
				resultClassMap.remove(concept2, concept1);
			}
			else
			{
				System.out.println(classesMap.get(i));
				bfw_Result.append(classesMap.get(i)+"\n");
				notFoundClassMaps.add(classesMap.get(i));
			}
		}

		if(resultClassMap.size()==0)
		{
			System.out.println("概念匹配对均已找到!");
			bfw_Result.append("概念匹配对均已找到!"+"\n");
		}
		else
		{
			System.out.println("未找到的概念匹配对为：");
			bfw_Result.append("未找到的概念匹配对为："+"\n");
			ArrayList<String> result=new ArrayList<String>();
			result=resultClassMap.Print_Value();		
			for(String a:result)
			{
				bfw_Result.append(a+"\n");
			}
		}
		System.out.println("找到正确的Class个数为："+rightClassNumber);
		bfw_Result.append("找到正确的Class个数为："+rightClassNumber+"\n");
		System.out.println("**************************************");
		bfw_Result.append("**************************************"+"\n");
		ArrayList<String> notFoundPropertyMaps=new ArrayList<String>();//O2的概念
		for(int i=0;i<propertiesMap.size();i++)
		{
			//String part[]=propertiesMap.get(i).split("--");
			String part[]=propertiesMap.get(i).split(",");
			String property1=part[0].toLowerCase();
			String property2=part[1].toLowerCase();
			//System.out.println(property1+","+property2);
			//可能给出的reference alignment是反的
			if(resultPropertyMap.has_relation(property1, property2))
			{
				System.out.println(part[0]+","+part[1]+","+part[2]+" "+" (Right)");
				//System.out.println(propertiesMap.get(i)+" (Right)");
				bfw_Result.append(part[0]+","+part[1]+","+part[2]+" "+" (Right)"+"\n");
				//bfw_Result.append(propertiesMap.get(i)+" (Right)"+"\n");
				rightPropertyNumber++;
				resultPropertyMap.remove(property1, property2);
			}
			else if(resultPropertyMap.has_relation(property2, property1))
			{
				System.out.println(part[0]+","+part[1]+","+part[2]+" "+" (Right)");
				//System.out.println(propertiesMap.get(i)+" (Right)");
				bfw_Result.append(part[0]+","+part[1]+","+part[2]+" "+" (Right)"+"\n");
				//bfw_Result.append(propertiesMap.get(i)+" (Right)"+"\n");
				rightPropertyNumber++;
				resultPropertyMap.remove(property2, property1);
			}
			else
			{
				System.out.println(part[0]+","+part[1]+","+part[2]);
				//System.out.println(propertiesMap.get(i));
				bfw_Result.append(part[0]+","+part[1]+","+part[2]+"\n");
				//bfw_Result.append(propertiesMap.get(i)+"\n");
				notFoundPropertyMaps.add(propertiesMap.get(i));
			}
		}
		if(resultPropertyMap.size()==0)
		{
			System.out.println("属性匹配对均已找到!");
			bfw_Result.append("属性匹配对均已找到!"+"\n");
		}
		else
		{
			System.out.println("未找到的属性匹配对为：");
			bfw_Result.append("未找到的属性匹配对为："+"\n");
			ArrayList<String> result=new ArrayList<String>();
			result=resultPropertyMap.Print_Value();	
			for(String a:result)
			{
				bfw_Result.append(a+"\n");
			}
		}
		System.out.println("找到正确的Property个数为："+rightPropertyNumber);
		P=(rightClassNumber+rightPropertyNumber)/(findClassNumbers+findPropertyNumbers);
		R=(rightClassNumber+rightPropertyNumber)/(alignmentClassNumbers+alignmentPropertyNumbers);
		R=Math.min(R, 1);
		if(P==0&&R==0)
			F1=0;
		else
			F1=(2*P*R)/(P+R);
		System.out.println("==========================================");
		System.out.println("正确率："+P+" "+"	召回率："+R+" "+"	F1值："+F1);
		bfw_Result.append("=========================================="+"\n");
		bfw_Result.append("正确率："+P+" "+"	召回率："+R+" "+"	F1值："+F1+"\n");

	}
	
	public  static void printAnatomyResult(ArrayList<String> classesMap,ArrayList<String> propertiesMap,HashMap<String,String> ClassLabel1,HashMap<String,String> ClassLabel2,TreeMap_Tools resultClassMap,TreeMap_Tools resultPropertyMap) throws IOException
	{		
		double alignmentClassNumbers=resultClassMap.getNumberOfMap();
		double alignmentPropertyNumbers=resultPropertyMap.getNumberOfMap();

		double findClassNumbers=classesMap.size();
		double findPropertyNumbers=propertiesMap.size();

		double rightClassNumber=0,rightPropertyNumber=0;
		double R,P,F1;

		System.out.println("**************************************");
		bfw_Result.append("**************************************"+"\n");
		ArrayList<String> notFoundClassMaps=new ArrayList<String>();//O2的概念
		for(int i=0;i<classesMap.size();i++)
		{
			//String part[]=classesMap.get(i).split("--");
			String part[]=classesMap.get(i).split(",");
			String concept1=part[0].toLowerCase();
			String concept2=part[1].toLowerCase();
			String label1=ClassLabel1.get(concept1);
			String label2=ClassLabel2.get(concept2);
			//可能给出的reference alignment是反的
			if(resultClassMap.has_relation(concept1, concept2))
			{
				/*System.out.println(classesMap.get(i)+" (Right)");
				bfw_Result.append(classesMap.get(i)+" (Right)"+"\n");
				rightClassNumber++;
				resultClassMap.remove(concept1, concept2);*/
				System.out.println(label1+","+label2+","+part[2]+" (Right)");
				bfw_Result.append(label1+","+label2+","+part[2]+" (Right)"+"\n");
				rightClassNumber++;
				resultClassMap.remove(concept1, concept2);
				
			}
			else if(resultClassMap.has_relation(concept2,concept1))
			{
				/*System.out.println(classesMap.get(i)+" (Right)");
				bfw_Result.append(classesMap.get(i)+" (Right)"+"\n");
				rightClassNumber++;
				resultClassMap.remove(concept2, concept1);*/
				System.out.println(label1+","+label2+","+part[2]+" (Right)");
				bfw_Result.append(label1+","+label2+","+part[2]+" (Right)"+"\n");
				rightClassNumber++;
				resultClassMap.remove(concept1, concept2);
			}
			else
			{
				/*System.out.println(classesMap.get(i));
				bfw_Result.append(classesMap.get(i)+"\n");
				notFoundClassMaps.add(classesMap.get(i));*/
				System.out.println(label1+","+label2+","+part[2]);
				bfw_Result.append(label1+","+label2+","+part[2]+"\n");
				notFoundClassMaps.add(classesMap.get(i));
			}
		}

		if(resultClassMap.size()==0)
		{
			System.out.println("概念匹配对均已找到!");
			bfw_Result.append("概念匹配对均已找到!"+"\n");
		}
		else
		{
			System.out.println("未找到的概念匹配对为：");
			bfw_Result.append("未找到的概念匹配对为："+"\n");
			ArrayList<String> keySet=resultClassMap.GetKey();
			for(int i=0;i<keySet.size();i++)
			{
				String concept1=keySet.get(i);
				ArrayList<String> concepts2=resultClassMap.GetKey_Value(concept1);
				String label1=ClassLabel1.get(concept1);
				String label2="";
				for(int j=0;j<concepts2.size();j++)
				{
					label2=label2+ClassLabel2.get(concepts2.get(j))+",";
				}
				label2=label2+"]";
				label2.replace(",]", "]");
				bfw_Result.append(label1+",["+label2+"\n");
			}	
		}
		System.out.println("找到正确的Class个数为："+rightClassNumber);
		bfw_Result.append("找到正确的Class个数为："+rightClassNumber+"\n");
		System.out.println("**************************************");
		bfw_Result.append("**************************************"+"\n");
		ArrayList<String> notFoundPropertyMaps=new ArrayList<String>();//O2的概念
		for(int i=0;i<propertiesMap.size();i++)
		{
			//String part[]=propertiesMap.get(i).split("--");
			String part[]=propertiesMap.get(i).split(",");
			String property1=part[0].toLowerCase();
			String property2=part[1].toLowerCase();
			//System.out.println(property1+","+property2);
			//可能给出的reference alignment是反的
			if(resultPropertyMap.has_relation(property1, property2))
			{
				System.out.println(part[0]+","+part[1]+","+part[2]+" "+" (Right)");
				//System.out.println(propertiesMap.get(i)+" (Right)");
				bfw_Result.append(part[0]+","+part[1]+","+part[2]+" "+" (Right)"+"\n");
				//bfw_Result.append(propertiesMap.get(i)+" (Right)"+"\n");
				rightPropertyNumber++;
				resultPropertyMap.remove(property1, property2);
			}
			else if(resultPropertyMap.has_relation(property2, property1))
			{
				System.out.println(part[0]+","+part[1]+","+part[2]+" "+" (Right)");
				//System.out.println(propertiesMap.get(i)+" (Right)");
				bfw_Result.append(part[0]+","+part[1]+","+part[2]+" "+" (Right)"+"\n");
				//bfw_Result.append(propertiesMap.get(i)+" (Right)"+"\n");
				rightPropertyNumber++;
				resultPropertyMap.remove(property2, property1);
			}
			else
			{
				System.out.println(part[0]+","+part[1]+","+part[2]);
				//System.out.println(propertiesMap.get(i));
				bfw_Result.append(part[0]+","+part[1]+","+part[2]+"\n");
				//bfw_Result.append(propertiesMap.get(i)+"\n");
				notFoundPropertyMaps.add(propertiesMap.get(i));
			}
		}
		if(resultPropertyMap.size()==0)
		{
			System.out.println("属性匹配对均已找到!");
			bfw_Result.append("属性匹配对均已找到!"+"\n");
		}
		else
		{
			System.out.println("未找到的属性匹配对为：");
			bfw_Result.append("未找到的属性匹配对为："+"\n");
			ArrayList<String> result=new ArrayList<String>();
			result=resultPropertyMap.Print_Value();	
			for(String a:result)
			{
				bfw_Result.append(a+"\n");
			}
		}
		System.out.println("找到正确的Property个数为："+rightPropertyNumber);
		P=(rightClassNumber+rightPropertyNumber)/(findClassNumbers+findPropertyNumbers);
		R=(rightClassNumber+rightPropertyNumber)/(alignmentClassNumbers+alignmentPropertyNumbers);
		R=Math.min(R, 1);
		if(P==0&&R==0)
			F1=0;
		else
			F1=(2*P*R)/(P+R);
		System.out.println("==========================================");
		System.out.println("正确率："+P+" "+"	召回率："+R+" "+"	F1值："+F1);
		bfw_Result.append("=========================================="+"\n");
		bfw_Result.append("正确率："+P+" "+"	召回率："+R+" "+"	F1值："+F1+"\n");

	}

}
