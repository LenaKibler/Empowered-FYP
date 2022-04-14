package com.example.empowered;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.paris.Paris;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SalesFlowPage extends AppCompatActivity implements SalesDialog.SalesDialogListener {

    FloatingActionButton addButton;
    FloatingActionButton deleteButton;

    private BottomNavigationView bottomNavigationView;
    private LinearLayout linearLayoutCallbacks;
    private LinearLayout linearLayoutNotIn;
    private LinearLayout linearLayoutNoSale;
    private LinearLayout linearLayoutSaleMade;
    private Context context = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_flow_page);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_logo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1A759F")));

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);


        linearLayoutCallbacks = findViewById(R.id.CallbacksColumn);
        // Set on drag listener to target dropped view.
        linearLayoutCallbacks.setOnDragListener(new DragDropOnDragListener(context));

        linearLayoutNotIn = findViewById(R.id.NotInColumn);
        // Set on drag listener to target dropped view.
        linearLayoutNotIn.setOnDragListener(new DragDropOnDragListener(context));

        linearLayoutNoSale = findViewById(R.id.NoSaleColumn);
        // Set on drag listener to target dropped view.
        linearLayoutNoSale.setOnDragListener(new DragDropOnDragListener(context));

        linearLayoutSaleMade = findViewById(R.id.SaleMadeColumn);
        // Set on drag listener to target dropped view.
        linearLayoutSaleMade.setOnDragListener(new DragDropOnDragListener(context));


        addButton.setOnClickListener(view -> {

            SalesDialog salesDialog = new SalesDialog();
            salesDialog.show(getSupportFragmentManager(), "example dialog");

        });


    }

    @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
    @Override
    public void applyTexts(String name, String eircode, String mobile, String supplier,
                           String returns, String time, String comments) {

        //Linear Layout to hold user sales info input
        LinearLayout rootLayout = new LinearLayout(SalesFlowPage.this);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        //rootLayout.setBackgroundResource(R.drawable.boarders);
        Paris.style(rootLayout).apply(R.style.SalesContainer);
        rootLayout.setTag("Main");
        rootLayout.setOnTouchListener(new DragDropOnTouchListener());
        rootLayout.setClickable(true);


        //Linear Layout for name
        LinearLayout nameLayout = new LinearLayout(SalesFlowPage.this);
        nameLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        nameLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Linear Layout for eircode
        LinearLayout eircodeLayout = new LinearLayout(SalesFlowPage.this);
        eircodeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        eircodeLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Linear Layout for mobile number
        LinearLayout mobileLayout = new LinearLayout(SalesFlowPage.this);
        mobileLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mobileLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Linear Layout for supplier
        LinearLayout supplierLayout = new LinearLayout(SalesFlowPage.this);
        supplierLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        supplierLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Linear Layout for returns date
        LinearLayout returnsLayout = new LinearLayout(SalesFlowPage.this);
        returnsLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        returnsLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Linear Layout for returns time
        LinearLayout timeLayout = new LinearLayout(SalesFlowPage.this);
        timeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        timeLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Linear Layout for comments
        LinearLayout commentsLayout = new LinearLayout(SalesFlowPage.this);
        commentsLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        commentsLayout.setOrientation(LinearLayout.HORIZONTAL);

        //text input holders
        TextView txtNamelabel = new TextView(SalesFlowPage.this);
        txtNamelabel.setText("Name: ");
        TextView txtName = new TextView(SalesFlowPage.this);
        txtName.setText(name);
        nameLayout.addView(txtNamelabel);
        nameLayout.addView(txtName);
        txtName.setTextAppearance(this, R.style.textStyle);
        txtNamelabel.setTextAppearance(this, R.style.TextViewStyles);


        TextView txtEircodelabel = new TextView(SalesFlowPage.this);
        txtEircodelabel.setText("Eircode: ");
        TextView txtEircode = new TextView(SalesFlowPage.this);
        txtEircode.setText(eircode);
        eircodeLayout.addView(txtEircodelabel);
        eircodeLayout.addView(txtEircode);
        txtEircode.setTextAppearance(this, R.style.textStyle);
        txtEircodelabel.setTextAppearance(this, R.style.TextViewStyles);


        TextView txtMobilelabel = new TextView(SalesFlowPage.this);
        txtMobilelabel.setText("Mobile: ");
        TextView txtMobile = new TextView(SalesFlowPage.this);
        txtMobile.setText(mobile);
        mobileLayout.addView(txtMobilelabel);
        mobileLayout.addView(txtMobile);
        txtMobile.setTextAppearance(this, R.style.textStyle);
        txtMobilelabel.setTextAppearance(this, R.style.TextViewStyles);


        TextView txtSupplierlabel = new TextView(SalesFlowPage.this);
        txtSupplierlabel.setText("Supplier: ");
        TextView txtSupplier = new TextView(SalesFlowPage.this);
        txtSupplier.setText(supplier);
        supplierLayout.addView(txtSupplierlabel);
        supplierLayout.addView(txtSupplier);
        txtSupplier.setTextAppearance(this, R.style.textStyle);
        txtSupplierlabel.setTextAppearance(this, R.style.TextViewStyles);


        TextView txtReturnslabel = new TextView(SalesFlowPage.this);
        txtReturnslabel.setText("Return Date: ");
        TextView txtReturns = new TextView(SalesFlowPage.this);
        txtReturns.setText(returns);
        returnsLayout.addView(txtReturnslabel);
        returnsLayout.addView(txtReturns);
        txtReturns.setTextAppearance(this, R.style.textStyle);
        txtReturnslabel.setTextAppearance(this, R.style.TextViewStyles);

        TextView txtTimelabel = new TextView(SalesFlowPage.this);
        txtTimelabel.setText("Return Time: ");
        TextView txtTime = new TextView(SalesFlowPage.this);
        txtTime.setText(time);
        timeLayout.addView(txtTimelabel);
        timeLayout.addView(txtTime);
        txtTime.setTextAppearance(this, R.style.textStyle);
        txtTimelabel.setTextAppearance(this, R.style.TextViewStyles);

        TextView txtCommentslabel = new TextView(SalesFlowPage.this);
        txtCommentslabel.setText("Comments: ");
        TextView txtComments = new TextView(SalesFlowPage.this);
        txtComments.setText(comments);
        commentsLayout.addView(txtCommentslabel);
        commentsLayout.addView(txtComments);
        txtComments.setTextAppearance(this, R.style.textStyle);
        txtCommentslabel.setTextAppearance(this, R.style.TextViewStyles);


        rootLayout.addView(nameLayout);
        rootLayout.addView(eircodeLayout);
        rootLayout.addView(mobileLayout);
        rootLayout.addView(supplierLayout);
        rootLayout.addView(returnsLayout);
        rootLayout.addView(timeLayout);
        rootLayout.addView(commentsLayout);


        linearLayoutCallbacks.addView(rootLayout);


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {

                rootLayout.setOnTouchListener(new View.OnTouchListener() {
                    final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public boolean onDoubleTap(MotionEvent e){

                            AlertDialog alertDialog = new AlertDialog.Builder(SalesFlowPage.this).create();
                            alertDialog.setTitle("");
                            alertDialog.setMessage("Are you sure you want to delete this entry?");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"YES",
                                    (dialogInterface, i) -> rootLayout.setVisibility(View.GONE));
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"NO",
                                    (dialogInterface, i) -> rootLayout.setOnTouchListener(new DragDropOnTouchListener()));
                            alertDialog.show();


                            return super.onDoubleTap(e);
                        }

                    });


                    @SuppressLint("ClickableViewAccessibility")
                    public boolean onTouch(View view, MotionEvent motionEvent){

                        gestureDetector.onTouchEvent(motionEvent);
                        return false;
                    }
                });
            }
        });


    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod= menuitem -> {

        switch(menuitem.getItemId()){

            case R.id.map:

                Intent intentMap = new Intent(SalesFlowPage.this, MapsPage.class);
                //The purpose of this flag is to bring existing activity instance to the
                //foreground if one already exists
                //This ensures that contents created by user in different activities
                //won't be deleted as the user navigates through the app
                intentMap.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentMap);

                break;

            case R.id.sales:
                /*startActivity(new Intent(SalesFlowPage.this, SalesFlowPage.class));*/
                break;

            case R.id.logout:

                //Firebase logout functionality
                FirebaseAuth.getInstance().signOut();

                //Navigate user back to login page
                startActivity(new Intent(SalesFlowPage.this, LoginPage.class));

                break;

            case R.id.calculator:

                Intent intentCalculator = new Intent(SalesFlowPage.this, CalculatorPage.class);
                intentCalculator.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentCalculator);

                break;

        }
        return false;
    };

}