package com.interview.egpaf;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    private Context context;

    public Helper(Context c) {
        this.context = c;
    }

    public void MakeToast(String info) {

        Toast.makeText(context,info,Toast.LENGTH_SHORT).show();
    }

    public String capitalizeFirstLetter(String s){
        String str = s;
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);

        return cap;
    }

    // Date Validation
    public boolean isValidDate(String date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date testDate = null;
        try
        {
            testDate = simpleDateFormat.parse(date);
        }
        catch (ParseException e)
        {
            // invalid date

            return false;
        }
        if (!simpleDateFormat.format(testDate).equals(date))
        {
            // invalid date
            return false;
        }

        return true;
    }

    public boolean isDateAhead(String date) throws ParseException {

        Date d = new SimpleDateFormat("dd/MM/yyyy").parse(date);

        if(System.currentTimeMillis() < d.getTime()){

            return false;
        }
        return true;
    }



}
