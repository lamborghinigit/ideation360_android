package vadevelopment.ideation360.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vadevelopment.ideation360.R;

/**
 * Created by vibrantappz on 6/14/2017.
 */

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {

    private Context context;
    String text;


    public AdapterHome(Context context,String text) {
        this.context = context;
        this.text = text;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterHome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowshome, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        if(text.equalsIgnoreCase("profile")){
            viewHolder.date.setVisibility(View.INVISIBLE);
        }
        else {
            viewHolder.date.setVisibility(View.VISIBLE);
        }

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            date = (TextView) itemLayoutView.findViewById(R.id.date);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 8;
    }
}

