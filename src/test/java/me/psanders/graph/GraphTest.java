// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-13

package me.psanders.graph;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import me.psanders.graph.Graph;
import me.psanders.graph.MatrixGraph;

public class GraphTest {

  @Test
  public void matrixGraphIsFaithful() {
    testFidelity(new TestGraphFactory().getMatrixGraph());
  }

  @Test
  public void linkedGraphIsFaithful() {
    testFidelity(new TestGraphFactory().getLinkedGraph());
  }

  private void testFidelity(Graph graph) {
    assertNull(graph.getWeight(0, 0));
    assertNull(graph.getWeight(0, 3));
    assertNull(graph.getWeight(1, 1));
    assertNull(graph.getWeight(2, 0));
    assertNull(graph.getWeight(2, 2));
    assertNull(graph.getWeight(3, 1));
    assertNull(graph.getWeight(3, 2));
    assertNull(graph.getWeight(3, 3));

    assertEquals(graph.getWeight(0, 1), 2);
    assertEquals(graph.getWeight(0, 2), 3);
    assertEquals(graph.getWeight(1, 0), 1);
    assertEquals(graph.getWeight(1, 2), 2);
    assertEquals(graph.getWeight(1, 3), 3);
    assertEquals(graph.getWeight(2, 1), 3);
    assertEquals(graph.getWeight(2, 3), 4);
    assertEquals(graph.getWeight(3, 0), 1);
  }

}