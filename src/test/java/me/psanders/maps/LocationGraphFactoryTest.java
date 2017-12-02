

package me.psanders.maps;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

import com.google.maps.GeoApiContext;
import com.google.maps.errors.OverDailyLimitException;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LocationGraphFactoryTest {

  /** The program should catch the error if there is no internet connection, returning null.
   *
   * <p>Null return values are how we signal a problem cleanly, and at each step that can produce
   * such an error we have checks that propagate up to <code>main</code> for program exit.
   *
   */
  @Test
  public void exitsCleanlyIfNoConnection() {
    LocationGraphFactory lgf = new LocationGraphFactory(
        new NoConnectionMatrixFactory(), null);

    try {
      Assert.assertNull(lgf.build());
    } catch (Exception e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
  }

  /** The program should catch the error if the API key supplied is invalid for some reason.
   *
   */
  @Test
  public void exitsCleanlyIfBadKey() {
    String[] args = new String[0];

    try {
    CommandLine flags = new GnuParser().parse(new Options(), args);
    LocationGraphFactory lgf = new LocationGraphFactory(
        new DistanceMatrixFactory(new GeoApiContext.Builder().build(), args, flags), flags);


      Assert.assertNull(lgf.build());
    } catch (Exception e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
  }

  /** The program should catch the error if the API Key supplied is valid but over its quota.
   *
   */
  @Test
  public void exitsCleanlyIfQueryLimitExceeded() {
    LocationGraphFactory lgf = new LocationGraphFactory(
        new OverLimitMatrixFactory(), null);

    try {
      Assert.assertNull(lgf.build());
    } catch (Exception e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
  }

  /** This class simulates a failure of internet connection.
   *
   */
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

  /** This class simulates an account that has exceeded its quota.
   *
   */
  private class OverLimitMatrixFactory extends DistanceMatrixFactory {
    public OverLimitMatrixFactory() {
      super(null, null, null);
    }

    @Override
    public DistanceMatrix build()
        throws IllegalStateException, InterruptedException, ApiException, IOException {
      throw new OverDailyLimitException("This account has exceeded its daily limit");
    }
  }
}