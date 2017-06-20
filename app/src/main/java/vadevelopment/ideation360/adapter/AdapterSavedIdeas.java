package vadevelopment.ideation360.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.AllIdeas_Skeleton;
import vadevelopment.ideation360.Skeleton.SavedIdeas_Skeleton;

/**
 * Created by vibrantappz on 6/20/2017.
 */

public class AdapterSavedIdeas extends RecyclerView.Adapter<AdapterSavedIdeas.ViewHolder> {

    private Context context;
    private ArrayList<SavedIdeas_Skeleton> arraylist;
    String text;


    public AdapterSavedIdeas(Context context, ArrayList<SavedIdeas_Skeleton> arraylist) {
        this.context = context;
        this.text = text;
        this.arraylist = arraylist;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterSavedIdeas.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowshome, parent, false);
        AdapterSavedIdeas.ViewHolder viewHolder = new AdapterSavedIdeas.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterSavedIdeas.ViewHolder viewHolder, int position) {

        // if (text.equalsIgnoreCase("profile")) {
        viewHolder.date.setVisibility(View.INVISIBLE);


        viewHolder.maintitle.setText(arraylist.get(position).getIdea_name());
        viewHolder.compaign.setText(arraylist.get(position).getCampaign());
        viewHolder.noof_rating.setText("0");
        viewHolder.noof_comment.setText("0");
        viewHolder.ratingBar.setRating(Float.parseFloat("0"));
        viewHolder.date.setText(arraylist.get(position).getAudio_path());

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date, maintitle, compaign, noof_rating, noof_comment;
        public RatingBar ratingBar;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            maintitle = (TextView) itemLayoutView.findViewById(R.id.maintitle);
            compaign = (TextView) itemLayoutView.findViewById(R.id.compaign);
            ratingBar = (RatingBar) itemLayoutView.findViewById(R.id.ratingBar);
            noof_rating = (TextView) itemLayoutView.findViewById(R.id.noof_rating);
            noof_comment = (TextView) itemLayoutView.findViewById(R.id.noof_comment);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arraylist.size();
    }
}

