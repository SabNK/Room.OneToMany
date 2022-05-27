package ru.polescanner.room.onetomany.db;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.List;

@androidx.room.Dao
public interface BookStore {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Book... books);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Book.BookCategoryJoin... joins);

    @Insert
    void addAll(List<Book.BookCategoryJoin> joins);

    default void add(Category category, Book... books) {
        /*Attempt to prevent adding an existing Item to the database -
        I managed with OnConflictStrategy

        List<Category> categoriesDb = getCategories();
        List<Book> booksDb = getBooks();
        if (!categoriesDb.contains(category))
            add(category);
        List<Book> booksToDb = new ArrayList<>();
        for (Book b : books)
            if (!booksDb.contains(b))
                booksToDb.add(b);
        add(booksToDb.toArray(new Book[0]));
        */
        add(category);
        add(books);
        List<Book.BookCategoryJoin> bcj = new ArrayList<>();
        for (Book b : books)
            bcj.add(new Book.BookCategoryJoin(b.isbn, category.shortCode));
        addAll(bcj);
    }

    @Transaction
    @Query("SELECT * FROM categories")
    List<Book.CategoryAndBooks> getAll();

    @Transaction
    @Query("SELECT * FROM categories WHERE shortCode = :id")
    Book.CategoryAndBooks getById(String id);

    @Query("SELECT * FROM books")
    List<Book> getBooks();

    @Query("SELECT * FROM categories")
    List<Category> getCategories();

    //The helper for prevent adding an existing Item
    default <T> List<T> aNotB(List<T> listA, List<T> listB) {
        List<T> result = new ArrayList(listA);
        result.removeAll(listB);
        return result;
    }

}
