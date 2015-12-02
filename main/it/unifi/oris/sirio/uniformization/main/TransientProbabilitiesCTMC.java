package it.unifi.oris.sirio.uniformization.main;

import java.util.*;

import it.unifi.oris.sirio.models.gspn.Pair;
import it.unifi.oris.sirio.models.gspn.TransientAndSteadyMatrixes;
import it.unifi.oris.sirio.analyzer.log.AnalysisLogger;
/**
 * Questa classe prende in ingresso i parametri iniziali, inizializza la struttura dati interna
 * e crea un'istanza di ogni Strategy: Finder, Weighter e Counter. Tali oggetti seguono di default
 * la procedura ideata da Fox e Glynn ma, in accordo allo strategy, possono essere opportunamente
 * ridefinite, fornendo la possibilità di poter sfruttare tecniche alternative per il calcolo delle  
 * probabilita' transienti di una CTMC tramite Uniformization.
 * @author Samuele Foni e Lorenzo Pardini
 *
 */
public class TransientProbabilitiesCTMC {
    
    //OGGETTI STRATEGY
    private DataContainer myData;
    private TruncationPointsFinderTechnique myFinder;
    private PoissonWeighterTechnique myWeighter;
    private CounterTechnique myCounter;
    
    // FUNZIONI DI CONFIGURAZIONE
    public void setData(DataContainer ft){
        this.myData = ft;
    }
    
    public void setTruncationPointsFinderTechnique(TruncationPointsFinderTechnique ft){
        this.myFinder = ft;
    }
    
    public void setPoissonWeighterTechnique(PoissonWeighterTechnique ft){
        this.myWeighter = ft;
    }
    
    public void setCounterTechnique(CounterTechnique ft){
        this.myCounter = ft;
    }
    
    public void updateLambda(double l){
    	this.myData.setLambda(l);
    }
    
    // Costruttore Dummy
    private TransientProbabilitiesCTMC(){}
    
    // STATIC FACTORY METHOD - DEFAULT
    /**
     * This is a static factory method for the TransientProbabilitiesCTMC class. This method provides
     * a pre-configured object with three default strategies, the finder, the weighter 
     * and the counter. Each strategy is based on "Computing Poisson Probabilities" by
     * Fox & Glynn. 
     * @param lambda the rate multiplied for the time (lambda * t).
     * @param wantedAccuracy the wanted accuracy.
     * @param matrixUnif the transient probability matrix (embedded).
     * @param initialProbsVector the initial probability vector (one probability foreach state).
     * @return a new pre-configured object ready for transient analysis. Return null for bad parameters.
     */
    public static TransientProbabilitiesCTMC getFoxGlynnSolution(double lambda, double wantedAccuracy, double[][] matrixUnif, double[] initialProbsVector){
        
        //defensive programming : check parameters
        if( lambda <=0.0    ||      // lambda * t isn't valid
            wantedAccuracy <=0.0   ||      // the accuracy isn't valid
            matrixUnif[0].length != matrixUnif.length ||    //the matrix is not square
            matrixUnif[0].length != initialProbsVector.length             //the matrix cannot work with the vector    
           ){
               //System.out.println("TransientProbabilitiesCTMC.getDefault() - bad parameters");
               return null;
        }
        
        
        TransientProbabilitiesCTMC x = new TransientProbabilitiesCTMC();
        x.setTruncationPointsFinderTechnique(FoxGlynnFinder.getIstance());
        x.setPoissonWeighterTechnique(FoxGlynnWeighter.getIstance());
        x.setCounterTechnique(FoxGlynnCounter.getIstance());
        x.setData(new DataContainer());
        
        x.myData.setL(Integer.valueOf(0));
        x.myData.setR(Integer.valueOf(0));
        
        x.myData.setFoxGlynnFlagF(Boolean.FALSE); 
        x.myData.setLambda(lambda);
        x.myData.setWantedAccuracy(wantedAccuracy);
        
        x.myData.setUnderFlowLimit(Double.MIN_VALUE);
        x.myData.setOverFlowLimit(Double.MAX_VALUE);
        
       // x.myData.setPiGreco_ue((LinkedList<double[]>) piDTMC_ue.clone());    // COPIA DIFENSIVA
        
      //  x.myData.setPiGrecoCTMC(new double[x.myData.getPiGreco_ue().size()]);
        
        x.myData.setMatrixUnif(matrixUnif);
        
        x.myData.setInitialProbsVector(initialProbsVector);
        
        return x;
    }
    
