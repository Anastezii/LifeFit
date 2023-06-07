package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.Date;

public class BuySportActivity extends AppCompatActivity {

    private SignInButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sport);

        button=findViewById(R.id.google_pay_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


    }

    private void showDialog() {

        final Dialog dialog=new Dialog(BuySportActivity.this);

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

        final Dialog dialog=new Dialog(BuySportActivity.this);

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