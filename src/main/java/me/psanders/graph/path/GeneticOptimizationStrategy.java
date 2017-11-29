// Copyright (c) Peter Sanders. All rights reserved.
// Date: 2017-10-18

package me.psanders.graph.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import me.psanders.graph.Graph;

/** Finds a "probably optimal" route through a complete graph using a genetic algorithm.
 *
 * <p>This algorithm is only guaranteed to work on a complete graph. Incomplete graphs can be
 * represented by using arbitrarily large values for the edge weight without affecting the optimal
 * route.
 *
 * <p>The parameters of the genetic algorithm are as used in (DeJong & Spears, 1990) cited below,
 * with the exceptions that we use permutation encoding rather than binary encoding and uniform
 * mutation rather than two-segment.
 *
 */
public class GeneticOptimizationStrategy<L, T extends Number>
    implements OptimizationStrategy<L, T> {

  private Random random;

  // DeJong, K.A. and Spears, W.M. "An Analysis of the Interacting Roles of Population Size and
  // Crossover in Genetic Algorithms," Proc. First Workshop Parallel Problem Solving from Nature,
  // Springer-Verlag, Berlin, 1990. pp. 38-47.
  //
  // Modified from their values: 50 species did not optimize quickly enough. Increasing to 500
  // obviated the large number of generations, permitting us to reduce to 100 instead of 1000.
  static final int POPULATION = 500;
  static final double CROSSOVER = 0.6;
  static final int GENERATIONS = 100;

  public GeneticOptimizationStrategy(Random random) {
    this.random = random;
  }

  /** Genetic algorithm to optimize a path through a complete graph.
   *
   * @return Cycle with a high probability of being optimal, depending on the parameters above
   */
  @Override
  public Cycle getOptimalCycle(Graph<L, T> graph) {
    int numNodes = graph.getNodes().size();
    int batchSize = Math.max(numNodes * 2, POPULATION);
    int numParents = batchSize / 10;

    ArrayList<L> labels = new ArrayList();
    labels.addAll(graph.getNodes());
    Cycle start = new Cycle(graph, labels);

    // Initially use a population of completely random orderings.
    Cycle[] paths = new Cycle[batchSize];
    for (int i = 0; i < batchSize; ++i) {
      List order = start.getOrder();
      Collections.shuffle(order, this.random);
      paths[i] = new Cycle(graph, order);
    }

    // Number of swaps such that CROSSOVER is the probability of an element remaining unmoved
    int swaps = (int) (Math.log(CROSSOVER) / Math.log(1.0D - (2.0D / (double) (numNodes))));

    for (int i = 0; i < GENERATIONS; ++i) {

      // Start off with the top `numParents` best paths from the previous round
      Path[] best = Arrays.copyOfRange(paths, 0, numParents);
      System.arraycopy(best, 0, paths, 0, best.length);

      // Mutate the best old paths to create the new paths
      // NOTE: starts after the end of the copy operation above
      for (int j = best.length; j < batchSize; ++j) {
        // We mod with `numParents` to achieve a round robin, repeatedly using each parent in turn.
        List<String> order = new ArrayList(best[j % numParents].getOrder());

        // Swap enough times such that we probably have 60% crossover, as per DeJong & Spears above.
        for (int k = 0; k < swaps; ++k) {
          int a = random.nextInt(order.size());
          int b = random.nextInt(order.size());

          Collections.swap(order, a, b);
        }

        paths[j] = new Cycle(graph, order);
      }

      // Sort such that best paths are at the beginning of the list
      Arrays.sort(paths);
    }

    return paths[0];
  }
}