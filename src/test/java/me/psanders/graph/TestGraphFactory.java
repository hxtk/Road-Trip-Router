

package me.psanders.graph;

import java.util.HashMap;
import java.util.HashSet;

class TestGraphFactory {

  Graph<String, Integer> getLinkedGraph() {
    HashMap<String, HashMap<String, Integer>> adjacency = new HashMap();

    HashMap<String, Integer> a = new HashMap();
    a.put("b", 2);
    a.put("c", 3);

    HashMap<String, Integer> b = new HashMap();
    b.put("a", 1);
    b.put("c", 2);
    b.put("d", 3);

    HashMap<String, Integer> c = new HashMap();
    c.put("b", 3);
    c.put("d", 4);

    HashMap<String, Integer> d = new HashMap();
    d.put("a", 1);

    adjacency.put("a", a);
    adjacency.put("b", b);
    adjacency.put("c", c);
    adjacency.put("d", d);

    return new LinkedGraph(adjacency);
  }

  Graph<String, Integer> getMatrixGraph() {
    HashMap<String, Integer> vertices = new HashMap();

    vertices.put("a", 0);
    vertices.put("b", 1);
    vertices.put("b", 2);
    vertices.put("d", 3);

    Integer[][] adjMatrix = {
        {null, 2, 3, null},
        {1, null, 2, 3},
        {null, 3, null, 4},
        {1, null, null, null}
    };
    return new MatrixGraph(vertices, adjMatrix);
  }
}