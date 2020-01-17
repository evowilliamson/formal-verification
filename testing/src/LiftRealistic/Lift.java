/*     */ package LiftRealistic;
/*     */ 
/*     */ import com.uppaal.tron.VirtualLock;
/*     */ import com.uppaal.tron.VirtualThread;
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ 
/*     */ public class Lift
/*     */   extends VirtualThread
/*     */   implements LiftInterface
/*     */ {
/*     */   static final int MAXIMUM_QUEUE_SIZE = 3;
/*     */   private int floor;
/*     */   private ArrayList<Integer> requests;
/*  16 */   private int mutant = 0;
/*  17 */   Lock lock = null;
/*  18 */   Condition cond = null;
/*     */   boolean started = false;
/*  20 */   static LiftListener listener = null;
/*     */   boolean doorCompleted;
/*     */   int reachedFloor;
/*     */   
/*  24 */   public Lift(int paramInt) { super("Lift");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     this.doorCompleted = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     this.reachedFloor = 0; this.mutant = paramInt; this.floor = 0; this.requests = new ArrayList<>(); }
/*     */   public static void setLiftListener(LiftListener paramLiftListener) { listener = paramLiftListener; }
/* 107 */   public synchronized void waitForStart() throws InterruptedException { for (; !this.started; wait()); } public void run() { try { execute(); } catch (InterruptedException interruptedException) {} listener.disconnect(); this.lock.unlock(); } protected void execute() throws InterruptedException { this.lock = (Lock)new VirtualLock("LiftLock"); this.cond = this.lock.newCondition(); this.lock.lock(); synchronized (this) { this.started = true; notifyAll(); }  while (true) { int i = waitOnNextFloor(); gotoFloor(i); }  } public void moveUp(int paramInt) { listener.reportMoveUp();
/* 108 */     if (this.mutant == 2) {
/* 109 */       this.reachedFloor = 1;
/*     */     } else {
/* 111 */       this.reachedFloor = paramInt;
/* 112 */     }  while (this.reachedFloor > 0)
/* 113 */       doWait(); 
/* 114 */     listener.reportStop(); }
/*     */   public void doWait() { try { this.cond.await(); } catch (InterruptedException interruptedException) {} }
/*     */   public int waitOnNextFloor() { while (this.requests.size() == 0) doWait();  if (this.mutant == 1) return 1;  return ((Integer)this.requests.remove(0)).intValue(); }
/* 117 */   public void gotoFloor(int paramInt) { if (this.mutant != 4 && paramInt == this.floor) return;  closeDoor(); if (paramInt > this.floor) { moveUp(paramInt - this.floor); } else { moveDown(this.floor - paramInt); }  this.floor = paramInt; openDoor(); } public void closeDoor() { listener.reportCloseDoor(); this.doorCompleted = false; while (!this.doorCompleted) doWait();  } public void openDoor() { listener.reportOpenDoor(); this.doorCompleted = false; while (!this.doorCompleted) doWait();  } public void moveDown(int paramInt) { if (this.mutant != 3)
/* 118 */       listener.reportMoveDown(); 
/* 119 */     this.reachedFloor = paramInt;
/* 120 */     while (this.reachedFloor > 0)
/* 121 */       doWait(); 
/* 122 */     listener.reportStop(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleRequest(int paramInt) throws InterruptedException {
/* 128 */     this.lock.lock();
/* 129 */     if (this.requests.size() < 3) {
/* 130 */       this.requests.add(new Integer(paramInt));
/* 131 */       if (this.mutant == 6 && this.requests.size() == 3) {
/* 132 */         Integer integer = this.requests.remove(2);
/* 133 */         this.requests.add(1, integer);
/*     */       } 
/* 135 */       this.cond.signalAll();
/*     */     } 
/* 137 */     this.lock.unlock();
/*     */   }
/*     */   
/*     */   public void handleDoorClosed() throws InterruptedException {
/* 141 */     this.lock.lock();
/* 142 */     this.doorCompleted = true;
/* 143 */     this.cond.signalAll();
/* 144 */     this.lock.unlock();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleDoorOpened() throws InterruptedException {
/* 149 */     this.lock.lock();
/* 150 */     this.doorCompleted = true;
/* 151 */     this.cond.signalAll();
/* 152 */     this.lock.unlock();
/*     */   }
/*     */   
/*     */   public void handleReachedFloor() throws InterruptedException {
/* 156 */     this.lock.lock();
/* 157 */     if (this.mutant != 5)
/* 158 */       this.reachedFloor--; 
/* 159 */     this.cond.signalAll();
/* 160 */     this.lock.unlock();
/*     */   }
/*     */ }


/* Location:              /home/ivo/shared/bla/!/LiftRealistic/Lift.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */