import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.Collection;

public class PascalTriangleParallelStreamsBeter{

	private Integer[][] points;




	public PascalTriangleParallelStreamsBeter(int size) throws Exception{

		List<Callable<Integer[]>> tasks = IntStream.range(0,size).boxed().map(this::rowGenerator)
		 																	.collect(Collectors.toList());

		points = new ForkJoinPool(30).invokeAll(tasks)
					.stream()
					.map(f -> {
						try{
							return f.get();
						}catch(Exception e){
							return null;
						}
					})
					.toArray(Integer[][]::new);*/

			//Triangles.print(points);
}
	private Callable<Integer[]> rowGenerator(int row){
		return (() -> IntStream.range(0,row+1).boxed()
					.map(col->PascalTriangle.getValueAtPoint(row,col))
					.toArray(Integer[]::new));

	}



}
//System.out.println(row + ": "+Thread.currentThread().getName());
