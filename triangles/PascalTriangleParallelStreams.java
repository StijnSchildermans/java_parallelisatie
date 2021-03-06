import java.util.stream.IntStream;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class PascalTriangleParallelStreams{

	private Integer[][] points;

	/*public PascalTriangleParallelStreams(int size){

		points = IntStream.range(0,size).boxed()
				.parallel()
				.map(row -> IntStream.range(0,row+1).boxed()
						.map(col->PascalTriangle.getValueAtPoint(row,col))
						.toArray(Integer[]::new))
				.toArray(Integer[][]::new);
	}*/

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




	public PascalTriangleParallelStreams(int size){
        ForkJoinPool pool = new ForkJoinPool(30);
       
        points = IntStream.range(0,size).boxed()
                .map(row -> pool.submit(()-> IntStream.range(0,row+1).boxed()
                        .map(col->PascalTriangle.getValueAtPoint(row,col))
                        .toArray(Integer[]::new)))
                .collect(Collectors.toList())
                .stream()
                .map(f -> {
                        try{
                            return f.get();
                        }catch(Exception e){
                            return null;
                        }
                    })
                .toArray(Integer[][]::new);       
    }
}
