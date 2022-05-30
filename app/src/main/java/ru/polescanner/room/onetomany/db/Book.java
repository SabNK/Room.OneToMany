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
        @Embedded
        public Category category;
        @Embedded
        public Book book;
        public Rating rating;
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
    static class BooksTable {
        @NonNull
        String isbn;
        @NonNull
        String shortCode;
        Rating rating;

        public BooksTable() {}

        @Ignore
        public BooksTable(@NonNull String isbn, @NonNull String shortCode, Rating rating) {
            this.isbn = isbn;
            this.shortCode = shortCode;
            this.rating = rating;
        }
    }
}

