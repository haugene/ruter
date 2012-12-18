package no.ruter.app.android;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import no.ruter.app.android.fragment.ClosestStopFragment;
import no.ruter.app.android.fragment.RealTimeSectionFragment;
import no.ruter.app.android.fragment.RoutePlannerSectionFragment;
import no.ruter.app.service.ServiceFactory;


public class MainViewActivity extends FragmentActivity implements ActionBar.TabListener {

    private static String TAG = "ruter";

    private ActionBar actionBar;
    private ActionBar.TabListener tabListener;

    private ViewPager viewPager;

    // TODO: Use FragmentStatePagerAdapter instead if we are operating on collections
    private FragmentPagerAdapter fragmentPagerAdapter;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.main);

        fragmentPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        actionBar = getActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);


        setUpViewPager();
        setUpTabs();
        
        // Start looking for current location
        ServiceFactory.getRuterService().startLookingForNearbyLocations(getApplication());
    }

    private void setUpViewPager() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getActionBar().setSelectedNavigationItem(position);
            }
        });

    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new RealTimeSectionFragment();
                case 2:
                    return new RoutePlannerSectionFragment();
                default:
                    return new ClosestStopFragment();
            }
        }

        @Override
        public int getCount() {
            return 3; // TODO: Constant
        }
    }

    private void setUpTabs() {
        actionBar.addTab(actionBar.newTab().setText("Sanntid").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("I nærheten").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Planlegger").setTabListener(this));
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}

