// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders;

import me.psanders.graph.path.Path;

class TspSolver {

  public static void main(String[] args) {
    Path path = new ShortRouteFinder(args).getRoute();
    if (path == null) {
      System.out.println("An unexpected error has occurred and no route was found.");
      System.exit(2);
    }

    System.out.println(path);
  }
}