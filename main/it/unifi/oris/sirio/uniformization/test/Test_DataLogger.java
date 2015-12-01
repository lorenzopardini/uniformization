package it.unifi.oris.sirio.uniformization.test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import it.unifi.oris.sirio.uniformization.main.*;

public class Test_DataLogger {

    public static void main(String[] args) {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            
            String timeLog = new String("./precisione.csv");

            File logFile = new File(timeLog);

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            
            
            
            // AREA DI CALCOLO E COLLEZIONAMENTO DATI
        
            
            double matrixUnif[][]= {{0.1, 0.9, 0, 0},{0.4, 0, 0.6, 0}, {0, 0.4, 0.3, 0.3},{0, 0, 0.4, 0.6}};
            double initialProbsVector[]= {1.0,0.0,0.0,0.0};
            
            double wantedAccuracy = Math.pow(10.0, -128);
           
            //System.out.println(Double.valueOf(Double.MIN_VALUE).toString());
            
            TransientProbabilitiesCTMC newSolution;
            TransientProbabilitiesCTMC OldSolution;
            
            long a;
            long b;
            long t_new;
            long t_old;
   

            //for(int i=1; i<323; i++){
            //for(double lambda=50.0; lambda<760.0; lambda=lambda+10.0){
            //double lambda=50.0;
            //double lambda = 744.0;

            //for(double lambda=50.0; lambda<760.0; lambda=lambda+10.0){
            double lambda=50.0;
            //double lambda =1000.0;

                
               //double wantedAccuracy = Math.pow(10.0, -i);
                
                //a = System.currentTimeMillis();
                newSolution = TransientProbabilitiesCTMC.getFoxGlynnSolution(lambda, wantedAccuracy, matrixUnif, initialProbsVector);
                double[] ret = newSolution.getCtmcTransientProbabilitiesByUniformization();
                if(ret == null){ System.out.println("ERRoRE!\n"); }
                //b = System.currentTimeMillis();
                //t_new=b-a;
                

                //OldSolution = SteadyStateCTMC.getOld(lambda, wantedAccuracy, matrixUnif, initialProbsVector);
                //double[] ret2 = OldSolution.calculatePiGrecoCTMC();

                OldSolution = TransientProbabilitiesCTMC.getOldSolution(lambda, wantedAccuracy, matrixUnif, initialProbsVector);
                //double[] ret2 = OldSolution.calculatePiGrecoCTMC();
                //a = System.currentTimeMillis();
                //t_old = a-b;
                
                //System.out.println(" tempo newSolution = "+t_new+"\n tempo OldSolution = "+t_old);
             
                
                //LOGGING SECTION

                //Double Lambda = Double.valueOf(lambda);
                //String s = Double.valueOf(wantedAccuracy).toString();
                
                
/*
                Double Lambda = Double.valueOf(lambda);
                String s = Lambda.toString();

                
                //s=s+","+t_old+","+t_new;
                
                Double scartoPerc1 = Double.valueOf(((ret[0]-ret2[0])/(ret[0]))*100);
                Double scartoPerc2 = Double.valueOf(((ret[1]-ret2[1])/(ret[1]))*100);
                Double scartoPerc3 = Double.valueOf(((ret[2]-ret2[2])/(ret[2]))*100);
                Double scartoPerc4 = Double.valueOf(((ret[3]-ret2[3])/(ret[3]))*100);
             
                s=s+","+scartoPerc1.toString()+","+scartoPerc2.toString()+","+scartoPerc3.toString()+","+scartoPerc1.toString()+"\n";
                
                //double[] scartoPerc = new double[4];
                //String sp;
                double sum =0.0;
                for(int i=0; i<ret.length; i++){
                    System.out.println(ret[i]); 
                   sum+=ret[i];
                }
                s = s+","+newSolution.getLR().getFirst().toString()+","+newSolution.getLR().getSecond().toString();
                for(int i=0; i<ret2.length; i++){
                    s = s+","+ret2[i];
                }

                //s = s+","+OldSolution.getLR().getFirst().toString()+","+OldSolution.getLR().getSecond().toString();
                System.out.println(sum);
                 s=s+"\n";

            //    s = s+","+OldSolution.getLR().getFirst().toString()+","+OldSolution.getLR().getSecond().toString();

                  //s=s+"\n";

                 writer.write(s); 
           */ 
           // }//END FOR LAMBDA
            
        //FINE AREA
        if(ret!=null){
                System.out.println("\n piGreco CTMC New: \n");
                double checkSum = 0.0;
                for(int i=0; i<ret.length; i++){
                    System.out.println("piGrecoCTMC["+i+"] = "+ret[i]);
                    checkSum += ret[i];
                }
                System.out.println("\ntotalWeight = "+ checkSum);
        }else{
            System.out.println("\n FALLIMENTO! \n "); 
        }
               /* System.out.println("\n piGreco CTMC Old: \n");
                double checkSum2 = 0.0;
                for(int i=0; i<ret2.length; i++){
                    System.out.println("piGrecoCTMC["+i+"] = "+ret2[i]);
                    checkSum2 += ret2[i];
                }
                System.out.println("\ntotalWeight = "+ checkSum2);*/
        
        
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
                System.out.println("finito!");
            } catch (Exception e) {
            }
        }
    }
}