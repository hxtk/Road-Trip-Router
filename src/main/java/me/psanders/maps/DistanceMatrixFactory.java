

package me.psanders.maps;


import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;

public class DistanceMatrixFactory {

  private GeoApiContext context;
  private String[] places;
  private CommandLine flags;

  public DistanceMatrixFactory(GeoApiContext context, String[] places, CommandLine flags) {
    this.context = context;
    this.places = places;
    this.flags = flags;
  }

  /** Get the distance matrix containing the distance between each pair of places passed in.
   *
   * <p>Flags are used to control the API Request. For more details, see `getOptions`.
   *
   */
  public DistanceMatrix build()
      throws IllegalStateException, InterruptedException, ApiException, IOException {
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
}