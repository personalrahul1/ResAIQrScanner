package bharatiya.innovators.resaiqrscanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.TransitionAdapter;

import com.google.firebase.auth.FirebaseAuth;

import pl.droidsonroids.gif.GifImageView;

public class AuthenticationAct extends AppCompatActivity {
    private CardView googleBtnCv;
    public static FirebaseAuth F_AUTH;

    private DialogsSnackBarPopMenu dialogPopUp;

    private void initialisation() {
        F_AUTH = FirebaseAuth.getInstance();
        dialogPopUp = new DialogsSnackBarPopMenu(this);
        googleBtnCv = findViewById(R.id.logSignGoogleBtnCv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_authentication);

        initialisation();
        motionOperation();
        new EmailAuth(AuthenticationAct.this);
        googleBtnCv.setOnClickListener(view ->
                startActivity(new Intent(AuthenticationAct.this, GoogleAuth.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)));
    }

    private void motionOperation() {
        GifImageView mainGifV = findViewById(R.id.logSignMainGif);
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        MotionLayout motionLayout = findViewById(R.id.logSignMLayout);
        motionLayout.addTransitionListener(new TransitionAdapter() {
            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if (currentId == R.id.signUpMotion) {
                    mainGifV.setImageResource(R.drawable.sign_up1);
                } else {
                    mainGifV.setImageResource(R.drawable.sign_in);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        dialogPopUp.ExitDialog("Abort Authentication",
                "Authentication is required in order to use this application.",
                "Authentication Cancelled");
    }

    @Override
    protected void onStart() {
        super.onStart();
        F_AUTH = FirebaseAuth.getInstance();
        if (F_AUTH.getCurrentUser() != null) {
            startActivity(new Intent(AuthenticationAct.this, QrScanAct.class));
            finish();
        }
    }
}