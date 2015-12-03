package it.unifi.oris.sirio.uniformization.test;

import it.unifi.oris.sirio.models.gspn.GSPNGraphAnalyzer;
import it.unifi.oris.sirio.models.gspn.GSPNGraphGenerator;
import it.unifi.oris.sirio.models.gspn.RateExpressionFeature;
import it.unifi.oris.sirio.models.gspn.TransientAndSteadyMatrixes;
import it.unifi.oris.sirio.models.gspn.WeightExpressionFeature;
import it.unifi.oris.sirio.models.stpn.StochasticTransitionFeature;
import it.unifi.oris.sirio.models.tpn.Priority;
import it.unifi.oris.sirio.petrinet.EnablingFunction;
import it.unifi.oris.sirio.petrinet.Marking;
import it.unifi.oris.sirio.petrinet.PetriNet;
import it.unifi.oris.sirio.petrinet.Place;
import it.unifi.oris.sirio.petrinet.Transition;

import it.unifi.oris.sirio.uniformization.main.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.Integer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import java.io.*;

import it.unifi.oris.sirio.analyzer.graph.*;

public class Test_Server {

      /**
       * Questo metodo costruisce la GPSN del modello di Florjan. 
       * @param net la rete che verra' restituita popolata (input/output)
       * @param marking la marcatura annessa (input/output)
       */
      public static void build(PetriNet net, Marking marking) {

        //Generating Nodes
        Place Failed = net.addPlace("Failed");
        Place Idle = net.addPlace("Idle");
        Place Off = net.addPlace("Off");
        Place On = net.addPlace("On");
        Place Running = net.addPlace("Running");
        Place ToSwitchOff = net.addPlace("ToSwitchOff");
        Place ToSwitchOn = net.addPlace("ToSwitchOn");
        Place switchedOn = net.addPlace("switchedOn");
        Transition arrival = net.addTransition("arrival");
        Transition ko = net.addTransition("ko");
        Transition ok = net.addTransition("ok");
        Transition repair = net.addTransition("repair");
        Transition served = net.addTransition("served");
        Transition t0 = net.addTransition("t0");
        Transition t1 = net.addTransition("t1");
        Transition t2 = net.addTransition("t2");
        Transition t3 = net.addTransition("t3");
        Transition t4 = net.addTransition("t4");

        //Generating Connectors
        net.addInhibitorArc(ToSwitchOn, t2);
        net.addInhibitorArc(ToSwitchOff, t0);
        net.addInhibitorArc(ToSwitchOn, t2);
        net.addInhibitorArc(ToSwitchOff, t0);
        net.addPrecondition(switchedOn, ok);
        net.addPostcondition(served, Idle);
        net.addPostcondition(served, On);
        net.addPrecondition(On, t4);
        net.addPostcondition(t2, ToSwitchOn);
        net.addPrecondition(On, t0);
        net.addPrecondition(Failed, repair);
        net.addPostcondition(ko, Failed);
        net.addPostcondition(t1, Off);
        net.addPrecondition(Failed, repair);
        net.addPrecondition(Idle, arrival);
        net.addPostcondition(t4, Failed);
        net.addPrecondition(switchedOn, ko);
        net.addPostcondition(t4, Failed);
        net.addPostcondition(repair, Off);
        net.addPrecondition(ToSwitchOff, t1);
        net.addPrecondition(Idle, arrival);
        net.addPostcondition(arrival, Running);
        net.addPostcondition(t1, Off);
        net.addPrecondition(Running, served);
        net.addPrecondition(On, served);
        net.addPrecondition(Off, t2);
        net.addPostcondition(t0, ToSwitchOff);
        net.addPostcondition(ok, On);
        net.addPrecondition(ToSwitchOn, t3);
        net.addPostcondition(arrival, Running);
        net.addPostcondition(repair, Off);
        net.addPostcondition(t3, switchedOn);

        //Generating Properties
        marking.setTokens(Failed, 0);
        marking.setTokens(Idle, 0);
        marking.setTokens(Off, 4);
        marking.setTokens(On, 0);
        marking.setTokens(Running, 8);
        marking.setTokens(ToSwitchOff, 0);
        marking.setTokens(ToSwitchOn, 0);
        marking.setTokens(switchedOn, 0);
        arrival.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1")));
        arrival.addFeature(new RateExpressionFeature("1*Idle"));
        ko.addFeature(StochasticTransitionFeature.newDeterministicInstance(new BigDecimal("0"), new BigDecimal("1")));
        ko.addFeature(new WeightExpressionFeature("5"));
        ko.addFeature(new Priority(new Integer("0")));
        ok.addFeature(StochasticTransitionFeature.newDeterministicInstance(new BigDecimal("0"), new BigDecimal("1")));
        ok.addFeature(new WeightExpressionFeature("95"));
        ok.addFeature(new Priority(new Integer("0")));
        repair.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1")));
        repair.addFeature(new RateExpressionFeature("0.1*min(Failed,2)"));
        served.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1")));
        served.addFeature(new RateExpressionFeature("4*min(On,Running)"));
        t0.addFeature(new EnablingFunction("Running<(On*2-2)"));
        t0.addFeature(StochasticTransitionFeature.newDeterministicInstance(new BigDecimal("0"), new BigDecimal("1")));
        t0.addFeature(new WeightExpressionFeature("1"));
        t0.addFeature(new Priority(new Integer("0")));
        t1.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1")));
        t1.addFeature(new RateExpressionFeature("1*ToSwitchOff"));
        t2.addFeature(new EnablingFunction("Running>(2*On+2)"));
        t2.addFeature(StochasticTransitionFeature.newDeterministicInstance(new BigDecimal("0"), new BigDecimal("1")));
        t2.addFeature(new WeightExpressionFeature("1"));
        t2.addFeature(new Priority(new Integer("0")));
        t3.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1")));
        t3.addFeature(new RateExpressionFeature("1*ToSwitchOn"));
        t4.addFeature(StochasticTransitionFeature.newExponentialInstance(new BigDecimal("1")));
        t4.addFeature(new RateExpressionFeature("On*0.01"));
      }
 
