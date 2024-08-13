package one.digitalinnovation.parking;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import one.digitalinnovation.parking.entity.Parking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudParkingApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	@DirtiesContext
	void shouldCreateNewParking() {
		Parking newParking =new Parking(null,"AAA1919", "SP", "Fiesta", "Branco", LocalDateTime.now(), null, 10.00);

		ResponseEntity<Void> createResponse = restTemplate
				.withBasicAuth("user", "12345")
				.postForEntity("/parking", newParking, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationNewParking =createResponse.getHeaders().getLocation();
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("user", "12345")
				.getForEntity(locationNewParking, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(locationNewParking).isNotNull();

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

	@Test
	void shouldReturnAllParkingWhenListIsRequested() {
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("user", "12345")
				.getForEntity("/parking", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int parkingCount = documentContext.read("$.length()");
		assertThat(parkingCount).isEqualTo(4);

		List<Integer> ids = documentContext.read("$..id");
		assertThat(ids).containsExactlyInAnyOrder(50, 51, 52, 53);

		List<String> licenses = documentContext.read("$..license");
		assertThat(licenses).containsExactlyInAnyOrder("AAA1111", "BBB1111", "AAA2222", "BBB2222");

		List<String> states = documentContext.read("$..state");
		assertThat(states).containsExactlyInAnyOrder("SP", "MG", "PR", "AM");

		List<String> models = documentContext.read("$..model");
		assertThat(models).containsExactlyInAnyOrder("CELTA", "FOX", "MONZA", "VOYAGE");

		List<String> colors = documentContext.read("$..color");
		assertThat(colors).containsExactlyInAnyOrder("BRANCO", "CINZA", "PRETO", "AZUL");

		List<Double> bills = documentContext.read("$..bill");
		assertThat(bills).containsExactlyInAnyOrder(10.00, 15.00, 20.00, 100.00);


	}

	@Test
	void shouldReturnAParkingThatIsSaved() {
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("user", "12345")
				.getForEntity("/parking/51", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		String states = documentContext.read("$.state");
		String license = documentContext.read("$.license");
		String models = documentContext.read("$.model");
		String colors = documentContext.read("$.color");
		Double bills = documentContext.read("$.bill");
		assertThat(id).isEqualTo(51);
		assertThat(license).isEqualTo("BBB1111");
		assertThat(states).isEqualTo("MG");
		assertThat(models).isEqualTo("FOX");
		assertThat(colors).isEqualTo("CINZA");
		assertThat(bills).isEqualTo(15.00);
	}

	@Test
	void shouldReturnAPageOfCashCards() {
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("user", "12345")
				.getForEntity("/parking?page=0&size=1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		List<Integer> page = documentContext.read("$[*]");
		assertThat(page.size()).isEqualTo(4);
	}

	@Test
	@Transactional
	void shouldUpdateAnExistParking() {
		Parking parkingUpdate = new Parking(null, "AAA1234", null, null, null, null, null, null);
		HttpEntity<Parking> request = new HttpEntity<>(parkingUpdate);
		ResponseEntity<Void> response = restTemplate
				.withBasicAuth("user", "12345")
				.exchange("/parking/50", HttpMethod.PUT,request, Void.class);

		ResponseEntity<String>getResponse = restTemplate
				.withBasicAuth("user", "12345")
				.getForEntity("/parking/50", String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number id = documentContext.read("$.id");
		String license = documentContext.read("$.license");
		assertThat(id).isEqualTo(50);
		assertThat(license).isEqualTo("AAA1234");
	}

	@Test
	void shouldNotUpdateAParkingThatDoesNotExist() {
		Parking unknownCard = new Parking(null, "ccc9999", "RS", null, null, null, null, null);
		HttpEntity<Parking> request = new HttpEntity<>(unknownCard);
		ResponseEntity<Void>response = restTemplate
				.withBasicAuth("user", "12345")
				.exchange("/parking/5050", HttpMethod.PUT, request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@Transactional
	@DirtiesContext
	void shouldDeleteAnExistedParking() {
		ResponseEntity<String> preDeleteParking = restTemplate
				.withBasicAuth("user", "12345")
				.getForEntity("/parking/50", String.class);
		assertThat(preDeleteParking.getStatusCode()).isEqualTo(HttpStatus.OK);

		ResponseEntity<Void> response = restTemplate
				.withBasicAuth("user", "12345")
				.exchange("/parking/53", HttpMethod.DELETE, null, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> getResponse = restTemplate
				.withBasicAuth("user", "12345")
				.getForEntity("/parking/53", String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
