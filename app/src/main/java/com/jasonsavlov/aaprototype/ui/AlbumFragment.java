package com.jasonsavlov.aaprototype.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import android.support.v4.app.Fragment;

import com.jasonsavlov.aaprototype.activity.MainActivity;
import com.jasonsavlov.aaprototype.obj.AAAlbum;
import com.jasonsavlov.aaprototype.obj.AAArtist;
import com.jasonsavlov.aaprototype.obj.AAMediaManager;
import com.jasonsavlov.aaprototype.ApplicationMain;
import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.obj.AlbumDateComparator;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class AlbumFragment extends ListFragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CLICKED_ARTIST_NAME = "clicked_artist_name";
    private static final String ARG_ARTIST_ALBUM_LIST = "list_of_albums";

    // TODO: Rename and change types of parameters
    private String mClickedArtistName;
    private ArrayList<AAAlbum> listOfAlbums;
    private View gView;

    private boolean shouldShowDialog = true;

    private ProgressDialog loadingDialog;

    private static final String MESSAGE = "Loading...";


    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    private AAMediaManager mMediaManager;

    private boolean isShownInArtistTab = false; // For potential future use
    private static final String TAG = "AlbumFragment";

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter<AAAlbum> mAdapter;

    // TODO: Rename and change types of parameters
    public static AlbumFragment newInstance(String clickedArtistName, ArrayList<AAAlbum> albums) {

        Log.d(TAG, "newInstance: clickedArtistName = " + clickedArtistName);
        Log.d(TAG, "newInstance: albums = " + albums);

        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CLICKED_ARTIST_NAME, clickedArtistName);

        if (albums != null)
            args.putSerializable(ARG_ARTIST_ALBUM_LIST, albums);

        fragment.setArguments(args);
        fragment.setMediaManager(ApplicationMain.getInstance().getMainMediaManager());

        if (clickedArtistName.equals("all")) {
            fragment.setmClickedArtistName("all");
            fragment.shouldShowDialog = false;
            //fragment.initialize();
        }

        return fragment;
    }


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlbumFragment() {
    }

    public void setMediaManager(AAMediaManager aam) {
        this.mMediaManager = aam;
    }

    // TODO: Add toString method

    public void setLoadingDialog(ProgressDialog pd) {
        this.loadingDialog = pd;
    }

    public void setmClickedArtistName(String mClickedArtistName)
    {
        if (this.mClickedArtistName != null)
            throw new AlreadySetupException("mClickedArtistName is already set.");

        this.mClickedArtistName = mClickedArtistName;
    }

    public void setListOfAlbums(ArrayList<AAAlbum> listOfAlbums)
    {
        if (this.listOfAlbums != null)
            throw new AlreadySetupException("listOfAlbums is already set.");

        this.listOfAlbums = listOfAlbums;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");
        this.loadingDialog = new ProgressDialog(getActivity());

        if (shouldShowDialog) {
            Log.d(TAG, "onCreate: All is good to show that progress dialog...");
            this.loadingDialog.setMessage(MESSAGE);
            this.loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

    }

    public final void initialize() {
        if (mClickedArtistName == null)
            throw new NotSetupException("Cannot initialize: mClickedArtistName is null.");

        if (listOfAlbums == null && !mClickedArtistName.equals("all"))
            throw new NotSetupException("Cannot initialize: listOfAlbums is null.");

        new GenerateAdapterTask().execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView()");
        Log.d(TAG, "onCreateView: mClickedArtistName = " + mClickedArtistName);
        // Try setting the mediamanager here, for shits and gigs...
        mMediaManager = ApplicationMain.getInstance().getMainMediaManager();

        View view = inflater.inflate(R.layout.fragment_album_list, container, false);


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");

        if (shouldShowDialog)
            this.loadingDialog.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart: ");
        this.initialize();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction((AAAlbum) mAdapter.getItem(position));
            AlbumRowView mView = (AlbumRowView) view;
            AAAlbum album = mView.getAlbum();

            // TODO: Implement the album detail window

            Log.v(TAG, "---- Album clicked ----");
            Log.v(TAG, "Name: " + album.getAlbumName());
            Log.v(TAG, "Font size: " + mView.getAlbumNameFontSize());
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(AAAlbum album);
    }

    // holder class for the row view
    // TODO: Remove holder class
    private static class AlbumHolder {
        // View instance references
        ImageView albumArtworkImage;
        TextView albumNameView;
        TextView artistNameView;

        // Property instance references


    }


    private class GenerateAdapterTask extends AsyncTask<Object, Integer, Void> {

        LayoutInflater layoutInflater;
        ViewGroup viewGroupContainer;

        private static final String MESSAGE = "Loading...";


        public GenerateAdapterTask()
        {
            super();
        }

        @Override
        protected Void doInBackground(Object... params)
        {
            Log.d(getClass().getSimpleName(), "doInBackground()");
            ArrayList<AAAlbum> albumsToShow = new ArrayList<>();

            if (listOfAlbums != null) {
                albumsToShow = listOfAlbums;
            } else {
                albumsToShow = mMediaManager.getFullListOfAlbums();
            }

            if (getActivity().getClass().getSimpleName().equals(MainActivity.class.getSimpleName()))
            {
                Log.d(TAG, "doInBackground: calling from MainActivity");
                Collections.sort(albumsToShow, AlbumDateComparator.INSTANCE);
            }
            else
            {
                Log.d(TAG, "doInBackground: calling from outside MainActivity, " + getActivity().getClass().getSimpleName());
            }

            AlbumFragment.this.mAdapter = new ArrayAdapter<AAAlbum>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, albumsToShow) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent)
                {
                    //Log.d("getView() in mAdapter", "getView called");
                    AlbumRowView row = (AlbumRowView) convertView;
                    if (row == null) {
                        row = AlbumRowView.inflate(parent);
                    }
                    row.setAlbum(this.getItem(position));
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
            Log.d(getClass().getSimpleName(), "onPostExecute()");

            super.onPostExecute(aVoid);

            //AlbumFragment.this.mAdapter.notifyDataSetChanged();
            try {
                AlbumFragment.this.setListAdapter(AlbumFragment.this.mAdapter);
            } catch (NullPointerException ex) {
                Log.i(TAG, "onPostExecute: Could not set the list adapter, the objects are null.", ex);
            }

            AlbumFragment.this.getListView().setOnItemClickListener(AlbumFragment.this);


            try {
                AlbumFragment.this.loadingDialog.dismiss();
            } catch (NullPointerException ex) {
                Log.d(TAG, "onPostExecute: loadingDialog is null.", ex);
            }

        }

        @Override
        protected void onPreExecute()
        {
            Log.d(getClass().getSimpleName(), "onPreExecute()");

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
        }
    }
}

class AlreadySetupException extends RuntimeException {
    public AlreadySetupException(String detailMessage) {
        super(detailMessage);
    }
}

class NotSetupException extends RuntimeException {
    public NotSetupException(String detailMessage) {
        super(detailMessage);
    }
}

