package one.digitalinnovation.parking;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import one.digitalinnovation.parking.entity.Parking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import static org.assertj.core.api.Assertions.*;
import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudParkingApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	@DirtiesContext
	void shouldCreateNewParking() {
		Parking newParking =new Parking(null,"AAA1919", "SP", "Fiesta", "Branco", null, null, 10.00);

		ResponseEntity<Void> createResponse = restTemplate
				.postForEntity("/parking", newParking, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationNewParking =createResponse.getHeaders().getLocation();
		ResponseEntity<String> response = restTemplate
				.getForEntity(locationNewParking, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		String license = documentContext.read("$.license");
		String state = documentContext.read("$.state");
		String model = documentContext.read("$.model");
		String color = documentContext.read("$.color");
		Double bill = documentContext.read("$.bill");
		assertThat(id).isNotNull();
		assertThat(license).isEqualTo("AAA1919");
		assertThat(state).isEqualTo("SP");
		assertThat(model).isEqualTo("Fiesta");
		assertThat(color).isEqualTo("Branco");
		assertThat(bill).isEqualTo(10.00);

	}

}
