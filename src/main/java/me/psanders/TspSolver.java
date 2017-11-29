// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders;

import me.psanders.graph.path.Cycle;

import java.text.DecimalFormat;
import java.util.Iterator;

class TspSolver {

  public static void main(String[] args) {
    Cycle<String, Long> path = new ShortRouteFinder(args).getRoute();
    if (path == null) {
      System.out.println("An unexpected error has occurred and no route was found.");
      System.exit(2);
    }

    // Print all of the destinations in the order prescribed by the router
    for (Object place: path.getOrder()) {
      System.out.println(place + " ->");
    }

    // Print the first place again because this is a cycle, i.e., it returns to its starting point.
    System.out.println(path.getOrder().get(0));

    double cost = ((double) (path.getCost())) / 1609.344/*meters per mile*/;
    DecimalFormat df = new DecimalFormat("#.#");
    System.out.println("Total distance: " + df.format(cost) + "mi");
  }
}