      public static void main(String args[]){
          
          // creazione della GSPN
          PetriNet net = new PetriNet();
          Marking marking = new Marking();
          build(net,marking);
          
          //generazione della catena
          
          SuccessionGraph graph = GSPNGraphGenerator.generateGraph(net, marking, null, null);
          // TBD: stopCondition null funziona?
          
          // lancio l'analche??
          GSPNGraphAnalyzer analyzer = new GSPNGraphAnalyzer(graph, net);
          
          Map<Node, Double> rootMap = GSPNGraphGenerator.calculateRootAbsorption(
                                      analyzer, graph.getRoot());
          
          Map<SuccessionGraph,Double> mapOfGraphs = GSPNGraphGenerator.generateReducedGraphs(analyzer,
                                                      rootMap);
          
          // calcolo della matrice Q (generatore infinitesimale)
          double[][] Q = TransientAndSteadyMatrixes
                  .createInfinitesimalGeneratorForTransient(analyzer.getTangiblesStateList(),
                          mapOfGraphs);
           
          // calcolo delle probabilita' iniziali
          double[] initialProbsVector = new double[Q.length];
          for (Node n : rootMap.keySet()) {
              initialProbsVector[analyzer.getTangiblesList().indexOf(n)] = rootMap.get(n)
                      .doubleValue();
          }
          
          //calcolo del tasso
          double gamma = TransientAndSteadyMatrixes.findLambda(Q);
          
          
          //FILE
 
          BufferedWriter writer = null;
          try{
              
              //creazione file 
              String timeLog = new String("/home/lore/Desktop/FlorjanSumError.csv");
              File logFile = new File(timeLog);
              writer = new BufferedWriter(new FileWriter(logFile));
              
              String timeString;
              String out;
              
           // time, example
              double time = 39.0;
              //System.out.println(gamma);
              double TIME_MAX = 40.0;
              double STEP = 0.01;
              //for(time = 39.9; time<TIME_MAX; time+=STEP){
              //for(double exp=1.0; exp<25.0; exp+=1.0){    
                  double lambda = gamma * time;
                  
                  double wantedAccuracy = Math.pow(10.0, -6.0);
                  
                  double[][] P = TransientAndSteadyMatrixes.createMatrixUniformized(Q, gamma);
                  
                  TransientProbabilitiesCTMC newSolution = TransientProbabilitiesCTMC.getFoxGlynnSolution(lambda, wantedAccuracy, P, initialProbsVector);
                  TransientProbabilitiesCTMC oldSolution = TransientProbabilitiesCTMC.getOldSolution(lambda, wantedAccuracy, P, initialProbsVector);
                  out = Double.valueOf(lambda).toString() + ",";
                  //long time1 = System.currentTimeMillis();
                  double[] transientProbsNew = newSolution.getCtmcTransientProbabilitiesByUniformization();
                  //long time2 = System.currentTimeMillis();
                  //out += Long.valueOf(time2-time1).toString() + ",";
                  //time1 = System.currentTimeMillis();
                  double[] transientProbsOld = oldSolution.getCtmcTransientProbabilitiesByUniformization();
                  double sumOld = 0.0;
                  double sumNew = 0.0;
                  System.out.println(transientProbsNew.length);
                  for(int j=0; j<transientProbsOld.length; j++){ 
                      sumOld+=transientProbsOld[j];
                      sumNew+=transientProbsNew[j];
                  }

                  
                  //time2 = System.currentTimeMillis();
                  out += Double.valueOf(sumNew).toString() + ",";
                  out += Double.valueOf(sumOld).toString();
                  //out = 
                  //out = Double.valueOf(time).toString() + "," + Double.valueOf(transientProbsNew[7]).toString() + "," + Double.valueOf(transientProbsOld[7]).toString();
                  
                  out += "\n";
                  
                  //writer.write(out);

          // }//END FOR
              
          }catch(Exception e) {
              e.printStackTrace();
          } finally {
              try {
                  // Close the writer regardless of what happens...
                  writer.close();
                  System.out.println("finito!");
              } catch (Exception e) {
          }
          
          
          
          
          /*
          
          double sumNew =0.0;
          double sumOld = 0.0;
          for(int i=0; i<transientProbsNew.length; i++){
              sumOld+=transientProbsOld[i];
              sumNew+=transientProbsNew[i];
              System.out.println(transientProbsNew[i]+"\t"+transientProbsOld[i]);
          }
          System.out.println("sum New = " + sumNew);
          System.out.println("sum Old = " + sumOld);
          
          
          */
          
          }
      }
}
