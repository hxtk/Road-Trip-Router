

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
    Assert.assertTrue(perm.hasNext());
    Assert.assertEquals(perm.next(), end);
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

    Assert.assertFalse(new LexicographicPermuter<Integer>(list).iterator().hasNext());
  }
}