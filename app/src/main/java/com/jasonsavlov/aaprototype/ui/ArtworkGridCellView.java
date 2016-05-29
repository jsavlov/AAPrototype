package com.jasonsavlov.aaprototype.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jasonsavlov.aaprototype.R;

/**
 * TODO: document your custom view class.
 */
public class ArtworkGridCellView extends RelativeLayout {

    // Instance variables for UI resources
    ImageView artworkImageView;
    TextView dimensionsTextView;
    TextView dataSizeTextView;

    Bitmap artworkImageBitmap;
    String dimensionTextString;
    String dataSizeTextString;


    /*
        Stuff to add:
        - Instance variables for the values to be held in the
            UI resources
        - Getter/Setter methods
     */

    public ArtworkGridCellView(Context context) {
        super(context);
        init(context);
    }

    public ArtworkGridCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ArtworkGridCellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public ArtworkGridCellView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.artwork_grid_cell_view_children, this, true);
        setupChildren();
    }

    private void setupChildren() {
        artworkImageView = (ImageView) findViewById(R.id.artwork_cell_image_view);
        dimensionsTextView = (TextView) findViewById(R.id.dimensions_text_view);
        dataSizeTextView = (TextView) findViewById(R.id.data_size_text_view);

        // TODO: Set the size of the imageView to something reasonable
    }

    public static ArtworkGridCellView inflate(ViewGroup parent) {
        ArtworkGridCellView view = (ArtworkGridCellView) LayoutInflater.from(parent.getContext()).inflate(R.layout.artwork_grid_cell_view, parent, false);
        return view;
    }

    public Bitmap getArtworkImageBitmap() {
        return artworkImageBitmap;
    }

    public void setArtworkImageBitmap(Bitmap artworkImageBitmap) {
        this.artworkImageBitmap = artworkImageBitmap;

        this.artworkImageView.setImageBitmap(this.artworkImageBitmap);
    }

    public String getDimensionTextString() {
        return dimensionTextString;
    }

    public void setDimensionTextString(String dimensionTextString) {
        this.dimensionTextString = dimensionTextString;

        this.dimensionsTextView.setText(this.dimensionTextString);
    }

    public String getDataSizeTextString() {
        return dataSizeTextString;
    }

    public void setDataSizeTextString(String dataSizeTextString) {
        this.dataSizeTextString = dataSizeTextString;

        this.dataSizeTextView.setText(this.dataSizeTextString);
    }

    /*
        static helper methods
     */

    public static final String getDimensionStringFromDims(int width, int height) {
        return Integer.toString(width) + " by " + Integer.toString(height);
    }

    public static final String getDataSizeStringFromByteLength(int size) {
        // TODO: implement the conversion code

        return Integer.toString(size);
    }
}
