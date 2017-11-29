// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-11-29

package me.psanders.graph.path;

import java.util.List;

import me.psanders.graph.Graph;

public class Path<L, T extends Number> implements Comparable<Path> {
  private Graph<L, T> graph;
  private List<L> order;

  public Path(Graph<L, T> graph, List<L> order) {
    this.graph = graph;
    this.order = order;
  }

  /** Get the cumulative cost of visiting every node in the path, in the order provided.
   *
   * We assume here that the path is valid, e.g., if there is no edge between two consecutive
   * nodes in the correct direction, this code will throw an error.
   */
  public long getCost() {
    long cost = 0;
    for (int i = 1; i < order.size(); ++i) {
      cost += graph.getWeight(order.get(i - 1), order.get(i)).longValue();
    }
    return cost;
  }

  /** Get the list of nodes in the order that this path visits them.
   *
   */
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