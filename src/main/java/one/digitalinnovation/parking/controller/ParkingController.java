package one.digitalinnovation.parking.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import java.time.LocalDateTime;
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
    public ResponseEntity<Parking> findById(@PathVariable Long id) {
        Parking parking = parkingService.findById(id);

        if (parking != null) {
            return ResponseEntity.ok(parking);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> createParking(@RequestBody Parking parking, UriComponentsBuilder ucb) {
        parking.setEntryDate(LocalDateTime.now());
        Parking savedParking = parkingService.save(parking);
        URI locationNewParking =ucb
                .path("/parking/{id}")
                .buildAndExpand(savedParking.getId())
                .toUri();
        return ResponseEntity.created(locationNewParking).build();
    }

    @Operation(summary = "Update a field of a parking.")
    @PutMapping("/{id}")
    public ResponseEntity<Void> putParking(@PathVariable Long id, @RequestBody Parking parkingUpdated) {
        Parking existingParking = parkingService.findById(id);

        if (existingParking != null) {
            if (parkingUpdated.getLicense() != null) {
                existingParking.setLicense(parkingUpdated.getLicense());
            }
            if (parkingUpdated.getState() != null) {
                existingParking.setState(parkingUpdated.getState());
            }
            if (parkingUpdated.getModel() != null) {
                existingParking.setModel(parkingUpdated.getModel());
            }
            if (parkingUpdated.getColor() != null) {
                existingParking.setColor(parkingUpdated.getColor());
            }
            if (parkingUpdated.getEntryDate() != null) {
                existingParking.setEntryDate(parkingUpdated.getEntryDate());
            }
            if (parkingUpdated.getExitDate() != null) {
                existingParking.setExitDate(parkingUpdated.getExitDate());
            }
            if (parkingUpdated.getBill() != null) {
                existingParking.setBill(parkingUpdated.getBill());
            }
            parkingService.save(existingParking);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a parking.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParking(@PathVariable Long id) {
        if (parkingService.findParkingById(id)) {
            parkingService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Checking out a parking spot", description = "Updates the exitDate for a parking spot with the given ID")
    @PostMapping("/{id}")
    public ResponseEntity<Void> checkOut(@PathVariable Long id) {
        Parking parking = parkingService.chekOut(id);
        parkingService.save(parking);
        return ResponseEntity.noContent().build();
    }
}
