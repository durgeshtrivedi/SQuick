package com.durgesh.quick.squick;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.durgesh.R;
import com.durgesh.util.Constants;
import com.sileria.android.view.SlidingTray;

/**
 * Create the LEFT ,BOTTOM, RIGHT and TOP drawer
 * 
 * @author durgesht
 */
public class SQDrawers extends Activity {
    protected SQTapListener sqTapListener;
    final Context context = this;
    // Represent on which sqbar is swap and what shortcut it has directdial,directmessage,app or contact
    public int selector;
    SlidingTray leftDrawer, bottomDrawer, rightDrawer, topDrawer;
    FrameLayout maindrawerContainer, leftDrawerContainer, rightDrawerContainer, topDrawerContainer, bottomDrawerContainer;
    protected List<View> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerscontainer);
        init();
    }

    /**
     * @param drawer
     *            drawer id to be layout
     * @param position
     *            position of the drawer on screen
     * @param conten
     *            content layout for the drawer items
     */
    private SlidingTray layoutDrawer(int drawer, int position, int conten) {
        LinearLayout content = (LinearLayout) LayoutInflater.from(this).inflate(conten, null);
        LinearLayout drawerhandle = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawerhandle, null);
        SlidingTray slidedrawer = new SlidingTray(this, drawerhandle, content, position);
        slidedrawer.setVisibility(View.GONE);
        slidedrawer.animateOpen();
        return slidedrawer;
        // historico.animateOpen();
    }

    protected void fillDrawerItem(BaseAdapter list) {
        int size = list.getCount();
        LinearLayout leftDrawerContent = (LinearLayout) leftDrawer.getChildAt(1);
        leftDrawer.setVisibility(View.VISIBLE);
        leftDrawer.animateOpen();
        LinearLayout bottomDrawerContent = (LinearLayout) bottomDrawer.getChildAt(1);
        LinearLayout rightDrawerContent = (LinearLayout) rightDrawer.getChildAt(1);
        LinearLayout topDrawerContent = (LinearLayout) topDrawer.getChildAt(1);
        LayoutInflater li = LayoutInflater.from(this);

        for (int listItem = 0; listItem < size; listItem++) {
            if (listItem < 6) {
                View itemLeftRightDrawer = li.inflate(R.layout.leftright_drawer_item, null);
                leftDrawerContent.addView((View) list.getView(listItem, itemLeftRightDrawer, null));
            } else if (listItem >= 6 && listItem <= 11) {
                View itemTopBottomDrawer = li.inflate(R.layout.topbottom_drawer_item, null);
                bottomDrawerContent.addView((View) list.getView(listItem, itemTopBottomDrawer, null));
                if (bottomDrawer.getVisibility() == View.GONE) {
                    bottomDrawer.setVisibility(View.VISIBLE);
                    bottomDrawer.animateOpen();
                }
            } else if (listItem > 11 && listItem <= 17) {
                View itemLeftRightDrawer = li.inflate(R.layout.leftright_drawer_item, null);
                rightDrawerContent.addView((View) list.getView(listItem, itemLeftRightDrawer, null));
                if (rightDrawer.getVisibility() == View.GONE) {
                    rightDrawer.setVisibility(View.VISIBLE);
                    rightDrawer.animateOpen();
                }
            } else if (listItem > 17 && listItem <= 21) {
                View itemTopBottomDrawer = li.inflate(R.layout.topbottom_drawer_item, null);
                topDrawerContent.addView((View) list.getView(listItem, itemTopBottomDrawer, null));
                if (topDrawer.getVisibility() == View.GONE) {
                    topDrawer.setVisibility(View.VISIBLE);
                    topDrawer.animateOpen();
                }
            }
        }
    }

    protected void layoutDrawers() {

        // The order in which the drawers container are added to main container
        // should be maintained else the drawers will not behave properly

        // leftDrawerContainer.addView(leftDrawer);
        // maindrawerContainer.addView(leftDrawerContainer);
        // rightDrawerContainer.addView(rightDrawer);
        // maindrawerContainer.addView(rightDrawerContainer);
        // topDrawerContainer.addView(topDrawer);
        // maindrawerContainer.addView(topDrawerContainer);
        // bottomDrawerContainer.addView(bottomDrawer);
        // maindrawerContainer.addView(bottomDrawerContainer);
        
        if (leftDrawer.getVisibility() == View.VISIBLE) 
            leftDrawer.animateOpen();
        if (rightDrawer.getVisibility() == View.VISIBLE) 
            rightDrawer.animateOpen();
        if (topDrawer.getVisibility() == View.VISIBLE) 
            topDrawer.animateOpen();
        if (bottomDrawer.getVisibility() == View.VISIBLE) 
            bottomDrawer.animateOpen();
        
        leftDrawerContainer.addView(leftDrawer);
        maindrawerContainer.addView(leftDrawerContainer, new FrameLayout.LayoutParams(60, 400, Gravity.LEFT | Gravity.CENTER_VERTICAL));
        rightDrawerContainer.addView(rightDrawer);
        maindrawerContainer.addView(rightDrawerContainer, new FrameLayout.LayoutParams(60, 400, Gravity.CENTER_VERTICAL | Gravity.RIGHT));
        topDrawerContainer.addView(topDrawer);
        maindrawerContainer.addView(topDrawerContainer, new FrameLayout.LayoutParams(400, 60, Gravity.TOP | Gravity.CENTER_HORIZONTAL));
        bottomDrawerContainer.addView(bottomDrawer);
        maindrawerContainer.addView(bottomDrawerContainer, new FrameLayout.LayoutParams(400, 60, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM));
        
    }

    private void init() {
        itemList = new ArrayList<View>();
        selector = getIntent().getIntExtra(Constants.SUPERQUICK, Constants.DO_NOTHING);
        sqTapListener = new SQTapListener(this);
        maindrawerContainer = (FrameLayout) findViewById(R.id.drawer_container);
        leftDrawer = layoutDrawer(R.id.left_drawer_container, SlidingTray.LEFT, R.layout.leftright_drawer_content);
        rightDrawer = layoutDrawer(R.id.right_drawer_container, SlidingTray.RIGHT, R.layout.leftright_drawer_content);
        topDrawer = layoutDrawer(R.id.top_drawer_container, SlidingTray.TOP, R.layout.topbottom_drawer_content);
        bottomDrawer = layoutDrawer(R.id.bottom_drawer_container, SlidingTray.BOTTOM, R.layout.topbottom_drawer_content);
        leftDrawerContainer = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.leftdrawercontainer, null);
        rightDrawerContainer = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.rightdrawercontainer, null);
        topDrawerContainer = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.topdrawercontainer, null);
        bottomDrawerContainer = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.bottomdrawercontainer, null);
    }
}
