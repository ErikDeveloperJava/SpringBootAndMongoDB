package mongodb.demo.manager;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Component;

@Component
public class MongoManager {

    private MongoDatabase database;

    public MongoManager(){
        database = new  MongoClient("localhost",27017)
                .getDatabase("new_mongo_db");
    }

    public MongoCollection<Document> getCollection(String name){
        return database.getCollection(name);
    }
}