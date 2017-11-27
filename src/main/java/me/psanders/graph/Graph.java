// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders.graph;

import java.util.Set;

/** Represents a Graph construct as found in Graph Theory.
 *
 * <p>A graph is defined by a set of vertices and a set of edges connecting those vertices.
 *
 *
 * @param <L> Type to be used for the graph's vertices or labels
 * @param <T> Numeric type to be used for the graph's weight
 */
public interface Graph<L, T extends Number> {
  /** Returns a list of all vertices' labels.
   *
   * Each label is guaranteed to uniquely determine exactly one vertex, i.e., implementers and
   * users should consider labels synonymous with vertices.
   */
  Set<L> getNodes();

  /** Get the weight of the edge between Start and End
   *
   * <p>If there is an edge directly connecting Start and End, this will return the weight of
   * that edge. Otherwise, it returns <code>null</code>.
   *
   * @param start The label of the starting point
   * @param end The label of the ending point
   * @return Weight of edge connecting the vertices Start and End
   */
  T getWeight(L start, L end);
}