// Copyright: Peter Sanders. All rights reserved.
// Date: 2017-11-29

package me.psanders;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.UnrecognizedOptionException;

import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.errors.ApiException;

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
    Options options = getOptions();

    try {
      CommandLine flags = new GnuParser().parse(options, args);

      // Print help if the user asked for it or if we cannot proceed.
      if (flags.hasOption("help")) {
        usage(options);
        System.exit(0);
      }

      String[] places = getPlaces(flags);
      if (places.length < 2) {
        System.out.println("You must provide at least two locations.");
        usage(options);
        System.exit(1);
      }

      // Retrieve the matrix of distances between each pair of places passed in to args.
      DistanceMatrix distMatrix = getDistMatrix(flags, places);

      // Generate the list of labels to look up matrix indices.
      HashMap<String, Integer> labels = new HashMap<String, Integer>();
      for (int i = 0; i < places.length; ++i) {
        labels.put(places[i], i);
      }

      // Convert the DistanceMatrix object to a 2D array for the MatrixGraph.
      Long[][] matrix = new Long[places.length][places.length];
      for (int i = 0; i < places.length; ++i) {
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
              if (flags.hasOption("fast")) {
                matrix[i][j] = element.duration.inSeconds;
              } else {
                matrix[i][j] = element.distance.inMeters;
              }
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
    } catch (IllegalStateException | MissingOptionException e) {
      System.out.println("You must include a valid Google Maps Services API key\n");
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

  /** Get the distance matrix containing the distance between each pair of places passed in.
   *
   * <p>Flags are used to control the API Request. For more details, see `getOptions`.
   *
   */
  private DistanceMatrix getDistMatrix(CommandLine flags, String[] places)
      throws IllegalStateException, InterruptedException, ApiException, IOException {
    GeoApiContext context = new GeoApiContext.Builder().
        apiKey(flags.getOptionValue("key")).build();

    DistanceMatrixApiRequest request = DistanceMatrixApi.
        getDistanceMatrix(context, places, places);

    if (flags.hasOption("scenic")) {
      request = request.avoid(DirectionsApi.RouteRestriction.HIGHWAYS)
          .avoid(DirectionsApi.RouteRestriction.TOLLS);
    }
    if (flags.hasOption("no-fee")) {
      request = request.avoid(DirectionsApi.RouteRestriction.TOLLS)
          .avoid(DirectionsApi.RouteRestriction.FERRIES);
    }
    if (flags.hasOption("mode")) {
      switch (flags.getOptionValue("mode")) {
        case "driving":
          request = request.mode(TravelMode.DRIVING);
          break;
        case "transit":
          request = request.mode(TravelMode.TRANSIT);
          break;
        case "bicycling":
          request = request.mode(TravelMode.BICYCLING);
          break;
        case "walking":
          request = request.mode(TravelMode.WALKING);
          break;
        default:
          System.out.println("Invalid transportation mode. Options include:");
          System.out.println("    driving, transit, bicycling, walking");
      }
    }

    return request.await();
  }

  /** Produces a parser that will process the arguments below:
   *
   * <code>
   * -F,--no-fee        Selects route that avoids toll roads and ferries.
   * -f,--file <FILE>   Reads newline-delimited locations from a file.
   * --fast             Selects route with shortest time.
   *                    (Default is shortest distance)
   * -h,--help          Print this usage message.
   * -k,--key <KEY>     Google Maps Services API Key.
   * -M,--mode <arg>    Transportation mode (uses Google default)
   *                    Options: "driving" "transit" "bicycling" "walking"
   * -s,--scenic        Selects route that avoids highways and toll roads.
   * </code>
   */
  private Options getOptions() {
    Options options = new Options();
    options.addOption(
        OptionBuilder
            .withLongOpt("key")
            .hasArg().withArgName("KEY")
            .withDescription("Google Maps Services API Key (Required).")
            .isRequired()
            .create("k")
    );
    options.addOption("h", "help", false, "Print this usage message.");

    OptionGroup modifiers = new OptionGroup();
    modifiers.addOption(
        OptionBuilder
            .withLongOpt("mode")
            .hasArg().withArgName("MODE")
            .withDescription("Transportation mode (uses Google default).\n"
                + "Options: \"driving\" \"transit\" \"bicycling\" \"walking\"")
            .create("m")
    );
    modifiers.addOption(
        OptionBuilder
            .withLongOpt("no-fee")
            .withDescription("Selects route that avoids toll roads and ferries.")
            .create("F")
    );
    modifiers.addOption(
        OptionBuilder
            .withLongOpt("scenic")
            .withDescription("Selects route that avoids highways and toll roads.")
            .create("s")
    );
    modifiers.addOption(
        OptionBuilder
            .withLongOpt("fast")
            .withDescription("Selects route with shortest time.\n(Default is shortest distance.)")
            .create()
    );
    options.addOptionGroup(modifiers);

    // Commented out by hxtk (2017-11-29) pending logic for file IO in `ShortRouteFinder.getPlaces`.
    //options.addOption(
    //   OptionBuilder
    //        .withLongOpt("file")
    //        .hasArg().withArgName("FILE")
    //        .withDescription("Reads newline-delimited locations from a file.")
    //        .create("f")
    //);
    return options;
  }

  /** Return a list of locations among which we will find our cycle.
   *
   * <p>The flags provided will change the behavior of this method. If there is a "file" flag,
   * we will attempt to open the file named as that flag's argument and read newline-delimited
   * locations from that file. Otherwise, any unconsumed arguments are assumed to be locations.
   */
  private String[] getPlaces(CommandLine flags) {
    if (flags.hasOption("file")) {
      // TODO(hxtk): Handle file input.
      return new String[0];
    } else {
      return flags.getArgs();
    }
  }

  /** Print the usage message for the given options.
   *
   * For more information, see the documentation for HelpFormatter:
   *     https://commons.apache.org/proper/commons-cli/usage.html#UsageHelp
   *     https://commons.apache.org/proper/commons-cli/javadocs/api-release/org/apache/commons/cli/HelpFormatter.html
   */
  private void usage(Options options) {
    new HelpFormatter().printHelp("RoutePlanner",
        "Find a (nearly) optimal route for your next road trip.\n\n", options,
        "\n\nPlease report any bugs or comments to "
        +"https://github.com/hxtk/Road-Trip-Router/issues",
        true);
  }
}
