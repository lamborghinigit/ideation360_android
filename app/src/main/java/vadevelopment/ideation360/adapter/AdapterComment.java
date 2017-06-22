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

import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.AllIdeas_Skeleton;
import vadevelopment.ideation360.Skeleton.Comments_Skeleton;
import vadevelopment.ideation360.fragments.IdeaDeatilFragment;

/**
 * Created by vibrantappz on 6/22/2017.
 */

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolder> {

    private Context context;
    private ArrayList<Comments_Skeleton> arraylist;

    public AdapterComment(Context context, ArrayList<Comments_Skeleton> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterComment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowcomment, parent, false);
        AdapterComment.ViewHolder viewHolder = new AdapterComment.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterComment.ViewHolder viewHolder, final int position) {

        viewHolder.name.setText(arraylist.get(position).getIdeator_name());
        viewHolder.comment.setText(arraylist.get(position).getComment());
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, comment;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            name = (TextView) itemLayoutView.findViewById(R.id.name);
            comment = (TextView) itemLayoutView.findViewById(R.id.comment);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arraylist.size();
    }
}

