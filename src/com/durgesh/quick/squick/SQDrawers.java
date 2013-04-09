package com.durgesh.quick.squick;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.durgesh.R;
import com.durgesh.quick.squick.ShortcutIntentBuilder.OnShortcutIntentCreatedListener;
import com.durgesh.util.Constants;
import com.sileria.android.view.HorzListView;
import com.sileria.android.view.SlidingTray;

/**
 * Create the LEFT ,BOTTOM, RIGHT and TOP drawer
 * 
 * @author durgesht
 */
public class SQDrawers extends Activity {
    // Represent on which sqbar is swap and what shortcut it has directdial,directmessage,app or contact
    public int selector;
    protected SQTapListener sqTapListener;
    final Context context = this;
    private LinearLayout leftDrawerContent, rightDrawerContent, topDrawerContent, bottomDrawerContent;
    private CustomAdapter leftDrawerAdapter, rightDrawerAdapter, topDrawerAdapter, bottomDrawerAdapter;
    private List<View> leftAdapterList, rightAdapterList, topAdapterList, bottomAdapterList;
    private FrameLayout leftDrawerContainer, rightDrawerContainer, topDrawerContainer, bottomDrawerContainer;
    private SlidingTray leftDrawer, bottomDrawer, rightDrawer, topDrawer;
    protected ListView leftDraweritemList, rightDraweritemList;
    protected HorzListView topDraweritemList, bottomDraweritemList;

