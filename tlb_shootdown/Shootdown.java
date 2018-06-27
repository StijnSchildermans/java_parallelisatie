import java.util.stream.IntStream;

public class Shootdown{

  private static Wrapper data;
  private static int data2;

  public static void main(String[] args){
    String type = args[0];
    int size = Integer.parseInt(args[1]);

    switch(type){
      case "threads": manyThreads(size); break;
      case "threadPool": manyThreadsThreadPool(size); break;
      default: System.out.println("Da ga ni");
    }
    System.out.println(data2+"");
  }

  private static void sync(){
    data = new Wrapper();
    Thread[] threads = new Thread[1000];

    for (int i = 0; i < 1000; i++){
      final int buffer = i;
      threads[i] = new Thread(()->Shootdown.executeThread(data));
      threads[i].start();
    }
    for(Thread t : threads){
      try{
        t.join();
      } catch (Exception ex){}
    }
    System.out.println(data.getA()[(int)(Math.random()*1024)]+"");
  }
  private static void executeThread(Wrapper w){

    for(int i = 0; i < 100000; i++){
      w.setData(i%1024,w.getA()[i%1024]+1);
    }
  }

  private static void manyThreads(int size){
    Thread[] threads = new Thread[size];
    for (int i = 0; i < size; i++){
      final int buffer = i;
      threads[i] = new Thread(Shootdown::runThreadMany);
      threads[i].start();
      /*try{
        Thread.sleep(1);
      }catch(Exception ex){}*/
    }
    for(Thread t : threads){
      try{
        t.join();
      } catch (Exception ex){}
    }
  }
  private static void manyThreadsThreadPool(int size){
    IntStream.range(0,size)
    .parallel()
    .forEach(i->runThreadMany());
  }
  private static void runThreadMany(){
    int[] d = new int[1024];
    for (int i = 0; i < 10000000; i++){
      d[i%1024]++;
    }
    data2+=d[/*(int)Math.random()*1024*/ 1000];
  }
}
