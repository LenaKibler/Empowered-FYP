package com.example.empowered;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;

public class CalculatorPage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight,
            btnNine, btnDot, btnZero, btnDivide, btnAdd, btnMultiply, btnClear,
            btnEquals, btnMinus;

    private TextView tvInfo;
    private EditText etEdit;


    private Double val1 = Double.NaN;
    private Double val2;

    private static final char ADDITION = '+';
    private static final char SUBTRACTION = '-';
    private static final char MULTIPLICATION = '*';
    private static final char DIVISION = '/';

    private char CURRENT_ACTION;



    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_page);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_logo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1A759F")));


        bottomNavigationView=findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

        decimalFormat = new DecimalFormat("#.##########");

        tvInfo = findViewById(R.id.tv_info);
        etEdit = findViewById(R.id.et_edit);

        btnSeven = findViewById(R.id.btn_seven);
        btnSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEdit.setText(etEdit.getText() + "7");
            }
        });

        btnEight = findViewById(R.id.btn_eight);
        btnEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEdit.setText(etEdit.getText() + "8");
            }
        });

        btnNine = findViewById(R.id.btn_nine);
        btnNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEdit.setText(etEdit.getText() + "9");
            }
        });

        btnFour = findViewById(R.id.btn_four);
        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEdit.setText(etEdit.getText() + "4");
            }
        });

        btnFive = findViewById(R.id.btn_five);
        btnFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEdit.setText(etEdit.getText() + "5");
            }
        });

        btnSix = findViewById(R.id.btn_six);
        btnSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEdit.setText(etEdit.getText() + "6");
            }
        });

        btnOne = findViewById(R.id.btn_one);
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEdit.setText(etEdit.getText() + "1");
            }
        });

        btnTwo = findViewById(R.id.btn_two);
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEdit.setText(etEdit.getText() + "2");
            }
        });

        btnThree = findViewById(R.id.btn_three);
        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEdit.setText(etEdit.getText() + "3");
            }
        });


        btnDot = findViewById(R.id.btn_dot);
        btnDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEdit.setText(etEdit.getText() + ".");
            }
        });

        btnZero = findViewById(R.id.btn_zero);
        btnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEdit.setText(etEdit.getText() + "0");
            }
        });

        btnEquals = findViewById(R.id.btn_equals);
        btnEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculate();
                tvInfo.setText(tvInfo.getText().toString() + decimalFormat.format(val2) + " = " + decimalFormat.format(val1));
                val1 = Double.NaN;
                CURRENT_ACTION = '0';
            }
        });

        btnDivide = findViewById(R.id.btn_divide);
        btnDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculate();
                CURRENT_ACTION = DIVISION;
                tvInfo.setText(decimalFormat.format(val1) + " / ");
                etEdit.setText(null);
            }
        });

        btnMultiply = findViewById(R.id.btn_multiply);
        btnMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculate();
                CURRENT_ACTION = MULTIPLICATION;
                tvInfo.setText(decimalFormat.format(val1) + " * ");
                etEdit.setText(null);
            }
        });


        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculate();
                CURRENT_ACTION = ADDITION;
                tvInfo.setText(decimalFormat.format(val1) + " + ");
                etEdit.setText(null);
            }
        });

        btnMinus = findViewById(R.id.btn_minus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculate();
                CURRENT_ACTION = SUBTRACTION;
                tvInfo.setText(decimalFormat.format(val1) + " - ");
                etEdit.setText(null);
            }
        });

        btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEdit.getText().length() > 0){
                    CharSequence currentText = etEdit.getText();
                    etEdit.setText(currentText.subSequence(0, currentText.length() - 1));
                }else{
                    val1 = Double.NaN;
                    val2 = Double.NaN;
                    etEdit.setText("");
                    tvInfo.setText("");
                }
            }
        });
    }

    private void Calculate(){
        if (!Double.isNaN(val1)){
            val2 = Double.parseDouble(etEdit.getText().toString());
            etEdit.setText(null);

            switch (CURRENT_ACTION){
                case ADDITION:
                    val1 = this.val1 + val2;
                    break;
                case SUBTRACTION:
                    val1 = this.val1 - val2;
                    break;
                case MULTIPLICATION:
                    val1 = this.val1 * val2;
                    break;
                case DIVISION:
                    val1 = this.val1 / val2;
                    break;
            }
        }else{
            try{
                val1 = Double.parseDouble(etEdit.getText().toString());
            }catch (Exception e){}
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod= menuitem -> {

        switch(menuitem.getItemId()){

            case R.id.map:

                Intent intentMap = new Intent(CalculatorPage.this, MapsPage.class);
                //The purpose of this flag is to bring existing activity instance to the
                //foreground if one already exists
                //This ensures that contents created by user in different activities
                //won't be deleted as the user navigates through the app
                intentMap.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentMap);

                break;

            case R.id.sales:

                Intent intentSales = new Intent(CalculatorPage.this, SalesFlowPage.class);
                intentSales.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentSales);
                break;

            case R.id.logout:

                //Firebase logout functionality
                FirebaseAuth.getInstance().signOut();

                //Navigate user back to login page
                startActivity(new Intent(CalculatorPage.this, LoginPage.class));
                break;

            case R.id.calculator:

                break;

        }
        return false;
    };
}