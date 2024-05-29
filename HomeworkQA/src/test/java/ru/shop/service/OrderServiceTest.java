package ru.shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.shop.exception.BadOrderCountException;
import ru.shop.model.Customer;
import ru.shop.model.Order;
import ru.shop.model.Product;
import ru.shop.model.ProductType;
import ru.shop.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderServiceTest {
    private final OrderRepository repository = Mockito.mock();
    private final OrderService orderService = new OrderService(repository);

    @Test
    public void ShouldAddOrder() {

        var customer = new Customer(UUID.randomUUID(), "customerName", "customerPhone", 11);
        var product = new Product(UUID.randomUUID(), "productName", 10, ProductType.GOOD);

        orderService.add(customer, product, 10);

        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.captor();

        Mockito.verify(repository).save(orderArgumentCaptor.capture());
        Order savedOrder = orderArgumentCaptor.getValue();

        Assertions.assertEquals(10, savedOrder.getCount());
    }

    @Test
    public void ShouldThrowBadOrderCountException() {

        var customer = new Customer(UUID.randomUUID(), "customerName", "customerPhone", 11);
        var product = new Product(UUID.randomUUID(), "productName", 10, ProductType.GOOD);

        //when


        //then
        Assertions.assertThrows(
                BadOrderCountException.class,
                () -> orderService.add(customer, product, 0)
        );

    }

    @Test
    public void shouldFindCustomerOrders() {

        var customer = new Customer(UUID.randomUUID(), "customerName", "customerPhone", 11);
        var firstOrderId = UUID.randomUUID();
        var secondOrderId = UUID.randomUUID();
        Mockito.when(repository.findAll()).thenReturn(
                List.of(
                        new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 2, 18),
                        new Order(firstOrderId, customer.getId(), UUID.randomUUID(), 2, 10),
                        new Order(secondOrderId, customer.getId(), UUID.randomUUID(), 2, 20)

                )
        );

        //when
        List<Order> customerOrders = orderService.findByCustomer(customer);

        //then
        // assertThat(customerOrders)

    }

    @Test
    public void shouldGetTotalCustomerAmount() {

        var customer = new Customer(UUID.randomUUID(), "customerName", "customerPhone", 11);
        var firstOrderId = UUID.randomUUID();
        var secondOrderId = UUID.randomUUID();
        Mockito.when(repository.findAll()).thenReturn(
                List.of(
                        new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 2, 18),
                        new Order(firstOrderId, customer.getId(), UUID.randomUUID(), 2, 10),
                        new Order(secondOrderId, customer.getId(), UUID.randomUUID(), 2, 20),
                        new Order(secondOrderId, customer.getId(), UUID.randomUUID(), 2, 30)

                )
        );

        //when
        long totalCustomerAmount = orderService.getTotalCustomerAmount(customer);

        //then
        assertThat(totalCustomerAmount).isEqualTo(60);

    }
}
