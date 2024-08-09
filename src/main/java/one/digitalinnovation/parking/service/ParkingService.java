package one.digitalinnovation.parking.service;

import one.digitalinnovation.parking.entity.Parking;
import one.digitalinnovation.parking.repository.ParkingRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingService {

    private final ParkingRepository parkingRepository;


    public ParkingService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }


    public List<Parking> findAll() {
        return parkingRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Optional<Parking> findById(Long id) {
        return parkingRepository.findById(id);
    }

    public Parking save(Parking parking) {
        return parkingRepository.save(parking);
    }

    public void deleteById(Long id) {
        parkingRepository.deleteById(id);
    }

    public Boolean findParkingById(Long id) {
        return parkingRepository.findById(id).isPresent();
    }
}
