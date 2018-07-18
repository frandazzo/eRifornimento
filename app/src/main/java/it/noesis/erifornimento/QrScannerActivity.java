package it.noesis.erifornimento;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import it.noesis.erifornimento.utils.Constants;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class QrScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    public static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        //se la versione delle api è superiore alla 23
        //allora i permessi li devo chiedere esplicitamente
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (evaluatePermission()){
                //ritorno all'attività chiamante


                Toast.makeText(this,"Permesso concesso precedentemente", Toast.LENGTH_SHORT).show();

            }else{
                requirePermission();
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (evaluatePermission()){
                //ritorno all'attività chiamante
                if (scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();


            }else{
                requirePermission();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
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
    public void handleResult(Result result) {
            String scanResult = result.getText();

            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.DATA,scanResult);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
    }
}
