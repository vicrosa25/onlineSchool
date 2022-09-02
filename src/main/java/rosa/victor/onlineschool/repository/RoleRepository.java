package rosa.victor.onlineschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rosa.victor.onlineschool.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
  
  Role getByRoleName(String roleName);
}
