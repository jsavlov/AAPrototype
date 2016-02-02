package com.jasonsavlov.aaprototype.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jasonsavlov.aaprototype.ApplicationMain;
import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.obj.AAAlbum;
import com.jasonsavlov.aaprototype.obj.AAMediaManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailAlbumTopFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailAlbumTopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailAlbumTopFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_ALBUM = "_album";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AAAlbum mAlbum;

    // UI Components
    private TextView albumNameTextView;
    private TextView artistNameTextView;
    private ImageButton albumArtworkImageButton;

    private AAMediaManager mainManager;



    private OnFragmentInteractionListener mListener;

    public DetailAlbumTopFragment()
    {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DetailAlbumTopFragment newInstance(String albumName, String param2)
    {
        DetailAlbumTopFragment fragment = new DetailAlbumTopFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        args.putString(ARG_ALBUM, albumName);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainManager = ApplicationMain.getInstance().getMainMediaManager();
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //synchronized (lock) {
                mAlbum = mainManager.getAlbumByName(getArguments().getString(ARG_ALBUM));
                mParam2 = getArguments().getString(ARG_PARAM2);
            //}
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View viewToReturn = inflater.inflate(R.layout.fragment_detail_album_top, container, false);

        albumNameTextView = (TextView) viewToReturn.findViewById(R.id.d_album_name_text);
        artistNameTextView = (TextView) viewToReturn.findViewById(R.id.d_artist_text);
        albumArtworkImageButton = (ImageButton) viewToReturn.findViewById(R.id.album_artwork_button);

        try {
                albumNameTextView.setText(mAlbum.getAlbumName());
                artistNameTextView.setText(mAlbum.getAlbumArtistName());
                Bitmap b = mAlbum.getArtwork();
                if (b != null)
                    albumArtworkImageButton.setImageBitmap(mAlbum.getArtwork());
        } catch (Exception e) {
                //e.printStackTrace();
        }

        return viewToReturn;
    }

    public void setAlbum(AAAlbum album) {
        mAlbum = album;
        albumNameTextView.setText(mAlbum.getAlbumName());
        artistNameTextView.setText(mAlbum.getAlbumArtistName());
        albumArtworkImageButton.setImageBitmap(mAlbum.getArtwork());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
