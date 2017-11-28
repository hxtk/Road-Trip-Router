// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-11-28

package me.psanders.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/** Iterator over the lexicographic permutations of a list.
 *
 * <p>Take in a items of elements which have a natural ordering. Suppose that the elements are
 * in descending order of significance, i.e., the least value of one element is greater than the
 * greatest possible value of its successor (e.g., the digits of numbers, 10 is greater than 09).
 *
 * <p>For example, the permutations of <code>[1, 2, 3, 4]</code> in order are
 * <code>[[1, 2, 3, 4], [1, 2, 4, 3], [1, 3, 2, 4], [1, 3, 4, 2], ...]</code>
 *
 */
public class LexicographicPermuter<T extends Comparable<T>> implements Iterable<List<T>> {
  
  private List<T> items;
  
  public LexicographicPermuter(List<T> items) {
    this.items = items;
  }

  @Override
  public Iterator<List<T>> iterator() {
    return new PermutationIterator();
  }

  private class PermutationIterator implements Iterator<List<T>> {

    /** Return whether a larger permutation of the data is defined.
     *
     * <p>This is equivalent to the question "Is this list sorted in non-increasing order?" because
     * in that case there is no higher value to promote to a more significant position.
     *
     */
    @Override
    public boolean hasNext() {
      Comparator<T> cmp = Collections.reverseOrder();
      for (int i = 1; i < items.size(); ++i) {
        if (cmp.compare(items.get(i - 1), items.get(i)) > 0) {
          return true;
        }
      }
      return false;
    }

    /** Return the next largest permutation of a items if it exists, otherwise null.
     *
     * <p>We return the least reordering of the elements which is greater than the current ordering.
     * Such an ordering exists so long as the elements are not sorted in non-increasing order.
     *
     * <p>If the elements are sorted in non-increasing order, there is no valid permutation greater
     * than the current permutation, so we return null.
     *
     */
    @Override
    public List<T> next() {
      // Find the least significant digit which is less than the next least significant digit.
      int j = items.size() - 2;
      while (j >= 0) {
        if (items.get(j).compareTo(items.get(j + 1)) < 0) {
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
      for (int i = j + 2; i < items.size(); ++i) {
        if (items.get(i).compareTo(items.get(k)) < 0 && items.get(j).compareTo(items.get(i)) <= 0) {
          k = i;
        }
      }

      // Swap the values at j and k, sorting all values after j.
      Collections.swap(items, j, k);
      List<T> res = items.subList(0, j+1);
      List<T> end = items.subList(j+1, items.size());
      Collections.sort(end);
      res.addAll(end);

      items = res;

      return items;
    }
  }
}

