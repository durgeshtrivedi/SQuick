package com.durgesh.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.durgesh.R;
import com.sileria.android.view.SlidingTray;
import com.sileria.util.Side;

public class SlidingDrawerActivity extends Activity {
    private LinearLayout leftDrawer;
    private LinearLayout rightDrawer;
    private LinearLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sqleftdrawer);
        RelativeLayout contentparent = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.sqrightdrawer, null);
        RelativeLayout contentparent1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.sqrightdrawer, null);
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.left_drawer);
        RelativeLayout parent2 = (RelativeLayout) findViewById(R.id.bottom_drawer);
        Button handle = new Button(this);
        handle.setText("Push Me");
        Button handle1 = new Button(this);
        handle.setText(" HEllo Push Me");

        Button handle2 = new Button(this);
        handle2.setText("Push Me");
        Button handle3 = new Button(this);
        handle3.setText("Push Me");

        final SlidingTray historico1 = new SlidingTray(this, handle, contentparent, SlidingTray.TOP);

        historico1.setHandlePosition(Side.TOP);

        parent.addView(historico1, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        // historico1.animateOpen();

        Button handle11 = new Button(this);
        handle11.setText("Push Me");
        Button handle22 = new Button(this);
        handle22.setText(" HEllo Push Me");

        SlidingTray historico2 = new SlidingTray(this, handle11, contentparent1, SlidingTray.BOTTOM);

        historico2.setHandlePosition(Side.BOTTOM);

        parent2.addView(historico2, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        historico2.animateOpen();
        historico1.animateOpen();

    }

    @Override
    protected void onPause() {
        // Whenever this activity is paused (i.e. looses focus because another activity is started etc)
        // Override how this activity is animated out of view
        // The new activity is kept still and this activity is pushed out to the left
        // overridePendingTransition(R.anim.hold, R.anim.push_out_to_left);
        super.onPause();
    }
}