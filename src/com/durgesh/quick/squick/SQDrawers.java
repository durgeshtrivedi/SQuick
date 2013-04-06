package com.durgesh.quick.squick;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
    SlidingTray leftDrawer;
    SlidingTray bottomDrawer;
    SlidingTray rightDrawer;
    SlidingTray topDrawer;
    protected List<View> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerscontainer);
        itemList = new ArrayList<View>();
        selector = getIntent().getIntExtra(Constants.SUPERQUICK, Constants.DO_NOTHING);
        sqTapListener = new SQTapListener(this);
        leftDrawer = layoutDrawer(R.id.left_drawer_container, SlidingTray.LEFT, R.layout.leftright_drawer_content);
        bottomDrawer = layoutDrawer(R.id.bottom_drawer_container, SlidingTray.BOTTOM, R.layout.topbottom_drawer_content);
        rightDrawer = layoutDrawer(R.id.right_drawer_container, SlidingTray.RIGHT, R.layout.leftright_drawer_content);
        topDrawer = layoutDrawer(R.id.top_drawer_container, SlidingTray.TOP, R.layout.topbottom_drawer_content);

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
        LinearLayout drawerContent = (LinearLayout) LayoutInflater.from(this).inflate(conten, null);
        LinearLayout drawerhandle = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawerhandle, null);
        FrameLayout drawerContainer = (FrameLayout) findViewById(drawer);
        SlidingTray slidedrawer = new SlidingTray(this, drawerhandle, drawerContent, position);
        drawerContainer.addView(slidedrawer);
        slidedrawer.setVisibility(View.GONE);
        return slidedrawer;
    }

    protected void fillDrawerItem(BaseAdapter drawerItemList) {
        int size = drawerItemList.getCount();
        LayoutInflater li = LayoutInflater.from(context);

        LinearLayout leftDrawerContent = (LinearLayout) leftDrawer.getChildAt(1);
        leftDrawer.setVisibility(View.VISIBLE);
        LinearLayout bottomDrawerContent = (LinearLayout) bottomDrawer.getChildAt(1);
        LinearLayout rightDrawerContent = (LinearLayout) rightDrawer.getChildAt(1);
        LinearLayout topDrawerContent = (LinearLayout) topDrawer.getChildAt(1);
        for (int listItem = 0; listItem < size; listItem++) {
            if (listItem < 6) {
                leftDrawerContent.addView((View) drawerItemList.getView(listItem,li.inflate(R.layout.leftright_drawer_item, null), null));
            } else if (listItem >= 6 && listItem <= 11) {
                bottomDrawerContent.addView((View) drawerItemList.getView(listItem,li.inflate(R.layout.topbottom_drawer_item, null), null));
                if (bottomDrawer.getVisibility() == View.GONE) bottomDrawer.setVisibility(View.VISIBLE);
            } else if (listItem > 11 && listItem <= 17) {
                rightDrawerContent.addView((View) drawerItemList.getView(listItem,li.inflate(R.layout.leftright_drawer_item, null), null));
                if (rightDrawer.getVisibility() == View.GONE) rightDrawer.setVisibility(View.VISIBLE);
            } else if (listItem > 17 && listItem <= 21) {
                topDrawerContent.addView((View) drawerItemList.getView(listItem, li.inflate(R.layout.topbottom_drawer_item, null), null));
                if (topDrawer.getVisibility() == View.GONE) topDrawer.setVisibility(View.VISIBLE);
            }
        }

    }

    protected void openDrawer() {
        leftDrawer.animateOpen();
        rightDrawer.animateOpen();
        topDrawer.animateOpen();
        bottomDrawer.animateOpen();
    }
}
