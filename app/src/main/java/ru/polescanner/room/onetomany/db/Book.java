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

    public static class CategoryAndBooks{
        @Embedded
        public Category category;
        @Relation(
                parentColumn = "shortCode",
                entityColumn = "isbn",
                associateBy = @Junction(    value = BookCategoryJoin.class,
                                            parentColumn = "shortCode",
                                            entityColumn = "isbn")
                )
        public Book book;
        @Relation(
                parentColumn = "shortCode",
                entityColumn = "rating",
                associateBy = @Junction(value = BookCategoryJoin.class,
                parentColumn = "shortCode",
                entityColumn = "rating"))
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
    static class BookCategoryJoin{
        @NonNull
        String isbn;
        @NonNull
        String shortCode;
        Rating rating;

        public BookCategoryJoin() {}

        @Ignore
        public BookCategoryJoin(@NonNull String isbn, @NonNull String shortCode, Rating rating) {
            this.isbn = isbn;
            this.shortCode = shortCode;
            this.rating = rating;
        }
    }
}

