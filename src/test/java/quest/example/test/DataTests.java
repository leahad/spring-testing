package quest.example.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import quest.example.test.Entity.Fire;
import quest.example.test.Entity.Fireman;
import quest.example.test.Repository.FireRepository;
import quest.example.test.Repository.FiremanRepository;

@DataJpaTest
public class DataTests {

    @Autowired
    FireRepository fireRepository;

    @Autowired
    FiremanRepository firemanRepository;

    @Test
    public void testCreateFire() {
        int severity = 8;
        Instant date = Instant.now();
        var fire = new Fire(severity, date);

        fireRepository.saveAndFlush(fire);

        assertFireValues(fire.getId(), date, severity);
    }

    @Test
    public void testCreateFiresAssociatedWithFireman() {
        int severityFire_1 = 8;
        Instant dateFire_1 = Instant.now();
        var fire_1 = new Fire(severityFire_1, dateFire_1);

        int severityFire_2 = 3;
        Instant dateFire_2 = dateFire_1.minus(7, ChronoUnit.DAYS);
        var fire_2 = new Fire(severityFire_2, dateFire_2);

        int severityFire_3 = 6;
        Instant dateFire_3 = dateFire_1.minus(14, ChronoUnit.DAYS);
        var fire_3 = new Fire(severityFire_3, dateFire_3);

        fireRepository.saveAndFlush(fire_1);
        fireRepository.saveAndFlush(fire_2);
        fireRepository.saveAndFlush(fire_3);

        var firewoman = new Fireman("Clarisse");
        List<Fire> fires = new ArrayList<>();
        fires.add(fire_1);
        fires.add(fire_2);
        fires.add(fire_3);
        firewoman.setFires(fires);

        firemanRepository.saveAndFlush(firewoman);

        fire_1.setFireman(firewoman);
        fire_2.setFireman(firewoman);
        fire_3.setFireman(firewoman);

        fireRepository.saveAndFlush(fire_1);
        fireRepository.saveAndFlush(fire_2);
        fireRepository.saveAndFlush(fire_3);

        assertFireValues(fire_1.getId(), dateFire_1, severityFire_1);
        assertFireValues(fire_2.getId(), dateFire_2, severityFire_2);
        assertFireValues(fire_3.getId(), dateFire_3, severityFire_3);

        assertFiremanAndFiresValues(firewoman.getId(), fires);
    }

    @Test
    public void testGetFiremanVeteran() {
        int severityFire_1 = 8;
        Instant dateFire_1 = Instant.now();
        var fire_1 = new Fire(severityFire_1, dateFire_1);

        int severityFire_2 = 3;
        Instant dateFire_2 = dateFire_1.minus(7, ChronoUnit.DAYS);
        var fire_2 = new Fire(severityFire_2, dateFire_2);

        int severityFire_3 = 6;
        Instant dateFire_3 = dateFire_1.minus(14, ChronoUnit.DAYS);
        var fire_3 = new Fire(severityFire_3, dateFire_3);

        fireRepository.saveAndFlush(fire_1);
        fireRepository.saveAndFlush(fire_2);
        fireRepository.saveAndFlush(fire_3);

        var firewoman = new Fireman("Clarisse");
        List<Fire> FirewomanFires = new ArrayList<>();
        FirewomanFires .add(fire_1);
        FirewomanFires .add(fire_2);
        firewoman.setFires(FirewomanFires);

        var fireman = new Fireman("Henry");
        List<Fire> FiremanFires = new ArrayList<>();
        FiremanFires.add(fire_3);
        fireman.setFires(FiremanFires);

        firemanRepository.saveAndFlush(firewoman);
        firemanRepository.saveAndFlush(fireman);

        fire_1.setFireman(firewoman);
        fire_2.setFireman(firewoman);
        fire_3.setFireman(fireman);

        fireRepository.saveAndFlush(fire_1);
        fireRepository.saveAndFlush(fire_2);
        fireRepository.saveAndFlush(fire_3);

        Optional<Fireman> veteranFromDB = firemanRepository.getVeteran();

        assertEquals(firewoman.getId(), veteranFromDB.get().getId());
    }

    @Test
    public void testGetVeteranWhenOnlyOneFireman() {
        var firewoman = new Fireman("Clarisse");
        firemanRepository.saveAndFlush(firewoman);

        Optional<Fireman> veteranFromDB = firemanRepository.getVeteran();
        assertEquals(firewoman.getId(), veteranFromDB.get().getId());
    }

    @Test
    public void testGetVeteranWhenNoFireman() {
        Optional<Fireman> veteranFromDB = firemanRepository.getVeteran();
        assertFalse(veteranFromDB.isPresent());
    }

    private void assertFireValues(Long fireId, Instant expectedDate, int expectedSeverity) {
        Optional<Fire> fromDB = fireRepository.findById(fireId);

        assertTrue(fromDB.isPresent());
        assertEquals(fireId, fromDB.get().getId());
        assertEquals(expectedDate, fromDB.get().getDate());
        assertEquals(expectedSeverity, fromDB.get().getSeverity());
    }

    private void assertFiremanAndFiresValues(Long firemanId, List<Fire> expectedFires) {
        Optional<Fireman> firemanFromDB = firemanRepository.findById(firemanId);

        assertTrue(firemanFromDB.isPresent());
        assertEquals(firemanId, firemanFromDB.get().getId());
        assertEquals(expectedFires, firemanFromDB.get().getFires());
    }
}
