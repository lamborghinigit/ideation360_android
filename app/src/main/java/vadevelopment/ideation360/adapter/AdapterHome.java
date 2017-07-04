package vadevelopment.ideation360.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.AllIdeas_Skeleton;
import vadevelopment.ideation360.fragments.IdeaDeatilFragment;

/**
 * Created by vibrantappz on 6/14/2017.
 */

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {

    private Context context;
    private ArrayList<AllIdeas_Skeleton> arraylist;
    String text;
    private FragmentManager fm;
    private HomeActivity homecontainor;

    public AdapterHome(Context context, String text, ArrayList<AllIdeas_Skeleton> arraylist, FragmentManager fm) {
        this.context = context;
        this.text = text;
        this.arraylist = arraylist;
        this.fm = fm;
        homecontainor = ((HomeActivity) context);
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
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        try {
            LazyHeaders.Builder builder = new LazyHeaders.Builder()
                    .addHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
            GlideUrl glideUrl = new GlideUrl(arraylist.get(position).getImage(), builder.build());

            Glide.with(context).load(glideUrl).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(viewHolder.image);
        } catch (Exception e) {
        }
        if (text.equalsIgnoreCase("profile")) {
            viewHolder.date.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.date.setVisibility(View.VISIBLE);
        }

        viewHolder.maintitle.setText(arraylist.get(position).getMain_title());
        viewHolder.compaign.setText(arraylist.get(position).getCampaign_title());
        viewHolder.noof_rating.setText(arraylist.get(position).getNoof_rating());
        viewHolder.noof_comment.setText(arraylist.get(position).getNoof_comments());
        String[] d = arraylist.get(position).getDate().split("T");
        String[] datesplit = d[0].split("-");
        viewHolder.date.setText(datesplit[0] + "." + datesplit[1] + "." + datesplit[2]);
        viewHolder.ratingBar.setRating(Float.parseFloat(arraylist.get(position).getRating_meanvalue()));


        viewHolder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IdeaDeatilFragment id_frg = new IdeaDeatilFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "idea");
                bundle.putString("ideaid", arraylist.get(position).getIdeas_id());
                bundle.putString("ideatorid", arraylist.get(position).getIdeator_id());
                String[] d = arraylist.get(position).getDate().split("T");
                String[] datesplit = d[0].split("-");
                bundle.putString("date", datesplit[0] + "." + datesplit[1] + "." + datesplit[2]);
                id_frg.setArguments(bundle);
                homecontainor.replaceFragmentHome(id_frg);
            }
        });

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date, maintitle, compaign, noof_rating, noof_comment;
        public RatingBar ratingBar;
        public ImageView image;
        public LinearLayout ll_main;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            maintitle = (TextView) itemLayoutView.findViewById(R.id.maintitle);
            compaign = (TextView) itemLayoutView.findViewById(R.id.compaign);
            ratingBar = (RatingBar) itemLayoutView.findViewById(R.id.ratingBar);
            noof_rating = (TextView) itemLayoutView.findViewById(R.id.noof_rating);
            noof_comment = (TextView) itemLayoutView.findViewById(R.id.noof_comment);
            image = (ImageView) itemLayoutView.findViewById(R.id.image);
            ll_main = (LinearLayout) itemLayoutView.findViewById(R.id.ll_main);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arraylist.size();
    }
}

