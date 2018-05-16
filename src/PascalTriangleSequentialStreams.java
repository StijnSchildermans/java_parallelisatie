import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;


public class PascalTriangleSequentialStreams{

	private Integer[][] points;

	public PascalTriangleSequentialStreams(int size) throws Exception{
		points = (Integer[][]) IntStream.range(0,size).boxed()
			.map(row -> IntStream.range(0,row+1).boxed()
					.map(col->PascalTriangle.getValueAtPoint(row,col))
					.toArray(Integer[]::new))
			.toArray(Integer[][]::new);
	}

}
