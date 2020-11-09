package com.ciandt.techlab.order.entrypoints.controllers;

import com.ciandt.techlab.order.entities.Order;
import com.ciandt.techlab.order.entrypoints.controllers.dto.RequestCreateOrderDTO;
import com.ciandt.techlab.order.usecases.CreateOrder;
import com.ciandt.techlab.order.usecases.DeleteOrder;
import com.ciandt.techlab.order.usecases.GetOrder;
import com.ciandt.techlab.order.usecases.UpdateOrder;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CreateOrderControllerTest extends AbstractControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private GetOrder getOrder;

    @Mock
    private CreateOrder createOrder;

    @Mock
    private DeleteOrder deleteOrder;

    @Mock
    private UpdateOrder updateOrder;

    @BeforeEach
    public void setup() {
        this.setUp(orderController);
    }

    @Test
    public void shouldCreateOrder() throws Exception {
        final RequestCreateOrderDTO createOrderDTO = buildRequestObject("jlima", "Venda de moveis", BigDecimal.valueOf(100), BigDecimal.ZERO);
        final Order order = buildOrderEntity(createOrderDTO);

        Mockito.when(createOrder.execute(order)).thenReturn(order.toBuilder().id(UUID.randomUUID().toString()).build());

        mockMvc.perform(post("/orders")
                .content(mapToJson(createOrderDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.any(String.class)));
    }

    @Test
    public void shouldThrowBadRequestWhenCreateOrderWithoutSalesman() throws Exception {
        final RequestCreateOrderDTO createOrderDTO = buildRequestObject(null, "Venda de moveis", BigDecimal.valueOf(100), BigDecimal.ZERO);

        MvcResult result = mockMvc.perform(post("/orders")
                .content(mapToJson(createOrderDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        final String bodyResult = result.getResponse().getContentAsString();
        Assert.assertTrue(bodyResult.contains("salesman"));
        Assert.assertTrue(bodyResult.contains("must not be blank"));
    }

    @Test
    public void shouldThrowBadRequestWhenCreateOrderWithoutDescription() throws Exception {
        final RequestCreateOrderDTO createOrderDTO = buildRequestObject("jlima", null, BigDecimal.valueOf(100), BigDecimal.ZERO);

        MvcResult result = mockMvc.perform(post("/orders")
                .content(mapToJson(createOrderDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        final String bodyResult = result.getResponse().getContentAsString();
        Assert.assertTrue(bodyResult.contains("description"));
        Assert.assertTrue(bodyResult.contains("must not be blank"));
    }

    @Test
    public void shouldThrowBadRequestWhenCreateOrderWithoutAmount() throws Exception {
        final RequestCreateOrderDTO createOrderDTO = buildRequestObject("jlima", "Venda de moveis", null, BigDecimal.ZERO);

        MvcResult result = mockMvc.perform(post("/orders")
                .content(mapToJson(createOrderDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        final String bodyResult = result.getResponse().getContentAsString();
        Assert.assertTrue(bodyResult.contains("amount"));
        Assert.assertTrue(bodyResult.contains("must not be null"));
    }

    @Test
    public void shouldThrowBadRequestWhenCreateOrderWithAmountLessThan5() throws Exception {
        final RequestCreateOrderDTO createOrderDTO = buildRequestObject("jlima", "Venda de moveis", BigDecimal.ONE, BigDecimal.ZERO);

        MvcResult result = mockMvc.perform(post("/orders")
                .content(mapToJson(createOrderDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        final String bodyResult = result.getResponse().getContentAsString();
        Assert.assertTrue(bodyResult.contains("amount"));
        Assert.assertTrue(bodyResult.contains("must be greater than or equal to 5.00"));
    }

    @Test
    public void shouldThrowBadRequestWhenCreateOrderWithAmountGreaterThen() throws Exception {
        final RequestCreateOrderDTO createOrderDTO = buildRequestObject("jlima", "Venda de moveis", BigDecimal.valueOf(100001), BigDecimal.ZERO);

        MvcResult result = mockMvc.perform(post("/orders")
                .content(mapToJson(createOrderDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        final String bodyResult = result.getResponse().getContentAsString();
        Assert.assertTrue(bodyResult.contains("amount"));
        Assert.assertTrue(bodyResult.contains("must be less than or equal to 100000.00"));
    }

    private Order buildOrderEntity(RequestCreateOrderDTO createOrderDTO) {
        return Order.builder()
                .salesman(createOrderDTO.getSalesman())
                .discount(createOrderDTO.getDiscount())
                .currentAmount(createOrderDTO.getAmount())
                .description(createOrderDTO.getDescription())
                .build();
    }

    private RequestCreateOrderDTO buildRequestObject(final String salesman, final String description, final BigDecimal amount, final BigDecimal discount) {
        return RequestCreateOrderDTO.builder()
                .amount(amount)
                .discount(discount)
                .salesman(salesman)
                .description(description)
                .build();
    }
}