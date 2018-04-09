package praktijkopdracht.prg027.mark.hr.smarthome;

import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    static final String LOG_TAG = "jsonDataTest";
    static final String API_URL = "http://markvonk.com:8000/api/devices";

    // set up ArrayLists
    ArrayList<String> ids       = new ArrayList<>();
    ArrayList<String> titles    = new ArrayList<>();
    ArrayList<String> types     = new ArrayList<>();
    ArrayList<String> rooms     = new ArrayList<>();
    ArrayList<String> states    = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Call Volley function with api url
        volleyJsonObjectRequest(API_URL);
    }

    class CustomAdapter extends BaseAdapter {
        String[] IDS    = ids.toArray(new String[ids.size()]);
        String[] TITLES = titles.toArray(new String[titles.size()]);
        String[] TYPES  = types.toArray(new String[types.size()]);
        String[] ROOMS  = rooms.toArray(new String[rooms.size()]);
        String[] STATES = states.toArray(new String[states.size()]);

        @Override
        public int getCount() {
            return IDS.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.customlayout,null);

//            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
            TextView textView_name          = (TextView)view.findViewById(R.id.textView_name);
            TextView textView_description   = (TextView)view.findViewById(R.id.textView_description);

//            imageView.setImageResource(IMAGES[i]);
            textView_name.setText(TITLES[i]);
            textView_description.setText(IDS[i]);

            return view;
        }
    }

    // Get json data from web API using Volley
    public void volleyJsonObjectRequest(String url){

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            // Log entire fetched json
//                        Log.d("jsonDataTest", response.toString());

                        try {
                            // get all 'items' from json array
                            JSONArray jsonData = response.getJSONArray("items");

                            // log tests
//                            Log.d(LOG_TAG, "Array length: "+String.valueOf(jsonData.length()));
//                            Log.d(LOG_TAG, "First of array: "+jsonData.getJSONObject(1));

                            // prepare strings
                            String id;
                            String title;
                            String type;
                            String room;
                            String state;
                            // loop through items
                            for (int i = 0; i < jsonData.length(); i++) {

                                JSONObject item = jsonData.getJSONObject(i);

                                id      = item.getString("_id"); ids.add(id);
                                title   = item.getString("title"); titles.add(title);
                                type    = item.getString("type"); types.add(type);
                                room    = item.getString("room"); rooms.add(room);
                                state   = item.getString("state"); states.add(state);

                                // log the item with all data
//                                Log.d(LOG_TAG, "item: "+String.valueOf(i)+"  id: "+String.valueOf(id)+"  title: "+title+"  type: "+type+"  room: "+room+"  state: "+state);
                            }

                            // Get custom list view
                            ListView listView=(ListView)findViewById(R.id.listView);
                            // Set custom list adapter
                            CustomAdapter customAdapter = new CustomAdapter();
                            listView.setAdapter(customAdapter);
                        }
                        catch (JSONException e) {
                            Log.d(LOG_TAG, "ERROR: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(LOG_TAG, "Error: " + error.getMessage());
                    }
                });

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,"jsonDataTest");
    }
}
