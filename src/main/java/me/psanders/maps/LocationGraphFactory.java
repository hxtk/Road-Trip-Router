// Copyright (c) Peter Sanders. All rights reserved.
// Date: 2017-12-01

package me.psanders.maps;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;

import me.psanders.graph.MatrixGraph;
import org.apache.commons.cli.CommandLine;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.errors.ApiException;

public class LocationGraphFactory {

  private DistanceMatrixFactory factory;
  private String[] places;
  private CommandLine flags;

  public LocationGraphFactory(DistanceMatrixFactory factory, String[] places, CommandLine flags) {
    this.factory = factory;
    this.places = places;
    this.flags = flags;
  }

  public MatrixGraph<String, Long> build() throws InterruptedException, ApiException, IOException {
    DistanceMatrix distMatrix;
    try {
      distMatrix = factory.build();
    } catch (ConnectException e) {
      System.out.println("Connection failed. Are you sure you're connected to the internet?");
      return null;
    }

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

    return new MatrixGraph<>(labels, matrix);
  }
}