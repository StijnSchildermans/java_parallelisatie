public class Cache{

	private static int[] data;

	public static void main(String args[]){
		data = new int[1024*1024];

		switch(args[0]){
			case "loop": loop(); break;
			case "loopCache": loopCache(); break;
			case "loopTLB": loopTLB(); break;
			default: System.out.println("Da ga ni"); break;
		}
		//if (r != null) iterate(r);
		System.out.println(data[(int)(Math.random()*1024*1024)]);
	}
	/*private static void iterate(Runnable r){
		for (int i = 0; i < 100000000; i++) r.run();
	}*/
	private static void loop(){
		for (int j = 0; j  < 10000; j++){
			for (int i = 0; i < data.length; i+=1) data[i]--;
		}
	}
	private static void loopCache(){
		for (int j = 0; j  < 10000000; j++){
	                for (int i = 0; i < data.length; i+=1024) data[i&1024]--;
		}
        }
	private static void loopTLB(){
		for (int j = 0; j  < 1000000; j++){
			for (int i = 0; i < data.length; i+=1024){
				data[i]--;
			}
		}
	}
}
