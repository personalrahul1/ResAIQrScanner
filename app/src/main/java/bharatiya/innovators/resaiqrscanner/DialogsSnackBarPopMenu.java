package bharatiya.innovators.resaiqrscanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import pl.droidsonroids.gif.GifImageView;

public class DialogsSnackBarPopMenu {
    public static AlertDialog FINAL_LOADING_DIALOG, FINAL_BASIC_DIALOG, FINAL_EDIT_TEXT_DIALOG;
    public static PopupMenu POPUP_MENU;
    public static GifImageView LOADING_CANCEL_ICON, BASIC_END_ICON_ACTION, EDIT_TEXT_END_ICON_ACTION;

    public TextView BASIC_BUTTON_POSITIVE, BASIC_BUTTON_NEGATIVE,
            EDIT_TEXT_BUTTON_POSITIVE, EDIT_TEXT_BUTTON_NEGATIVE;

    public TextInputEditText EDIT_TEXT_MAIN_ET;
    public EditText EDIT_TEXT_SEC_ET;
    public String MAIN_IP_STRING, SEC_IP_STRING;
    private static Snackbar SNACKBAR;
    private final Context context;

    public DialogsSnackBarPopMenu(Context context) {
        this.context = context;
    }


    public void LoadingDialog(View view, int DIALOG_DRAWABLE, int CANCEL_TIME, boolean IS_CANCELLABLE,
                              String LOADING_MSG, String CANCEL_TRUE_MSG, String ON_DISMISS_MSG) {
        int time = CANCEL_TIME * 100;
        AlertDialog.Builder loadingDialog = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.info_loading_dialog, null);
        loadingDialog.setView(dialogView);

