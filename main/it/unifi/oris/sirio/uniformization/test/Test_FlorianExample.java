package it.unifi.oris.sirio.uniformization.test;

import java.util.LinkedList;
import it.unifi.oris.sirio.models.gspn.*;
import it.unifi.oris.sirio.uniformization.main.*;

public class Test_FlorianExample {

    public static void main(String[] args){
        
        // transient probabilities matrix (embedded)
        double matrixUnif[][]= {{0.1, 0.9, 0, 0},{0.4, 0, 0.6, 0}, {0, 0.4, 0.3, 0.3},{0, 0, 0.4, 0.6}};
        
        // initial state probabilities
        double initialProbsVector[]= {1.0,0.0,0.0,0.0};
        
        // namely, rate * time
        double lambda = 50;
        
        // accuracy
        double wantedAccuracy = Math.pow(10, -6);
       
        TransientProbabilitiesCTMC NewSolution = TransientProbabilitiesCTMC.getFoxGlynnSolution(lambda, wantedAccuracy, matrixUnif, initialProbsVector);
        //defensive programming
        if(NewSolution ==null){
            System.out.println("bad parameters!");
        }
        
        double[] ret = NewSolution.getCtmcTransientProbabilitiesByUniformization();
        
        System.out.println("\n piGreco CTMC: \n");
        double checkSum = 0.0;
        for(int i=0; i<ret.length; i++){
            System.out.println("piGrecoCTMC["+i+"] = "+ret[i]);
            checkSum += ret[i];
        }
        System.out.println("\ntotalWeight = "+ checkSum); 
        
        // Ora lanciamo l'Old! ;-)
        /*SteadyStateCTMC OldSolution = SteadyStateCTMC.getOld(lambda, wantedAccuracy, matrixUnif, initialProbsVector);
        //defensive programming
        if(OldSolution ==null){
            System.out.println("bad parameters!");
        }
        
        double[] ret2 = OldSolution.calculatePiGrecoCTMC();
        
        System.out.println("\n piGreco CTMC: \n");
        double checkSum2 = 0.0;
        for(int i=0; i<ret2.length; i++){
            System.out.println("piGrecoCTMC["+i+"] = "+ret2[i]);
            checkSum2 += ret2[i];
        }
        System.out.println("\ntotalWeight = "+ checkSum2);
        
        System.out.println("L ="+OldSolution.getLR().getFirst().intValue()+"\n R="+OldSolution.getLR().getSecond().intValue());
        */
    }
    
}