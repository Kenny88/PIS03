package com.example.pisv1;



import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class StartActivity extends Activity {
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main();
    }
        
        public void main(){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        	setContentView(R.layout.activity_start);
            final Button NuevaP = (Button) findViewById(R.id.button6);
    		final Button Continuar = (Button) findViewById(R.id.button2);
    		final Button Cargar = (Button) findViewById(R.id.button3);
    		final Button Opciones = (Button) findViewById(R.id.button4);
    		

            NuevaP.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				clickSound();
    				 Intent intentNewGame = new Intent(StartActivity.this , NewGameActivity.class);//NewGameActivity instead TMXTiledMapExample
    				 StartActivity.this.startActivity(intentNewGame);
    				 Log.i("Content "," Main layout ");
    				
    		}});

            
            Continuar.setOnClickListener(new View.OnClickListener() {
            	@Override
            	public void onClick(View v) {
            		clickSound();
            		 Intent intentContinue = new Intent(StartActivity.this , Game.class);
            		 StartActivity.this.startActivity(intentContinue);
            		 Log.i("Content "," Start layout ");
            		
            }});
            Cargar.setOnClickListener(new View.OnClickListener() {
            	@Override
            	public void onClick(View v) {
            		clickSound();
            		 Intent intentLoad = new Intent(StartActivity.this , LoadActivity.class);
            		 StartActivity.this.startActivity(intentLoad);
            		 Log.i("Content "," Start layout ");
            		
            }});
            Opciones.setOnClickListener(new View.OnClickListener() {
            	@Override
            	public void onClick(View v) {
            		clickSound();
            		 Intent intentOptions = new Intent(StartActivity.this , OptionsActivity.class);
            		 StartActivity.this.startActivity(intentOptions);
            		 Log.i("Content "," Start layout ");
            		
            }});
            
        }

public void clickSound(){
	MediaPlayer mp = MediaPlayer.create(StartActivity.this, R.raw.click);
    mp.setLooping(false);
    mp.start();
}
}