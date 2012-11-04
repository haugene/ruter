package no.ruter.app.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class RoutePlannerSectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.fragment_section_routeplanner, container, false);
        Bundle args = getArguments();

        AutoCompleteTextView fromStation = (AutoCompleteTextView) rootView.findViewById(R.id.fromStationAutoCompleteView);
        fromStation.setThreshold(3);
        ArrayAdapter<String> fromStationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tempArray());
        fromStation.setAdapter(fromStationAdapter);

        return rootView;
    }

    private String[] tempArray() {
        return new String[]{"Lysaker", "Skøyen", "Jernbanetorget", "Torshov", "Bislett", "Majorstuen", "Frogner"};
    }
}
