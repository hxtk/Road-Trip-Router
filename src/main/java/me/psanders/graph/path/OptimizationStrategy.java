// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-17

package me.psanders.graph.path;

import me.psanders.graph.Graph;

/** Strategy Interface for the optimization to be performed by HCycleFinder
 */
interface OptimizationStrategy<L, T extends Number> {

  /** Returns what the concrete strategy finds to be the shortest Hamiltonian Cycle.
   *
   * Note, this may differ from the actual best cycle.
   */
  Cycle getOptimalCycle(Graph<L, T> graph);
}