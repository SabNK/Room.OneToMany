package ru.polescanner.room.onetomany.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Junction;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Relation;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(    tableName = "books")
public class Book {
    @NonNull
    @PrimaryKey
    String isbn;
    String title;

    public Book() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }

    @Ignore
    public Book(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }

    @androidx.room.Dao
    public interface Dao{
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void add(Category category);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void add(Book... books);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void add(BookCategoryJoin... joins);

        @Insert
        void addAll(List<BookCategoryJoin> joins);

        default void add(Category category, Book... books){
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
            List<BookCategoryJoin> bcj = new ArrayList<>();
            for (Book b : books)
                bcj.add(new BookCategoryJoin(b.isbn, category.shortCode));
            addAll(bcj);
        }

        @Transaction
        @Query("SELECT * FROM categories")
        List<CategoryAndBooks> getAll();

        @Transaction
        @Query("SELECT * FROM categories WHERE shortCode = :id")
        CategoryAndBooks getById(String id);

        @Query("SELECT * FROM books")
        List<Book> getBooks();

        @Query("SELECT * FROM categories")
        List<Category> getCategories();

        default <T> List<T> aNotB(List<T> listA, List<T> listB) {

            List<T> result = new ArrayList(listA);
            result.removeAll(listB);

            return result;
        }

    }

    public static class CategoryAndBooks{
        @Embedded
        public
        Category category;
        @Relation(
                parentColumn = "shortCode",
                entityColumn = "isbn",
                associateBy = @Junction(    value = BookCategoryJoin.class,
                                            parentColumn = "shortCode",
                                            entityColumn = "isbn")
                )
        public List<Book> books;
    }

    @Entity(tableName = "books_categories",
            primaryKeys = {"isbn", "shortCode"},
            indices = {@Index("isbn"), @Index("shortCode")},
            foreignKeys = { @ForeignKey(entity = Category.class,
                                        parentColumns = "shortCode",
                                        childColumns = "shortCode",
                                        onDelete = ForeignKey.CASCADE),
                            @ForeignKey(entity = Book.class,
                                        parentColumns = "isbn",
                                        childColumns = "isbn",
                                        onDelete = ForeignKey.CASCADE)})
    static class BookCategoryJoin{
        @NonNull
        String isbn;
        @NonNull
        String shortCode;

        public BookCategoryJoin() {}

        @Ignore
        public BookCategoryJoin(@NonNull String isbn, @NonNull String shortCode) {
            this.isbn = isbn;
            this.shortCode = shortCode;
        }
    }
}

