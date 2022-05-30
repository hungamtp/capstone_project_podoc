package com.capstone.pod;

import com.capstone.pod.entities.*;
import com.capstone.pod.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@SpringBootApplication
@EnableJpaAuditing
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Bean
    public CommandLineRunner demoData(RoleRepository roleRepository, ProductRepository productRepository
        , DesignedProductRepository designedProductRepository , CategoryRepository categoryRepository
        , RatingRepository ratingRepository , TagRepository tagRepository,
                                      DesignedProductTagRepository designedProductTagRepository) {
        return args -> {
            Category savedCate = categoryRepository.save(Category.builder()
                .id(1)
                .name("Ao")
                .build());
            Product savedProduct = productRepository.save(Product.builder()
                .id(1)
                .name("thun")
                .description("dep")
                .category(savedCate)
                .build());

            Tag savedTag =tagRepository.save(

                    Tag.builder()
                        .id(1)
                        .name("Best seller")
                        .build()
            );

            Tag savedTag2 =tagRepository.save(

                Tag.builder()
                    .id(1)
                    .name("Hot")
                    .build()
            );

            DesignedProduct designedProduct = designedProductRepository.save(
                DesignedProduct.builder()
                    .id(1)
                    .name("designed product")
                    .designedPrice(12.2)
                    .price(100)
                    .image("images")
                    .product(savedProduct)
                    .build()
            );
            designedProductTagRepository.save(DesignedProductTag.builder()
                    .designedProduct(designedProduct)
                    .tag(savedTag)
                .build());
            designedProductTagRepository.save(DesignedProductTag.builder()
                    .designedProduct(designedProduct)
                    .tag(savedTag2)
                .build());

            ratingRepository.saveAll(
                Arrays.asList(
                    Rating.builder()
                        .id(1)
                        .ratingStar(4)
                        .designedProduct(designedProduct)
                        .build(),
                    Rating.builder()
                        .id(3)
                        .ratingStar(4)
                        .designedProduct(designedProduct)
                        .build(),
                    Rating.builder()
                        .id(3)
                        .ratingStar(5)
                        .designedProduct(designedProduct)
                        .build()

                )
            );

            roleRepository.saveAll(Arrays.asList(
                Role.builder()
                    .id(1)
                    .name("ADMIN")
                    .build()
                ,
                Role.builder()
                    .id(2)
                    .name("USER")
                    .build()
            ));
        };
    }


}
