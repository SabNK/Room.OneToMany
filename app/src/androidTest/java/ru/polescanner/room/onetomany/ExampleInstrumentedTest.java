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
import ru.polescanner.room.onetomany.db.BookStore;
import ru.polescanner.room.onetomany.db.Category;
import ru.polescanner.room.onetomany.db.Rating;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private AppDatabase mDb;
    private BookStore sut;

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
        categoryOne.books.put(Rating.A, bookOne);
        categoryOne.books.put(Rating.B, bookTwo);

        categoryTwo.books.put(Rating.A, bookOne);
        categoryTwo.books.put(Rating.C, bookThree);
        sut.add(categoryOne, categoryTwo);

        List<Category> all = sut.getAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.get(0)).isEqualTo(categoryOne);
        assertThat(all.get(1).books.values()).containsExactly(bookOne, bookThree);
        assertThat(all.get(0).books.keySet()).containsExactly(Rating.A, Rating.B);

        Category one = sut.getById(categoryTwo.shortCode);
        assertThat(one).isEqualTo(categoryTwo);
        assertThat(one.books.values()).containsExactly(bookOne, bookThree);
        assertThat(one.books.keySet()).containsExactly(Rating.A, Rating.C);
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ru.polescanner.room.onetomany", appContext.getPackageName());
    }
}