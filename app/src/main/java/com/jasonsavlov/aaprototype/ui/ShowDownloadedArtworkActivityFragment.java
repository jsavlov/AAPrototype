package com.jasonsavlov.aaprototype.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.activity.DetailAlbumActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class ShowDownloadedArtworkActivityFragment extends Fragment {

    // Instance variables
    private GridView mGridView;


    public ShowDownloadedArtworkActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_downloaded_artwork, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mGridView = (GridView) getActivity().findViewById(R.id.gridview);
        mGridView.setAdapter(new ArtworkImageAdapter(this.getActivity()));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private class ArtworkImageAdapter extends BaseAdapter {

        private Context mContext;

        ArtworkImageAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return DetailAlbumActivity.imageList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // The ImageView that will be returned
            ImageView imageView;

            ArtworkGridCellView gridCellView;
            if (convertView == null) {
                // if we don't have a view from the recycling guys, make our own!
                /*imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);*/

                gridCellView = ArtworkGridCellView.inflate(parent);

            } else {
                // Use the recycled view
                //imageView = (ImageView) convertView;
                gridCellView = (ArtworkGridCellView) convertView;
            }

            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display mDisplay = wm.getDefaultDisplay();
            DisplayMetrics dims = new DisplayMetrics();
            mDisplay.getMetrics(dims);

            int thirds = dims.widthPixels / 3;


            if (DetailAlbumActivity.imageList == null)
                throw new NullPointerException("imageList in DetailAlbumActivity was never set...");

            //imageView.setImageBitmap(DetailAlbumActivity.imageList.get(position));
            Bitmap artworkBitmap = DetailAlbumActivity.imageList.get(position);
            gridCellView.setArtworkImageBitmap(artworkBitmap);
            gridCellView.setDimensionTextString(ArtworkGridCellView.getDimensionStringFromDims(artworkBitmap.getWidth(), artworkBitmap.getHeight()));
            gridCellView.setDataSizeTextString(ArtworkGridCellView.getDataSizeStringFromByteLength(artworkBitmap.getByteCount()));


            return gridCellView;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
