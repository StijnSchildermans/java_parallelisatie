import java.util.ArrayList;

public class Exits{
	private static int[][] buf;
	private static ArrayList<Integer> cache;

	public static void main(String[] args){
		int size = Integer.parseInt(args[0]);
		buf = new int[100000][];
		/*cache = new ArrayList();

		for (int i = 0; i<size*5000000;i++){
			cache.add(i);
		}*/
		faults(size);

		//System.out.println(cache.get((int)(Math.random()*size*5000000)));
	   System.out.println(buf[((int)Math.random())*100000][((int)Math.random())*1024]);

	}
	
	private static void faults(int size){
		
		for (int i = 0; i  < size*75000; i++){
			 buf[i%100000] = new int[1024];
		}
		cache.add(100);
	}




}
