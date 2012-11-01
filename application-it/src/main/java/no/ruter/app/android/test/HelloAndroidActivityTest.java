package no.ruter.app.android.test;

import android.test.ActivityInstrumentationTestCase2;
import no.ruter.app.android.*;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<MainViewActivity> {

    public HelloAndroidActivityTest() {
        super(MainViewActivity.class);
    }

    public void testActivity() {
        MainViewActivity activity = getActivity();
        assertNotNull(activity);
    }
}

