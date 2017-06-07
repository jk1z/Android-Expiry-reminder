package edu.monash.fit3027.fit3027_final_application.UI.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.model.Item;

/**
 * Created by Jack on 23/5/17.
 */

public class BannerViewHolder extends RecyclerView.ViewHolder {


    private ImageView bannerImageView;
    private TextView bannerTextView;

    BannerViewHolder(View itemView) {
        super(itemView);
        bannerImageView = (ImageView) itemView.findViewById(R.id.bannerImageView);
        bannerTextView = (TextView) itemView.findViewById(R.id.bannerTextView);
    }


}
