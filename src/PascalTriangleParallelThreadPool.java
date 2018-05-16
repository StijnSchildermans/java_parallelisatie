import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class PascalTriangleParallelThreadPool{

	private int[][] points;

	public PascalTriangleParallelThreadPool(int size){
		points = new int[size][];

		ForkJoinPool pool = new ForkJoinPool(30);
		Future<int[]>[] tasks = new Future[size];

		for (int i = 0; i<size; i++){
			final int k = i;
			tasks[i] = pool.submit(new Callable<int[]>(){
					public int[] call(){
						final int[] row = new int[k+1];
						for (int j = 0; j<=k;j++) row[j] = PascalTriangle.getValueAtPoint(k,j);
						return row;
					}

			});
		}
		try{
			for (int i = 0; i<size; i++) points[i] = tasks[i].get();
		}catch(InterruptedException ex){
		}catch(ExecutionException ex){}




		/*for (int i = 0; i<threads.size(); i++){
			try{
				threads.get(i).join();

			}catch(InterruptedException ex){
			}

		}*/
	}

}
