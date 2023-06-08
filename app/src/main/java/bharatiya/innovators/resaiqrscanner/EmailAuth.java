package bharatiya.innovators.resaiqrscanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class EmailAuth extends AuthenticationAct {
    private final Activity activity;

    protected TextInputEditText nameEt, signUpEmailEt, signUpPassEt, conPassEt,
            signInEmailEt, signInPassEt;

    TextInputLayout signPassIp;
    protected Button signUpBtn, signInBtn;
    public TextView signUpErrorTv, signInErrorTv, forgotPassTv, retrySignInTv, passResetLinkTv;
    private final DialogsSnackBarPopMenu dialogPopUp;


    private void emailInitial() {
        nameEt = activity.findViewById(R.id.signUpNameEt);
        signUpEmailEt = activity.findViewById(R.id.signUpEmailEt);
        signUpPassEt = activity.findViewById(R.id.signUpPasswordEt);
        conPassEt = activity.findViewById(R.id.signUpConfPasswordEt);
        signInEmailEt = activity.findViewById(R.id.signInEmailEt);
        signInPassEt = activity.findViewById(R.id.signInPasswordEt);
        signPassIp = activity.findViewById(R.id.signInPasswordIp);
        signUpBtn = activity.findViewById(R.id.signUpBtn);
        signInBtn = activity.findViewById(R.id.signInBtn);
        signUpErrorTv = activity.findViewById(R.id.signUpErrorTv);
        signInErrorTv = activity.findViewById(R.id.signInErrorTv);
        forgotPassTv = activity.findViewById(R.id.signInForgetPasswordTv);
        retrySignInTv = activity.findViewById(R.id.signInRetryTv);
        passResetLinkTv = activity.findViewById(R.id.signInSendLinkTv);
        signInBtn.setOnClickListener(view -> emailSignIn());
        signUpBtn.setOnClickListener(view -> emailSignUp());
        forgotPassTv.setOnClickListener(view -> forgetPassAct());
    }

    @SuppressLint("SetTextI18n")
    private void forgetPassAct() {
        forgotPassTv.setVisibility(View.INVISIBLE);
        signPassIp.setVisibility(View.GONE);
        signInBtn.setVisibility(View.GONE);
        retrySignInTv.setVisibility(View.VISIBLE);
        passResetLinkTv.setVisibility(View.VISIBLE);
        retrySignInTv.setOnClickListener(view -> activity.recreate());

        passResetLinkTv.setOnClickListener(view -> {
            String email = signInEmailEt.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                signInEmailEt.requestFocus();
                signInEmailEt.setError("Enter correct formatted Email");
            } else if (email.toLowerCase().contains("@gmail.com")) {
                dialogPopUp.BasicDialog(true,
                        "Seems Google Account", "Try Signing Using Google\nStill Facing Problem",
                        "", "Report Us", "No", "",
                        R.drawable.google, 0, 0, null);

                dialogPopUp.BASIC_BUTTON_NEGATIVE.setOnClickListener(view1 -> DialogsSnackBarPopMenu.FINAL_BASIC_DIALOG.dismiss());
                dialogPopUp.BASIC_BUTTON_POSITIVE.setOnClickListener(view1 -> {
                    Toast.makeText(activity, "send Report", Toast.LENGTH_SHORT).show();
                    DialogsSnackBarPopMenu.FINAL_BASIC_DIALOG.dismiss();
                });
            } else {
                Toast.makeText(activity, "send Link", Toast.LENGTH_SHORT).show();
                activity.recreate();
            }
        });

    }

    public EmailAuth(Activity activity) {
        this.activity = activity;
        dialogPopUp = new DialogsSnackBarPopMenu(activity);
        emailInitial();
    }

    @SuppressLint("SetTextI18n")
    public void emailSignUp() {
        String name, email, password, confPass;
        name = nameEt.getText().toString().trim();
        email = signUpEmailEt.getText().toString().trim().toLowerCase();
        password = signUpPassEt.getText().toString().trim();
        confPass = conPassEt.getText().toString().trim();
        if (!name.isEmpty() && !name.matches("[a-zA-Z]{3,}(?: [a-zA-Z]*){0,3}$")) {
            nameEt.requestFocus();
            nameEt.setError("Enter correct name Ex. Rahul Kumar");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpEmailEt.requestFocus();
            signUpEmailEt.setError("Enter correct formatted Email");
        } else if (!(password.length() > 5)) {
            signUpPassEt.requestFocus();
            signUpPassEt.setError("Password must be greater than 5 characters");
        } else if (!password.equals(confPass)) {
            conPassEt.requestFocus();
            conPassEt.setError("Should be same as Password");
        } else {
            dialogPopUp.LoadingDialog(null, R.drawable.loader_round_green_smile, 1, false,
                    "Creating User", "", "");
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String UUID = user.getUid();
                        DateTimeUtil dateTimeUtil = new DateTimeUtil();
                        dateTimeUtil.getCurrentDateTime();
                        AuthModelData authModelData = new AuthModelData(name, email, password,
                                dateTimeUtil.getCurrentDateTime(), DateTimeUtil.getTimeStamp());
                        FirebaseDatabase.getInstance().getReference()
                                .child("User Auth Data/" + UUID)
                                .setValue(authModelData)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        activity.startActivity(new Intent(activity, QrScanAct.class));
                                        activity.finish();
                                    } else {
                                        Toast.makeText(activity, "Something went wrong\nplease try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        dialogPopUp.DismissLoadingDialog();
                    }).addOnFailureListener(e -> {
                        signUpErrorTv.setVisibility(View.VISIBLE);
                        if (e instanceof FirebaseAuthWeakPasswordException) {
                            signUpErrorTv.setText(e.getMessage() + "");
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            signUpErrorTv.setText(e.getMessage() + "");
                        } else if (e instanceof FirebaseAuthUserCollisionException) {
                            signUpErrorTv.setText(e.getMessage() + "");
                        } else if (e instanceof FirebaseAuthEmailException) {
                            signUpErrorTv.setText(e.getMessage() + "");
                        } else if (e instanceof FirebaseAuthInvalidUserException) {
                            signUpErrorTv.setText(e.getMessage() + "");
                        } else {
                            Toast.makeText(activity, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                        }
                        dialogPopUp.DismissLoadingDialog();
                    });
        }
    }

    @SuppressLint("SetTextI18n")
    public void emailSignIn() {
        String email, password;
        email = signInEmailEt.getText().toString().trim().toLowerCase();
        password = signInPassEt.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signInEmailEt.requestFocus();
            signInEmailEt.setError("Enter correct formatted Email");
        } else if (!(password.length() > 5)) {
            signInPassEt.requestFocus();
            signInPassEt.setError("Password must ber greater than 5 char");
        } else {
            dialogPopUp.LoadingDialog(null, R.drawable.loader_round_green_smile, 1, false,
                    "Sign In", "", "");
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        activity.startActivity(new Intent(activity, QrScanAct.class));
                        Toast.makeText(activity, "Signed In", Toast.LENGTH_SHORT).show();
                        dialogPopUp.DismissLoadingDialog();
                    }).addOnFailureListener(e -> {
                        signInErrorTv.setVisibility(View.VISIBLE);
                        if (e instanceof FirebaseAuthInvalidUserException) {
                            signInErrorTv.setText(e.getMessage() + "");
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            signInErrorTv.setText(e.getMessage() + "");
                        } else if (e instanceof FirebaseNetworkException) {
                            signInErrorTv.setText(e.getMessage() + "");
                        } else {
                            Toast.makeText(activity, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                        }
                        dialogPopUp.DismissLoadingDialog();
                    });
        }
    }
}
