package com.durgesh.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.durgesh.quick.SQMainVeiw;

/**
 * Represent the TopLeftView
 * 
 * @author durgesht
 */
public class TopLeftView extends SQMainVeiw {

    public TopLeftView(Context context) {
        super(context);
    }

    @Override
    public void onRightToLeft() {
        Toast mToast = Toast.makeText(context.getApplicationContext(), "Long Press TopLeftView", Toast.LENGTH_SHORT);
        mToast.show();

    }

    @Override
    public void onLeftToRight() {
        Toast mToast = Toast.makeText(context.getApplicationContext(), "Long Press TopLeftView", Toast.LENGTH_SHORT);
        mToast.show();

    }

    @Override
    public void onBottomToTop() {
        Toast mToast = Toast.makeText(context.getApplicationContext(), "Long Press TopLeftView", Toast.LENGTH_SHORT);
        mToast.show();

    }

    @Override
    public void onTopToBottom() {
        Toast mToast = Toast.makeText(context.getApplicationContext(), "Long Press TopLeftView", Toast.LENGTH_SHORT);
        mToast.show();

    }

    public void updateViewParameter() {
        updateView(0, SQ_TOP_VIEW_POSITION_RATIO, Gravity.LEFT | Gravity.TOP);
    }

}
