import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class Linear{

	private static int cache;

	public static void main(String[] args){
		String type = args[0];
		int size = Integer.parseInt(args[1]);
		int count = Integer.parseInt(args[2]);
		int iterations = Integer.parseInt(args[3]);
		
		for (int i = 0; i < iterations; i++){
			switch (type){
				case "sequential": sequential(size); break;
				case "sequentialStreams": sequentialStreams(size); break;
				case "parallel"  : parallel(size,count); break;
				case "parallelThreadPool": parallelThreadPool(size,count); break;
				case "parallelStreams": parallelStreams(size,count); break;
				default: System.out.println("Invalid input!"); break;
			}
		}

		System.out.println(cache+"");
	}

	private static void task(){
		double j=Math.random()*100;

		for (long i = 0; i< 1000000000; i++){
			if (j < 100000) j++;
			else j=1;
		}
		System.out.println(j);
	}

	private static void sequential(int size){
		for (int i = 0; i < size; i++) task();
	}
	private static void sequentialStreams(int size){
		IntStream.range(0,size).forEach(i->task());
	}
	private static void parallel(int size, int ts){
		Thread[] threads = new Thread[ts];
		for (int i = 0; i < ts; i++){
			Thread t = new Thread(()->sequential(size/ts)); 
			t.start();
			threads[i] = t;
		}
		try{
			for (int i = 0; i < threads.length; i++) {
				threads[i].join();
			}
		}catch(InterruptedException e){
			System.out.println("InterruptedException!");
		}
	}
	private static void parallelThreadPool(int size, int ts){
		CompletableFuture[] tasks = new CompletableFuture[ts];
		for (int i = 0; i < ts; i++) 
			tasks[i] = CompletableFuture.runAsync(()->sequential(size/ts));
		try{
			for (int i = 0; i < tasks.length; i++) {
				tasks[i].get();
			}
		}catch(Exception e){
			System.out.println("Exception!");
		}

	}
	private static void parallelStreams(int size, int ts){
		IntStream.range(0,ts)
			 .parallel()
			 .forEach(i -> sequentialStreams(size/ts));
	}
}
