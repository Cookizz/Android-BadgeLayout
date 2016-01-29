package com.cookizz.badgedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cookizz.badge.style.DotStyleNormal;
import com.cookizz.badge.style.FigureStyleNormal;
import com.cookizz.badge.core.BadgeManager;

public class MainActivity extends Activity implements View.OnClickListener {

    private BadgeManager manager;

    private View txt1;
    private View txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        txt1 = findViewById(R.id.text_1);
        txt2 = findViewById(R.id.text_2);
        txt1.setOnClickListener(this);
        txt2.setOnClickListener(this);

        manager = (BadgeManager) findViewById(R.id.badge_layout);
        manager.createDotBadge(R.id.text_1, DotStyleNormal.class).show();
        manager.createFigureBadge(R.id.text_2, FigureStyleNormal.class)
                .setFigure((int) (Math.random() * 150))
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_1:
                manager.findBadge(R.id.text_1).show();
                break;
            case R.id.text_2:
                manager.findBadge(R.id.text_2)
                        .setFigure((int) (Math.random() * 150))
                        .show();
                break;
        }
    }
}
