package vadevelopment.ideation360.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.Campaign_Skeleton;
import vadevelopment.ideation360.Skeleton.SavedIdeas_Skeleton;
import vadevelopment.ideation360.fragments.AddIdeaFragment;

/**
 * Created by vibrantappz on 6/22/2017.
 */

public class AdapterCampaign extends RecyclerView.Adapter<AdapterCampaign.ViewHolder> {

    private Context context;
    private ArrayList<Campaign_Skeleton> arraylist;


    public AdapterCampaign(Context context, ArrayList<Campaign_Skeleton> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterCampaign.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowscampaign, parent, false);
        AdapterCampaign.ViewHolder viewHolder = new AdapterCampaign.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterCampaign.ViewHolder viewHolder, int position) {
        viewHolder.campaign_name.setText(arraylist.get(position).getCampaign_name());
        viewHolder.ideas_submited.setText(arraylist.get(position).getIdeas_submitted() + " " + "ideas submitted");
        viewHolder.days_left.setText(arraylist.get(position).getDaysleft() + " " + "days left");
        viewHolder.ll_outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandyObjects.showAlert(context, "clkick");
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView campaign_name, ideas_submited, days_left;
        public LinearLayout ll_outer;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            campaign_name = (TextView) itemLayoutView.findViewById(R.id.campaign_name);
            ideas_submited = (TextView) itemLayoutView.findViewById(R.id.ideas_submited);
            days_left = (TextView) itemLayoutView.findViewById(R.id.days_left);
            ll_outer = (LinearLayout) itemLayoutView.findViewById(R.id.ll_outer);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arraylist.size();
    }
}


