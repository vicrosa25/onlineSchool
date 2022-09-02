package rosa.victor.onlineschool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rosa.victor.onlineschool.constants.OnlineSchoolConstants;
import rosa.victor.onlineschool.model.Role;
import rosa.victor.onlineschool.model.User;
import rosa.victor.onlineschool.repository.RoleRepository;
import rosa.victor.onlineschool.repository.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public boolean createNewUser(User user) {
    boolean isSaved = false;
    Role role = roleRepository.getByRoleName(OnlineSchoolConstants.STUDENT_ROLE);
    user.setRole(role);

    user.setPwd(passwordEncoder.encode(user.getPwd()));

    user = userRepository.save(user);

    if(user != null && user.getUserId() > 0) {
      isSaved = true;
    }

    return isSaved;
  }

}
