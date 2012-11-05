package no.ruter.app.android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.service.ServiceFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RoutePlannerSectionFragment extends Fragment {

    private View rootView;

    private final static int SEARCH_THRESHOLD = 3;

    private RealTimeLocation[] locations;
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


        fromStationAutoComplete.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Cancel the old task
                if(asyncTask != null) {
                    asyncTask.cancel(true);
                }
                if(charSequence.toString().length() >= SEARCH_THRESHOLD) {
                    try {
                        // AsyncTask can only be used once, so have to make a new one each time
                        asyncTask = new GetRealTimeLocationAsyncTask();

                        List<RealTimeLocation> realTimeLocations = asyncTask.execute(new String[]{ charSequence.toString() }).get();
                        locations = realTimeLocations.toArray(new RealTimeLocation[realTimeLocations.size()]);

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
            return ServiceFactory.getRuterService().findRealTimeLocations(location[0].replaceAll(" ", "%20")); // TODO: Replace space in repo
        }
    }

}
