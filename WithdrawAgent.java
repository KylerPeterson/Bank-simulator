//package part2;

//Consumer's run method loops ten times reading a value from buffer.
import java.util.Random;
import java.nio.Buffer;
public class WithdrawAgent implements Runnable 
{ 
private static Random generator = new Random();
private Bank sharedBank;
private String AgentName;
// constructor
public WithdrawAgent(Bank bank, String name)
{
   sharedBank = bank;
   AgentName = name;
} // end Consumer constructor
// read sharedLocation's value four times and sum the values
public void run()
{
   int sum = 0;

   while(true) 
   {
      // sleep 0 to 3 seconds, read value from buffer and add to sum
      try 
      {
    	  Thread.sleep( generator.nextInt( 50 ) );
         sharedBank.withdraw(1 + generator.nextInt( 99 ), AgentName);
         System.out.println("Balance is: " + sharedBank.balance);
    	  //sum += sharedLocation.get();
          // uncomment next line for unsync version - 
          //System.out.printf( "\t\t\t%2d\n", sum );
    	     
       } // end try
      // if sleeping thread interrupted, print stack trace
      catch ( InterruptedException exception ) 
      {
         exception.printStackTrace();
      } // end catch
   } // end for

} // end method run
} // end class Consumer