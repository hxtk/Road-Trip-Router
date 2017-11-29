// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders;

import me.psanders.graph.path.Path;

class TspSolver {

  public static void main(String[] args) throws Exception {
    Path path = new ShortRouteFinder(args).getRoute();
    System.out.println(path);
  }
}