package ru.shop.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.shop.exception.EntityNotFoundException;
import ru.shop.model.ProductReturn;
import ru.shop.repository.ProductReturnRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ProductReturnServiceTest {
    private final ProductReturnRepository repository = Mockito.mock();
    OrderService orderService = Mockito.mock(OrderService.class);
    private final ProductReturnService productReturnService = new ProductReturnService(repository, orderService);


    @Test
    public void shouldFindAll() {
        List<ProductReturn> productReturns = List.of(new ProductReturn(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), 3));
        when(repository.findAll()).thenReturn(productReturns);
        List<ProductReturn> result = productReturnService.findAll();
        assertEquals(1, result.size());
    }


    @Test
    void shouldGetProductReturn() {
        UUID id = UUID.randomUUID();
        ProductReturn productReturn = new ProductReturn(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), 3);

        when(repository.findById(id)).thenReturn(Optional.of(productReturn));

        ProductReturn result = productReturnService.findById(id);

        assertEquals(productReturn, result);
    }


    @Test
    public void shouldThrowWhenProductReturnNotFound() {

        assertThatThrownBy(() -> productReturnService.findById(UUID.randomUUID())).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void shouldFindNotExceptId() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            productReturnService.findById(id);
        });
    }

}


