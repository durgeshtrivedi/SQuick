package com.durgesh.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.durgesh.R;
import com.sileria.android.view.SlidingTray;
import com.sileria.util.Side;

public class SlidingDrawerActivity extends Activity {
    SlidingTray historico;
    SlidingTray historico1;
    SlidingTray historico2;
    SlidingTray historico3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sqdrawers);
        LinearLayout content = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawercontent, null);
        LinearLayout drawerhandle = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawerhandle, null);
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.top_drawer);
        
        LinearLayout content1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawercontent, null);
        LinearLayout drawerhandle1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawerhandle, null);
        RelativeLayout parent1 = (RelativeLayout) findViewById(R.id.bottom_drawer);
        
        LinearLayout content2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawercontent, null);
        LinearLayout drawerhandle2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawerhandle, null);
        RelativeLayout parent2 = (RelativeLayout) findViewById(R.id.left_drawer);
        
        LinearLayout content3 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawercontent, null);
        LinearLayout drawerhandle3 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawerhandle, null);
        RelativeLayout parent3 = (RelativeLayout) findViewById(R.id.right_drawer);
        

          historico = new SlidingTray(this, drawerhandle, content, SlidingTray.TOP);
        historico.setHandlePosition(Side.TOP);
        parent.addView(historico, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        // historico1.animateOpen();
       
         historico1 = new SlidingTray(this, drawerhandle1, content1, SlidingTray.BOTTOM);
        historico1.setHandlePosition(Side.BOTTOM);
        parent1.addView(historico1, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
         historico2 = new SlidingTray(this, drawerhandle2, content2, SlidingTray.LEFT);
        historico2.setHandlePosition(Side.LEFT);
        parent2.addView(historico2, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        
         historico3 = new SlidingTray(this, drawerhandle3, content3, SlidingTray.RIGHT);
        historico3.setHandlePosition(Side.RIGHT);
        parent3.addView(historico3, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
       
       
        historico1.animateOpen();
        historico2.animateOpen();
        historico3.animateOpen();
        historico.animateOpen();

    }

    @Override
    protected void onPause() {
        // Whenever this activity is paused (i.e. looses focus because another activity is started etc)
        // Override how this activity is animated out of view
        // The new activity is kept still and this activity is pushed out to the left
        // overridePendingTransition(R.anim.hold, R.anim.push_out_to_left);
        historico1.animateClose();
        historico2.animateClose();
        historico3.animateClose();
        historico.animateClose();
        super.onPause();
       
    }
}