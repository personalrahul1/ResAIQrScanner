package bharatiya.innovators.resaiqrscanner;

import static bharatiya.innovators.resaiqrscanner.AuthenticationAct.F_AUTH;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class QrScanAct extends AppCompatActivity {

    private TextView decodedValueTextView;
    private IntentIntegrator qrScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        decodedValueTextView = findViewById(R.id.decodedValueTextView);

        qrScanner = new IntentIntegrator(this);
        qrScanner.setPrompt("Scan a QR Code");
        qrScanner.setOrientationLocked(false);
        findViewById(R.id.scanButton).setOnClickListener(view -> qrScanner.initiateScan());
        GifImageView qrImg = findViewById(R.id.qrImg);
        qrImg.setOnClickListener(view -> qrScanner.initiateScan());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String decodedValue = result.getContents();
            decodedValueTextView.setText(decodedValue);
            saveQRData(decodedValue);
        }
    }

    private void saveQRData(String decodedValue) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String documentId = db.collection("QRCodeData").document().getId();
        Map<String, Object> data = new HashMap<>();
        data.put("decodedValue", decodedValue);

        db.collection(F_AUTH.getCurrentUser().getUid()).document(documentId)
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(QrScanAct.this, "Data saved to Firestore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(QrScanAct.this, "Failed to save data to Firestore", Toast.LENGTH_SHORT).show();
                });
    }

}