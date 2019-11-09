package productionapp;

/**
 * Interface for defining what a screen should be able to do.
 */
public interface ScreenSpec {

  public String getString();

  public int getRefreshRate();

  public int getResponseTime();
}
