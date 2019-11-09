package productionapp;

/**
 * Screen class that represents a screen that can be produced.
 */
public class Screen implements ScreenSpec {

  private String resolution;
  private int refreshRate;
  private int responseTime;

  /**
   * Constructor for the screen class.
   *
   * @param resolution   The resolution of the screen.
   * @param refreshRate  The refresh rate of the screen.
   * @param responseTime The response time of the screen in Ms.
   */
  public Screen(String resolution, int refreshRate, int responseTime) {
    this.resolution = resolution;
    this.refreshRate = refreshRate;
    this.responseTime = responseTime;
  }

  @Override
  public String getString() {
    return resolution;
  }

  @Override
  public int getRefreshRate() {
    return refreshRate;
  }

  @Override
  public int getResponseTime() {
    return responseTime;
  }
}
