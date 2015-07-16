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
			// 得到一个位置   
			int r = random.nextInt(len - i);   
			// 得到那个位置的数值   
			result[i] = seed[r];   
			// 将最后一个未用的数字放到这里   
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
			// 得到一个位置   
			int r = random.nextInt(len - i);   
			// 得到那个位置的数值   
			result[i] = seed[r];   
			// 将最后一个未用的数字放到这里   
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
		//简单的做一下置换，顺序不变！
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
