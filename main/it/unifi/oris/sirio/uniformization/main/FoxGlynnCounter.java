package it.unifi.oris.sirio.uniformization.main;

import java.util.LinkedList;

import it.unifi.oris.sirio.models.gspn.Pair;
/**
 * Questa classe realizza il calcolo dello steady state di una CTMC tramite Uniformization, 
 * partendo dalla conoscenza delle probabilità transienti della DTMC Uniformizzata Embedded,
 * seguendo l'algoritmo ideato da Fox e Glynn per il calcolo delle distribuzioni di Poisson.
 * @author Samuele Foni e Lorenzo Pardini
 *
 */
public final class FoxGlynnCounter extends CounterTechnique {
    
    //Singleton Pattern
    private static CounterTechnique istance = null;
    
    private FoxGlynnCounter(){};
    
    protected static CounterTechnique getIstance(){
        if(FoxGlynnCounter.istance == null){
            FoxGlynnCounter.istance = new FoxGlynnCounter(); 
        }
        return FoxGlynnCounter.istance;
    }
    //End - Singleton
    
    /**
     * This method implement the sum for the steady state of the CTMC between
     * the left and right truncation point.
     */
    protected DataContainer calculateTransientProbabilitiesDTMCue(DataContainer myData){
       
        //System.out.println("\nStarting Counter...");
        
        LinkedList<double[]> piGreco_ue=myData.getDtmcTransientProbsUE();
        double[] weights = myData.getFoxGlynnWeights();
        
        int L = myData.getL().intValue();
        int R = myData.getR().intValue();
        int size = piGreco_ue.getFirst().length;
    
        double totalWeight = myData.getFoxGlynnTotalWeight().doubleValue();

        
        double[] result = new double[size];
        // defensive programming, initialize each to 0.0
        for(int p=0; p<result.length; p++) result[p]= 0.0;
       
        for (int listIndex = L; listIndex <= R; listIndex++) {
            for (int i = 0; i < size; i++) {
                result[i] += piGreco_ue.get(listIndex)[i] * weights[listIndex-L];
            }
        }
        for (int i = 0; i < size; i++) {
            result[i] = result[i]/totalWeight;
        }
            
        //System.out.println("Counter finished!");
        
        myData.setCmtcTransientProbs(result);
        return myData;
    }
 
}