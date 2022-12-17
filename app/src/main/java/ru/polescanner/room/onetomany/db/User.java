package ru.polescanner.room.onetomany.db;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

@Entity( tableName = "users")
public class User extends EntityDb{
    String name;
    @Embedded (prefix="fav_")
    public Book favourite;

    @Ignore
    public List<Book> taken;

    public User() {
    }

    @Ignore
    public User(@NonNull String id, int version, String name, Book favourite, List<Book> taken) {
        super(id, version);
        this.name = name;
        this.favourite = favourite;
        this.taken = taken;
    }

    @Entity(tableName = "user_book_cross_ref",
            primaryKeys = {"userId", "bookId"})
    public static class UserBookCrossRef{
        @NonNull
        String userId;
        @NonNull
        String bookId;

        public UserBookCrossRef() {
        }

        @Ignore
        public UserBookCrossRef(String userId, String bookId) {
            this.userId = userId;
            this.bookId = bookId;
        }
    }

    public static class WithBooks {
        @Embedded  User user;
        @Relation( parentColumn = "id", entityColumn = "id",
                   associateBy = @Junction(value = UserBookCrossRef.class,
                           parentColumn = "userId",
                           entityColumn = "bookId")
        )
        public List<Book> books;
    }
}
