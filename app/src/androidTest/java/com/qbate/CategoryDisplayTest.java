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
public class CategoryDisplayTest {

    @Rule
    public ActivityTestRule<CategoryDisplay> cdTestRule = new ActivityTestRule<CategoryDisplay>(CategoryDisplay.class);

    private CategoryDisplay cdContext = null;

    @Before
    public void setUp() throws Exception {
        cdContext = cdTestRule.getActivity();
    }

    @Test
    public void testLaunch1(){
        View v = cdContext.findViewById(R.id.category_list);
        assertNotNull(v);
        cdContext.finish();
    }

    @Test
    public void testLaunch2(){
        View v = cdContext.findViewById(R.id.my_toolbar);
        assertNotNull(v);
        cdContext.finish();
    }

    @After
    public void tearDown() throws Exception {
        cdContext = null;
    }
}