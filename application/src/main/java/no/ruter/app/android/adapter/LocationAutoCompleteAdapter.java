package no.ruter.app.android.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;
import no.ruter.app.service.ServiceFactory;

import java.util.List;

/**
 *
 *
 */
public class LocationAutoCompleteAdapter extends ArrayAdapter<RealTimeLocation> implements Filterable{

    private List<RealTimeLocation> resultList;

    public LocationAutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                try {
                    resultList = ServiceFactory.getRuterService().findRealTimeLocations((String) charSequence);
                } catch (RepositoryException e) {
                    // TODO: Connection issues
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                filterResults.values = resultList;
                filterResults.count = resultList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(filterResults != null && filterResults.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };



        return filter;
    }
}
