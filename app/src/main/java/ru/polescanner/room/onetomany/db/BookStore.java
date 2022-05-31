package ru.polescanner.room.onetomany.db;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@androidx.room.Dao
public interface BookStore {
    //ToDo Rethink OnConflictStrategy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPojoCategory(Category... category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Book... books);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Book.BooksTable... joins);

    @Transaction
    default void add(Category... category) {
        List<Category> categories = Arrays.asList(category);
        List<Book.BooksTable> bcj = new ArrayList<>();
        for (Category cat : categories)
            if (cat.books != null) {
                Book[] booksToAdd =  cat.books.values().toArray(new Book[0]);
                add(booksToAdd);
                for (Map.Entry<Rating, Book> entry : cat.books.entrySet()) {
                    Rating r = entry.getKey();
                    Book b = entry.getValue();
                    bcj.add(new Book.BooksTable(b.isbn, cat.isbn, r));
                }
            }
        addPojoCategory(category);
        add(bcj.toArray(new Book.BooksTable[0]));
    }


    @Query("SELECT isbnBook as b_isbn, isbnCategory as c_isbn, displayName as c_displayName, title as b_title, rating " +
            "FROM categories JOIN books_categories ON categories.isbn = books_categories.isbnCategory " +
                                    "JOIN books ON books.isbn = books_categories.isbnBook")
    List<Book.BooksMap> getAllCategoryAndBook();

    //ToDo Rethink of many objects-copy in the system.
    default List<Category> getAll(){
        List<Category> categories = getAllPojoCategories();
        //ToDo move to Constructor
        /*for (Category category : categories)
            category.books = new HashMap<>();*/
        List<Book.BooksMap> cabs = getAllCategoryAndBook();
        for (Book.BooksMap cab : cabs) {
            int j = categories.indexOf(cab.category);
            categories.get(j).books.put(cab.rating, cab.book);
        }
        return categories;
    }

    default Category getById(String id){
        Category category = getByIdPojoCategory(id);
        //category.books = new HashMap<>();
        List<Book.BooksMap> cabs = getByIdCategoryAndBook(id);
        for (Book.BooksMap cab : cabs)
            category.books.put(cab.rating, cab.book);
        return category;
    }

    @Query("SELECT isbnBook as b_isbn, isbnCategory as c_isbn, displayName as c_displayName, title as b_title, rating " +
            "FROM categories JOIN books_categories ON categories.isbn = books_categories.isbnCategory " +
                                    "JOIN books ON books.isbn = books_categories.isbnBook " +
            "WHERE categories.isbn = :id")
    List<Book.BooksMap> getByIdCategoryAndBook(String id);



    @Query("SELECT * FROM books")
    List<Book> getBooks();

    @Query("SELECT * FROM categories")
    List<Category> getAllPojoCategories();

    @Query("SELECT * FROM categories WHERE isbn = :id")
    Category getByIdPojoCategory(String id);


}
