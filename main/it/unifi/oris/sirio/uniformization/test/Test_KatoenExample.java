package it.unifi.oris.sirio.uniformization.test;

import it.unifi.oris.sirio.models.gspn.TransientAndSteadyMatrixes;
import it.unifi.oris.sirio.uniformization.main.*;

import static org.junit.Assert.*;

public class Test_KatoenExample {

    public static void main(String args[]){
        
        double Q[][]= {{-1.5, 1.5, 0.0, 0.0},
                       {3.0, -4.5, 1.5, 0.0},  
                       {0.0, 3.0, -4.5, 1.5},
                       {0.0, 0.0, 3.0, -3.0}};
        
        double uniformizationRate = 20.0;
        
        double P[][] = TransientAndSteadyMatrixes.createMatrixUniformized(Q, uniformizationRate); 
        double initialProbsVector[]= {1.0, 0.0, 0.0, 0.0};
        double lambda = 20.0;
        double wantedAccuracy = Math.pow(10, -2);
        TransientProbabilitiesCTMC obj = TransientProbabilitiesCTMC.getOldSolution(lambda, wantedAccuracy, P, initialProbsVector);
        double[] ret = obj.getCtmcTransientProbabilitiesByUniformization();
        
        TransientProbabilitiesCTMC obj2 = TransientProbabilitiesCTMC.getFoxGlynnSolution(lambda, wantedAccuracy, P, initialProbsVector);
        double[] ret2 = obj2.getCtmcTransientProbabilitiesByUniformization();
        
        double sum =0.0;
        for(int i=0; i<ret.length; i++){
            System.out.println(myRound(ret[i],3));
            sum+=ret[i];
        }
        System.out.println(sum);
        
        sum=0.0;
        for(int i=0; i<ret2.length; i++){
            System.out.println(myRound(ret2[i],3));
            sum+=ret2[i];
        }
        System.out.println(sum);
        
    }
    
    private static String myRound(double x, int cifre){
       String tmp = Double.valueOf(x).toString();
       int l = tmp.length();
       
       String dec = tmp.substring(2, cifre+2);
       int decimali = Integer.valueOf(tmp.substring(2, cifre+1));
       int lastDigit = Integer.valueOf(dec.substring(dec.length()-1));
       if(lastDigit>=5){
           decimali++;
       }
       
       String s = "0."+Integer.valueOf(decimali).toString();
       return s;
    }
    
    
}