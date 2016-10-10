package tenthousand.hour.law.outliers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import tenthousand.hour.law.outliers.utils.StyledTextView;

/**
 * Created by jeeyu_000 on 2016-10-01.
 */
public class ItemAdapter extends ArrayAdapter<Item>{
    private String TAG = "ItemAdapter";
    ArrayList<Item> users;
    public ItemAdapter(Context context, ArrayList<Item> users) {
        super(context, 0, users);
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_record, parent, false);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        StyledTextView date = (StyledTextView) convertView.findViewById(R.id.date);
        StyledTextView time = (StyledTextView) convertView.findViewById(R.id.time);
        StyledTextView hourNum = (StyledTextView) convertView.findViewById(R.id.hourNum);
        StyledTextView hourText = (StyledTextView) convertView.findViewById(R.id.hourText);
        StyledTextView minNum = (StyledTextView) convertView.findViewById(R.id.minNum);

        Item item = getItem(position);

        date.setText(item.date);
        time.setText(item.time);
        setIcon(icon, item.time);

        setHour(hourNum, hourText, item.duration);
        minNum.setText(item.duration[1]+"");

        return convertView;

    }

    private static Drawable imgSun;
    private static Drawable imgMoon;

    @Override
    public void add(Item object) {
        super.add(object);
    }

    public void refresh(ArrayList<Item> users){
        this.users = users;
        notifyDataSetChanged();
    }

    private void setIcon(ImageView icon, String time){
        int timeInt = Integer.parseInt(time.substring(0, 2));
//        Log.d(TAG, timeInt+"");
        if(imgSun == null){
            imgSun = getContext().getResources().getDrawable(R.drawable.sun);
        }
        if(imgMoon == null){
            imgMoon = getContext().getResources().getDrawable(R.drawable.moon);
        }


        if(timeInt > 6 && timeInt < 18){
            icon.setImageDrawable(imgSun);
        }else{
            icon.setImageDrawable(imgMoon);
        }
    }

    private void setHour(StyledTextView hourNum, StyledTextView hourText, int[] duration){
        String hour = duration[0]+"";

//        Log.d(TAG, "**"+hour);
        switch(hour){
            case "0" : {
                hourNum.setText("");
                hourText.setVisibility(View.INVISIBLE);
                break;
            }
            case "1" : {
                hourNum.setText(hour);
//                hourText.setText("hour");
                hourText.setVisibility(View.VISIBLE);
                break;
            }
            default : {
                Log.d(TAG, "**"+hour);
                hourNum.setText(hour);
//                hourText.setText("hours");
                hourText.setVisibility(View.VISIBLE);
            }
        }
    }


}
