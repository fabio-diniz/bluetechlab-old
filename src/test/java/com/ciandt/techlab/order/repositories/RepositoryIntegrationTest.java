package com.ciandt.techlab.order.repositories;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;

public abstract class RepositoryIntegrationTest {

    private static DynamoDbClient dynamoDbClient;
    private static DynamoDBProxyServer dynamoDBProxyServer;

    private static final String[] AMAZON_PROXY_SERVER_ARGS = {"-inMemory", "-port", "8150"};

    private static final String AMAZON_DB_PROXY_SERVER_URI = "http://localhost:8150";
    private static final String SQLITE_LIBRARY_PATH = "sqlite4java.library.path";
    private static final String NATIVE_LIBRARY_PATH = "native-libs";

    private static final String ORDERS_TABLE_NAME = "orders";
    private static final String ORDERS_TABLE_ID = "id";

    @BeforeClass
    public static void setupServer() throws Exception {
        System.setProperty(SQLITE_LIBRARY_PATH, NATIVE_LIBRARY_PATH);
        dynamoDBProxyServer = ServerRunner.
                createServerFromCommandLineArgs(AMAZON_PROXY_SERVER_ARGS);
        dynamoDBProxyServer.start();
        dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(URI.create(AMAZON_DB_PROXY_SERVER_URI))
                .build();
        setupTables();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        dynamoDBProxyServer.stop();
    }

    private static void setupTables() {
        final CreateTableRequest createTableRequest = CreateTableRequest.builder()
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeType(ScalarAttributeType.S)
                        .attributeName(ORDERS_TABLE_ID)
                        .build())
                .keySchema(KeySchemaElement.builder()
                        .attributeName(ORDERS_TABLE_ID)
                        .keyType(KeyType.HASH)
                        .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .writeCapacityUnits(10L)
                        .readCapacityUnits(10L)
                        .build())
                .tableName(ORDERS_TABLE_NAME)
                .build();

        dynamoDbClient.createTable(createTableRequest);
    }
}
