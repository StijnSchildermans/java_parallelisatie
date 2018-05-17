public class PascalTriangle{

	private final int[][] points;

	public PascalTriangle(int size){
		points = new int[size][];

		for (int i = 0; i < size; i++){
			int[] row = new int[i+1];
			for (int j = 0; j <= i; j++){
				row[j] = getValueAtPoint(i,j);
			}
			points[i] = row;		
		}

	}

	public static int getValueAtPoint(int row, int col){
		if (col == 0 || col == row) return 1;
		else return getValueAtPoint(row-1,col-1) + getValueAtPoint(row-1,col);
	}
}
