package enhancedclient;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Main {
    static String tableName="vbedi-test-demo-ingestion-table";
    public static void main(String[] args) {
        DynamoDBHandler ddbHandler=new DynamoDBHandler();
        EnhancedDynamoDBHandler enhancedDdbHandler=new EnhancedDynamoDBHandler();

        // Create table
        if(ddbHandler.doesTableExist(tableName)){
            System.out.println("Table already exists!");
        }
        else{
            ddbHandler.createTable(tableName,"id");
            System.out.println("Created Table with name: "+tableName);
        }

        Customer c=new Customer();

        c.setId("123");
        c.setEmail("vikram.bedi97@gmail.com");
        c.setName("Vikram");

        /* Nested field */
        Address address=new Address();
        address.setHome("J-78");
        address.setState("Delhi");
        address.setCountry("India");
        c.setAddress(address);

        LocalDate localDate=LocalDate.parse("2020-04-07");
        LocalDateTime localDateTime=localDate.atStartOfDay();
        Instant instant=localDateTime.toInstant(ZoneOffset.UTC);

        c.setRegDate(instant);

        // Create item
//        enhancedDdbHandler.putRecord(tableName,c);

        // Get Item
         Customer result= enhancedDdbHandler.getItem(tableName,"123");
        if(result!=null){
            System.out.println(result);
        }
        else{
            System.out.println("Customer with given key doesn't exist");
        }

        // Update item
       /* c.setName("Vikram Singh Bedi");
        Customer updatedCustomer=enhancedDdbHandler.updateItem(tableName,c);
        System.out.println(updatedCustomer);*/

        // Try to update the partition/hash key
/*        c.setId("124");
        Customer result= enhancedDdbHandler.updateItem(tableName,c);
        System.out.println(result);*/
        // ** Inserts a new record

        /*Customer deletedCustomer=enhancedDdbHandler.deleteItem(tableName,c);
        System.out.println(deletedCustomer);*/

        // Delete customer with key 124
        /*Customer deletedCustomer=enhancedDdbHandler.deleteItem(tableName,"124");
        System.out.println(deletedCustomer);*/

    }

}



