package tenthousand.hour.law.outliers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
        StyledTextView itemDate = (StyledTextView) convertView.findViewById(R.id.itemDate);
        StyledTextView itemTime = (StyledTextView) convertView.findViewById(R.id.itemTime);
        StyledTextView itemPercent = (StyledTextView) convertView.findViewById(R.id.itemPercent);


        Item item = getItem(position);

        itemDate.setText(item.date);
        itemTime.setText(intArrToString(item.duration));
        itemPercent.setText(item.accumulation);

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

//    private void setIcon(ImageView icon, String time){
//        int timeInt = Integer.parseInt(time.substring(0, 2));
////        Log.d(TAG, timeInt+"");
//        if(imgSun == null){
//            imgSun = getContext().getResources().getDrawable(R.drawable.sun);
//        }
//        if(imgMoon == null){
//            imgMoon = getContext().getResources().getDrawable(R.drawable.moon);
//        }
//
//
//        if(timeInt > 6 && timeInt < 18){
//            icon.setImageDrawable(imgSun);
//        }else{
//            icon.setImageDrawable(imgMoon);
//        }
//    }


    public static String intArrToString(int[] intArr){
        String[] StringArr = new String[3];
        String res = new String();

        for(int i = 0; i < 3; i++){
            if(intArr[i] < 10){
                StringArr[i] = "0" + intArr[i];
            }else{
                StringArr[i] = intArr[i]+"";
            }
        }

        res = StringArr[0] + ":" + StringArr[1] + ":" + StringArr[2];
        return res;
    }


}
