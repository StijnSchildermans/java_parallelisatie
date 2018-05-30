public class Exits{

	private static int[] cache;

	public static void main(String[] args){
		cache = new int[10000000];

		for (int i = 0; i<10000000;i+=1024){
			cache[i]++;
		}

		System.out.println(cache[(int)(Math.random()*10000000)]);

	}





}
