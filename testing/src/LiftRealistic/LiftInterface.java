package LiftRealistic;

public interface LiftInterface {
  void start();
  
  void waitForStart() throws InterruptedException;
  
  void join() throws InterruptedException;
  
  void handleRequest(int paramInt) throws InterruptedException;
  
  void handleReachedFloor() throws InterruptedException;
  
  void handleDoorClosed() throws InterruptedException;
  
  void handleDoorOpened() throws InterruptedException;
}


/* Location:              /home/ivo/shared/bla/!/LiftRealistic/LiftInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */