package one.digitalinnovation.parking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


import java.time.LocalDateTime;

@Entity
public class Parking {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String license;
        private String state;
        private String model;
        private String color;
        @JsonFormat(pattern = "dd/MM/yyyy")
        private LocalDateTime entryDate;
        @JsonFormat(pattern = "dd/MM/yyyy")
        private LocalDateTime exitDate;
        private Double bill;

        public Parking() {
        }

        public Parking(Long id, String license, String state, String model, String color, LocalDateTime entryDate, LocalDateTime exitDate, Double bill) {
                this.id = id;
                this.license = license;
                this.state = state;
                this.model = model;
                this.color = color;
                this.entryDate = entryDate;
                this.exitDate = exitDate;
                this.bill = bill;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getLicense() {
                return license;
        }

        public void setLicense(String license) {
                this.license = license;
        }

        public String getState() {
                return state;
        }

        public void setState(String state) {
                this.state = state;
        }

        public String getModel() {
                return model;
        }

        public void setModel(String model) {
                this.model = model;
        }

        public String getColor() {
                return color;
        }

        public void setColor(String color) {
                this.color = color;
        }

        public LocalDateTime getEntryDate() {
                return entryDate;
        }

        public void setEntryDate(LocalDateTime entryDate) {
                this.entryDate = entryDate;
        }

        public LocalDateTime getExitDate() {
                return exitDate;
        }

        public void setExitDate(LocalDateTime exitDate) {
                this.exitDate = exitDate;
        }

        public Double getBill() {
                return bill;
        }

        public void setBill(Double bill) {
                this.bill = bill;
        }
}
