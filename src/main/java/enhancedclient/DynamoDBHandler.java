package enhancedclient;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.List;

public class DynamoDBHandler {
    private final AmazonDynamoDB client= AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build();
    private final DynamoDB dynamoDB=new DynamoDB(client);

    public void createTable(String tableName,String hashKey){
        try{
            List<AttributeDefinition> attributeDefinitionList=new ArrayList<>();
            attributeDefinitionList.add(new AttributeDefinition().withAttributeName(hashKey).withAttributeType(ScalarAttributeType.S));

            List<KeySchemaElement> keySchemaElements=new ArrayList<>();
            keySchemaElements.add(new KeySchemaElement().withAttributeName(hashKey).withKeyType(KeyType.HASH));

            CreateTableRequest request=new CreateTableRequest().withTableName(tableName).withKeySchema(keySchemaElements).withAttributeDefinitions(attributeDefinitionList).withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));

            System.out.println("Issuing CreateTable");
            Table table=dynamoDB.createTable(request);

            System.out.println("Waiting for table to be created this may take a while");

            table.waitForActive();

        } catch (Exception e) {
            System.out.println("Create table request failed");
            System.out.println(e.getMessage());
        }
    }

    public boolean doesTableExist(String tableName){
        Table table=dynamoDB.getTable(tableName);
        try{
            table.describe();
        }catch (ResourceNotFoundException e){
            return false;
        }
        return true;
    }
}
