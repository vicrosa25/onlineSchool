package rosa.victor.onlineschool.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rosa.victor.onlineschool.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
  User getByEmail(String email);
}
