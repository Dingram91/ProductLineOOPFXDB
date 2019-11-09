package productionapp;

public class MoviePlayer extends Product implements MultimediaControl {

  Screen screen;
  MonitorType monitorType;

  /**
   * Movie player class that allows for content to be played, stopped, skipped to the next track or
   * skipped back to the previous track.
   *
   * @param name         Name of the product.
   * @param manufacturer Name of the manufacturer.
   * @param screen       The type of screen to be used.
   * @param monitorType  The type of monitor to be used.
   */
  public MoviePlayer(String name, String manufacturer, Screen screen,
      MonitorType monitorType) {
    super(name, manufacturer, ItemType.VISUAL);
    this.screen = screen;
    this.monitorType = monitorType;
  }

  @Override
  public void play() {
    System.out.println("Playing");
  }

  @Override
  public void stop() {
    System.out.println("Stopping");
  }

  @Override
  public void previous() {
    System.out.println("Previous");
  }

  @Override
  public void next() {
    System.out.println("Next");
  }

  @Override
  public String toString() {
    return super.toString() + "Monitor Type: " + monitorType + "\nScreen Type: " + screen;
  }
}