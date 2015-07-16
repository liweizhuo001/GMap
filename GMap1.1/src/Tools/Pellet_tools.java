package Tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.graph.FrontsNode;
import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.CardinalityRestriction;
import com.hp.hpl.jena.ontology.ComplementClass;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.HasValueRestriction;
import com.hp.hpl.jena.ontology.IntersectionClass;
import com.hp.hpl.jena.ontology.MaxCardinalityRestriction;
import com.hp.hpl.jena.ontology.MinCardinalityRestriction;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class Pellet_tools {
	
/*	 Reasoner reasoner = PelletReasonerFactory.theInstance().create();
	    // create an empty model
	     Model rawModel = ModelFactory.createDefaultModel( );        
	       // create an inferencing model using Pellet reasoner
	     RDF_Model = ModelFactory.createInfModel( reasoner, emptyModel );
	     OntModel model = ModelFactory.createOntologyModel(
	    		 PelletReasonerFactory.THE_SPEC, rawModel);*/
	String OntoID="";
	Reasoner reasoner = PelletReasonerFactory.theInstance().create();
	Model rawModel = ModelFactory.createDefaultModel( );
	OntModel ontology = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, rawModel);
	
	//OntModel ontology = ModelFactory.createOntologyModel();
	
	
	//�жϱ����Ƿ���سɹ�
	public void readOnto(String f)  
	{
		try {
			ontology.read(new FileInputStream(f), "");
			System.out.println("loadOntology");
			String concept="";
			OntClass temp=null;
			ExtendedIterator<OntClass> classlist;
			try
			{
				classlist = ontology.listNamedClasses();
			}
			catch(Exception ex) //this method can be used for loading the information of Website
			{
				System.out.println("this ontology can not be analyzed once by pellet!");
			}
			classlist = ontology.listNamedClasses();
			while(classlist.hasNext()){			//ǰ�����ѭ����Ҫ��Ϊ�˶�λ������
				temp=classlist.next();
				concept=temp.getLocalName().toString();	
				OntoID=temp.getURI().replace(concept, "");
				System.out.println("URL is:"+OntoID);
				break;
			}
		} 
		catch (FileNotFoundException ex) 
		{
			System.out.println("file error!");
		}	
	}
	
	
	public ArrayList<String> GetAllConcept()
	{
		//ע������Ҫ��ִ��readOnto��ȷ���ļ��Ķ���
		ArrayList<String> classes=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //�����ı��������������ʽ����
		OntClass temp ;
		//ArrayList<String> class1=new ArrayList<String>();  
		while(classlist.hasNext()){
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			System.out.println(a);
			if(a.equals("Nothing")||a.equals("Thing"))//���汾��
				continue;
			//ҽѧ����
			else if(a.equals("DbXref")||a.equals("Subset")||a.equals("Synonym")||a.equals("ObsoleteClass")||a.equals("SynonymType")||a.equals("Definition"))
				continue;
			//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
			else
				classes.add(a);
			}
		return classes;
	}
	
	/*public ArrayList<String> GetAllConceptLabel()
	{
		//ע������Ҫ��ִ��readOnto��ȷ���ļ��Ķ���
		ArrayList<String> classes_labels=new ArrayList<String>();
		ArrayList<String> classes=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //�����ı��������������ʽ����
		OntClass temp ;
		int num=0;//ͳ��null�ĸ���
		//ArrayList<String> class1=new ArrayList<String>();  
		while(classlist.hasNext())
	   {
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			String label=temp.getLabel(null);
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			//System.out.println(a);
			System.out.println(label);
			if(a.equals("Nothing")||a.equals("Thing"))//���汾��
				continue;
			//ҽѧ����
			else if(a.equals("DbXref")||a.equals("Subset")||a.equals("Synonym")||a.equals("ObsoleteClass")||a.equals("SynonymType")||a.equals("Definition"))
				continue;
			//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
			if(label!=null&&!label.equals(a))//�����Լ������Լ���label
			//if(label!=null)//�����Լ������Լ���label
				classes_labels.add(label);
			else 
			{
				classes_labels.add(a);
				num++;
			}
			
		}
		if(num<=0.5*classes_labels.size())
			return classes_labels;
		else
			return classes;
	}*/
	
	public ArrayList<String> GetAllConceptLabel()
	{
		//ע������Ҫ��ִ��readOnto��ȷ���ļ��Ķ���
		ArrayList<String> classes_labels=new ArrayList<String>();
		//ArrayList<String> classes=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //�����ı��������������ʽ����
		OntClass temp ;
		//int num=0;//ͳ��null�ĸ���
		//ArrayList<String> class1=new ArrayList<String>();  
		while(classlist.hasNext())
	   {
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			String label=temp.getLabel(null);
			String comment=temp.getComment(null);
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			//System.out.println(a);
			System.out.println(label);
			if(a.equals("Nothing")||a.equals("Thing"))//���汾��
				continue;
			//ҽѧ����
			else if(a.equals("DbXref")||a.equals("Subset")||a.equals("Synonym")||a.equals("ObsoleteClass")||a.equals("SynonymType")||a.equals("Definition"))
				continue;
			//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
			if(label!=null&&!label.equals(a))//�����Լ������Լ���label
				classes_labels.add(a+"--"+label);	
			else if(label!=null&&comment!=null&&!comment.equals(""))  
			{
				classes_labels.add(a+"--"+comment.trim());	
			}
	   }
		return classes_labels;
		
	}
	
	public ArrayList<String> GetAllProperty()
	{
		ArrayList<String> Properties=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist1 = ontology.listObjectProperties(); //�����ı��������������ʽ����
		ObjectProperty temp1 ;	
		while(propertylist1.hasNext()){
			temp1=propertylist1.next();		
			String property=temp1.getLocalName().toString();
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))
				continue;
			Properties.add(property);
		}
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //�����ı��������������ʽ����
		DatatypeProperty temp2 ;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			if(property.equals("topDataProperty")||property.equals("bottomDataProperty"))
				continue;
			Properties.add(property);
		}
		return Properties;
	}
	
	public ArrayList<String> GetObjectProperty()
	{
		ArrayList<String> Properties=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist1 = ontology.listObjectProperties(); //�����ı��������������ʽ����
		ObjectProperty temp1 ;	
		while(propertylist1.hasNext()){
			temp1=propertylist1.next();		
			String property=temp1.getLocalName().toString();
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))//���汾��
				continue;
			//ҽѧ����
			//else if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED")||property.equals("UNDEFINED_part_of"))
			else if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED"))
				continue;
			else
				Properties.add(property);
		}
		return Properties;
	}
	
	public ArrayList<String> GetObjectPropertyLabel()
	{
		ArrayList<String> PropertiesLabel=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist1 = ontology.listObjectProperties(); //�����ı��������������ʽ����
		ObjectProperty temp1 ;	
		while(propertylist1.hasNext()){
			temp1=propertylist1.next();		
			String property=temp1.getLocalName().toString();
			String label=temp1.getLabel(null);
			String comment=temp1.getComment(null);
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))//���汾��
				continue;
			//ҽѧ����
			//else if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED")||property.equals("UNDEFINED_part_of"))
			else if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED"))
				continue;
			if(label!=null&&!tokeningWord(label).equals(tokeningWord(property)))
				PropertiesLabel.add(property+"--"+label);
			else if(comment!=null&&!comment.equals(""))  
			{
				PropertiesLabel.add(property+"--"+comment.trim());	
			}
			
		}
		return PropertiesLabel;
	}
	
	public ArrayList<String> GetDataProperty()
	{
		ArrayList<String> Properties=new ArrayList<String>();
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //�����ı��������������ʽ����
		DatatypeProperty temp2 ;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			if(property.equals("topDataProperty")||property.equals("bottomDataProperty"))
				continue;
			Properties.add(property);
		}
		return Properties;
	}
	
	public ArrayList<String> GetDataPropertyLabel()
	{
		ArrayList<String> PropertiesLabel=new ArrayList<String>();
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //�����ı��������������ʽ����
		DatatypeProperty temp2 ;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			String label=temp2.getLabel(null);
			String comment=temp2.getComment(null);
			if(property.equals("topDataProperty")||property.equals("bottomDataProperty"))
				continue;
			if(label!=null&&!tokeningWord(label).equals(tokeningWord(property)))
				PropertiesLabel.add(property+"--"+label);
			else if(comment!=null&&!comment.equals(""))  
			{
				PropertiesLabel.add(property+"--"+comment.trim());	
			}
		}
		return PropertiesLabel;
	}
	
	public ArrayList<String> GetPropertyAndInverse()
	{
		ArrayList<String> propertiesAndInverse=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist1 = ontology.listObjectProperties(); //�����ı��������������ʽ����
		ObjectProperty temp1 ;	
		while(propertylist1.hasNext()){
			temp1=propertylist1.next();		
			String property=temp1.getLocalName().toString();
			if(temp1.getInverse()!=null)
			{
				String inverseProperty=temp1.getInverse().getLocalName().toString();
				propertiesAndInverse.add(property+"--"+inverseProperty);
			}
		}
		//DataProperty������Inverse
		/*ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //�����ı��������������ʽ����
		DatatypeProperty temp2 ;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			if(temp2.getInverse()!=null)
			{
				String inverseProperty=temp2.getLocalName().toString();
				propertiesAndInverse.add(property+"--"+inverseProperty);
			}
		}*/
		return propertiesAndInverse;
	}
	
	public ArrayList<String> GetAllRelations()//����ֻ��ȡobjectproperty�Ĺ�ϵ
	{
		ArrayList<String> Relations=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist = ontology.listObjectProperties(); //�����ı��������������ʽ����
		ObjectProperty temp ;
		while(propertylist.hasNext()){
			temp=propertylist.next();			
			String property=temp.getLocalName().toString();
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))
				continue;
			String subject="";
			String object="";
		//	System.out.println("************************");
		//	System.out.println(property);		
            /*ExtendedIterator<? extends OntResource> Domains=temp.listDomain();
			ExtendedIterator<? extends OntResource> Ranges=temp.listRange();*/
        	//System.out.println(temp.getDomain().isAnon());//�жϸýڵ��Ƿ���һ��RDF Node
			//����ȡʽ�������صĺ������
			// ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();	
			//������ֵ�򶼲�Ϊ��
			if(temp.getDomain()!=null&&temp.getRange()!=null)
			{
				if(!temp.getDomain().isAnon()&&!temp.getRange().isAnon())
				{
					subject=temp.getDomain().getLocalName();
					object=temp.getRange().getLocalName();
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);			 
				}

				else if(temp.getDomain().isAnon()&&!temp.getRange().isAnon())
				{
					object=temp.getRange().getLocalName();
					//����������������ȡ��or�����ڵ����
					ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();				 
					while (PropertyDomains.hasNext()) 
					{					
						subject=PropertyDomains.next().getLocalName();		
						//	System.out.println(subject);				
						String Triple=subject+","+property+","+object;
						System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}

				else if(!temp.getDomain().isAnon()&&temp.getRange().isAnon())
				{
					subject=temp.getDomain().getLocalName();
					//���ֵ���������ȡ��or�����ڵ����
					ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
					while (PropertyRanges.hasNext()) 
					{					
						object=PropertyRanges.next().getLocalName();		
						//	System.out.println(subject);				
						String Triple=subject+","+property+","+object;
						System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}

				else if(temp.getDomain().isAnon()&&temp.getRange().isAnon()) //��������д�����������
				{		
					//���������ֵ�򶼴�����ȡ��or�����ڵ����
					/*Property prop =temp; 	//temp ��һ������
					ExtendedIterator<? extends OntClass> ConcetpDomains = ((OntProperty)prop).getDomain().asClass().asUnionClass().listOperands();*/
					ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();
					ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();
					while (PropertyDomains.hasNext()) 
					{		
						subject=PropertyDomains.next().getLocalName();		
						//		System.out.println(subject);
						while(PropertyRanges.hasNext())
						{
							object=PropertyRanges.next().getLocalName();
							//			System.out.println(object);
							String Triple=subject+","+property+","+object;
							System.out.println(Triple);
							if(!Relations.contains(Triple))
								Relations.add(Triple);
						}			
					}
				}		 			 
			}			
			//������Ϊ�ա�ֵ��Ϊ��
			 if(temp.getDomain()!=null&&temp.getRange()==null)
			{
				 if(!temp.getDomain().isAnon())
				 {
					subject=temp.getDomain().getLocalName();
					object=null;
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);			 
				 }
				 	
				 else if(temp.getDomain().isAnon())
				{
					 object=null;
					 //����������������ȡ��or�����ڵ����
					 ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();				 
					 while (PropertyDomains.hasNext()) 
					 {					
						subject=PropertyDomains.next().getLocalName();		
					//	System.out.println(subject);				
						String Triple=subject+","+property+","+object;
						System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					 }
				}			
			}			 
				//������Ϊ�ա�ֵ��Ϊ��
			 if(temp.getDomain()==null&&temp.getRange()!=null)
			{
				 if(!temp.getRange().isAnon())
				 {
					subject=null;
					object=temp.getRange().getLocalName();
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);			 
				 }
				 	
				 else if(temp.getRange().isAnon())
					{
						 subject=null;
						 //���ֵ���������ȡ��or�����ڵ����
						 ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
						 while (PropertyRanges.hasNext()) 
						 {					
							object=PropertyRanges.next().getLocalName();		
						//	System.out.println(subject);				
							String Triple=subject+","+property+","+object;
							System.out.println(Triple);
							Relations.add(Triple);
						 }
					}			
			}
			 
		}	//while propertylist.hasNext() �����Ƕ�Objectproperties
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");		
/*		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //�����ı��������������ʽ����
		DatatypeProperty temp2 ;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			System.out.println(property);
		}*/
		
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //�����ı��������������ʽ����
		DatatypeProperty temp2;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			if(property.equals("topDataProperty")||property.equals("bottomDataProperty"))
				continue;
			String subject="";
			String object="";
			System.out.println("####################################");
			//System.out.println(property);			
			if(temp2.getDomain()!=null&&temp2.getRange()!=null)
			{
				if(!temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
				{
					subject=temp2.getDomain().getLocalName();
					object=temp2.getRange().getLocalName();
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);			 
				}

				else if(temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
				{
					object=temp2.getRange().getLocalName();
					//����������������ȡ��or�����ڵ����
					ExtendedIterator<? extends OntClass> PropertyDomains = temp2.getDomain().asClass().asUnionClass().listOperands();				 
					while (PropertyDomains.hasNext()) 
					{					
						subject=PropertyDomains.next().getLocalName();		
						//	System.out.println(subject);				
						String Triple=subject+","+property+","+object;
						System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}
			}		 
		}	
		return Relations;
	}
	
	public ArrayList<String> GetAll_Infered_Relations()//����ֻ��ȡ���ֹ�ϵ����ȡ��������صķ���
	{
		ArrayList<String> Relations=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist = ontology.listObjectProperties(); //�����ı��������������ʽ����
		ObjectProperty temp ;
		while(propertylist.hasNext()){
			temp=propertylist.next();			
			String property=temp.getLocalName().toString();
			//�г����Ķ������ֵ����з�����������࣬��������Ԫ��ļ���
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))
				continue;
			String subject="";
			String object="";
			if(temp.getDomain()!=null&&temp.getRange()!=null)
			{
			 if(!temp.getDomain().isAnon()&&!temp.getRange().isAnon())
			 {
				subject=temp.getDomain().getLocalName();
				object=temp.getRange().getLocalName();
				String Triple=subject+","+property+","+object;
				//System.out.println(subject+","+property+","+object);
				if(!Relations.contains(Triple))
					Relations.add(Triple);				
				//���ڶ�������ֵ�����չ
				//ArrayList<String> subject_children=new ArrayList<String>();
				/*ArrayList<String> subject_children=FindConcept_children(subject);
				ArrayList<String> object_children=FindConcept_children(object);			
				for(String a :subject_children)
				{
					for(String b:object_children)
					{
						String Triple=a+","+property+","+b;
						//System.out.println(Triple);
						Relations.add(Triple);
					}
				}*/					 
			 }		 	
			 else if(temp.getDomain().isAnon()&&!temp.getRange().isAnon())
			{
				 object=temp.getRange().getLocalName();
				 //����������������ȡ��or�����ڵ����
				 ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();				 
				 while (PropertyDomains.hasNext()) 
				 {					
					subject=PropertyDomains.next().getLocalName();		
				//	System.out.println(subject);
					String Triple=subject+","+property+","+object;
					//System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);
					//���ڶ�������ֵ�����չ
					/*ArrayList<String> subject_children=FindConcept_children(subject);
					ArrayList<String> object_children=FindConcept_children(object);
					subject_children.add(subject);
					object_children.add(object);
					for(String a :subject_children)
					{
						for(String b:object_children)
						{
							String Triple=a+","+property+","+b;
							//System.out.println(Triple);
							Relations.add(Triple);
						}
					}*/
				 }
			}			 
			 else if(!temp.getDomain().isAnon()&&temp.getRange().isAnon())
			{
				 subject=temp.getDomain().getLocalName();
				 //���ֵ���������ȡ��or�����ڵ����
				 ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
				 while (PropertyRanges.hasNext()) 
				 {					
					object=PropertyRanges.next().getLocalName();		
				//	System.out.println(subject);				
					String Triple=subject+","+property+","+object;
					//System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);
					//���ڶ�������ֵ�����չ
					/*ArrayList<String> subject_children=FindConcept_children(subject);
					ArrayList<String> object_children=FindConcept_children(object);
					subject_children.add(subject);
					object_children.add(object);
					for(String a :subject_children)
					{
						for(String b:object_children)
						{
							String Triple=a+","+property+","+b;
							//System.out.println(Triple);
							Relations.add(Triple);
						}
					}*/
				 }
			}
			 
			 else if(temp.getDomain().isAnon()&&temp.getRange().isAnon()) //��������д�����������
				{		
					//���������ֵ�򶼴�����ȡ��or�����ڵ����
					/*Property prop =temp; 	//temp ��һ������
					ExtendedIterator<? extends OntClass> ConcetpDomains = ((OntProperty)prop).getDomain().asClass().asUnionClass().listOperands();*/
					ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();
					ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();
					 while (PropertyDomains.hasNext()) 
					 {		
						subject=PropertyDomains.next().getLocalName();		
				//		System.out.println(subject);
						while(PropertyRanges.hasNext())
						{
							object=PropertyRanges.next().getLocalName();
				//			System.out.println(object);
							String Triple=subject+","+property+","+object;
							//System.out.println(Triple);
							if(!Relations.contains(Triple))
								Relations.add(Triple);
							//���ڶ�������ֵ�����չ
							/*ArrayList<String> subject_children=FindConcept_children(subject);
							ArrayList<String> object_children=FindConcept_children(object);
							subject_children.add(subject);
							object_children.add(object);
							for(String a :subject_children)
							{
								for(String b:object_children)
								{
									String Triple=a+","+property+","+b;
									//System.out.println(Triple);
									Relations.add(Triple);
								}
							}*/
						}			
					 }
				}		 			 
			}						 
		}	//while propertylist.hasNext() �����Ƕ�Objectproperties
		
		//System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");		
		
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //�����ı��������������ʽ����
		DatatypeProperty temp2;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			if(property.equals("topDataProperty")||property.equals("bottomDataProperty"))
				continue;
			String subject="";
			String object="";
			//System.out.println("####################################");
			//System.out.println(property);			
			if(temp2.getDomain()!=null&&temp2.getRange()!=null)
			{
				if(!temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
				{
					subject=temp2.getDomain().getLocalName();
					object=temp2.getRange().getLocalName();
					/*String Triple=subject+","+property+","+object;
				System.out.println(Triple);
				Relations.add(Triple);	*/		
					//���ڶ�������ֵ�����չ
					ArrayList<String> subject_children=findConceptChildrenforDatatype(subject);
					//subject_children.add(subject);
					for(String a :subject_children)
					{
						String Triple=a+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}

				else if(temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
				{
					object=temp2.getRange().getLocalName();
					//����������������ȡ��or�����ڵ����
					ExtendedIterator<? extends OntClass> PropertyDomains = temp2.getDomain().asClass().asUnionClass().listOperands();				 
					while (PropertyDomains.hasNext()) 
					{					
						subject=PropertyDomains.next().getLocalName();		
						//	System.out.println(subject);				
						/*String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					Relations.add(Triple);*/
						//���ڶ�������ֵ�����չ
						ArrayList<String> subject_children=findConceptChildrenforDatatype(subject);
						//subject_children.add(subject);
						for(String a :subject_children)
						{
							String Triple=a+","+property+","+object;
							//System.out.println(Triple);
							if(!Relations.contains(Triple))
								Relations.add(Triple);
						}
					}
				}	 			 
			}
		}		
		//ò��pellel�Լ��������ⲿ�ֹ����ˡ�
		/*ArrayList<String> enhancedRelations=enhancedInverseRelations(Relations);
		return enhancedRelations;*/
		return Relations;
	}
	
	public ArrayList<String> GetDataPropertyRelations()//����ֻ��ȡdataproperty�Ĺ�ϵ
	{
		ArrayList<String> Relations=new ArrayList<String>();
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //�����ı��������������ʽ����
		DatatypeProperty temp2;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			if(property.equals("topDataProperty")||property.equals("bottomDataProperty"))
				continue;
			String subject="";
			String object="";
			//System.out.println("####################################");
			//System.out.println(property);			
			if(temp2.getDomain()!=null&&temp2.getRange()!=null)
			{
				//������ֵ���Ƿ�������
				if(!temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
				{
					subject=temp2.getDomain().getLocalName();
					object=temp2.getRange().getLocalName();
					ArrayList<String> subject_children=findConceptChildrenforDatatype(subject);//���ڶ�������ֵ�����չ
					subject_children.add(subject);
					for(String a :subject_children)
					{
						String Triple=a+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}					
				}
				else if(temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
				{
					object=temp2.getRange().getLocalName();
					//����������������ȡ��or�����ڵ����
					ExtendedIterator<? extends OntClass> PropertyDomains = temp2.getDomain().asClass().asUnionClass().listOperands();				 
					while (PropertyDomains.hasNext()) 
					{							
							subject=PropertyDomains.next().getLocalName();		
							ArrayList<String> subject_children=findConceptChildrenforDatatype(subject);//���ڶ�������ֵ�����չ
							subject_children.add(subject);
							for(String a :subject_children)
							{
								String Triple=a+","+property+","+object;
								if(!Relations.contains(Triple))
									Relations.add(Triple);
							}
						
					}
				}			 
			}
		}		
		//ò��pellel�Լ��������ⲿ�ֹ����ˡ�
		/*ArrayList<String> enhancedRelations=enhancedInverseRelations(Relations);
		return enhancedRelations;*/
		return Relations;
	}
	
	public ArrayList<String> GetObjectRelations()//����ֻ��ȡobjectproperty�Ĺ�ϵ
	{
		ArrayList<String> Relations=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist = ontology.listObjectProperties(); //�����ı��������������ʽ����
		ObjectProperty temp ;
		while(propertylist.hasNext()){
			temp=propertylist.next();			
			String property=temp.getLocalName().toString();
			//�г����Ķ������ֵ����з�����������࣬��������Ԫ��ļ���
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))
				continue;
			String subject="";
			String object="";
			if(temp.getDomain()!=null&&temp.getRange()!=null)
			{
				if(!temp.getDomain().isAnon()&&!temp.getRange().isAnon())
				{
					subject=temp.getDomain().getLocalName();
					object=temp.getRange().getLocalName();
					String Triple=subject+","+property+","+object;
					//System.out.println(subject+","+property+","+object);
					if(!Relations.contains(Triple))
						Relations.add(Triple);					 
				}		 	
				else if(temp.getDomain().isAnon()&&!temp.getRange().isAnon())
				{
					object=temp.getRange().getLocalName();
					//����������������ȡ��or�����ڵ����
					ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();				 
					while (PropertyDomains.hasNext()) 
					{					
						subject=PropertyDomains.next().getLocalName();		
						String Triple=subject+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}			 
				else if(!temp.getDomain().isAnon()&&temp.getRange().isAnon())
				{
					subject=temp.getDomain().getLocalName();
					//���ֵ���������ȡ��or�����ڵ����
					ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
					while (PropertyRanges.hasNext()) 
					{					
						object=PropertyRanges.next().getLocalName();						
						String Triple=subject+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}

				else if(temp.getDomain().isAnon()&&temp.getRange().isAnon()) //��������д�����������
				{		
					//���������ֵ�򶼴�����ȡ��or�����ڵ����
					/*Property prop =temp; 	//temp ��һ������
					ExtendedIterator<? extends OntClass> ConcetpDomains = ((OntProperty)prop).getDomain().asClass().asUnionClass().listOperands();*/
					ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();
					ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();
					while (PropertyDomains.hasNext()) 
					{		
						subject=PropertyDomains.next().getLocalName();		
						//		System.out.println(subject);
						while(PropertyRanges.hasNext())
						{
							object=PropertyRanges.next().getLocalName();
							String Triple=subject+","+property+","+object;
							System.out.println(Triple);
							if(!Relations.contains(Triple))
								Relations.add(Triple);
						}			
					}
				}		 			 
			}
			else if(temp.getDomain()==null&&temp.getRange()!=null)//Ǳ�ڵĹ�ϵ�������ܹ����õȼ۹�ϵ�������
			{
				if(temp.getRange().isAnon())//ֵ����������
				{
					 subject=null;
					 //���ֵ���������ȡ��or�����ڵ����
					 ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
					 while (PropertyRanges.hasNext()) 
					 {					
						object=PropertyRanges.next().getLocalName();
						/*ArrayList<String> subject_children=findConceptChildrenforObjcet(subject);//���ڶ�������ֵ�����չ
						//֤�������ǽ�Ϊ��ϸ�Ŀ��ǹ�ֵ��
						subject_children.add(subject);
						for(String a :subject_children)
						{
							String Triple=a+","+property+","+object;
							//System.out.println(Triple);
							Relations.add(Triple);
						}*/					
						String Triple=subject+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					 }
				}
				else //ֵ���ǵ�����
				{
					subject=null;
					object=temp.getRange().getLocalName();
					/*ArrayList<String> object_children=findConceptChildrenforObjcet(object);//���ڶ�������ֵ�����չ
					object_children.add(object);
					for(String a :object_children)
					{
						String Triple=subject+","+property+","+a;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}*/
					String Triple=subject+","+property+","+object;
					if(!Relations.contains(Triple))
						Relations.add(Triple);
				}
			}
		}	//while propertylist.hasNext() �����Ƕ�Objectproperties
		return Relations;
	}
	
	public ArrayList<String> New_GetObjectRelations()//����ֻ��ȡobjectproperty�Ĺ�ϵ
	{
		ArrayList<String> Relations=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist = ontology.listObjectProperties(); //�����ı��������������ʽ����
		ObjectProperty temp ;
		while(propertylist.hasNext()){
			temp=propertylist.next();			
			String property=temp.getLocalName().toString();
			System.out.println(property);
			//�г����Ķ������ֵ����з�����������࣬��������Ԫ��ļ���
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))
				continue;
			String subject="";
			String object="";
			if(temp.getDomain()!=null&&temp.getRange()!=null)
			{
			 if(!temp.getDomain().isAnon()&&!temp.getRange().isAnon())
			 {
				subject=temp.getDomain().getLocalName();
				object=temp.getRange().getLocalName();
				ArrayList<String> subject_children=findConceptChildrenforObjcet(subject);//���ڶ�������ֵ�����չ
				subject_children.add(subject);
				for(String a :subject_children)
				{
					String Triple=a+","+property+","+object;
					//System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);
				}
				ArrayList<String> object_children=findConceptChildrenforObjcet(object);//���ڶ�������ֵ�����չ
				object_children.add(object);
				for(String a :object_children)
				{
					String Triple=subject+","+property+","+a;
					//System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);
				}
				/*String Triple=subject+","+property+","+object;
				//System.out.println(subject+","+property+","+object);
				Relations.add(Triple);	*/				 
			 }		 	
			 else if(temp.getDomain().isAnon()&&!temp.getRange().isAnon())
			{
				 object=temp.getRange().getLocalName();
				 //����������������ȡ��or�����ڵ����
				 ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();				 
				 while (PropertyDomains.hasNext()) 
				 {					
					subject=PropertyDomains.next().getLocalName();	
					//֤�������ǽ�Ϊ��ϸ�Ŀ��ǹ�������
					ArrayList<String> object_children=findConceptChildrenforObjcet(object);//���ڶ�������ֵ�����չ
					object_children.add(object);
					for(String a :object_children)
					{
						String Triple=subject+","+property+","+a;
						if(!Relations.contains(Triple))
							Relations.add(Triple);
						//System.out.println(Triple);
					}
					
					/*String Triple=subject+","+property+","+object;
					//System.out.println(Triple);
					Relations.add(Triple);*/
				 }
			}			 
			 else if(!temp.getDomain().isAnon()&&temp.getRange().isAnon())
			{
				 subject=temp.getDomain().getLocalName();
				 //���ֵ���������ȡ��or�����ڵ����
				 ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
				 while (PropertyRanges.hasNext()) 
				 {					
					object=PropertyRanges.next().getLocalName();
					ArrayList<String> subject_children=findConceptChildrenforObjcet(subject);//���ڶ�������ֵ�����չ
					//֤�������ǽ�Ϊ��ϸ�Ŀ��ǹ�ֵ��
					subject_children.add(subject);
					for(String a :subject_children)
					{
						String Triple=a+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
					
					/*String Triple=subject+","+property+","+object;
					//System.out.println(Triple);
					Relations.add(Triple);*/
				 }
			}
			 
			 else if(temp.getDomain().isAnon()&&temp.getRange().isAnon()) //��������д�����������
				{		
					//���������ֵ�򶼴�����ȡ��or�����ڵ����
					/*Property prop =temp; 	//temp ��һ������
					ExtendedIterator<? extends OntClass> ConcetpDomains = ((OntProperty)prop).getDomain().asClass().asUnionClass().listOperands();*/
					ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();
					ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();
					 while (PropertyDomains.hasNext()) 
					 {		
						subject=PropertyDomains.next().getLocalName();		
				//		System.out.println(subject);
						while(PropertyRanges.hasNext())
						{
							object=PropertyRanges.next().getLocalName();
							String Triple=subject+","+property+","+object;
							System.out.println(Triple);
							if(!Relations.contains(Triple))
								Relations.add(Triple);
						}			
					 }
				}		 			 
			}
			else if(temp.getDomain()==null&&temp.getRange()!=null)//Ǳ�ڵĹ�ϵ�������ܹ����õȼ۹�ϵ�������
			{
				if(temp.getRange().isAnon())//ֵ����������
				{
					 subject=null;
					 //���ֵ���������ȡ��or�����ڵ����
					 ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
					 while (PropertyRanges.hasNext()) 
					 {					
						object=PropertyRanges.next().getLocalName();
						/*ArrayList<String> subject_children=findConceptChildrenforObjcet(subject);//���ڶ�������ֵ�����չ
						//֤�������ǽ�Ϊ��ϸ�Ŀ��ǹ�ֵ��
						subject_children.add(subject);
						for(String a :subject_children)
						{
							String Triple=a+","+property+","+object;
							//System.out.println(Triple);
							Relations.add(Triple);
						}*/					
						String Triple=subject+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					 }
				}
				else //ֵ���ǵ�����
				{
					subject=null;
					object=temp.getRange().getLocalName();
					ArrayList<String> object_children=findConceptChildrenforObjcet(object);//���ڶ�������ֵ�����չ
					object_children.add(object);
					for(String a :object_children)
					{
						String Triple=subject+","+property+","+a;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}
			}
		}	//while propertylist.hasNext() �����Ƕ�Objectproperties
		return Relations;
	}
	//���������д��ڿ����pair�����м���
	public  ArrayList<String> enhancedInverseRelations(ArrayList<String> Relations)
	{
		System.out.println("*********************************");
		int m=0;
		ArrayList<String> propertiesInverse=GetPropertyAndInverse();
		//TreeMap_Tools inversrPair=new TreeMap_Tools(propertiesInverse);
		for(int i=0;i<propertiesInverse.size();i++)
		{
			String pairs[]=propertiesInverse.get(i).split("--");
			ArrayList<String> Triples =findIndexOfRelation(Relations,pairs[0]);//��ʵ3Ԫ��������������
			for(int j=0;j<Triples.size();j++)
			{
				String part[]=Triples.get(j).split(",");
				String inverseTriple=part[2]+","+pairs[1]+","+part[0];
				//System.out.println(Relations.contains(inverseTriple));
				if(!Relations.contains(inverseTriple))
				{
					Relations.add(inverseTriple);
					System.out.println(inverseTriple);
					m++;
				}
			}
		}
		System.out.println("����ӵ���Ԫ�������Ϊ: "+m);
		return Relations;
	}
	
	public  ArrayList<String> enhancedSubClasses(ArrayList<String> Subclasses,ArrayList<String> EquivalentClass)
	{
		System.out.println("*********************************");
		int m=0,n=0;
		for(int i=0;i<EquivalentClass.size();i++)
		{
			String pairs[]=EquivalentClass.get(i).split(",");
			if(pairs.length==3&&pairs[2].equals("sub"))  //parts[0]��parts[1]�Ķ���
			{
				int index =findIndexClass(Subclasses,pairs[1]);
				if(index!=-1)//ɾ��ԭ���ģ�����µ�
				{
					String parts1[]=Subclasses.get(index).split("--");
					String parts2[]=parts1[1].split(",");
					boolean flag=false;
					for(int k=0;k<parts2.length;k++)
					{
						if(parts2[k].equals(pairs[0]))
						{
							flag=true;
							break;
						}
							
					}
					if(flag==false)
					{
						String newDisjointPair=Subclasses.get(index)+","+pairs[0];
						//System.out.println(Classes_Disjoint.get(index));
						//System.out.println(newDisjointPair);
						Subclasses.remove(index);
						Subclasses.add(newDisjointPair);
						m++;
					}			
				}
				else  //ֱ������µģ������������Ļ�������Ҫ����Ӧ������������
				{
					String sub=pairs[1]+"--"+pairs[0];
					Subclasses.add(sub);
					n++;
				}			
			}		
		}
		System.out.println("���ĸ��ӹ�ϵ������Ϊ: "+m);
		System.out.println("����ӵĸ��ӹ�ϵ������Ϊ: "+n);
		return Subclasses;
	}
	
	public  ArrayList<String> enhancedSuperClasses(ArrayList<String> Superclasses,ArrayList<String> EquivalentClass)
	{
		System.out.println("*********************************");
		int m=0,n=0;
		for(int i=0;i<EquivalentClass.size();i++)
		{
			String pairs[]=EquivalentClass.get(i).split(",");
			if(pairs.length==3&&pairs[2].equals("sub"))  //parts[0]��parts[1]�Ķ���
			{
				int index =findIndexClass(Superclasses,pairs[0]);
				if(index!=-1)//ɾ��ԭ���ģ�����µ�
				{
					String parts1[]=Superclasses.get(index).split("--");
					String parts2[]=parts1[1].split(",");
					boolean flag=false;
					for(int k=0;k<parts2.length;k++)
					{
						if(parts2[k].equals(pairs[1]))
						{
							flag=true;
							break;
						}
							
					}
					if(flag==false)
					{
						String newDisjointPair=Superclasses.get(index)+","+pairs[1];
						//System.out.println(Classes_Disjoint.get(index));
						//System.out.println(newDisjointPair);
						Superclasses.remove(index);
						Superclasses.add(newDisjointPair);
						m++;
					}			
				}
				else  //ֱ������µģ������������Ļ�������Ҫ����Ӧ������������
				{
					String sub=pairs[0]+"--"+pairs[1];
					Superclasses.add(sub);
					n++;
				}			
			}		
		}
		System.out.println("�����Ӹ���ϵ������Ϊ: "+m);
		System.out.println("����ӵ��Ӹ���ϵ������Ϊ: "+n);
		return Superclasses;
	}
	
	public  ArrayList<String> enhancedClassesDisjoint(ArrayList<String> Classes_Disjoint,ArrayList<String> Subclasses,ArrayList<String> EquivalentClass)
	{
		System.out.println("*********************************");
		int m=0,n=0;
		for(int i=0;i<EquivalentClass.size();i++)
		{
			String pairs[]=EquivalentClass.get(i).split(",");
			if(pairs.length==3&&pairs[2].equals("not"))  //parts[0]��parts[1]�Ķ���
			{
				int index =findIndexClass(Classes_Disjoint,pairs[0]);
				if(index!=-1)//ɾ��ԭ���ģ�����µ�
				{
					String parts1[]=Classes_Disjoint.get(index).split("--");
					String parts2[]=parts1[1].split(",");
					boolean flag=false;
					for(int k=0;k<parts2.length;k++)
					{
						if(parts2[k].equals(pairs[1]))
						{
							flag=true;
							break;
						}
							
					}
					if(flag==false)
					{
						String newDisjointPair=Classes_Disjoint.get(index)+","+pairs[1];
						//System.out.println(Classes_Disjoint.get(index));
						//System.out.println(newDisjointPair);
						Classes_Disjoint.remove(index);
						Classes_Disjoint.add(newDisjointPair);
						m++;
					}		
				}
				else  //ֱ������µģ������������Ļ�������Ҫ����Ӧ������������
				{
					int indexSub1 =findIndexClass(Subclasses,pairs[0]);
					int indexSub2 =findIndexClass(Subclasses,pairs[1]);
					if(indexSub2!=-1)	//pairs[1]��Ҷ�ӽ��
					{
						String disjoint2=Subclasses.get(indexSub2).replace("--", ",");//ֱ�ӿ�����pairs[1]��subClass����Ϣ����pairs[1]���Լ�����pairs[0]��disjoint��Ϣ�ˡ�
						String newdisjoint1=pairs[0]+"--"+disjoint2;
						Classes_Disjoint.add(newdisjoint1);
					}
					else  //pairs[1]��Ҷ�ӽ�㣬���������������
					{
						String newdisjoint1=pairs[0]+"--"+pairs[1];
						Classes_Disjoint.add(newdisjoint1);
					}
					if(indexSub1!=-1) //pairs[0]��Ҷ�ӽ��
					{
						String disjoint1=Subclasses.get(indexSub1).replace("--", ",");
						String newdisjoint2=pairs[1]+"--"+disjoint1;
						Classes_Disjoint.add(newdisjoint2);
					}
					else  //pairs[0]��Ҷ�ӽ�㣬���������������
					{
						String newdisjoint2=pairs[1]+"--"+pairs[0];
						Classes_Disjoint.add(newdisjoint2);
					}
					n++;
				}
			
			}			
		}
		System.out.println("���Ĳ��뽻������Ϊ: "+m);
		System.out.println("����ӵĲ��뽻������Ϊ: "+n);
		return Classes_Disjoint;
	}
	
	public  ArrayList<String> enhancedRelation(ArrayList<String> ObjectRelations,ArrayList<String> EquivalentClass)
	{
		System.out.println("*********************************");
		int m=0,n=0;
		for(int i=0;i<EquivalentClass.size();i++)
		{
			String pairs[]=EquivalentClass.get(i).split(",");
			if(pairs.length==4&&pairs[2].equals("some"))  //parts[0] ObjectProperty some parts[1]
			{
				ArrayList<String> triple=new ArrayList<String>();
				triple =findIndexOfRelation(ObjectRelations,pairs[1]);//��λ����,����Ԫ����Բ�ֹһ������ֻ�ж�����Ϊnull���߲�Ϊnull�������
				//boolean flag1=false;//���������Ԫ��Ķ�����Ϊ�յ����
				boolean flag2=false;//���������Ԫ��Ķ�����Ϊ�գ�������ȵ����
				for(int j=0;j<triple.size();j++)
				{
					String parts1[]=triple.get(j).split(",");
					/*for(String a:parts1)
					{
						System.out.println(a+" ");
					}
					System.out.println();*/
					
					int index=Integer.parseInt(parts1[3]);
					String domain=parts1[0];
					if(domain.equals("null"))//������Ϊ�յ���Ԫ���ϵ
					{
						String orginalRelation[]=ObjectRelations.get(index).split(",");
						String newRelation=pairs[0]+","+orginalRelation[1]+","+orginalRelation[2];
						ObjectRelations.remove(index);
						ObjectRelations.add(newRelation);
						if(!ObjectRelations.contains(newRelation))
						{
							System.out.println("�޸ĵ���Ԫ��Ϊ: "+newRelation);
							m++;
						}
						//flag1=true;//ȷʵ��Ϊ�յ����
					}
					else if(!domain.equals("null")&&pairs[0].equals(domain))
					{
						flag2=true;//������ȵ����
						break;
					}
					
				}
				if(flag2==false)//ȷʵ�������domain��Ϊ0
				{
					triple =findIndexOfRelation(ObjectRelations,pairs[1]);//���¼���һ��
					for(int j=0;j<triple.size();j++)
					{
						String parts2[]=triple.get(j).split(",");
						//int index=Integer.parseInt(parts2[3]);
						//String range=parts2[2];
						//String orginalRelation[]=ObjectRelations.get(index).split(",");
						String newRelation=pairs[0]+","+parts2[1]+","+parts2[2];		
						if(!ObjectRelations.contains(newRelation))
						{
							System.out.println("��ӵ���Ԫ��Ϊ: "+newRelation);
							ObjectRelations.add(newRelation);
							n++;
						}
					}
				}	
				flag2=false;
			}
		
		}			
		System.out.println("���Ĺ�ϵ������Ϊ: "+m);
		System.out.println("��ӹ�ϵ������Ϊ: "+n);
		return ObjectRelations;
	}
	
	public  ArrayList<String> transformToRelation(ArrayList<String> ObjectRelations,ArrayList<String> Restriction,ArrayList<String> subclasses)
	{
		System.out.println("*********************************");
		ArrayList<String> partOf=new ArrayList<String>();
		HashMap<String,String> partOfMap=new HashMap<String,String>();
		int n=0;
		String relation="";
		for(int i=0;i<Restriction.size();i++)
		{
			String pairs[]=Restriction.get(i).split(",");
			relation=pairs[1];
			if(pairs.length==4&&pairs[2].equals("some"))  //parts[0] ObjectProperty some parts[1]
			{
				String newRelation=pairs[0]+"--"+pairs[3];		
				if(!partOf.contains(newRelation))
				{
					partOf.add(newRelation.toLowerCase());
					partOfMap.put(pairs[0].toLowerCase(), pairs[3].toLowerCase());
				}
			}	
		}	
		
		TreeMap_Tools partOfRelation=new TreeMap_Tools(partOf);
		for(int i=0;i<partOf.size();i++)
		{
			String part[]=partOf.get(i).split("--");
			String index=part[0];
			while(partOfMap.get(index)!=null)
			{
				String object=partOfMap.get(index);//�ҵ�ԭʼ��partof��ϵ��object
				if(!partOfMap.get(index).contains(object))//����϶��ǰ�����
				{
					partOfRelation.putAdd(part[0], object);
				}
				index=object;//��������
			}		
		}
		
		TreeMap_Tools subclass=new TreeMap_Tools(subclasses);
		for(int i=0;i<partOf.size();i++)
		{
			String part[]=partOf.get(i).split("--");
			String index=part[0];
			ArrayList<String> subjects=subclass.GetKey_Value(index);//���������ҵ���������
			if(subjects!=null)
			{		
					ArrayList<String> objects=partOfRelation.GetKey_Value(index);//���������ҵ�ԭ��partof��Objects
					for(int j=0;j<subjects.size();j++)
					{
						String subject=subjects.get(j);
						partOfRelation.putAdd(subject, objects);
					}

			}
		}
		ArrayList<String> keySet=partOfRelation.GetKey();
		for(int i=0;i<partOfRelation.size();i++)
		{
			String subject=keySet.get(i);
			ArrayList<String> objects=partOfRelation.GetKey_Value(subject);
			if(objects!=null)
				for(int j=0;j<objects.size();j++)
				{
					ObjectRelations.add(subject.toUpperCase()+","+relation+","+objects.get(j).toUpperCase());
					n++;
				}
		}
		System.out.println("Restricitonת��Ϊ��Ԫ�������Ϊ: "+n);
		//�������Լ�������is-a��ϵ
		return ObjectRelations;
	}
	
	/*public  TreeMap_Tools transformToPartOf(ArrayList<String> Restriction,ArrayList<String> subclasses)
	{
		System.out.println("*********************************");
		//ArrayList<String> partOf=new ArrayList<String>();
		ArrayList<String> partOf=new ArrayList<String>();
		HashMap<String,String> partOfMap=new HashMap<String,String>();
		//int n=0;
		String relation="";
		for(int i=0;i<Restriction.size();i++)
		{
			String pairs[]=Restriction.get(i).split(",");
			relation=pairs[1];
			if(pairs.length==4&&pairs[2].equals("some"))  //parts[0] ObjectProperty some parts[1]
			{
				String newRelation=pairs[0]+"--"+pairs[3];		
				if(!partOf.contains(newRelation))
				{
					partOf.add(newRelation.toLowerCase());
					partOfMap.put(pairs[0].toLowerCase(), pairs[3].toLowerCase());
				}
			}	
		}	
		
		TreeMap_Tools partOfRelation=new TreeMap_Tools(partOf);
		for(int i=0;i<partOf.size();i++)
		{
			String part[]=partOf.get(i).split("--");
			String index=part[0];
			while(partOfMap.get(index)!=null)
			{
				String object=partOfMap.get(index);//�ҵ�ԭʼ��partof��ϵ��object
				if(!partOfRelation.GetKey_Value(index).contains(object))//����϶��ǰ�����
				//if(!partOfMap.get(index).contains(object))//����϶��ǰ�����
				{
					partOfRelation.putAdd(part[0], object);
				}
				index=object;//��������
			}		
		}
		
		TreeMap_Tools subclass=new TreeMap_Tools(subclasses);
		for(int i=0;i<partOf.size();i++)
		{
			String part[]=partOf.get(i).split("--");
			String index=part[0];
			ArrayList<String> subjects=subclass.GetKey_Value(index);//���������ҵ���������
			if(subjects!=null)
			{		
					ArrayList<String> objects=partOfRelation.GetKey_Value(index);//���������ҵ�ԭ��partof��Objects
					for(int j=0;j<subjects.size();j++)
					{
						String subject=subjects.get(j);
						partOfRelation.putAdd(subject, objects);
					}
			}
		}
		ArrayList<String> keySet=partOfRelation.GetKey();
		partOf.clear();
		for(int i=0;i<partOfRelation.size();i++)
		{
			String subject=keySet.get(i);
			ArrayList<String> objects=partOfRelation.GetKey_Value(subject);
			if(objects!=null)
				partOf.add(subject)
				for(int j=0;j<objects.size();j++)
				{
					partOf.add(subject.toUpperCase()+","+relation+","+objects.get(j).toUpperCase());
					n++;
				}
		}
		System.out.println("Restricitonת��ΪpartOf������Ϊ: "+partOfRelation.getNumberOfMap());
		//�������Լ�������is-a��ϵ
		return partOfRelation;
	}
	
	public  TreeMap_Tools transformToHaspart(ArrayList<String> Restriction,ArrayList<String> subclasses)
	{
		System.out.println("*********************************");
		ArrayList<String> hasPart=new ArrayList<String>();
		HashMap<String,String> hasPartOfMap=new HashMap<String,String>();
		for(int i=0;i<Restriction.size();i++)
		{
			String pairs[]=Restriction.get(i).split(",");
			if(pairs.length==4&&pairs[2].equals("some"))  //parts[0] ObjectProperty some parts[1]
			{
				String newRelation=pairs[3]+"--"+pairs[0];		
				if(!hasPart.contains(newRelation))
				{
					hasPart.add(newRelation.toLowerCase());
					hasPartOfMap.put(pairs[3].toLowerCase(), pairs[0].toLowerCase());
				}
			}	
		}	
		
		
		TreeMap_Tools hasPartRelation=new TreeMap_Tools(hasPart);
		
		for(int i=0;i<hasPart.size();i++)
		{
			String part[]=hasPart.get(i).split("--");
			String index=part[0];
			while(hasPartOfMap.get(index)!=null)
			{
				String object=hasPartOfMap.get(index);//�ҵ�ԭʼ��hasPart����
				if(!hasPartRelation.GetKey_Value(index).contains(object))//����϶��ǰ�����
				{
					hasPartRelation.putAdd(part[0], object);
				}
				index=object;//��������
			}		
		}
		
		TreeMap_Tools subclass=new TreeMap_Tools(subclasses);
		for(int i=0;i<hasPart.size();i++)
		{
			String part[]=hasPart.get(i).split("--");
			String index=part[1];//Ӧ������hasPart��Object�Ĳ���
			ArrayList<String> objects=subclass.GetKey_Value(index);//���������ҵ���������
			if(objects!=null)
			{		
					//ArrayList<String> objects=hasPartRelation.GetKey_Value(index);//���������ҵ�ԭ��partof��Objects
					for(int j=0;j<objects.size();j++)
					{
						String object=objects.get(j);
						hasPartRelation.putAdd(index, object);
					}
			}
		}
		ArrayList<String> keySet=partOfRelation.GetKey();
		partOf.clear();
		for(int i=0;i<partOfRelation.size();i++)
		{
			String subject=keySet.get(i);
			ArrayList<String> objects=partOfRelation.GetKey_Value(subject);
			if(objects!=null)
				partOf.add(subject)
				for(int j=0;j<objects.size();j++)
				{
					partOf.add(subject.toUpperCase()+","+relation+","+objects.get(j).toUpperCase());
					n++;
				}
		}
		System.out.println("Restricitonת��ΪhasPart������Ϊ: "+hasPartRelation.getNumberOfMap());
		//�������Լ�������is-a��ϵ
		return hasPartRelation;
	}*/
	
	public  TreeMap_Tools transformToPartOf(ArrayList<String> Restriction,ArrayList<String> subclasses)
	{
		System.out.println("*********************************");
		//ArrayList<String> partOf=new ArrayList<String>();
		ArrayList<String> partOf=new ArrayList<String>();
		//HashMap<String,String> partOfMap=new HashMap<String,String>();
		HashMap<String, HashSet<String>> partOfMap=new HashMap<String, HashSet<String>>();
		for(int i=0;i<Restriction.size();i++)
		{
			String pairs[]=Restriction.get(i).split(",");
			if(pairs.length==4&&pairs[2].equals("some"))  //parts[0] ObjectProperty some parts[1]
			{
				String newRelation=pairs[0]+"--"+pairs[3];		
				if(!partOf.contains(newRelation))
				{
					partOf.add(newRelation.toLowerCase());
					HashSet<String> a=new HashSet<String>();
					a.add(pairs[3].toLowerCase());
					if(partOfMap.containsKey(pairs[0].toLowerCase()))
					{
						partOfMap.get(pairs[0].toLowerCase()).add(pairs[3].toLowerCase());
					}
					else
						partOfMap.put(pairs[0].toLowerCase(), a);
					//partOfMap.put(pairs[0].toLowerCase(), pairs[3].toLowerCase());
				}
			}	
		}	
		
		System.out.println(partOf.size());
		System.out.println(partOfMap.size());
		
		int num1=0;
		Set<String> keyset1=partOfMap.keySet();
		for(String a:keyset1)
		{
			if(partOfMap.get(a).size()==1)
				num1++;
			else
				num1=num1+partOfMap.get(a).size();
			
		}
		System.out.println("Restricitonת��ΪpartOf������Ϊ: "+num1);
		//System.out.println(partOfMap.getNumberOfMap());
		
		//ArrayList<String> newPartOfRelation=new ArrayList<String>();

		//TreeMap_Tools partOfRelation=new TreeMap_Tools(partOf);
		TreeMap_Tools partOfRelation=new TreeMap_Tools();
		Queue<String> order = new LinkedList<String>();
		for(int i=0;i<partOf.size();i++)
		{
			String part[]=partOf.get(i).split("--");
			String index=part[0];		//��part of�е���λ��Ϊ����
			while(partOfMap.get(index)!=null||!order.isEmpty())//������������ҳ���Ϊ�գ�����������û��ֵ��
			{
						
				if(partOfMap.get(index)!=null)
				{
					HashSet<String> b=new HashSet<String>();	
					b.addAll(partOfMap.get(index));
					for(String a: b)
					{
						order.offer(a);
						if(partOfMap.get(a)!=null)
							partOfMap.get(part[0]).addAll(partOfMap.get(a));
					}
					index=order.remove();
				}
				else
				{
					index=order.remove();
				}			
			}
		}
		
		/*TreeMap_Tools subclass=new TreeMap_Tools(subclasses);
		Set<String> keySet= new HashSet<String>();
		keySet.addAll(partOfMap.keySet());
		for(String a:keySet)
		{
			ArrayList<String> objects=subclass.GetKey_Value(a);
			if(objects!=null)
			{
				for(int i=0;i<objects.size();i++)
				{
					String object=objects.get(i);
					if(!partOfMap.containsKey(object))
					{
						partOfMap.put(object,partOfMap.get(a));
						//partOfMap.get(pairs[0].toLowerCase()).add(pairs[3].toLowerCase());
					}
					else
					{
						partOfMap.get(object).addAll(partOfMap.get(a));
					}
				}
			}
		}
		
		int num2=0;
		Set<String> keyset2=partOfMap.keySet();
		for(String a:keyset2)
		{
			if(partOfMap.get(a).size()==1)
				num2++;
			else
				num2=num2+partOfMap.get(a).size();
			
		}
		System.out.println("Restricitonת��ΪpartOf������Ϊ: "+num2);*/
				
	/*	int num=0;
		Set<String> keyset=partOfMap.keySet();
		for(String a:keyset)
		{
			if(partOfMap.get(a).size()==1)
				num++;
			else
				num=num+partOfMap.get(a).size();
			
		}
		System.out.println("Restricitonת��ΪpartOf������Ϊ: "+num);*/
		Set<String> keyset=partOfMap.keySet();
		for(String key:keyset)
		{
			HashSet<String>  a=partOfMap.get(key);
			ArrayList<String> values=new ArrayList<String>();
			for(String value:a)
			{
				values.add(value);
			}
			partOfRelation.putAdd(key, values);	
		}
		System.out.println("Restricitonת��ΪpartOf������Ϊ: "+partOfRelation.getNumberOfMap());
		
		return partOfRelation;
	}
	
	public  TreeMap_Tools transformToHaspart(ArrayList<String> Restriction,ArrayList<String> subclasses)
	{
		System.out.println("*********************************");
		ArrayList<String> hasPart=new ArrayList<String>();
		//HashMap<String,String> hasPartOfMap=new HashMap<String,String>();
		HashMap<String, HashSet<String>> hasPartOfMap=new HashMap<String, HashSet<String>>();
		for(int i=0;i<Restriction.size();i++)
		{
			String pairs[]=Restriction.get(i).split(",");
			if(pairs.length==4&&pairs[2].equals("some"))  //parts[0] ObjectProperty some parts[1]
			{
				String newRelation=pairs[3]+"--"+pairs[0];		
				if(!hasPart.contains(newRelation))
				{
					hasPart.add(newRelation.toLowerCase());
					HashSet<String> a=new HashSet<String>();
					a.add(pairs[0].toLowerCase());
					if(hasPartOfMap.containsKey(pairs[3].toLowerCase()))
					{
						hasPartOfMap.get(pairs[3].toLowerCase()).add(pairs[0].toLowerCase());
					}
					else
						hasPartOfMap.put(pairs[3].toLowerCase(), a);
				//	hasPartOfMap.put(pairs[3].toLowerCase(), pairs[0].toLowerCase());
				}
			}	
		}	
			
		System.out.println(hasPart.size());
		System.out.println(hasPartOfMap.size());
		int num1=0;
		Set<String> keyset1=hasPartOfMap.keySet();
		for(String a:keyset1)
		{
			if(hasPartOfMap.get(a).size()==1)
				num1++;
			else
				num1=num1+hasPartOfMap.get(a).size();
			
		}
		System.out.println("Restricitonת��ΪhasPart������Ϊ: "+num1);
		

		
		//TreeMap_Tools hasPartRelation=new TreeMap_Tools(hasPart);	
		TreeMap_Tools hasPartRelation=new TreeMap_Tools();	
		Queue<String> order = new LinkedList<String>();
		for(int i=0;i<hasPart.size();i++)
		{
			String part[]=hasPart.get(i).split("--");
			String index=part[0];		//��part of�е���λ��Ϊ����
			while(hasPartOfMap.get(index)!=null||!order.isEmpty())//������������ҳ���Ϊ�գ�����������û��ֵ��
			{				
				if(hasPartOfMap.get(index)!=null)
				{				
					HashSet<String> b=new HashSet<String>();
					b.addAll(hasPartOfMap.get(index));
					for(String a: b)
					{
						order.offer(a);
						if(hasPartOfMap.get(a)!=null)
							hasPartOfMap.get(part[0]).addAll(hasPartOfMap.get(a));
					}
					index=order.remove();
				}
				else
				{
					index=order.remove();
				}			
			}
		}
	
		/*TreeMap_Tools subclass=new TreeMap_Tools(subclasses);
		Set<String> keySet= new HashSet<String>();
		keySet.addAll(hasPartOfMap.keySet());
		for(String a:keySet)
		{
			HashSet<String> b=new HashSet<String>();
			b.addAll(hasPartOfMap.get(a));//��Ҫ��hasPart�й�ϵ�ĺ��沿��
			for(String c:b)
			{
				ArrayList<String> objects=subclass.GetKey_Value(c);//ͨ���������ҵ��Ӽ�������
				if(objects!=null)
				{
					hasPartOfMap.get(a).addAll(objects);
					for(int i=0;i<objects.size();i++)//ͬ�������п��ܻ���hasPart��ϵ��Ӧ���е��������
					{
						String object=objects.get(i);
						if(hasPartOfMap.get(object)!=null)
							hasPartOfMap.get(a).addAll(hasPartOfMap.get(object));
					}
				}
			}
		}
		
		int num2=0;
		Set<String> keyset2=hasPartOfMap.keySet();
		for(String a:keyset2)
		{
			if(hasPartOfMap.get(a).size()==1)
				num2++;
			else
				num2=num2+hasPartOfMap.get(a).size();
			
		}
		System.out.println("Restricitonת��ΪhasPart������Ϊ: "+num2);*/
	/*	int num=0;
		Set<String> keyset=hasPartOfMap.keySet();
		for(String a:keyset)
		{
			if(hasPartOfMap.get(a).size()==1)
				num++;
			else
				num=num+hasPartOfMap.get(a).size();		
		}
		System.out.println("Restricitonת��ΪhasPart������Ϊ: "+num);*/
		Set<String> keyset=hasPartOfMap.keySet();
		for(String key:keyset)
		{
			HashSet<String>  a=hasPartOfMap.get(key);
			ArrayList<String> values=new ArrayList<String>();
			for(String value:a)
			{
				values.add(value);
			}
			hasPartRelation.putAdd(key, values);	
		}
		System.out.println("Restricitonת��ΪhasPart������Ϊ: "+hasPartRelation.getNumberOfMap());
		
	
		return hasPartRelation;
	}
	
	
	
	public ArrayList<String> findIndexOfRelation(ArrayList<String> Relations,String index)
	{
		ArrayList<String> Triples=new ArrayList<String>();
		for(int i=0;i<Relations.size();i++)
		{
			String parts[]=Relations.get(i).split(",");
			if(index.equals(parts[1]))
			{
				Triples.add(Relations.get(i)+","+i);//ͬʱ��¼������ֵ
			}
		}
		return Triples;
	}
	
	public String findIndexOfPair(ArrayList<String> pair,String index)
	{
		int  num=findIndexClass(pair,index);
		if(num!=-1)
		{
			String parts[]=pair.get(num).split("--");
			return parts[1];	
		}
		else
		{
			return null;
		}
	}
	
	
	
	public int findIndexClass(ArrayList<String> Class,String index)
	{
		//ArrayList<String> Triples=new ArrayList<String>();
		for(int i=0;i<Class.size();i++)
		{
			String parts[]=Class.get(i).split("--");
			if(index.equals(parts[0]))
			{
				return i;
			}
		}
		return -1;
	}
	
	public ArrayList<String> findIndexProperty(ArrayList<String> Relation,String index)
	{
		ArrayList<String> Triples=new ArrayList<String>();
		for(int i=0;i<Relation.size();i++)
		{
			String parts[]=Relation.get(i).split(",");
			if(index.equals(parts[1]))	//Ϊ�յ����һ����Ψһ��
			{
				Triples.add(Relation.get(i));
			}
		}
		return Triples;
	}
	
	public ArrayList<String> GetConcept_Instances()
	{
		ArrayList<String> Concept_Instances=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //�����ı��������������ʽ����
		OntClass temp ;
		while(classlist.hasNext()){		
			temp=classlist.next();
			String Concept_Members="";
			String name=temp.getLocalName().toString();
			//System.out.println(name);
			//name=name.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
			String members="";
			String instance="";
			if(name.equals("Thing")||name.equals("Nothing"))
				continue;
			ExtendedIterator<? extends OntResource> Instance=temp.listInstances(false);
			while (Instance.hasNext()) {
				OntResource member=Instance.next(); //���������ת��ֻ����OntResource
				instance=member.getLocalName();
				if(instance!=null&&!instance.equals("nil"))
				{
					instance=NLP(instance);//�д�����
					members=members+","+instance;		
				}
			}	
			//System.out.println(!(instance==null));
			
			if(instance!=null&&!members.equals(""))
			{
				Concept_Members=name+"--"+members;	
			//	System.out.println(Concept_Members);
				Concept_Instances.add(Concept_Members);
			}
		}	
		return Concept_Instances;
	}
	
	public String NLP(String name) //��ʵ������һ������
	{
		String tokensWord=tokeningWord(name);
		String tokens[]=tokensWord.split(" ");
		if(tokens.length==1)
			return name;	
		else //�õ�һЩ��Ȼ����ļ���,�����,����ʵ��ʶ��,������Ҫ�����Ǳ�׼��
		{
			/*for(int i=0;i<tokens.length;i++)
			{
				if(tokens[i].equals("of"))
				{
					tokens=exchange();//�������ߵ�λ��
				}
			}*/
			return tokensWord;
			//�����ļ�Ƹ�ԭ��
			//������Ҫ��׼��
			//�绰�����ʶ��(�����õ�����ʵ��ʶ��)
			//ʱ̬����
		}		
	}
	
	public String tokeningWord(String str)
	{
		String s1=str;
		//s1="Registration_SIGMOD_Member";
		String ss = "";
		for(int i=0;i<s1.length()-1;i++){
			char aa=s1.charAt(i+1);
			char a=s1.charAt(i);
			if(Character.isUpperCase(a) && i==0)//�������ĸ�Ǵ�д��ֱ�����
			{
				ss=ss+String.valueOf(a);
			}
			else if(Character.isUpperCase(a) &&Character.isLowerCase(aa)&& i!=0)//�������ĸ�Ǵ�д����Ҫ����ָ���
			{
				ss=ss+" "+String.valueOf(a);
			}	
			else if((a=='-'||a=='_'||a=='.'))//�������ַ�"-","_" ���Һ���aa�Ǵ�д����������
			{
				//continue;
				ss=ss+" ";//���ڼ�ӽ�'_','-'�������滻
			}
			else if(Character.isUpperCase(a)&&Character.isUpperCase(aa))
			{
				ss=ss+String.valueOf(a);
			}	
			else if(Character.isLowerCase(a)&&Character.isUpperCase(aa))//ǰ��Сд����Ӵ�д
			{
				ss=ss+String.valueOf(a)+" ";
			}	
			else  //��ʵ����������
			{             
				ss=ss+String.valueOf(a);
			}
		}
		ss=ss+s1.charAt(s1.length()-1);
		ss=ss.replace("  ", " ").trim();
		return ss.toLowerCase().replaceAll("_|-","");		
	}
	
	public ArrayList<String> GetSubclass()
	{
		/*ArrayList<String> Subclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		//String sub_information=null;
		OntClass temp ;
		while(classlist.hasNext())
		{
			temp=classlist.next();
			String a=temp.getLocalName();
			System.out.println(a);
			ExtendedIterator<OntClass> subClasses = temp.listSubClasses(true);		
			if(subClasses.hasNext())
			{
				OntClass temp1=subClasses.next();
				System.out.print(temp1.getLocalName()+" ");
			}
			System.out.println("\n********************");
		}
		return Subclasses;*/
		ArrayList<String> Subclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String sub_information=null;
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			System.out.println("***********************");
			System.out.println(temp.getLocalName().toString());
			ExtendedIterator<OntClass> classlist1 = temp.listSubClasses(false);//false��ʾ�г����е����࣬true��ʾֻ�г�ֱ������
			if(classlist1.hasNext())
			{
				String a=temp.getLocalName().toString();
				if(a.charAt(0)=='_')
					a=a.replaceFirst("_", "");
				//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				if(a.equals("Thing")||a.equals("Nothing"))
					continue;
				sub_information=a+"--";
				OntClass temp1;
				while(classlist1.hasNext())
				{
					temp1=classlist1.next();
					String b=temp1.getLocalName().toString();
					sub_information=sub_information+","+b;								
				}
				
				if(sub_information.replace(a, "").equals("--,Nothing"))
					continue;
				else
				{
					sub_information=sub_information.replace(",Nothing","");
					sub_information=sub_information.replace("--,","--");	//���ǿ��ܳ���Nothing�����
					Subclasses.add(sub_information);
				}
			//��д���ļ�ʱ����ý�"--,"�滻��"--"
			}			
		}
		return Subclasses;		
	}
	
	
	public ArrayList<String> GetSubclass_Direct()
	{
		ArrayList<String> Subclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String sub_information=null;
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			//����thing��ֱ��������г���
			ExtendedIterator<OntClass> classlist1 = temp.listSubClasses(true);//false��ʾ�г����е����࣬true��ʾֻ�г�ֱ������
			if(classlist1.hasNext()){
				String a=temp.getLocalName().toString();
				if(a.charAt(0)=='_')
					a=a.replaceFirst("_", "");
				//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				sub_information=a+"--";
				//sub_information=temp.getLocalName().toString()+"--";
			OntClass temp1;
			while(classlist1.hasNext())
			{
				temp1=classlist1.next();
				String b=temp1.getLocalName().toString();
				//b=b.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				sub_information=sub_information+","+b;		
				//sub_information=sub_information+","+temp1.getLocalName().toString();				
			 }
			sub_information=sub_information.replace("--,","--");	//���ǿ��ܳ���Nothing�����
			if(!sub_information.replace(a, "").equals("--Nothing"))
				Subclasses.add(sub_information);	
			}	
			/*//����thing��ֱ����ص������г���
		//	System.out.println(temp.getLocalName().toString());
			ExtendedIterator<OntClass> classlist2 = temp.listSuperClasses(true);//false��ʾ�г����е����࣬true��ʾֻ�г�ֱ������
		//	System.out.println(classlist2.hasNext());
			if(!classlist2.hasNext())    //��һ������ǿ��ܸ���Ϊ�յ����
			{
				String a=temp.getLocalName().toString();
				a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				sub_information="Thing"+"--"+a;
				//sub_information="Thing"+"--"+temp.getLocalName().toString();	
				Subclasses.add(sub_information)	;		
			 }
			while(classlist2.hasNext()) //�ڶ�������ǿ��ܸ���ΪResource�����
			{
				OntClass temp2;
				temp2=classlist2.next();
				if(temp2.isRestriction()||(temp2.getLocalName()==null)) //����������������
				{
					continue;
				}
			//	System.out.println(temp2.isRestriction());
			//	System.out.println(temp2.getLocalName().toString());
				//System.out.println("***********");
				String father=temp2.getLocalName().toString();
			//	father=father.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				if(father.equals("Resource")||father.equals("Thing"))
				{	
					String a=temp.getLocalName().toString();
				    a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'		
				    sub_information="Thing"+"--"+a;
					//sub_information="Thing"+"--"+temp.getLocalName().toString();	
					Subclasses.add(sub_information)	;	
					break;
				}
			}*/
			//��д���ļ�֮ǰ����ý�"--,"�滻��"--"		
		}
		return Subclasses;
	}
	
	
	/*public ArrayList<String> GetSuperclass()
	{
		ArrayList<String> Superclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String super_information=null;
		OntClass temp ;
		while(classlist.hasNext())
		{
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			if(a.equals("Thing")||a.equals("Nothing"))
				continue;
			else
			{
				//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				super_information=a+"--Thing";		//�κν��Ķ���Thing��Ϊ�����Ƚ��
				ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(false);//false��ʾ�г����еĸ��࣬true��ʾֻ�г�ֱ�Ӹ���
				if(!classlist1.hasNext())    //��һ������ǿ��ܸ���Ϊ�յ����
				{
					//continue;
				}
				while(classlist1.hasNext()) //�ڶ�������ǿ��ܸ���ΪResource�����
				{
					OntClass temp2;
					temp2=classlist1.next();
					if(temp2.isRestriction()||(temp2.getLocalName()==null)) //����������������
					{
						continue;
					}
					//�ų�������������father�Ͳ���һ�������࣬������������ķ�ʽ��ȡ����
					String father=temp2.getLocalName().toString();
					if(father.equals("Resource")||father.equals("Thing"))
					{
						continue; //����������Ŀ����Ҫ�������Ჹ���������
					}
					else
					{
						super_information=super_information+","+father;
					}
				}
				Superclasses.add(super_information)	;	
			}
		}
		return Superclasses;
	}*/
	
	public ArrayList<String> GetSuperclass()
	{
		ArrayList<String> Superclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String super_information="";
		OntClass temp ;
		while(classlist.hasNext())
		{
			boolean flag=false;
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			if(a.equals("Thing")||a.equals("Nothing"))
				continue;
			else
			{
				//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				super_information=a+"--";		//�κν��Ķ���Thing��Ϊ�����Ƚ��
				
				ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(false);//false��ʾ�г����еĸ��࣬true��ʾֻ�г�ֱ�Ӹ���
				while(classlist1.hasNext()) //�ڶ�������ǿ��ܸ���ΪResource�����
				{
					OntClass temp2;
					temp2=classlist1.next();
					//����һ�������࣬�Ҳ���Resource����Thing
					if(!temp2.isAnon()&&!temp2.getLocalName().equals("Resource")&&!temp2.getLocalName().equals("Thing"))
					{
					//�ų�������������father�Ͳ���һ�������࣬������������ķ�ʽ��ȡ����
						flag=true;
						String father=temp2.getLocalName().toString();
						super_information=super_information+","+father;
					}
				}
				if(flag==true)
					Superclasses.add(super_information)	;	
			}
			//flag=false;
		}
		return Superclasses;
	}
	
	/*public ArrayList<String> GetSupclass_Direct()
	{
		ArrayList<String> Superclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String super_information=null;
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
			if(a.equals("Thing")||a.equals("Nothing"))
				continue;
			else
			{
			super_information=a+"--Thing";		//�κν��Ķ���Thing��Ϊ�����Ƚ��
			ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(true);//false��ʾ�г����еĸ��࣬true��ʾֻ�г�ֱ�Ӹ���
			if(!classlist1.hasNext())    //��һ������ǿ��ܸ���Ϊ�յ����
			{
				//continue;
			}
			while(classlist1.hasNext()) //�ڶ�������ǿ��ܸ���ΪResource�����
			{
				OntClass temp2;
				temp2=classlist1.next();
				if(temp2.isRestriction()||(temp2.getLocalName()==null)) //����������������
				{
					continue;
				}
				//�ų�������������father�Ͳ���һ�������࣬������������ķ�ʽ��ȡ����
				String father=temp2.getLocalName().toString();
				if(father.equals("Resource")||father.equals("Thing"))
				{
					continue; //����������Ŀ����Ҫ�������Ჹ���������
				}
				else
				{
					super_information=super_information+","+father;
				}
			}
			Superclasses.add(super_information)	;	
		}
		}
		return Superclasses;
	}*/
	
	public ArrayList<String> GetSupclass_Direct()
	{
		ArrayList<String> Superclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String super_information="";
		OntClass temp ;
		while(classlist.hasNext())
		{
			boolean flag=false;
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			if(a.equals("Thing")||a.equals("Nothing"))
				continue;
			else
			{
				//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				super_information=a+"--";		//�κν��Ķ���Thing��Ϊ�����Ƚ��		
				ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(true);//false��ʾ�г����еĸ��࣬true��ʾֻ�г�ֱ�Ӹ���
				while(classlist1.hasNext()) //�ڶ�������ǿ��ܸ���ΪResource�����
				{
					OntClass temp2;
					temp2=classlist1.next();
					//����һ�������࣬�Ҳ���Resource����Thing
					if(!temp2.isAnon()&&!temp2.getLocalName().equals("Resource")&&!temp2.getLocalName().equals("Thing"))
					{
					//�ų�������������father�Ͳ���һ�������࣬������������ķ�ʽ��ȡ����
						flag=true;
						String father=temp2.getLocalName().toString();
						super_information=super_information+","+father;
					}
				}
				if(flag==true)
					Superclasses.add(super_information)	;	
			}
			//flag=false;
		}
		return Superclasses;
	}
	
	public ArrayList<String> GetEquivalentClass()
	{
		ArrayList<String> EquivalentClass=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		//String equivalentInformation=null;
		OntClass temp ;
		while(classlist.hasNext())
		{
			temp=classlist.next();
			String concept=temp.getLocalName().toString();
			System.out.println(concept);
			if(concept.charAt(0)=='_')
				concept=concept.replaceFirst("_", "");
			//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
			if(concept.equals("Thing")||concept.equals("Nothing"))
				continue;
			else
			{	
				ExtendedIterator<OntClass> equivaluentClass = temp.listEquivalentClasses();
				while(equivaluentClass.hasNext()) //�ڶ�������ǿ��ܸ���ΪResource�����
				{
					OntClass equivalent=equivaluentClass.next();
					if(!equivalent.isAnon())//ֱ�Ӵ��ڵȼ۵����
					{
						String equivalentConcept=equivalent.getLocalName().toString();
						if(!equivalentConcept.equals(concept)&&!equivalentConcept.equals("Nothing"))
							EquivalentClass.add(concept+","+equivalentConcept+","+"Equal");
					}
					else
					{
						if(equivalent.isRestriction())						
						{
							Restriction res = equivalent.asRestriction();
							String relation = "";
							String restriction = "";
							String type = "";
							if (res.isSomeValuesFromRestriction()) //OP some Class
							{
								System.out.println("some!");
								SomeValuesFromRestriction res_some = res.asSomeValuesFromRestriction();
								relation = res_some.getOnProperty().getLocalName();
								OntClass a = (OntClass) res_some.getSomeValuesFrom();
								restriction = "some";
								type = a.getLocalName();
								if (a.isUnionClass()) 
								{
									UnionClass uc = a.asUnionClass();
									ExtendedIterator<? extends OntClass> uco = uc.listOperands();
									while (uco.hasNext()) 
									{
										type = uco.next().getLocalName();
										String constraint = concept+","+relation + ","+ restriction + "," + type;
										System.out.println(constraint);
										EquivalentClass.add(constraint);
									}
								}
								else 
								{
									String constraint = concept+","+relation + "," + restriction+ "," + type;
									System.out.println(constraint);
									EquivalentClass.add(constraint);
								}
							}					
						}//����has some class�����
						else if(equivalent.isUnionClass())//or�����,����������Ѿ���������
						{
							UnionClass uc = equivalent.asUnionClass();
							ExtendedIterator<? extends OntResource> uco = uc.listOperands();
							while (uco.hasNext()) {
								OntResource subConcept=uco.next();
								//String subConcept = uco.next();
								if(!subConcept.isAnon())
								{							
									EquivalentClass.add(subConcept.getLocalName()+","+concept+","+"sub");
									System.out.println(subConcept.getLocalName()+","+concept+","+"sub");
								}				
							}
						}
						else if(equivalent.isIntersectionClass())//and �����,��ʵֻ��Ҫ����not�����
						{
							IntersectionClass uc = equivalent.asIntersectionClass();
							ExtendedIterator<? extends OntResource> uco = uc.listOperands();
							while (uco.hasNext()) {
								OntResource subConcept=uco.next();
								if(!subConcept.isAnon())//���岻����Ϊ���ӹ�ϵ�Ѿ���and���ֳ�����
								{
									EquivalentClass.add(concept+","+subConcept.getLocalName()+","+"sub");
									System.out.println(concept+","+subConcept.getLocalName()+","+"sub");
								}
								if(subConcept.asClass().isComplementClass())//�����ʵ��Ĳ���
								{
									ComplementClass uc1 = subConcept.asClass().asComplementClass();
									ExtendedIterator<? extends OntClass> uco1 = uc1.listOperands();
									while (uco1.hasNext()) {
										OntResource disjointConcept=uco1.next();
										if(!disjointConcept.isAnon()&&!disjointConcept.getLocalName().equals("Nothing"))
										{
											EquivalentClass.add(concept+","+disjointConcept.getLocalName()+","+"not");
											EquivalentClass.add(disjointConcept.getLocalName()+","+concept+","+"not");
											System.out.println(concept+","+disjointConcept.getLocalName()+","+"not");
										}				
									}
								}
							}
						}
						else if(equivalent.isComplementClass())//not �����
						{
							ComplementClass uc = equivalent.asComplementClass();
							ExtendedIterator<? extends OntClass> uco = uc.listOperands();
							while (uco.hasNext()) {
								OntResource disjointConcept=uco.next();
								if(!disjointConcept.isAnon()&&!disjointConcept.getLocalName().equals("Nothing"))
								{
									EquivalentClass.add(concept+","+disjointConcept.getLocalName()+","+"not");
									EquivalentClass.add(disjointConcept.getLocalName()+","+concept+","+"not");
									System.out.println(concept+","+disjointConcept.getLocalName()+","+"not");
								}				
							}
						}
					}			
				}
			}
		}
		return EquivalentClass;
	}
	
	public  ArrayList<String> GetEquivalentProperty()
	{
		ArrayList<String> EquivalentProperties=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist = ontology.listObjectProperties(); //�����ı��������������ʽ����
		ObjectProperty temp ;	
		while(propertylist.hasNext())
		{
			temp=propertylist.next();		
			String property=temp.getLocalName().toString();
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))
				continue;
			else
			{	
				ExtendedIterator<? extends OntProperty> equivalentProperties = temp.listEquivalentProperties();
				while(equivalentProperties.hasNext()) //�ڶ�������ǿ��ܸ���ΪResource�����
				{
					OntProperty equivalent=equivalentProperties.next();
					if(!equivalent.isAnon())//ֱ�Ӵ��ڵȼ۵����
					{
						String equivalentProperty=equivalent.getLocalName().toString();
						if(!equivalentProperty.equals(property))
							EquivalentProperties.add(property+","+equivalentProperty+","+"Equal");
					}
				}
			}
		}
		return EquivalentProperties;
	}
	
	public ArrayList<String> GetSibling(ArrayList<String> Subclasses_Direct)
	{
		ArrayList<String> Sibling=new ArrayList<String>();
		for(int i=0;i<Subclasses_Direct.size();i++)
		{
			String father_children[]=Subclasses_Direct.get(i).split("--");
			if(father_children[0].equals("Thing"))
				continue;
			else
			{
				String children[]=father_children[1].split(",");
				if(children.length>1)
				{
					String sibling_information="";
					for(int j=0;j<children.length;j++)
					{
						//������ӵ�λ�ÿ��������һλ��Ҳ�����ǵ�һλ
						String others=father_children[1].replace(children[j]+",", "").replace(","+children[j], "");
						sibling_information=children[j].toString()+"--"+others;
					/*	if(!Sibling.contains(sibling_information))
							Sibling.add(sibling_information);	*/				
						/*for(int k=0;k<children.length;k++)
						{
							if(k!=j)
							{
								sibling_information=sibling_information+","+children[k].toString();
							}
						}
						if(!Sibling.contains(sibling_information))
							Sibling.add(sibling_information);*/
					}		
				}
			}		
		}	
		return Sibling;
	}
	
	public ArrayList<String> GetDisjointwith()
	{
		ArrayList<String> Disjointion=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String sub_information=null;
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.equals("Thing")||a.equals("Nothing"))
				continue;
			else
			{
			ExtendedIterator<OntClass> disjointionlist = temp.listDisjointWith();//false��ʾ�г����е����࣬true��ʾֻ�г�ֱ������
			if(disjointionlist.hasNext()){
			//	String a=temp.getLocalName().toString();
			//	a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				sub_information=a+"--";
				OntClass temp1;
				while(disjointionlist.hasNext())
				{
					temp1=disjointionlist.next();
					String b=temp1.getLocalName().toString();
					//if(!b.equals("Nothing"))
						sub_information=sub_information+","+b;	
					//b=b.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
								
				}
				sub_information=sub_information.replace("--,","--");	//���ǿ��ܳ���Nothing�����
				if(sub_information.replace(a, "").equals("--Nothing"))
					continue;
				else
				Disjointion.add(sub_information.replace("--,", "--").replace(",Nothing", ""));
			//��д���ļ�ʱ����ý�"--,"�滻��"--"
			}		
		}
		}
		return Disjointion;
	}
	
	public ArrayList<String> GetLeaves()
	{
		//��������Ҷ��������������ʽ���з���
		ArrayList<String> leaf=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String sub_information="";
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			//System.out.println(a);
			ExtendedIterator<OntClass> classlist1 = temp.listSubClasses(true);	
			while(classlist1.hasNext())
			{
				OntClass temp1=classlist1.next();
				String b=temp1.getLocalName().toString();
			//	a=a.replace("-","_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'		
				sub_information=b+sub_information;
				//leaf.add(temp.getLocalName().toString());
			}
			if(sub_information.equals("Nothing"))
				leaf.add(a);	
			sub_information="";
		}
		return leaf;
	}
	
	public ArrayList<String> get_reference(String alignmentFile)
	{
	    ArrayList<String> references=new ArrayList<String>();  
	    Model model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open( alignmentFile );
        if (in == null) {
            throw new IllegalArgumentException( "File: " + alignmentFile + " not found");
        }
        model.read(in,"");
		//model.read(in,null);
        //������ʽ1(���YAM++)
/*		Property alignmententity1, alignmententity2;
		alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity1");
		alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity2");
		Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#measure");
		Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#relation");*/
		//OntClass temp;
        //���ͷ�ʽ2(���AML++,reference alignments)
	    Property alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity1");
		Property alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity2");//alignment
		Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentmeasure");
		Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentrelation");

        ResIterator resstmt = model.listSubjectsWithProperty(alignmententity1);	//���߷�����һ����
	  
		//Answer an iterator [with no duplicates] over all the resources in this model that have property p
      //  ResIterator resstmt = model.listResourcesWithProperty(alignmententity1);
		//StmtIterator resstmt = model.listStatements();
	//	int id1,id2;
	    Resource temp;
	    //Resource temp,entity1,entity2;
	//	ResIterator resstmt = model.listSubjectsWithProperty(alignmententity1);
		//System.out.print(resstmt.hasNext());
		while(resstmt.hasNext()){
			temp = resstmt.next();//temp����Ԫ��
			String entity1 = temp.getPropertyResourceValue(alignmententity1).getLocalName();//��ȡ����1�ı���
			String entity2 = temp.getPropertyResourceValue(alignmententity2).getLocalName();//��ȡ����2�ı���
			String Confidence=temp.getProperty(value).getObject().asLiteral().getString();//��ȡ����ֵ
			String Relation=temp.getProperty(relation).getObject().toString();//��ȡƥ��Ĺ�ϵ
			
			//�Ƚϱ��ķ���
			/*String Confidence=temp.getProperty(value).getLiteral().toString();
			Confidence=Confidence.replace("^^xsd:float", "").replace("^^http://www.w3.org/2001/XMLSchema#float", "");
			System.out.println(Confidence);*/
			
			
		//	System.out.println(Relation);
		//	System.out.println(entity1+" "+entity2);
			System.out.println(entity1+" "+entity2+" "+Relation+" "+Confidence);
			
			//������е���Ԫ��
	/*		StmtIterator stmt = model.listStatements();
			while(stmt.hasNext()){
				System.out.println(stmt.next());
			}*/
			entity1=entity1.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'		
			entity2=entity2.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'		
			references.add(entity1+"--"+entity2);//ͳһת����Сд
		}
		return references;
	}
	

	
	public ArrayList<String> filter_property(ArrayList<String> reference,ArrayList<String> concept1)
	{
		ArrayList<String> Class_Match=new ArrayList<String>();
		for(int i=0;i<reference.size();i++)
		{
			String[] Match_pairs = reference.get(i).split("--");//ֻҪȡǰ��һ���ּ�����1�еĸ���Աȼ���
			//System.out.println(Match_pairs[0]);
			for(int j=0;j<concept1.size();j++)
			{
				//Session_Chair
				//SessionChair
			//	System.out.println(concept1.get(j));
				if(Match_pairs[0].equals(concept1.get(j)))
				{
					Class_Match.add(reference.get(i));
					break;
				}
				//��ʱ��������ֶ�һ��"_"����ƥ������
				else if(Match_pairs[0].replace("_", "").equals(concept1.get(j)))
				{
					Class_Match.add(reference.get(i));
					break;
				}
				else if(Match_pairs[0].equals(concept1.get(j).replace("_", "")))
				{
					Class_Match.add(reference.get(i));
					break;
				}
			}
		}		
		//��reference�а������Ե�ƥ��Խ��й���
		return Class_Match;
	}
	
	public ArrayList<String> filter_class(ArrayList<String> reference,ArrayList<String> class_match)
	{
		ArrayList<String> Property_Match=new ArrayList<String>();
		boolean label[]=new boolean[reference.size()];
		for(int i=0;i<reference.size();i++)
		{
			//String[] Match_pairs = reference.get(i).split("--");//ֻҪȡǰ��һ���ּ�����1�еĸ���Աȼ���
			//System.out.println(Match_pairs[0]);
			
			for(int j=0;j<class_match.size();j++)
			{
				if(reference.get(i).equals(class_match.get(j)))
				{
					label[i]=true;
					break;					
				}			
			}
		}	
		//ƥ�������Щ���Ǹ����ƥ��ԣ�Ĭ�Ͻ��俴�����Ե�ƥ��ԡ�
		for(int k=0;k<reference.size();k++)
		{
			if(label[k]!=true)
			{
				Property_Match.add(reference.get(k));
			}
		}
		//��reference�а������Ե�ƥ��Խ��й���
		return Property_Match;
	}
	
	
    //��ȡֻ���������ƥ��
	public ArrayList<String> Get_haschild_match(ArrayList<String> Match,ArrayList<String> Leaf1,ArrayList<String> Leaf2) //��ȡ˫�����к��ӵ�ƥ���
	{
		ArrayList<String> constrained_match=new ArrayList<String>();
        boolean flag1=false,flag2=false;
		for(int i=0;i<Match.size();i++)
		{
			
			//��ƥ��Խ����з��ж�
			String[] Match_pairs = Match.get(i).split("--");
			if(Leaf1==null)
			{
				flag1=true;
			}
			else
			{
			 for(int j=0;j<Leaf1.size();j++)
			 {
				//System.out.println(Leaf1.get(j));
				//��ƥ��Եĵ�һ�����Ƿ���ڱ���1��Ҷ�ӽڵ�
				if(Match_pairs[0].equals(Leaf1.get(j)))
				{
					flag1=false;
					break;
				}
				flag1=true;
			 }
			}
			if(Leaf2==null)
			{		
				flag2=true;
			}
			else
			{
			 for(int L=0;L<Leaf2.size();L++)			
			 {
				//��ƥ��Եĵڶ������Ƿ���ڱ���2��Ҷ�ӽڵ�
				if(Match_pairs[1].equals(Leaf2.get(L)))
				{
					flag2=false;
					break;
				}
				flag2=true;
			 }
			}
			if(flag1==true&&flag2==true)
			{
				constrained_match.add(Match.get(i));
			}
		}		
		return constrained_match;
	}
	
	public ArrayList<String> findConceptChildrenforObjcet(String concept )//�ҵ�ĳһ���������������
	{
		ArrayList<String> children = new ArrayList<String>();
		ArrayList<String> new_children = new ArrayList<String>();
		OntClass temp = null;
		temp = ontology.getOntClass(OntoID + concept);
		if(temp==null)
		{
			//children.add(concept);//����Ҫ�ظ����
			return children;
		}
		ExtendedIterator<OntClass> classes = temp.listSubClasses(true);//������ֻҪһ��ת���ͻ����
		//System.out.println(concept);
		/*System.out.println(classes.toList().size());
		System.out.println(classes.toList().size()==1);
		System.out.println(classes.toList().size());*/
		int m=0; //��Щ��Nothing,��Щû��Nothing
		while(classes.hasNext())
		{
			OntClass temp1=classes.next();				
			String b=temp1.getLocalName().toString();	
			if(!b.equals("Nothing"))
			{
				children.add(b);	
				m++;
			}
			//ֻ��һ�����ӵĵݹ����,��ʵ�����ᵼ���������ֹ�ϵ�Ŀ�����Խ��Խ��
			/*temp = ontology.getOntClass(OntoID + b);
				classes = temp.listSubClasses(true);*/		
			//��д���ļ�ʱ����ý�"--,"�滻��"--"	
		}
		if(m==1)
		{
			ArrayList<String> oneSubClass=findConceptChildrenforObjcet(children.get(0));
			if(oneSubClass.size()==1)
				children.add(oneSubClass.get(0));
			return children;
		}
		else
			return new_children;
	}
	
	public ArrayList<String> findConceptChildrenforDatatype(String concept )//�ҵ�ĳһ���������������
	{
		ArrayList<String> children = new ArrayList<String>();
		OntClass temp = null;
		temp = ontology.getOntClass(OntoID + concept);
		if(temp==null)
		{
			//children.add(concept);//����Ҫ�ظ����
			return children;
		}
		ExtendedIterator<OntClass> classes = temp.listSubClasses(false);
		//System.out.println(concept);
		if(classes.hasNext())//�൱�����ݿ��˼�롣���װ���������ԣ���ô���ӿ϶���
		{		
			OntClass temp1;
			while(classes.hasNext())
			{
				temp1=classes.next();
				String b=temp1.getLocalName().toString();
				if(b.equals("Nothing"))
					continue;
				children.add(b);						
			}
			
		//��д���ļ�ʱ����ý�"--,"�滻��"--"
		}		
		return children;
	}
	
	
	public ArrayList<String> FindConcept_superclasses(String concept )//�ҵ�ĳһ���������������
	{
		ArrayList<String> superclasses = new ArrayList<String>();
		OntClass temp = null;
		temp = ontology.getOntClass(OntoID + concept);
		System.out.println(concept);
		ExtendedIterator<OntClass> superclass = temp.listSuperClasses(false);
		while (superclass.hasNext()) {
			OntClass Restriction = superclass.next();
			// System.out.println(Restriction.getLocalName().toString());
			// //���ӹ�ϵʽ����Ҫ�ˣ�ֻ��Ҫ��ȡ�����������
			if (Restriction.isRestriction()) {
				System.out.println("****************************");
				// System.out.println(Restriction.isRestriction());
				// System.out.println(Restriction);
				Restriction res = Restriction.asRestriction();
				// System.out.println(res.isMinCardinalityRestriction());
				String relation = "";
				String restriction = "";
				String type = "";
				if (res.isAllValuesFromRestriction()) {
					System.out.println("Only!");
					AllValuesFromRestriction res_all = res
							.asAllValuesFromRestriction();
					relation = res_all.getOnProperty().getLocalName();
					// System.out.println(res_all.getOnProperty().getLocalName());
					OntClass a = (OntClass) res_all.getAllValuesFrom();
					restriction = "only";
					type = a.getLocalName();
					if (a.isUnionClass()) {
						UnionClass uc = a.asUnionClass();
						ExtendedIterator<? extends OntClass> uco = uc
								.listOperands();
						while (uco.hasNext()) {
							type = uco.next().getLocalName();
							String constraint = relation + "," + restriction
									+ "," + type;
							System.out.println(constraint);
							superclasses.add(constraint);
						}
					} else {
						String constraint = relation + "," + restriction + ","
								+ type;
						System.out.println(constraint);
						superclasses.add(constraint);
					}
				} else if (res.isSomeValuesFromRestriction()) {
					System.out.println("some!");
					SomeValuesFromRestriction res_some = res
							.asSomeValuesFromRestriction();
					relation = res_some.getOnProperty().getLocalName();
					// System.out.println(res_some.getOnProperty().getLocalName());
					OntClass a = (OntClass) res_some.getSomeValuesFrom();
					restriction = "some";
					type = a.getLocalName();
					String constraint = relation + "," + restriction + ","
							+ type;
					System.out.println(constraint);
					superclasses.add(constraint);
				} else if (res.isHasValueRestriction()) {
					System.out.println("hasvalue!!");
					HasValueRestriction res_hasvalue = res
							.asHasValueRestriction();
					relation = res_hasvalue.getOnProperty().getLocalName();
					// System.out.println(res_hasvalue.getOnProperty().getLocalName());
					restriction = "value";
					type = res_hasvalue.getHasValue().toString();
					String constraint = relation + "," + restriction + ","
							+ type;
					System.out.println(constraint);
					superclasses.add(constraint);
				} else if (res.isCardinalityRestriction()) {
					System.out.println("exactly!");
					CardinalityRestriction res_exact = res
							.asCardinalityRestriction();
					relation = res_exact.getOnProperty().getLocalName();
					restriction = "exactly " + res_exact.getCardinality();
					NodeIterator value = res_exact.listPropertyValues(null);
					while (value.hasNext()) {
						RDFNode a = value.next();
						// System.out.println();
						if (a.isLiteral()) {
							type = "Literal";
							break;
						} else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
						{
							type="Thing";
							break;
						}
					}
					String constraint = relation + "," + restriction + ","
							+ type;
					System.out.println(constraint);
					superclasses.add(constraint);
					// System.out.println(res_exact.getOnProperty().getLocalName());
					// System.out.println(res_exact.getCardinality());

					// res_exact.isLiteral();
					// ExtendedIterator<RDFNode>label=res_exact.listPropertyValues(res_exact.getOnProperty());
					// System.out.println(res_exact.getPropertyResourceValue(null));
					/*
					 * NodeIterator value=res_exact.listPropertyValues(null);
					 * System.out.println(value.hasNext()); // int count=0;
					 * while(value.hasNext()) { RDFNode a=value.next(); //
					 * System.out.println(); System.out.println(a.isLiteral());
					 * System.out.println(a); }
					 */

				} else if (res.isMaxCardinalityRestriction()) {
					System.out.println("Max!");
					MaxCardinalityRestriction res_max = res
							.asMaxCardinalityRestriction();
					relation = res_max.getOnProperty().getLocalName();
					restriction = "Max " + res_max.getMaxCardinality();
					// OntProperty pro=res_max.getOnProperty();
					NodeIterator value = res_max.listPropertyValues(null);
					while (value.hasNext()) {
						RDFNode a = value.next();
						// System.out.println();
						if (a.isLiteral()) {
							type = "Literal";
							break;
						} else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
						{
							type="Thing";
							break;
						}
					}
					String constraint = relation + "," + restriction + ","
							+ type;
					System.out.println(constraint);
					superclasses.add(constraint);
					/*
					 * //
					 * System.out.println(res_max.getOnProperty().getLocalName
					 * ()); System.out.println(res_max.getMaxCardinality()); //
					 * OntClass a=(OntClass) res_min.get(); NodeIterator
					 * value=res_max.listPropertyValues(null);
					 * System.out.println(value.hasNext()); // int count=0;
					 * while(value.hasNext()) { RDFNode a=value.next(); //
					 * System.out.println(); System.out.println(a.isLiteral());
					 * System.out.println("***************");
					 * System.out.println(a); }
					 */
				} else if (res.isMinCardinalityRestriction()) {
					System.out.println("Min!");
					MinCardinalityRestriction res_min = res
							.asMinCardinalityRestriction();
					relation = res_min.getOnProperty().getLocalName();
					restriction = "Min " + res_min.getMinCardinality();
					// OntProperty pro=res_max.getOnProperty();
					NodeIterator value = res_min.listPropertyValues(null);
					while (value.hasNext()) {
						RDFNode a = value.next();
						// System.out.println();
						if (a.isLiteral()) {
							type = "Literal";
							break;
						} 
						else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
						{
							type="Thing";
							break;
						}
					}
					String constraint = relation + "," + restriction + ","
							+ type;
					System.out.println(constraint);
					superclasses.add(constraint);
					/*
					 * OntProperty pro=res_min.getOnProperty();
					 * System.out.println("&&&&&&&&&&&&&&&&&");
					 * System.out.println
					 * (res_min.getOnProperty().getLocalName());
					 * System.out.println(res_min.getMinCardinality()); //
					 * OntClass a=(OntClass) res_min.get(); NodeIterator
					 * value=res_min.listPropertyValues(null);
					 * System.out.println(value.hasNext()); // int count=0;
					 * while(value.hasNext()) { RDFNode a=value.next(); //
					 * System.out.println(); System.out.println(a.isLiteral());
					 * System.out.println("***************");
					 * System.out.println(a); }
					 */

					/*
					 * System.out.println(res_min.getPropertyValue(pro));
					 * System.out.println(res_min.getLabel(concept));
					 */
				} else {
					System.out.println("None restriction!");
				}

			} 
			else if(Restriction.isUnionClass())
			{
				UnionClass uc = Restriction.asUnionClass();
				ExtendedIterator<? extends OntClass> uco = uc.listOperands();
				while (uco.hasNext()) 
				{
					String U_father = uco.next().getLocalName();
					ArrayList<String> new_Restrictions=FindFather_Restriction(U_father);
					for(String a:new_Restrictions)
					{
						superclasses.add(concept+","+a);
					}
				}
			}
			else if(Restriction.isIntersectionClass())
			{
				IntersectionClass ic=Restriction.asIntersectionClass();
				ExtendedIterator<? extends OntClass> uco = ic.listOperands();
				System.out.print("++++++++++++++++++++++++++");
				while (uco.hasNext()) 
				{
					OntClass Intersection= uco.next();
					if(Intersection.isRestriction())
					{
						Restriction res2 = Intersection.asRestriction();
						// System.out.println(res.isMinCardinalityRestriction());
						String relation = "";
						String restriction = "";
						String type = "";
						if (res2.isAllValuesFromRestriction()) {
							System.out.println("Only!");
							AllValuesFromRestriction res_all = res2
									.asAllValuesFromRestriction();
							relation = res_all.getOnProperty().getLocalName();
							// System.out.println(res_all.getOnProperty().getLocalName());
							OntClass a = (OntClass) res_all.getAllValuesFrom();
							restriction = "only";
							type = a.getLocalName();
							if (a.isUnionClass()) {
								UnionClass uc = a.asUnionClass();
								ExtendedIterator<? extends OntClass> uco2 = uc
										.listOperands();
								while (uco2.hasNext()) {
									type = uco2.next().getLocalName();
									String constraint = relation + "," + restriction
											+ "," + type;
									System.out.println(constraint);
									superclasses.add(constraint);
								}
							} else {
								String constraint = relation + "," + restriction + ","
										+ type;
								System.out.println(constraint);
								superclasses.add(constraint);
							}
						} 
						else if (res2.isSomeValuesFromRestriction()) {
							System.out.println("some!");
							SomeValuesFromRestriction res_some = res2
									.asSomeValuesFromRestriction();
							relation = res_some.getOnProperty().getLocalName();
							// System.out.println(res_some.getOnProperty().getLocalName());
							OntClass a = (OntClass) res_some.getSomeValuesFrom();
							restriction = "some";
							type = a.getLocalName();
							if (a.isUnionClass()) {
								UnionClass uc = a.asUnionClass();
								ExtendedIterator<? extends OntClass> uco2 = uc
										.listOperands();
								while (uco2.hasNext()) {
									type = uco2.next().getLocalName();
									String constraint = relation + "," + restriction
											+ "," + type;
									System.out.println(constraint);
									superclasses.add(constraint);
								}
							} else {
								String constraint = relation + "," + restriction + ","
										+ type;
								System.out.println(constraint);
								superclasses.add(constraint);
							}
						} 
					}
					
					
					//System.out.print(Restriction);
					
					/*ArrayList<String> new_Restrictions=FindFather_Restriction(U_father);
					for(String a:new_Restrictions)
					{
						superclasses.add(concept+","+a);
					}*/
				}
			}
			else if(Restriction!=null)//�������ã���Ϊ�ø���̳��丸�׵���������
			{
				String Father=Restriction.getLocalName().toString();
				if(!Father.equals("Thing"))
				{
						ArrayList<String> new_Restrictions=FindFather_Restriction(Restriction.getLocalName().toString());
						for(String a:new_Restrictions)
						{
							superclasses.add(a);
						}
				}
			}
		}

		// temp.getSuperClass();

		return superclasses;
	}
	
	
	public ArrayList<String> FindFather_Restriction(String concept )//�����Ϳ��Ա�����ܳ��ֵĵ����������
	{
		ArrayList<String> superclasses=new ArrayList<String>();
		//OntClass temp = null;
		OntClass temp ;
		System.out.println(concept);
		temp = ontology.getOntClass(OntoID + concept);
		if(temp==null)
			return null;
				ExtendedIterator<OntClass> superclass=temp.listSuperClasses(false);
				while(superclass.hasNext())
				{
					OntClass Restriction=superclass.next();		
					////���ӹ�ϵʽ����Ҫ�ˣ�ֻ��Ҫ��ȡ�����������
					if(Restriction.isRestriction())
					{							
						System.out.println("****************************");
						//System.out.println(Restriction.isRestriction());
						//System.out.println(Restriction);
						Restriction res=Restriction.asRestriction();
						//System.out.println(res.isMinCardinalityRestriction());
						String relation="";
						String restriction="";
						String type="";	
						if(res.isAllValuesFromRestriction())
						{
							System.out.println("Only!");
							AllValuesFromRestriction res_all= res.asAllValuesFromRestriction();
							relation=res_all.getOnProperty().getLocalName();
							//System.out.println(res_all.getOnProperty().getLocalName());
							OntClass a=(OntClass) res_all.getAllValuesFrom();
							restriction="only";
							type=a.getLocalName();
							if(a.isUnionClass())
							{
								UnionClass uc = a.asUnionClass();
								ExtendedIterator<? extends OntClass> uco = uc.listOperands();
								while (uco.hasNext())
								{
									type=uco.next().getLocalName();
									String constraint=relation+","+restriction+","+type;
									System.out.println(constraint);
									superclasses.add(constraint);
								}
							}
							else
							{
								String constraint=relation+","+restriction+","+type;
								System.out.println(constraint);
								superclasses.add(constraint);
							}
						}
						else if(res.isSomeValuesFromRestriction())
						{
							System.out.println("some!");
							SomeValuesFromRestriction res_some= res.asSomeValuesFromRestriction();
							relation=res_some.getOnProperty().getLocalName();
							//System.out.println(res_some.getOnProperty().getLocalName());
							OntClass a=(OntClass) res_some.getSomeValuesFrom();
							restriction="some";
							type=a.getLocalName();
							if(a.isUnionClass())
							{
								UnionClass uc = a.asUnionClass();
								ExtendedIterator<? extends OntClass> uco = uc.listOperands();
								while (uco.hasNext())
								{
									type=uco.next().getLocalName();
									String constraint=relation+","+restriction+","+type;
									System.out.println(constraint);
									superclasses.add(constraint);
								}
							}
							else
							{
								String constraint=relation+","+restriction+","+type;
								System.out.println(constraint);
								superclasses.add(constraint);
							}
						}
						else if(res.isHasValueRestriction())
						{
							System.out.println("hasvalue!!");
							HasValueRestriction res_hasvalue= res.asHasValueRestriction();
							relation=res_hasvalue.getOnProperty().getLocalName();
							//System.out.println(res_hasvalue.getOnProperty().getLocalName());
							restriction="value";
							type=res_hasvalue.getHasValue().toString();
							String constraint=relation+","+restriction+","+type;
							System.out.println(constraint);
							superclasses.add(constraint);
						}
						else if(res.isCardinalityRestriction())
						{
							System.out.println("exactly!");
							CardinalityRestriction res_exact= res.asCardinalityRestriction();
							relation=res_exact.getOnProperty().getLocalName();
							restriction="exactly "+res_exact.getCardinality();
							NodeIterator  value=res_exact.listPropertyValues(null);
						    while(value.hasNext())
						   {
									RDFNode a=value.next();
								//	System.out.println();
								if(a.isLiteral())									
							   {
								  type="Literal";
								  break;
							   }
								else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
								{
									type="Thing";
									break;
								}																		
							}
						    String constraint=relation+","+restriction+","+type;
							System.out.println(constraint);	
							superclasses.add(constraint);
							//System.out.println(res_exact.getOnProperty().getLocalName());
							//System.out.println(res_exact.getCardinality());
							
							//res_exact.isLiteral();
							//ExtendedIterator<RDFNode>label=res_exact.listPropertyValues(res_exact.getOnProperty());
						//	System.out.println(res_exact.getPropertyResourceValue(null));
	/*					  NodeIterator  value=res_exact.listPropertyValues(null);
						 System.out.println(value.hasNext());
					//	 int count=0;
						 while(value.hasNext())
							{
								RDFNode a=value.next();
							//	System.out.println();
								System.out.println(a.isLiteral());
								System.out.println(a);													
							}*/
							
						}
						else if(res.isMaxCardinalityRestriction())
						{
							System.out.println("Max!");
							MaxCardinalityRestriction res_max= res.asMaxCardinalityRestriction();
							relation=res_max.getOnProperty().getLocalName();
							restriction="Max "+res_max.getMaxCardinality();
							//OntProperty pro=res_max.getOnProperty();							
							NodeIterator  value=res_max.listPropertyValues(null);
						    while(value.hasNext())
						   {
									RDFNode a=value.next();
								//	System.out.println();
								if(a.isLiteral())									
							   {
								  type="Literal";
								  break;
							   }
								else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
								{
									type="Thing";
									break;
								}																			
							}
						    String constraint=relation+","+restriction+","+type;
							System.out.println(constraint);	
							superclasses.add(constraint);
						/*//	System.out.println(res_max.getOnProperty().getLocalName());
							System.out.println(res_max.getMaxCardinality());							
						//	OntClass a=(OntClass) res_min.get();							
							  NodeIterator  value=res_max.listPropertyValues(null);							  
								 System.out.println(value.hasNext());
							//	 int count=0;
								 while(value.hasNext())
									{
										RDFNode a=value.next();
									//	System.out.println();
										System.out.println(a.isLiteral());
										System.out.println("***************");		
										System.out.println(a);													
									}*/
						}
						else if(res.isMinCardinalityRestriction())
						{
							System.out.println("Min!");
							MinCardinalityRestriction res_min= res.asMinCardinalityRestriction();
							relation=res_min.getOnProperty().getLocalName();
							restriction="Min "+res_min.getMinCardinality();
							//OntProperty pro=res_max.getOnProperty();							
							NodeIterator  value=res_min.listPropertyValues(null);
						    while(value.hasNext())
						   {
									RDFNode a=value.next();
								//	System.out.println();
								if(a.isLiteral())									
							   {
								  type="Literal";
								  break;
							   }
								else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
								{
									type="Thing";
									break;
								}																		
							}
						    String constraint=relation+","+restriction+","+type;
							System.out.println(constraint);
							superclasses.add(constraint);
							/*OntProperty pro=res_min.getOnProperty();							
							System.out.println("&&&&&&&&&&&&&&&&&");		
							System.out.println(res_min.getOnProperty().getLocalName());
							System.out.println(res_min.getMinCardinality());							
						//	OntClass a=(OntClass) res_min.get();							
							  NodeIterator  value=res_min.listPropertyValues(null);							  
								 System.out.println(value.hasNext());
							//	 int count=0;
								 while(value.hasNext())
									{
										RDFNode a=value.next();
									//	System.out.println();
										System.out.println(a.isLiteral());
										System.out.println("***************");		
										System.out.println(a);													
									}*/
								 								 
							/*System.out.println(res_min.getPropertyValue(pro));
							System.out.println(res_min.getLabel(concept));*/							
						}
						else 
						{
							System.out.println("None restriction!");
						}					
				}
					else if(Restriction.isUnionClass())
					{
						UnionClass uc = Restriction.asUnionClass();
						ExtendedIterator<? extends OntClass> uco = uc.listOperands();
						while (uco.hasNext()) 
						{
							String U_father = uco.next().getLocalName();
							ArrayList<String> new_Restrictions=FindFather_Restriction(U_father);
							for(String a:new_Restrictions)
							{
								superclasses.add(concept+","+a);
							}
						}
					}
					else if(Restriction.isIntersectionClass())
					{
						IntersectionClass ic=Restriction.asIntersectionClass();
						ExtendedIterator<? extends OntClass> uco = ic.listOperands();
						System.out.print("++++++++++++++++++++++++++");
						while (uco.hasNext()) 
						{
							OntClass Intersection= uco.next();
							if(Intersection.isRestriction())
							{
								Restriction res2 = Intersection.asRestriction();
								// System.out.println(res.isMinCardinalityRestriction());
								String relation = "";
								String restriction = "";
								String type = "";
								if (res2.isAllValuesFromRestriction()) {
									System.out.println("Only!");
									AllValuesFromRestriction res_all = res2
											.asAllValuesFromRestriction();
									relation = res_all.getOnProperty().getLocalName();
									// System.out.println(res_all.getOnProperty().getLocalName());
									OntClass a = (OntClass) res_all.getAllValuesFrom();
									restriction = "only";
									type = a.getLocalName();
									if (a.isUnionClass()) {
										UnionClass uc = a.asUnionClass();
										ExtendedIterator<? extends OntClass> uco2 = uc
												.listOperands();
										while (uco2.hasNext()) {
											type = uco2.next().getLocalName();
											String constraint = relation + "," + restriction
													+ "," + type;
											System.out.println(constraint);
											superclasses.add(constraint);
										}
									} else {
										String constraint = relation + "," + restriction + ","
												+ type;
										System.out.println(constraint);
										superclasses.add(constraint);
									}
								} else if (res2.isSomeValuesFromRestriction()) {
									System.out.println("some!");
									SomeValuesFromRestriction res_some = res2
											.asSomeValuesFromRestriction();
									relation = res_some.getOnProperty().getLocalName();
									// System.out.println(res_some.getOnProperty().getLocalName());
									OntClass a = (OntClass) res_some.getSomeValuesFrom();
									restriction = "some";
									type = a.getLocalName();
									if (a.isUnionClass()) {
										UnionClass uc = a.asUnionClass();
										ExtendedIterator<? extends OntClass> uco2 = uc
												.listOperands();
										while (uco2.hasNext()) {
											type = uco2.next().getLocalName();
											String constraint = relation + "," + restriction
													+ "," + type;
											System.out.println(constraint);
											superclasses.add(constraint);
										}
									} else {
										String constraint = relation + "," + restriction + ","
												+ type;
										System.out.println(constraint);
										superclasses.add(constraint);
									}
								} 
							}
						}
					}
			}				
		//temp.getSuperClass();
		return superclasses;
	}
	
	public ArrayList<String> findFatherSomeRestriction(String concept )//�����Ϳ��Ա�����ܳ��ֵĵ����������
	{
		ArrayList<String> superclasses=new ArrayList<String>();
		//OntClass temp = null;
		OntClass temp ;
		System.out.println(concept);
		temp = ontology.getOntClass(OntoID + concept);
		if(temp==null)
			return null;
		ExtendedIterator<OntClass> superclass=temp.listSuperClasses(false);
		while(superclass.hasNext())
		{
			OntClass Restriction=superclass.next();		
			////���ӹ�ϵʽ����Ҫ�ˣ�ֻ��Ҫ��ȡ�����������
			if(Restriction.isRestriction())
			{							
				System.out.println("****************************");
				//System.out.println(Restriction.isRestriction());
				//System.out.println(Restriction);
				Restriction res=Restriction.asRestriction();
				//System.out.println(res.isMinCardinalityRestriction());
				String relation="";
				String restriction="";
				String type="";	
				if(res.isSomeValuesFromRestriction())
				{
					System.out.println("some!");
					SomeValuesFromRestriction res_some= res.asSomeValuesFromRestriction();
					relation=res_some.getOnProperty().getLocalName();
					//System.out.println(res_some.getOnProperty().getLocalName());
					OntClass a=(OntClass) res_some.getSomeValuesFrom();
					restriction="some";
					type=a.getLocalName();
					if(a.isUnionClass())
					{
						UnionClass uc = a.asUnionClass();
						ExtendedIterator<? extends OntClass> uco = uc.listOperands();
						while (uco.hasNext())
						{
							type=uco.next().getLocalName();
							String constraint=relation+","+restriction+","+type;
							System.out.println(constraint);
							superclasses.add(constraint);
						}
					}
					else
					{
						String constraint=relation+","+restriction+","+type;
						System.out.println(constraint);
						superclasses.add(constraint);
					}
				}					
			}
		}				
		//temp.getSuperClass();
		return superclasses;
	}
	
	public ArrayList<String> GetAllRestrictions()//�ҵ�ĳ���������и������������
	{
		ArrayList<String> Restrictions=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //�����ı��������������ʽ����
	    OntClass temp = null; 
	    
		while(classlist.hasNext())
		{
			temp=classlist.next();	
			String concept=temp.getLocalName();
			if(concept.equals("Nothing")||concept.equals("Thing"))
				continue;
		    //��ӡ������һ�ַ�ʽ
			//System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			//System.out.println(concept);
			ExtendedIterator<OntClass> superclass=temp.listSuperClasses(false);
			while(superclass.hasNext())
			{
				OntClass Restriction=superclass.next();		
				////���ӹ�ϵʽ����Ҫ�ˣ�ֻ��Ҫ��ȡ�����������
				if(Restriction.isRestriction())
				{							
					//System.out.println("****************************");
					// System.out.println(Restriction.isRestriction());
					// System.out.println(Restriction);
					Restriction res = Restriction.asRestriction();
					// System.out.println(res.isMinCardinalityRestriction());
					String relation = "";
					String restriction = "";
					String type = "";
					if (res.isAllValuesFromRestriction()) 
					{
						//System.out.println("Only!");
						AllValuesFromRestriction res_all = res
								.asAllValuesFromRestriction();
						relation = res_all.getOnProperty().getLocalName();
						//System.out.println(res_all.getOnProperty().getLocalName
						// ());
						OntClass a = (OntClass) res_all.getAllValuesFrom();
						restriction = "only";
						type = a.getLocalName();
						if (a.isUnionClass()) 
						{
							UnionClass uc = a.asUnionClass();
							ExtendedIterator<? extends OntClass> uco = uc
									.listOperands();
							while (uco.hasNext()) {
								type = uco.next().getLocalName();
								String constraint = concept+","+relation + ","
										+ restriction + "," + type;
								//System.out.println(constraint);
								Restrictions.add(constraint);
							}
						} 
						else 
						{
							String constraint = concept+","+relation + "," + restriction
									+ "," + type;
							//System.out.println(constraint);
							Restrictions.add(constraint);
						}
					} 
					else if (res.isSomeValuesFromRestriction()) 
					{
						//System.out.println("some!");
						SomeValuesFromRestriction res_some = res
								.asSomeValuesFromRestriction();
						relation = res_some.getOnProperty().getLocalName();
						// System.out.println(res_some.getOnProperty().
						// getLocalName());
						OntClass a = (OntClass) res_some.getSomeValuesFrom();
						restriction = "some";
						type = a.getLocalName();
						if (a.isUnionClass()) 
						{
							UnionClass uc = a.asUnionClass();
							ExtendedIterator<? extends OntClass> uco = uc
									.listOperands();
							while (uco.hasNext()) {
								type = uco.next().getLocalName();
								String constraint = concept+","+relation + ","
										+ restriction + "," + type;
								//System.out.println(constraint);
								Restrictions.add(constraint);
							}
						} 
						else 
						{
							String constraint = concept+","+relation + "," + restriction
									+ "," + type;
							//System.out.println(constraint);
							Restrictions.add(constraint);
						}
					} 
					else if (res.isHasValueRestriction()) 
					{
						//System.out.println("hasvalue!!");
						HasValueRestriction res_hasvalue = res
								.asHasValueRestriction();
						relation = res_hasvalue.getOnProperty().getLocalName();
						// System.out.println(res_hasvalue.getOnProperty().
						// getLocalName());
						restriction = "value";
						type = res_hasvalue.getHasValue().toString();						
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						//System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else if (res.isCardinalityRestriction()) 
					{
						//System.out.println("exactly!");
						CardinalityRestriction res_exact = res
								.asCardinalityRestriction();
						relation = res_exact.getOnProperty().getLocalName();
						restriction = "exactly," + res_exact.getCardinality();
						NodeIterator value = res_exact.listPropertyValues(null);
						/*try
						{*/
						while (value.hasNext()) 
						{				
							RDFNode a = value.next();
							System.out.println(a);
							// System.out.println();
							if (a.isLiteral()) {
								type = "Literal";
								break;
							}
							else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
							{
								type="Thing";
								break;
							}
						}
						/*}
						catch(Exception e)
						{
							System.out.print("11");
						}*/
						
						
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						//System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else if (res.isMaxCardinalityRestriction()) 
					{
						//System.out.println("Max!");
						MaxCardinalityRestriction res_max = res
								.asMaxCardinalityRestriction();
						relation = res_max.getOnProperty().getLocalName();
						restriction = "Max," + res_max.getMaxCardinality();
						// OntProperty pro=res_max.getOnProperty();
						NodeIterator value = res_max.listPropertyValues(null);
						while (value.hasNext()) {
							RDFNode a = value.next();
							// System.out.println();
							if (a.isLiteral()) {
								type = "Literal";
								break;
							} 
							else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
							{
								type="Thing";
								break;
							}
						}
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						//System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else if (res.isMinCardinalityRestriction())
					{
						//System.out.println("Min!");
						MinCardinalityRestriction res_min = res
								.asMinCardinalityRestriction();
						relation = res_min.getOnProperty().getLocalName();
						restriction = "Min," + res_min.getMinCardinality();
						// OntProperty pro=res_max.getOnProperty();
						NodeIterator value = res_min.listPropertyValues(null);
						while (value.hasNext()) {
							RDFNode a = value.next();
							// System.out.println();
							if (a.isLiteral()) {
								type = "Literal";
								break;
							} 
							else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
							{
								type="Thing";
								break;
							}
						}
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						//System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else 
					{
						System.out.println("None restriction!");
					}					
				}
				else if(Restriction.isUnionClass())
				{
					UnionClass uc = Restriction.asUnionClass();
					ExtendedIterator<? extends OntClass> uco = uc.listOperands();
					while (uco.hasNext()) 
					{
						String U_father = uco.next().getLocalName();
						ArrayList<String> new_Restrictions=FindFather_Restriction(U_father);
						for(String a:new_Restrictions)
						{
							Restrictions.add(concept+","+a);
						}
					}
				}
				else if(Restriction.isIntersectionClass())
				{
					IntersectionClass ic=Restriction.asIntersectionClass();
					ExtendedIterator<? extends OntClass> uco = ic.listOperands();
					System.out.print("++++++++++++++++++++++++++");
					while (uco.hasNext()) 
					{
						OntClass Intersection= uco.next();
						if(Intersection.isRestriction())
						{
							Restriction res2 = Intersection.asRestriction();
							// System.out.println(res.isMinCardinalityRestriction());
							String relation = "";
							String restriction = "";
							String type = "";
							if (res2.isAllValuesFromRestriction()) {
								System.out.println("Only!");
								AllValuesFromRestriction res_all = res2
										.asAllValuesFromRestriction();
								relation = res_all.getOnProperty().getLocalName();
								// System.out.println(res_all.getOnProperty().getLocalName());
								OntClass a = (OntClass) res_all.getAllValuesFrom();
								restriction = "only";
								type = a.getLocalName();
								if (a.isUnionClass()) {
									UnionClass uc = a.asUnionClass();
									ExtendedIterator<? extends OntClass> uco2 = uc
											.listOperands();
									while (uco2.hasNext()) {
										type = uco2.next().getLocalName();
										String constraint = relation + "," + restriction
												+ "," + type;
										System.out.println(constraint);
										Restrictions.add(constraint);
									}
								} else {
									String constraint = relation + "," + restriction + ","
											+ type;
									System.out.println(constraint);
									Restrictions.add(constraint);
								}
							} else if (res2.isSomeValuesFromRestriction()) {
								System.out.println("some!");
								SomeValuesFromRestriction res_some = res2
										.asSomeValuesFromRestriction();
								relation = res_some.getOnProperty().getLocalName();
								// System.out.println(res_some.getOnProperty().getLocalName());
								OntClass a = (OntClass) res_some.getSomeValuesFrom();
								restriction = "some";
								type = a.getLocalName();
								if (a.isUnionClass()) {
									UnionClass uc = a.asUnionClass();
									ExtendedIterator<? extends OntClass> uco2 = uc
											.listOperands();
									while (uco2.hasNext()) {
										type = uco2.next().getLocalName();
										String constraint = relation + "," + restriction
												+ "," + type;
										System.out.println(constraint);
										Restrictions.add(constraint);
									}
								} else {
									String constraint = relation + "," + restriction + ","
											+ type;
									System.out.println(constraint);
									Restrictions.add(constraint);
								}
							} 
						}
					}
				}
				else if(Restriction!=null)//�������ã���Ϊ�ø���̳��丸�׵���������
				{
					String Father=Restriction.getLocalName().toString();
					if(!Father.equals("Thing"))
					{
							ArrayList<String> new_Restrictions=FindFather_Restriction(Restriction.getLocalName().toString());
							if(new_Restrictions!=null)//��Ϊ�������չ�����ʽ�޷���ý�������Ҫ�����ֿ�ָ���������˵�
							{
								for(String a:new_Restrictions)
								{
									Restrictions.add(concept+","+a);
								}
							}
					}				
				}
			}
		}			
				
		//temp.getSuperClass();
		return Restrictions;
	}
	
	public ArrayList<String> GetSomeRestrictions()//�ҵ�ĳ���������и������������
	{
		ArrayList<String> someRestrictions=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //�����ı��������������ʽ����
	    OntClass temp = null; 
	    
		while(classlist.hasNext())
		{
			temp=classlist.next();	
			String concept=temp.getLocalName();
			if(concept.equals("Nothing"))
				continue;
		    //��ӡ������һ�ַ�ʽ
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			System.out.println(concept);
			ExtendedIterator<OntClass> superclass=temp.listSuperClasses(false);
			while(superclass.hasNext())
			{
				OntClass Restriction=superclass.next();		
				////���ӹ�ϵʽ����Ҫ�ˣ�ֻ��Ҫ��ȡ�����������
				if(Restriction.isRestriction())
				{							
					//System.out.println("****************************");
					// System.out.println(Restriction.isRestriction());
					// System.out.println(Restriction);
					Restriction res = Restriction.asRestriction();
					// System.out.println(res.isMinCardinalityRestriction());
					String relation = "";
					String restriction = "";
					String type = "";
					if (res.isSomeValuesFromRestriction()) 
					{
						//System.out.println("some!");
						SomeValuesFromRestriction res_some = res.asSomeValuesFromRestriction();
						relation = res_some.getOnProperty().getLocalName();
						// System.out.println(res_some.getOnProperty().
						// getLocalName());
						OntClass a = (OntClass) res_some.getSomeValuesFrom();
						restriction = "some";
						type = a.getLocalName();
						if (a.isUnionClass()) 
						{
							UnionClass uc = a.asUnionClass();
							ExtendedIterator<? extends OntClass> uco = uc.listOperands();
							while (uco.hasNext()) {
								type = uco.next().getLocalName();
								String constraint = concept+","+relation + ","+ restriction + "," + type;
								//System.out.println(constraint);
								someRestrictions.add(constraint);
							}
						} 
						else 
						{
							String constraint = concept+","+relation + "," + restriction
									+ "," + type;
							//System.out.println(constraint);
							someRestrictions.add(constraint);
						}
					} 
				else if(!Restriction.isAnon())//�������ã���Ϊ�ø���̳��丸�׵���������
				{
					String Father=Restriction.getLocalName().toString();
					if(!Father.equals("Thing"))
					{
							ArrayList<String> new_Restrictions=findFatherSomeRestriction(Restriction.getLocalName().toString());
							if(new_Restrictions!=null)//��Ϊ�������չ�����ʽ�޷���ý�������Ҫ�����ֿ�ָ���������˵�
							{
								for(String a:new_Restrictions)
								{
									someRestrictions.add(concept+","+a);
								}
							}
					}				
				}
			}
		}
	 }			
		//temp.getSuperClass();
		return someRestrictions;
	}
	
	public int GetConcept_Path(String concept)
	{	
		//ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String b="";
		System.out.println("����Ϊ:"+concept);
		OntClass temp=ontology.getOntClass(OntoID+concept);
		int depth=0;
		//boolean flag=false;
			ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(false);//false��ʾ�г����е����࣬true��ʾֻ�г�ֱ������
			if(classlist1.hasNext()){				
				OntClass temp1;
				while(classlist1.hasNext()){
					temp1=classlist1.next();
					if(temp1.isRestriction()||(temp1.getLocalName()==null))
					{
						continue;
					}
					else
					{
						b=temp1.getLocalName().toString();
						System.out.println(b);				
					}
					if(b.equals("Thing")||b.equals("Resource"))//�����丸����Resource����Thing������
					{
						continue;
					}
					else //���������ĸ��׸�����ȼ�1
					{
						depth++;
					}
			 }
			}
			depth=depth+1;
		return depth;
	}
}
