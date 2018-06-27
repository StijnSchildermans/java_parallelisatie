public class Wrapper{
  private int[] a;

  public Wrapper(){
      a = new int[1024];
  }

  public /*synchronized*/ void setA(int[] a){
    this.a = a;
  }
  public int[] getA(){
    return a;
  }
  public void setData(int index, int data){
	this.a[index] = data;
  }
}
