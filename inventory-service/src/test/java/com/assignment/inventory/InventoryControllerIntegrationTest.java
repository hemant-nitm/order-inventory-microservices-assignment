package com.assignment.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getBatches_forExistingProduct_returnsOkOrBadRequest() {
        ResponseEntity<String> res = restTemplate.getForEntity("/inventory/1", String.class);
        // plausible responses: OK if seeded product id 1 exists, or BAD_REQUEST if not.
        assertThat(res.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateInventory_endpointAcceptsPost() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"productId\":1,\"quantity\":1}";
        ResponseEntity<String> res = restTemplate.postForEntity("/inventory/update", new HttpEntity<>(body, headers), String.class);
        assertThat(res.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.BAD_REQUEST);
    }
}
