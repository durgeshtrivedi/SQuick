package com.sileria.android.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ExpandViewAnimation extends Animation {
    int targetWidth;
    int initialWidth;
    View view;

    public ExpandViewAnimation(View view, int targetWidth) {
      this.view = view;
      this.targetWidth = targetWidth;
      initialWidth = view.getWidth();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

      int newWidth = initialWidth + (int) ((targetWidth - initialWidth) * interpolatedTime);

      LayoutParams lp = new LayoutParams((LayoutParams) view.getLayoutParams());
      lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      view.setLayoutParams(lp);

      view.getLayoutParams().width = newWidth;
      view.requestLayout();

      if (newWidth == targetWidth) {
        LayoutParams matchParentParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        view.setLayoutParams(matchParentParams);
        view.clearAnimation();
      }
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
      super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
      return true;
    }
  }