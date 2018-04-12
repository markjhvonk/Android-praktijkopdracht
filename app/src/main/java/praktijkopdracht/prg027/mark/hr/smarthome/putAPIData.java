package praktijkopdracht.prg027.mark.hr.smarthome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class putAPIData extends AppCompatActivity {

    // Constants
    private static final String API_URL = "http://markvonk.com:8000/api/devices";
    private static final String LOG_TAG = "jsonDataTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_apidata);

        // get all intent data
        Intent putI = getIntent();
        String putId = putI.getStringExtra("putId");
        String putTitle = putI.getStringExtra("putTitle");
        String putType = putI.getStringExtra("putType");
        String putRoom = putI.getStringExtra("putRoom");
        String putCurrentState = putI.getStringExtra("putCurrentState");

        // define new state based on old state
        String putNewState;
        if(putCurrentState.equals("false"))
            putNewState = "true";
        else
            putNewState = "false";

        // call Volley function
        volleyPut(API_URL, putId, putTitle, putType, putRoom, putNewState);

    }

    public void volleyPut(String url, String id, String title, String type, String room, String state) {

        // prepare string values
        final String putId = id;
        final String putTitle = title;
        final String putType = type;
        final String putRoom = room;
        final String putNewState = state;

        // Volley put request
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url + "/" + putId,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d(LOG_TAG, "Volley Put Success: "+response);

                        Intent intent = new Intent(getBaseContext(), getAPIData.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(LOG_TAG, "Volley Put Error: "+String.valueOf(error));
                    }
                }
        ) {

            // set http headers
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }

            // set put data
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();

                Log.d(LOG_TAG, "Title: "+putTitle+"  Type: "+putType+"  Room: "+putRoom+"  State: "+putNewState);
                params.put("title", putTitle);
                params.put("type", putType);
                params.put("room", putRoom);
                params.put("state", putNewState);

                return params;
            }

        };

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(putRequest,"jsonDataTest");
    }
}
