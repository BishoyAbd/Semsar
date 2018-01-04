package com.projects.cactus.maskn.appartments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projects.cactus.maskn.R;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.profile.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by el on 2/7/2017.
 */

public class ApartmentRecyclerViewAdapter extends RecyclerView.Adapter<ApartmentRecyclerViewAdapter.ApartmentViewHolder> {

    @NonNull
    List<Apartment> apartmentList;
    @NonNull
    Context context;

    public ApartmentRecyclerViewAdapter(List<Apartment> apartmentList, Context context) {
        this.apartmentList = apartmentList;
        this.context = context;
    }

    @Override
    public ApartmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apartment, parent, false);
        return new ApartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApartmentViewHolder holder, int position) {

        Apartment apartment = apartmentList.get(position);
        holder.apartmentPrice.setText(apartment.getPrice() + "");
        holder.numOfRooms.setText(apartment.getRoomsNumber() + "");
        holder.numViews.setText(apartment.getNumOfViews() + "");

        if (null != apartment.getmApartmentImages()) {
            if (apartment.getmApartmentImages().size() > 0)
                Picasso.with(context)
                        .load(apartment.getmApartmentImages().get(0))
                .resize(300,300)
                        .placeholder(R.drawable.ic_photo_camera_white_24dp).
                        into(holder.apartmentImage);

            else Picasso.with(context)
                    .load(R.drawable.ic_menu_gallery)
                    .placeholder(R.drawable.ic_photo_camera_white_24dp)
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.apartmentImage);

        }
    }


    @Override
    public int getItemCount() {
        return apartmentList.size();
    }

    public class ApartmentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.apartment_price_id)
        TextView apartmentPrice;
        @BindView(R.id.apartment_mainPhoto_id)
        ImageView apartmentImage;
        @BindView(R.id.numVies_textView_id)
        TextView numViews;
        @BindView(R.id.apartment_numOfRooms_id)
        TextView numOfRooms;
//        @BindView(R.id.singleApartmentCardContainer)
//        CardView singleApartmentCardContainer;
//        @BindView(R.id.singleApartmenLinearContainer)
//        LinearLayout singleApartmentLinearontainer;


        public ApartmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
