package it.unifi.oris.sirio.uniformization.main;

import it.unifi.oris.sirio.models.gspn.*;

public class OldFinder extends TruncationPointsFinderTechnique{

    //Singleton Pattern
    private static TruncationPointsFinderTechnique istance = null;
    
    private OldFinder(){};
    
    protected static TruncationPointsFinderTechnique getIstance(){
        if(OldFinder.istance == null){
            OldFinder.istance = new OldFinder(); 
        }
        return OldFinder.istance;
    }
    //End - Singleton
    
    protected DataContainer finder( DataContainer myData){
        Pair <Integer,Integer> LR;
        LR = TransientAndSteadyMatrixes.calculateLeftAndRight(myData.getLambda(), myData.getWantedAccuracy(), null);
       // myData.setL(Integer.valueOf(0));
        myData.setL(Integer.valueOf(LR.getFirst().intValue()));
        myData.setR(Integer.valueOf(LR.getSecond().intValue()));
        //System.out.println("Sono OldL="+Integer.valueOf(LR.getFirst().intValue())+" OldR="+Integer.valueOf(LR.getSecond().intValue()));
        return myData;
    }
}