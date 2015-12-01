package it.unifi.oris.sirio.uniformization.main;

import it.unifi.oris.sirio.models.gspn.*;
import java.util.*;

public class OldCounter extends CounterTechnique{

    //Singleton Pattern
    private static CounterTechnique istance = null;
    
    private OldCounter(){};
    
    protected static CounterTechnique getIstance(){
        if(OldCounter.istance == null){
            OldCounter.istance = new OldCounter(); 
        }
        return OldCounter.istance;
    }
    //End - Singleton
    
    protected DataContainer calculateTransientProbabilitiesDTMCue(DataContainer myData){
        LinkedList<Double> factConstants = new LinkedList<Double>();
        for(int i=0; i<myData.getFoxGlynnWeights().length;i++){
            factConstants.addLast(Double.valueOf(myData.getFoxGlynnWeights()[i]));
        }
        Pair <Integer,Integer> LR = new Pair<Integer,Integer>(Integer.valueOf(myData.getL().intValue()),Integer.valueOf(myData.getR().intValue()));

        myData.setCmtcTransientProbs(TransientAndSteadyMatrixes.transientStateProbability(myData.getDtmcTransientProbsUE(), factConstants, LR));
        return myData;
    }
}