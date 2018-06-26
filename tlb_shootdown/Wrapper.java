public class Wrapper{
  private int[] a;

  public Wrapper(){
      a = new int[1024];
  }

  public synchronized void setA(int[] a){
    this.a = a;
  }
  public synchronized int[] getA(){
    return a;
  }
}
