package mongodb.demo.service;

import mongodb.demo.model.Book;
import mongodb.demo.repository.BookRepository;
import mongodb.demo.util.pagination.MongoPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public void add(Book book){
        bookRepository.save(book);
    }

    public void update(Book book){
        bookRepository.update(book);
    }

    public void deleteById(String id){
        bookRepository.removeById(id);
    }

    public int count(){
        return bookRepository.count();
    }

    public List<Book> getAll(MongoPage page){
        return bookRepository.findAll(page);
    }

    public Optional<Book> getById(String id){
        return bookRepository.findById(id);
    }
}