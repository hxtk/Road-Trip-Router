// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders.graph.path;

import java.util.Random;
import java.util.Arrays;

import me.psanders.graph.Graph;

public class HCycleFinder {

  private Graph<String, Long> graph;
  private OptimizationStrategy strategy;

  public HCycleFinder(Graph<String, Long> graph, OptimizationStrategy strategy) {
    this.graph = graph;
    this.strategy = strategy;
  }

  public Cycle getOptimalCycle() {
    return strategy.getOptimalCycle(this.graph);
  }
}