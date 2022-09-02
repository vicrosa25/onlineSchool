package rosa.victor.onlineschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rosa.victor.onlineschool.model.OnlineClass;

@Repository
public interface OnlineClassRepository extends JpaRepository<OnlineClass, Integer>{

}
