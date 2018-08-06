package it.noesis.erifornimento;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import it.noesis.erifornimento.utils.Constants;

import static android.Manifest.permission.CAMERA;

public class BudievNativeQrScannerActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    public static final int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //poichè la libreria ha un minsdk di 19 mentre il progetto ha un minsdk di 15
        //sono stato costretto ad aggiungere nel manifest una direttiva che consente
        //comunque l'utilizzo della libreria. In questi casi pero' possono esserci dei crush
        //che proteggo con una try catch e con la rterminazione dell'activity
        try{

            setContentView(R.layout.activity_budiev_native_qr_scanner);
            CodeScannerView scannerView = findViewById(R.id.scanner_view);
            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    BudievNativeQrScannerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(BudievNativeQrScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();


                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(Constants.DATA,result.getText());
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }
                    });
                }
            });
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if (BudievNativeQrScannerActivity.this.evaluatePermission()){

                                mCodeScanner.startPreview();


                            }else{
                                BudievNativeQrScannerActivity.this.requirePermission();
                            }
                        }
                    }catch(Exception ex){
                        BudievNativeQrScannerActivity.this.finish();
                    }
                }
            });




        }catch (Exception ex){
            Log.e("Budyev Activity", "Errore attitvità scanner QRCode Budyev: " + ex.getMessage());
            finish();
        }



    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (evaluatePermission()){

                    mCodeScanner.startPreview();


                }else{
                    requirePermission();
                }
            }
        }catch(Exception ex){
            finish();
        }




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

    private void requirePermission() {
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);
    }

    @Override
    protected void onPause() {
        try{
            mCodeScanner.releaseResources();
        }catch(Exception ex){

        }

        super.onPause();
    }
}
