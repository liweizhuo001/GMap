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
			String line = lineTxt.trim(); // ȥ���ַ�����λ�Ŀո񣬱�����ո���ɵĴ���
			//line=line.toLowerCase();//ȫ�����Сд
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
		//������ֻ��һ�������Ҳ�Ϊ�յ���������ܵ������ƶ���һ���ģ�����Ľ��ռ䣩
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
		//��ֻ��һ����ţ���������������label

		/*toc = System.currentTimeMillis();
		System.out.println(toc-tic);*/
		ArrayList<String> editSimClass=new ArrayList<String>();
		ArrayList<String> semanticSimClass=new ArrayList<String>();
		ArrayList<String> tfidfSim=new ArrayList<String>();
		HashMap<String,String> classLabels1=new HashMap<String,String>();
		HashMap<String,String> classLabels2=new HashMap<String,String>();

		System.out.println(InstanceSim.get(0));
		boolean labelflag=classes1.size()==classlabel1.size()&&classes2.size()==classlabel2.size();//ֻ����ҽѧ�����вŻ����
		if(labelflag==true)
		{		
			classLabels1=tool.transformToHashMap(classlabel1);//�洢һ�ݶ�Ӧ��ʽ
			classLabels2=tool.transformToHashMap(classlabel2);//�洢һ�ݶ�Ӧ��ʽ
			
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
			System.out.println("��ȵĸ���Ϊ:"+m);
			//editSimClass=tool.editSimClass(classlabel1, classlabel2);
			//newEditSimClass=tool.editSimClass(classlabel1, classlabel2);
			System.out.println("*************************************");
			System.out.println(editSimClass.size());
			
			toc = System.currentTimeMillis();
			System.out.println("�༭�������ƶȼ������ĵ�ʱ��Ϊ��"+(toc-tic));
			bfw_Result.append("�༭�������ƶȼ������ĵ�ʱ��Ϊ��"+(toc-tic)/1000+"s"+"\n");
			//����Ҫ��class--label����һ������,ֻ����label
			
			tic = System.currentTimeMillis();
			semanticSimClass=tool.NewsemanticSimClass3(classlabel1, classlabel2);//����Ҳ�ʵ������������ж���������
			System.out.println("*************************************");
			System.out.println(semanticSimClass.size());
			toc = System.currentTimeMillis();
			System.out.println("�������ƶȼ������ĵ�ʱ��Ϊ��"+(toc-tic));
			bfw_Result.append("�������ƶȼ������ĵ�ʱ��Ϊ��"+(toc-tic)/1000+"s"+"\n");
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

			//��ʱ�俪�������ǵĻ�������2�ߵļ��㣬ֻ����label�滻������
			classlabel1=tool.replaceLabel(classes1,classlabel1);
			classlabel2=tool.replaceLabel(classes2,classlabel2);
			tic = System.currentTimeMillis();
			semanticSimClass=tool.semanticSimClass(classlabel1,classlabel2);//����Ҳ�ʵ������������ж���������
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
		objectproperty_lableflag=objectProperties1.size()==objectPropertieslabel1.size()&&objectProperties2.size()==objectPropertieslabel2.size();//ֻ����ҽѧ�����вŻ����
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
			//ֻ��label�����м���,һ���������ֻ������ҽѧ������
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
			//��label�滻������༭����(�ֿ������̫��)
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
		dataproperty_lableflag=dataProperties1.size()==dataPropertieslabel1.size()&&dataProperties2.size()==dataPropertieslabel2.size();//ֻ����ҽѧ�����вŻ����
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
			//��label�滻������༭����(�ֿ������̫��)
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

		//�Ժ�϶���ѭ��ģʽ(����Ӧ����ArrayList�ĸ�ʽ����д��)
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

		System.out.println("���յĸ���ƥ���Ϊ��");
		for(int i=0;i<classesMap.size();i++)
		{
			System.out.println(classesMap.get(i));	
		}
		System.out.println("�ҵ�����ƥ��Եĸ���Ϊ��"+classesMap.size());

		System.out.println("���յ�����ƥ���Ϊ��");
		for(int i=0;i<propertiesMap.size();i++)
		{
			System.out.println(propertiesMap.get(i));
		}
		System.out.println("�ҵ�����ƥ��Եĸ���Ϊ��"+propertiesMap.size());

		/*ArrayList<String> classAlignments=changeToAlignments(classesMap);
		ArrayList<String> propertyAlignments=changeToAlignments(propertiesMap);*/

		System.out.println("���ø�ϵͳ���� "+ontologyName1+" �� "+ontologyName2+" �Ľ�����£�");
		bfw_Result.append("���ø�ϵͳ���� "+ontologyName1+" �� "+ontologyName2+" �Ľ�����£�"+"\n");
		//printResult(classAlignments,propertyAlignments,resultClassMap,resultPropertyMap);
		if(labelflag==true)
		{
			//ֻ��Ϊ��ӡҽѧ�����ṩ����
			printAnatomyResult(classesMap,propertiesMap,classLabels1,classLabels2,resultClassMap,resultPropertyMap);
		}	
		else
		{
			printResult(classesMap,propertiesMap,resultClassMap,resultPropertyMap);
		}
		System.out.println(ontologyName1+" "+ontologyName2+" has been done!");
		bfw_Result.append("\n\n");
		
		//}//1��ѭ��I3CON

		/* }//2��ѭ��Conference��Benchmark
		}*/
		toc1=System.currentTimeMillis();
		System.out.println("������ʱ��Ϊ��"+(toc1-tic1)/1000+"s");
		bfw_Result.append("������ʱ��Ϊ��"+(toc1-tic1)/1000+"s"+"\n");
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
		if(flag==false)//���������maps��Ϊ�յ�
		{
			for(int i=0;i<hiddenmaps.size();i++)				
			{
				maps.add(hiddenmaps.get(i));
			}
			preciseMaps=tools.keepOneToOneAlignment(maps);//���Class��ʱ������Ҫ�ġ����п��ܳ���1�Զ�����
			maps.clear();
			for(int i=0;i<preciseMaps.size();i++)
			{
				String parts[]=preciseMaps.get(i).split(",");
				if(Double.parseDouble(parts[2])>0)
					maps.add(preciseMaps.get(i));
			}
			return maps;	//ע�⣬���ﷵ�صĸ�ʽ����Ҫ�޸�
		}
		else	//Ĭ��ȱʡʱ��ӵ����
		{
			//Ҫ��1��1��ƥ��,������ͨ��ģ�ͼ�������Ľ���������Ľ��ֻ����Ϊ����
			preciseMaps=tools.keepOneToOneAlignment(hiddenmaps);
			for(int i=0;i<preciseMaps.size();i++)
			{
				boolean index=false;
				String parts1[]=preciseMaps.get(i).split(",");
				for(int j=0;j<maps.size();j++)
				{
					String parts2[]=maps.get(j).split(",");
					if(parts2[0].equals(parts1[0])||parts2[1].equals(parts1[1])||Double.parseDouble(parts1[2])==0)//1��1������
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
					if(length1==length2&&length1==1&&Double.parseDouble(parts1[2])>0.7)//��������ͬ�Ÿ��迼�ǣ���Ϊ�кܶ�WordNet��������һ�����󣬵����ٻ��ʺܵ�
						maps.add(preciseMaps.get(i));
					if((length1==1&&length2>1)||(length1==1&&length2>1))	//����������ϴʵ������
						maps.add(preciseMaps.get(i));
					if(length1>1&&length2>1&&Double.parseDouble(parts1[2])>0.6)	//��ϴʵ�token������2����
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
		if(type.equals("class")&&maps.size()==0)//ClassΪ��ʱ�Ĳ���
		{
			for(int i=0;i<hiddenmaps.size();i++)				
			{
				maps.add(hiddenmaps.get(i));
			}
			preciseMaps=tools.keepOneToOneAlignment(maps);//���Class��ʱ������Ҫ�ġ����п��ܳ���1�Զ�����
			maps.clear();
			for(int i=0;i<preciseMaps.size();i++)
			{
				String parts[]=preciseMaps.get(i).split(",");
				if(Double.parseDouble(parts[2])>0)
					maps.add(preciseMaps.get(i));
			}
			return maps;	//ע�⣬���ﷵ�صĸ�ʽ����Ҫ�޸�
		}
		else if(type.equals("class")&&maps.size()!=0)//Class��Ϊ��ʱ�Ĳ���
		{
			preciseMaps=tools.keepOneToOneAlignment(hiddenmaps);
			for(int i=0;i<preciseMaps.size();i++)
			{
				boolean index=false;
				String parts1[]=preciseMaps.get(i).split(",");
				for(int j=0;j<maps.size();j++)
				{
					String parts2[]=maps.get(j).split(",");
					if(parts2[0].equals(parts1[0])||parts2[1].equals(parts1[1])||Double.parseDouble(parts1[2])==0)//1��1������
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
					if(length1==length2&&length1==1&&Double.parseDouble(parts1[2])>0.7)//��������ͬ�Ÿ��迼�ǣ���Ϊ�кܶ�WordNet��������һ�����󣬵����ٻ��ʺܵ�
						maps.add(preciseMaps.get(i));
					if((length1==1&&length2>1)||(length1>1&&length2==1))	//����������ϴʵ������
						maps.add(preciseMaps.get(i));
					if(length1>1&&length2>1&&Double.parseDouble(parts1[2])>0.6)	//��ϴʵ�token������2����
						maps.add(preciseMaps.get(i));
				}
			}
			return maps;
		}
		else if((type.equals("property")&&maps.size()!=0))//���Բ�Ϊ�յ����
		{
			preciseMaps=tools.keepOneToOneAlignment(hiddenmaps);
			for(int i=0;i<preciseMaps.size();i++)
			{
				boolean index=false;
				String parts1[]=preciseMaps.get(i).split(",");
				for(int j=0;j<maps.size();j++)
				{
					String parts2[]=maps.get(j).split(",");
					if(parts2[0].equals(parts1[0])||parts2[1].equals(parts1[1])||Double.parseDouble(parts1[2])==0)//1��1������
					{
						index=true;
						break;
					}
				}
				if(index==false)//����ͻ��������
					maps.add(preciseMaps.get(i));
			}
			return maps;
		}
		else
			//else if(type.equals("property"))//��Ϊ����϶�Ϊ��,�����Ĺ��̱�����ѭ����Ԥ����һ�ξ���
		{
			for(int i=0;i<hiddenmaps.size();i++)				
			{
				maps.add(hiddenmaps.get(i));
			}
			return maps;	//ע�⣬���ﷵ�صĸ�ʽ����Ҫ�޸�
		}
	}

	public static ArrayList<String> addMaps(ArrayList<String> maps,ArrayList<String> newMaps)//ǰ�����ϵģ��������µ�
	{
		//Sim_Tools tools=new Sim_Tools();
		ArrayList<String> preciseMaps=new ArrayList<String>();	

		for(int i=0;i<newMaps.size();i++)
		{
			preciseMaps.add(newMaps.get(i));
		}
		ArrayList<String> alignments=changeToAlignments(preciseMaps);
		for(int i=0;i<maps.size();i++)//����µ�MapsҪ����ֵ�Ĵ�С,�µı�ԭ����ֵҪ��
		{
			String parts[]=maps.get(i).split(",");
			String conceptPair=parts[0]+"--"+parts[1];
			if(alignments.contains(conceptPair))//��Ϊ�µ�ֵһ����ǰ�ߴ����Կ���pass��
			{
				continue;
			}
			else
				preciseMaps.add(maps.get(i));
		}
		return preciseMaps;	
	}

	public static ArrayList<String> refineClass(ArrayList<String> maps,ArrayList<String>Classes1,ArrayList<String> Classes2)//���㽫һЩ�����̫�ɿ�������û����SPN��Noisy-OR�еļ�ǿ��ƥ��Թ��˵�
	{
		Refine_Tools tools=new Refine_Tools();
		ArrayList<String> lowerCaseClasses1=changeToLowerCase(Classes1);
		ArrayList<String> lowerCaseClasses2=changeToLowerCase(Classes2);
		ArrayList<String> preciseMaps=new ArrayList<String>();
		for(int i=0;i<maps.size();i++)
		{
			String parts[]=maps.get(i).split(",");	
			//��Ϊ���յ�ƥ�䶼ת������Сд�����뻹ԭ֮���֪������ʵ�ĳ���
			int index1=tools.find_index(lowerCaseClasses1, parts[0]);
			int index2=tools.find_index(lowerCaseClasses2, parts[1]);
			int length1=tool.tokeningWord(Classes1.get(index1)).split(" ").length;
			int length2=tool.tokeningWord(Classes2.get(index2)).split(" ").length;
			/*if(length1==length2&&length1==1&&Double.parseDouble(parts[2])>0.7)//��������ͬ�Ÿ��迼�ǣ���Ϊ�кܶ�WordNet��������һ�����󣬵����ٻ��ʺܵ�
				preciseMaps.add(maps.get(i));
			else if((length1==1&&length2>1)||(length1>1&&length2==1)&&Double.parseDouble(parts[2])>=0.6)
				preciseMaps.add(maps.get(i));
			else if(length1>1&&length2>1&&Double.parseDouble(parts[2])>0.6)	//��ϴʵ�token������2����
				preciseMaps.add(maps.get(i));*/
			/*if((length1==1&&length2>1)||(length1>1&&length2==1))	//����������ϴʵ������
				preciseMaps.add(maps.get(i));
			if(length1>1&&length2>1&&Double.parseDouble(parts[2])>0.6)	//��ϴʵ�token������2����
				preciseMaps.add(maps.get(i));*/
			if(length1==length2&&length1==1&&Double.parseDouble(parts[2])>0.7)//��������ͬ�Ÿ��迼�ǣ���Ϊ�кܶ�WordNet��������һ�����󣬵����ٻ��ʺܵ�
				preciseMaps.add(maps.get(i));
			else if(length1==length2&&length1!=1&&Double.parseDouble(parts[2])>0.6)//��������ͬ�Ÿ��迼�ǣ���Ϊ�кܶ�WordNet��������һ�����󣬵����ٻ��ʺܵ�
				preciseMaps.add(maps.get(i));
			else if(length1!=length2&&Double.parseDouble(parts[2])>=0.6)
				preciseMaps.add(maps.get(i));
		}
		return preciseMaps;
	}

	/*public static ArrayList<String> refineProperty(ArrayList<String> maps)//���㽫һЩ�����̫�ɿ�������û����SPN��Noisy-OR�еļ�ǿ��ƥ��Թ��˵�
	{
		ArrayList<String> preciseMaps=new ArrayList<String>();
		for(int i=0;i<maps.size();i++)
		{
			String parts[]=maps.get(i).split(",");
			int length1=tool.tokeningWord(parts[0]).split(" ").length;
			int length2=tool.tokeningWord(parts[1]).split(" ").length;
			if(length1==length2&&length1==1&&Double.parseDouble(parts[2])>0.7)//��������ͬ�Ÿ��迼�ǣ���Ϊ�кܶ�WordNet��������һ�����󣬵����ٻ��ʺܵ�
				preciseMaps.add(maps.get(i));
			if((length1==1&&length2>1)||(length1>1&&length2==1))	//����������ϴʵ������
				preciseMaps.add(maps.get(i));
			if(length1>1&&length2>1&&Double.parseDouble(parts[2])>0.6)	//��ϴʵ�token������2����
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
			if(equivalent.length==3&&equivalent[2].equals("Equal"))  //parts[0]��parts[1]�Ķ���
			{
				int index =findIndex(Map,equivalent[0]);
				if(index!=-1)
				{
					String newMap=Map.get(index).replaceFirst(equivalent[0], equivalent[1]);//�������
					String newMap2=Map.get(index).replaceFirst(equivalent[0].toLowerCase(), equivalent[1].toLowerCase());//�����
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
				if(pos.equals("CD")||pos.equals("NNP"))//���ǵ�����ĸ��д������
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
		System.out.println("�淶������ĸ���Ϊ:"+num);
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
		ArrayList<String> notFoundClassMaps=new ArrayList<String>();//O2�ĸ���
		for(int i=0;i<classesMap.size();i++)
		{
			//String part[]=classesMap.get(i).split("--");
			String part[]=classesMap.get(i).split(",");
			String concept1=part[0].toLowerCase();
			String concept2=part[1].toLowerCase();
			//���ܸ�����reference alignment�Ƿ���
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
			System.out.println("����ƥ��Ծ����ҵ�!");
			bfw_Result.append("����ƥ��Ծ����ҵ�!"+"\n");
		}
		else
		{
			System.out.println("δ�ҵ��ĸ���ƥ���Ϊ��");
			bfw_Result.append("δ�ҵ��ĸ���ƥ���Ϊ��"+"\n");
			ArrayList<String> result=new ArrayList<String>();
			result=resultClassMap.Print_Value();		
			for(String a:result)
			{
				bfw_Result.append(a+"\n");
			}
		}
		System.out.println("�ҵ���ȷ��Class����Ϊ��"+rightClassNumber);
		bfw_Result.append("�ҵ���ȷ��Class����Ϊ��"+rightClassNumber+"\n");
		System.out.println("**************************************");
		bfw_Result.append("**************************************"+"\n");
		ArrayList<String> notFoundPropertyMaps=new ArrayList<String>();//O2�ĸ���
		for(int i=0;i<propertiesMap.size();i++)
		{
			//String part[]=propertiesMap.get(i).split("--");
			String part[]=propertiesMap.get(i).split(",");
			String property1=part[0].toLowerCase();
			String property2=part[1].toLowerCase();
			//System.out.println(property1+","+property2);
			//���ܸ�����reference alignment�Ƿ���
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
			System.out.println("����ƥ��Ծ����ҵ�!");
			bfw_Result.append("����ƥ��Ծ����ҵ�!"+"\n");
		}
		else
		{
			System.out.println("δ�ҵ�������ƥ���Ϊ��");
			bfw_Result.append("δ�ҵ�������ƥ���Ϊ��"+"\n");
			ArrayList<String> result=new ArrayList<String>();
			result=resultPropertyMap.Print_Value();	
			for(String a:result)
			{
				bfw_Result.append(a+"\n");
			}
		}
		System.out.println("�ҵ���ȷ��Property����Ϊ��"+rightPropertyNumber);
		P=(rightClassNumber+rightPropertyNumber)/(findClassNumbers+findPropertyNumbers);
		R=(rightClassNumber+rightPropertyNumber)/(alignmentClassNumbers+alignmentPropertyNumbers);
		R=Math.min(R, 1);
		if(P==0&&R==0)
			F1=0;
		else
			F1=(2*P*R)/(P+R);
		System.out.println("==========================================");
		System.out.println("��ȷ�ʣ�"+P+" "+"	�ٻ��ʣ�"+R+" "+"	F1ֵ��"+F1);
		bfw_Result.append("=========================================="+"\n");
		bfw_Result.append("��ȷ�ʣ�"+P+" "+"	�ٻ��ʣ�"+R+" "+"	F1ֵ��"+F1+"\n");

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
		ArrayList<String> notFoundClassMaps=new ArrayList<String>();//O2�ĸ���
		for(int i=0;i<classesMap.size();i++)
		{
			//String part[]=classesMap.get(i).split("--");
			String part[]=classesMap.get(i).split(",");
			String concept1=part[0].toLowerCase();
			String concept2=part[1].toLowerCase();
			String label1=ClassLabel1.get(concept1);
			String label2=ClassLabel2.get(concept2);
			//���ܸ�����reference alignment�Ƿ���
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
			System.out.println("����ƥ��Ծ����ҵ�!");
			bfw_Result.append("����ƥ��Ծ����ҵ�!"+"\n");
		}
		else
		{
			System.out.println("δ�ҵ��ĸ���ƥ���Ϊ��");
			bfw_Result.append("δ�ҵ��ĸ���ƥ���Ϊ��"+"\n");
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
		System.out.println("�ҵ���ȷ��Class����Ϊ��"+rightClassNumber);
		bfw_Result.append("�ҵ���ȷ��Class����Ϊ��"+rightClassNumber+"\n");
		System.out.println("**************************************");
		bfw_Result.append("**************************************"+"\n");
		ArrayList<String> notFoundPropertyMaps=new ArrayList<String>();//O2�ĸ���
		for(int i=0;i<propertiesMap.size();i++)
		{
			//String part[]=propertiesMap.get(i).split("--");
			String part[]=propertiesMap.get(i).split(",");
			String property1=part[0].toLowerCase();
			String property2=part[1].toLowerCase();
			//System.out.println(property1+","+property2);
			//���ܸ�����reference alignment�Ƿ���
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
			System.out.println("����ƥ��Ծ����ҵ�!");
			bfw_Result.append("����ƥ��Ծ����ҵ�!"+"\n");
		}
		else
		{
			System.out.println("δ�ҵ�������ƥ���Ϊ��");
			bfw_Result.append("δ�ҵ�������ƥ���Ϊ��"+"\n");
			ArrayList<String> result=new ArrayList<String>();
			result=resultPropertyMap.Print_Value();	
			for(String a:result)
			{
				bfw_Result.append(a+"\n");
			}
		}
		System.out.println("�ҵ���ȷ��Property����Ϊ��"+rightPropertyNumber);
		P=(rightClassNumber+rightPropertyNumber)/(findClassNumbers+findPropertyNumbers);
		R=(rightClassNumber+rightPropertyNumber)/(alignmentClassNumbers+alignmentPropertyNumbers);
		R=Math.min(R, 1);
		if(P==0&&R==0)
			F1=0;
		else
			F1=(2*P*R)/(P+R);
		System.out.println("==========================================");
		System.out.println("��ȷ�ʣ�"+P+" "+"	�ٻ��ʣ�"+R+" "+"	F1ֵ��"+F1);
		bfw_Result.append("=========================================="+"\n");
		bfw_Result.append("��ȷ�ʣ�"+P+" "+"	�ٻ��ʣ�"+R+" "+"	F1ֵ��"+F1+"\n");

	}

}
