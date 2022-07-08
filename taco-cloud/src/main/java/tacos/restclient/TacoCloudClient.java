package tacos.restclient;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;

@Service
@Slf4j
public class TacoCloudClient {

    private RestTemplate rest;

    public TacoCloudClient() {
        this.rest = new RestTemplate();;
    }

    public Ingredient getIngredientById(String ingredientId) {
        return rest.getForObject(
            "http://localhost:8080/ingredients/{id}",
            Ingredient.class,
            ingredientId
        );
    }

    public void updateIngredient(Ingredient ingredient) {
        rest.put(
            "http://localhost:8080/ingredients/{id}",
            ingredient,
            ingredient.getId()
        );
    }

    public void deleteIngredient(Ingredient ingredient) {
        rest.delete(
            "http://localhost:8080/ingredients/{id}",
            ingredient.getId()
        );
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        ResponseEntity<Ingredient> responseEntity = rest.postForEntity(
            "http://localhost:8080/ingredients",
            ingredient,
            Ingredient.class
        );

        log.info(
            "New resource created at {}",
            responseEntity.getHeaders().getLocation()
        );

        return responseEntity.getBody();
    }

} 