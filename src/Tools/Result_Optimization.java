package Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result_Optimization {
	
	public ArrayList<String> Stable_Marriage(double [][]array)
    {
		List<Map.Entry<Integer,Double>> value = new ArrayList<Map.Entry<Integer,Double>>();
		HashMap<Integer,Double> map=new HashMap<Integer,Double>();  //�洢ÿһ��
		int num=0;
		for(int i=0;i<array.length;i++)
		{
			for(int j=0;j<array[0].length;j++)
			{
				map.put(num, array[i][j]);  //����ÿһ�н�������
				num++;
			}			
		}		
		//���ú����������������������(���������������)
		value.addAll(map.entrySet());
		Collections.sort(value, new Comparator<Map.Entry<Integer, Double>>() {   
			public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {      
				//return (o2.getValue() - o1.getValue()); 
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});				
		ArrayList<Double> Stable_value=new ArrayList<Double>();
		ArrayList<Integer> Used_row_index=new ArrayList<Integer>();
		ArrayList<Integer> Used_column_index=new ArrayList<Integer>();
		for(int i=0;i<value.size();i++)
		{
			int index=value.get(i).getKey();	//������±�Ҳ�Ǵ�0��ʼ��
			int row_index=index/array[0].length;//��λ���ڼ��еڼ���	
			int column_index=index%array[0].length;
			//ֵ�ĸ����϶����ᳬ���������ݵ��У��н�С������֤�����е�������û�б��ù�
			if(Stable_value.size()<=array.length&&!Used_row_index.contains(row_index)&&!Used_column_index.contains(column_index))
			{
				Used_row_index.add(row_index);
				Used_column_index.add(column_index);
				Stable_value.add(value.get(i).getValue());
			}
			else if(Stable_value.size()>array.length)
				break;
		}
		ArrayList<String> pair=new ArrayList<String>();
		//��ֵ�������±����б����ʽ���뵽������
		double global_sum=0;
		for(int i=0;i<Stable_value.size();i++)
		{
			String used_index=Used_row_index.get(i)+","+Used_column_index.get(i);
			double similarity=Stable_value.get(i);
			pair.add(used_index+","+similarity);
			global_sum=global_sum+similarity;
		}	
		System.out.println("Stable������ֵΪ:"+global_sum);
    	return pair;
    }
	
	public int[] FindGreedySim_Pair(double [][]array,String []str1,String []str2)
    {
    	double sim=-0.1;
    	double sum=0;   
    	//int min_length=Math.min(str1.length, str2.length);//ֻȥ�����ٵĳ���
    	int pair[]=new int[str1.length];//���շ��ص�ƥ��Եĳ���Ӧ������С���ַ����ĳ���
    	for(int i=0;i<array[0].length;i++)//������õ��Ǳ����ķ���(�����Ժ�Ҫ�õ���̬�滮)
    	{  
    		//Ԥ�ȶ���һ���������Ҵ洢��һ��ĳ�еķ���
    		ArrayList<Integer> labeled_row=new ArrayList<Integer>();
    		labeled_row.add(i);
    		sum=array[0][i];
    		for(int k=1;k<array.length;k++)
    		{
    			int label=FindsuitLabel(array[k],labeled_row);
    			labeled_row.add(label);  
    			sum=sum+array[k][label];
    		}		
    		if(sim<sum)
    		{
    			sim=sum; //��ȡ���ƶ��������   		
    			for(int L=0;L<labeled_row.size();L++)
    				pair[L]=labeled_row.get(L);
    		}
    	}
    	return pair;
    }
	
	public int FindsuitLabel(double []row,ArrayList<Integer> label)//ͨ���ѱ�ǵı�ǩ����ÿһ�е����ֵ
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
    	return suit_label;//��ʵ�ҵ��Ķ�Ӧ�ı�ǩ��Ҳ��֪�������ֵ�Ƕ��١���Ϊ�±����������
    }
	
	public ArrayList<Integer> Dynamic_Programming(double [][]array)
	{
		//����Ϊͳ�Ƹ����Զ����½����ۼ�
		double global_sum=0;
		ArrayList<Integer> global_index=new ArrayList<Integer>();		
		double sum[]=new double[array[0].length];
		HashMap<Integer,ArrayList<Integer>> order=new HashMap<Integer,ArrayList<Integer>>();
		//��Ҫ��HashMap��ʼ��
		for(int k=0;k<array[0].length;k++)
		{
			//ArrayList<Integer> a=new ArrayList<Integer>();
			
			order.put(k, new ArrayList<Integer>());
		}	
		//��ʼ��
		for(int i=0;i<array[0].length;i++)
		{
			sum[i]=array[0][i];
			//order.get(i).add(i);
		}
		
		for(int L=1;L<array.length;L++)
		{	
			double[] new_sum=sum.clone();//�ñ��ݵ������������
			for(int i=0;i<array[0].length;i++)//�����µ�һ���е�ÿһ��
			{
				int index=0;			
				double local_sum=0;
				
				for(int j=0;j<array[0].length;j++)//�����һ���е�ǰһ��
				{				
					double value=array[L][i]+sum[j];
					if(local_sum<value&&!order.get(i).contains(j))
					//if(local_sum<value)
					{
						local_sum=value;
						index=j;//�������У�ǰһ���е��±�
					}
				}
				new_sum[i]=local_sum;	//�洢��ǰ�����ֵ
				order.get(i).add(index); //�洢��ǰ���ֵ��Ӧ��ǰһ�е��±�
			}
			sum=new_sum.clone();//����sum��ֵ���Ĳ���Ժ����������Ӱ��,�ر��ǵڶ��� 
		}
		double max_Value=0;
		int last_index=0;
		for(int i=0;i<array[0].length;i++)
		{
			if(max_Value<sum[i]&&!order.get(i).contains(i))
			{
				max_Value=sum[i];
				last_index=i;
			}
		}
		//����
		ArrayList<Integer> backtrack=new ArrayList<Integer>();
		backtrack.add(last_index);
		int index=last_index;
		for(int L=array.length-2;L>=0;L--)
		{
			index=order.get(index).get(L);
			backtrack.add(index);
		}
		//�����ݵ�˳�򵽹���
		for(int i=backtrack.size()-1;i>=0;i--)
		{
			global_index.add(backtrack.get(i));
		}							
		return global_index;
	}
	
	/*public ArrayList<Integer> Beam_Search(double [][]array,int n)//������������Ķ�̬�滮��������̰��
	{
		//����Ϊͳ�Ƹ����Զ����½����ۼ�,����Ҫ�������ֱ�ѡ������ȡ����һ�֡������ƽ��ķ����������ھ������ʱ������ȫ������
		double global_sum=0;
		ArrayList<Integer> global_index=new ArrayList<Integer>();		//�洢ȫ������ʱÿһ�ж�Ӧ���±�
		double sum[][]=new double[n][array[0].length];			//��ʼ��ÿһ�еĿ�ͷ��ֵ
		//HashMap<Integer,ArrayList<Integer>> order=new HashMap<Integer,ArrayList<Integer>>();//����ȷ���Ƿ�����±��ظ������
		//HashMap<Integer,ArrayList<Integer>> []order=new HashMap[n];	//Hasmap�����ʼ���ķ���
		HashMap<Integer,ArrayList<Integer>[]> order=new HashMap<Integer,ArrayList<Integer>[]>();	
		//��Ҫ��HashMap��ʼ��
		for(int i=0;i<array[0].length;i++)	//�������еĴ�Сn��ȷ����ѡ��Map�ĸ���
		{
			ArrayList<Integer> a[]=new ArrayList[n];
			for(int j=0;j<n;j++)
				a[j]= new ArrayList<Integer>();
			order.put(i, a);
		}	
		//ArrayList<Integer> a[]=new ArrayList[n];	//���������ʼ���ķ���
		sum[0]=array[0].clone();
		for(int i=0;i<n;i++)//�������Ҫ�����ʼ��ʱǰn������ǰk����ѡֵ����ȵ�
		{
			for(int k=0;k<array[0].length;k++)//Ϊ�˱�֤�䲻�ظ���Ӧ��ֻ���ʼ����һ��
			{
				sum[i][k]=array[0][k];
			}
		}	
		//����һ����С��������,Ӧ�����������ٴ洢
		for(int L=1;L<array.length;L++)
		{
			double	[][]new_sum=new double[n][array[0].length];
			for(int w=0;w<n;w++)
			{
					new_sum[w]=sum[w].clone();//�ñ��ݵ������������,��ά����Ŀ�����1ά���鲻ͬ	
			}
			for(int i=0;i<array[0].length;i++)//�����µ�һ���е�ÿһ��
			{
				int index=0;			
				double local_sum=0;
				int new_candidate[]=new int[n];
				double local_sum[]=new double[n];
				int index[]=new int[n];
				for(int C=0;C<n;C++)  	
				{
					index[C]=-1;
					local_sum[C]=-C-1;	//����Ĭ�ϵĽ�������Ϳ���ʡ������Ĺ���									
				}
				//ArrayList<Integer>[]index=order.get(i).clone();//����i�Ĵ洢��index���п�¡
				//��local_sum�������������ڶ�Ӧ���±궼��0�������һ������ʡ�ԣ�
				//sort(local_sum,0,n-1);
				for(int j=0;j<array[0].length;j++)//�����һ���е�ÿһ�н��м���
				{	
					for(int candidate_num=0;candidate_num<n;candidate_num++)
					{
						double value=array[L][i]+sum[candidate_num][j];	
						System.out.println(order.get(i)[candidate_num].size());
						System.out.println(order.get(i)[candidate_num].contains(j));
						for(int t=0;t<order.get(i)[candidate_num].size();t++)
							System.out.println(order.get(i)[candidate_num].get(t));
						//Ĭ���ǽ���ģ������Ҫ����С���Ǹ����бȽ�
						if(value>local_sum[n-1]&&!order.get(i)[candidate_num].contains(j))//��Ϊ�ǽ���ֻ��Ҫ�����һ��ֵ���бȽ�
						{
							
							int change_index=Change_local_value(local_sum,index,value,j);
							int change_index=Change_local_value(local_sum,value);//����
							index[change_index]=j;			//Ҳ��Ҫ�޸�			
							index[candidate_num].remove(change_index);
							index[candidate_num].add(change_index, j);//���ﲢû��insert							
						}							
					}
				}
				System.out.println("*******************************");
				for(int candidate_num=0;candidate_num<n;candidate_num++)  
				{
					new_sum[candidate_num][i]=local_sum[candidate_num];	//������Ľ�����б���
					order.get(i)[candidate_num].add(index[candidate_num]);
				}
			}
			sum=new_sum.clone();
		}
			
		//����Ҫһ���������ҵ�sum�е����ֵ
		double max_Value=0;
		int last_index=0;
		int last_candidate=0;
		for(int k=0;k<n;k++)
		{
			for(int i=0;i<array[0].length;i++)
			{
				if(max_Value<sum[k][i]&&!order.get(i)[k].contains(i))
				{
					max_Value=sum[k][i];
					last_index=i;
					last_candidate=k;
				}
			}
		}
		//����
		ArrayList<Integer> backtrack=new ArrayList<Integer>();
		backtrack.add(last_index);
		int index=last_index;
		for(int L=array.length-2;L>=0;L--)//���ݵĶ���ͬ��,�ҵ������Ǹ�index�µ����꼴��
		{
			index=order.get(index)[last_candidate].get(L);
			backtrack.add(index);
		}
		//�����ݵ�˳�򵽹���
		for(int i=backtrack.size()-1;i>=0;i--)
		{
			global_index.add(backtrack.get(i));
		}							
		return global_index;
	}*/
	
	public ArrayList<Integer> Beam_Search(double [][]array,int n)//�������������Beam_Search
	{
		//����Ϊͳ�Ƹ����Զ����½����ۼ�,����Ҫ�������ֱ�ѡ������ȡ����һ�֡������ƽ��ķ����������ھ������ʱ������ȫ������	
		ArrayList<Integer> global_index=new ArrayList<Integer>();		//�洢ȫ������ʱÿһ�ж�Ӧ���±�		
		int m=array[0].length;  //mΪ�������
		class Node
		{
			double value;
			int index1;		//������Ӧ��valueֵ��һ�еĵڼ���
			int index2;		//������Ӧ��valueֵԭʼ�����еĵڼ������		
		}
		
		//��Ҫ��HashMap��ʼ��
		HashMap<Integer,ArrayList<Integer>[]> order=new HashMap<Integer,ArrayList<Integer>[]>();	
		for(int i=0;i<array[0].length;i++)	//�������еĴ�Сn��ȷ����ѡ��Map�ĸ���
		{
			ArrayList<Integer> a[]=new ArrayList[n];
			for(int j=0;j<n;j++)
				a[j]= new ArrayList<Integer>();
			order.put(i, a);
		}	
		
		for(int k=0;k<array[0].length;k++)//Ĭ�Ͻ�ÿһ�еĵ�һ��д��Map
		{
			for(int i=0;i<n;i++)
			{
				order.get(k)[i].add(k);
			}
		}
		
		Node node[][]=new Node[m][n];  	//һ����m�У�ÿ�д洢ǰn�����
		//��һ����Ҫ������Ĵ���
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				node[i][j] = new Node();
				if(j==0)
					node[i][j].value=array[0][i];			
				else
					node[i][j].value=0;
				node[i][j].index1=i;//������Ӧ��valueֵ��һ�еĵڼ���
				node[i][j].index2=j;//������Ӧ��valueֵԭʼ�����еĵڼ������
			}
		}	
		//ͨ������洢��˳��õ�Node�е�Value
		//��Value�����µ���һ����ӵõ��µ�Valueֵ
		//index1��Ӧ��¼k����������ĵ���ڵ�
		//index2��Ӧ��¼��Ӧ���������е�ԭʼ�����˳��
		//�������Ҫ�¶���һ��MAP���洢�µ�˳��ֵ�������ܴ�ĩβ��ӣ�
		//�ٸ���Map�е�˳��
		//����ǻ������е�Node�����е�һ���Աȣ�ȡValue�����Ǹ������ɵ��Ǹ�˳�򣬼�Ϊ����
			
		for(int i=0;i<m;i++)  //�µ�ÿһ�е�ÿһ��
		{
			double local_sum[]=new double[n];
			int index1[]=new int[n];
			int index2[]=new int[n];
			for(int K=0;K<n;K++)  	
			{
				index1[K]=-1;	
				index2[K]=-1;
				local_sum[K]=-K-1;	//����Ĭ�ϵĽ�������Ϳ���ʡ������Ĺ���									
			}			
			
			for(int j=0;j<m;j++)	//������ǰ�е�n�����
			{
				double value=node[i][0].value+array[1][j];//
				if(value>local_sum[n-1]&&!order.get(i)[0].contains(j))//��Ϊ�ǽ���ֻ��Ҫ�����һ��ֵ���бȽ�
				{	
					//�������������в�ֵ
					Change_local_value(local_sum,index1,index2,value,j,0);
				}
			}		
			//����Node�ڵ�,Map ����
			ArrayList<Integer> copy[]=new ArrayList[n];
			for(int t=0;t<n;t++)
				copy[t]= new ArrayList<Integer>();
			for(int K=0;K<n;K++)  	
			{
				//���½ڵ�
				ArrayList<Integer> original[]=new ArrayList[n];
				for(int t=0;t<n;t++)
					original[t]= new ArrayList<Integer>();
				for(int r=0;r<n;r++)
					original[r]=(ArrayList<Integer>) order.get(i)[r].clone();
				node[i][K].value=local_sum[K];
				node[i][K].index1=index1[K];//������Ӧ��valueֵ��һ�еĵڼ���
				node[i][K].index2=index2[K];//������Ӧ��valueֵԭʼ�����еĵڼ������
				//�ýڵ������ȥ��������	
				original[index2[K]].add(index1[K]);	
				copy[K]=original[index2[K]];	
			}	
			for(int K=0;K<n;K++)
				order.get(i)[K]=copy[K];
		}
			
		//��ʽ����
		for(int L=2;L<array.length;L++)
		{		
			for(int i=0;i<m;i++)  //�µ�ÿһ�е�ÿһ��
			{
				double local_sum[]=new double[n];
				int index1[]=new int[n];
				int index2[]=new int[n];
				for(int K=0;K<n;K++)  	
				{
					index1[K]=-1;	
					index2[K]=-1;
					local_sum[K]=-K-1;	//����Ĭ�ϵĽ�������Ϳ���ʡ������Ĺ���									
				}			
				for(int c=0;c<n;c++)	//����һ�е�ԭʼnode��ֵǰn�ֽ��б���һ��
				{
					for(int j=0;j<m;j++)	//������ǰ�е�n�����
					{
						double value=node[i][c].value+array[L][j];
						if(value>local_sum[n-1]&&!order.get(i)[c].contains(j))//��Ϊ�ǽ���ֻ��Ҫ�����һ��ֵ���бȽ�
						{	
							//�������������в�ֵ
							Change_local_value(local_sum,index1,index2,value,j,c);
							//index2[c]=c;//�洢���Եڼ��ֱ�ѡ��������Ϣ
						}
					}
				}
				//����Node�ڵ�,Map ����
				
				ArrayList<Integer> copy[]=new ArrayList[n];
				for(int t=0;t<n;t++)
					copy[t]= new ArrayList<Integer>();
				for(int K=0;K<n;K++)  	
				{
					//���½ڵ�
					ArrayList<Integer> original[]=new ArrayList[n];
					for(int t=0;t<n;t++)
						original[t]= new ArrayList<Integer>();
					for(int r=0;r<n;r++)
						original[r]=(ArrayList<Integer>) order.get(i)[r].clone();
					node[i][K].value=local_sum[K];
					node[i][K].index1=index1[K];//������Ӧ��valueֵ��һ�еĵڼ���
					node[i][K].index2=index2[K];//������Ӧ��valueֵԭʼ�����еĵڼ������
					//�ýڵ������ȥ��������	
					original[index2[K]].add(index1[K]);	
					copy[K]=original[index2[K]];	
				}	
				for(int K=0;K<n;K++)
					order.get(i)[K]=copy[K];
			}
		}	
		double global_sum=0;
		int last_index=0;
		for(int i=0;i<m;i++)
		{
			if(global_sum<node[i][0].value)
			{
				global_sum=node[i][0].value;
				last_index=i;
			}
		}
		global_index=order.get(last_index)[0];
		System.out.println("ȫ������ֵΪ:"+global_sum);
		return global_index;
	}
	
	
	
	public int find_index(ArrayList<String> tokens,String concept)
	{
		//int index=0;	
		for(int i=0;i<tokens.size();i++)
		{
			if(tokens.get(i).equals(concept))
			{
				return i;
			}
		}
		return 0;
	}
	
	
	//����	
	public void sort(double[] a, int left, int right) {
		if (right > left) {
			int i = left;
			double p = a[left];
			for (int j = 1; j <= right - left; j++) {
				if (a[j+left] > p) {
					double tmp_j=a[left+j];
					for(int k=left+j;k>i;k--){
						a[k]=a[k-1];
					}
					a[i]=tmp_j;
					i++;
				}

			}
			if (left + 1 < i){
				sort(a, left, i - 1);
			}
			if (i < right - 1){
				sort(a, i + 1, right);
			}
		}
	}
	
	//��ֵ����ʱ����Ӧ��ֵ���и���
	public void Change_local_value(double a[],int b[],int c[],double value,int m,int n)
	{
		//local_sum,index1,index2,value,j,c
		int k=0;//��¼�ȴ�Сʱ������������Ϣ
		
		for(int i=0;i<a.length;i++)
		{
			if(a[i]<value)
			{
				k=i;
				break;
			}				
		}
		//��ֵ�����ֵ������	
		for(int i=a.length-1;i>k;i--)
		{
			a[i]=a[i-1];
		}
		a[k]=value;
		//��ֵ�����index1������һ�е�Ӱ��
		for(int i=b.length-1;i>k;i--)
		{
			b[i]=b[i-1];
		}
		b[k]=m;
		//��ֵ�����index2������һ�еĵڼ��������Ӱ��
		for(int i=c.length-1;i>k;i--)
		{
			c[i]=c[i-1];
		}
		c[k]=n;
	}
}

	 

