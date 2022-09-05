package rosa.victor.onlineschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rosa.victor.onlineschool.model.Course;

public interface CourseRepositoy extends JpaRepository<Course, Integer>{
  
}
