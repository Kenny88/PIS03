package com.example.pisv1;



import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class GameOver extends Activity {
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main();
    }
        
        public void main(){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        	setContentView(R.layout.activity_game_over);
        	final View Start = findViewById(R.id.click_to_start);
        	
          
            
            Start.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
					 finish();
    					
    		}});
        }
}