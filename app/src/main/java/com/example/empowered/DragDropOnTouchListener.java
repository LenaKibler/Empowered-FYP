package com.example.empowered;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

public class DragDropOnTouchListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // Get view object tag value.
        String tag = (String)view.getTag();
        // Create clip data.
        ClipData clipData = ClipData.newPlainText("", tag);
        // Create drag shadow builder object.
        View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);

        view.startDrag(clipData, dragShadowBuilder, view, 0);
        // Hiding the view object because it is being dragged
        view.setVisibility(View.INVISIBLE);
        return true;
    }
}
