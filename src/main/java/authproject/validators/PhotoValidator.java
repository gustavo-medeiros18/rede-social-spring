package authproject.validators;

public class PhotoValidator {
  private static String urlRegex = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";

  public static boolean urlIsValid(String url) {
    return url.matches(urlRegex);
  }

  public static boolean descriptionIsValid(String description) {
    return description != null && !description.isEmpty();
  }

  public static boolean userIdIsValid(Long userId) {
    return userId != null;
  }
}