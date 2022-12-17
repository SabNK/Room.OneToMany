package ru.polescanner.room.onetomany.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {  Category.class,
                        Book.class,
                        Book.BooksTable.class,
                        User.class,
                        User.UserBookCrossRef.class},
        version=1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract BookStore bookDbDao();

    public static AppDatabase getDbInstance(Context context) {
        if (INSTANCE == null) {
            //ToDo DBname to Settings
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                            AppDatabase.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();}
        return INSTANCE;
    }
}
