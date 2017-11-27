// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders.graph.path;

import java.util.List;

import me.psanders.graph.Graph;

public class Cycle<L> extends Path {

  private Graph<L, Long> graph;
  private List<L> order;

  public Cycle(Graph<L, Long> graph, List<L> order) {
    super(graph, order);
    this.graph = graph;
    this.order = order;
  }

  @Override
  public long getCost() {
    long cost = 0;
    for (int i = 0; i < order.size(); ++i) {
      cost += graph.getWeight(order.get(i), order.get((i + 1) % order.size()));
    }
    return cost;
  }
}