package com.assignment.order.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

/**
 * @author hemant-nitm
 */
@Component
public class InventoryClient {

    private final RestTemplate restTemplate;

    // Inventory service URL
    private static final String INVENTORY_UPDATE_URL = "http://localhost:8081/inventory/update";

    public InventoryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public boolean deductStock(Long productId, Integer quantity) {
        try {
            String jsonBody = String.format("{\"productId\": %d, \"quantity\": %d}", productId, quantity);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<Void> response =
                    restTemplate.postForEntity(INVENTORY_UPDATE_URL, requestEntity, Void.class);

            return response.getStatusCode().is2xxSuccessful();

        } catch (RestClientException ex) {
            System.out.println("Inventory update failed: " + ex.getMessage());
            return false;
        }
    }
}
