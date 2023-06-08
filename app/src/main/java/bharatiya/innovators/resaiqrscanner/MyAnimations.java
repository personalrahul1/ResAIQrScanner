package bharatiya.innovators.resaiqrscanner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class MyAnimations {

    public void vibrateAnim(View view, Context context, int duration) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX",
                -30f, 30f, -20f, 20f, -10f, 10f, 0f);
        animator.setDuration(1000);

        animatorSet.playTogether(animator);
        animatorSet.start();
        vibrate(context, duration);
    }

    public void vibrateAnim(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX",
                -30f, 30f, -20f, 20f, -10f, 10f, 0f);
        animator.setDuration(1000);

        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public void slideDownToOriginal(View view, int duration) {
        // Get the height of the view
        int height = view.getHeight();
        view.setTranslationY(-height);
        view.animate()
                .translationY(0)
                .setDuration(duration)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    public static void showPopUpAnimation(View view, int duration) {
        AnimationSet animationSet = new AnimationSet(true);

        // Scale animation
        Animation scaleAnimation = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);

        // Alpha animation
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(duration);

        // Add animations to the animation set
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        // Start the animation
        view.startAnimation(animationSet);
        view.setVisibility(View.VISIBLE);
    }

    public static void slideDownExitAnimation(View view, int duration) {
        Animation animation = new TranslateAnimation(0, 0, 0, view.getHeight());
        animation.setDuration(duration);
        view.startAnimation(animation);
        view.setVisibility(View.INVISIBLE);
    }

    public static void smoothHideView(final View view) {
        view.animate()
                .alpha(0f)
                .translationY(view.getHeight())
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.INVISIBLE);
                        view.setAlpha(1f);
                        view.setTranslationY(0f);
                    }
                })
                .start();
    }

    public static void animateSlideUpExit(final View view, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, -view.getHeight());
        animator.setDuration(duration);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
                view.setTranslationY(0);
            }
        });
        animator.start();
    }

    public static void leftInSmoothZoomAndTrans(View view, int duration) {
        view.setAlpha(0f);
        view.setPivotX(0f);
        view.setPivotY(0f);
        view.setScaleX(0.5f);
        view.setScaleY(0.5f);
        view.setTranslationX(view.getWidth() / 2f);
        view.setTranslationY(view.getHeight() / 2f);
        view.setVisibility(View.VISIBLE);

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f);
        scaleXAnimator.setDuration(duration);
        scaleXAnimator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f);
        scaleYAnimator.setDuration(duration);
        scaleYAnimator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f);
        translateXAnimator.setDuration(duration);
        translateXAnimator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(view, "translationY", 0f);
        translateYAnimator.setDuration(duration);
        translateYAnimator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f);
        alphaAnimator.setDuration(duration);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());

        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, translateXAnimator, translateYAnimator, alphaAnimator);
        animatorSet.start();
    }

    public static void rightInSmoothZoomAndTrans(View view, int duration) {
        view.setAlpha(0f);
        view.setPivotX(0f);
        view.setPivotY(0f);
        view.setScaleX(0.5f);
        view.setScaleY(0.5f);
        view.setTranslationX(-view.getWidth() / 2f);
        view.setTranslationY(-view.getHeight() / 2f);
        view.setVisibility(View.VISIBLE);

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f);
        scaleXAnimator.setDuration(duration);
        scaleXAnimator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f);
        scaleYAnimator.setDuration(duration);
        scaleYAnimator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f);
        translateXAnimator.setDuration(duration);
        translateXAnimator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(view, "translationY", 0f);
        translateYAnimator.setDuration(duration);
        translateYAnimator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f);
        alphaAnimator.setDuration(duration);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());

        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, translateXAnimator, translateYAnimator, alphaAnimator);
        animatorSet.start();
    }

    public static void softSlideRightExit(View view, int duration) {
        Animation animation = new TranslateAnimation(0, view.getWidth(), 0, 0);
        animation.setDuration(duration);
        animation.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animation);
        view.setVisibility(View.INVISIBLE);
    }

    public static void softSlideLeftExit(View view, int duration) {
        Animation animation = new TranslateAnimation(0, -view.getWidth(), 0, 0);
        animation.setDuration(duration);
        animation.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animation);
        view.setVisibility(View.INVISIBLE);
    }

    public static void softlyHideView(final View view, int duration) {
        view.animate()
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(duration)
                .setInterpolator(new AccelerateInterpolator())
                .start();
        view.setVisibility(View.INVISIBLE);
    }

    public void vibrate(Context context, int duration) {
        // Get instance of Vibrator from the current Context
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Check if the device has a vibrator
        if (vibrator.hasVibrator()) {
            // Vibrate for 1000 milliseconds (1 second)
            vibrator.vibrate(duration);
        }
    }

}
