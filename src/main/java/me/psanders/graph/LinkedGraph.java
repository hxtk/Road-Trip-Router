// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-13

package me.psanders.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Represents a Graph Theory graph construct using an Adjacency List.
 *
 * <p>This construct is mutable, allowing both edges and vertices to be added and set
 * after construction.
 */
public class LinkedGraph<L, T extends Number, M extends Map<L, T>> implements Graph<L, T> {

  private Map<L, Map<L,T>> edges;

  public LinkedGraph(Map<L, Map<L,T>> edges) {
    this.edges = edges;
  }

  @Override
  public Set<L> getNodes() {
    return edges.keySet();
  }

  @Override
  public T getWeight(L start, L end) {
    T res = null;
    if (edges.containsKey(start)) {
      res =  edges.get(start).get(end);
    }

    return res;
  }

  /** Adds a new vertex to the graph
   *
   * <p>Creates a new vertex with an empty set of outgoing connections. Does not alter vertices
   * that already existed, in which case it returns the adjacency map for that vertex. On success,
   * it should return null.
   */
  public Map<L, T> addVertex(L name) {
    if (edges.containsKey(name)) {
      return edges.get(name);
    }
    return edges.put(name, new HashMap<L, T>());
  }

  /** Adds a new edge to the graph
   *
   * <p>If this edge was already set, returns the previous value. Otherwise returns null.
   */
  public T addEdge(L from, L to, T weight) {
    T res = null;
    if (edges.containsKey(from) && edges.containsKey(to)) {
      res = edges.get(from).put(to, weight);
    }
    return res;
  }
}