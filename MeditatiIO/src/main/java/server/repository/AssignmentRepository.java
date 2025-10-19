package server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.Assignments;

public interface  AssignmentRepository extends JpaRepository<Assignments, Integer>{


}
