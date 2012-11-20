package no.ruter.app.android.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import no.ruter.app.android.R;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RoutePlannerSectionFragment extends Fragment {

    private View rootView;

    private final static int SEARCH_THRESHOLD = 3;

    private RealTimeLocation[] locations;

    private RealTimeLocation selectedFromLocation;

    private GetRealTimeLocationAsyncTask asyncTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        rootView = inflater.inflate(R.layout.fragment_section_routeplanner, container, false);
        Bundle args = getArguments();

        setUpAutoCompleteTextViews();

        return rootView;
    }

    private void setUpAutoCompleteTextViews() {
        final AutoCompleteTextView fromStationAutoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.fromStationAutoCompleteView);
        fromStationAutoComplete.setThreshold(0); // TODO: Set to SEARCH_THRESHOLD

        locations = new RealTimeLocation[0];

        final ArrayAdapter<RealTimeLocation> fromStationAdapter = new ArrayAdapter<RealTimeLocation>(getActivity(), android.R.layout.simple_list_item_1, locations);
        fromStationAutoComplete.setAdapter(fromStationAdapter);

        fromStationAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFromLocation = (RealTimeLocation) parent.getItemAtPosition(position);
            }
        });

        fromStationAutoComplete.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Cancel the old task
                if(asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                    asyncTask.cancel(true);
                }
                if(charSequence.toString().length() >= SEARCH_THRESHOLD) {
                    try {
                        Log.d("Fragment", "On text changed thread = " + Thread.currentThread().getName());
                        // AsyncTask can only be used once, so have to make a new one each time
                        asyncTask = new GetRealTimeLocationAsyncTask();

                        List<RealTimeLocation> realTimeLocations = asyncTask.execute(new String[]{ charSequence.toString() }).get();
                        locations = realTimeLocations.toArray(new RealTimeLocation[realTimeLocations.size()]);

                        // TODO: 05.11.12 - daniel - shouldn't have to initialize a new ArrayAdapter each time. fromStationAdapter.notifyDataSetChanged() should do the trick, but isn't
                        fromStationAutoComplete.setAdapter(new ArrayAdapter<RealTimeLocation>(getActivity(), android.R.layout.simple_list_item_1, locations));
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (ExecutionException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }

            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private class GetRealTimeLocationAsyncTask extends AsyncTask<String, Void, List<RealTimeLocation>> {

        @Override
        protected List<RealTimeLocation> doInBackground(String... location) {
            Log.d("Fragment", "doInBackground thread = " + Thread.currentThread().getName());
//            return ServiceFactory.getRuterService().findRealTimeLocations(location[0].replaceAll(" ", "%20")); // TODO: Replace space in repo
            return new ArrayList<RealTimeLocation>();
        }
    }

}
