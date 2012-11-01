package no.ruter.app.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class RoutePlannerSectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.fragment_section_routeplanner, container, false);
        Bundle args = getArguments();

        EditText fromStation = (EditText) rootView.findViewById(R.id.from_station);
        fromStation.setHint("Fra stasjon");
        fromStation.setCursorVisible(true);

        return rootView;
    }
}
