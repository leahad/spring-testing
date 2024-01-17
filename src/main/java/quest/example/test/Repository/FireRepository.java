package quest.example.test.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import quest.example.test.Entity.Fire;

@Repository
public interface FireRepository extends JpaRepository<Fire, Long> {
    
}
