package server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.Assignments;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface  AssignmentRepository extends JpaRepository<Assignments, Integer>{

    @Query("SELECT a FROM Assignments a WHERE a.student.user_id = :studentId")
    List<Assignments> findAssignmentsByStudentId(@Param("studentId") Integer studentId);

    @Query("SELECT a FROM Assignments a WHERE a.professor.user_id = :professorId")
    List<Assignments> findAssignmentsByProfessorUserId(@Param("professorId") Integer professorId);
}
