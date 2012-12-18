package no.ruter.app.android.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import no.ruter.app.android.R;
import no.ruter.app.android.adapter.RealTimeDataAdapter;
import no.ruter.app.domain.Platform;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;
import no.ruter.app.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying real time data
 *
 * @author daniel
 *
 */
public class RealTimeSectionFragment extends Fragment {

    private View rootView;

    // Auto complete threshold and search delay
    private static final int SEARCH_THRESHOLD = 3;
    private static final long SEARCH_DELAY = 500;

    // TAG for logging
    private static final String TAG = "RealTimeSectionFragment";

    private RealTimeLocation selectedLocation;

    // Used when searching in the AutoCompleteTextView
    private GetRealTimeLocationAsyncTask getRealTimeLocationAsyncTask;

    // Used when selecting an item or searching on a location from the AutoCompleteTextView
    private GetRealTimeDataAsyncTask getRealTimeDataAsyncTask;

    private ArrayAdapter<RealTimeLocation> realTimeAutoCompleteAdapter;

    private ArrayAdapter<RealTimeLocation> selectStationAdapter;
    private RealTimeDataAdapter realTimeDataAdapter;
    private AutoCompleteTextView realtimeAutoCompleteTextView;

    private ListView realTimeResultsListView;
    private ListView selectStationListView;

    private List<Platform> realTimeData;
    private List<RealTimeLocation> realTimeLocations;
    // Need separate list because another adapter uses it
    private List<RealTimeLocation> selectRealTimeLocations;

    private ProgressBar progressBar;
    private ProgressBar autoCompleteProgressBar;

    private long lastUserInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        rootView = inflater.inflate(R.layout.fragment_section_realtime, container, false);
        Bundle args = getArguments();

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.GONE);

        autoCompleteProgressBar = (ProgressBar) rootView.findViewById(R.id.autoCompleteProgressBar);
        autoCompleteProgressBar.setVisibility(ProgressBar.INVISIBLE);

        setUpViews();

        return rootView;
    }

    private void setUpViews() {
        setUpResultListView();
        setUpAutoCompleteTextView();
//        setUpSelectStationListView();
    }

