package ru.polescanner.room.onetomany.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity (tableName = "categories")
public class Category {
    @NonNull
    @PrimaryKey
    public String isbn;
    String displayName;

    @Ignore
    public Map<Rating, Book> books;

    public Category() {
        books = new HashMap<>();
    }

    @Ignore
    public Category(@NonNull String isbn, String displayName) {
        this.isbn = isbn;
        this.displayName = displayName;
        this.books = new HashMap<>();
    }

    @Ignore
    public Category(String isbn, String displayName, Map<Rating, Book> books) {
        this.isbn = isbn;
        this.displayName = displayName;
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return isbn.equals(category.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }
}
