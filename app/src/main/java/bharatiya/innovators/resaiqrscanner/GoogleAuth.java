package bharatiya.innovators.resaiqrscanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class GoogleAuth extends AppCompatActivity {
    DialogsSnackBarPopMenu dialogPopUp;

    ActivityResultLauncher<Intent> googleSignInResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                } else {
                    super.onBackPressed();
                    finish();
                    dialogPopUp.DismissLoadingDialog();
                }
            });

    private void initialisation() {
        dialogPopUp = new DialogsSnackBarPopMenu(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialisation();

        dialogPopUp.LoadingDialog(null, R.drawable.loader_round_green_smile, 1, false,
                "Processing...", "", "");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInResult.launch(signInIntent);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
            Toast.makeText(getApplicationContext(), account.getEmail(), Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        assert user != null;
                        String UUID = user.getUid();
                        String name = user.getDisplayName();
                        String email = user.getEmail();
                        DateTimeUtil dateTimeUtil = new DateTimeUtil();
                        dateTimeUtil.getCurrentDateTime();
                        AuthModelData authModelData = new AuthModelData(name, email, null,
                                dateTimeUtil.getCurrentDateTime(), DateTimeUtil.getTimeStamp());
                        FirebaseDatabase.getInstance().getReference()
                                .child("User's Auth Data/" + UUID)
                                .setValue(authModelData)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        startActivity(new Intent(GoogleAuth.this, QrScanAct.class));
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Something went wrong\nplease try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        Toast.makeText(getApplicationContext(), "Signed In",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        dialogPopUp.DismissLoadingDialog();
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                    }
                });
    }

}