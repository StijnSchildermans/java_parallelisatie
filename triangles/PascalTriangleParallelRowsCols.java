import java.util.ArrayList;
import java.util.List;

public class PascalTriangleParallelRowsCols{

	private int[][] points;

	public PascalTriangleParallelRowsCols(int size){
		points = new int[size][];
		final List<Thread> rowThreads = new ArrayList();

		for (int i = 0; i<size; i++){
			final int iBuffer = i;
			Thread rowThread = new Thread(new Runnable(){
				public void run(){
					//System.out.println(iBuffer+" ");
					final int[] row = new int[iBuffer+1];
					List<Thread> colThreads = new ArrayList();
					for (int j = 0; j<=iBuffer;j++){
						final int jBuffer = j;
						Thread colThread = new Thread(new Runnable(){
							public void run(){
								row[jBuffer] = PascalTriangle.getValueAtPoint(iBuffer,jBuffer);
							}
						});
						colThreads.add(colThread);
						colThread.start();
					}
					for (int i = 0; i < colThreads.size(); i++){
						try{
							colThreads.get(i).join();
						} catch(Exception e){System.out.println("Exception!");}

					}
					points[iBuffer] = row;

				}
			});
			rowThread.start();
			rowThreads.add(rowThread);
		}

		for (int i = 0; i<rowThreads.size(); i++){
			try{
				rowThreads.get(i).join();

			}catch(InterruptedException ex){
			}

		}
	}

}
