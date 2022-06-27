package tacos;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

@Configuration
@Profile({"!prod", "!qa"})
public class DevelopmentConfig {
    
	@Bean
	CommandLineRunner dataLoader(
        IngredientRepository ingredientRepo,
        TacoRepository tacoRepo,
        UserRepository userRepo,
        PasswordEncoder encoder) {

		return args -> {
			ingredientRepo.save(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
			ingredientRepo.save(new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
			ingredientRepo.save(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
			ingredientRepo.save(new Ingredient("CARN", "Carnitas", Type.PROTEIN));
			ingredientRepo.save(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
			ingredientRepo.save(new Ingredient("LETC", "Lettuce", Type.VEGGIES));
			ingredientRepo.save(new Ingredient("CHED", "Cheddar", Type.CHEESE));
			ingredientRepo.save(new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
			ingredientRepo.save(new Ingredient("SLSA", "Salsa", Type.SAUCE));
			ingredientRepo.save(new Ingredient("SRCR", "Sour Cream", Type.SAUCE));

            userRepo.save(
                new User(
                    "register",
                    encoder.encode("register"),
                    "Full name",
                    "123 North Street",
                    "Cross Roads",
                    "TX",
                    "76227",
                    "123-123-1234"
                )
            );
		};
	}

}
