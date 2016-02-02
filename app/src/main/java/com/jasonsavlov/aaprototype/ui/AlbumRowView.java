package com.jasonsavlov.aaprototype.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jasonsavlov.aaprototype.obj.AAAlbum;
import com.jasonsavlov.aaprototype.ApplicationMain;
import com.jasonsavlov.aaprototype.R;

/**
 * Created by jason on 8/16/15.
 */
public class AlbumRowView extends RelativeLayout
{

    // Instance variables
    private TextView albumNameTextView;
    private TextView artistNameTextView;
    private ImageView albumArtworkImageView;
    private AAAlbum mAlbum;

    private final float ALBUM_NAME_SIZE = 16.0f;

    public AlbumRowView(Context context)
    {
        super(context);
        init(context);
    }

    public AlbumRowView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public AlbumRowView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public AlbumRowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.album_row_layout_children, this, true);
        setupChildren();
    }

    public static AlbumRowView inflate(ViewGroup parent)
    {
        AlbumRowView rowView = (AlbumRowView) LayoutInflater.from(parent.getContext()).inflate(R.layout.album_row_layout, parent, false);
        return rowView;
    }

    // Override Methods

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        albumNameTextView.setTextSize(ALBUM_NAME_SIZE);
        invalidate();
        int line_count = albumNameTextView.getLineCount();
        Log.v(TAG, "onLayout() - line_count = " + line_count);
        //reduceAlbumNameLines();
        //invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int line_count = albumNameTextView.getLineCount();
        Log.v(TAG, "onDraw() - line_count = " + line_count);
    }

    // Getters & Setters

    public AAAlbum getAlbum()
    {
        return mAlbum;
    }

    public void setAlbum(AAAlbum album)
    {
        mAlbum = album;

        // Set the appropriate text for the widgets in this view
        albumNameTextView.setText(mAlbum.getAlbumName());
        artistNameTextView.setText(mAlbum.getAlbumArtistName());
        albumArtworkImageView.setImageBitmap(mAlbum.getArtwork());
    }

    /* Debugging methods used externally */
    float getAlbumNameFontSize()
    {
        return albumNameTextView.getTextSize();
    }

    /* Private internal methods */

    /**
     *     setupChildren(): sets up the child views in this view
     */
    private void setupChildren()
    {
        albumNameTextView = (TextView) findViewById(R.id.albumName);
        artistNameTextView = (TextView) findViewById(R.id.artistName);
        albumArtworkImageView = (ImageView) findViewById(R.id.albumArt);
    }


    // FIXME: This was migrated to AAAlbum. Leave for now.
    private Bitmap generateAlbumArtworkBitmap()
    {
        String albumArtPath = mAlbum.getAlbumArtPath();
        Bitmap rawBitmap = null;

        if (albumArtPath == null) {
            // There is no album artwork.
            return generateDefaultArtworkBitmap();
        }

        rawBitmap = BitmapFactory.decodeFile(albumArtPath);

        if (rawBitmap == null) {
            // The bitmap file doesn't exist.
            return generateDefaultArtworkBitmap();
        }

        // We should have a bitmap at this point. Return it.
        return rawBitmap;
    }

    private Bitmap generateDefaultArtworkBitmap()
    {
        // TODO: Implement a default album artwork bitmap image
        return null;
    }

    // FIXME: This method is defunct.
    private void reduceAlbumNameLines()
    {
        Log.v(TAG, "----- reduceAlbumNameLines() called -----");

        DisplayMetrics dm = ApplicationMain.getInstance().getApplicationContext().getResources().getDisplayMetrics();
        float albumNameTextSize = albumNameTextView.getTextSize() / dm.density;
        final float reductionFactor = 0.8f;
        Log.v(TAG, "startingTextSize: " + albumNameTextSize);
        Log.v(TAG, "Starting line count: " + albumNameTextView.getLineCount());
        int i = 0;
        int lineCount = albumNameTextView.getLineCount();
        float newTextSize;
        while (albumNameTextView.getLineCount() > 2) {
            Log.v(TAG, "reduction iteration " + i + ":");
            newTextSize = albumNameTextSize * reductionFactor;
            albumNameTextView.setTextSize(newTextSize);
            albumNameTextSize = albumNameTextView.getTextSize();
            albumNameTextView.invalidate();
            i++;
        }
        Log.v(TAG, "endingTextSize: " + albumNameTextView.getTextSize());
        Log.v(TAG, "Ending line count: " + albumNameTextView.getLineCount());
    }

    private final String TAG = "AlbumRowView";

}
