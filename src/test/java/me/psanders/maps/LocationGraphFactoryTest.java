

package me.psanders.maps;

import java.io.IOException;
import java.net.ConnectException;

import com.google.maps.GeoApiContext;
import org.apache.commons.cli.CommandLine;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LocationGraphFactoryTest {

  /** The 
   *
   */
  @Test
  public void exitsCleanlyIfNoConnection() {
    LocationGraphFactory lgf = new LocationGraphFactory(
        new NoConnectionMatrixFactory(), null, null);

    try {
      Assert.assertNull(lgf.build());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }



  private class NoConnectionMatrixFactory extends DistanceMatrixFactory {

    public NoConnectionMatrixFactory() {
      super(null, null, null);
    }

    @Override
    public DistanceMatrix build()
        throws IllegalStateException, InterruptedException, ApiException, IOException {
      throw new ConnectException();
    }
  }
}