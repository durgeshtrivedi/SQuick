/**Copyright (c) 2013 Durgesh Trivedi

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.durgesh.quick.centermenu;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

import com.durgesh.R;

public class SQCenterMenu extends AbsoluteLayout implements OnTouchListener, GestureDetector.OnGestureListener {

    private final double arcLength = (3.14 * 2 / 250);
    private Context mcontext;
    public SQCenterMenuListener mSpecialmenuCallBack;
    // the GestureDector for checking user behavour like onClick,Long Press
    private GestureDetector mTapDetector;
    // main menuButton
    private ImageView menuButton;
    private final int menuButtonLength = 60; // Menu Button
    private final int submenuButtonLength = 30; // SubMenu Button
    private int menuButtonX = 0; // Menu Button X coordinate
    private int menuButtonY = 0; // Menu Button Y coordinate
    private ArrayList<ImageView> subMenuLeft;
    private ArrayList<ImageView> subMenuRight;
    private ArrayList<SQCenterMenuItem> submenuModel; // submenu model store the information of each submenu
    private double degreegap = 0;

    private int actionid = -1;
    private boolean openFlag = false;
    private int layoutSize;
    private final String logName = "SpecialMenu";

    public SQCenterMenu(Context context, int layoutSize, int position, ArrayList<SQCenterMenuItem> _model) {
        super(context);
        // TODO Auto-generated constructor stub

        mcontext = context;
        submenuModel = _model;

        mTapDetector = new GestureDetector(this); // use Gesture Detector for touch event

        degreegap = 120 / submenuModel.size();
        // degreegap = 120 / 5; // divede number of sub menu item

        Log.d(logName, "new Degree : " + degreegap);
        this.layoutSize = layoutSize;

    }

    public void setspecialmenuCallBack(SQCenterMenuListener v) {
        this.mSpecialmenuCallBack = v;
    }

    public void onBuildComponent(ImageView imageView) {
        menuButton = imageView;
        menuButton.setBackgroundResource(R.drawable.superquick_gray);
        menuButton.setOnTouchListener(this);
        menuButtonX = (int) (layoutSize / 1.8);
        menuButtonY = (int) (layoutSize / 1.8);
        setBackgroundColor(Color.LTGRAY);
        setMinimumHeight(400);

        addView(menuButton, new AbsoluteLayout.LayoutParams(menuButtonLength, menuButtonLength, menuButtonX, menuButtonY));
        subMenuRight = new ArrayList<ImageView>();
        onBuildSubMenuComponent(-45, subMenuRight);
        subMenuLeft = new ArrayList<ImageView>();
        onBuildSubMenuComponent(80, subMenuLeft);
    }

    private void onBuildSubMenuComponent(int degree, ArrayList<ImageView> subMenu) {

        for (int i = 0; i < submenuModel.size(); i++) {
            ImageView ib = new ImageView(mcontext);
            ib.setBackgroundResource(R.drawable.bgbutton);
            ib.setOnTouchListener(this);
            ib.setVisibility(INVISIBLE);
            int x = (int) getCircleX(menuButtonX, arcLength * (degreegap * i + degree), 100);
            int y = (int) getCircleY(menuButtonY, arcLength * (degreegap * i + degree), 100);
            addView(ib, new AbsoluteLayout.LayoutParams(submenuButtonLength, submenuButtonLength, x, y));
            subMenu.add(ib);
        }
        menuButton.bringToFront();
    }

    // move StartMenu
    private void startSubMenuMove(int degree, ArrayList<ImageView> subMenu) {
        openFlag = true;
        int time = 800;
        for (int i = 0; i < subMenu.size(); i++) {
            subMenu.get(i).setVisibility(VISIBLE);
            int x = (int) getCircleX(menuButtonX, arcLength * (degreegap * i + degree), 100);
            int y = (int) getCircleY(menuButtonY, arcLength * (degreegap * i + degree), 100);
            subMenu.get(i).startAnimation(AnimationSetMoveOut(-(x - menuButtonX), 0, -(y - menuButtonY), 0, time));
            time -= 50;

        }
    }

    private void removeSubMenuMove(int degree, ArrayList<ImageView> subMenu) {
        openFlag = false;
        int time = 800;
        for (int i = 0; i < subMenu.size(); i++) {
            subMenu.get(i).clearAnimation();
            int x = (int) getCircleX(menuButtonX, arcLength * (degreegap * i + degree), 100);
            int y = (int) getCircleY(menuButtonY, arcLength * (degreegap * i + degree), 100);
            Log.i("TEST", "x: " + x + " y: " + y);
            subMenu.get(i).startAnimation(AnimationSetMoveIn(0, -(x - menuButtonX) + 5, 0, -(y - menuButtonY), time));
            subMenu.get(i).setVisibility(INVISIBLE);
            time -= 50;
        }
    }

    // Animation Set for Menu
    private AnimationSet AnimationSetA() {
        AnimationSet rootSet = new AnimationSet(true);
        rootSet.setInterpolator(new AccelerateInterpolator());
        rootSet.setRepeatMode(Animation.ABSOLUTE);

        RotateAnimation rotateA = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateA.setRepeatCount(0);
        rotateA.setDuration(250);

        rootSet.addAnimation(rotateA);
        return rootSet;
    }

    // Animation Set for Menu
    private AnimationSet AnimationSetA2() {
        AnimationSet rootSet = new AnimationSet(true);
        rootSet.setInterpolator(new AccelerateInterpolator());
        rootSet.setRepeatMode(Animation.ABSOLUTE);

        RotateAnimation rotateA = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateA.setRepeatCount(0);
        rotateA.setDuration(250);

        rootSet.addAnimation(rotateA);
        return rootSet;
    }

    // Move out Animation
    private AnimationSet AnimationSetMoveOut(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, int timer) {
        AnimationSet rootSet = new AnimationSet(true);
        rootSet.setInterpolator(new AccelerateInterpolator());
        rootSet.setRepeatMode(Animation.ABSOLUTE);
        TranslateAnimation translateA = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        translateA.setDuration(timer);
        rootSet.addAnimation(translateA);
        return rootSet;
    }

    // Move back Animation
    private AnimationSet AnimationSetMoveIn(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, int timer) {
        AnimationSet rootSet = new AnimationSet(true);
        rootSet.setInterpolator(new AccelerateInterpolator());
        rootSet.setRepeatMode(Animation.ABSOLUTE);
        TranslateAnimation translateA = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        translateA.setDuration(timer);
        rootSet.addAnimation(translateA);
        return rootSet;
    }

    // geting the Circle Point X
    private double getCircleX(int centerX, double delta, int radius) {
        return radius * Math.cos(delta) + centerX;
    }

    // geting the Circle Point Y
    private double getCircleY(int centerY, double delta, int radius) {
        return radius * Math.sin(delta) + centerY;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        if (v.equals(menuButton))
            actionid = 100;
        else {
            for (int i = 0; i < 5; i++) {
                if (v.equals(subMenuRight.get(i)) || v.equals(subMenuLeft.get(i))) actionid = i;
            }
        }
        return mTapDetector.onTouchEvent(event);
        // return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        Log.i("TEST", "onDown");

        switch (actionid) {
        case 100:
            Log.i("TEST", "menuButton pressed");

            if (!openFlag) {
                menuButton.startAnimation(AnimationSetA());
                startSubMenuMove(-45, subMenuRight);
                startSubMenuMove(80, subMenuLeft);
            } else {
                menuButton.startAnimation(AnimationSetA2());
                removeSubMenuMove(-45, subMenuRight);
                removeSubMenuMove(80, subMenuLeft);
            }
            break;
        default:
            menuButton.startAnimation(AnimationSetA2());
            removeSubMenuMove(-45, subMenuRight);
            removeSubMenuMove(80, subMenuLeft);
            mSpecialmenuCallBack.subMenuEvent(actionid);
            Log.i("TEST", "sunMenuButton pressed : action id is : " + actionid);
        }

        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        actionid = -1;
        return false;
    }

}
