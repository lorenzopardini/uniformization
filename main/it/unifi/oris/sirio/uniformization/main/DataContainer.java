package it.unifi.oris.sirio.uniformization.main;

import java.util.LinkedList;
/**
 * Questa classe implementa un container di dati comune a tutti i metodi del package Uniformization.
 * I dati contenuti sono:<ul>
 * <li>foxGlynnFlagF e' un flag di controllo che segnala il corretto esito della computazione. Se alla fine e' "False" allora l'algoritmo di Fox&Glynn fallisce.
 * <li>L e' il punto di troncamento sinistro della distribuzione di Poisson.
 * <li> R e' il punto di troncamento destro della distribuzione di Poisson.
 * <li> lambda e' il tasso della distribuzione di Poisson.
 * <li> foxGlynnWeights rappresenta il vettore degli approssimanti di Fox&Glynn delle probabilita' che siano avvenuti esattamente i eventi entro lambda.
 * <li> totalWeight rappresenta la somma totale dei weights.
 * <li> wantedAccuracy e' l'accuratezza desiderata.
 * <li> underFlowLimit rappresenta il limite di underflow.
 * <li> overFlowLimit rappresenta il limite di overflow.
 * <li> foxGlynnModeWeight rappresenta il valore del termine centrale da cui si avvia il calcolo dei weights.
 * <li> dtmcTransientProbsUE e' il vettore delle probabilita' transienti della DTMC Uniformizzata Embedded.
 * <li> ctmcTransientProbs e' il vettore delle probabilita' transienti della CTMC di partenza approssimate tramite uniformization.
 * <li> matrixUnif e' la matrice P Uniformizzata-Embedded.
 * <li> initialProbsVector e' il vettore delle probabilita' iniziali.
 * </ul>
 * @author Samuele Foni e Lorenzo Pardini
 *
 */
public class DataContainer {
    private Boolean foxGlynnFlagF;              // F e' un flag di controllo per verificare l'eventualita' di underflow. 
                                    // Se alla fine e' "False" allora l'algoritmo di Fox&Glynn fallisce.
    private Integer L;              // L e' il punto di troncamento sinistro della distribuzione di Poisson.
    private Integer R;              // R e' il punto di troncamento destro della distribuzione di Poisson.
    private double lambda;          // lambda e' il tasso della distribuzione di Poisson.
    private double[] foxGlynnWeights;       // weights rappresenta il vettore degli approssimanti di Fox&Glynn della
                                    // probabilita' che siano avvenuti esattamente i eventi entro lambda.
    private Double foxGlynnTotalWeight;     // totalWeight rappresenta la somma totale dei weights.
    private double wantedAccuracy;         // epsilon e' l'accuratezza desiderata.
    private double underFlowLimit;             // tau rappresenta il limite di underflow.
    private double overFlowLimit;           // omega rappresenta il limite di overflow.
    private Double foxGlynnModeWeight;          // omegaM rappresenta il valore del termine centrale da cui si avvia il calcolo dei weights.
    private LinkedList<double[]> dtmcTransientProbsUE;    // piGreco_ue e' il vettore delle probabilita' transienti della DTMC Uniformizzata Embedded.
    private double[] cmtcTransientProbs;   // piGrecoCTMC e' il vettore delle probabilita' transienti della CTMC di partenza approssimate tramite uniformization.
    private double[][] matrixUnif;  // matrixUnif e' la matrice P Uniformizzata-Embedded.
    private double[] initialProbsVector;          // root (o PiGreco_0) e' il vettore delle probabilita' iniziali.
    
    //METHODS
    
    //Setters And Getters
    public Boolean getFoxGlynnFlagF() {
        return foxGlynnFlagF;
    }
    public void setFoxGlynnFlagF(Boolean f) {
        foxGlynnFlagF = f;
    }
    public Integer getL() {
        return L;
    }
    public void setL(Integer l) {
        L = l;
    }
    public Integer getR() {
        return R;
    } 
    public void setR(Integer r) {
        R = r;
    }
    public double getLambda() {
        return lambda;
    }
    public void setLambda(double lambda) {
        this.lambda = lambda;
    }
    public double[] getFoxGlynnWeights() {
        return foxGlynnWeights;
    }
    public void setFoxGlynnWeights(double[] weights) {
        this.foxGlynnWeights = weights;
    }
    public Double getFoxGlynnTotalWeight() {
        return foxGlynnTotalWeight;
    }
    public void setFoxGlynnTotalWeight(Double totalWeight) {
        this.foxGlynnTotalWeight = totalWeight;
    }
    public double getWantedAccuracy() {
        return wantedAccuracy;
    }
    public void setWantedAccuracy(double epsilon) {
        this.wantedAccuracy = epsilon;
    }
    public double getUnderFlowLimit() {
        return underFlowLimit;
    }
    public void setUnderFlowLimit(double tau) {
        this.underFlowLimit = tau;
    }
    public double getOverFlowLimit() {
        return overFlowLimit;
    }
    public void setOverFlowLimit(double omega) {
        this.overFlowLimit = omega;
    }
    public Double getFoxGlynnModeWeight() {
        return foxGlynnModeWeight;
    }
    public void setFoxGlynnModeWeight(Double omegaM) {
        this.foxGlynnModeWeight = omegaM;
    }
    public LinkedList<double[]> getDtmcTransientProbsUE() {
        return dtmcTransientProbsUE;
    }
    public void setDtmcTransientProbsUE(LinkedList<double[]> piGreco_ue) {
        this.dtmcTransientProbsUE = piGreco_ue;
    }
    public double[] getCmtcTransientProbs() {
        return cmtcTransientProbs;
    }
    public void setCmtcTransientProbs(double[] piGrecoCTMC) {
        this.cmtcTransientProbs = piGrecoCTMC;
    }
    public double[][] getMatrixUnif() {
        return matrixUnif;
    }
    public void setMatrixUnif(double[][] matrixUnif) {
        this.matrixUnif = matrixUnif;
    }
    public double[] getInitialProbsVector() {
        return initialProbsVector;
    }
    public void setInitialProbsVector(double[] root) {
        this.initialProbsVector = root;
    }
    
    public String toString(){
    	String out = new String();
    	out+="|-----DataContainer------";
    	out+="\n|lambda=";
    	out+=Double.valueOf(this.lambda).toString();
    	out+="\n|Left Truncation Point=";
    	out+=this.L.toString();
    	out+="\n|Right Truncation Point=";
    	out+=this.L.toString();
    	out+="\n|FoxGlynn Falg F=";
    	out+=this.foxGlynnFlagF.toString();
    	out+="\n|wanted accuracy=";
    	out+=Double.valueOf(this.wantedAccuracy).toString();
    	out+="\n|------------------------";
    	return out;
    }

}