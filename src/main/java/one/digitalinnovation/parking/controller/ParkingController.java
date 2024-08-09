package one.digitalinnovation.parking.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import one.digitalinnovation.parking.entity.Parking;
import one.digitalinnovation.parking.repository.ParkingRepository;
import one.digitalinnovation.parking.service.ParkingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }


    @GetMapping
    public ResponseEntity<List<Parking>> findAll() {
        List<Parking> parkingPage = parkingService.findAll();
        return ResponseEntity.ok(parkingPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Parking>> findById(@PathVariable Long id) {
        Optional<Parking> parking = parkingService.findById(id);

        if (parking != null) {
            return ResponseEntity.ok(parking);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> createParking(@RequestBody Parking parking, UriComponentsBuilder ucb) {
        Parking savedParking = parkingService.save(parking);
        URI locationNewParking =ucb
                .path("/parking/{id}")
                .buildAndExpand(savedParking.getId())
                .toUri();
        return ResponseEntity.created(locationNewParking).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putParking(@PathVariable Long id, @RequestBody Parking parkingUpdated) {
        Optional<Parking> parking = parkingService.findById(id);

        if (parking != null) {
            Parking updateParking =new Parking(parking.get().getId(), parkingUpdated.getLicense(), parkingUpdated.getState(),
                    parkingUpdated.getModel(), parkingUpdated.getColor(), parkingUpdated.getEntryDate(), parkingUpdated.getExitDate(),
                    parkingUpdated.getBill());
            parkingService.save(updateParking);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkgin(@PathVariable Long id) {
        if (parkingService.findParkingById(id)) {
            parkingService.deleteById(id);
        }
        return ResponseEntity.notFound().build();
    }
}
