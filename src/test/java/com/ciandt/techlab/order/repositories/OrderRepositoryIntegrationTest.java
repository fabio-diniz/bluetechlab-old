package com.ciandt.techlab.order.repositories;

import com.ciandt.techlab.order.repositories.dto.OrderData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderRepositoryIntegrationTest extends RepositoryIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
//    @Ignore
    public void shouldSaveOrder() {
        final OrderData orderToSave = buildOrderData("jlima", "Teste Repo", BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
        final OrderData orderSaved = orderRepository.save(orderToSave);

        assertNotNull(orderSaved);
        assertNotNull(orderSaved.getId());

        final Optional<OrderData> optionalFound = orderRepository.findById(orderSaved.getId());
        assertTrue(optionalFound.isPresent());

        final OrderData orderFound = optionalFound.get();

        assertEquals(orderSaved.getId(), orderFound.getId());
        assertEquals(orderSaved.getSalesman(), orderFound.getSalesman());
        assertEquals(orderSaved.getDescription(), orderFound.getDescription());

        final OrderData orderToUpdate = orderFound.toBuilder()
                .salesman("techlab")
                .description("Teste Update Repo")
                .build();

        orderRepository.update(orderToUpdate);
        final Optional<OrderData> optionalUpdateFound = orderRepository.findById(orderToUpdate.getId());
        assertTrue(optionalUpdateFound.isPresent());

        final OrderData orderUpdatedFound = optionalUpdateFound.get();
        assertNotEquals(orderUpdatedFound.getSalesman(), orderFound.getSalesman());
        assertNotEquals(orderUpdatedFound.getDescription(), orderFound.getDescription());
    }

    private OrderData buildOrderData(final String salesman, final String description, final BigDecimal currentAmount, final BigDecimal discount, final BigDecimal originalAmount) {
        return OrderData.builder()
                .salesman(salesman)
                .discount(discount)
                .description(description)
                .currentAmount(currentAmount)
                .originalAmount(originalAmount)
                .build();
    }
}
