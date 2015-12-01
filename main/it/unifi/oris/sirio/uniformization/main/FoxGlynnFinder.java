package it.unifi.oris.sirio.uniformization.main;

/**
 * Questa classe implementa il Finder di Fox e Glynn per il calcolo delle probabilita' di Poisson.
 * @author Samuele Foni & Lorenzo Pardini
 *
 */
public final class FoxGlynnFinder extends TruncationPointsFinderTechnique{

    //Singleton Pattern
    private static TruncationPointsFinderTechnique istance = null;
    
    private FoxGlynnFinder(){};
    
    protected static TruncationPointsFinderTechnique getIstance(){
        if(FoxGlynnFinder.istance == null){
            FoxGlynnFinder.istance = new FoxGlynnFinder(); 
        }
        return FoxGlynnFinder.istance;
    }
    //End - Singleton
    
	/**
	 * This method implements the finder technique by Fox and Glynn, see "Computing Poisson Probabilities".
	 * @param myData the container of data. 
	 */
	protected DataContainer finder( DataContainer myData){
	    double lambda = myData.getLambda();
	    double wantedAccuracy = myData.getWantedAccuracy(); 
	    double underFlowLimit = myData.getUnderFlowLimit();
	    double overFlowLimit = myData.getOverFlowLimit();
	    Integer L = myData.getL();
	    Integer R = myData.getR();
	    Boolean F = myData.getFoxGlynnFlagF();
	    Double foxGlynnModeWeight = myData.getFoxGlynnModeWeight();
	    //System.out.println("Starting Fox&Glynn Finder...");
		//engeneered version by Foni & Pardini
		double m = Math.floor(lambda);
		
		//TEST
		//System.out.println("\t calcolo m = "+m);
		
		//lambda should be >0, if not, this must be checked out in previously.
		double halfwantedAccuracy = wantedAccuracy / ((double)2);
		
		//TEST
		//System.out.println("\t HalfwantedAccuracy = "+halfwantedAccuracy);
		
		double kOut_L=0.0;
		double kOut_R=0.0;
		
		//TEST
		//System.out.println("\n\t Calculating R...\n");
		
	    //CALCULATE R: based on lambda values
		boolean rFound=false; //per vedere se ha trovato R alla fine
        if(lambda<400){
            
            //TEST
            //System.out.println("\t Case: (lambda < 400) --> apply corollary1");
            
            //apply corollary 1 with lambda=400 pg. 444
            
            double lambda_app = 400.0; //fixed lambda by the algorithm
            double k = ((double)1)/ ( ((double)2) * Math.sqrt(((double)2)*lambda_app) );    //starting k
                  
            double top = ( Math.sqrt(lambda_app) / ( ((double)2) * Math.sqrt(((double)2))));    //the top admitted value of k
            double a_lambda = (Double.valueOf(1).doubleValue() + ( Double.valueOf(1).doubleValue()/ lambda_app) ) * Math.sqrt(Double.valueOf(2).doubleValue()) * Math.exp((Double.valueOf(1).doubleValue()/ (double)16)); // fixed by the algorithm
            
            double d=0.0;
            double qUpperBound=0.0;
            
            
            //TEST
            
            //System.out.println("\t top = "+top);
            //System.out.println("\t k = "+k);
            
            
            double a0;
            double a1;
            double b;
            double c;
            double e;
            double dd;  // for Darkwing Duck effect
   
            //System.out.println("\t Starting Helper Function...");
            //helper function
            for(double y=k; y<=top; y=y+1){
                
                //d = 1 / (1 - Math.exp((-(2/9))*((y*Math.sqrt(2*lambda_app))+(3/2))));
                

                a0 = Math.sqrt(Double.valueOf(2).doubleValue()*lambda_app);
               // //System.out.println("a0 = "+a0);
                a1 = a0 * y;
               // //System.out.println("a1 = "+a1);
                b = a1 + 1.5;
                
                c = ((double) -2 /(double) 9)*b;
               // //System.out.println("c = "+c);
                e = Math.exp(c);
               // //System.out.println("e = "+e);
                dd = Double.valueOf(1).doubleValue() / ( Double.valueOf(1).doubleValue() - e);
                ////System.out.println("dd = "+dd);
               
          
                qUpperBound = ( a_lambda * dd * Math.exp(-((y*y)/((double)2))) ) / ( y * (Math.sqrt(((double)2)*Math.PI)));
                
              //TEST
                //System.out.println("\t\t loop.. y = "+y+"\t qUpperBound = "+qUpperBound);
                
                if(qUpperBound <= halfwantedAccuracy){
                    //TEST
                    //System.out.println("\t precision satisfied: "+qUpperBound+"<="+halfwantedAccuracy);
                    int r = (int)Math.round( (m + (y * Math.sqrt(((double)2)*lambda_app)) + Double.valueOf(1.5).doubleValue()) );
                    R = Integer.valueOf(r);
                    kOut_R = y;
                    rFound=true;
                    
                    //TEST
                    //System.out.println("\t Helper Function hit! Right truncation point R = "+R.intValue());
                    
                    break; // not very beautiful, TBD
                }  
            }
            
            //TEST
            //System.out.println("\t qUpperBound = "+qUpperBound);
            
            // if R was not found, we need a better precision on L. 
            // So set R as best possible then set the new accuracy for L. 
            if(!rFound){
                //System.out.println("\t insufficient accuracy on R, increasing accuracy on L...");
                k = Math.sqrt(lambda_app) / (((double)2)*Math.sqrt(((double)2)));
                kOut_R = k;
                R = Integer.valueOf((int)Math.round( (m + (k * Math.sqrt(((double)2)*lambda_app)) + (((double)3)/((double)2))) ));
                dd = ((double)1) / (((double)1) - Math.exp((-(((double)2)/((double)9)))*(k*Math.sqrt(((double)2)*lambda_app)+(((double)3)/((double)2)))));
                qUpperBound = ( a_lambda * dd * Math.exp(-((k*k)/((double)2))) ) / ( k * (Math.sqrt(((double)2)*Math.PI)));
                halfwantedAccuracy = wantedAccuracy - qUpperBound;
            }
        }else{
          //apply corollary 1, leave lambda as is
            //System.out.println("\t Case: (lambda >= 400) --> apply corollary1");
            double k = ((double)1) / ( ((double)2) * Math.sqrt(((double)2)) );    //starting k
            double lambda_app = lambda;              
            double top = ( Math.sqrt(lambda_app) / ( ((double)2) * Math.sqrt(((double)2))));    //the top admitted value of k
            double a_lambda = (((double)1) + ( ((double)1)/ lambda_app) ) * Math.sqrt(((double)2)) * Math.exp(((double)1)/((double)16)); // fixed by the algorithm
            
            double d;
            double qUpperBound;
            
            //System.out.println("\t Starting Helper Function...");
            //helper function
            for(; k<=top; k=k+1){
                d = ((double)1) / (((double)1) - Math.exp((-(((double)2)/((double)9)))*(k*Math.sqrt(((double)2)*lambda_app)+Double.valueOf(1.5).doubleValue())));
                
                qUpperBound = ( a_lambda * d * Math.exp(-((k*k)/((double)2))) ) / ( k * (Math.sqrt(((double)2)*Math.PI)));
                
                if(qUpperBound <= halfwantedAccuracy){
                    //System.out.println("\t precision satisfied: "+qUpperBound+"<="+halfwantedAccuracy);
                    int r = (int)Math.round( (m + (k * Math.sqrt(((double)2)*lambda_app)) + Double.valueOf(1.5).doubleValue()) );
                    R = Integer.valueOf(r);
                    kOut_R = k;
                    rFound=true;
                    break; // not very beautiful, TBD
                }  
            }       
            
            // if R was not found, we need a better precision on L. 
            // So set R as best possible then set the new accuracy for L. 
            if(!rFound){
                   
                k = Math.sqrt(lambda_app) / (((double)2)*Math.sqrt(((double)2)));
                kOut_R = k;
                R = Integer.valueOf((int)Math.round( (m + (k * Math.sqrt(((double)2)*lambda_app)) + Double.valueOf(1.5).doubleValue()) ));
                d = ((double)1) / (((double)1) - Math.exp((-(((double)2)/((double)9)))*(k*Math.sqrt(((double)2)*lambda_app)+Double.valueOf(1.5).doubleValue())));
                qUpperBound = ( a_lambda * d * Math.exp(-((k*k)/((double)2))) ) / ( k * (Math.sqrt(((double)2)*Math.PI)));
                halfwantedAccuracy = wantedAccuracy - qUpperBound;
                
              //TEST
                //System.out.println("\t R not found with wanted precision, R = "+R+" accuracy on L will be: "+halfwantedAccuracy);
               
            }
            
            //TEST
            //System.out.println("\t Right truncation point finally found, R = "+R.intValue());
            
        }
		
		
		//CALCULATE L: based on lambda values
        //System.out.println("\n\t Calculatin L...\n");
        
		boolean lFound = false; // TEST
        if (lambda<25){
		    L = Integer.valueOf(0);
		    kOut_L = 0;
            F = Boolean.FALSE;
            //System.out.println("\t Case: (lambda < 25) --> FAIL!");
		}else{
		    // apply corollary 2
		    //System.out.println("\t Case: (lambda >=25) --> apply corollary 2...");
		    double k = ((double)1) /( Math.sqrt(((double)2)* lambda) );
		    double b_lambda = (((double)1) + (((double)1)/lambda))*(Math.exp(((double)1)/(((double)8)*lambda))); //Helper variable
		    double kUpperBound = 1000000.0; // To keep control of the for statement (because in Fox&Glynn there is no limit for k)
		    double t_lambdaUpperBound;
		    
		    //System.out.println("\t Starting Helper Function...");
		    
		    for(;k<kUpperBound;k=k+1){
		        t_lambdaUpperBound = (b_lambda*Math.exp(-((k*k)/((double)2)))) / (k * Math.sqrt(((double)2)*(Math.PI)));
		        //System.out.println("\t\t loop... k="+k+"\t t_lambdaUpperBound="+t_lambdaUpperBound);
		        
		        if(t_lambdaUpperBound<halfwantedAccuracy){
		            //System.out.println("\t precision satisfied: "+t_lambdaUpperBound+"<="+halfwantedAccuracy);
		            L = Integer.valueOf((int) Math.floor( m - (k*Math.sqrt(lambda)) - Double.valueOf(1.5).doubleValue()));
		            kOut_L = k;
		            lFound = true;
		            break;
		        }
		    }
		    // TEST - se per qualche motivo non trovo L
		    if(!lFound){
		        //System.out.println("\t Unexpected case: L not found");
		    }
		}
        
        //System.out.println("\t k_L="+kOut_L);
		F = checkF(lambda, underFlowLimit, overFlowLimit,kOut_L, kOut_R, m, R, L);
		
		//System.out.println("\n\t Flag F is "+F.toString()+"\n");
		//System.out.println("\n\t L = "+L.toString()+" R = "+R.toString()+"\n\n");
		
		myData.setLambda(lambda); 
        myData.setL(L);
        myData.setR(R);
        myData.setFoxGlynnFlagF(F);
        myData.setFoxGlynnModeWeight(foxGlynnModeWeight);
        
        return myData;
	}
	/**
	 * This method evaluate some parameters and returns a boolean value according to 
	 * Fox & Glynn solution. 
	 * @param lambda the rate of the uniformization (input).
     * @param underFlowLimit the underflow precision of the machine (input).
     * @param overFlowLimit the overflow precision of the machine (input).
	 * @param kOut_L value of last k in the helper function for L.
	 * @param kOut_R value of last k in the helper function for R.
	 * @param m the mode of the Poisson distribution.
	 * @param L the left truncation point of Poisson Probabilities distribution (is an output value).
     * @param R the right truncation point of Poisson Probabilities distribution (is an output value). 
	 * @return the flag F value of the finder algorithm.
	 */
	private Boolean checkF(double lambda, double underFlowLimit, double overFlowLimit, double kOut_L, double kOut_R, double m, Integer R, Integer L){
	   //System.out.println("\n\t Checking Flaf F...\n");
	    if(lambda==0.0){
	        //System.out.println("\t Case: (lambda==0) --> FAIL");
	        return Boolean.FALSE; 
	    }
	    if((lambda<25)&&(Math.exp(-lambda)<underFlowLimit)){
	        //System.out.println("\t Case: (lambda < 25 && e^-lambda < underFlowLimit) --> FAIL");
	        return Boolean.FALSE;
	    }
	    
	    // Bounding Poisson Probabilities, pg.444 in basso. Variabile di appoggio.
	    double c_m = (((double)1)/Math.sqrt(((double)2)*(Math.PI)*m))*Math.exp(m-lambda-(((double)1)/(((double)12)*m)));
	    //System.out.println("\t c_m="+c_m);
	    // Calcolo del lower bound di R, utilizzando il corollario 3 di pg. 444
	    double k_r = kOut_R*Math.sqrt(((double)2))+(((double)3)/(((double)2)*Math.sqrt(lambda)));
	    double lowerBoundR = c_m*Math.exp(-((k_r + ((double)1))*(k_r + ((double)1)))/((double)2));
	    
	    //System.out.println("\t kOut_l="+kOut_L);
	    // Calcolo del lower bound di L, utilizzando il corollario 4 di pg. 444
	    double k_l = kOut_L +((double)3)/(((double)2)*Math.sqrt(lambda));
	    //System.out.println("\t k_l="+k_l);
	    double lowerBoundL=0.0;
	    if((k_l>0)&&(k_l<=Math.sqrt(lambda)/((double)2))){
	        lowerBoundL=c_m*Math.exp(-((k_l*k_l)/((double)2))-(Math.pow(k_l, 3)/(((double)3)*Math.sqrt(lambda))));
	    }else{
	        double lowerBoundL_ii=0.0;
	        double lowerBoundL_iii=0.0;
	        //System.out.println("\t k_l="+k_l+" <= "+Math.sqrt((m + ((double)1/m)))+" m="+m);
	        if(k_l<=Math.sqrt((m + ((double)1/m)))){
	            lowerBoundL_ii=c_m*Math.pow((((double)1)-(k_l/(Math.sqrt(m+((double)1))))),(k_l*Math.sqrt(m+((double)1))));
	            lowerBoundL_iii=Math.exp(-lambda);
	            lowerBoundL = Math.max(lowerBoundL_ii, lowerBoundL_iii);
	            //System.out.println("\t Corollary4: lowerBound="+lowerBoundL);
	        }else{
	            //TEST- 
	            System.out.println("\t Corollary4: FAIL");
	            return Boolean.FALSE;
	        }  
	    }
	    
	    // Caso particolare: Controllo su L
	    double RR = R.doubleValue();
	    double LL = L.doubleValue();
	    if((lambda>=25)&&((lowerBoundL*overFlowLimit)/(Math.pow(10, 10)*(RR -LL))<=underFlowLimit)){
	        return Boolean.FALSE;
	    }
	    
	    // Caso particolare: controllo su R
	    if((lambda>=400)&&((lowerBoundR*overFlowLimit)/(Math.pow(10, 10)*(RR -LL))<underFlowLimit)){
            return Boolean.FALSE;
        }
	  
	    return Boolean.TRUE;
	}
}