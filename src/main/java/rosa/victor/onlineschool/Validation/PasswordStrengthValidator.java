package rosa.victor.onlineschool.Validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import rosa.victor.onlineschool.annotation.PasswordValidator;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordValidator, String>{

  List<String> weakPasswords;

  @Override
  public void initialize(PasswordValidator passwordValidator) {
    weakPasswords = Arrays.asList("12345", "password", "qwert");
  }

  @Override
  public boolean isValid(String passwordFiled, ConstraintValidatorContext cxt) {
    return passwordFiled != null && (!weakPasswords.contains(passwordFiled));
  }
  
}
