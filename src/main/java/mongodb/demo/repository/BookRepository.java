package mongodb.demo.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import mongodb.demo.manager.MongoManager;
import mongodb.demo.model.Book;
import mongodb.demo.util.pagination.MongoPage;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Repository
public class BookRepository {

    private static final String COLLECTION = "book";

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MongoManager mongoManager;

    @Autowired
    private UserRepository userRepository;

    public void save(Book book){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String json = "{'title' : '%s','description' : '%s','price' : %d,'createdDate' : '%s','userId' : '%s'}";
        String insertValue = format(json,book.getTitle(),book.getDescription(),
                book.getPrice(),dateFormat.format(book.getCreatedDate()),book.getUser().getId());
        MongoCollection<Document> collection = mongoManager.getCollection(COLLECTION);
        collection.insertOne(Document.parse(insertValue));
        LOGGER.info("book successfully saved");
    }

    public void update(Book book){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String json = "{'title' : '%s','description' : '%s','price' : %d,'createdDate' : '%s','userId' : '%s'}";
        String updateValue = format(json,book.getTitle(),book.getDescription(),
                book.getPrice(),dateFormat.format(book.getCreatedDate()),book.getUser().getId());
        MongoCollection<Document> collection = mongoManager.getCollection(COLLECTION);
        collection.updateOne(Document.parse(format("{'_id' : ObjectId('%s')}",book.getId())),
                new Document("$set",Document.parse(updateValue)));
        LOGGER.info("post successfully updated");
    }

    public void removeById(String id){
        MongoCollection<Document> collection = mongoManager.getCollection(COLLECTION);
        collection.findOneAndDelete(Document.parse(format("{'_id' : ObjectId('%s')}",id)));
        LOGGER.info("book successfully deleted");
    }

    public List<Book> findAll(MongoPage page){
        List<Book> books = new ArrayList<>();
        MongoCollection<Document> collection = mongoManager.getCollection(COLLECTION);
        FindIterable<Document> documents = collection.find()
                .sort(Document.parse("{'createdDate' : -1}"))
                .limit(page.getSize())
                .skip(page.getSkip());
        for (Document document : documents) {
            books.add(getBook(document));
        }
        return books;
    }

    public int count(){
        return (int) mongoManager.getCollection(COLLECTION).countDocuments();
    }

    public Optional<Book> findById(String id){
        String json = "{'_id' : ObjectId('%s')}";
        MongoCollection<Document> collection = mongoManager.getCollection(COLLECTION);
        Document document = collection.find(Document.parse(format(json, id))).first();
        return Optional.of(getBook(document));
    }

    private Book getBook(Document document){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return Book
                    .builder()
                    .id(document.getObjectId("_id").toString())
                    .title(document.getString("title"))
                    .description(document.getString("description"))
                    .price(document.getInteger("price"))
                    .createdDate(dateFormat.parse(document.getString("createdDate")))
                    .user(userRepository.findById(document.getString("userId")))
                    .build();
        }catch (Exception e){
            LOGGER.info("failed parse created date");
            throw new RuntimeException(e);
        }
    }
}