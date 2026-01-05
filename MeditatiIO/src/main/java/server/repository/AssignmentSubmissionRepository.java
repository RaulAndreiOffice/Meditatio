package server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.model.AssignmentSubmission;
import server.model.Assignments;
import server.model.User;
import org.springframework.data.jpa.repository.Query; // <-- Adaugă import
import org.springframework.data.repository.query.Param; // <-- Adaugă import
import java.util.List; // <-- Adaugă import

import java.util.Optional;


public interface AssignmentSubmissionRepository extends  JpaRepository<AssignmentSubmission, Integer> {

    // Găsește rezolvarea (submission) pe baza ID-ului temei (assignment)
    Optional<AssignmentSubmission> findFirstByAssignmentAssidOrderBySubmittedAtDesc(Integer assid);

    //  Găsește toate rezolvările pentru temele create de un profesor
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment.professor.user_id = :professorId")
    List<AssignmentSubmission> findAllSubmissionsForProfessor(@Param("professorId") Integer professorId);

    @Query("SELECT s FROM AssignmentSubmission s WHERE s.student.user_id = :studentId AND s.grade IS NOT NULL")
    List<AssignmentSubmission> findGradedSubmissionsForStudent(@Param("studentId") Integer studentId);

}
