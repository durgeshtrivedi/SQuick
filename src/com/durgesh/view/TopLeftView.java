package com.durgesh.view;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;

import com.durgesh.quick.squick.SQDirectDialActivity;

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
       // Toast mToast = Toast.makeText(context.getApplicationContext(), "Long Press TopLeftView", Toast.LENGTH_SHORT);
       // mToast.show();
        Intent dialerActivity = new Intent(context, SQDirectDialActivity.class);
        dialerActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dialerActivity);
    }

    @Override
    public void onLeftToRight() {
     //   Toast mToast = Toast.makeText(context.getApplicationContext(), "Long Press TopLeftView", Toast.LENGTH_SHORT);
      //  mToast.show();

        Intent dialerActivity = new Intent(context, SQDirectDialActivity.class);
        dialerActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dialerActivity);
    }

    @Override
    public void onBottomToTop() {
      //  Toast mToast = Toast.makeText(context.getApplicationContext(), "Long Press TopLeftView", Toast.LENGTH_SHORT);
       // mToast.show();
        Intent dialerActivity = new Intent(context, SQDirectDialActivity.class);
        dialerActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dialerActivity);
    }

    @Override
    public void onTopToBottom() {
       // Toast mToast = Toast.makeText(context.getApplicationContext(), "Long Press TopLeftView", Toast.LENGTH_SHORT);
       // mToast.show();
        Intent dialerActivity = new Intent(context, SQDirectDialActivity.class);
        dialerActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dialerActivity);
    }
   
    
    public void updateViewParameter() {
        updateView(0, SQ_TOP_VIEW_POSITION_RATIO, Gravity.LEFT | Gravity.TOP);
    }

    

}
