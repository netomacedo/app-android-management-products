package com.example.projetofinalandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class MainActivity extends Activity {
	
	/*
	 * Classe SplashScreen
	 * 
	 * */
	
	// Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new Handler().postDelayed(new Runnable() {
			 
            /*
             * Mostrar splashScreen com tempo determinado de 3 segundos
             */
 
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, ListProdActivity.class);
                startActivity(i);
                
                //mata a activity
                finish();
            }
        }, SPLASH_TIME_OUT);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
