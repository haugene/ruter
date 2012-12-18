package no.ruter.app.android.fragment;

import java.util.List;

import no.ruter.app.android.R;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;
import no.ruter.app.observers.NearMeObserver;
import no.ruter.app.service.RuterService;
import no.ruter.app.service.ServiceFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ClosestStopFragment extends Fragment {

	private View rootView;
	
	private NearMeObserver nearMeObserver;
	
	private TextView textView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		RuterService ruterService = ServiceFactory.getRuterService();
		
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		rootView = inflater.inflate(R.layout.fragment_section_closest_stop,
				container, false);
		Bundle args = getArguments();

		textView = (TextView) rootView.findViewById(R.id.textView1);
		textView.setText("Waiting for update");
		
		try {
			ruterService.registerNearMeObserver(getNearMeObserver(), getActivity());
		} catch (RepositoryException e) {
			textView.setText("Got exception registering observer");
		}

		return rootView;
	}

	private NearMeObserver getNearMeObserver() {
		
		if(nearMeObserver == null){
			nearMeObserver = createNearMeObserver();
		}
		
		return nearMeObserver;
	}

	private NearMeObserver createNearMeObserver() {
		
		return new NearMeObserver() {
			
			public void stoppedLooking() {
				
				// We won't get any more updates on this one
				nearMeObserver = null;
				
				textView.setText("Stopped looking for locations");
			}
			
			public void listUpdated(List<RealTimeLocation> nearby) {
				
				if(nearby == null){
					/*
					 *  Something horrible happened. We got a new location,
					 *  but somehow we had an error getting stops.
					 */
					textView.setText("An error occured");
					return;
				}
				
				if(nearby.size() == 0){
					textView.setText("Got empty list of stops");
					return;
				}
				
				StringBuilder sb = new StringBuilder();

				for(RealTimeLocation loc : nearby){
					sb.append(loc.getName()).append(" | ");
				}
				
				textView.setText(sb.toString());
			}
		};
		
	}
}
