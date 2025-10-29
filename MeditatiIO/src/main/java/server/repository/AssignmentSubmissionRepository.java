package server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.model.AssignmentSubmission;
import server.model.Assignments;
import server.model.User;

import java.util.Optional;


public interface AssignmentSubmissionRepository extends  JpaRepository<AssignmentSubmission, Integer> {

}
