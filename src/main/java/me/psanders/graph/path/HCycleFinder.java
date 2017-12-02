// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders.graph.path;

import me.psanders.graph.Graph;

public class HCycleFinder {

  private Graph<String, Long> graph;
  private OptimizationStrategy strategy;

  public HCycleFinder(Graph<String, Long> graph, OptimizationStrategy strategy) {
    this.graph = graph;
    this.strategy = strategy;
  }

  public Cycle getOptimalCycle() {
    if (graph == null) {
      return null;
    }
    return strategy.getOptimalCycle(this.graph);
  }
}
