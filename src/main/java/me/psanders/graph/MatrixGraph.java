// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders.graph;

import java.util.Map;
import java.util.HashSet;

/** Represents a Graph Theory graph construct using an Adjacency Matrix.
 *
 * <p>This construct is efficient for representing complete or nearly complete graphs. Sparse graphs
 * should use the LinkedGraph implementation, which carries slight overhead and can be harder to
 * construct, but has the advantage of requiring O(V + E) memory instead of O(V^2).
 */
public class MatrixGraph<L, T extends Number> implements Graph<L, T> {

  private T[][] adjMatrix;
  private Map<L, Integer> labels;

  /** Constructs a graph from a map of labels and an adjacency matrix
   *
   * <p>adjMatrix must be a square 2d array. Any non-null value represents a valid edge of its value
   * weight from its row to its column, i.e., the cost of going from i to j shall be
   * <code>adjMatrix[i][j]</code>.
   *
   * <p>The set of vertices in this graph representation is immutable.
   *
   * @param labels Map from the label type to the indices of <code>adjMatrix</code>.
   * @param adjMatrix Matrix of the weight type representing the cost of each edge.
   */
  public MatrixGraph(Map<L, Integer> labels, T[][] adjMatrix) {
    this.labels = labels;
    this.adjMatrix = adjMatrix;
  }

  @Override
  public HashSet<L> getNodes() {
    return new HashSet<L>(labels.keySet());
  }

  @Override
  public T getWeight(L start, L end) {
    T res = null;
    if (labels.containsKey(start) && labels.containsKey(end)) {
      res = this.adjMatrix[this.labels.get(start)][this.labels.get(end)];
    }

    return res;
  }
}