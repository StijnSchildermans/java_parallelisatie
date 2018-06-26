public class Shootdown{

  private static Wrapper[] data;

  public static void main(String[] args){
    data= new Wrapper[10];
    for (int i = 0; i < 10; i++) data[i] = new Wrapper();
    Thread[] threads = new Thread[1000];

    for (int i = 0; i < 1000; i++){
      final int buffer = i;
      threads[i] = new Thread(()->Shootdown.executeThread(data[buffer%10]));
      threads[i].start();
    }
    for(Thread t : threads){
      try{
        t.join();
      } catch (Exception ex){}
    }
    System.out.println(data[(int)(Math.random()*10)].getA()[(int)(Math.random()*1024)]+"");
  }

  private static void executeThread(Wrapper w){

    for(int i = 0; i < 100000; i++){
      w.getA()[i%1024]++;
    }
  }
}
