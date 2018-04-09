package praktijkopdracht.prg027.mark.hr.smarthome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    ArrayList<String> ids;
    ArrayList<String> titles;
    ArrayList<String> types;
    ArrayList<String> rooms;
    ArrayList<String> states;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        ids = i.getStringArrayListExtra("ids");
        titles = i.getStringArrayListExtra("titles");
        types = i.getStringArrayListExtra("types");
        rooms = i.getStringArrayListExtra("rooms");
        states = i.getStringArrayListExtra("states");

        // Check to see if
        if(ids != null){
            // Get custom list view
            ListView listView = (ListView)findViewById(R.id.listView);
            // Set custom list adapter
            MainActivity.CustomAdapter customAdapter = new MainActivity.CustomAdapter();
            listView.setAdapter(customAdapter);
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Something went wrong with the API data!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    // Initialize custom adapter
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

            ImageView imageView             = (ImageView)view.findViewById(R.id.imageView);
            TextView textView_name          = (TextView)view.findViewById(R.id.textView_name);
            TextView textView_description   = (TextView)view.findViewById(R.id.textView_description);

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

            textView_name.setText(TITLES[i]);
            textView_description.setText(IDS[i]);

            return view;
        }
    }
}
