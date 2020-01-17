/*     */ package LiftRealistic;
/*     */ 
/*     */ import com.uppaal.tron.Adapter;
/*     */ import com.uppaal.tron.Reporter;
/*     */ import com.uppaal.tron.TronException;
/*     */ import com.uppaal.tron.VirtualLock;
/*     */ import com.uppaal.tron.VirtualThread;
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
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
/*     */ public class TestIOHandler
/*     */   extends VirtualThread
/*     */   implements LiftListener, Adapter
/*     */ {
/*     */   public static boolean DBG = false;
/*  31 */   Lock lock = null;
/*  32 */   Condition cond = null;
/*  33 */   LinkedList<Integer> inputBuffer = new LinkedList<>();
/*     */ 
/*     */ 
/*     */   
/*  37 */   int opened = 0;
/*  38 */   int closed = 1;
/*  39 */   int reachedFloor = 2;
/*  40 */   int moveUp = 3;
/*  41 */   int moveDown = 4;
/*  42 */   int openDoor = 5;
/*  43 */   int closeDoor = 6;
/*  44 */   int button_0 = 8;
/*  45 */   int button_1 = 9;
/*  46 */   int button_2 = 10;
/*  47 */   int button_3 = 11;
/*  48 */   int stop = 12;
/*     */   
/*     */   Reporter reporter;
/*     */   
/*  52 */   LiftInterface lift = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TestIOHandler(LiftInterface paramLiftInterface) {
/*  59 */     super("TestInput");
/*  60 */     this.lift = paramLiftInterface;
/*  61 */     start();
/*  62 */     if (DBG) System.err.println("IOHandler: wait for thread to start"); 
/*  63 */     synchronized (this) { while (true) {
/*     */         try {
/*  65 */           if (this.lock == null) { wait(); continue; }
/*     */         
/*  67 */         } catch (InterruptedException interruptedException) {} break;
/*     */       }  }
/*  69 */      if (DBG) System.err.println("IOHandler: thread is started");
/*     */   
/*     */   }
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
/*     */   public void configure(Reporter paramReporter) throws TronException, IOException {
/*  85 */     this.opened = paramReporter.addInput("door_open");
/*  86 */     this.closed = paramReporter.addInput("door_closed");
/*  87 */     this.reachedFloor = paramReporter.addInput("reach_floor");
/*  88 */     this.button_0 = paramReporter.addInput("req[0]");
/*  89 */     this.button_1 = paramReporter.addInput("req[1]");
/*  90 */     this.button_2 = paramReporter.addInput("req[2]");
/*  91 */     this.button_3 = paramReporter.addInput("req[3]");
/*     */ 
/*     */     
/*  94 */     this.moveUp = paramReporter.addOutput("move_up");
/*  95 */     this.moveDown = paramReporter.addOutput("move_down");
/*  96 */     this.openDoor = paramReporter.addOutput("open_door");
/*  97 */     this.closeDoor = paramReporter.addOutput("close_door");
/*  98 */     this.stop = paramReporter.addOutput("stop");
/*     */ 
/*     */     
/* 101 */     paramReporter.setTimeUnit(100000L);
/* 102 */     paramReporter.setTimeout(3000);
/*     */ 
/*     */ 
/*     */     
/* 106 */     this.reporter = paramReporter;
/* 107 */     if (DBG) System.err.println("IOHandler: waiting for others");
/*     */     
/*     */     try {
/* 110 */       this.lift.waitForStart();
/*     */     }
/* 112 */     catch (InterruptedException interruptedException) {}
/* 113 */     if (DBG) System.err.println("IOHandler: starting test");
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void perform(int paramInt, int[] paramArrayOfint) {
/* 124 */     if (DBG) System.err.println("IOHandler: arrived"); 
/* 125 */     this.lock.lock();
/* 126 */     this.inputBuffer.add(new Integer(paramInt));
/* 127 */     this.cond.signalAll();
/* 128 */     this.lock.unlock();
/* 129 */     if (DBG) System.err.println("IOHandler: left");
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 139 */     synchronized (this) {
/* 140 */       this.lock = (Lock)new VirtualLock("InputQueue");
/* 141 */       this.cond = this.lock.newCondition();
/* 142 */       notifyAll();
/*     */     } 
/*     */     try {
/* 145 */       if (DBG) System.err.println("IOHandler: waiting for inputs"); 
/*     */       while (true) {
/* 147 */         this.lock.lock();
/* 148 */         while (this.inputBuffer.isEmpty())
/* 149 */           this.cond.await(); 
/* 150 */         int i = ((Integer)this.inputBuffer.poll()).intValue();
/* 151 */         this.lock.unlock();
/*     */ 
/*     */ 
/*     */         
/* 155 */         if (i == this.opened) {
/* 156 */           System.out.println("opened");
/* 157 */           this.lift.handleDoorOpened(); continue;
/* 158 */         }  if (i == this.closed) {
/* 159 */           System.out.println("closed");
/* 160 */           this.lift.handleDoorClosed(); continue;
/* 161 */         }  if (i == this.reachedFloor) {
/* 162 */           System.out.println("reachedFloor");
/* 163 */           this.lift.handleReachedFloor(); continue;
/*     */         } 
/* 165 */         if (i == this.button_0) {
/* 166 */           System.out.println("button 0");
/* 167 */           this.lift.handleRequest(0); continue;
/* 168 */         }  if (i == this.button_1) {
/* 169 */           System.out.println("button 1");
/* 170 */           this.lift.handleRequest(1); continue;
/* 171 */         }  if (i == this.button_2) {
/* 172 */           System.out.println("button 2");
/* 173 */           this.lift.handleRequest(2); continue;
/* 174 */         }  if (i == this.button_3) {
/* 175 */           System.out.println("button 3");
/* 176 */           this.lift.handleRequest(3); continue;
/*     */         } 
/* 178 */         System.err.println("IOHandler: UNKNOWN INPUT");
/*     */       }
/*     */     
/* 181 */     } catch (InterruptedException interruptedException) {
/* 182 */       System.err.println(interruptedException);
/*     */     } finally {
/* 184 */       this.lock.unlock();
/*     */     } 
/* 186 */     System.err.println("IOHandler: stopped listening for inputs");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reportMoveUp() {
/* 198 */     if (this.reporter != null) {
/* 199 */       System.out.println("MoveUp");
/* 200 */       this.reporter.report(this.moveUp);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void reportMoveDown() {
/* 205 */     if (this.reporter != null) {
/* 206 */       System.out.println("MoveDown");
/* 207 */       this.reporter.report(this.moveDown);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void reportOpenDoor() {
/* 212 */     if (this.reporter != null) {
/* 213 */       System.out.println("OpenDoor");
/* 214 */       this.reporter.report(this.openDoor);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void reportCloseDoor() {
/* 219 */     if (this.reporter != null) {
/* 220 */       System.out.println("CloseDoor");
/* 221 */       this.reporter.report(this.closeDoor);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void reportStop() {
/* 227 */     if (this.reporter != null) {
/* 228 */       System.out.println("stop");
/* 229 */       this.reporter.report(this.stop);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnect() {
/* 235 */     if (this.reporter != null) {
/* 236 */       System.out.println("(disconnect)");
/* 237 */       this.reporter.disconnect();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/ivo/shared/bla/!/LiftRealistic/TestIOHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */