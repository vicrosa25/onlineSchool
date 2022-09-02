package rosa.victor.onlineschool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;



import rosa.victor.onlineschool.Validation.FieldsValueMatchValidator;

@Constraint(validatedBy = FieldsValueMatchValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsValueMatch {

  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  String message() default "Fields values don't match!";

  String field();

  String fieldMatch();

  @Target({ ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @interface List {
    FieldsValueMatch[] value();
  }
  
}
