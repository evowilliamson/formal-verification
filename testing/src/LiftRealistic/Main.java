/*    */ package LiftRealistic;
/*    */ 
/*    */ import com.uppaal.tron.Reporter;
/*    */ import com.uppaal.tron.VirtualThread;
/*    */ 
/*    */ public class Main
/*    */ {
/*  8 */   protected Lift lift = null;
/*    */   
/* 10 */   TestIOHandler testIOHandler = null;
/* 11 */   Reporter reporter = null;
/*    */   
/* 13 */   protected int mutant = 0;
/*    */ 
/*    */   
/*    */   public Main(String[] paramArrayOfString) {
/* 17 */     handleArguments(paramArrayOfString);
/* 18 */     initialize();
/* 19 */     initializeIO();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void handleArguments(String[] paramArrayOfString) {
/* 24 */     byte b = 0;
/* 25 */     while (b < paramArrayOfString.length) {
/* 26 */       if ("-M".equals(paramArrayOfString[b])) {
/* 27 */         if (b + 1 < paramArrayOfString.length) { this.mutant = Integer.parseInt(paramArrayOfString[b + 1]); }
/*    */         else
/* 29 */         { System.err.println("Specify mutant id.");
/*    */           return; }
/*    */         
/* 32 */         b += 2; continue;
/* 33 */       }  if ("-C".equals(paramArrayOfString[b])) {
/* 34 */         if (b + 2 < paramArrayOfString.length) {
/* 35 */           int i = Integer.parseInt(paramArrayOfString[b + 2]);
/* 36 */           if (i <= 0) {
/* 37 */             System.err.println("The specified port (" + paramArrayOfString[b + 2] + ") is not in valid range.");
/*    */             
/*    */             return;
/*    */           } 
/* 41 */           VirtualThread.setRemoteClock(paramArrayOfString[b + 1], i);
/* 42 */           b += 3; continue;
/*    */         } 
/* 44 */         System.err.println("Specify virtual clock, like: -C localhost 6521");
/*    */         
/*    */         return;
/*    */       } 
/*    */       
/* 49 */       System.err.println("Uninterpreted option: " + paramArrayOfString[b]);
/* 50 */       b++;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void initialize() {
/* 60 */     this.lift = new Lift(this.mutant);
/*    */   }
/*    */   
/*    */   protected void initializeIO() {
/* 64 */     this.testIOHandler = new TestIOHandler(this.lift);
/* 65 */     this.reporter = new Reporter(this.testIOHandler, 9999);
/* 66 */     Lift.setLiftListener(this.testIOHandler);
/*    */   }
/*    */   
/*    */   public void play() {
/* 70 */     this.lift.start();
/* 71 */     System.out.println("Lift started");
/*    */     try {
/* 73 */       this.lift.join();
/*    */     }
/* 75 */     catch (InterruptedException interruptedException) {}
/*    */   }
/*    */ 
/*    */   
/*    */   public static void main(String[] paramArrayOfString) {
/* 80 */     Main main = new Main(paramArrayOfString);
/* 81 */     main.play();
/* 82 */     System.out.println("Lift terminated");
/*    */   }
/*    */ }


/* Location:              /home/ivo/shared/bla/!/LiftRealistic/Main.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */