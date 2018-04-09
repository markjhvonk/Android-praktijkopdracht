package praktijkopdracht.prg027.mark.hr.smarthome;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class getAPIData extends AppCompatActivity {

    static final String API_URL = "http://markvonk.com:8000/api/devices";
    private static final String LOG_TAG = "jsonDataTest";

    // set up ArrayLists
    private static ArrayList<String> ids       = new ArrayList<>();
    private static ArrayList<String> titles    = new ArrayList<>();
    private static ArrayList<String> types     = new ArrayList<>();
    private static ArrayList<String> rooms     = new ArrayList<>();
    private static ArrayList<String> states    = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_apidata);


        // Call Volley function with api url
        volleyJsonObjectRequest(API_URL);
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
                            Log.d(LOG_TAG, "Array length: "+String.valueOf(jsonData.length()));
                            Log.d(LOG_TAG, "First of array: "+jsonData.getJSONObject(1));

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

                            // Go to the main activity
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            intent.putStringArrayListExtra("ids", ids);
                            intent.putStringArrayListExtra("titles", titles);
                            intent.putStringArrayListExtra("types", types);
                            intent.putStringArrayListExtra("rooms", rooms);
                            intent.putStringArrayListExtra("states", states);
                            startActivity(intent);

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
