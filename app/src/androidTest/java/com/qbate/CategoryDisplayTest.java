package com.qbate;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class CategoryDisplayTest {

    @Rule
    public ActivityTestRule<CategoryDisplay> cdTestRule = new ActivityTestRule<CategoryDisplay>(CategoryDisplay.class);

    private CategoryDisplay cdContext = null;
    //private Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(FirebaseLogin.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        cdContext = cdTestRule.getActivity();
    }

    @Test
    public void testLaunch1(){
        //cdContext.openOptionsMenu();
        //getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        /*getInstrumentation().invokeMenuActionSync(cdContext, R.id.sign_out, 0);
        Activity firebaseLoginActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 3000);
        assertEquals(true, getInstrumentation().checkMonitorHit(monitor, 1));
        //assertNotNull(firebaseLoginActivity);
        firebaseLoginActivity.finish();*/
        View v = cdContext.findViewById(R.id.category_list);
        assertNotNull(v);
        cdContext.finish();
    }

    @Test
    public void testLaunch2(){
        View v = cdContext.findViewById(R.id.my_toolbar);
        assertNotNull(v);
    }

    @After
    public void tearDown() throws Exception {
        cdContext = null;
    }
}