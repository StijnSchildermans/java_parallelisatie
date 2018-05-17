import java.util.stream.IntStream;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class PascalTriangleParallelStreamsCols{

	private Integer[][] points;

	public PascalTriangleParallelStreamsCols(int size){

		points = IntStream.range(0,size).boxed()
				.map(row -> IntStream.range(0,row+1).boxed()
						.parallel()
						.map(col->PascalTriangle.getValueAtPoint(row,col))
						.toArray(Integer[]::new))
				.toArray(Integer[][]::new);
	}

	//System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "30");

	//ExecutorService pool = Executors.newFixedThreadPool(size);

	/*try{
		//points = pool.submit(task).get();
		Future<Integer[][]> pts = pool.submit(task);

		while (!pts.isDone()) Thread.sleep(100);
		points = pts.get();*/
		//pool.shutdown();
		//points = pts.get();
	//} catch(Exception e){System.out.println("Kapot!");}

	//Triangles.print(points);

}
