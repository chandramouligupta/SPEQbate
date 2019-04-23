package com.qbate;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class FirebaseLoginTest {
    @Rule
    public ActivityTestRule<FirebaseLogin> fblTestRule =  new ActivityTestRule<FirebaseLogin>(FirebaseLogin.class);

    private FirebaseLogin flContext = null;

    @Before
    public void setUp() throws Exception {
        flContext = fblTestRule.getActivity();
    }

    @Test
    public void testLaunch1(){
        View v = flContext.findViewById(R.id.google_login_button);
        assertNotNull(v);
        flContext.finish();
    }

    @Test
    public void testLaunch2(){
        View v = flContext.findViewById(R.id.firebase_logo);
        assertNotNull(v);
        flContext.finish();
    }

    @After
    public void tearDown() throws Exception {
        flContext = null;
    }
}