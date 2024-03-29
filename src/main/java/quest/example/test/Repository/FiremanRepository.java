package quest.example.test.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import quest.example.test.Entity.Fireman;

@Repository
public interface FiremanRepository extends JpaRepository<Fireman, Long>{
    
    @Query("SELECT fm FROM Fireman fm WHERE SIZE(fm.fires) = (SELECT max(SIZE(subFm.fires)) FROM Fireman subFm)")
    Optional<Fireman> getVeteran();
}
