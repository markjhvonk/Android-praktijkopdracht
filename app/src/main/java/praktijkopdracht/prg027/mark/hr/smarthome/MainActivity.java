package praktijkopdracht.prg027.mark.hr.smarthome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    // constants
    private static final String LOG_TAG = "jsonDataTest";

    // set up arrayLists for later
    ArrayList<String> ids;
    ArrayList<String> titles;
    ArrayList<String> types;
    ArrayList<String> rooms;
    ArrayList<String> states;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up custom toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //set color toolbar
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String color = sharedPref.getString("ui_color", "");
        myToolbar.setBackgroundColor(Color.parseColor(color));

        // get the data from the getAPIData activity
        Intent i = getIntent();
        ids = i.getStringArrayListExtra("ids");
        titles = i.getStringArrayListExtra("titles");
        types = i.getStringArrayListExtra("types");
        rooms = i.getStringArrayListExtra("rooms");
        states = i.getStringArrayListExtra("states");

        // Check to see if content came through
        if(ids != null){
            // Get custom list view
            ListView listView = (ListView)findViewById(R.id.listView);
            // Set custom list adapter
            MainActivity.CustomAdapter customAdapter = new MainActivity.CustomAdapter();
            listView.setAdapter(customAdapter);

        } else {
            // if content didn't come through, throw some error's
            Context context = getApplicationContext();
            CharSequence text = "Something went wrong with the API data!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    // add custom icons to menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return true;
    }
    // handle clicks on custom menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Intent refreshIntent = new Intent(getBaseContext(), getAPIData.class);
                startActivity(refreshIntent);
                return true;

            case R.id.action_settings:
                Intent settingsIntent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    // Initialize custom adapter
    class CustomAdapter extends BaseAdapter {
        // set up String arrays with api data
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
            return IDS.length;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            // get the custom list item layout
            view = getLayoutInflater().inflate(R.layout.customlayout,null);

            // setup the editable view items
            ImageView imageView             = (ImageView)view.findViewById(R.id.imageView);
            TextView textView_name          = (TextView)view.findViewById(R.id.textView_name);
            TextView textView_description   = (TextView)view.findViewById(R.id.textView_description);

            // check which icon to use for device
            switch(TYPES[i]) {
                case "light":
                    imageView.setImageResource(R.drawable.ic_lightbulb_outline_black_24dp);
                    break;
                case "tv":
                    imageView.setImageResource(R.drawable.ic_tv_black_24dp);
                    break;
                case "speakers":
                case "radio":
                    imageView.setImageResource(R.drawable.ic_speaker_black_24dp);
                    break;
                case "phone":
                    imageView.setImageResource(R.drawable.ic_phone_android_black_24dp);
                    break;
                case "computer":
                    imageView.setImageResource(R.drawable.ic_desktop_windows_black_24dp);
                    break;
                default:
                    imageView.setImageResource(R.drawable.ic_devices_other_black_24dp);
            }

            // set background color according to state
            switch(STATES[i]) {
                case "true":
                    view.setBackgroundColor(Color.parseColor("#C8E6C9"));
                    break;
                case "false":
                    view.setBackgroundColor(Color.parseColor("#FFCDD2"));
                    break;
                default:
            }

            // Set onClick listener on the listView items for the put request
            final int currentI = i;
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // send data to the putAPIData class
                    Intent intent = new Intent(getBaseContext(), putAPIData.class);
                    intent.putExtra("putId", IDS[currentI]);
                    intent.putExtra("putTitle", TITLES[currentI]);
                    intent.putExtra("putType", TYPES[currentI]);
                    intent.putExtra("putRoom", ROOMS[currentI]);
                    intent.putExtra("putCurrentState", STATES[currentI]);
                    Log.d("Debuggg", "putState test putExtra: false"+STATES[currentI]);
                    startActivity(intent);
                }

            });

            // Set the fields with gathered data
            textView_name.setText(TITLES[i]);
            textView_description.setText(IDS[i]);

            return view;
        }
    }
}
