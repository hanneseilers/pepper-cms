package de.fhkiel.pepper.cms.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import de.fhkiel.pepper.cms.R;

public class BigImagePaneButton extends LinearLayout {

    public BigImagePaneButton(Context context){
        super(context);
        initalizeViews(context);
    }

    public BigImagePaneButton(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initalizeViews(context);
    }

    private void initalizeViews(Context context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.big_image_button_layout, this);
    }
}
