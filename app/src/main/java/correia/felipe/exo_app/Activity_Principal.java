package correia.felipe.exo_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.image.SmartImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Felipe on 18/08/2017.
 */



public class Activity_Principal extends AppCompatActivity {
    private static final String TAG = Activity_Principal.class.getSimpleName();


    private GridView mGridView, mGridView2, mGridView3, mGridView4, mGridView5;
    private ProgressBar mProgressBar;
    private TextView txt_key_0,txt_key_1, txt_key_2, txt_key_3;

    private GridViewAdapter mGridAdapter, mGridAdapter2, mGridAdapter3, mGridAdapter4;
    private ArrayList<VideoItem> mGridData, mGridData2,mGridData3,mGridData4;
    private GridViewAdapterTop mGridAdapterTop;
    private ArrayList<TopItem> mGridDataTop;
    private ImageView thumb_principal;
    private String FEED_URL = "http://blessp.azurewebsites.net/api/dependence/";
    //private String FEED_URL = "http://192.168.0.14:8000/api/dependence/";
    //private String FEED_URL = "http://javatechig.com/?json=get_recent_posts&count=45";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_principal);
        final String token = getIntent().getStringExtra("token");




        mGridView = (GridView)findViewById(R.id.gridView);
        mGridView2 = (GridView)findViewById(R.id.gridView_2);
        mGridView3 = (GridView)findViewById(R.id.gridView_3);
        mGridView4 = (GridView)findViewById(R.id.gridView_4);
        mGridView5 = (GridView)findViewById(R.id.gridView_5);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);


        txt_key_0 = (TextView)findViewById(R.id.textView_Key0);
        txt_key_1 = (TextView)findViewById(R.id.textView_Key1);
        txt_key_2 = (TextView)findViewById(R.id.textView_Key2);
        txt_key_3 = (TextView)findViewById(R.id.textView_Key3);





        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridData2 = new ArrayList<>();
        mGridData3 = new ArrayList<>();
        mGridData4 = new ArrayList<>();
        mGridDataTop = new ArrayList<>();

        mGridView.setNumColumns(8);
        mGridView2.setNumColumns(8);
        mGridView3.setNumColumns(8);
        mGridView4.setNumColumns(8);
        mGridView5.setNumColumns(1);

        mGridAdapter = new GridViewAdapter(this, R.layout.video_item, mGridData);
        mGridView.setAdapter(mGridAdapter);

        mGridAdapter2 = new GridViewAdapter(this, R.layout.video_item, mGridData2);
        mGridView2.setAdapter(mGridAdapter2);

        mGridAdapter3 = new GridViewAdapter(this, R.layout.video_item, mGridData3);
        mGridView3.setAdapter(mGridAdapter3);

        mGridAdapter4 = new GridViewAdapter(this, R.layout.video_item, mGridData4);
        mGridView4.setAdapter(mGridAdapter4);

        mGridAdapterTop = new GridViewAdapterTop(this, R.layout.top_item, mGridDataTop);
        mGridView5.setAdapter(mGridAdapterTop);

        //Grid view click event


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                VideoItem item = (VideoItem) parent.getItemAtPosition(position);
                String serie = item.getSerie();
                int serieNum = -1;
                try{
                    serieNum = Integer.parseInt(serie);
                } catch(NumberFormatException nfe) {
                    // Handle parse error.
                }
                Log.d("setOnClickListener", "Serie: " + serie);
                Log.d("setOnClickListener", "Serie Number: " + serieNum);

                if(serieNum == 0) {
                    Intent intent = new Intent(Activity_Principal.this, DetailsActivity.class);
                    SmartImageView imageView = (SmartImageView) v.findViewById(R.id.video_item_image);

                    // Interesting data to pass across are the thumbnail size/location, the
                    // resourceId of the source bitmap, the picture description, and the
                    // orientation (to avoid returning back to an obsolete configuration if
                    // the device rotates again in the meantime)

                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);

                    //Pass the image title and url to DetailsActivity
                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", imageView.getWidth()).
                            putExtra("height", imageView.getHeight()).
                            putExtra("id", item.getId()).
                            putExtra("title", item.getTitle()).
                            putExtra("thumb", item.getImage()).
                            putExtra("cover", item.getCover()).
                            putExtra("description", item.getDescription()).
                            putExtra("trailer", item.getTrailer()).
                            putExtra("duration", item.getDuration()).
                            putExtra("year", item.getYear()).
                            putExtra("serie", item.getSerie()).
                            putExtra("token", token);

                    //Start details activity
                    startActivity(intent);
                } else{

                    Intent intent = new Intent(Activity_Principal.this, Details_Activity_Serie.class);
                    SmartImageView imageView = (SmartImageView) v.findViewById(R.id.video_item_image);

                    // Interesting data to pass across are the thumbnail size/location, the
                    // resourceId of the source bitmap, the picture description, and the
                    // orientation (to avoid returning back to an obsolete configuration if
                    // the device rotates again in the meantime)

                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);

                    //Pass the image title and url to DetailsActivity
                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", imageView.getWidth()).
                            putExtra("height", imageView.getHeight()).
                            putExtra("id", item.getId()).
                            putExtra("title", item.getTitle()).
                            putExtra("thumb", item.getImage()).
                            putExtra("cover", item.getCover()).
                            putExtra("description", item.getDescription()).
                            putExtra("trailer", item.getTrailer()).
                            putExtra("duration", item.getDuration()).
                            putExtra("year", item.getYear()).
                            putExtra("serie", item.getSerie()).
                            putExtra("token", token);

                    //Start details activity
                    startActivity(intent);

                }

            }
        });

        mGridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                VideoItem item = (VideoItem) parent.getItemAtPosition(position);
                String serie = item.getSerie();
                int serieNum = -1;
                try{
                    serieNum = Integer.parseInt(serie);
                } catch(NumberFormatException nfe) {
                    // Handle parse error.
                }
                Log.d("setOnClickListener", "Serie: " + serie);
                Log.d("setOnClickListener", "Serie Number: " + serieNum);

                if(serieNum == 0) {
                    Intent intent = new Intent(Activity_Principal.this, DetailsActivity.class);
                    SmartImageView imageView = (SmartImageView) v.findViewById(R.id.video_item_image);

                    // Interesting data to pass across are the thumbnail size/location, the
                    // resourceId of the source bitmap, the picture description, and the
                    // orientation (to avoid returning back to an obsolete configuration if
                    // the device rotates again in the meantime)

                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);

                    //Pass the image title and url to DetailsActivity
                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", imageView.getWidth()).
                            putExtra("height", imageView.getHeight()).
                            putExtra("id", item.getId()).
                            putExtra("title", item.getTitle()).
                            putExtra("thumb", item.getImage()).
                            putExtra("cover", item.getCover()).
                            putExtra("description", item.getDescription()).
                            putExtra("trailer", item.getTrailer()).
                            putExtra("duration", item.getDuration()).
                            putExtra("year", item.getYear()).
                            putExtra("serie", item.getSerie()).
                            putExtra("token", token);

                    //Start details activity
                    startActivity(intent);
                } else{

                    Intent intent = new Intent(Activity_Principal.this, Details_Activity_Serie.class);
                    SmartImageView imageView = (SmartImageView) v.findViewById(R.id.video_item_image);

                    // Interesting data to pass across are the thumbnail size/location, the
                    // resourceId of the source bitmap, the picture description, and the
                    // orientation (to avoid returning back to an obsolete configuration if
                    // the device rotates again in the meantime)

                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);

                    //Pass the image title and url to DetailsActivity
                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", imageView.getWidth()).
                            putExtra("height", imageView.getHeight()).
                            putExtra("id", item.getId()).
                            putExtra("title", item.getTitle()).
                            putExtra("thumb", item.getImage()).
                            putExtra("cover", item.getCover()).
                            putExtra("description", item.getDescription()).
                            putExtra("trailer", item.getTrailer()).
                            putExtra("duration", item.getDuration()).
                            putExtra("year", item.getYear()).
                            putExtra("serie", item.getSerie()).
                            putExtra("token", token);

                    //Start details activity
                    startActivity(intent);

                }

            }
        });
        mGridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                VideoItem item = (VideoItem) parent.getItemAtPosition(position);
                String serie = item.getSerie();
                int serieNum = -1;
                try{
                    serieNum = Integer.parseInt(serie);
                } catch(NumberFormatException nfe) {
                    // Handle parse error.
                }
                Log.d("setOnClickListener", "Serie: " + serie);
                Log.d("setOnClickListener", "Serie Number: " + serieNum);

                if(serieNum == 0) {
                    Intent intent = new Intent(Activity_Principal.this, DetailsActivity.class);
                    SmartImageView imageView = (SmartImageView) v.findViewById(R.id.video_item_image);

                    // Interesting data to pass across are the thumbnail size/location, the
                    // resourceId of the source bitmap, the picture description, and the
                    // orientation (to avoid returning back to an obsolete configuration if
                    // the device rotates again in the meantime)

                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);

                    //Pass the image title and url to DetailsActivity
                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", imageView.getWidth()).
                            putExtra("height", imageView.getHeight()).
                            putExtra("id", item.getId()).
                            putExtra("title", item.getTitle()).
                            putExtra("thumb", item.getImage()).
                            putExtra("cover", item.getCover()).
                            putExtra("description", item.getDescription()).
                            putExtra("trailer", item.getTrailer()).
                            putExtra("duration", item.getDuration()).
                            putExtra("year", item.getYear()).
                            putExtra("serie", item.getSerie()).
                            putExtra("token", token);

                    //Start details activity
                    startActivity(intent);
                } else{

                    Intent intent = new Intent(Activity_Principal.this, Details_Activity_Serie.class);
                    SmartImageView imageView = (SmartImageView) v.findViewById(R.id.video_item_image);

                    // Interesting data to pass across are the thumbnail size/location, the
                    // resourceId of the source bitmap, the picture description, and the
                    // orientation (to avoid returning back to an obsolete configuration if
                    // the device rotates again in the meantime)

                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);

                    //Pass the image title and url to DetailsActivity
                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", imageView.getWidth()).
                            putExtra("height", imageView.getHeight()).
                            putExtra("id", item.getId()).
                            putExtra("title", item.getTitle()).
                            putExtra("thumb", item.getImage()).
                            putExtra("cover", item.getCover()).
                            putExtra("description", item.getDescription()).
                            putExtra("trailer", item.getTrailer()).
                            putExtra("duration", item.getDuration()).
                            putExtra("year", item.getYear()).
                            putExtra("serie", item.getSerie()).
                            putExtra("token", token);

                    //Start details activity
                    startActivity(intent);

                }

            }
        });
        mGridView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                VideoItem item = (VideoItem) parent.getItemAtPosition(position);
                String serie = item.getSerie();
                int serieNum = -1;
                try{
                    serieNum = Integer.parseInt(serie);
                } catch(NumberFormatException nfe) {
                    // Handle parse error.
                }
                Log.d("setOnClickListener", "Serie: " + serie);
                Log.d("setOnClickListener", "Serie Number: " + serieNum);

                if(serieNum == 0) {
                    Intent intent = new Intent(Activity_Principal.this, DetailsActivity.class);
                    SmartImageView imageView = (SmartImageView) v.findViewById(R.id.video_item_image);

                    // Interesting data to pass across are the thumbnail size/location, the
                    // resourceId of the source bitmap, the picture description, and the
                    // orientation (to avoid returning back to an obsolete configuration if
                    // the device rotates again in the meantime)

                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);

                    //Pass the image title and url to DetailsActivity
                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", imageView.getWidth()).
                            putExtra("height", imageView.getHeight()).
                            putExtra("id", item.getId()).
                            putExtra("title", item.getTitle()).
                            putExtra("thumb", item.getImage()).
                            putExtra("cover", item.getCover()).
                            putExtra("description", item.getDescription()).
                            putExtra("trailer", item.getTrailer()).
                            putExtra("duration", item.getDuration()).
                            putExtra("year", item.getYear()).
                            putExtra("serie", item.getSerie()).
                            putExtra("token", token);

                    //Start details activity
                    startActivity(intent);
                } else{

                    Intent intent = new Intent(Activity_Principal.this, Details_Activity_Serie.class);
                    SmartImageView imageView = (SmartImageView) v.findViewById(R.id.video_item_image);

                    // Interesting data to pass across are the thumbnail size/location, the
                    // resourceId of the source bitmap, the picture description, and the
                    // orientation (to avoid returning back to an obsolete configuration if
                    // the device rotates again in the meantime)

                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);

                    //Pass the image title and url to DetailsActivity
                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", imageView.getWidth()).
                            putExtra("height", imageView.getHeight()).
                            putExtra("id", item.getId()).
                            putExtra("title", item.getTitle()).
                            putExtra("thumb", item.getImage()).
                            putExtra("cover", item.getCover()).
                            putExtra("description", item.getDescription()).
                            putExtra("trailer", item.getTrailer()).
                            putExtra("duration", item.getDuration()).
                            putExtra("year", item.getYear()).
                            putExtra("serie", item.getSerie()).
                            putExtra("token", token);

                    //Start details activity
                    startActivity(intent);

                }
            }
        });
        mGridView5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                TopItem item = (TopItem) parent.getItemAtPosition(position);
                String serie = item.getSerie();
                int serieNum = -1;
                try{
                    serieNum = Integer.parseInt(serie);
                } catch(NumberFormatException nfe) {
                    // Handle parse error.
                }
                Log.d("setOnClickListener", "Serie: " + serie);
                Log.d("setOnClickListener", "Serie Number: " + serieNum);

                if(serieNum == 0) {
                    Intent intent = new Intent(Activity_Principal.this, DetailsActivity.class);
                    SmartImageView imageView = (SmartImageView) v.findViewById(R.id.videoTop_item_image);

                    // Interesting data to pass across are the thumbnail size/location, the
                    // resourceId of the source bitmap, the picture description, and the
                    // orientation (to avoid returning back to an obsolete configuration if
                    // the device rotates again in the meantime)

                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);

                    //Pass the image title and url to DetailsActivity
                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", imageView.getWidth()).
                            putExtra("height", imageView.getHeight()).
                            putExtra("id", item.getId()).
                            putExtra("title", item.getTitle()).
                            putExtra("thumb", item.getImage()).
                            putExtra("cover", item.getCover()).
                            putExtra("description", item.getDescription()).
                            putExtra("trailer", item.getTrailer()).
                            putExtra("duration", item.getDuration()).
                            putExtra("year", item.getYear()).
                            putExtra("serie", item.getSerie()).
                            putExtra("token", token);

                    //Start details activity
                    startActivity(intent);
                } else{

                    Intent intent = new Intent(Activity_Principal.this, Details_Activity_Serie.class);
                    SmartImageView imageView = (SmartImageView) v.findViewById(R.id.videoTop_item_image);

                    // Interesting data to pass across are the thumbnail size/location, the
                    // resourceId of the source bitmap, the picture description, and the
                    // orientation (to avoid returning back to an obsolete configuration if
                    // the device rotates again in the meantime)

                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);

                    //Pass the image title and url to DetailsActivity
                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", imageView.getWidth()).
                            putExtra("height", imageView.getHeight()).
                            putExtra("id", item.getId()).
                            putExtra("title", item.getTitle()).
                            putExtra("thumb", item.getImage()).
                            putExtra("cover", item.getCover()).
                            putExtra("description", item.getDescription()).
                            putExtra("trailer", item.getTrailer()).
                            putExtra("duration", item.getDuration()).
                            putExtra("year", item.getYear()).
                            putExtra("serie", item.getSerie()).
                            putExtra("token", token);

                    //Start details activity
                    startActivity(intent);
                }
            }
        });



        //Start download
        new AsyncHttpTask().execute(FEED_URL);
        mProgressBar.setVisibility(View.VISIBLE);
    }


    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            //Integer result;
            Integer statusCode = 0;
            String tokenParser = getIntent().getStringExtra("token");

            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                httpGet.setHeader("Authorization",
                        "Bearer" + tokenParser);
                HttpResponse httpResponse = httpclient.execute(httpGet);
                //HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));

                String response = streamToString(httpResponse.getEntity().getContent());
                Log.d("doInBackground", "Response: " + response);
                makeTitle(response);
                parseResult(response);

                statusCode = httpResponse.getStatusLine().getStatusCode();

                /*
                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = statusCode; // Successful
                } else {
                    result = 0; //"Failed
                }*/
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return statusCode;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Lets update UI

            if (result == 200) {
                Toast.makeText(Activity_Principal.this, "Status Code: "+result, Toast.LENGTH_SHORT).show();
                mGridAdapter.setGridData(mGridData);
                mGridAdapter2.setGridData(mGridData2);
                mGridAdapter3.setGridData(mGridData3);
                mGridAdapter4.setGridData(mGridData4);
                mGridAdapterTop.setGridData(mGridDataTop);
            } else {
                Toast.makeText(Activity_Principal.this, "Status Code: "+result, Toast.LENGTH_SHORT).show();
                //Toast.makeText(Activity_Principal.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }

            //Hide progressbar
            mProgressBar.setVisibility(View.GONE);
        }
    }


    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }

    /**
     * Parsing the feed results and get the list
     *
     * @param result
     */
    //parser do json da API
    private void parseResult(String result) throws IOException {
        try {
            JSONObject response = new JSONObject(result);
            VideoItem item = null;
            TopItem topItem = null;
            Iterator<?> iterator = response.keys();
            Log.d("Parser", "PARSER RESULT ");
            int c = 0;
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                Log.d("Parser", "JSON key: " + key);
                Log.d("Parser", "JSON Parser count: " + c);

                if(c == 0) {
                    Object obj = response.get(key);

                    JSONObject jsonObject = (JSONObject) obj;
                    Log.d("Parser", "JSON TOP OBJECT: " + jsonObject);

                    String id = jsonObject.getString("id");
                    Log.d("Parser", "JSON Id: " + id);
                    String title = jsonObject.getString("title");
                    Log.d("Parser", "JSON Title: " + title);
                    String thumb = jsonObject.getString("thumb");
                    Log.d("Parser", "JSON Thumb: " + thumb);
                    String cover = jsonObject.getString("cover");
                    Log.d("Parser", "JSON Cover: " + cover);
                    String description = jsonObject.getString("description");
                    Log.d("Parser", "JSON Description: " + description);
                    String trailer = jsonObject.getString("trailer");
                    Log.d("Parser", "JSON Trailer: " + trailer);
                    String duration = jsonObject.getString("duration");
                    Log.d("Parser", "JSON Duration: " + duration);
                    String year = jsonObject.getString("year");
                    Log.d("Parser", "JSON Year: " + year);
                    String serie = jsonObject.getString("serie");
                    Log.d("Parser", "JSON Serie: " + serie);
                    topItem = new TopItem();
                    topItem.setId(id);
                    topItem.setTitle(title);
                    topItem.setImage(thumb);
                    topItem.setCover(cover);
                    topItem.setDescription(description);
                    topItem.setTrailer(trailer);
                    topItem.setDuration(duration);
                    topItem.setYear(year);
                    topItem.setSerie(serie);



                    mGridDataTop.add(topItem);

                }else if(c == 1) {
                    Object o = response.get(key);
                    if (o instanceof JSONArray) {
                        JSONArray jsonA = (JSONArray) o;
                        Log.d("Parser", "JSON Array: " + jsonA);
                        int numberOfItems = jsonA.length();
                        for (int i = 0; i < numberOfItems; i++) {

                            JSONObject jsonO = jsonA.optJSONObject(i);
                            Log.d("Parser", "JSON Object: " + jsonO);

                            String id = jsonO.getString("id");
                            Log.d("Parser", "JSON Id: " + id);
                            String title = jsonO.getString("title");
                            Log.d("Parser", "JSON Title: " + title);
                            String thumb = jsonO.getString("thumb");
                            Log.d("Parser", "JSON Thumb: " + thumb);
                            String description = jsonO.getString("description");
                            Log.d("Parser", "JSON Thumb: " + description);
                            String cover = jsonO.getString("cover");
                            Log.d("Parser", "JSON Cover: " + cover);
                            String trailer = jsonO.getString("trailer");
                            Log.d("Parser", "JSON Trailer: " + trailer);
                            String duration = jsonO.getString("duration");
                            Log.d("Parser", "JSON Duration: " + duration);
                            String year = jsonO.getString("year");
                            Log.d("Parser", "JSON Year: " + year);
                            String serie = jsonO.getString("serie");
                            Log.d("Parser", "JSON Serie: " + serie);

                            /*if(serie != "0"){
                                JSONObject seasons = jsonO.getJSONObject("serie");
                                Log.d("Parser", "JSONArray TEMPORADAS: " + seasons);
                            }*/


                            item = new VideoItem();
                            item.setTitle(title);
                            item.setImage(thumb);
                            item.setDescription(description);
                            item.setDuration(duration);
                            item.setCover(cover);
                            item.setTrailer(trailer);
                            item.setYear(year);
                            item.setSerie(serie);
                            item.setId(id);

                            mGridData.add(item);
//                        }
                        }
                    }
                } else if(c == 2) {
                    Object o = response.get(key);
                    if (o instanceof JSONArray) {
                        JSONArray jsonA = (JSONArray) o;
                        Log.d("Parser", "JSON Array: " + jsonA);
                        int numberOfItems = jsonA.length();
                        for (int i = 0; i < numberOfItems; i++) {

                            JSONObject jsonO = jsonA.optJSONObject(i);
                            Log.d("Parser", "JSON Object: " + jsonO);

                            String id = jsonO.getString("id");
                            Log.d("Parser", "JSON Id: " + id);
                            String title = jsonO.getString("title");
                            Log.d("Parser", "JSON Title: " + title);
                            String thumb = jsonO.getString("thumb");
                            Log.d("Parser", "JSON Thumb: " + thumb);
                            String description = jsonO.getString("description");
                            Log.d("Parser", "JSON Thumb: " + description);
                            String cover = jsonO.getString("cover");
                            Log.d("Parser", "JSON Cover: " + cover);
                            String trailer = jsonO.getString("trailer");
                            Log.d("Parser", "JSON Trailer: " + trailer);
                            String duration = jsonO.getString("duration");
                            Log.d("Parser", "JSON Duration: " + duration);
                            String year = jsonO.getString("year");
                            Log.d("Parser", "JSON Year: " + year);
                            String serie = jsonO.getString("serie");
                            Log.d("Parser", "JSON Serie: " + serie);


                            item = new VideoItem();
                            item.setTitle(title);
                            item.setImage(thumb);
                            item.setDescription(description);
                            item.setDuration(duration);
                            item.setCover(cover);
                            item.setTrailer(trailer);
                            item.setYear(year);
                            item.setSerie(serie);
                            item.setId(id);

                            mGridData2.add(item);
//                        }
                        }
                    }
                } else if(c == 3) {
                    Object o = response.get(key);
                    if (o instanceof JSONArray) {
                        JSONArray jsonA = (JSONArray) o;
                        Log.d("Parser", "JSON Array: " + jsonA);
                        int numberOfItems = jsonA.length();
                        for (int i = 0; i < numberOfItems; i++) {

                            JSONObject jsonO = jsonA.optJSONObject(i);
                            Log.d("Parser", "JSON Object: " + jsonO);

                            String id = jsonO.getString("id");
                            Log.d("Parser", "JSON Id: " + id);
                            String title = jsonO.getString("title");
                            Log.d("Parser", "JSON Title: " + title);
                            String thumb = jsonO.getString("thumb");
                            Log.d("Parser", "JSON Thumb: " + thumb);
                            String description = jsonO.getString("description");
                            Log.d("Parser", "JSON Thumb: " + description);
                            String cover = jsonO.getString("cover");
                            Log.d("Parser", "JSON Cover: " + cover);
                            String trailer = jsonO.getString("trailer");
                            Log.d("Parser", "JSON Trailer: " + trailer);
                            String duration = jsonO.getString("duration");
                            Log.d("Parser", "JSON Duration: " + duration);
                            String year = jsonO.getString("year");
                            Log.d("Parser", "JSON Year: " + year);
                            String serie = jsonO.getString("serie");
                            Log.d("Parser", "JSON Serie: " + serie);

                            item = new VideoItem();
                            item.setTitle(title);
                            item.setImage(thumb);
                            item.setDescription(description);
                            item.setDuration(duration);
                            item.setCover(cover);
                            item.setTrailer(trailer);
                            item.setYear(year);
                            item.setSerie(serie);
                            item.setId(id);

                            mGridData3.add(item);
//                        }
                        }
                    }
                }else if(c == 4) {
                    Object o = response.get(key);
                    if (o instanceof JSONArray) {
                        JSONArray jsonA = (JSONArray) o;
                        Log.d("Parser", "JSON Array: " + jsonA);
                        int numberOfItems = jsonA.length();
                        for (int i = 0; i < numberOfItems; i++) {

                            JSONObject jsonO = jsonA.optJSONObject(i);
                            Log.d("Parser", "JSON Object: " + jsonO);

                            String id = jsonO.getString("id");
                            Log.d("Parser", "JSON Id: " + id);
                            String title = jsonO.getString("title");
                            Log.d("Parser", "JSON Title: " + title);
                            String thumb = jsonO.getString("thumb");
                            Log.d("Parser", "JSON Thumb: " + thumb);
                            String description = jsonO.getString("description");
                            Log.d("Parser", "JSON Thumb: " + description);
                            String cover = jsonO.getString("cover");
                            Log.d("Parser", "JSON Cover: " + cover);
                            String trailer = jsonO.getString("trailer");
                            Log.d("Parser", "JSON Trailer: " + trailer);
                            String duration = jsonO.getString("duration");
                            Log.d("Parser", "JSON Duration: " + duration);
                            String year = jsonO.getString("year");
                            Log.d("Parser", "JSON Year: " + year);
                            String serie = jsonO.getString("serie");
                            Log.d("Parser", "JSON Serie: " + serie);



                            item = new VideoItem();
                            item.setTitle(title);
                            item.setImage(thumb);
                            item.setDescription(description);
                            item.setDuration(duration);
                            item.setCover(cover);
                            item.setTrailer(trailer);
                            item.setYear(year);
                            item.setSerie(serie);
                            item.setId(id);

                            mGridData4.add(item);
//                        }
                        }
                    }
                }
                c++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void makeTitle(final String result) throws IOException {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject response = new JSONObject(result);
                    Iterator<?> iterator = response.keys();
                    Log.d("Parser", "MAKE TITLE ");
                    int count = 0;
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        Log.d("Parser", "JSON Title key: " + key);
                        Log.d("Parser", "count: " + count);
                        if(count ==1){
                            txt_key_0.setText(key);
                        }else if(count == 2){
                            txt_key_1.setText(key);
                        }else if(count == 3){
                            txt_key_2.setText(key);
                        }else if(count == 4){
                            txt_key_3.setText(key);
                        }
                        count++;
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }


            }
        });


    }

    //Parse do json do exemplo


    /*private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("posts");
            VideoItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");
                item = new VideoItem();
                item.setTitle(title);
                JSONArray attachments = post.getJSONArray("attachments");
                if (null != attachments && attachments.length() > 0) {
                    JSONObject attachment = attachments.getJSONObject(0);
                    if (attachment != null)
                        item.setImage(attachment.getString("url"));
                }
                mGridData.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //possivel solução
    private void parseResult(String result) throws IOException {
        try {
            Toast.makeText(Activity_Principal.this, "kkk", Toast.LENGTH_SHORT).show();
            JSONArray jsonArray = new JSONArray(result);
            VideoItem item;
            for(int i=0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                JSONArray lancamento = object.getJSONArray("lançamento");
                for(int j=0;j<lancamento.length();j++){
                    JSONObject lancamento_type = lancamento.getJSONObject(j);
                    String title = lancamento_type.getString("title");
                    String thumb = lancamento_type.getString("thumb");
                    URL url = new URL(thumb);
                    Bitmap thumb_bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    item = new VideoItem();
                    item.setTitle(title);
                    if(thumb_bmp != null){
                        item.setImage(thumb_bmp);
                    }
                    mGridData.add(item);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    */
}