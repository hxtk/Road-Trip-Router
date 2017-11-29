// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-11-29

package me.psanders.graph;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import me.psanders.graph.Graph;
import me.psanders.graph.MatrixGraph;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GraphTest {

  @Test
  public void matrixGraphIsFaithful() {
    testFidelity(new TestGraphFactory().getMatrixGraph());
  }

  @Test
  public void linkedGraphIsFaithful() {
    testFidelity(new TestGraphFactory().getLinkedGraph());
  }

  // If this fails then our insertion functions do not match our retrieval functions
  // i.e., `Graph.getWeight(X,Y)` searches
  private void testFidelity(Graph graph) {
    assertNull(graph.getWeight("a", "a"));
    assertNull(graph.getWeight("a", "d"));
    assertNull(graph.getWeight("b", "b"));
    assertNull(graph.getWeight("c", "a"));
    assertNull(graph.getWeight("c", "c"));
    assertNull(graph.getWeight("d", "b"));
    assertNull(graph.getWeight("d", "c"));
    assertNull(graph.getWeight("d", "d"));

    assertEquals(2, graph.getWeight("a", "b"));
    assertEquals(3, graph.getWeight("a", "c"));
    assertEquals(1, graph.getWeight("b", "a"));
    assertEquals(2, graph.getWeight("b", "c"));
    assertEquals(3, graph.getWeight("b", "d"));
    assertEquals(3, graph.getWeight("c", "b"));
    assertEquals(4, graph.getWeight("c", "d"));
    assertEquals(1, graph.getWeight("d", "a"));
  }

}