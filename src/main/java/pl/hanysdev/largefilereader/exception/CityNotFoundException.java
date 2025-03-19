package pl.hanysdev.largefilereader.exception;

public class CityNotFoundException extends RuntimeException {
  public CityNotFoundException(String city) {
    super("City '" + city + "' not found");
  }
}