        TextView text = dialogView.findViewById(R.id.infoLoadingDialogTv);
        text.setMovementMethod(new ScrollingMovementMethod());
        GifImageView gifImv = dialogView.findViewById(R.id.infoLoadingDialogGifImv);
        LOADING_CANCEL_ICON = dialogView.findViewById(R.id.infoLoadingCancelIconGifImv);
        text.setText(LOADING_MSG);
        if (DIALOG_DRAWABLE > 0) {
            gifImv.setImageResource(DIALOG_DRAWABLE);
        }
        FINAL_LOADING_DIALOG = loadingDialog.create();
        FINAL_LOADING_DIALOG.setCancelable(IS_CANCELLABLE);
        FINAL_LOADING_DIALOG.requestWindowFeature(Window.FEATURE_NO_TITLE);
        FINAL_LOADING_DIALOG.setOnDismissListener(dialogInterface -> MakeToast(ON_DISMISS_MSG));
        LOADING_CANCEL_ICON.setOnClickListener(view1 -> DismissLoadingDialog());
        if (view == null) {
            FINAL_LOADING_DIALOG.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        if (IS_CANCELLABLE) {
            LOADING_CANCEL_ICON.setVisibility(View.VISIBLE);
        }
        if (!IS_CANCELLABLE && time > 200) {
            new Handler().postDelayed(() -> {
                text.setText(CANCEL_TRUE_MSG);
                FINAL_LOADING_DIALOG.setCancelable(true);
                LOADING_CANCEL_ICON.setVisibility(View.VISIBLE);
            }, time);
        }

        if (view != null) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            FINAL_LOADING_DIALOG.getWindow().setGravity(Gravity.TOP | Gravity.START);
            FINAL_LOADING_DIALOG.getWindow().setAttributes(getLayoutParams(location[0], location[1]));
            MyAnimations.showPopUpAnimation(dialogView, 800);
        }
        FINAL_LOADING_DIALOG.show();
    }

    private WindowManager.LayoutParams getLayoutParams(int x, int y) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.TOP | Gravity.START;
        layoutParams.x = x;
        layoutParams.y = y;
        return layoutParams;
    }

    private void MakeToast(String TOAST_MSG) {
        Toast.makeText(context, TOAST_MSG + "", Toast.LENGTH_SHORT).show();
    }

    public void DismissLoadingDialog() {
        if (FINAL_LOADING_DIALOG != null && FINAL_LOADING_DIALOG.isShowing()) {
            FINAL_LOADING_DIALOG.dismiss();
        }
    }

    @SuppressLint("MissingInflatedId")
    public void BasicDialog(boolean IS_CANCELLABLE, String TITTLE, String MSG1, String MSG2,
                            String BTN_POS, String BTN_NEG, String ON_DISMISS_MSG,
                            int ICON_DRAWABLE_START, int ICON_DRAWABLE_END, int IMAGE_DRAWABLE, String IMAGE_LINK) {
        AlertDialog.Builder basicDialog = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.basic_msg_dialog, null);
        basicDialog.setView(dialogView);

        TextView tittleTv, msg1Tv, msg2Tv;
        GifImageView startIconGifImv;
        ImageView imageView;
        View view;

        BASIC_BUTTON_POSITIVE = dialogView.findViewById(R.id.basicDialogPosTv);
        BASIC_BUTTON_NEGATIVE = dialogView.findViewById(R.id.basicDialogNegTv);
        tittleTv = dialogView.findViewById(R.id.basicDialogTittleTv);
        msg1Tv = dialogView.findViewById(R.id.basicDialogMsg1Tv);
        msg2Tv = dialogView.findViewById(R.id.basicDialogMsg2Tv);
        startIconGifImv = dialogView.findViewById(R.id.basicDialogStartGifImv);
        BASIC_END_ICON_ACTION = dialogView.findViewById(R.id.basicDialogEndGifImv);
        imageView = dialogView.findViewById(R.id.basicDialogImv);
        view = dialogView.findViewById(R.id.basicDialogViewBelowImv);

        FINAL_BASIC_DIALOG = basicDialog.create();
        FINAL_BASIC_DIALOG.setCancelable(IS_CANCELLABLE);
        FINAL_BASIC_DIALOG.requestWindowFeature(Window.FEATURE_NO_TITLE);
        FINAL_BASIC_DIALOG.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        FINAL_BASIC_DIALOG.setOnDismissListener(dialogInterface -> MakeToast(ON_DISMISS_MSG));

        tittleTv.setText(TITTLE);
        if (MSG1 != null) {
            msg1Tv.setText(MSG1);
            msg1Tv.setVisibility(View.VISIBLE);
        } else {
            msg1Tv.setVisibility(View.GONE);
        }
        if (MSG2 != null) {
            msg2Tv.setText(MSG2);
            msg2Tv.setVisibility(View.VISIBLE);
        } else {
            msg2Tv.setVisibility(View.GONE);
        }
        if (BTN_POS != null) {
            BASIC_BUTTON_POSITIVE.setText(BTN_POS);
            BASIC_BUTTON_POSITIVE.setVisibility(View.VISIBLE);
        } else {
            BASIC_BUTTON_POSITIVE.setVisibility(View.GONE);
        }
        if (BTN_NEG != null) {
            BASIC_BUTTON_NEGATIVE.setText(BTN_NEG);
            BASIC_BUTTON_NEGATIVE.setVisibility(View.VISIBLE);
        } else {
            BASIC_BUTTON_NEGATIVE.setVisibility(View.GONE);
        }
        if (ICON_DRAWABLE_START > 0) {
            startIconGifImv.setImageResource(ICON_DRAWABLE_START);
            startIconGifImv.setVisibility(View.VISIBLE);
        } else {
            startIconGifImv.setVisibility(View.GONE);
        }
        if (ICON_DRAWABLE_END > 0) {
            BASIC_END_ICON_ACTION.setImageResource(ICON_DRAWABLE_END);
            BASIC_END_ICON_ACTION.setVisibility(View.VISIBLE);
        } else if (IS_CANCELLABLE) {
            BASIC_END_ICON_ACTION.setVisibility(View.VISIBLE);
        } else {
            BASIC_END_ICON_ACTION.setVisibility(View.GONE);
        }
        if (IMAGE_DRAWABLE > 0 || IMAGE_LINK != null) {
            if (IMAGE_DRAWABLE > 0) {
                Glide.with(context)
                        .load(IMAGE_DRAWABLE)
                        .fitCenter()
                        .placeholder(R.drawable.picture_landscape_gallery)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(IMAGE_LINK)
                        .fitCenter()
                        .placeholder(R.drawable.picture_landscape_gallery)
                        .into(imageView);
            }
            imageView.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }

        FINAL_BASIC_DIALOG.setOnDismissListener(dialogInterface -> {
            MakeToast(ON_DISMISS_MSG);
            BASIC_END_ICON_ACTION.setVisibility(View.GONE);
            startIconGifImv.setVisibility(View.GONE);
            msg1Tv.setVisibility(View.GONE);
            msg2Tv.setVisibility(View.GONE);
            BASIC_BUTTON_POSITIVE.setVisibility(View.GONE);
            BASIC_BUTTON_NEGATIVE.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        });

        FINAL_BASIC_DIALOG.show();

        BASIC_END_ICON_ACTION.setOnClickListener(view1 -> {
            if (IS_CANCELLABLE) {
                FINAL_BASIC_DIALOG.dismiss();
            }
        });
    }

    public void ExitDialog(String TITLE, String MSG, String DISMISS_MSG) {
        BasicDialog(true, TITLE, MSG, "",
                "No", "Yes",
                "", R.drawable.exit_sign, 0, 0, null);
        if (DialogsSnackBarPopMenu.FINAL_BASIC_DIALOG != null &&
                DialogsSnackBarPopMenu.FINAL_BASIC_DIALOG.isShowing()) {
            BASIC_BUTTON_POSITIVE
                    .setOnClickListener(view -> DialogsSnackBarPopMenu.FINAL_BASIC_DIALOG.dismiss());
            BASIC_BUTTON_NEGATIVE
                    .setOnClickListener(view -> {
                        MakeToast(DISMISS_MSG);
                        Activity activity = (Activity) context;
                        activity.finish();
                    });
        }
    }
}
