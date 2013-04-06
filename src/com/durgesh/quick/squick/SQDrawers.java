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
        setContentView(R.layout.sqdrawers);
        itemList = new ArrayList<View>();
        selector = getIntent().getIntExtra(Constants.SUPERQUICK, Constants.DO_NOTHING);
        sqTapListener = new SQTapListener(this);
        leftDrawer = layoutDrawer(R.id.left_drawer, SlidingTray.LEFT, R.layout.leftright_drawer_content);
        bottomDrawer = layoutDrawer(R.id.bottom_drawer, SlidingTray.BOTTOM, R.layout.topbottom_drawer_content);
        rightDrawer = layoutDrawer(R.id.right_drawer, SlidingTray.RIGHT, R.layout.leftright_drawer_content);
        topDrawer = layoutDrawer(R.id.top_drawer, SlidingTray.TOP, R.layout.topbottom_drawer_content);

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
        FrameLayout parent = (FrameLayout) findViewById(drawer);
        SlidingTray slidedrawer = new SlidingTray(this, drawerhandle, content, position);
        parent.addView(slidedrawer);
        slidedrawer.setVisibility(View.INVISIBLE);
        return slidedrawer;
        // historico.animateOpen();
    }

    protected void fillDrawerItem(List<View> list) {
        int size = list.size();
        LinearLayout leftDrawerContent = (LinearLayout) leftDrawer.getChildAt(0);
        leftDrawer.setVisibility(View.VISIBLE);
        leftDrawer.animateOpen();
        LinearLayout bottomDrawerContent = (LinearLayout) bottomDrawer.getChildAt(0);
        LinearLayout rightDrawerContent = (LinearLayout) rightDrawer.getChildAt(0);
        LinearLayout topDrawerContent = (LinearLayout) topDrawer.getChildAt(0);
        for (int listItem = 0; listItem < size; listItem++) {
            if (listItem < 6) {
                leftDrawerContent.addView((View) list.get(listItem));
            } else if (listItem >= 6 && listItem <= 11) {
                bottomDrawerContent.addView((View) list.get(listItem));
                bottomDrawer.setVisibility(View.VISIBLE);
                bottomDrawer.animateOpen();
            } else if (listItem > 11 && listItem <= 17) {
                rightDrawerContent.addView((View) list.get(listItem));
                rightDrawer.setVisibility(View.VISIBLE);
                rightDrawer.animateOpen();
            } else if (listItem > 17 && listItem <= 21) {
                topDrawerContent.addView((View) list.get(listItem));
                topDrawer.setVisibility(View.VISIBLE);
                topDrawer.animateOpen();
            }
        }

    }
}
