package com.example.pisv1;



import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main();
        TextView tv = (TextView) findViewById(R.id.StartText);
        new TextoParpadeante(getBaseContext(),tv);
    }
        
        public void main(){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        	setContentView(R.layout.activity_main);
        	final View Start = (View) findViewById(R.id.click_to_start);
        	
          
            
            Start.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				 MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.click);
				        mp.setLooping(false);
				        mp.start();
    				 Intent intentStart = new Intent(MainActivity.this , StartActivity.class);
    				 MainActivity.this.startActivity(intentStart);
    				 Log.i("Content "," Main layout ");
    					
    		}});
            

            
        }
}
