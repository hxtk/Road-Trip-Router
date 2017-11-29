// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-10-11

package me.psanders;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.UnrecognizedOptionException;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.GeoApiContext;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.model.DistanceMatrix;

import me.psanders.graph.path.Cycle;
import me.psanders.graph.path.HCycleFinder;
import me.psanders.graph.path.GeneticOptimizationStrategy;
import me.psanders.graph.MatrixGraph;

/** Factory class for Cycle based on a list of geographic locations.
 *
 * <p>We pass in a list of arguments that are used to create a Google Maps
 * API instance, look up the set of places from that list, and optimize a
 * route between those places using a Genetic Algorithm.
 *
 * <p>The arguments are designed to be formatted as arguments coming in from
 * the command line, i.e., the <code>args</code> passed to a program entry.
 *
 * <p>Distinct places are delimited by the distinct elements of the array.
 * Consult your command line's documentation for more information. Usually
 * you will group using quotes ('"') or escaped spaces ('\ ').
 */
public class ShortRouteFinder {

  private String[] args;

  public ShortRouteFinder(String[] args) {
    this.args = args;
  }

  /** Parse command line arguments and deliver a route between the points listed.
   *
   */
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
        usage(options);
        System.exit(0);
      } else if (!flags.hasOption("key") || places.length < 2) {
        System.out.println("Please give at least two destinations.");
        usage(options);
        System.exit(1);
      }

      // Retrieve the matrix of distances between each pair of places passed in to args.
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
                  + "\" so we're trying to route around it.");
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

      // Find the optimal route.
      return new HCycleFinder(
          new MatrixGraph<>(labels, matrix),
          new GeneticOptimizationStrategy<String, Long>(new Random())
      ).getOptimalCycle();

    // Error handling
    } catch (UnrecognizedOptionException e) {
      // This will be triggered if a user passes in a flag that is not in our list.
      System.out.println("Option \"" + e.getOption() + "\" Not found. See usage:");
      usage(options);
      System.exit(1);
    } catch (ParseException e) {
      // TODO(hxtk): Exit gracefully on exception.
      e.printStackTrace();
    } catch (ApiException | InterruptedException | IOException e) {
      // TODO(hxtk): Exit gracefully on exception.
      e.printStackTrace();
    }

    return null;
  }

  /** Print the usage message for the given options.
   *
   * For more information, see the documentation for HelpFormatter:
   *     https://commons.apache.org/proper/commons-cli/usage.html#UsageHelp
   *     https://commons.apache.org/proper/commons-cli/javadocs/api-release/org/apache/commons/cli/HelpFormatter.html
   */
  private void usage(Options options) {
    new HelpFormatter().printHelp("RoutePlanner",
        "Find a (nearly) optimal route for your next road trip.", options,
        "Please report any bugs or comments to https://github.com/hxtk/Road-Trip-Router",
        true);
  }
}
