package com.eg.moviehub;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eg.moviehub.DAL.UserDAL;
import com.eg.moviehub.DTO.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class GenreActivity extends AppCompatActivity {



    UserDAL userDAL;
    public class Item {
        boolean checked;

        String ItemString;
        Item( String t, boolean b){

            ItemString = t;
            checked = b;
        }

        public boolean isChecked(){
            return checked;
        }
    }

    static class ViewHolder {
        CheckBox checkBox;
        ImageView icon;
        TextView text;
    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            ViewHolder viewHolder = new ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.genre_cell, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
            //    viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

         //   viewHolder.icon.setImageDrawable(list.get(position).ItemDrawable);
            viewHolder.checkBox.setChecked(list.get(position).checked);

            final String itemStr = list.get(position).ItemString;
            viewHolder.text.setText(itemStr);

            viewHolder.checkBox.setTag(position);


            viewHolder.checkBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.get(position).checked = b;

//                    Toast.makeText(getApplicationContext(),
//                            itemStr + "onCheckedChanged\nchecked: " + b,
//                            Toast.LENGTH_LONG).show();
                }
            });


//            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    boolean newState = !list.get(position).isChecked();
//                    list.get(position).checked = newState;
//                    Toast.makeText(getApplicationContext(),
//                            itemStr + "setOnClickListener\nchecked: " + newState,
//                            Toast.LENGTH_LONG).show();
//                }
//            });

            viewHolder.checkBox.setChecked(isChecked(position));

            return rowView;
        }
    }

    Button btnLookup;
    List<Item> items;
    ListView listView;
    ItemsListAdapter myItemsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        listView = (ListView)findViewById(R.id.listview);
        btnLookup = (Button)findViewById(R.id.lookup);

        initItems();
        myItemsListAdapter = new ItemsListAdapter(this, items);
        listView.setAdapter(myItemsListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(GenreActivity.this,
                        ((Item)(parent.getItemAtPosition(position))).ItemString,
                        Toast.LENGTH_LONG).show();
            }});

        btnLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "Check items:\n";
                UserDTO userDTO= new UserDTO();
                userDAL = new UserDAL();



                for (int i=0; i<items.size(); i++){
                    if (items.get(i).isChecked()){
                        str += i + "\n";

                        userDTO.genreList.add(items.get(i).ItemString);
                      //  userDAL.pushNewGenre();
                    }
                }
                userDAL.updateGenreList(userDTO.genreList);
                userDAL.setUserDALListner(new UserDAL.UserDALListener() {
                    @Override
                    public void onUserFetch(UserDTO userDTO) {

                    }

                    @Override
                    public void onUserPushed(String s) {

                    }

                    @Override
                    public void onUserGenreListUpdated(boolean b) {
                        Toast.makeText(GenreActivity.this,"Updated Genere List",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUserVideoListUpdated(boolean b) {

                    }

                    @Override
                    public void onUserStatusUpdated(boolean b) {

                    }

                    @Override
                    public void onVidAdd(String b) {

                    }

                    @Override
                    public void onUpdateGenreList(ArrayList<String> list) {

                    }

                    @Override
                    public void onAllGenreFetch(ArrayList<String> list) {

                    }
                });
                /*
                int cnt = myItemsListAdapter.getCount();
                for (int i=0; i<cnt; i++){
                    if(myItemsListAdapter.isChecked(i)){
                        str += i + "\n";
                    }
                }
                */

                Toast.makeText(GenreActivity.this,
                        str,
                        Toast.LENGTH_LONG).show();

            }
        });
    }

    private void initItems(){
        items = new ArrayList<Item>();

      //  TypedArray arrayDrawable = getResources().obtainTypedArray(R.array.resicon);
        TypedArray arrayText = getResources().obtainTypedArray(R.array.restext);

        for(int i=0; i<arrayText.length(); i++){
        //    Drawable d = arrayDrawable.getDrawable(i);
            String s = arrayText.getString(i);
            boolean b = false;
            Item item = new Item( s, b);
            items.add(item);
        }

       // arrayDrawable.recycle();
        arrayText.recycle();
    }

}
