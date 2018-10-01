package mongodb.demo.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import mongodb.demo.manager.MongoManager;
import mongodb.demo.model.User;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Optional;
import static java.lang.String.format;

@Repository
public class UserRepository {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static final String COLLECTION = "user";

    @Autowired
    private MongoManager mongoManager;

    public Optional<User> findByEmail(String email){
        String json = "{'email' : '%s'}";
        MongoCollection<Document> collection = mongoManager.getCollection(COLLECTION);
        FindIterable<Document> documents = collection.find(Document.parse(format(json, email)));
        Document document = documents.first();
        User user = null;
        if(document != null){
            user = getUser(document);
        }
        return user == null ? Optional.empty() : Optional.of(user);
    }

    public void save(User user){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String json = "{'name' : '%s','surname' : '%s','email' : '%s'," +
                "'password' : '%s','age' : %d,'birthDate' : '%s'}";
        MongoCollection<Document> collection = mongoManager.getCollection(COLLECTION);
        String insertValue = format(json,user.getName(),user.getSurname(),
                user.getEmail(),user.getPassword(),user.getAge(),dateFormat.format(user.getBirthDate()));
        collection.insertOne(Document.parse(insertValue));
        LOGGER.info("user successfully saved");
    }

    public User findById(String id){
        String json = "{'_id' : ObjectId('%s')}";
        MongoCollection<Document> collection = mongoManager.getCollection(COLLECTION);
        FindIterable<Document> documents = collection.find(Document.parse(format(json, id)));
        Document document = documents.first();
        if(document != null){
            return getUser(document);
        }
        return null;
    }

    private User getUser(Document document){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return User
                    .builder()
                    .id(document.getObjectId("_id").toString())
                    .name(document.getString("name"))
                    .surname(document.getString("surname"))
                    .email(document.getString("email"))
                    .password(document.getString("password"))
                    .age(document.getInteger("age"))
                    .birthDate(dateFormat.parse(document.getString("birthDate")))
                    .build();
        }catch (Exception e){
            LOGGER.info("failed parse date format");
            return null;
        }

    }
}