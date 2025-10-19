package server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.model.Test;

public interface TestRepository extends JpaRepository<Test, Integer> {

}