//    private void setUpSelectStationListView() {
//        selectStationListView = (ListView) rootView.findViewById(R.id.selectStationListView);
//
//        selectRealTimeLocations = new ArrayList<RealTimeLocation>();
//
//        selectStationAdapter = new ArrayAdapter<RealTimeLocation>(getActivity(), android.R.layout.simple_list_item_1, selectRealTimeLocations);  
//        selectStationAdapter.setNotifyOnChange(true);
//        selectStationListView.setAdapter(selectStationAdapter);
//
//        selectStationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
//                selectedLocation = (RealTimeLocation) parent.getItemAtPosition(position);
//                selectRealTimeLocations.clear();
//                realTimeData = getRealTimeData(selectedLocation.getId());
//
//                // TODO: Should not have to do this. Da fuk?
//                realTimeListViewAdapter = new ArrayAdapter<RealTimeData>(getActivity(), android.R.layout.simple_list_item_1, realTimeData);
//                realTimeResultsListView.setAdapter(realTimeListViewAdapter);
//            }
//        });
//    }

    private void setUpResultListView() {
        realTimeResultsListView = (ListView) rootView.findViewById(R.id.listView1);

        realTimeData = new ArrayList<Platform>();

        realTimeDataAdapter = new RealTimeDataAdapter(getActivity(), R.layout.listview_realtime_data, realTimeData);
        realTimeDataAdapter.setNotifyOnChange(true);
        realTimeResultsListView.setAdapter(realTimeDataAdapter);
    }

    private void setUpAutoCompleteTextView() {
        realtimeAutoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.realtimeAutoCompleteView);
        realtimeAutoCompleteTextView.setThreshold(SEARCH_THRESHOLD);

        realTimeLocations = new ArrayList<RealTimeLocation>();

        realTimeAutoCompleteAdapter = new ArrayAdapter<RealTimeLocation>(getActivity(), android.R.layout.simple_list_item_1, realTimeLocations);
        realTimeAutoCompleteAdapter.setNotifyOnChange(true);
        realtimeAutoCompleteTextView.setAdapter(realTimeAutoCompleteAdapter);

        realtimeAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;

                // Setup the keyboard search key
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    // If we have a selectedLocation do a search on that ID
                    // If not we have to do a places search on the string
                    if (selectedLocation != null) {
                        selectRealTimeLocations.clear();
                        realTimeData = getRealTimeDataByPlatform(selectedLocation.getId());
                    }
                    else {
                        // TODO: Exception handling
                        realTimeLocations.clear();
                        try {
                            realTimeLocations.addAll(ServiceFactory.getRuterService().findRealTimeLocations(textView.getText().toString()));
                        } catch (RepositoryException e) {
                            // TODO: Display some sort of connection error?
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        if (realTimeLocations.size() == 0) {
                            // TODO: Inform user of no match
                        } else if (realTimeLocations.size() == 1) {
                            selectRealTimeLocations.clear();
                            realTimeData.clear();
                            realTimeData.addAll(getRealTimeDataByPlatform(realTimeLocations.get(0).getId()));
                        } else {
                            realTimeData.clear();
                            selectRealTimeLocations.addAll(realTimeLocations);
                        }
                    }
//                    realTimeListViewAdapter.clear();
//                    realTimeListViewAdapter.addAll(realTimeData);

                    handled = true;
                }
                return handled;
            }
        });


        // Clicking on an item in the auto complete list
        realtimeAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = (RealTimeLocation) parent.getItemAtPosition(position);
                //realTimeData = getRealTimeData(selectedLocation.getId());

                // Cancel the old task
                if (getRealTimeDataAsyncTask != null && getRealTimeDataAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                    Log.d(TAG, "Cancelling getRealTimeDataAsyncTask");
                    getRealTimeDataAsyncTask.cancel(true);
                }

                realTimeAutoCompleteAdapter.clear();
                realTimeDataAdapter.clear();

                // Hide the keyboard on select
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getView().getContext().INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                // AsyncTask can only be used once, so have to make a new one each time
                getRealTimeDataAsyncTask = new GetRealTimeDataAsyncTask();
                getRealTimeDataAsyncTask.execute(selectedLocation.getId().toString());
            }
        });

        realtimeAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Clearing the location that might have been set by realTimeAutoComplete.onItemClick
                selectedLocation = null;

                // Cancels an already running task
                if (getRealTimeLocationAsyncTask != null && getRealTimeLocationAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                    Log.d(TAG, "Cancelling getRealTimeLocationAsyncTask");
                    getRealTimeLocationAsyncTask.cancel(true);
                }

                // Hide the progress bar when under search threshold
                if(charSequence.toString().length() < SEARCH_THRESHOLD) {
                    autoCompleteProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }

                if (charSequence.toString().length() >= SEARCH_THRESHOLD) {
                    // AsyncTask can only be used once, so have to make a new one each time
                    getRealTimeLocationAsyncTask = new GetRealTimeLocationAsyncTask();

                    getRealTimeLocationAsyncTask.execute(new String[]{charSequence.toString()});
                }
            }

            public void afterTextChanged(Editable editable) {
            }
        });

    }

    /**
     * Fetches a list of RealTimeLocations from the service and updates the realTimeAutoCompleteAdapter with the data.
     * Shows a progress bar while searching
     */
    private class GetRealTimeLocationAsyncTask extends AsyncTask<String, Void, List<RealTimeLocation>> {

        @Override
        protected List<RealTimeLocation> doInBackground(String... location) {
            try {
                try {
                    Thread.sleep(SEARCH_DELAY);
                } catch (InterruptedException e) {
                    Log.d(TAG, "Thread interrupted");
                }

                // Only perform search if the AsyncTask is not cancelled
                if(!isCancelled()) {
                    Log.d(TAG, "Performing search");
                    realTimeLocations = ServiceFactory.getRuterService().findRealTimeLocations(location[0]);
                }
            } catch (RepositoryException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return realTimeLocations;
        }

        @Override
        protected void onPreExecute() {
            autoCompleteProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

        // Need to call notifyDataSetChanged on the UI thread
        @Override
        protected void onPostExecute(List<RealTimeLocation> result) {
            realTimeAutoCompleteAdapter = new ArrayAdapter<RealTimeLocation>(getActivity(), android.R.layout.simple_list_item_1, result);
            realtimeAutoCompleteTextView.setAdapter(realTimeAutoCompleteAdapter);
            realtimeAutoCompleteTextView.showDropDown();
            autoCompleteProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }


    /**
     * Gets a list of platforms with data for the selected location and updates the RealTimeDataAdapter
     * Shows a progress bar while searching
     */
    private class GetRealTimeDataAsyncTask extends AsyncTask<String, Void, List<Platform>> {

        @Override
        protected List<Platform> doInBackground(String... id) {
            realTimeData.clear();
            try {
                realTimeData.addAll(ServiceFactory.getRuterService().getRealTimeDataByPlatform(Integer.parseInt(id[0])));
            } catch (RepositoryException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        // Need to call notifyDataSetChanged on the UI thread
        @Override
        protected void onPostExecute(List<Platform> result) {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            realTimeDataAdapter.notifyDataSetChanged();
            // Data is successfully fetched, clear the input field
            clearInput();
        }
    }

    private List<Platform> getRealTimeDataByPlatform(Integer id) {
        try {
            realTimeData = ServiceFactory.getRuterService().getRealTimeDataByPlatform(id);
            realTimeDataAdapter = new RealTimeDataAdapter(getActivity(), R.layout.listview_realtime_data, realTimeData);
            realTimeDataAdapter.setNotifyOnChange(true);
            realTimeResultsListView.setAdapter(realTimeDataAdapter);
        } catch (RepositoryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return realTimeData;
    }

    // Clears the user input
    private void clearInput() {
        realtimeAutoCompleteTextView.setText("");
    }
}
