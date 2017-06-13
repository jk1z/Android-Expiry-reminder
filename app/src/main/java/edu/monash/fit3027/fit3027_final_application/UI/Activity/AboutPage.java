package edu.monash.fit3027.fit3027_final_application.UI.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.monash.fit3027.fit3027_final_application.R;

/**
 * Created by YunHao Zhang
 * Student ID: 26956047
 * Activity controller for about_page
 */

public class AboutPage extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        //A really simple about page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_page);

        setTitle("About Page");



    }
}
