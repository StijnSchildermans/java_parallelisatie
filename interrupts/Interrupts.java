public class Interrupts{

	private static volatile long cache;


	public static void main(String[] args){
		switch(args[0]){
			case "async": processTasks(Interrupts::task); break;
			case "sync": processTasks(Interrupts::taskInterrupt); break;
			default: System.out.println("Da ga ni");
		}

	}

	private static void processTasks(Runnable r){
		Thread[] ts = new Thread[4];
                for (int i = 0; i<4;i++){
                        ts[i] = new Thread(r);
			ts[i].start();
                }
		try{
			for (int i = 0; i<4;i++)ts[i].join();
		}catch(Exception e){System.out.println("Excepion!");}
	}

	private static void task(){
		long j = 0;
		for (long i = 0; i < 100000000; i++){
			j+=1;
		}
		System.out.println(j);
	}

	private static void taskInterrupt(){
		long j = 0;
		for (long i = 0; i < 100000000; i++){
                        //setCache(getCache()+ 1);
			cache +=1;
                }
		System.out.println(cache);
	}

	private static synchronized void setCache(long c){
		cache = c;
	}
	private static synchronized long getCache(){
		return cache;
	}
}
