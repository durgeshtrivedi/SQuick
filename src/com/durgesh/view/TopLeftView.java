package com.durgesh.view;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.durgesh.quick.SQMainVeiw;

public class TopLeftView extends SQMainVeiw{

    
    
   
    public TopLeftView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onRightToLeft() {
        Toast mToast =  Toast.makeText(context.getApplicationContext(), "Long Press TopLeftView", Toast.LENGTH_SHORT);
        mToast.show();
        
    }

    @Override
    public void onLeftToRight() {
        Toast mToast =  Toast.makeText(context.getApplicationContext(), "Long Press TopLeftView", Toast.LENGTH_SHORT);
        mToast.show();
        
    }

    @Override
    public void onBottomToTop() {
        Toast mToast =  Toast.makeText(context.getApplicationContext(), "Long Press TopLeftView", Toast.LENGTH_SHORT);
        mToast.show();
        
    }

    @Override
    public void onTopToBottom() {
        Toast mToast =  Toast.makeText(context.getApplicationContext(), "Long Press TopLeftView", Toast.LENGTH_SHORT);
        mToast.show();
        
    }

   

}
