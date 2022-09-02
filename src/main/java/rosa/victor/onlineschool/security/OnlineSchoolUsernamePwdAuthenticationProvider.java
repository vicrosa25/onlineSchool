package rosa.victor.onlineschool.security;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import rosa.victor.onlineschool.model.Role;
import rosa.victor.onlineschool.model.User;
import rosa.victor.onlineschool.repository.UserRepository;

@Component
public class OnlineSchoolUsernamePwdAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = authentication.getName();
    String pwd = authentication.getCredentials().toString();
    User user = userRepository.getByEmail(email);

    if (user != null && user.getUserId() > 0 && passwordEncoder.matches(pwd, user.getPwd())) {
      return new UsernamePasswordAuthenticationToken(email,
                                                     null,
                                                     getGrantedAuthorities(user.getRole()));
    } else {
      throw new BadCredentialsException("Invalid Credentials");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

  private List<GrantedAuthority> getGrantedAuthorities(Role role) {
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
    return grantedAuthorities;
  }


}
