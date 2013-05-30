package com.example.pisv1;



import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.media.MediaPlayer;

public class NewGameActivity extends Activity {
    private MediaPlayer mp;
    private  Animation intro;
    boolean can=true;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main();
        mp = MediaPlayer.create(NewGameActivity.this, R.raw.intro);
        mp.setLooping(false);
        mp.start();
        TextView tv = (TextView) findViewById(R.id.NewText);
        intro= AnimationUtils.loadAnimation(this, R.anim.animationfile);
        tv.startAnimation(intro);
        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask, 45000);        
    }
class MyTimerTask extends TimerTask {
	  public void run() {
		  if(can){
			  	can=false;
			 	finish();
				Intent intentNewGame = new Intent(NewGameActivity.this , Game.class);//NewGameActivity instead TMXTiledMapExample
		       	NewGameActivity.this.startActivity(intentNewGame);
		       	Log.i("Content "," Main layout ");	
		  }
	}
 
    }
        
        public void main(){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        	setContentView(R.layout.activity_new);
        	
final View Start = findViewById(R.id.click_new);
        	

            Start.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				
    		   		 if(intro.hasEnded()){
    					  	can=false;
    		   			 	finish();
     		   	        	Intent intentNewGame = new Intent(NewGameActivity.this , Game.class);//NewGameActivity instead TMXTiledMapExample
         		        	NewGameActivity.this.startActivity(intentNewGame);
         		        	Log.i("Content "," Main layout ");	
    		          }
    					
    		}});

        }
        
        @Override
        protected void onStop()
    {
        // Stop play
        super.onStop();
        mp.stop();
    }

}