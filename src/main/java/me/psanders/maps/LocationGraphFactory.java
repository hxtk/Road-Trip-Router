// Copyright (c) Peter Sanders. All rights reserved.
// Date: 2017-12-01

package me.psanders.maps;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;

import com.google.maps.errors.OverDailyLimitException;
import com.google.maps.errors.OverQueryLimitException;
import me.psanders.graph.MatrixGraph;
import org.apache.commons.cli.CommandLine;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.errors.ApiException;

public class LocationGraphFactory {

  private DistanceMatrixFactory factory;
  private CommandLine flags;

  public LocationGraphFactory(DistanceMatrixFactory factory, CommandLine flags) {
    this.factory = factory;
    this.flags = flags;
  }

  /** Render the request and flags into a Location Graph for the optimizer.
   *
   */
  public MatrixGraph<String, Long> build() {
    DistanceMatrix distMatrix = getMatrix();
    if (distMatrix == null) {
      return null;
    }

    // Generate the list of labels to look up matrix indices.
    HashMap<String, Integer> labels = new HashMap<String, Integer>();
    for (int i = 0; i < distMatrix.originAddresses.length; ++i) {
      labels.put(distMatrix.originAddresses[i], i);
    }

    // Convert the DistanceMatrix object to a 2D array for the MatrixGraph.
    Long[][] matrix =
        new Long[distMatrix.originAddresses.length][distMatrix.originAddresses.length];

    for (int i = 0; i < distMatrix.originAddresses.length; ++i) {
      for (int j = 0; j < distMatrix.originAddresses.length; ++j) {
        if (i == j) continue;  // Skip the diagonal axis.

        Long weight = getElementWeight(distMatrix.rows[i].elements[j]);
        if (weight == null) {
          System.out.println("We couldn't resolve the leg between \""
              + distMatrix.originAddresses[i] + "\" and \"" + distMatrix.destinationAddresses[j]
              + "\", so we're trying to route around it.");
          matrix[i][j] = Long.MAX_VALUE / 2;
        } else {
          matrix[i][j] = weight;
        }
      }
    }

    return new MatrixGraph<>(labels, matrix);
  }

  /** Retrieve the weight of a given element.
   *
   * <p>This will depend upon a variety of factors. First, we must consult element and ensure that
   * a route was found. If that connection was intractable then we return null. In the graph
   * encoding found in `build`, we handle these cases by using an arbitrarily large weight for
   * those connections, as our algorithm assumes a connected graph.
   *
   * <p>Second, we consult the flags. If the "fast" flag is present then we will score the leg
   * by time. Otherwise, we will score it by distance as the default setting. We choose this
   * default because road-trips are not expected to be rushed experiences, but distance will
   * influence the fuel spent on the trip.
   *
   */
  private Long getElementWeight(DistanceMatrixElement element) {
    switch (element.status) {
      case NOT_FOUND:
      case ZERO_RESULTS:
        // The algorithm will optimize away from this leg if the cost is arbitrarily high.
        return null;
      default:
        if (flags.hasOption("fast")) {
          return element.duration.inSeconds;
        } else {
          return element.distance.inMeters;
        }
    }
  }

  /** Get the distance matrix between our various locations, handling error cases.
   *
   * <p><code>Over*Limit</code> indicates that an account has made more requests in a time frame
   * than its billed rate allows, and we must wait for it to cool down.
   *
   * <p>Connect Exceptions indicate network failure.
   *
   * <p>Illegal state indicates that the request object was malformed. We do not expect this to
   * occur except if the key passed in is bad or absent.
   *
   */
  private DistanceMatrix getMatrix() {
    try {
      return factory.build();
    } catch (OverDailyLimitException | OverQueryLimitException e) {
      System.out.println("Your API Key has exceeded its quota. Please wait before trying again.");
      return null;
    } catch (ConnectException e) {
      System.out.println("Connection failed. Are you sure you're connected to the internet?");
      return null;
    } catch (IllegalStateException e) {
      System.out.println("Your Google Maps API key was invalid.");
      return null;
    } catch (ApiException | InterruptedException | IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}