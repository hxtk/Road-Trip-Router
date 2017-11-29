// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

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

  public Cycle getRoute() {
    Options options = new Options();
    options.addOption("k", "key", true, "Google Maps Services API Key");
    options.addOption("h", "help", false, "Print this usage message");

    // Commented out by hxtk (2017-10-18) pending addition of new features
    //options.addOption("mode", true, "Transportation mode (uses Google default)");
    //options.addOption("no-fee", false, "Selects route that avoids toll roads and ferries");
    //options.addOption("scenic", false, "Selects route that avoids highways and toll roads");

    try {
      CommandLine flags = new GnuParser().parse(options, args);
      String[] places = flags.getArgs();

      // Print help if they asked for it
      if (flags.hasOption("help")) {
        new HelpFormatter().printHelp("Road Trip Router",
            "Find a (nearly) optimal route for your next road trip", options,
            "Please report any bugs or comments to https://github.com/hxtk/Road-Trip-Router",
            true);
        System.exit(0);
      } else if (!flags.hasOption("key") || places.length < 2) {
        new HelpFormatter().printHelp("RoutePlanner",
            "Find a (nearly) optimal route for your next road trip", options,
            "Please report any bugs or comments to https://github.com/hxtk/Road-Trip-Router",
            true);
        System.exit(1);
      }

      GeoApiContext context = new GeoApiContext.Builder().apiKey(flags.getOptionValue("key")).build();
      DistanceMatrixApiRequest req = DistanceMatrixApi.getDistanceMatrix(
          context, places, places);

      DistanceMatrix distMatrix = req.await();
      HashMap<String, Integer> labels = new HashMap<String, Integer>();

      // Convert the DistanceMatrix object to a 2D array for the MatrixGraph.
      Long[][] matrix = new Long[places.length][places.length];
      for (int i = 0; i < places.length; ++i) {
        labels.put(places[i], i);
        for (int j = 0; j < places.length; ++j) {
          if (i == j) continue;  // Skip the diagonal axis.

          DistanceMatrixElement element = distMatrix.rows[i].elements[j];
          switch (element.status) {
            case NOT_FOUND:
              System.out.println("We couldn't resolve \"" + places[i]
                  + "\" so we're trying to route around it...");
              // Fallthrough
            case ZERO_RESULTS:
              // The algorithm will optimize away from this leg if the cost is arbitrarily high.
              matrix[i][j] = matrix[j][i] = Long.MAX_VALUE / 2;
              break;
            default:
              matrix[i][j] = element.distance.inMeters;
          }
        }
      }

      HCycleFinder finder = new HCycleFinder(
          new MatrixGraph<String, Long>(labels, matrix),
          new GeneticOptimizationStrategy<String, Long>(new Random()));

      return finder.getOptimalCycle();
    } catch (ParseException e) {
      // TODO(hxtk): Exit gracefully on exception.
      e.printStackTrace();
    } catch (ApiException | InterruptedException | IOException e) {
      // TODO(hxtk): Exit gracefully on exception.
      e.printStackTrace();
    }

    return null;
  }
}