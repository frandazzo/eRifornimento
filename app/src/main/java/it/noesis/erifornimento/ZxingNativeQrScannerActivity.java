package it.noesis.erifornimento;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import it.noesis.erifornimento.utils.Constants;


import static android.Manifest.permission.CAMERA;


public class ZxingNativeQrScannerActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener{// implements ZXingScannerView.ResultHandler{

    public static final int REQUEST_CAMERA = 1;
    private QRCodeReaderView qrCodeReaderView;

    //private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        scannerView = new ZXingScannerView(this);
//        setContentView(scannerView);
        setContentView(R.layout.activity_qr_scanner);



        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        // qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        //qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();


   

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (evaluatePermission()){

                qrCodeReaderView.startCamera();


            }else{
                requirePermission();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //scannerView.stopCamera();
        qrCodeReaderView.stopCamera();
    }




    private void requirePermission() {
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);
    }

    private boolean evaluatePermission() {

        return ContextCompat.checkSelfPermission(this, CAMERA)
                == PackageManager.PERMISSION_GRANTED;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // camera-related task you need to do.
                    Toast.makeText(this,"Permesso concesso", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"Permesso negato", Toast.LENGTH_SHORT).show();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(Constants.ERROR,"permesso negato");
                    setResult(Activity.RESULT_CANCELED,returnIntent);
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.DATA,text);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
    }

//    @Override
//    public void handleResult(Result result) {
//            String scanResult = result.getText();
//
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra(Constants.DATA,scanResult);
//            setResult(Activity.RESULT_OK,returnIntent);
//            finish();
//    }
}
