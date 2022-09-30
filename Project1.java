/*Kyler Peterson
 COurse: CNT 4714 Summer 2022
 Assignment title: project 1 - Syncronized, cooperating threads under locking
 Due date: June 5th, 2022
 */


import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;
import java.io.FileWriter;
//package project1;
//import java.io.*;
//Class to generate threads by extending the Thread class
//TestThread - example of extending the Thread class
import java.io.IOException;

public class Project1 {
	// Main method 
	public static void main(String[] args) throws IOException {
       FileWriter fw = new FileWriter("transactions.txt");
       Bank bank = new Bank(fw);
      
       ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(15);
       for(int i = 0; i < 10; i++){
       pool.submit(new WithdrawAgent(bank, "Agent W" + i));
       }
       for(int k = 0; k < 5; k++){
       pool.submit(new DepositAgent(bank, "Agent D" + k));
       }
       
    }  //end TestThread
}