package com.example.pisv1;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public class TextoParpadeante {
	Context context;
	private TextView texto = null;
	private Animation fadeIn = null;
	private Animation fadeOut = null;
    // Listeners to detect the end of an animation
    private LocalFadeInAnimationListener myFadeInAnimationListener = new LocalFadeInAnimationListener();
    private LocalFadeOutAnimationListener myFadeOutAnimationListener = new LocalFadeOutAnimationListener();
    
    /**
     * Constructor
     * @param Context context
     * @param TextView text
     */
	public TextoParpadeante(Context context, TextView text){
		this.context = context;
		this.texto = (TextView)text;
	    runAnimations();
	}

    /**
     * Performs the actual fade-out
     */
    private void launchOutAnimation() {
	    texto.startAnimation(fadeOut);
    }    
    
    /**
     * Performs the actual fade-in
     */
    private void launchInAnimation() {
	    texto.startAnimation(fadeIn);
    }    

    /**
     * Starts the animation
     */
    private void runAnimations() {  
    	//uso de las animaciones
	    fadeIn = AnimationUtils.loadAnimation(this.context, R.anim.fadein);
	    fadeIn.setAnimationListener( myFadeInAnimationListener );
	    fadeOut = AnimationUtils.loadAnimation(this.context, R.anim.fadeout);
	    fadeOut.setAnimationListener( myFadeOutAnimationListener ); 
	    // And start
    	launchInAnimation();
    }
    
    // Runnables to start the actual animation
    private Runnable mLaunchFadeOutAnimation = new Runnable() {
	    public void run() {
	    	launchOutAnimation();
	    }
    };    
    
    private Runnable mLaunchFadeInAnimation = new Runnable() {
	    public void run() {
	    	launchInAnimation();
	    }
    };    

    /**
     * Animation listener for fade-out
     * 
     * @author moi
     *
     */
    private class LocalFadeInAnimationListener implements AnimationListener {
	    public void onAnimationEnd(Animation animation) {
		    texto.post(mLaunchFadeOutAnimation);
		}
	    public void onAnimationRepeat(Animation animation){
	    }
	    public void onAnimationStart(Animation animation) {
	    }
    };
    
    /**
     * Listener de animación para el Fadein
     */
    private class LocalFadeOutAnimationListener implements AnimationListener {
	    public void onAnimationEnd(Animation animation) {
		    texto.post(mLaunchFadeInAnimation);
		}	
	    public void onAnimationRepeat(Animation animation) {
	    }
	    public void onAnimationStart(Animation animation) {
	    }
    };
}
