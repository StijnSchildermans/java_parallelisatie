import java.util.ArrayList;

public class Exits{
	private static int[][] buf;
	private static ArrayList<Integer> cache;

	public static void main(String[] args){
		//int size = Integer.parseInt(args[0]);
		buf = new int[50000][];
		/*cache = new ArrayList();

		for (int i = 0; i<size*5000000;i++){
			cache.add(i);
		}*/
		faults();

		//System.out.println(cache.get((int)(Math.random()*size*5000000)));
	   System.out.println(buf[((int)Math.random())*50000][((int)Math.random())*1024]);

	}
	
	private static void faults(){
		
		for (int i = 0; i  < 50000; i++){
			 buf[i] = new int[1024];
		}
		//cache.add(100);
	}




}
