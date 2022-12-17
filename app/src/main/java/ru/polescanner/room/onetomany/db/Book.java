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
public class Book extends EntityDb {
    @NonNull
    public String isbn;
    String title;

    public Book() {}

    @Ignore
    public Book(@NonNull String id, int version, @NonNull String isbn, String title) {
        super(id, version);
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
            primaryKeys = {"idBook", "isbnCategory"},
            indices = {@Index("idBook"), @Index("isbnCategory")},
            foreignKeys = { @ForeignKey(entity = Category.class,
                                        parentColumns = "isbn",
                                        childColumns = "isbnCategory",
                                        onDelete = ForeignKey.CASCADE),
                            @ForeignKey(entity = Book.class,
                                        parentColumns = "id",
                                        childColumns = "idBook",
                                        onDelete = ForeignKey.CASCADE)})
    static class BooksTable {
        @NonNull
        String idBook;
        @NonNull
        String isbnCategory;
        Rating rating;

        public BooksTable() {}

        @Ignore
        public BooksTable(@NonNull String idBook, @NonNull String isbnCategory, Rating rating) {
            this.idBook = idBook;
            this.isbnCategory = isbnCategory;
            this.rating = rating;
        }
    }
}

