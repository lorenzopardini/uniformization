package it.unifi.oris.sirio.uniformization.main;

/**
 * Questa classe realizza il calcolo dei weights e del Total Weight necessari ad effettuare 
 * un'approssimazione dell'andamento di una distribuzione di Poisson secondo Fox e Glynn.
 * @author Samuele Foni & Lorenzo Pardini
 *
 */
public final class FoxGlynnWeighter extends PoissonWeighterTechnique{
    
    //Singleton Pattern
    private static PoissonWeighterTechnique istance = null;
    
    private FoxGlynnWeighter(){};
    
    protected static PoissonWeighterTechnique getIstance(){
        if(FoxGlynnWeighter.istance == null){
            FoxGlynnWeighter.istance = new FoxGlynnWeighter(); 
        }
        return FoxGlynnWeighter.istance;
    }
    //End - Singleton
    
    /**
     * This method implements the WEIGHTER function of "Computing Poisson Probabilities". 
     * @param lambda (input) is the rate of the Poisson distribution.
     * @param tau is the underflow threshold.
     * @param omega is the overflow threshold.
     * @param epsilon (input) is the wanted accuracy.
     * @param L (input) is the left truncation point.
     * @param R (input/output) is the right truncation point. Warning: Don't use defensive copy!
     * This method may had to change R's value.
     * @param F (output) is the flag for successful ending of the method. Should be checked by the caller.
     * @param weights (output) is the array of weights.
     * @param totalWeight (output) is the sum of the weights. Warning: don't use defensive copy!
     */
    protected DataContainer weighter(DataContainer myData){
        double lambda =myData.getLambda(); 
        double tau =myData.getUnderFlowLimit(); 
        double omega=myData.getOverFlowLimit(); 
        double epsilon=myData.getWantedAccuracy(); 
        Integer L=myData.getL(); 
        Integer R=myData.getR(); 
        Boolean F=myData.getFoxGlynnFlagF(); 
        double[] weights = new double[R.intValue()-L.intValue()+1]; 
        Double totalWeight;
        
        double m = Math.floor(lambda);
        /**
         * CALCOLO DI w(m):
         * w(m) rappresenta il valore medio della moda da cui si parte per 
         * effettuare il calcolo dei restanti weights. La scelta di w(m) deve essere fatta
         * a priori. Il valore ideale sarebbe pari ad 1 (v. pg. 441 Computing Poisson Probabilities).
         * Ma per evitare eventuali errori di underflow, lo poniamo uguale a Omega/(10^10)*(R-L).
         * Questa scelta euristica garantisce che il Total Weights sarà tale da risultare minore 
         * o uguale di Omega/(10^10) (v. pg. 443 Computing Poisson Probabilities). 
         * Dove con Omega si intende il massimo valore rappresentabile.
         */
        double LL=L.doubleValue();
        double RR=R.doubleValue();
        double omega_m = omega/(Math.pow((double)10, (double)10)*(RR-LL));
        
        //TEST 
        //System.out.println("\n Starting Weighter... \n\t L="+LL+" R="+RR+" omega_m="+omega_m);
        int mm = (int)m - L.intValue(); //indice del valore della moda traslato sul vettore tra 0 ed R-L.
        weights[mm] = omega_m;
        
        //Caso L
        int j = (int) m;
        
        while(j>L.intValue()){
            weights[mm - 1] = ((double)j/lambda)*weights[mm];
            mm=mm-1;
            j=j-1;
        }
        
        if(lambda<400&&R.intValue()>600){
            F = Boolean.FALSE;
            myData.setFoxGlynnFlagF(F);
            //System.out.println("\t Case: (lambda<400 && R>660) --> FAIL!");
            return myData;
        }
        
        if(lambda<400 && R.intValue()<=600){
            j = (int) m;
            mm = (int)m - L.intValue();
            double q = 0.0;
            while(j<R.intValue()){
                q = lambda/(((double)j)+((double)1));
                if(weights[mm]>(tau/q)){
                    weights[mm+1] = q*weights[mm];
                    mm = mm+1;
                    j = j+1;
                }else{
                    R=Integer.valueOf(j);
                    //System.out.println("\t Special: R was updated to: "+R);
                }
            }   
        }
        
        if(lambda>=400){
            j = (int) m;
            mm = (int)m - L.intValue();
            while(j<R.intValue()){
                weights[mm+1]=(lambda/(((double)j)+((double)1)))*weights[mm];
                j=j+1;
                mm=mm+1;
            }
        }
        totalWeight = Double.valueOf(computeW(L, R, weights));
        
        //System.out.println("\n\t Total Weight = "+totalWeight);
        
        myData.setFoxGlynnFlagF(F);
        myData.setR(R);
        myData.setFoxGlynnTotalWeight(totalWeight);
        myData.setFoxGlynnWeights(weights);
        
        return myData;
        
    }
    
    /**
    * This method compute the Total Weight W adding the weights between L and R.
    * For numerical stability (see Computing Poisson Probabilities pg. 441) small weights
    * are added first.
    * @param R the right truncation point
    * @param L the left truncation point
    * @param weights the array of weights
    * @return the Total Weight W
    */
    private double computeW( Integer L, Integer R, double[] weights){
        int s = 0; //Sarebbe L-L
        int t = R.intValue() - L.intValue();
        double W = 0.0;
        while(s<t){
            if(weights[s]<=weights[t]){
                W = W + weights[s];
                s = s+1;
            }else{
                W = W + weights[t];
                t = t-1;
            }
        }
        // A questo punto s==t e rappresenta la moda.
        // Quindi sommo il weights centrale: omega_m.
        W = W + weights[s];
        return W;
    }
}