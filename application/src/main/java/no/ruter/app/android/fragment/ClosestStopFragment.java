package no.ruter.app.android.fragment;

import no.ruter.app.android.R;
import no.ruter.app.service.ServiceFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ClosestStopFragment extends Fragment {

	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		rootView = inflater.inflate(R.layout.fragment_section_closest_stop,
				container, false);
		Bundle args = getArguments();

		TextView textView = (TextView) rootView.findViewById(R.id.textView1);

		textView.setText(ServiceFactory.getRuterService().printLocationData(
				getActivity()));

		return rootView;
	}
}
