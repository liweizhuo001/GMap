package Tools;

import java.util.Random;

public class Random_Sequence_Order {
	public static int[] randomorder(int len)
	{ 
		int[] seed = new int[len];
		for(int i = 0;i<len;++i)
			seed[i] = i;

		int[] result= new int[len];   
		Random random = new Random();   
		for (int i = 0; i < len; i++)
		{   
			// �õ�һ��λ��   
			int r = random.nextInt(len - i);   
			// �õ��Ǹ�λ�õ���ֵ   
			result[i] = seed[r];   
			// �����һ��δ�õ����ַŵ�����   
			seed[r] = seed[len - 1 - i];
		}
		return result.clone();
	}
	
	public static int[] sampleK(int len, int K)
	{ 
		int[] seed = new int[len];
		for(int i = 0;i<len;++i)
			seed[i] = i;

		int[] result= new int[K];   
		Random random = new Random();   
		for (int i = 0; i < K; i++)
		{   
			// �õ�һ��λ��   
			int r = random.nextInt(len - i);   
			// �õ��Ǹ�λ�õ���ֵ   
			result[i] = seed[r];   
			// �����һ��δ�õ����ַŵ�����   
			seed[r] = seed[len - 1 - i];
		}
		return result.clone();
	}
	
	public static int[] selectorder(int len,int select_number)
	{ 
		int[] result= new int[len];
		int[] seed = new int[len];
		for(int i = 0;i<len;++i)
			seed[i] = i;
		//�򵥵���һ���û���˳�򲻱䣡
		int num=seed[select_number];
		seed[select_number]=seed[len-1];
		seed[len-1]=num;
		for(int i=0;i<len;i++)
		{
			result[i]=seed[i];
		}	
		return result;
	}

}
