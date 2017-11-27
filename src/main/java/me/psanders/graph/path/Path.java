// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders.graph.path;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.psanders.graph.Graph;

public class Path<L> implements Comparable<Path> {
  private Graph<L, Long> graph;
  private List<L> order;

  public Path(Graph<L, Long> graph, List<L> order) {
    this.graph = graph;
    this.order = order;
  }

  public long getCost() {
    long cost = 0;
    for (int i = 1; i < order.size(); ++i) {
      cost += graph.getWeight(order.get(i - 1), order.get(i));
    }
    return cost;
  }

  public List<L> getOrder() {
    return order;
  }

  @Override
  public int compareTo(Path other) {
    return (int) (this.getCost() - other.getCost());
  }

  @Override
  public String toString() {
    return "Cost: " + getCost() + "\nRoute: " + this.order.toString();
  }
}