// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-11-29

package me.psanders.utils;

import java.util.ArrayList;
import java.util.Collections;
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
 * <p>NOTE: The first permutation returned will always be the permutation passed in.
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

    /** Return whether the last permutation has already been given.
     *
     * <p>When a larger permutation does not exist, the new permutation is set to null. Thus, this
     * is false only when items is null.
     *
     */
    @Override
    public boolean hasNext() {
      return items != null;
    }

    /** Return the next largest permutation of a items if it exists, otherwise null.
     *
     * <p>We return the current ordering and update to the least reordering of the elements which is
     * greater than the current ordering. Such an ordering exists so long as the elements are not
     * sorted in non-increasing order.
     *
     * <p>If the elements are sorted in non-increasing order, there is no valid permutation greater
     * than the current permutation, so we update to null.
     *
     */
    @Override
    public List<T> next() {
      List<T> old = new ArrayList(items);

      // Find the least significant digit which is less than its successor.
      int j = items.size() - 2;  // Begin at the second to last element, since the last element has
                                 // no successor.
      while (j >= 0) {
        if (items.get(j).compareTo(items.get(j + 1)) < 0) {
          break;
        }
        --j;
      }

      // If there is no digit satisfying the above constraint, then we have reached the largest
      // lexicographic permutation.
      if (j < 0) {
        items = null;
        return old;
      }

      // Find the smallest value greater than the value at j which succeeds j. Note, due to the way
      // we selected j, j+1 is a safe starting value.
      int k = j + 1;
      for (int i = j + 2; i < items.size(); ++i) {
        T atI = items.get(i);
        T atJ = items.get(j);
        T atK = items.get(k);
        if (atI.compareTo(atK) < 0 && atI.compareTo(atJ) > 0) {
          k = i;
        }
      }

      // Swap the values at j and k, sorting all values after j.
      Collections.swap(items, j, k);
      Collections.sort(items.subList(j+1, items.size()));

      return old;
    }
  }
}