    protected List<View> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerscontainer);
        itemList = new ArrayList<View>();
        selector = getIntent().getIntExtra(Constants.SUPERQUICK, Constants.DO_NOTHING);
        sqTapListener = new SQTapListener(this);
        init();
    }

    protected void fillAllDrawerItem(BaseAdapter drawerItemList) {
        int size = drawerItemList.getCount();
        LayoutInflater li = LayoutInflater.from(this);
        for (int listItem = 0; listItem < size; listItem++) {
            if (listItem < 6) {
                if (leftDrawerContent == null) {
                    initLeftDrawerContent();
                }
                leftAdapterList.add((View) drawerItemList.getView(listItem, null, null));
                leftDrawerAdapter.notifyDataSetChanged();
            } else if (listItem >= 6 && listItem <= 11) {
                if (bottomDrawerContent == null) {
                    initBottomDrawerContent();
                }
                bottomAdapterList.add((View) drawerItemList.getView(listItem, null, null));
                bottomDrawerAdapter.notifyDataSetChanged();
            } else if (listItem > 11 && listItem <= 17) {
                if (rightDrawerContent == null) {
                    initRightDrawerContent();
                }
                rightAdapterList.add((View) drawerItemList.getView(listItem, null, null));
                rightDrawerAdapter.notifyDataSetChanged();
            } else if (listItem > 17 && listItem <= 21) {
                if (topDrawerContent == null) {
                    initTopDrawerContent();
                }
                topAdapterList.add((View) drawerItemList.getView(listItem, null, null));
                topDrawerAdapter.notifyDataSetChanged();
            }
        }

    }

    private void init() {
        initDrawerContainer();
    }

    private void initDrawerContainer() {
        leftDrawerContainer = (FrameLayout) findViewById(R.id.left_drawer_container);
        rightDrawerContainer = (FrameLayout) findViewById(R.id.right_drawer_container);
        topDrawerContainer = (FrameLayout) findViewById(R.id.top_drawer_container);
        bottomDrawerContainer = (FrameLayout) findViewById(R.id.bottom_drawer_container);
    }

    private void initLeftDrawerContent() {
        leftDrawerContent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.leftright_drawer_content, null);
        leftAdapterList = new ArrayList<View>();
        leftDrawerAdapter = new CustomAdapter(this, R.layout.leftright_drawer_item, leftAdapterList);
        leftDraweritemList = (ListView) leftDrawerContent.findViewById(R.id.leftright_drawer_list);
        leftDraweritemList.setAdapter(leftDrawerAdapter);

    }

    private void initRightDrawerContent() {
        rightDrawerContent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.leftright_drawer_content, null);
        rightAdapterList = new ArrayList<View>();
        rightDrawerAdapter = new CustomAdapter(this, R.layout.leftright_drawer_item, rightAdapterList);
        rightDraweritemList = (ListView) rightDrawerContent.findViewById(R.id.leftright_drawer_list);
        rightDraweritemList.setAdapter(rightDrawerAdapter);

    }

    private void initTopDrawerContent() {
        topDrawerContent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.topbottom_drawer_content, null);
        topAdapterList = new ArrayList<View>();
        topDrawerAdapter = new CustomAdapter(this, R.layout.topbottom_drawer_item, topAdapterList);
        topDraweritemList = (HorzListView) topDrawerContent.findViewById(R.id.topbottom_drawer_list);
        topDraweritemList.setAdapter(topDrawerAdapter);

    }

    private void initBottomDrawerContent() {
        bottomDrawerContent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.topbottom_drawer_content, null);
        bottomAdapterList = new ArrayList<View>();
        bottomDrawerAdapter = new CustomAdapter(this, R.layout.topbottom_drawer_item, bottomAdapterList);
        bottomDraweritemList = (HorzListView) bottomDrawerContent.findViewById(R.id.topbottom_drawer_list);
        bottomDraweritemList.setAdapter(bottomDrawerAdapter);

    }

    /**
     * @param drawer
     *            drawer id to be layout
     * @param position
     *            position of the drawer on screen
     * @param conten
     *            content layout for the drawer items
     */
    private SlidingTray initDrawer(int position, View drawer) {
        LinearLayout drawerhandle = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawerhandle, null);
        SlidingTray slidedrawer = new SlidingTray(this, drawerhandle, drawer, position);
        return slidedrawer;
    }

    protected void openDrawer() {
        if (leftDrawerContent != null) {
            leftDrawer = initDrawer(SlidingTray.LEFT, leftDrawerContent);
            leftDrawerContainer.addView(leftDrawer);
            leftDrawer.animateOpen();
        }
        if (rightDrawerContent != null) {
            rightDrawer = initDrawer(SlidingTray.RIGHT, rightDrawerContent);
            rightDrawerContainer.addView(rightDrawer);
            rightDrawer.animateOpen();
        }
        if (bottomDrawerContent != null) {
            bottomDrawer = initDrawer(SlidingTray.BOTTOM, bottomDrawerContent);
            bottomDrawerContainer.addView(bottomDrawer);
            bottomDrawer.animateOpen();
        }
        if (topDrawerContent != null) {
            topDrawer = initDrawer(SlidingTray.TOP, topDrawerContent);
            topDrawerContainer.addView(topDrawer);
            topDrawer.animateOpen();
        }
    }

    class CustomAdapter extends ArrayAdapter<View> {
        Context context;
        int layoutResourceId;
        List<View> objects = null;

        public CustomAdapter(Context context, int textViewResourceId, List<View> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            layoutResourceId = textViewResourceId;
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return objects.get(position);
        }
    }

    protected void setOnItemListener(ItemClickListener itemClick) {
        if (leftDraweritemList != null) {
            leftDraweritemList.setOnItemClickListener(itemClick);
            leftDraweritemList.setOnItemLongClickListener(itemClick);
        }
        if (rightDraweritemList != null) {
            rightDraweritemList.setOnItemClickListener(itemClick);
            rightDraweritemList.setOnItemLongClickListener(itemClick);
        }
        if (topDraweritemList != null) {
            topDraweritemList.setOnItemClickListener(itemClick);
            topDraweritemList.setOnItemLongClickListener(itemClick);
        }
        if (bottomDraweritemList != null) {
            bottomDraweritemList.setOnItemClickListener(itemClick);
            bottomDraweritemList.setOnItemLongClickListener(itemClick);
        }

    }

    interface ItemClickListener extends OnItemLongClickListener, OnItemClickListener{

    }
}
