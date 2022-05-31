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
import java.util.Map;

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

    public static class BooksMap {
        @Embedded(prefix = "c_")
        public Category category;
        @Embedded(prefix = "b_")
        public Book book;
        public Rating rating;
    }

    @Entity(tableName = "books_categories",
            primaryKeys = {"isbnBook", "isbnCategory"},
            indices = {@Index("isbnBook"), @Index("isbnCategory")},
            foreignKeys = { @ForeignKey(entity = Category.class,
                                        parentColumns = "isbn",
                                        childColumns = "isbnCategory",
                                        onDelete = ForeignKey.CASCADE),
                            @ForeignKey(entity = Book.class,
                                        parentColumns = "isbn",
                                        childColumns = "isbnBook",
                                        onDelete = ForeignKey.CASCADE)})
    static class BooksTable {
        @NonNull
        String isbnBook;
        @NonNull
        String isbnCategory;
        Rating rating;

        public BooksTable() {}

        @Ignore
        public BooksTable(@NonNull String isbnBook, @NonNull String isbnCategory, Rating rating) {
            this.isbnBook = isbnBook;
            this.isbnCategory = isbnCategory;
            this.rating = rating;
        }
    }
}

