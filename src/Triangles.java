import java.util.stream.Stream;

public class Triangles{

	public static void main(String[] args) throws Exception{
		int size = Integer.parseInt(args[1]);

		switch (args[0]){
			case "sequential": new PascalTriangle(size); break;
			case "sequentialStreams": new PascalTriangleSequentialStreams(size); break;
			case "parallel": new PascalTriangleParallel(size); break;
			case "parallelThreadPool": new PascalTriangleParallelThreadPool(size); break;
			case "parallelStreams": new PascalTriangleParallelStreams(size); break;
			case "parallelStreamsBeter": new PascalTriangleParallelStreamsBeter(size); break;
			default: System.out.println("Da ga ni"); break;

		}
	}

	public static void print(Integer[][] triangle){
		Stream.of(triangle).forEach(r->{
			Stream.of(r).forEach(v->System.out.print(v+" "));
			System.out.println("");
		});

	}

}
