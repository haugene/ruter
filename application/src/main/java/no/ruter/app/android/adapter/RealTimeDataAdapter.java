package no.ruter.app.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import no.ruter.app.android.R;
import no.ruter.app.domain.Platform;
import no.ruter.app.domain.RealTimeData;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom ArrayAdapter for displaying the real time data.
 *
 * @author daniel
 *
 */
public class RealTimeDataAdapter extends ArrayAdapter {

    private List<Platform> platformList;
    private Context context;
    private int layoutResourceId;

    // Regex for matching the different departure time formats
    private String regex = "(\\d{2}[\\:]\\d{2})|(\\s{1}nå\\s{3})|(\\s{1}\\d{1}\\s{1}min)";
    private Matcher matcher;
    private Pattern pattern;

    public RealTimeDataAdapter(Context context, int textViewResourceId, List objects) {
        super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        platformList = objects;
        this.context = context;
    }

    public int getCount() {
        return platformList.size();
    }

    public Object getItem(int i) {
        return i;
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View view, ViewGroup parent) {
        System.out.println("getView()");
        View realtimeDataView = view;
        if (realtimeDataView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            inflater.inflate(layoutResourceId, parent, false);
            realtimeDataView = inflater.inflate(R.layout.listview_realtime_data, null);
        }

        TextView platformName = (TextView) realtimeDataView.findViewById(R.id.textViewPlatformName);
        TextView lineNumber = (TextView) realtimeDataView.findViewById(R.id.textViewLineNumber);
        TextView lineName = (TextView) realtimeDataView.findViewById(R.id.textViewDestinationName);
        TextView currentDeparture = (TextView) realtimeDataView.findViewById(R.id.textViewDepartureTime);
        TextView nextDepartures = (TextView) realtimeDataView.findViewById(R.id.textViewDepartureTimeScroller);

        Platform currentPlatform = platformList.get(position);
        List<RealTimeData> departures = currentPlatform.getDepartures();

        platformName.setText("Platform " + currentPlatform.getName()); // TODO: String resource
        lineNumber.setText(departures.get(0).getLine() + " "); // TODO: XMLify
        lineName.setText(departures.get(0).getDestination() + " ");
        currentDeparture.setText(departures.get(0).getFormattedDepartureTime());

        SpannableStringBuilder destinationSpannableStringBuilder = new SpannableStringBuilder();
        String departuresString = "";

        // Loop through the departures and concat them to a string
        for (int i = 1; i < departures.size() && i < 5; i++) {
            departuresString = departuresString.concat(departures.get(i).getLine() + " " +
                    departures.get(i).getDestination() + " " + departures.get(i).getFormattedDepartureTime() + "   ");
        }
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(departuresString);

        destinationSpannableStringBuilder.append(departuresString);

        // Formats the departure time texts
        while (matcher.find()) {
            destinationSpannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FC6868")), matcher.start(), matcher.end(), 0);
        }

        nextDepartures.setText(destinationSpannableStringBuilder);

        return realtimeDataView;
    }
}
