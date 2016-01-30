package id.dapur.d4ti3a.example;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

    // Deklarasi Variabel
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView name;
        TextView locationname;
        TextView longitude;
        TextView latitude;
        TextView date;
        ImageView flag;
        ImageView arrow;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        name = (TextView) itemView.findViewById(R.id.name);
        locationname = (TextView) itemView.findViewById(R.id.locationname);
        latitude = (TextView) itemView.findViewById(R.id.latitude);
        longitude = (TextView) itemView.findViewById(R.id.longitude);
        date = (TextView) itemView.findViewById(R.id.date);


        // Locate the ImageView in listview_item.xml
        flag = (ImageView) itemView.findViewById(R.id.flag);
        arrow = (ImageView) itemView.findViewById((R.id.arrow));

        // Capture position and set results to the TextViews
        name.setText(resultp.get(MainActivity.NAME));
        locationname.setText(resultp.get(MainActivity.LOCATIONNAME));
        latitude.setText(resultp.get(MainActivity.LATITUDE));
        longitude.setText(resultp.get(MainActivity.LONGITUDE));
        date.setText(resultp.get(MainActivity.DATE));
        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        imageLoader.DisplayImage(resultp.get(MainActivity.FLAG), flag);
        // Capture ListView item click
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
                Intent intent = new Intent(context, SingleItemView.class);
                // Pass all data name
                intent.putExtra("name", resultp.get(MainActivity.NAME));
                // Pass all data locationname
                intent.putExtra("locationname", resultp.get(MainActivity.LOCATIONNAME));
                // Pass all data long lat date
                intent.putExtra("latitude",resultp.get(MainActivity.LATITUDE));
                intent.putExtra("longitude",resultp.get(MainActivity.LONGITUDE));
                intent.putExtra("date",resultp.get(MainActivity.DATE));
                // Pass all data flag
                intent.putExtra("flag", resultp.get(MainActivity.FLAG));
                // Start SingleItemView Class
                context.startActivity(intent);

            }
        });
        return itemView;
    }
}