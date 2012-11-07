package no.ruter.app.android;

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
import android.widget.*;
import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RealtimeSectionFragment extends Fragment {

    private View rootView;

    private final static int SEARCH_THRESHOLD = 3;

    private RealTimeLocation selectedFromLocation;
    private List<RealTimeLocation> realTimeLocations;

    private GetRealTimeLocationAsyncTask asyncTask;

    private ArrayAdapter<RealTimeLocation> realtimeAdapter;

    private AutoCompleteTextView realtimeAutoComplete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        rootView = inflater.inflate(R.layout.fragment_section_realtime, container, false);
        Bundle args = getArguments();

        setUpAutoCompleteTextViews();

        return rootView;
    }

    private void setUpAutoCompleteTextViews() {
        realtimeAutoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.realtimeAutoCompleteView);
        realtimeAutoComplete.setThreshold(0); // TODO: Set to SEARCH_THRESHOLD - or maybe keep at 0, but start pulling from the API at 3?

        realtimeAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    List<RealTimeData> realTimeData = getRealTimeData(3010011);
                    for (RealTimeData data : realTimeData) {
                        System.out.println(data.getDestination());
                    }
                    handled = true;
                }
                return handled;
            }
        });

        realTimeLocations = new ArrayList<RealTimeLocation>();

        realtimeAdapter = new ArrayAdapter<RealTimeLocation>(getActivity(), android.R.layout.simple_list_item_1, realTimeLocations);
        realtimeAutoComplete.setAdapter(realtimeAdapter);

        realtimeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFromLocation = (RealTimeLocation) parent.getItemAtPosition(position); // TODO: Index out of bounds issue
            }
        });

        realtimeAutoComplete.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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

    private class GetRealTimeLocationAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... location) {
            realTimeLocations = ServiceFactory.getRuterService().findRealTimeLocations(location[0].replaceAll(" ", "%20"));
            System.out.println("*UPDATED* " + realTimeLocations.size());

            return null;
        }

        // Need to call notifyDataSetChanged on the UI thread
        @Override
        protected void onPostExecute(String result) {
            // TODO: Why does it not show up on the first update? Triggers at second update
            realtimeAdapter.addAll(realTimeLocations);
            realtimeAdapter.notifyDataSetChanged();
        }
    }

    private List<RealTimeData> getRealTimeData(Integer id) {
        return ServiceFactory.getRuterService().getRealTimeData(id);
    }

}
