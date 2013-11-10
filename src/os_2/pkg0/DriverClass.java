/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package os_2.pkg0;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

/**
 *
 * @author overload
 */
public class DriverClass {
    
    BufferedReader in;
    private String[] token;
    private String read;
    int id;
    int memoryLocation;
    String[] Ram;	
    String[] Disk; 
    PCB pc;
    private static Stack<PCB> jobQueue;
    
    public DriverClass()
    {
        Ram = new String[2048];
        Disk = new String[4048];
        jobQueue = new Stack<PCB>();
        id = 1;
        pc = new PCB();
        read = "";
        token = new String[1024];
        memoryLocation = 0;
    }

    void Begin() {
        Load();
        while(!jobQueue.empty()){
            Schedule();
            Cpu();
        }
    }

    private void Load() {
        
        try {
            
            in = new BufferedReader(new FileReader("DataFile.txt"));

            while ((read = in.readLine()) != null) {
                
                    if (read.startsWith("// JOB")) {
                    jobQueue.push(pc);
                    pc = new PCB();
                    token = read.split(" ");
                    pc.id = Integer.parseInt(token[2], 16);
                    pc.size = Integer.parseInt(token[3], 16);
                    pc.priority  = Integer.parseInt(token[4], 16);
                    pc.begin = memoryLocation;
                    
                    read = in.readLine();
                    
                }

                
                if(read.startsWith("// Data")){
                                        
                    token = read.split(" ");
                    
                    pc.inputBuffersize = Integer.parseInt(token[2], 16);
                    pc.outputBuffersize = Integer.parseInt(token[3], 16);
                    pc.tempBuffersize = Integer.parseInt(token[4], 16);
                    
                    read = in.readLine();
                }
                
               Disk[memoryLocation] = read;
               memoryLocation++;

            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        } finally {
            try {
                
            } catch (Throwable t) {
                System.exit(1);
            }
        }
            
    }

    private void Schedule() {
        pc = jobQueue.pop();
        for(int i=0;i<pc.size;i++){
            Ram[i] = Disk[i];
        }
    }

    private void Cpu() {
       
      Runnable r = new Runnable() {
      
      public void run() {
          
      }
     };
    }

    private static class PCB {
        
        int id;
        int begin;
        int end;
        int size;
        int priority;
        int address;
        int inputBuffersize;
        int outputBuffersize;
        int tempBuffersize;

        public PCB(){
            id = 0;
            begin = 0;
            end = 0;
            size = 0;
            priority = 0;
            address = 0;
            inputBuffersize = 0;
            outputBuffersize = 0;
            tempBuffersize = 0;
        }            
                
    }
    
}

