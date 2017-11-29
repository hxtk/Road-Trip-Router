

package me.psanders.graph.path;

import me.psanders.graph.Graph;
import me.psanders.graph.MatrixGraph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Random;

@RunWith(JUnit4.class)
public class GeneticOptimizationStrategyTest {

  private static final Integer[][] MATRIX = {
      {2, 36, 19, 23, 36, 94, 43, 69, 52, 13},
      {86, 99, 30, 45, 67, 15, 4, 54, 64, 29},
      {79, 66, 20, 74, 52, 57, 74, 33, 64, 88},
      {14, 96, 75, 99, 36, 62, 1, 79, 58, 48},
      {21, 2, 36, 5, 50, 97, 50, 25, 39, 78},
      {11, 97, 68, 30, 93, 57, 53, 17, 64, 18},
      {59, 58, 35, 90, 14, 30, 64, 35, 55, 75},
      {27, 77, 74, 26, 63, 52, 92, 19, 85, 55},
      {16, 33, 87, 0, 44, 64, 92, 10, 90, 33},
      {72, 64, 23, 65, 38, 17, 57, 42, 4, 16}
  };
  // Determined using BruteForceOptimizationStrategy
  private static final int OPTIMAL_COST = 148;

  //private Cycle<Integer, Integer> optimalPath;
  private Graph<Integer, Integer> graph;

  @Before
  public void computeOptimalSolutions() {
    HashMap<Integer, Integer> labels = new HashMap();
    for (int i = 0; i < MATRIX.length; ++i) {
      labels.put(i, i);
    }
    graph = new MatrixGraph(labels, MATRIX);

    // Commented out 2017-11-29: Saved the computed cost in `OPTIMAL_COST` constant to save
    // computation time in the unit test.
    //optimalPath = new BruteForceOptimizationStrategy<Integer, Integer>().getOptimalCycle(graph);
  }

  @Test
  public void getsCloseEnough() {
    Cycle genetic = new GeneticOptimizationStrategy(new Random(0)).getOptimalCycle(graph);
    double cost = (double) genetic.getCost();
    Assert.assertTrue(0.95*cost < OPTIMAL_COST);
  }

}