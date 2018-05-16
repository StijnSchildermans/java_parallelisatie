import java.util.ArrayList;
import java.util.List;

public class PascalTriangleParallel{

	private int[][] points;

	public PascalTriangleParallel(int size){
		points = new int[size][];
		final List<Thread> threads = new ArrayList();

		for (int i = 0; i<size; i++){
			final int iBuffer = i;
			Thread rowThread = new Thread(new Runnable(){
				public void run(){
					//System.out.println(iBuffer+" ");
					final int[] row = new int[iBuffer+1];
					for (int j = 0; j<=iBuffer;j++){
						row[j] = PascalTriangle.getValueAtPoint(iBuffer,j);

					}
					points[iBuffer] = row;

				}
			});
			rowThread.start();
			threads.add(rowThread);
		}

		for (int i = 0; i<threads.size(); i++){
			try{
				threads.get(i).join();

			}catch(InterruptedException ex){
			}

		}
	}

}
