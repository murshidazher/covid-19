package com.example.ncovidtracker.data;

import android.widget.ImageView;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ncovidtracker.R;

import java.util.List;

import androidx.annotation.NonNull;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.CountryViewHolder> {

    class CountryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalCases, tvCountryName;
        ImageView imgCountryFlag;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTotalCases = itemView.findViewById(R.id.tvTotalCases);
            tvCountryName = itemView.findViewById(R.id.tvCountryName);
            imgCountryFlag = itemView.findViewById(R.id.imgCountryFlag);
        }


    }

    private Context context;

    private final LayoutInflater mInflater;
    private List<Country> mCountries; // Cached copy of words

    public CountryListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_list_covid_country, parent, false);
        return new CountryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        if (mCountries != null) {
            Country current = mCountries.get(position);
            holder.tvCountryName.setText(current.getCountry());
            holder.tvTotalCases.setText(Integer.toString(current.getCases()));


//            holder.containerCountry.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

            // Glide
            Glide.with(context)
                    .load(current.getFlags())
                    .apply(new RequestOptions().override(240, 160))
                    .into(holder.imgCountryFlag);
        }
    }




    public void setCountries(List<Country> countries) {
        mCountries = countries;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mCountries != null)
            return mCountries.size();
        else return 0;
    }
}


