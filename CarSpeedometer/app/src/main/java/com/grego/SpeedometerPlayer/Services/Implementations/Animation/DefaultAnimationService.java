package com.grego.SpeedometerPlayer.Services.Implementations.Animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import com.grego.SpeedometerPlayer.Services.Definitions.IAnimationService;

/**
 * Provides and handles easy access to simple UI animations.
 */
public class DefaultAnimationService implements IAnimationService
{
    /**
     * Listener to start a fade animation on any animation end.
     */
    private class FadeAnimationListener extends AnimatorListenerAdapter
    {
        private View viewToAnimate;
        private boolean fadeIn;
        private int animationMilliseconds;

        public FadeAnimationListener(View viewToAnimate, boolean fadeIn, int animationMilliseconds)
        {
            this.viewToAnimate = viewToAnimate;
            this.fadeIn = fadeIn;
            this.animationMilliseconds = animationMilliseconds;
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            float alpha = 0;

            if (fadeIn)
            {
                alpha = 1;
            }

            viewToAnimate.animate()
                        .alpha(alpha)
                        .setDuration(animationMilliseconds)
                        .setListener(null);

            super.onAnimationEnd(animation);
        }
    }

    @Override
    public void FadeViewInOut(View view, int animationMilliseconds)
    {
        view.animate()
                .alpha(1f)
                .setDuration(animationMilliseconds)
                .setListener(new FadeAnimationListener(view,false, animationMilliseconds));
    }
}
