package com.example.ncovidtracker.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "country_table")
public class Country implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "country")
    private String mCountry;

    @ColumnInfo(name = "cases")
    private int mCases;

    private String mTodayCases;
    private String mDeaths;
    private String mTodayDeaths;
    private String mRecovered;
    private String mActive;
    private String mCritical;
    private String mFlags;

    public Country(@NonNull String country, int cases, String todayCases, String deaths, String todayDeaths, String recovered, String active, String critical, String flags) {
        this.mCountry = country;
        this.mCases = cases;
        this.mTodayCases = todayCases;
        this.mDeaths = deaths;
        this.mTodayDeaths = todayDeaths;
        this.mRecovered = recovered;
        this.mActive = active;
        this.mCritical = critical;
        this.mFlags = flags;
    }

    @NonNull
    public String getCountry() {
        return this.mCountry;
    }

    public int getCases() {
        return this.mCases;
    }

    public String getTodayCases() {
        return this.mTodayCases;
    }

    public String getDeaths() {
        return this.mDeaths;
    }

    public String getTodayDeaths() {
        return this.mTodayDeaths;
    }

    public String getRecovered() {
        return this.mRecovered;
    }

    public String getActive() {
        return this.mActive;
    }

    public String getCritical() {
        return this.mCritical;
    }

    public String getFlags() {
        return this.mFlags;
    }

    public void setCountry(@NonNull String mCountry) {
        this.mCountry = mCountry;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCountry);
        dest.writeInt(this.mCases);
        dest.writeString(this.mTodayCases);
        dest.writeString(this.mDeaths);
        dest.writeString(this.mTodayDeaths);
        dest.writeString(this.mRecovered);
        dest.writeString(this.mActive);
        dest.writeString(this.mCritical);
        dest.writeString(this.mFlags);
    }

    protected Country(Parcel in) {
        this.mCountry = in.readString();
        this.mCases = in.readInt();
        this.mTodayCases = in.readString();
        this.mDeaths = in.readString();
        this.mTodayDeaths = in.readString();
        this.mRecovered = in.readString();
        this.mActive = in.readString();
        this.mCritical = in.readString();
        this.mFlags = in.readString();
    }

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
