package enhancedclient;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class EnhancedDynamoDBHandler {
    private final Region region=Region.US_WEST_2;
    private final DynamoDbClient ddb=DynamoDbClient.builder().region(region).build();
    private final DynamoDbEnhancedClient enhancedClient=DynamoDbEnhancedClient.builder().dynamoDbClient(ddb).build();

    public void putRecord(String tableName,Customer custRecord){
        try{
            DynamoDbTable<Customer> custTable=enhancedClient.table(tableName, TableSchema.fromBean(Customer.class));
            custTable.putItem(custRecord);
        }
        catch (DynamoDbException de){
            System.out.println(de.getMessage());
        }
    }
    public Customer getItem(String tableName,String hashKeyValue){
        try{
            DynamoDbTable<Customer> custTable=enhancedClient.table(tableName, TableSchema.fromBean(Customer.class));

            Key key=Key.builder().partitionValue(hashKeyValue).build();

            Customer result=custTable.getItem(r->r.key(key));

            return result;
        }catch (DynamoDbException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

   public Customer updateItem(String tableName,Customer updatedCustomer){
        try{
            DynamoDbTable<Customer> custTable=enhancedClient.table(tableName,TableSchema.fromBean(Customer.class));

            Customer result=custTable.updateItem(updatedCustomer);

            return result;

        }catch (DynamoDbException e){
            System.out.println(e.getMessage());
            return null;
        }
   }

   public Customer deleteItem(String tableName,Customer customer){
        try{
            DynamoDbTable<Customer> custTable=enhancedClient.table(tableName,TableSchema.fromBean(Customer.class));
            Customer deletedCustomer=custTable.deleteItem(customer);
            return deletedCustomer;
        }catch (DynamoDbException e){
            System.out.println(e.getMessage());
            return null;
        }
   }

   public Customer deleteItem(String tableName,String hashKeyValue){
        try{
            DynamoDbTable<Customer> custTable=enhancedClient.table(tableName,TableSchema.fromBean(Customer.class));
            Key key= Key.builder().partitionValue(hashKeyValue).build();

            Customer deletedCustomer=custTable.deleteItem(r->r.key(key));

            return deletedCustomer;

        }catch (DynamoDbException e){
            System.out.println(e.getMessage());
            return null;
        }
   }
}
