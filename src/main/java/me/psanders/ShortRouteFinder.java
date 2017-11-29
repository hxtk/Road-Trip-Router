// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders;

import java.util.HashMap;
import java.util.Random;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;

import com.google.maps.GeoApiContext;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.model.DistanceMatrix;

import me.psanders.graph.path.Cycle;
import me.psanders.graph.path.HCycleFinder;
import me.psanders.graph.path.GeneticOptimizationStrategy;
import me.psanders.graph.MatrixGraph;

public class ShortRouteFinder {

  private String[] args;

  public ShortRouteFinder(String[] args) {
    this.args = args;
  }

  public Cycle getRoute() throws ParseException {
    Options options = new Options();
    options.addOption("k", "key", true, "Google Maps Services API Key");

    // Commented out by hxtk (2017-10-18) pending addition of new features
    //options.addOption("mode", true, "Transportation mode (uses Google default)");
    //options.addOption("no-fee", false, "Selects route that avoids toll roads and ferries");
    //options.addOption("scenic", false, "Selects route that avoids highways and toll roads");

    CommandLine flags = new GnuParser().parse(options, args);
    String[] places = flags.getArgs();

    GeoApiContext context = new GeoApiContext.Builder().apiKey(flags.getOptionValue("key")).build();
    DistanceMatrixApiRequest req = DistanceMatrixApi.getDistanceMatrix(
        context, places, places);

    try {
      DistanceMatrix distMatrix = req.await();
      HashMap<String, Integer> labels = new HashMap<String, Integer>();

      // Convert the DistanceMatrix object to a 2D array for the MatrixGraph.
      Long[][] matrix = new Long[places.length][places.length];
      for (int i = 0; i < places.length; ++i) {
        labels.put(places[i], i);
        for (int j = 0; j < places.length; ++j) {
          matrix[i][j] = distMatrix.rows[i].elements[j].distance.inMeters;
        }
      }

      HCycleFinder finder = new HCycleFinder(
          new MatrixGraph<String, Long>(labels, matrix),
          new GeneticOptimizationStrategy<String, Long>(new Random()));

      return finder.getOptimalCycle();
    } catch (Exception e) {
      // TODO(hxtk): Exit gracefully on exception.
      e.printStackTrace();
    }

    return null;
  }
}