import java.util.stream.Stream;

public class Triangles{

	public static void main(String[] args) throws Exception{
		int size = Integer.parseInt(args[2]);
		
		for (int i = 0; i < Integer.parseInt(args[3]); i++){
			switch (args[0]){
				case "sequential": new PascalTriangle(size); break;
				case "sequentialStreams": new PascalTriangleSequentialStreams(size); break;
				case "parallel": switch (args[1]){
					case "rows": new PascalTriangleParallel(size); break;
					case "cols": new PascalTriangleParallelCols(size); break;
				} break;
				case "parallelThreadPool": new PascalTriangleParallelThreadPool(size); break;
				case "parallelStreams": switch (args[1]){
					case "rows": new PascalTriangleParallelStreams(size); break;
					case "cols": new PascalTriangleParallelStreamsCols(size); break;
				} break;
				//case "parallelStreamsBeter": new PascalTriangleParallelStreamsBeter(size); break;
				default: System.out.println("Da ga ni"); break;

			}
		}
	}

	public static void print(Integer[][] triangle){
		Stream.of(triangle).forEach(r->{
			Stream.of(r).forEach(v->System.out.print(v+" "));
			System.out.println("");
		});

	}

}
