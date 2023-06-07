package com.example.fitness.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.fitness.R;
import com.google.android.gms.common.SignInButton;

public class BuyFoodActivity extends AppCompatActivity {

    private SignInButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_food);

        button=findViewById(R.id.google_pay_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    private void showDialog() {

        final Dialog dialog=new Dialog(BuyFoodActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.google_pay_dialog);

        final EditText card_number=dialog.findViewById(R.id.number_card);
        final EditText cvv=dialog.findViewById(R.id.cvv);
        final EditText expire_date=dialog.findViewById(R.id.date_expire);
        final CheckBox terms=dialog.findViewById(R.id.terms);
        Button payButton=dialog.findViewById(R.id.pay);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int number=Integer.parseInt(card_number.getText().toString());
                int cvv_number=Integer.parseInt(cvv.getText().toString());
                String expire_date_num=expire_date.getText().toString();
                Boolean hasAccepted=terms.isChecked();
                showAccepted();
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    private void showAccepted() {

        final Dialog dialog=new Dialog(BuyFoodActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.payment);

        Button close=dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}