   /**
    * This is a static factory method that provides data configuration for the calculus of the 
    * transient probabilities of a CTMC according an hybrid approach beteen Fox & Glynn's algorithm,
    * for the, evaluating of truncation points, and an effective esponential Poisson data
    * distribution. 
    * @param lambda
    * @param wantedAccuracy
    * @param matrixUnif
    * @param initialProbsVector
    * @return
    */
    public static TransientProbabilitiesCTMC getOldSolution(double lambda, double wantedAccuracy, double[][] matrixUnif, double[] initialProbsVector){
        //defensive programming : check parameters
        if( lambda <=0.0    ||      // lambda * t isn't valid
            wantedAccuracy <=0.0   ||      // the accuracy isn't valid
            matrixUnif[0].length != matrixUnif.length ||    //the matrix is not square
            matrixUnif[0].length != initialProbsVector.length             //the matrix cannot work with the vector    
           ){
               //System.out.println("TransientProbabilitiesCTMC.getDefault() - bad parameters");
               return null;
        }
        
        
        TransientProbabilitiesCTMC x = new TransientProbabilitiesCTMC();
        x.setTruncationPointsFinderTechnique(OldFinder.getIstance());
        x.setPoissonWeighterTechnique(OldWeighter.getIstance());
        x.setCounterTechnique(OldCounter.getIstance());
        x.setData(new DataContainer());
        
        x.myData.setL(Integer.valueOf(0));
        x.myData.setR(Integer.valueOf(0));
        
        x.myData.setFoxGlynnFlagF(Boolean.TRUE);    // Imposto a TRUE per by-passare il controllo finale.
                                        // Le vecchie funzioni non fanno uso del flag F.
        x.myData.setLambda(lambda);
        x.myData.setWantedAccuracy(wantedAccuracy);
        
        x.myData.setUnderFlowLimit(Double.MIN_VALUE);
        x.myData.setOverFlowLimit(Double.MAX_VALUE);
        
       // x.myData.setPiGreco_ue((LinkedList<double[]>) piDTMC_ue.clone());    // COPIA DIFENSIVA
        
      //  x.myData.setPiGrecoCTMC(new double[x.myData.getPiGreco_ue().size()]);
        
        x.myData.setMatrixUnif(matrixUnif);
        
        x.myData.setInitialProbsVector(initialProbsVector);
        
        return x;
    }
    
    // FUNZIONE PRINCIPALE - Utilizza tutti gli strategy
    /**
     * This method calculate the array of transient probabilities for the CTMC.
     * @return an array of double containing the probabilities.
     */
    public double[] getCtmcTransientProbabilitiesByUniformization(){
        myData = this.myFinder.finder(myData);
        
        //TEST
        //System.out.println(myData.getL().intValue());
        //System.out.println(myData.getR().intValue());
        
        if(myData.getFoxGlynnFlagF() == Boolean.FALSE){
            // Il finder ha fallito. L'algoritmo di Fox&Glynn non è applicabile.
            // TEST
            System.out.println("CalculatePiGrecoCTMTC - Finder failed");
            return null;
        }
        
       myData = this.myWeighter.weighter(myData);
       
       //TEST
       /*double sum = 0.0;
       //System.out.println("Total Weight="+myData.getTotalWeight().doubleValue());
       for(int i=0; i<myData.getWeights().length; i++){
           sum+=myData.getWeights()[i];
           ////System.out.println("Weights["+i+"]="+myData.getWeights()[i]);
       }
       //System.out.println("sum = "+sum);*/
        
        if(this.myData.getFoxGlynnFlagF() == Boolean.FALSE){
            // Il weighter ha fallito. L'algoritmo di Fox&Glynn non è applicabile.
            //System.out.println("CalculatePiGrecoCTMTC - Weighter failed");
            return null;
        }
        
        // Qui devo invocare la funzione per avere i pigreco fino a R
        myData.setDtmcTransientProbsUE(TransientAndSteadyMatrixes.calculateEmbeddedProbabilities(myData.getMatrixUnif(), myData.getInitialProbsVector() , myData.getR().intValue()));
        
        myData = this.myCounter.calculateTransientProbabilitiesDTMCue(myData);
        
        return myData.getCmtcTransientProbs();
    }
    
    public Pair<Integer,Integer> getLR(){
        return new Pair<Integer,Integer>(Integer.valueOf(myData.getL().intValue()),Integer.valueOf(myData.getR().intValue()));
    }
}