package lk.oxo.eshop.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import lk.oxo.eshop.MainActivity;
import lk.oxo.eshop.R;

public class ButtonColor {
    private int f_start_index;
    private int f_end_index;
    private int l_end_index;

    public ButtonColor(int f_start_index, int f_end_index, int l_end_index) {
        this.f_start_index = f_start_index;
        this.f_end_index = f_end_index;
        this.l_end_index = l_end_index;
    }

    public int getF_start_index() {
        return f_start_index;
    }

    public void setF_start_index(int f_start_index) {
        this.f_start_index = f_start_index;
    }

    public int getF_end_index() {
        return f_end_index;
    }

    public void setF_end_index(int f_end_index) {
        this.f_end_index = f_end_index;
    }

    public int getL_end_index() {
        return l_end_index;
    }

    public void setL_end_index(int l_end_index) {
        this.l_end_index = l_end_index;
    }

    public SpannableString changeButtonText(String text, Context context,String mode){
        SpannableString spannableString = new SpannableString(text);

        ForegroundColorSpan blackColorSpan;
        if(mode.equals("day"))
            blackColorSpan = new ForegroundColorSpan(Color.BLACK);
        else
            blackColorSpan = new ForegroundColorSpan(Color.WHITE);

        ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(context.getColor(R.color.button_blue));

        spannableString.setSpan(blackColorSpan, this.f_start_index,
                this.f_end_index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(blueColorSpan, this.f_end_index,
                this.l_end_index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
}
