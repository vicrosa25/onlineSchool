package rosa.victor.onlineschool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import rosa.victor.onlineschool.model.Course;

@Repository
public interface CourseRepositoy extends JpaRepository<Course, Integer>{
  
  List<Course> findByOrderByNameDesc();

  List<Course> findByOrderByName();
}
