package ru.com.testdribbble.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.androidannotations.annotations.EActivity;

import ru.com.testdribbble.R;
import ru.com.testdribbble.ui.BaseActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
