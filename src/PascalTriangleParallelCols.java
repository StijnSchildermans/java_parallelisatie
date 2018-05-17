import java.util.ArrayList;
import java.util.List;

public class PascalTriangleParallelCols{

	private int[][] points;

	public PascalTriangleParallelCols(int size){
		points = new int[size][];
		final List<Thread> threads = new ArrayList();

		for (int i = 0; i<size; i++){
			final int iBuffer = i;
			final int[] row = new int[i+1];
			for (int j = 0; j<=iBuffer;j++){
				final int jBuffer = j;
				Thread colThread = new Thread(new Runnable(){
					@Override
					public void run(){
						row[jBuffer] = PascalTriangle.getValueAtPoint(iBuffer,jBuffer);
					}
				});
				colThread.start();
				threads.add(colThread);
			}
			points[i] = row;
		}

		for (int i = 0; i<threads.size(); i++){
			try{
				threads.get(i).join();

			}catch(InterruptedException ex){
			}

		}
	}

}
