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

import java.util.Arrays;
import java.util.List;

import ru.polescanner.room.onetomany.db.AppDatabase;
import ru.polescanner.room.onetomany.db.Book;
import ru.polescanner.room.onetomany.db.BookStore;
import ru.polescanner.room.onetomany.db.Category;
import ru.polescanner.room.onetomany.db.Rating;
import ru.polescanner.room.onetomany.db.User;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private AppDatabase mDb;
    private BookStore sut;
    Category categoryOne;
    Category categoryTwo;
    Book bookOne;
    Book bookTwo;
    Book bookThree;

    @Before
    public void createDb(){
        Context context =  InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        sut = mDb.bookDbDao();
    }

    @Before
    public void initiate(){
        String shortCodeOne = "stuff";
        String shortCodeTwo = "love";
        categoryOne = new Category(shortCodeOne, "Books about stuff");
        categoryTwo = new Category(shortCodeTwo, "Books about love");
        bookOne = new Book("1", 0, "333", "Feed");
        bookTwo = new Book("2", 0, "555", "Dies the Fire");
        bookThree = new Book("3", 0, "777", "Love me tender");
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void roomTestMap(){

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

        Category one = sut.getById(categoryTwo.isbn);
        assertThat(one).isEqualTo(categoryTwo);
        assertThat(one.books.values()).containsExactly(bookOne, bookThree);
        assertThat(one.books.keySet()).containsExactly(Rating.A, Rating.C);
    }

    @Test
    public void roomTestManyToMany() {
        User u1 = new User("10", 0 , "Nick", bookOne, Arrays.asList(bookOne, bookThree));
        User u2 = new User("11", 0 , "Petr", bookTwo, Arrays.asList(bookTwo));

        sut.addUsers(u2);
        List<User> users = sut.getUsersSimple();
        assertThat(users).hasSize(1);

        sut.addUsers(u1);
        users = sut.getUsersSimple();
        assertThat(users).hasSize(2);
        assertThat(users).containsExactly(u1, u2);

        List<User.WithBooks> uWithBooks = sut.getUsersWithBooks();
        assertThat(uWithBooks).hasSize(2);


        users = sut.getUsers();
        assertThat(users).hasSize(2);
        assertThat(users).containsExactly(u1, u2);
        User validateUser = new User("12", 0 , "", bookThree, Arrays.asList(bookOne));
        for (User u : users) {
            if (u.equals(u1))
                validateUser = u;
        }
        assertThat(validateUser.taken).contains(bookOne);
        assertThat(validateUser.taken).contains(bookThree);
        assertThat(validateUser.favourite.isbn).isEqualTo(bookOne.isbn);
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ru.polescanner.room.onetomany", appContext.getPackageName());
    }
}