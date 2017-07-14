package vadevelopment.ideation360.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.Ratings_Skeleton;
import vadevelopment.ideation360.Skeleton.SavedIdeas_Skeleton;
import vadevelopment.ideation360.fragments.AddIdeaFragment;
import vadevelopment.ideation360.fragments.MyProfileFragment;

/**
 * Created by vibrantappz on 7/12/2017.
 */

public class Rating_Adapter extends RecyclerView.Adapter<Rating_Adapter.ViewHolder> {

    private Context context;
    private ArrayList<Ratings_Skeleton> arraylist;
    String text;
    private FragmentManager fm;
    private HomeActivity homeActivity;
    LazyHeaders.Builder builder;


    public Rating_Adapter(Context context, ArrayList<Ratings_Skeleton> arraylist, FragmentManager fm, HomeActivity homeActivity) {
        this.context = context;
        this.arraylist = arraylist;
        this.fm = fm;
        this.homeActivity = homeActivity;
        builder = new LazyHeaders.Builder().addHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Rating_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ratings, parent, false);
        Rating_Adapter.ViewHolder viewHolder = new Rating_Adapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final Rating_Adapter.ViewHolder viewHolder,final int position) {

        // if (text.equalsIgnoreCase("profile")) {
        final Ratings_Skeleton data = arraylist.get(position);
        viewHolder.name.setText(arraylist.get(position).getName());
        viewHolder.ratingBar.setRating(Float.parseFloat(arraylist.get(position).getValue()));
        GlideUrl glideUrl = new GlideUrl("https://app.ideation360.com/api/getprofileimage/" +  arraylist.get(position).getIdeatorid(), builder.build());
        Glide.with(context).load(glideUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.imageview);
        viewHolder.ll_outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProfileFragment myprofile_frg = new MyProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ideatorid", arraylist.get(position).getIdeatorid());
                bundle.putString("name", arraylist.get(position).getName().split("\\s+")[0]);
                myprofile_frg.setArguments(bundle);
                homeActivity.replaceFragmentHome(myprofile_frg);
            }
        });

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView imageview;
        public RatingBar ratingBar;
        public LinearLayout ll_outer;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            name = (TextView) itemLayoutView.findViewById(R.id.name);
            ratingBar = (RatingBar) itemLayoutView.findViewById(R.id.ratingBar);
            ll_outer = (LinearLayout) itemLayoutView.findViewById(R.id.ll_outer);
            imageview = (ImageView) itemLayoutView.findViewById(R.id.imageview);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arraylist.size();
    }
}


