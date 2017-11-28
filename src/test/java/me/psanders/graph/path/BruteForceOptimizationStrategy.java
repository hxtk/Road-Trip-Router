
package me.psanders.graph.path;

import me.psanders.graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BruteForceOptimizationStrategy<L extends Comparable, T extends Number>
    implements OptimizationStrategy<L, T> {

  @Override
  public Cycle<L, T> getOptimalCycle(Graph<L, T> graph) {
    List<L> list = new ArrayList<L>();
    list.addAll(graph.getNodes());
    Collections.sort(list);

    // Check every permutation and store the shortest one.
    Cycle<L, T> cheapestCycle = new Cycle<L, T>(graph, list.subList(0, list.size()));
    for (List<L> it = list; list != null; list = nextLexicographicPermutation(list)) {
      Cycle<L, T> tmpCycle = new Cycle<L, T>(graph, list.subList(0, list.size()));
      if (tmpCycle.getCost() < cheapestCycle.getCost()) cheapestCycle = tmpCycle;
    }

    return cheapestCycle;
  }

  public List<L> nextLexicographicPermutation(List<L> list) {

    // Find the least significant digit which is less than the next least significant digit.
    int j = list.size() - 2;
    while (j >= 0) {
      if (list.get(j).compareTo(list.get(j + 1)) < 0) {
        break;
      }
      --j;
    }

    // If there is no digit satisfying the above constraint, then we have reached the largest
    // lexicographic permutation.
    if (j < 0) return null;

    // Find the smallest value greater than the value at j which succeeds j. Note, due to the way
    // we selected j, j+1 is a safe starting value.
    int k = j + 1;
    for (int i = j + 2; i < list.size(); ++i) {
      if (list.get(i).compareTo(list.get(k)) < 0 && list.get(j).compareTo(list.get(i)) <= 0) {
        k = i;
      }
    }

    // Swap the values at j and k, sorting all values after j.
    Collections.swap(list, j, k);
    List<L> res = list.subList(0, j+1);
    List<L> end = list.subList(j+1, list.size());
    Collections.sort(end);
    res.addAll(end);

    return res;
  }
}