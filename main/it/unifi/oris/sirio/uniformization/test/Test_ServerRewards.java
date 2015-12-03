package it.unifi.oris.sirio.uniformization.test;

import it.unifi.oris.sirio.models.gspn.GSPNGraphAnalyzer;
import it.unifi.oris.sirio.models.gspn.GSPNOperator;
import it.unifi.oris.sirio.models.gspn.GSPNGraphGenerator;
import it.unifi.oris.sirio.models.gspn.RateExpressionFeature;
import it.unifi.oris.sirio.models.gspn.TransientAndSteadyMatrixes;
import it.unifi.oris.sirio.models.gspn.WeightExpressionFeature;
import it.unifi.oris.sirio.models.stpn.DeterministicEnablingState;
import it.unifi.oris.sirio.models.stpn.DeterministicEnablingStateBuilder;
import it.unifi.oris.sirio.models.stpn.EnablingSyncsEvaluator;
import it.unifi.oris.sirio.models.stpn.RegenerativeTransientAnalysis;
import it.unifi.oris.sirio.models.stpn.RewardRate;
import it.unifi.oris.sirio.models.stpn.StochasticTransitionFeature;
import it.unifi.oris.sirio.models.stpn.TransientSolution;
import it.unifi.oris.sirio.models.stpn.TransientSolutionViewer;
import it.unifi.oris.sirio.models.stpn.policy.TruncationPolicy;
import it.unifi.oris.sirio.models.tpn.Priority;
import it.unifi.oris.sirio.petrinet.EnablingFunction;
import it.unifi.oris.sirio.petrinet.Marking;
import it.unifi.oris.sirio.petrinet.MarkingCondition;
import it.unifi.oris.sirio.petrinet.PetriNet;
import it.unifi.oris.sirio.petrinet.Place;
import it.unifi.oris.sirio.petrinet.Transition;
//import it.unifi.oris.sirio.uniformization.main.*;

import java.awt.EventQueue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.Integer;
import java.math.BigDecimal;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.unifi.oris.sirio.analyzer.graph.*;
import it.unifi.oris.sirio.analyzer.log.AnalysisLogger;
import it.unifi.oris.sirio.analyzer.log.AnalysisMonitor;
import it.unifi.oris.sirio.math.OmegaBigDecimal;

public class Test_ServerRewards {

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
          
    	  System.out.println("Avvio...");
    	  
          // creazione della GSPN
          PetriNet net = new PetriNet();
          Marking marking = new Marking();
          System.out.println("Creazione PN...");
          build(net,marking);
          System.out.println("Creazione PN... Fatto");
          //generazione della catena
          
          System.out.println("Creazione graph...");
          SuccessionGraph graph = GSPNGraphGenerator.generateGraph(net, marking, null, null);
          // TBD: stopCondition null funziona?
          System.out.println("Creazione graph... Fatto");
          
          // lancio l'analche??
          GSPNGraphAnalyzer analyzer = new GSPNGraphAnalyzer(graph, net);
          System.out.println("Creazione graphAnalyzer... Fatto");
          
          Map<Node, Double> rootMap = GSPNGraphGenerator.calculateRootAbsorption(
                                      analyzer, graph.getRoot());
          System.out.println("Creazione rootMap... Fatto");
          
          Map<SuccessionGraph,Double> mapOfGraphs = GSPNGraphGenerator.generateReducedGraphs(analyzer,
                                                      rootMap);
          System.out.println("Creazione MapOfGraph... Fatto");
         
          // calcolo della matrice Q (generatore infinitesimale)
          double[][] Q = TransientAndSteadyMatrixes
                  .createInfinitesimalGeneratorForTransient(analyzer.getTangiblesStateList(),
                          mapOfGraphs);
          System.out.println("Creazione Q... Fatto"); 
          
          // calcolo delle probabilita' iniziali
          double[] initialProbsVector = new double[Q.length];
          for (Node n : rootMap.keySet()) {
              initialProbsVector[analyzer.getTangiblesList().indexOf(n)] = rootMap.get(n)
                      .doubleValue();
          }
          System.out.println("Calcolo initial Probs... Fatto");
          
          //calcolo del tasso
          double gamma = TransientAndSteadyMatrixes.findLambda(Q);
          System.out.println("Lambda... Fatto");
          
          double time = 15.0;
   
          double lambda = gamma * time;
                  
          double wantedAccuracy = Math.pow(10.0, -6.0);
                  
          double[][] P = TransientAndSteadyMatrixes.createMatrixUniformized(Q, gamma);
                  
          // nuovo
          BigDecimal timeLimit = new BigDecimal("15.0");
          BigDecimal step = new BigDecimal("0.1");
          BigDecimal error = new BigDecimal("0.000000000000000001");
          
          System.out.println("Creo il GSPN Operator...");
          GSPNOperator gspnOp = new GSPNOperator(timeLimit, step, error, net, marking, false, MarkingCondition.NONE, null, null); 
          System.out.println("Creo il GSPN Operator... Fatto");
          System.out.println("Eseguo getTransientS()...");
          TransientSolution<Marking,Marking> solution = gspnOp.getTransientS();
          System.out.println("Eseguo getTransientS()... Fatto");
          boolean cumulative = false;
          System.out.println("Calcolo Rewards...");
          TransientSolution<Marking, RewardRate> rewards = TransientSolution.computeRewards(cumulative, solution, "Running;ToSwitchOn+On;Failed;");
          System.out.println("Calcolo Rewards... Fatto");
          
          showPlot(rewards);
          
          
      }
      
      public static void showGraph(SuccessionGraph graph) {
          System.out.println("The graph contains " + graph.getStates().size() + " states and " + graph.getSuccessions().size() + " transitions");

          final JPanel viewer = new SuccessionGraphViewer(graph);
          EventQueue.invokeLater(new Runnable() {

                  @Override public void run() {
                          JFrame frame = new JFrame("State graph");
                      frame.add(viewer);
                      frame.setDefaultCloseOperation(3);
                      frame.setExtendedState(6);
                      frame.pack();
                      frame.setLocationRelativeTo(null);
                      frame.setVisible(true);
                  }
          });
      }

      public static void showPlot(TransientSolution<Marking, RewardRate> rewards) {
          final JPanel plot = TransientSolutionViewer.solutionChart(rewards);
          EventQueue.invokeLater(new Runnable() {

              @Override
              public void run() {
                  JFrame frame = new JFrame("Plot");
                  frame.add(plot);
                  frame.setDefaultCloseOperation(3);
                  frame.setExtendedState(6);
                  frame.pack();
                  frame.setLocationRelativeTo(null);
                  frame.setVisible(true);
              }
          });
      }

}
