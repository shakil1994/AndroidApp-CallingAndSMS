package com.blackbirds.shakil.phonecallservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1560;
    ImageView imgCall;
    AppCompatEditText edtNumber, edtTextMessage, edtPhoneNumber;
    AppCompatButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNumber = findViewById(R.id.edtNumber);
        imgCall = findViewById(R.id.imgCall);
        edtTextMessage = findViewById(R.id.edtTextMessage);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnSend = findViewById(R.id.btnSend);

        imgCall.setOnClickListener(view -> {
            makeCall();
        });

        btnSend.setOnClickListener(view -> {
            sendSMS();
        });

    }

    private void sendSMS() {
        String phoneNumber = edtPhoneNumber.getText().toString();
        String textMessage = edtTextMessage.getText().toString();

        if (phoneNumber.trim().length() > 0 && textMessage.trim().length() > 0){
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.SEND_SMS)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNumber, null, textMessage, null, null);
                            Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        }
                    })
                    .check();
        }
        else {
            Toast.makeText(this, "Please Enter Your Number", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeCall() {
        String number = edtNumber.getText().toString();
        if (number.trim().length() > 0){
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.CALL_PHONE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            String dail = "tel:" + number;
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dail)));
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        }
                    })
                    .check();
        }
        else {
            Toast.makeText(this, "Please Enter Your Number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //makeCall();
                sendSMS();
            }
        }
    }
}