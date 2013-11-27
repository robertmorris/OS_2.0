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
public class DriverClass implements Runnable {
    
    BufferedReader in;
    private String[] token;
    private String read;
    int id;
    int memoryLocation;
    String[] Ram;	
    String[] Disk; 
    PCB pc;
    private static Stack<PCB> jobQueue;
    FileReader file_1;
    private static int count=0;
    protected int addr;
    
    boolean t1Lock;
    boolean t2Lock;
    boolean t3Lock;
    boolean t4Lock;
    
    private short opCode;
    private short type;
    private short s1_reg;
    private short s2_reg;
    private short d_reg;
    private short b_reg;
    private short reg1;
    private short reg2;
    private long address;
    private static int ioCount;
    
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
        t1Lock = false;
        t2Lock = false;
        t3Lock = false;
        t4Lock = false;
    }

    void Begin() {
        System.out.println("Enter Loader");
        Load();
        System.out.println("Loader Complete");
        while(!jobQueue.empty()){
            System.out.println("Enter Schedule");
            Schedule();
            System.out.println("Scheduling Done");
            System.out.println("Enter CPU");
            
            //Cpu();
            
           
      
      Runnable r = new Runnable() {     
      public void run() {
            Cpu();                   
             }
          };
      
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        Thread t3 = new Thread(r);
        Thread t4 = new Thread(r);
        
        boolean foundThread = false;
        
        while(foundThread == false)
        {
            
        if(t1.isAlive() == false)
        {
            t1.start();
            foundThread = true;
        }
        else if(t2.isAlive() == false)
        {
            t2.start();
            foundThread = true;
        }
        else if(t3.isAlive() == false)
        {
            t3.start();
            foundThread = true;
        }
        else if(t4.isAlive() == false)
        {
            t4.start();
            foundThread = true;
        }
        
        }
        
        
        /*
        Thread t2 = new Thread(r);
        t2.start();
        
         
        
        Thread t3 = new Thread(r);
        Thread t4 = new Thread(r);
        
        if(t1Lock == false)
        {
            t1Lock = true;
            
            t1Lock = false;
        }
        if(t2Lock == false)
        {
            t2Lock = true;
            
            t2Lock = false;
        }
        if(t3Lock == false)
        {
            t3Lock = true;
            t3.start();
            t3Lock = false;
        }
        if(t4Lock == false)
        {
            t4Lock = true;
            t4.start();
            t4Lock = false;
        }   
                */
            
        }
        //System.out.println("COMPLETED SUCCESSFULLY");
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
        System.out.println("PC to be Scheduled: " + pc.id);
        for(int i=0;i<pc.size;i++){
            Ram[i] = Disk[i];
        }
    }

    private void Cpu() {
     
        
      
        
        String instr = new String();
        int counter = 0;
        for(int i=0; i<Ram.length;i++)
        {
            if(Ram[i] != null)
            {
            //System.out.println("Counter = " + counter++);
            System.out.println("Get Binary");
            instr = getBinaryData(Ram[i].toString());
            System.out.println("Binary Recieved");
            System.out.println("Begin Decode");
            Decode(instr);
            System.out.println("End Decode");
            System.out.println("Begin Execute");
            
            
            Execute(instr);
            
            
            System.out.println("Execution Complete");
            System.out.println("end of CPU method");
            System.out.println();
            System.out.println();
            
            }
            
        
        
        }
     
        
        
      
    }

    private int Decode(String instr) {
       //CHECK HERE IF ANYTHING IS WRONG WITH CALCULATED RESULTS!
        //String binInstr = Integer.toBinaryString(instr_req);
        //Integer.toBinaryString(Character.digit(line.charAt(i),16))

        System.out.println("\nBinary instruction: " + instr );


        //EXTRACT THE TYPE AND OPCODE FROM THE INSTRUCTION
        this.type = Short.parseShort(instr.substring(0,2),2);
        this.opCode = Short.parseShort(instr.substring(2,8),2);

        switch (type) {

            case 0:
                s1_reg = Short.parseShort(instr.substring(8,12),2);
                s2_reg = Short.parseShort(instr.substring(12,16),2);
                d_reg = Short.parseShort(instr.substring(16,20),2);
                System.out.println("s1_reg: " + s1_reg + "\ns2_reg:" + s2_reg + "\nd_reg:" + d_reg);
                long dataCHK = Long.parseLong(instr.substring(20,32),2);
                break;

            case 1:
                b_reg = Short.parseShort(instr.substring(8,12),2);
                d_reg = Short.parseShort(instr.substring(12,16),2);
                address = Long.parseLong(instr.substring(16,32),2);
                System.out.println("b_reg:" + b_reg + "\nd_reg:" + d_reg + "\naddress:" + address);
                break;

            case 2:
                address = Integer.parseInt(instr.substring(8,32));
                System.out.println("JUMP ADDRESS: " + address);
                break;

            case 3:
                reg1 = Short.parseShort(instr.substring(8,12),2);
                reg2 = Short.parseShort(instr.substring(12,16),2);
                address = Long.parseLong(instr.substring(16,32),2);
                System.out.println("Reg1: " + reg1 + "\nReg2: " + reg2 + "\nAddress: " + address);
                ioCount++;
                break;

            default:
                System.err.println("ERROR: HIT DEFAULT DECODE TYPE");
                break;

        }

        return opCode;
    }

    private String getBinaryData(String h) {
        // so we need to strip of the prefix 0x
        String hexString = h.substring(2,10);
        // then print again to see that it's just 0000dd99
        System.out.println("Adding hexString: " + hexString);

        long t = Long.parseLong(hexString, 16);
        String binaryBits = Long.toBinaryString(t);

        // then convert it to a string of bits
        System.out.println("BINARY STRING " + binaryBits);
        int length = binaryBits.length();

        if (length < 32) {
            int diff = 32 - length;
            for (int i=0; i<diff; i++) {
                binaryBits = "0" + binaryBits;
            }
        }
        return binaryBits;
    }

    private void Execute(String s) {
        
        
        System.out.println("\nExecuting instruction...." + " OPCODE = " + opCode);

        if (!(opCode < 0) || (opCode > 26)) {
            //out.append("\nOPCODE =" + opCode);
            switch (opCode) {

                case 0:
                    System.out.println("Putting input buffer contents into accumulator");
                    break;

                case 1:
                    System.out.println("Writing content of accumulator into output buffer");
                    break;

                case 2:
                    System.out.println("Storing register in address");
                    break;

                case 3:
                    System.out.println("Loading address into register");
                    break;

                case 4:
                    System.out.println("Swapping registers");
                    break;

                case 5:
                    System.out.println("Adding s_regs into d_reg");
                    break;

                case 6:
                    System.out.println("Subtracting s_regs into d_reg");
                    break;

                case 7:
                    System.out.println("Multiplying s_regs into d_reg");
                    break;

                case 8:
                    System.out.println("Dividing s_regs into d_reg");
                    break;

                case 9:
                    System.out.println("Logical AND of s_regs");
                    break;

                case 10:
                    System.out.println("Logical OR of s_regs");
                    break;

                case 11:
                    System.out.println("Transferring data into register");
                    break;

                case 12:
                    System.out.println("Adding data into register");
                    break;

                case 13:
                    System.out.println("Multiplying data into register");
                    break;

                case 14:
                    System.out.println("Dividing data into register");
                    break;

                case 15:
                    System.out.println("Loading data/address into register");
                    break;

                case 16:
                    System.out.println("Checking if s1_reg < s2_reg");
                    break;

                case 17:
                    System.out.println("Checking if s1_reg <  data");
                    break;

                case 18:
                    System.out.println("END OF PROGRAM - OPCODE " + opCode);
                    System.out.println("IOCOUNT: " + ioCount);
                    break;

                case 19:
                    System.out.println("Moving to the next instruction");
                    //Does nothing and moves to next instruction
                    break;

                case 20:
                    System.out.println("Jumping to another location");
                    break;

                case 21:
                    System.out.println("Checking if b_reg = d_reg, then branch");
                    break;

                case 22:
                    System.out.println("Checking if b_reg != d_reg, then branch");
                    break;

                case 23:
                    System.out.println("Checking if d_reg is 0, then branch");
                    break;

                case 24:
                    System.out.println("Checking if b_reg != 0, then branch");
                    break;

                case 25:
                    System.out.println("Checking if b_reg > 0, then branch");
                    break;

                case 26:
                    System.out.println("Checking if b_reg < 0, then branch");
                    break;

                default:
                    System.err.println("UNKNOWN OPCODE");
                    break;

            }
        } else {
            System.err.println("DIDN'T DECODE... OPCDOE = " + opCode);
        }
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

