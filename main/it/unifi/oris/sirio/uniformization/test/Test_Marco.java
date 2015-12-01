package it.unifi.oris.sirio.uniformization.test;

import it.unifi.oris.sirio.analyzer.graph.SuccessionGraph;
import it.unifi.oris.sirio.analyzer.graph.SuccessionGraphViewer;
import it.unifi.oris.sirio.math.OmegaBigDecimal;
import it.unifi.oris.sirio.models.pn.PostUpdater;
import it.unifi.oris.sirio.models.stpn.DeterministicEnablingState;
import it.unifi.oris.sirio.models.stpn.DeterministicEnablingStateBuilder;
import it.unifi.oris.sirio.models.stpn.EnablingSyncsEvaluator;
import it.unifi.oris.sirio.models.stpn.RegenerativeTransientAnalysis;
import it.unifi.oris.sirio.models.stpn.RewardRate;
import it.unifi.oris.sirio.models.stpn.StochasticTransitionFeature;
import it.unifi.oris.sirio.models.stpn.TransientSolution;
import it.unifi.oris.sirio.models.stpn.TransientSolutionViewer;
import it.unifi.oris.sirio.models.stpn.policy.TruncationPolicy;
import it.unifi.oris.sirio.petrinet.EnablingFunction;
import it.unifi.oris.sirio.petrinet.Marking;
import it.unifi.oris.sirio.petrinet.MarkingCondition;
import it.unifi.oris.sirio.petrinet.PetriNet;
import it.unifi.oris.sirio.petrinet.Place;
import it.unifi.oris.sirio.petrinet.Transition;

import java.awt.EventQueue;
import java.math.BigDecimal;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A simple model of trains getting on the same line.
 * 
 * <p>
 * Each train runs in parallel: when it gets to the junction point, it records
 * the predecessor's ID and saves its own ID for the next train. Upon arrival, it
 * waits for its predecessor's arrival, and then adds some headway.
 * 
 * @author Marco Paolieri
 */
public class Test_Marco {
    
    /**
     * Add a train to the Petri net model.
     * 
     * @param id           ID of the train (used for place and transition names) 
     * @param startTime    Nominal arrival time of the train 
     * @param fast         Fast train flag
     * @param pn           Add the train model to this Petri net 
     * @param m            Add the initial token of the train to this marking
     * @param lastEntered  Place used to record the last entered train
     * @param allIDs       IDs of all trains
     */
    private static void addTrain(int id, BigDecimal startTime, boolean fast, PetriNet pn, Marking m, 
            Place lastEntered, Integer... allIDs) {
        

        String name = (fast ? "V" : "R") + id;               // name of this train
        Place pred = pn.addPlace(name+"Pred");               // predecessor train's ID on shared line
        
        Place out = pn.addPlace(name+"Out");                 // train outside system
        Place in = pn.addPlace(name+"In");                   // train arrived to the junction point
        Place entered = pn.addPlace(name+"Entered");         // train entered the shared line
        Place waiting = pn.addPlace(name+"Waiting");         // train waiting for its predecessor to arrive
        Place completed = pn.addPlace(name+"Completed");     // predecessor arrived 
        Place withHeadway = pn.addPlace("head"+id);          // headway from predecessor
        
        Transition start = pn.addTransition(name+"Start");
        start.addFeature(StochasticTransitionFeature.newDeterministicInstance(startTime));
        pn.addPrecondition(out, start);
        pn.addPostcondition(start, in);
        
        Transition enter = pn.addTransition(name+"Enter");
        enter.addFeature(StochasticTransitionFeature.newUniformInstance(
                new OmegaBigDecimal(0), new OmegaBigDecimal(fast ? 10 : 20)));
        enter.addFeature(new PostUpdater(pred+"="+lastEntered+", "+lastEntered+"="+id, pn));
        pn.addPrecondition(in, enter);
        pn.addPostcondition(enter, entered);
        
        Transition complete = pn.addTransition(name+"Complete");
        complete.addFeature(StochasticTransitionFeature.newUniformInstance(
                new OmegaBigDecimal(fast ? 10 : 20), new OmegaBigDecimal(fast ? 12 : 24)));
        pn.addPrecondition(entered, complete);
        pn.addPostcondition(complete, waiting);

        Transition predArrival = pn.addTransition(name+"PredArrival");
        predArrival.addFeature(StochasticTransitionFeature.newDeterministicInstance(BigDecimal.ZERO));
        pn.addPrecondition(waiting, predArrival);
        pn.addPostcondition(predArrival, completed);
        
        StringBuilder b = new StringBuilder();
        b.append(pred+"==0"); // no predecessor
        for (Integer n: allIDs) {
            if (n != id) {
                b.append("|| ("+pred+"=="+n+" && head"+n+"==1)");
            }
        }
        predArrival.addFeature(new EnablingFunction(b.toString()));
        
        Transition headway = pn.addTransition(name+"Headway");
        headway.addFeature(StochasticTransitionFeature.newDeterministicInstance(BigDecimal.ONE));
        pn.addPrecondition(completed, headway);
        pn.addPostcondition(headway, withHeadway);
        
        m.setTokens(out, 1);
    }
    
    
    public static void main(String[] args) {
        
        // build the model with three trains
        PetriNet pn = new PetriNet();
        Marking m = new Marking();
        
        Place lastEntered = pn.addPlace("lastEnteredTrain");
        Integer[] allIDs = new Integer[] {1,2,3};
        
        addTrain(1, new BigDecimal(10), false, pn, m, lastEntered, allIDs);
        addTrain(2, new BigDecimal(25), true, pn, m, lastEntered, allIDs);
        addTrain(3, new BigDecimal(35), false, pn, m, lastEntered, allIDs);
        
        // start regenerative transient analysis 
        OmegaBigDecimal timeBound = new OmegaBigDecimal("90");
        BigDecimal step = new BigDecimal("1");
        BigDecimal error = BigDecimal.ZERO;
        TransientSolution<DeterministicEnablingState, Marking> solution = RegenerativeTransientAnalysis
                .compute(pn, new DeterministicEnablingState(m, pn),
                        new DeterministicEnablingStateBuilder(pn, true),
                        new EnablingSyncsEvaluator(),
                        new TruncationPolicy(error, new OmegaBigDecimal(timeBound)), false, false)
                .solveDiscretizedMarkovRenewal(timeBound.bigDecimalValue(), step,
                        MarkingCondition.ANY, false, null, null);
        
        TransientSolution<DeterministicEnablingState, RewardRate> rewards = 
                TransientSolution.computeRewards(false, solution, "head1;head2;head3");

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

    public static void showPlot(TransientSolution<DeterministicEnablingState, RewardRate> rewards) {
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