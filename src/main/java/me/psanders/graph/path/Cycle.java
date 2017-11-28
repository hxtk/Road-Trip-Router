// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders.graph.path;

import java.util.List;

import me.psanders.graph.Graph;

public class Cycle<L, T extends Number> extends Path {

  private Graph<L, T> graph;
  private List<L> order;

  public Cycle(Graph<L, T> graph, List<L> order) {
    super(graph, order);
    this.graph = graph;
    this.order = order;
  }

  @Override
  public long getCost() {
    long cost = 0;
    for (int i = 0; i < order.size(); ++i) {
      cost += graph.getWeight(order.get(i), order.get((i + 1) % order.size())).longValue();
    }
    return cost;
  }
}