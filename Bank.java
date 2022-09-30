import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

public class Bank {
    
    public int balance = 0;
    private Lock accessLock = new ReentrantLock();
    private Condition canWrite = accessLock.newCondition();  //used by consumer to notify deposit
    private Condition canRead = accessLock.newCondition();   //used by producer to notify withdraw
    private FileWriter myfile;
    public Bank(FileWriter fw){
        myfile = fw;
    }
    public void deposit(int amount, String name)
{
   accessLock.lock(); // lock this object
            
   // output thread information and buffer information, then wait
   try
   {
      
      //buffer = value; // set new buffer value
      balance += amount;
      // indicate producer cannot store another value
      // until consumer retrieves current buffer value
      
      System.out.println( name + " depositing " + amount);
      if(amount > 350){
          System.out.println("FLAGGED TRANSACTION, DEPOSIT EXCEEDS $350, SEE FLAGGED TRANSACTION LOGS");
          try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();
            myfile.write( name + " deposited " + amount + " at: " + dtf.format(now) + "\n");
             } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
      }
      // signal thread waiting to read from buffer
      canRead.signal(); 
   } // end try
   finally
   {
      accessLock.unlock(); // unlock this object
   } // end finally
} // end method set

public void withdraw(int amount, String name)
{
   int readValue = 0; // initialize value read from buffer
   accessLock.lock(); // lock this object
   // output thread information and buffer information, then wait
   try
   {
      // while no data to read, place thread in waiting state
      while ( balance < amount ) 
      {
         System.out.println( "Someone tries to withdraw." );
         //displayState( "Buffer empty. Consumer waits." );
 canRead.await(); // wait until buffer is full
      } // end while
      // indicate that producer can store another value 
      // because consumer just retrieved buffer value
    
      
      balance -= amount;
      System.out.println( name + " withdrawing " + amount);
      if(amount > 75){
          System.out.println("FLAGGED TRANSACTION, WITHDRAW EXCEEDS $75, SEE FLAGGED TRANSACTION LOGS");
        try {
          DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");  
          LocalDateTime now = LocalDateTime.now();
          
          myfile.write( name + " withdrew " + amount + " at: " + dtf.format(now) + "\n");
          //myWriter.close();
          //System.out.println(dtf.format(now));
        } catch (IOException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
    }
      
      // signal thread waiting for buffer to be empty
      canWrite.signal();
   } // end try
   catch ( InterruptedException exception ) 
   {
      exception.printStackTrace();
   } // end catch
 
   // if waiting thread interrupted, print stack trace
   finally
   {
      accessLock.unlock(); // unlock this object
   } // end finally
} // end method get
}


