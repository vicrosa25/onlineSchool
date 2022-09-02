package rosa.victor.onlineschool.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rosa.victor.onlineschool.annotation.FieldsValueMatch;
import rosa.victor.onlineschool.annotation.PasswordValidator;


@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@FieldsValueMatch.List({
  @FieldsValueMatch(field = "pwd", fieldMatch = "confirmPwd", message = "Password do not match"),
  @FieldsValueMatch(field = "email", fieldMatch = "confirmEmail", message = "Email do not match")
})
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
  @GenericGenerator(name = "native",strategy = "native")
  private int userId;

  @NotBlank(message="Name must not be blank")
  @Size(min=3, message="Name must be at least 3 characters long")
  private String name;

  @NotBlank(message="Mobile number must not be blank")
  @Pattern(regexp="(^$|[0-9]{9})",message = "Mobile number must be 9 digits")
  private String mobileNumber;

  @NotBlank(message="Email must not be blank")
  @Email(message = "Please provide a valid email address" )
  private String email;

  @NotBlank(message="Confirm Email must not be blank")
  @Email(message = "Please provide a valid confirm email address" )
  @Transient
  private String confirmEmail;

  @NotBlank(message="Password must not be blank")
  @Size(min=5, message="Password must be at least 5 characters long")
  @PasswordValidator
  private String pwd;

  @NotBlank(message="Confirm Password must not be blank")
  @Size(min=5, message="Confirm Password must be at least 5 characters long")
  @Transient
  private String confirmPwd;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "role_id", referencedColumnName = "roleId", nullable = false)
  private Role role;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id", referencedColumnName = "addressId", nullable = true)
  private Address address;

  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "class_id", referencedColumnName = "classId", nullable = true)
  private OnlineClass onlineClass;

}
