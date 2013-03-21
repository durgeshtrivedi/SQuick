package com.durgesh.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.durgesh.R;
import com.durgesh.quick.centermenu.SQCenterMenu;
import com.durgesh.quick.centermenu.SQCenterMenuItem;

public class SQCenterView extends SQMainVeiw {
    SQCenterMenu ownview;
    RelativeLayout customLinearLayout = null;

    public SQCenterView(Context context) {
        super(context);
        // SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        // String selector = settings.getString("left_bar", context.getResources().getString(R.string.pref_lefbar_title));
        viewSelector("center");
    }

    @Override
    public void onRightToLeft() {
        launchShorcut();
    }

    @Override
    public void onLeftToRight() {
        launchShorcut();
    }

    @Override
    public void onBottomToTop() {
        launchShorcut();
    }

    @Override
    public void onTopToBottom() {
        launchShorcut();
    }

    public void updateViewParameter() {
        updateView(0, 0, Gravity.CENTER);
    }

    protected void inflateView() {
        inflateMenu();
    }

    protected void updateView(int xAxis, int ration, int gravity) {

        WindowManager.LayoutParams paramsCenter = (WindowManager.LayoutParams) ownview.getLayoutParams();
        paramsCenter.height = R.dimen.center_menu_size;
        paramsCenter.width = R.dimen.center_menu_size;

        paramsCenter.gravity = gravity;

        // applyScaling(paramsCenter);
        // applyTransparency(paramsCenter);
        windowsmanger.updateViewLayout(ownview, paramsCenter);
    }

    public void inflateMenu() {
        ImageView centerButton = new ImageView(context);
        windowsmanger = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        ArrayList<SQCenterMenuItem> _model = new ArrayList<SQCenterMenuItem>();
        for (int i = 0; i < 5; i++) {
            SQCenterMenuItem m = new SQCenterMenuItem();
            m.name = "" + i;
            m.Rid = R.drawable.bgbutton;
            _model.add(m);
        }
        ownview = new SQCenterMenu(context, 300, 0, _model);
        ownview.onBuildComponent(centerButton);
        windowsmanger.addView(ownview, makeOverlayParams());
    }

}
