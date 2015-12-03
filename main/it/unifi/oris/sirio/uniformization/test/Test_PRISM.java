package it.unifi.oris.sirio.uniformization.test;

import it.unifi.oris.sirio.uniformization.main.TransientProbabilitiesCTMC;
import static org.junit.Assert.*;
import org.junit.Test;

public class Test_PRISM {

	public static void main(String[] args){
		
		// transient probabilities matrix (embedded)
        double matrixUnif[][]= {{0.6078431372549019,	0.3921568627450981,	0.0,	0.0}	,
                {0.29411764705882354,	0.31372549019607837,	0.3921568627450981,	0.0	},
                {0.0,	0.5882352941176471,	0.019607843137254832,	0.3921568627450981}	,
                {0.0,	0.0,	0.8823529411764707,	0.11764705882352935}};
        
        // initial state probabilities
        double initialProbsVector[]= {1.0,0.0,0.0,0.0};
		
        double gamma = 10.404;
		
        double time = 5.0;
        
        double lambda = gamma * time;
        
        // accuracy
        double wantedAccuracy = Math.pow(1.25, -7);
        
        TransientProbabilitiesCTMC NewSolution = TransientProbabilitiesCTMC.getFoxGlynnSolution(lambda, wantedAccuracy, matrixUnif, initialProbsVector);

        double[] ret = NewSolution.getCtmcTransientProbabilitiesByUniformization();
        
        double prism[] = { 0.2764505119453925,	0.3686006825938566,	0.24573378839590437,	0.10921501706484639 };
        
        System.out.println(NewSolution.getLR().getFirst().toString()+","+NewSolution.getLR().getSecond().toString());
        System.out.println("lambda = "+lambda);
        
        System.out.println("\n piGreco CTMC: \n");
        
        //TEST JUnit
        for(int i=0; i<ret.length; i++){
            assertTrue(Math.abs(ret[i]-prism[i])<wantedAccuracy);	
        }
        
	}
	
}
