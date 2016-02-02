package com.jasonsavlov.aaprototype.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.obj.AASong;
import com.jasonsavlov.aaprototype.ui.format.SongTimeFormatter;

/**
 * Created by jason on 1/2/16.
 */
public class SongDetailRowView extends RelativeLayout
{

    // Instance variables
    private TextView songNumberTextView;
    private TextView songTitleTextView;
    private TextView songDurationTextView;
    private TextView discTrackNumberText;

    private AASong mSong;


    /*
        A bunch of constructors, overridden as mandated...
     */

    public SongDetailRowView(Context context)
    {
        super(context);
        init(context);
    }

    public SongDetailRowView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public SongDetailRowView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(22)
    public SongDetailRowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.song_detail_row_view_children, this, true);
        setupChildren();
    }

    public static SongDetailRowView inflate(ViewGroup parent)
    {
        SongDetailRowView rowView = (SongDetailRowView) LayoutInflater.from(parent.getContext()).inflate(R.layout.song_detail_row_view, parent, false);
        return rowView;
    }

    private void setupChildren()
    {
        songNumberTextView = (TextView) findViewById(R.id.song_track_num_text);
        songTitleTextView = (TextView) findViewById(R.id.song_title_text);
        songDurationTextView = (TextView) findViewById(R.id.song_duration_text);
        discTrackNumberText = (TextView) findViewById(R.id.disc_number_text);
    }

    public void setSong(AASong _song)
    {
        this.mSong = _song;
        songTitleTextView.setText(mSong.getSongName());
        songNumberTextView.setText(mSong.getTrackNumString());
        songDurationTextView.setText(SongTimeFormatter.getTimeStringFromString(mSong.getSongDuration()));

        if (mSong.isStartingTrack()) {
            discTrackNumberText.setText("Disc " + mSong.getDiscNumber());
            songNumberTextView.setPadding(
                    songNumberTextView.getPaddingLeft(),
                    songNumberTextView.getPaddingTop() + 5,
                    songNumberTextView.getPaddingRight(),
                    songDurationTextView.getPaddingBottom());
        } else {
            discTrackNumberText.setText("");
        }
    }



}
