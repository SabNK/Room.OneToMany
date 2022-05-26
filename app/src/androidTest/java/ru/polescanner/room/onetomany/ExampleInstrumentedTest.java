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
        String shortCodeOne = "stuff";
        String shortCodeTwo = "love";
        Category categoryOne = new Category(shortCodeOne, "Books about stuff");
        Category categoryTwo = new Category(shortCodeTwo, "Books about love");
        Book bookOne = new Book("333", "Feed");
        Book bookTwo = new Book("555", "Dies the Fire");
        Book bookThree = new Book("777", "Love me tender");
        sut.add(categoryOne, bookOne, bookTwo);
        sut.add(categoryTwo, bookOne, bookThree);

        List<Book.CategoryAndBooks> all = sut.getAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.get(0).category).isEqualTo(categoryOne);
        assertThat(all.get(1).books).containsExactly(bookOne, bookThree);

        Book.CategoryAndBooks one = sut.getById(categoryTwo.shortCode);
        assertThat(one.category).isEqualTo(categoryTwo);
        assertThat(one.books).containsExactly(bookOne, bookThree);
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ru.polescanner.room.onetomany", appContext.getPackageName());
    }
}