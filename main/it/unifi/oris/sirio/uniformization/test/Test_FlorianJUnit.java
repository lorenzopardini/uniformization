package it.unifi.oris.sirio.uniformization.test;

import static org.junit.Assert.*;

import org.junit.Test;

import it.unifi.oris.sirio.uniformization.main.*;

public class Test_FlorianJUnit {

    private double errorOn(double expected, double got){
        return Math.abs(expected-got);
    }
    
    @Test
    public void testFlorianFirstTransientProb() {
        double matrixUnif[][]= {{0.1, 0.9, 0, 0},{0.4, 0, 0.6, 0}, {0, 0.4, 0.3, 0.3},{0, 0, 0.4, 0.6}};
        double initialProbsVector[]= {1.0,0.0,0.0,0.0};
        double lambda = 50;
        double wantedAccuracy = Math.pow(10, -6);
        TransientProbabilitiesCTMC obj = TransientProbabilitiesCTMC.getFoxGlynnSolution(lambda, wantedAccuracy, matrixUnif, initialProbsVector);
        double[] ret = obj.getCtmcTransientProbabilitiesByUniformization();
        
        assertTrue(errorOn(0.109215,ret[0])<=Math.pow(10.0, -6));
    }
    
    @Test
    public void testFlorianSecondTransientProb() {
        double matrixUnif[][]= {{0.1, 0.9, 0, 0},{0.4, 0, 0.6, 0}, {0, 0.4, 0.3, 0.3},{0, 0, 0.4, 0.6}};
        double initialProbsVector[]= {1.0,0.0,0.0,0.0};
        double lambda = 50;
        double wantedAccuracy = Math.pow(10, -6);
        TransientProbabilitiesCTMC obj = TransientProbabilitiesCTMC.getFoxGlynnSolution(lambda, wantedAccuracy, matrixUnif, initialProbsVector);
        double[] ret = obj.getCtmcTransientProbabilitiesByUniformization();
        
        assertTrue(errorOn(0.245734,ret[1])<=Math.pow(10.0, -6));
    }
    
    @Test
    public void testFlorianThirdTransientProb() {
        double matrixUnif[][]= {{0.1, 0.9, 0, 0},{0.4, 0, 0.6, 0}, {0, 0.4, 0.3, 0.3},{0, 0, 0.4, 0.6}};
        double initialProbsVector[]= {1.0,0.0,0.0,0.0};
        double lambda = 50;
        double wantedAccuracy = Math.pow(10, -6);
        TransientProbabilitiesCTMC obj = TransientProbabilitiesCTMC.getFoxGlynnSolution(lambda, wantedAccuracy, matrixUnif, initialProbsVector);
        double[] ret = obj.getCtmcTransientProbabilitiesByUniformization();
        
        assertTrue(errorOn(0.368600,ret[2])<=Math.pow(10.0, -6));
    }
    
    @Test
    public void testFlorianFourthTransientProb() {
        double matrixUnif[][]= {{0.1, 0.9, 0, 0},{0.4, 0, 0.6, 0}, {0, 0.4, 0.3, 0.3},{0, 0, 0.4, 0.6}};
        double initialProbsVector[]= {1.0,0.0,0.0,0.0};
        double lambda = 50;
        double wantedAccuracy = Math.pow(10, -6);
        TransientProbabilitiesCTMC obj = TransientProbabilitiesCTMC.getFoxGlynnSolution(lambda, wantedAccuracy, matrixUnif, initialProbsVector);
        double[] ret = obj.getCtmcTransientProbabilitiesByUniformization();
        
        assertTrue(errorOn(0.276450,ret[3])<=Math.pow(10.0, -6));
    }
   
}