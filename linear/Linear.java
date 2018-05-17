import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class Linear{

	private static int cache;

	public static void main(String[] args){
		String type = args[0];
		long size = Long.parseLong(args[1]);
		int iterations = Integer.parseInt(args[2]);
	
		switch (type){
			case "sequential": sequential(size, iterations); break;
			case "sequentialStreams": sequentialStreams(size, iterations); break;
			case "parallel"  : parallel(size, iterations); break;
			case "parallelThreadPool": parallelThreadPool(size, iterations); break;
			case "parallelStreams": parallelStreams(size, iterations); break;
			default: System.out.println("Invalid input!"); break;
		}

		System.out.println(cache+"");
	}

	private static void task(long size){
		int j=1;

		for (int i = 0; i< size; i++){
			if (j < 100000) j*=2;
			else j=1;
		}
		cache = j;
	}

	private static void sequential(long size, int iterations){
                for (int i = 0; i < iterations; i++) task(size);
        }
	private static void sequentialStreams(long size, int iterations){
		IntStream.range(0,iterations)
			 .forEach(i -> task(size));
	}
	private static void parallel(long size, int iterations){
		List<Thread> threads = new ArrayList();
		for (int i = 0; i < iterations; i++){
			Thread t = new Thread(() -> task(size)); 
			t.start();
			threads.add(t);
		}
		try{
			for (Thread t : threads) {
				t.join();
			}
		}catch(InterruptedException e){
			System.out.println("InterruptedException!");
		}
	}
	private static void parallelThreadPool(long size, int iterations){
		List<CompletableFuture> tasks = new ArrayList();
		for (int i = 0; i < iterations; i++) 
			tasks.add(CompletableFuture.runAsync(()->task(size)));
		try{
			for (CompletableFuture f : tasks) f.get();
		}catch(Exception e){
			System.out.println("Exception!");
		}

	}
	private static void parallelStreams(long size, int iterations){
		IntStream.range(0,iterations)
			 .parallel()
			 .forEach(i -> task(size));
	}
}
