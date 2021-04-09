package com.example.todotasks.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import com.example.todotasks.R;
import com.example.todotasks.taskdb.TaskRoomDataBase;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Suite;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
@RunWith(JUnit4.class)
public class AddTasksActivityResultTest extends TestCase {
    @Rule public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);
    public ActivityTestRule<MainActivity> testResult= new ActivityTestRule<>(MainActivity.class);
@Before public void setUp(){
    Context context = ApplicationProvider.getApplicationContext();
    TaskRoomDataBase dataBase = Room.inMemoryDatabaseBuilder(context,TaskRoomDataBase.class).build();
   /* Intent result = new Intent();
    result.putExtra("boolean",false);
    Instrumentation.ActivityResult result1 = new Instrumentation.ActivityResult(Activity.RESULT_OK,result);

    intending(toPackage(MapsActivity.class.getName())).respondWith(result1);*/
}


   /* @Test public void MapsActivityResultIntent(){

        onView(withId(R.id.setLocationText)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.setLocationText)).check(matches(withText("Save Task")));
    }*/
    @Test public void mainActivityTest(){


        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.setLocationText)).perform(click());
        onView(withId(R.id.saveButton)).perform(click());

        onView(withId(R.id.setLocationText)).check(matches(withText("Save")));

    }

}
