package ru.polescanner.room.onetomany;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static com.google.common.truth.Truth.assertThat;

import java.util.List;

import ru.polescanner.room.onetomany.db.AppDatabase;
import ru.polescanner.room.onetomany.db.Book;
import ru.polescanner.room.onetomany.db.Category;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private AppDatabase mDb;
    private Book.Dao sut;

    @Before
    public void createDb(){
        Context context =  InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        sut = mDb.bookDbDao();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void roomTest(){
        String shortCode = "stuff";
        Category category = new Category(shortCode, "Books about stuff");
        Book bookOne = new Book("333", "Feed", shortCode);
        Book bookTwo = new Book("999", "Dies the Fire", shortCode);
        sut.addCategory(category);
        sut.addBooks(bookOne, bookTwo);

        List<Book.CategoryAndBooks> all = sut.getAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).category).isEqualTo(category);
        assertThat(all.get(0).books).containsExactly(bookOne, bookTwo);

        Book.CategoryAndBooks one = sut.getById(category.shortCode);
        assertThat(one.category).isEqualTo(category);
        assertThat(one.books).containsExactly(bookOne, bookTwo);
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ru.polescanner.room.onetomany", appContext.getPackageName());
    }
}