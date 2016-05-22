package com.jasonsavlov.aaprototype.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jasonsavlov.aaprototype.ApplicationMain;
import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.obj.AAAlbum;
import com.jasonsavlov.aaprototype.obj.AAArtist;
import com.jasonsavlov.aaprototype.obj.AAMediaManager;
import com.jasonsavlov.aaprototype.ui.DetailAlbumTopFragment;
import com.jasonsavlov.aaprototype.ui.SongDetailListFragment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DetailAlbumActivity extends AppCompatActivity
        implements DetailAlbumTopFragment.OnFragmentInteractionListener,
        SongDetailListFragment.OnFragmentInteractionListener
{

    private final String TAG = getClass().getSimpleName();
    // Instance variables
    private AAAlbum mAlbum;
    private DetailAlbumTopFragment mTop;
    private SongDetailListFragment mList;
    private AAMediaManager mainManager;

    private ActionBar mActionBar;

    private final String userAgentString = "AAPrototype/0.1 (jason.savlov@gmail.com)";

    private final String LIMIT_EXCEEDED_KEY = "503 ERROR";

    public static List<Bitmap> imageList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainManager = ApplicationMain.getInstance().getMainMediaManager();

        setContentView(R.layout.activity_detail_album);

        mAlbum = mainManager.getAlbumByName(getIntent().getStringExtra(getString(R.string.album_identifier)));

        mActionBar = getSupportActionBar();
        mActionBar.setTitle(mAlbum.getAlbumName());
        mActionBar.setSubtitle(mAlbum.getAlbumArtistName());

        mTop = (DetailAlbumTopFragment) getSupportFragmentManager().findFragmentById(R.id.detail_top_fragment);
        mTop.setAlbum(mAlbum);
        mList = SongDetailListFragment.newInstance(mAlbum.getAlbumName(), null);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.detail_list_fragment, mList);
        ft.commit();


    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {
        Log.d(TAG, "onFragmentInteraction: uri = " + uri.toString());
    }

    public void artworkImageButtonClicked(View view)
    {
        // TODO: Implement initial steps in manipulating the artwork image
        Log.d(TAG, "artworkImageButtonClicked: button was clicked!");
        //Toast.makeText(this, "This feature is under construction...", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
        aBuilder.setTitle("Choose an Option...");
        aBuilder.setItems(R.array.artwork_button_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Log.d(TAG, "Item clicked: " + which);
                /*
                    This is where the action happens when the album artwork button is clicked.
                    The following options will pop up in the following order
                    (Index of 'which' used, starting at 0):
                    0: Download from Musicbrainz
                    1: Choose from photo library
                    2: Clear album artwork (if present)
                    3: Cancel (close the window and do nothing)
                 */

                switch (which) {
                    case 0:
                            new DownloadArtworkTask().execute(mAlbum);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        // This should never be reached except in the presence of an error
                        break;
                }

            }
        });
        aBuilder.show();


    }


    private class DownloadArtworkTask extends AsyncTask<AAAlbum,Integer,List<Bitmap>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Bitmap> list) {
            super.onPostExecute(list);

            if (list != null) {
                DetailAlbumActivity.imageList = list;
                //Toast.makeText(DetailAlbumActivity.this, "Artwork fetched successfully!", Toast.LENGTH_LONG).show();
                Intent artworkListIntent = new Intent(DetailAlbumActivity.this, ShowDownloadedArtworkActivity.class);
                DetailAlbumActivity.this.startActivity(artworkListIntent);
            } else {
                Toast.makeText(DetailAlbumActivity.this, "Error fetching album artwork. See logs.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<Bitmap> doInBackground(AAAlbum... params) {
            List<Bitmap> listOfBitmaps = new ArrayList<>();

            AAAlbum album = params[0];

            String albumName = album.getAlbumName();    // Get the artist name in a string

            // Create the String that will be used as the URL string in the HttpURLConnection
            String urlString;
            try {
                String artistID = getArtistID(album.getAlbumArtist());
                if (artistID.equals(LIMIT_EXCEEDED_KEY)) {
                    Log.d(TAG, "Error: limit exceeded");
                    return null;
                }
                urlString = "ws/2/release/?query=release:\""
                        + albumName + "\"" + " AND arid:" + artistID;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            urlString = urlString.replace("\"", "%22").replace(" ", "%20");


            URL mbUrl;
            try {
                mbUrl = new URL("http", "musicbrainz.org", urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            // Create a connection to MusicBrainz
            HttpURLConnection mbConection = null;
            try {
                mbConection = (HttpURLConnection) mbUrl.openConnection();
                mbConection.setRequestProperty("User-Agent", userAgentString);
                mbConection.connect();
                int status_code = mbConection.getResponseCode();
                Log.d(TAG, "Getting album list response code... status_code: " + status_code);
                if (status_code == 503) {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            // Create a DOM document builder
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlDocBuilder;
            try {
                xmlDocBuilder = docFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                return null;
            }

            // Create the DOM document
            Document xmlDocument;
            try {
                InputStream is = mbConection.getInputStream();
                xmlDocument = xmlDocBuilder.parse(is);
            } catch (SAXException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            mbConection.disconnect();

            Element releaseListElement = (Element) xmlDocument.getElementsByTagName("release-list").item(0);
            int releaseCount = Integer.parseInt(releaseListElement.getAttribute("count"));
            Log.d(TAG, "Release count is " + releaseCount);

            NodeList releaseNodes = releaseListElement.getElementsByTagName("release");

            List<String> releaseIDList = new ArrayList<>();
            List<Future<FetchedArtworkHolder>> futureList = new ArrayList<>();

            ExecutorService downloadImagesPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            for (int i = 0; i < releaseCount; i++) {
                Element workingElement = (Element) releaseNodes.item(i);
                String releaseID = workingElement.getAttribute("id");
                releaseIDList.add(releaseID);
                futureList.add(downloadImagesPool.submit(new FetchArtworkImageCallable(releaseID)));
            }

            downloadImagesPool.shutdown();

            for (Future future : futureList) {
                FetchedArtworkHolder artworkHolder;
                try {
                    artworkHolder = (FetchedArtworkHolder) future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                } catch (ExecutionException e) {
                    return null;
                }

                if (artworkHolder.fetchedBitmap == null) {
                    Log.d(TAG, "Bitmap is null, response code: " + artworkHolder.responseCode.toString());
                } else {
                    Log.d(TAG, "Bitmap success!");
                    listOfBitmaps.add(artworkHolder.fetchedBitmap);
                }
            }

            return listOfBitmaps;
        }

        private String getArtistID(AAArtist artist) throws IOException {
            String artistNameStr = artist.getArtistName();

            String urlString = "ws/2/artist/?query=artist:\"" + artistNameStr + "\"";

            urlString = urlString.replace("\"", "%22").replace(" ", "%20");

            URL mbUrl;
            try {
                mbUrl = new URL("http", "musicbrainz.org", urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            HttpURLConnection mbConnection = (HttpURLConnection) mbUrl.openConnection();
            mbConnection.setRequestProperty("User-Agent", userAgentString);
            mbConnection.setConnectTimeout(30*1000);
            mbConnection.connect();
            int status_code = mbConnection.getResponseCode();
            if (status_code == 503) {
                return LIMIT_EXCEEDED_KEY;
            }
            Log.d(TAG, "Getting artist ID response code... status_code: " + status_code);


            // Create a DOM document builder
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlDocBuilder;
            try {
                xmlDocBuilder = docFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                return null;
            }

            // Create the DOM document
            Document xmlDocument;
            try {
                InputStream is = mbConnection.getInputStream();
                xmlDocument = xmlDocBuilder.parse(is);
            } catch (SAXException e) {
                e.printStackTrace();
                return null;
            }

            mbConnection.disconnect();

            Element artistListElement = (Element) xmlDocument.getElementsByTagName("artist-list").item(0);
            int releaseCount = Integer.parseInt(artistListElement.getAttribute("count"));

            NodeList artistList = artistListElement.getElementsByTagName("artist");
            for (int i = 0; i < releaseCount; i++) {
                Element workingElement = (Element) artistList.item(i);
                String artistID = workingElement.getAttribute("id");
                Element artistNameElement = (Element) workingElement.getElementsByTagName("name").item(0);
                String rawName = artistNameElement.getTextContent();
                if (artistNameStr.equalsIgnoreCase(rawName)) {
                    return artistID;
                }
            }

            return null;
        }
    }

    private class FetchArtworkImageCallable implements Callable<FetchedArtworkHolder> {

        private String mbid;

        public FetchArtworkImageCallable(String mbid) {
            this.mbid = mbid;
        }

        @Override
        public FetchedArtworkHolder call() throws Exception {
            String queryURLStr = "release/" + mbid + "/front";

            URL queryURL = new URL("http", "coverartarchive.org", queryURLStr);

            HttpURLConnection mbConnection = (HttpURLConnection) queryURL.openConnection();
            mbConnection.setRequestProperty("User-Agent", userAgentString);
            mbConnection.connect();

            boolean redirected = false;

            int status_code = mbConnection.getResponseCode();

            /*
                find the status codes at https://musicbrainz.org/doc/Cover_Art_Archive/API#Summary_2
             */

            FetchedResponseCode responseCode = FetchedResponseCode.UNKNOWN;

            switch (status_code) {
                case 200:
                    Log.d(TAG, "FAAC response code 200");
                case 307: // The redirect succeeded!
                    responseCode = FetchedResponseCode.REDIRECT;
                    redirected = true;
                    break;
                case 400:
                    responseCode = FetchedResponseCode.CANNOT_PROCESS;
                    break;
                case 404:
                    responseCode = FetchedResponseCode.NOT_FOUND;
                    break;
                case 405:
                    responseCode = FetchedResponseCode.REQUEST_METHOD_ERROR;
                    break;
                case 503:
                    responseCode = FetchedResponseCode.EXCEEDED_LIMIT;
                    break;
                default:
                    // So the status code isn't anything we know about? WTF, man?!
                    Log.d(TAG, "Response code error. status_code = " + status_code);
                    break;
            }


            Bitmap imageBitmap = null;

            if (redirected) {
                InputStream is = mbConnection.getInputStream();
                imageBitmap = BitmapFactory.decodeStream(is);
                is.close();
                mbConnection.disconnect();
            }


            return new FetchedArtworkHolder(imageBitmap, responseCode);
        }
    }

    private enum FetchedResponseCode {
        UNKNOWN(0, "Unknown reason..."),
        REDIRECT(307, "redirect; community have decided upon a \"front\" image for this release"),
        CANNOT_PROCESS(400, "if {mbid} cannot be parsed as a valid UUID."),
        NOT_FOUND(404, "if there is either no release with this MBID, or the community have not chosen an image to represent the front of a release"),
        REQUEST_METHOD_ERROR(405, "the request method is not GET or HEAD"),
        EXCEEDED_LIMIT(503, "user has exceeded their rate limit");


        final int value;
        final String responseMeaning;

        FetchedResponseCode(int value, String meaning) {
            this.value = value;
            this.responseMeaning = meaning;
        }

        int getValue() {
            return this.value;
        }
    }

    private class FetchedArtworkHolder {
        final Bitmap fetchedBitmap;
        final FetchedResponseCode responseCode;

        FetchedArtworkHolder(Bitmap fetchedBitmap, FetchedResponseCode responseCode) {
            this.fetchedBitmap = fetchedBitmap;
            this.responseCode = responseCode;
        }

    }

}
