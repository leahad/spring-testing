package quest.example.test.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import quest.example.test.Dto.FiremanStatsDto;
import quest.example.test.Entity.Fire;
import quest.example.test.Entity.Fireman;
import quest.example.test.Repository.FiremanRepository;

@RestController
@RequestMapping("/fireman")
public class FiremanController {

    @Autowired
    FiremanRepository firemanRepository;

    record FiremanData(Long id, String name, int firesCount) {
        static FiremanData fromFireman(Fireman fireman) {
            return new FiremanData(fireman.getId(), fireman.getName(), fireman.getFires().size());
        }
    }

    @GetMapping("/veteran")
    public FiremanData getVeteran() {
        Optional<Fireman> veteranMaybe = firemanRepository.getVeteran();
        Fireman veteran = veteranMaybe.orElseThrow(() -> new NotFoundException());
        return FiremanData.fromFireman(veteran);
    }

    @GetMapping("/stats")
    public FiremanStatsDto getStats() {
        List<Fireman> firemen = firemanRepository.findAll();

        if (firemen.isEmpty()) {
            throw new NotFoundException();
        }
    
        int firemenCount = 0;
        Set<Fire> fires = new HashSet<>();

        for (Fireman fireman : firemen) {
            fires.addAll(fireman.getFires());
            firemenCount++;
        }
        
        return new FiremanStatsDto(firemenCount, fires.size());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class NotFoundException extends RuntimeException {
    }
}