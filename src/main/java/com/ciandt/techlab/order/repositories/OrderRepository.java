package com.ciandt.techlab.order.repositories;

import com.ciandt.techlab.order.repositories.common.CrudRepository;
import com.ciandt.techlab.order.repositories.dto.OrderData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class OrderRepository extends CrudRepository<OrderData, String> {

    // DYNAMO TABLE STATIC VALUES
    private static final String TABLE_NAME = "orders";
    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_AMOUNT = "amount";
    private static final String ATTRIBUTE_SALESMAN = "salesman";
    private static final String ATTRIBUTE_DISCOUNT = "discount";
    private static final String ATTRIBUTE_DESCRIPTION = "description";
    private static final String ATTRIBUTE_UPDATED_TIME = "updatedTime";
    private static final String ATTRIBUTE_CREATION_TIME = "creationTime";
    private static final String ATTRIBUTE_ORIGINAL_AMOUNT = "originalAmount";

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    @Autowired
    protected OrderRepository(final DynamoDbClient dynamoDbClient) {
        super(dynamoDbClient, TABLE_NAME);
    }

    @Override
    protected Map<String, AttributeValueUpdate> mapItemSaveAttributes(final OrderData entity) {
        final Map<String, AttributeValueUpdate> entityAttributes = new HashMap<>();

        entityAttributes.computeIfAbsent(ATTRIBUTE_ID, key -> computeUpdateValueIfNotNull(entity.getId()));
        entityAttributes.computeIfAbsent(ATTRIBUTE_AMOUNT, key -> computeUpdateValueIfNotNull(entity.getCurrentAmount()));
        entityAttributes.computeIfAbsent(ATTRIBUTE_DISCOUNT, key -> computeUpdateValueIfNotNull(entity.getDiscount()));
        entityAttributes.computeIfAbsent(ATTRIBUTE_DESCRIPTION, key -> computeUpdateValueIfNotNull(entity.getDescription()));
        entityAttributes.computeIfAbsent(ATTRIBUTE_ORIGINAL_AMOUNT, key -> computeUpdateValueIfNotNull(entity.getOriginalAmount()));
        entityAttributes.computeIfAbsent(ATTRIBUTE_SALESMAN, key -> computeUpdateValueIfNotNull(entity.getSalesman()));

        final LocalDateTime localDateTime = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        entityAttributes.put(ATTRIBUTE_CREATION_TIME, AttributeValueUpdate.builder()
                .value(AttributeValue.builder()
                        .s(localDateTime.format(formatter))
                        .build())
                .build());

        return entityAttributes;
    }

    @Override
    protected Map<String, AttributeValueUpdate> mapItemUpdateAttributes(OrderData entity) {
        final Map<String, AttributeValueUpdate> entityAttributes = new HashMap<>();

        entityAttributes.computeIfAbsent(ATTRIBUTE_AMOUNT, key -> computeUpdateValueIfNotNull(entity.getCurrentAmount()));
        entityAttributes.computeIfAbsent(ATTRIBUTE_DISCOUNT, key -> computeUpdateValueIfNotNull(entity.getDiscount()));
        entityAttributes.computeIfAbsent(ATTRIBUTE_DESCRIPTION, key -> computeUpdateValueIfNotNull(entity.getDescription()));
        entityAttributes.computeIfAbsent(ATTRIBUTE_SALESMAN, key -> computeUpdateValueIfNotNull(entity.getSalesman()));

        final LocalDateTime localDateTime = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        entityAttributes.put(ATTRIBUTE_UPDATED_TIME, AttributeValueUpdate.builder()
                .value(AttributeValue.builder()
                        .s(localDateTime.format(formatter))
                        .build())
                .build());

        return entityAttributes;
    }

    @Override
    protected Map<String, AttributeValue> mapItemIdAttribute(String orderId) {
        return Collections.singletonMap(ATTRIBUTE_ID, AttributeValue.builder()
                .s(orderId)
                .build());
    }

    @Override
    protected Map<String, AttributeValue> mapItemEntityIdAttribute(OrderData order) {
        final String orderId = order.getId() != null ? order.getId() : UUID.randomUUID().toString();
        return mapItemIdAttribute(orderId);
    }

    @Override
    protected OrderData mapAttributesToEntity(Map<String, AttributeValue> responseAttributes) {
        final BigDecimal parsedAmount = getAttributeValue(responseAttributes, ATTRIBUTE_AMOUNT).n() != null
                ? new BigDecimal(getAttributeValue(responseAttributes, ATTRIBUTE_AMOUNT).n())
                : null;

        final BigDecimal parsedDiscount = getAttributeValue(responseAttributes, ATTRIBUTE_DISCOUNT).n() != null
                ? new BigDecimal(getAttributeValue(responseAttributes, ATTRIBUTE_DISCOUNT).n())
                : null;

        final BigDecimal parsedOriginalAmount = getAttributeValue(responseAttributes, ATTRIBUTE_ORIGINAL_AMOUNT).n() != null
                ? new BigDecimal(getAttributeValue(responseAttributes, ATTRIBUTE_ORIGINAL_AMOUNT).n())
                : null;

        return OrderData.builder()
                .discount(parsedDiscount)
                .currentAmount(parsedAmount)
                .originalAmount(parsedOriginalAmount)
                .id(getAttributeValue(responseAttributes, ATTRIBUTE_ID).s())
                .description(getAttributeValue(responseAttributes, ATTRIBUTE_DESCRIPTION).s())
                .salesman(getAttributeValue(responseAttributes, ATTRIBUTE_SALESMAN).s())
                .build();
    }
}
