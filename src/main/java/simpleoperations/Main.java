package simpleoperations;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.*;

public class Main {

    static AmazonDynamoDB client= AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build();
    static DynamoDB dynamoDB=new DynamoDB(client);
    static String tableName="vbedi-test-demo-ingestion-table";

    public static void main(String[] args) {

        if(doesTableExist(tableName)){
            System.out.println("Table already exists");
        }else{
            createTable(tableName);
        }

        Table table=dynamoDB.getTable(tableName);

        Map<String,List<String> > reviews=new HashMap<>();

        List<String> fiveStarReviews=List.of("Review1","Review2");
        List<String> oneStarReviews=List.of("Review3","Review4");

        reviews.put("FiveStar",fiveStarReviews);
        reviews.put("OneStar",oneStarReviews);

        Item item=new Item();
        item
                .withPrimaryKey("Id","123")
                .withMap("Reviews",reviews);


        PutItemOutcome outcome=table.putItem(item);
        System.out.println(outcome.toString());

    }

    static void createTable(String tableName){
        try{
            List<AttributeDefinition> attributeDefinitionList=new ArrayList<>();
            attributeDefinitionList.add(new AttributeDefinition().withAttributeName("Id").withAttributeType(ScalarAttributeType.S));

            List<KeySchemaElement> keySchemaElements=new ArrayList<>();
            keySchemaElements.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));

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

    static void getTableInformation(String tableName){
        System.out.println("Describing " + tableName);

        TableDescription tableDescription = dynamoDB.getTable(tableName).describe();
        System.out.format(
                "Name: %s:\n" + "Status: %s \n" + "Provisioned Throughput (read capacity units/sec): %d \n"
                        + "Provisioned Throughput (write capacity units/sec): %d \n",
                tableDescription.getTableName(), tableDescription.getTableStatus(),
                tableDescription.getProvisionedThroughput().getReadCapacityUnits(),
                tableDescription.getProvisionedThroughput().getWriteCapacityUnits());
    }

    static boolean doesTableExist(String tableName){
        try{
            getTableInformation(tableName);
            return true;
        }catch (ResourceNotFoundException e){
            return false;
        }
    }
}
