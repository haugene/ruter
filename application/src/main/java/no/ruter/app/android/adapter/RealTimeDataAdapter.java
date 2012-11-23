package no.ruter.app.android.adapter;

import android.app.Activity;
import android.content.Context;
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

public class RealTimeDataAdapter extends ArrayAdapter {

    private List<Platform> platformList;
    private Context context;
    int layoutResourceId;

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
        View realtimeDataView = view;
        if(view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            inflater.inflate(layoutResourceId, parent, false);
            realtimeDataView = inflater.inflate(R.layout.listview_realtime_data, null);
        }

        TextView platformName = (TextView) view.findViewById(R.id.textViewPlatformName);
        TextView lineNumber = (TextView) view.findViewById(R.id.textViewLineNumber);
        TextView lineName = (TextView) view.findViewById(R.id.textViewDestinationName);
        TextView currentDeparture = (TextView) view.findViewById(R.id.textViewDepartureTime);
        TextView nextDepartures = (TextView) view.findViewById(R.id.textViewDepartureTimeScroller);

        platformName.setText(platformList.get(position).getName());
        lineNumber.setText(platformList.get(position).getDepartures().get(position).getLine());
        lineName.setText(platformList.get(position).getDepartures().get(position).getDestination());
        currentDeparture.setText(platformList.get(position).getDepartures().get(position).getFormattedDepartureTime());

        return realtimeDataView;
    }
}
