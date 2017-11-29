// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-11-29

package me.psanders.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LexicographicPermuterTest {

  @Test
  public void getsSecondPermutation() {
    ArrayList<Integer> list = new ArrayList();
    list.addAll(Arrays.asList(1, 2, 3, 4));

    ArrayList<Integer> end = new ArrayList();
    end.addAll(Arrays.asList(1, 2, 4, 3));

    Iterator<List<Integer>> perm = new LexicographicPermuter<Integer>(list).iterator();

    // Consume initial ordering
    perm.next();

    Assert.assertEquals(end, perm.next());
  }

  @Test
  public void sortsAfterFirstChangedValue() {
    ArrayList<Integer> list = new ArrayList();
    list.addAll(Arrays.asList(1, 2, 4, 3));

    ArrayList<Integer> end = new ArrayList();
    end.addAll(Arrays.asList(1, 3, 2, 4));

    Iterator<List<Integer>> perm = new LexicographicPermuter<Integer>(list).iterator();

    // Consume initial ordering
    perm.next();

    Assert.assertEquals(end, perm.next());
  }

  @Test
  public void findsCorrectQuantityOfPermutations() {
    ArrayList<Integer> list = new ArrayList();
    list.addAll(Arrays.asList(0, 1, 2, 3));

    int count = 0;
    for (List<Integer> it: new LexicographicPermuter<Integer>(list)){
      count++;
    }

    Assert.assertEquals(/*factorial(4) = */24, count);
  }

  @Test
  public void stopsAtEnd() {
    ArrayList<Integer> list = new ArrayList();
    list.addAll(Arrays.asList(3, 2, 1, 0));

    Iterator<List<Integer>> it = new LexicographicPermuter<Integer>(list).iterator();

    // Consume the initial item: We can always return at least one item because the first item
    // is the permutation passed in.
    it.next();

    Assert.assertFalse(it.hasNext());
  }
}