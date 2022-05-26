package ru.polescanner.room.onetomany.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Relation;
import androidx.room.Transaction;

import java.util.List;

@Entity(    tableName = "books",
            foreignKeys = {@ForeignKey( entity = Category.class,
                                        parentColumns = "shortCode",
                                        childColumns = "categoryShortCode",
                                        onDelete = ForeignKey.CASCADE
                                      )}
        )
public class Book {
    @NonNull
    @PrimaryKey
    String isbn;
    String title;
    @ColumnInfo(index = true)
    String categoryShortCode;

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
    public Book(String isbn, String title, String categoryShortCode) {
        this.isbn = isbn;
        this.title = title;
        this.categoryShortCode = categoryShortCode;


    }

    @androidx.room.Dao
    public interface Dao{
        @Insert
        void addCategory(Category category);

        @Insert
        void addBooks(Book... books);

        @Transaction
        @Query("SELECT * FROM categories")
        List<CategoryAndBooks> getAll();

        @Transaction
        @Query("SELECT * FROM categories WHERE shortCode = :id")
        CategoryAndBooks getById(String id);

    }

    public static class CategoryAndBooks{
        @Embedded
        public
        Category category;
        @Relation(
                parentColumn = "shortCode",
                entityColumn = "categoryShortCode")
        public
        List<Book> books;
    }
}

