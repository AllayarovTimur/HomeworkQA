package ru.shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.shop.exception.EntityNotFoundException;
import ru.shop.model.Product;
import ru.shop.repository.ProductRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductServiceTest {
    private final ProductRepository repository = Mockito.mock();
    private final ProductService productService = new ProductService(repository);

    @DisplayName("Cервис должен загружать продукт по id")
    @Test
    public void shouldGetProduct() {
        // given
        UUID productId = UUID.randomUUID();
        Product mockedProduct = new Product();
        Mockito.when(repository.findById(productId)).thenReturn(Optional.of(mockedProduct));
        // when
        Product product = productService.getById(productId);
        // then
        assertThat(product).isEqualTo(mockedProduct);
    }

    @Test
    public void shouldThrowWhenProductNotFound() {
        // then
        assertThatThrownBy(() -> productService.getById(UUID.randomUUID())).isInstanceOf(EntityNotFoundException.class);
    }


}
