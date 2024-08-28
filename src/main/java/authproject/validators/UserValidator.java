package authproject.validators;

public class UserValidator {
  private static String usernameRegex = "^[a-zA-Z][a-zA-Z0-9_-]{2,10}$";
  private static String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\\$%\\^&\\*]).{8,}$";
  private static String emailRegex = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$";

  public UserValidator() {

  }

  public static boolean usernameIsValid(String username) {
    return username.matches(usernameRegex);
  }

  public static boolean passwordIsValid(String password) {
    return password.matches(passwordRegex);
  }

  public static boolean emailIsValid(String email) {
    return email.matches(emailRegex);
  }
}
