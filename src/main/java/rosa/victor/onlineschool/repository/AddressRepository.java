package rosa.victor.onlineschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rosa.victor.onlineschool.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
  
}
