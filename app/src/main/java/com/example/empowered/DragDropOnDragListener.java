package com.example.empowered;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

public class DragDropOnDragListener extends AppCompatActivity implements View.OnDragListener {
    private Context context = null;
    public DragDropOnDragListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {

        // Getting the drag drop action.
        int dragAction = dragEvent.getAction();
        if(dragAction == dragEvent.ACTION_DRAG_STARTED)
        {
            // Checking if the dragged view can be placed in this target view or not.
            ClipDescription clipDescription = dragEvent.getClipDescription();
            if (clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                // Return true as target view can accept the dragged object.
                return true;
            }
        }else if(dragAction == dragEvent.ACTION_DRAG_ENTERED)
        {
            // When the dragged view enters the target view, the target view background color
            // changes.
            changeTargetViewBackground(view, Color.parseColor("#D9ED92"));
            return true;
        }else if(dragAction == dragEvent.ACTION_DRAG_EXITED)
        {
            // If the dragged view exits the target view area,
            // the previously set background colour clears
            resetTargetViewBackground(view);
            return true;
        }else if(dragAction == dragEvent.ACTION_DRAG_ENDED)
        {
            // When the drop ended reset target view background color.
            resetTargetViewBackground(view);

            return true;
        }else if(dragAction == dragEvent.ACTION_DROP)
        {
            //If drop action happened then the dragged views clip data
            //is placed in the drag event
            ClipData clipData = dragEvent.getClipData();
            // Get drag and drop item count.
            int itemCount = clipData.getItemCount();
            // If item count bigger than 0 i.e., if something has been
            // dropped inside of the target view
            if(itemCount > 0) {
                // Resetting target view background color.
                resetTargetViewBackground(view);
                // Getting dragged view object from drag event object.
                View srcView = (View)dragEvent.getLocalState();
                // Getting dragged view's parent view group.
                ViewGroup owner = (ViewGroup) srcView.getParent();
                // Removing source view from original parent view group.
                owner.removeView(srcView);
                // The drop target view will be a LinearLayout object
                // so casting the view object to the correct type.
                LinearLayout newContainer = (LinearLayout) view;
                // Adding dragged view in the new container.
                newContainer.addView(srcView);
                // Making the dragged view object visible.
                srcView.setVisibility(View.VISIBLE);
                // Returns true to make DragEvent.getResult() value to true.
                return true;
            }
        }else if(dragAction == dragEvent.ACTION_DRAG_LOCATION)
        {
            return true;
        }else
        {

        }
        return false;
    }

    //Resetting target view's background color
    private void resetTargetViewBackground(View view)
    {
        // Clear color filter.
        view.getBackground().clearColorFilter();
        // Redraw the target view to original color.
        view.invalidate();
    }
    //Changing target view's background color
    private void changeTargetViewBackground(View view, int color)
    {

        //SRC_IN means source view ( dragged view ) entering the target view.
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        // Redraw the target view to new color.
        view.invalidate();
    }
}
