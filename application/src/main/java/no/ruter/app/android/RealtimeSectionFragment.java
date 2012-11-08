package no.ruter.app.android;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class RealtimeSectionFragment extends Fragment {

    private View rootView;

    private final static int SEARCH_THRESHOLD = 3;

    private RealTimeLocation selectedLocation;
    private List<RealTimeLocation> realTimeLocations;

    private GetRealTimeLocationAsyncTask asyncTask;

    private ArrayAdapter<RealTimeLocation> realTimeAutoCompleteAdapter;
    private ArrayAdapter<RealTimeData> realTimeListViewAdapter;

    private AutoCompleteTextView realtimeAutoComplete;

    private ListView realTimeResultsListView;
    private List<RealTimeData> realTimeData;

    private ProgressBar progressBar;
    private ProgressBar autoCompleteProgressBar;

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

        setUpResultListView();
        setUpAutoCompleteTextViews();

        return rootView;
    }

    private void setUpResultListView() {
        // TODO: Maybe this should be an ExpandableListView?
        ListView listView = (ListView) rootView.findViewById(R.id.listView1);

        realTimeData = new ArrayList<RealTimeData>();

        realTimeListViewAdapter = new ArrayAdapter<RealTimeData>(getActivity(), android.R.layout.simple_list_item_1, realTimeData);
        realTimeListViewAdapter.setNotifyOnChange(true);
        listView.setAdapter(realTimeListViewAdapter);
    }

    private void setUpAutoCompleteTextViews() {
        realtimeAutoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.realtimeAutoCompleteView);
        realtimeAutoComplete.setThreshold(0); // TODO: Set to SEARCH_THRESHOLD - or maybe keep at 0, but start pulling from the API at 3?

        realtimeAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    // If we have a selectedLocation do a search on that ID
                    // If not we have to do a places search on the string
                    if(selectedLocation != null) {
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        realTimeData = getRealTimeData(selectedLocation.getId());
                        progressBar.setVisibility(ProgressBar.GONE);
                    }
                    realTimeListViewAdapter.clear();
                    realTimeListViewAdapter.addAll(realTimeData);
                    for (RealTimeData data : realTimeData) {
                        System.out.println(data.getDestination());

                    }


                    handled = true;
                }
                return handled;
            }
        });

        realTimeLocations = new ArrayList<RealTimeLocation>();

        realTimeAutoCompleteAdapter = new ArrayAdapter<RealTimeLocation>(getActivity(), android.R.layout.simple_list_item_1, realTimeLocations);
        realTimeAutoCompleteAdapter.setNotifyOnChange(true);
        realtimeAutoComplete.setAdapter(realTimeAutoCompleteAdapter);

        realtimeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = (RealTimeLocation) parent.getItemAtPosition(position); // TODO: Index out of bounds issue - 08.11.12 - Fixed by clearing the adapter?
            }
        });

        realtimeAutoComplete.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Clearing the location that might have been set by realTimeAutoComplete.onItemClick
                selectedLocation = null;

                // Cancel the old task
                if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                    System.out.println("*Cancelling*");
                }

                if (charSequence.toString().length() >= SEARCH_THRESHOLD) {
                    // AsyncTask can only be used once, so have to make a new one each time
                    asyncTask = new GetRealTimeLocationAsyncTask();

                    asyncTask.execute(new String[]{charSequence.toString()});
                }
            }

            public void afterTextChanged(Editable editable) {
            }
        });

    }

    // TODO: Possible to avoid return type?
    private class GetRealTimeLocationAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... location) {
            realTimeLocations = ServiceFactory.getRuterService().findRealTimeLocations(location[0].replaceAll(" ", "%20"));
            return null;
        }

        @Override
        protected void onPreExecute() {
            autoCompleteProgressBar.setVisibility(ProgressBar.VISIBLE);
        }

        // Need to call notifyDataSetChanged on the UI thread
        @Override
        protected void onPostExecute(String result) {
            // TODO: Why does it not show up on the first update? Triggers at second update
            // TODO: Should we clear, or just avoid duplicates?
            realTimeAutoCompleteAdapter.clear();
            realTimeAutoCompleteAdapter.addAll(realTimeLocations);
            autoCompleteProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    private List<RealTimeData> getRealTimeData(Integer id) {
        return ServiceFactory.getRuterService().getRealTimeData(id);
    }

}
