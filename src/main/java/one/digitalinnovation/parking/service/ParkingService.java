package one.digitalinnovation.parking.service;

import one.digitalinnovation.parking.entity.Parking;
import one.digitalinnovation.parking.exception.ParkingNotFoundException;
import one.digitalinnovation.parking.repository.ParkingRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingService {

    private final ParkingRepository parkingRepository;


    public ParkingService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    @Transactional
    public List<Parking> findAll() {
        return parkingRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional
    public Parking findById(Long id) {
        Optional<Parking> parking = parkingRepository.findById(id);

        if (parking.isEmpty()) {
            throw new ParkingNotFoundException(id);
        };
        return parking.get();
    }

    @Transactional
    public Parking save(Parking parking) {
        return parkingRepository.save(parking);
    }

    @Transactional
    public void deleteById(Long id) {
        parkingRepository.deleteById(id);
    }


    public Boolean findParkingById(Long id) {
        return parkingRepository.findById(id).isPresent();
    }

    @Transactional
    public Parking chekOut(Long id) {
        Parking parking = findById(id);
        parking.setExitDate(LocalDateTime.now());
        parking.setBill(ParkingCheckOut.getBill(parking));
        parkingRepository.save(parking);

        return parking;
    }
}
