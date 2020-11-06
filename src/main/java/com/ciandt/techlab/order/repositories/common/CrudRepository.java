package com.ciandt.techlab.order.repositories.common;

import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;
import java.util.Optional;

public abstract class CrudRepository<T, I> {

    private final String tableName;
    private final DynamoDbClient dynamoDbClient;

    protected CrudRepository(final DynamoDbClient dynamoDbClient, final String tableName) {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = tableName;
    }

    public T save(T entity) {
        final UpdateItemRequest putItemRequest = UpdateItemRequest.builder()
                .attributeUpdates(mapItemSaveAttributes(entity))
                .key(mapItemEntityIdAttribute(entity))
                .returnValues(ReturnValue.ALL_NEW)
                .tableName(tableName)
                .build();

        return mapAttributesToEntity(dynamoDbClient.updateItem(putItemRequest).attributes());
    }

    public T update(T entity) {
        final UpdateItemRequest putItemRequest = UpdateItemRequest.builder()
                .attributeUpdates(mapItemUpdateAttributes(entity))
                .key(mapItemEntityIdAttribute(entity))
                .returnValues(ReturnValue.ALL_NEW)
                .tableName(tableName)
                .build();

        return mapAttributesToEntity(dynamoDbClient.updateItem(putItemRequest).attributes());
    }

    public void delete(I entityId) {
        final DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(mapItemIdAttribute(entityId))
                .build();

        dynamoDbClient.deleteItem(deleteItemRequest);
    }

    public Optional<T> findById(I entityId) {
        final GetItemRequest getItemRequest = GetItemRequest.builder()
                .key(mapItemIdAttribute(entityId))
                .tableName(tableName)
                .build();

        final Map<String, AttributeValue> responseAttributes = dynamoDbClient.getItem(getItemRequest).item();

        if (responseAttributes != null && !responseAttributes.isEmpty()) {
            return Optional.of(mapAttributesToEntity(responseAttributes));
        }

        return Optional.empty();
    }

    protected AttributeValue getAttributeValue(final Map<String, AttributeValue> responseAttributes, final String key) {
        final AttributeValue attributeValue = responseAttributes.get(key);

        if (attributeValue == null) {
            return AttributeValue.builder().build();
        }

        return attributeValue;
    }

    protected AttributeValueUpdate computeUpdateValueIfNotNull(final Boolean value) {
        return value != null ? convertToAttributeValueUpdate(AttributeValue.builder()
                .bool(value).build()) : null;
    }

    protected AttributeValueUpdate computeUpdateValueIfNotNull(final Number value) {
        return value != null ? convertToAttributeValueUpdate(AttributeValue.builder()
                .n(value.toString()).build()) : null;
    }

    protected AttributeValueUpdate computeUpdateValueIfNotNull(final String value) {
        return !StringUtils.isEmpty(value) ? convertToAttributeValueUpdate(AttributeValue.builder()
                .s(value).build()) : null;
    }

    protected AttributeValueUpdate computeUpdateValueIfNotNull(final Map<String, AttributeValue> attributeValueMap) {
        return attributeValueMap != null && !attributeValueMap.isEmpty() ? convertToAttributeValueUpdate(AttributeValue.builder()
                .m(attributeValueMap).build()) : null;
    }

    protected AttributeValue computeIfNotNull(final Map<String, AttributeValue> value) {
        return value != null ? AttributeValue.builder()
                .m(value).build() : null;
    }

    protected AttributeValue computeIfNotNull(final Boolean value) {
        return value != null ? AttributeValue.builder()
                .bool(value).build() : null;
    }

    protected AttributeValue computeIfNotNull(final String value) {
        return !StringUtils.isEmpty(value) ? AttributeValue.builder()
                .s(value).build() : null;
    }

    protected AttributeValue computeIfNotNull(final Number value) {
        return value != null ? AttributeValue.builder()
                .n(value.toString()).build() : null;
    }

    private AttributeValueUpdate convertToAttributeValueUpdate(final AttributeValue attributeValue) {
        return AttributeValueUpdate.builder().value(attributeValue).build();
    }

    protected abstract Map<String, AttributeValueUpdate> mapItemSaveAttributes(final T entity);

    protected abstract Map<String, AttributeValueUpdate> mapItemUpdateAttributes(final T entity);

    protected abstract Map<String, AttributeValue> mapItemIdAttribute(final I entityId);

    protected abstract Map<String, AttributeValue> mapItemEntityIdAttribute(final T entity);

    protected abstract T mapAttributesToEntity(final Map<String, AttributeValue> responseAttributes);
}
