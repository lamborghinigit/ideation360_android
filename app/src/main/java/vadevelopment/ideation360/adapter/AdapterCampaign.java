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
import vadevelopment.ideation360.fragments.CampaignDetailFragment;

/**
 * Created by vibrantappz on 6/22/2017.
 */

public class AdapterCampaign extends RecyclerView.Adapter<AdapterCampaign.ViewHolder> {

    private Context context;
    private ArrayList<Campaign_Skeleton> arraylist;
    private FragmentManager fm;
    private HomeActivity homecontaionr;


    public AdapterCampaign(Context context, ArrayList<Campaign_Skeleton> arraylist, FragmentManager fm) {
        this.context = context;
        this.arraylist = arraylist;
        this.fm = fm;
        homecontaionr = ((HomeActivity) context);
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
    public void onBindViewHolder(final AdapterCampaign.ViewHolder viewHolder, final int position) {
        viewHolder.campaign_name.setText(arraylist.get(position).getCampaign_name());

        if (Integer.parseInt(arraylist.get(position).getIdeas_submitted()) > 1) {
            viewHolder.ideas_submited.setText(arraylist.get(position).getIdeas_submitted() + " " + context.getResources().getString(R.string.ideassubmitted));
        } else {
            viewHolder.ideas_submited.setText(arraylist.get(position).getIdeas_submitted() + " " + context.getResources().getString(R.string.ideasubmitted));
        }

        if (Integer.parseInt(arraylist.get(position).getDaysleft()) > 1) {
            viewHolder.days_left.setText(arraylist.get(position).getDaysleft() + " " + context.getResources().getString(R.string.daysleft));
        } else {
            viewHolder.days_left.setText(arraylist.get(position).getDaysleft() + " " + context.getResources().getString(R.string.dayleft));
        }


        viewHolder.ll_outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //HandyObjects.showAlert(context, "clkick");

                CampaignDetailFragment campdetail_frgm = new CampaignDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("dayleft", arraylist.get(position).getDaysleft());
                bundle.putString("campaignid", arraylist.get(position).getCompaignid());
                campdetail_frgm.setArguments(bundle);
                homecontaionr.replaceFragmentHome(campdetail_frgm);
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


