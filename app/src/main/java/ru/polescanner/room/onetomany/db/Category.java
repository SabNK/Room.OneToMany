package ru.polescanner.room.onetomany.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "categories")
public class Category {
    @NonNull
    @PrimaryKey
    public
    String shortCode;
    String displayName;

    public Category() {}

    @Ignore
    public Category(String shortCode, String displayName) {
        this.shortCode = shortCode;
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return shortCode.equals(category.shortCode);
    }

    @Override
    public int hashCode() {
        return shortCode.hashCode();
    }
}
