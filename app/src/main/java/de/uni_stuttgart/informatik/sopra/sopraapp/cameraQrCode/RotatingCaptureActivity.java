package de.uni_stuttgart.informatik.sopra.sopraapp.cameraQrCode;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * This class enables the QR-Code scanner
 */
public class RotatingCaptureActivity extends Activity
        implements DecoratedBarcodeView.TorchListener {

    private DecoratedBarcodeView barcodeView;
    private Button flashBtn;
    private CaptureManager capture;
    private String lastText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotatingcapture);

        Log.d("RotatingCaptureActivity", "onCreate started");

        flashBtn = findViewById(R.id.flashButton);
        barcodeView = findViewById(R.id.barcode_scanner);
        barcodeView.setStatusText(getString(R.string.statusTextBarcode));
        barcodeView.setTorchListener(this);
        Collection<BarcodeFormat> formats;
        formats = Collections.singletonList(BarcodeFormat.QR_CODE);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));

        capture = new CaptureManager(this, barcodeView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);

        final Activity activity = this;

        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                Log.d("MainActivity", "Starting result handling");

                if (result == null) {
                    Log.d("MainActivity", "Cancelled scan");
                    Toast.makeText(getParent(), getString(R.string.abgebrochenText), Toast.LENGTH_LONG).show();
                } else if (!result.getText().equals(lastText)) {
                    lastText = result.getText();
                    new ReactionController(activity, result.getText());
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });
    }


    /**
     * switching the flashlight.
     *
     * @param view View of flashButton.
     */
    public void switchFlashlight(View view) {
        if (getString(R.string.flashTurnOn).contentEquals(flashBtn.getText())) {
            barcodeView.setTorchOn();
        } else {
            barcodeView.setTorchOff();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        lastText = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
        lastText = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTorchOn() {
        flashBtn.setText(getString(R.string.flashTurnOff));
    }

    @Override
    public void onTorchOff() {
        flashBtn.setText(getString(R.string.flashTurnOn));
    }

}
