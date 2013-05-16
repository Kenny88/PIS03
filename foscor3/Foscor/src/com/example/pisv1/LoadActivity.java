package com.example.pisv1;



import java.io.File;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class LoadActivity extends Activity {
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main();
    }
        
		public void main(){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        	setContentView(R.layout.activity_load);
            final Button Save1 = (Button) findViewById(R.id.Save1Button);
    		final Button Save2 = (Button) findViewById(R.id.Save2Button);
    		final Button Save3 = (Button) findViewById(R.id.Save3Button);
    		File s1 = new File("s1.sav");
    		File s2 = new File("s2.sav");
    		File s3 = new File("s3.sav");
        	if(!(s1.exists())){
        		Save1.setEnabled(false);
        		}
        	if(!(s2.exists())){
        		Save2.setEnabled(false);
        		}
        	if(!(s3.exists())){
        		Save3.setEnabled(false);
        		}
    		Save1.setOnClickListener(new View.OnClickListener() {
     			@Override
     			public void onClick(View v) {
     				
     				 Intent intentNewGame = new Intent(LoadActivity.this , NewGameActivity.class);
     				 LoadActivity.this.startActivity(intentNewGame);
     				 Log.i("Content "," Main layout ");
     				
     		}});
    		
    		Save2.setOnClickListener(new View.OnClickListener() {
     			@Override
     			public void onClick(View v) {
     				
     				 Intent intentNewGame = new Intent(LoadActivity.this , NewGameActivity.class);
     				 LoadActivity.this.startActivity(intentNewGame);
     				 Log.i("Content "," Main layout ");
     				
     		}});
    		
    		Save3.setOnClickListener(new View.OnClickListener() {
     			@Override
     			public void onClick(View v) {
     				
     				 Intent intentNewGame = new Intent(LoadActivity.this , NewGameActivity.class);
     				 LoadActivity.this.startActivity(intentNewGame);
     				 Log.i("Content "," Main layout ");
     				
     		}});
        	
        }
}