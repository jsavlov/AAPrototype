package com.jasonsavlov.aaprototype.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.io.Files;
import com.jasonsavlov.aaprototype.ApplicationMain;
import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.obj.AAAlbum;
import com.jasonsavlov.aaprototype.obj.AAMediaManager;
import com.jasonsavlov.aaprototype.obj.AASong;
import com.jasonsavlov.aaprototype.parser.id3.ID3Tag;
import com.jasonsavlov.aaprototype.parser.id3.ID3TagHeader;
import com.jasonsavlov.aaprototype.parser.id3.ID3TagParser;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SongDetailListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongDetailListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongDetailListFragment extends ListFragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_ALBUM = "_album";

    private static final String TAG = SongDetailListFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AAAlbum mAlbum;

    private AAMediaManager mainManager;

    private OnFragmentInteractionListener mListener;

    private ArrayAdapter<AASong> mAdapter;

    public SongDetailListFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongDetailListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongDetailListFragment newInstance(String albumName, String param2)
    {
        SongDetailListFragment fragment = new SongDetailListFragment();
        Bundle args = new Bundle();
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
            mAlbum = mainManager.getAlbumByName(getArguments().getString(ARG_ALBUM));

            if (mAlbum != null) {
                new GenerateSongListAdapterTask().execute(mAlbum);
            }
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void setAlbum(AAAlbum _album) {
        this.mAlbum = _album;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_detail_list, container, false);
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        Log.d(TAG, "onListItemClick: song clicked, " + mAdapter.getItem(position));

        // TODO: This is where the file magic happens
        File workingFile = new File(mAdapter.getItem(position).getFilePath());

        new FileTestTask().execute(workingFile);
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

    private class GenerateSongListAdapterTask extends AsyncTask<AAAlbum, Integer, Void> {
        public GenerateSongListAdapterTask()
        {
            super();
        }

        @Override
        protected Void doInBackground(AAAlbum... params)
        {
            AAAlbum workingAlbum = params[0];


            SongDetailListFragment.this.mAdapter = new ArrayAdapter<AASong>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, workingAlbum.getAlbumSongs()) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent)
                {
                    AASong wObj = this.getItem(position);

                    SongDetailRowView row = (SongDetailRowView) convertView;
                    if (row == null) {
                        row = SongDetailRowView.inflate(parent);
                    }
                    row.setSong(wObj);
                    return row;

                }
            };

            return null;
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(Void aVoid)
        {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            SongDetailListFragment.this.setListAdapter(mAdapter);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
        }
    }

    private class FileTestTask extends AsyncTask<File, Integer, Integer>
    {
        ID3TagHeader theHeader;
        ID3Tag theTag;
        @Override
        protected Integer doInBackground(File... params)
        {
            File workingFile = params[0];
            if (workingFile == null) {
                throw new NullPointerException("file passed to FileTestTask was null.");
            }

            Log.d(TAG, "doInBackground: file extension = " + Files.getFileExtension(workingFile.getName()));

            if (!Files.getFileExtension(workingFile.getName()).equalsIgnoreCase("mp3")) {
                return 2;
            }

            theTag = new ID3TagParser(workingFile).parseTags();

            if (theTag != null)
                return 1;

            return 0;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //Toast.makeText(getActivity(), "Starting search...", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onPreExecute: beginning search...");
        }

        @Override
        protected void onPostExecute(Integer integer)
        {
            super.onPostExecute(integer);

            switch (integer) {
                case 0:
                    Toast.makeText(getActivity(), "ID3 tag byte sequence *not* found", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onPostExecute: ID3 tag *not found*");
                    break;
                case 1:
                    Toast.makeText(getActivity(), "ID3 tag byte sequence found", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onPostExecute: tag..." + theTag);
                    Log.d(TAG, "onPostExecute: ID3 tag found");
                    break;
                case 2:
                    Toast.makeText(getActivity(), "File is not an MP3 file.", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onPostExecute: Could not find ID3 tag because the file is not an MP3 file.");
                    break;
                default:
                    Toast.makeText(getActivity(), "Something bizarre occurred. Check logs.", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "integer = " + integer + ". What does this mean?");
                    break;
            }
        }
    }
}
