package it.unifi.oris.sirio.uniformization.main;

import it.unifi.oris.sirio.models.gspn.*;
import java.util.*;

public class OldWeighter extends PoissonWeighterTechnique{
    //Singleton Pattern
    private static PoissonWeighterTechnique istance = null;
    
    private OldWeighter(){};
    
    protected static PoissonWeighterTechnique getIstance(){
        if(OldWeighter.istance == null){
            OldWeighter.istance = new OldWeighter(); 
        }
        return OldWeighter.istance;
    }
    //End - Singleton

    protected DataContainer weighter(DataContainer myData){
        // Copia difensiva, non sappiamo se il costruttore di Pair fa la copia difensiva
        Pair <Integer,Integer> LR = new Pair<Integer,Integer>(Integer.valueOf(myData.getL().intValue()),Integer.valueOf(myData.getR().intValue()));
        LinkedList<Double> weightList = TransientAndSteadyMatrixes.calculateConstants(LR, 1.0, myData.getLambda());
        double[] w = new double[weightList.size()];
        for (int i=0; i<weightList.size();i++){
            w[i] = weightList.get(i).doubleValue();
        }
        myData.setFoxGlynnWeights(w);
        return myData;
    }
}