// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-11-29

package me.psanders.graph.path;

import me.psanders.graph.Graph;
import me.psanders.utils.LexicographicPermuter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BruteForceOptimizationStrategy<L extends Comparable<L>, T extends Number>
    implements OptimizationStrategy<L, T> {

  /** Return the cheapest Hamiltonian Cycle through a complete graph.
   *
   * <p>This is the true cheapest cycle, but it has factorial running time. We use this for testing
   * to verify that other strategies have an acceptable probability of being within a given margin
   * of error from this optimal cycle.
   */
  @Override
  public Cycle<L, T> getOptimalCycle(Graph<L, T> graph) {
    List<L> list = new ArrayList<L>();
    list.addAll(graph.getNodes());
    Collections.sort(list);

    // Check every permutation and store the shortest one.
    Cycle<L, T> cheapestCycle = new Cycle<L, T>(graph, list.subList(0, list.size()));
    for (List<L> it: new LexicographicPermuter<L>(list)) {
      Cycle<L, T> tmpCycle = new Cycle<L, T>(graph, list.subList(0, list.size()));
      if (tmpCycle.getCost() < cheapestCycle.getCost()) cheapestCycle = tmpCycle;
    }

    return cheapestCycle;
  }
}