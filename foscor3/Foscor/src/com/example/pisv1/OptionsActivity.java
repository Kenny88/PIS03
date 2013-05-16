package com.example.pisv1;



import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

public class OptionsActivity extends Activity {
    private AudioManager audio;
	private SeekBar volume;
	private MediaPlayer mp;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main();
    }
        
        public void main(){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        	setContentView(R.layout.activity_options);
        	audio = (AudioManager) getSystemService(OptionsActivity.AUDIO_SERVICE);
        	volume= (SeekBar) findViewById( R.id.seekBar2);
        	volume.setProgress((audio.getStreamVolume(AudioManager.STREAM_MUSIC)*100)/audio.getStreamMaxVolume(audio.STREAM_MUSIC));
        	
        	volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
					 audio.setStreamVolume(AudioManager.STREAM_MUSIC, (audio.getStreamMaxVolume(audio.STREAM_MUSIC)*volume.getProgress())/100, 0);
				}
			});
        	
        }
       
}