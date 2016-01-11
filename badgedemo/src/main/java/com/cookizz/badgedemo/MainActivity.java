package com.cookizz.badgedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cookizz.badgelib.mutable.DotBadge;
import com.cookizz.badgelib.style.DotStyleNormal;
import com.cookizz.badgelib.mutable.FigureBadge;
import com.cookizz.badgelib.style.FigureStyleNormal;
import com.cookizz.badgelib.core.BadgeManager;

public class MainActivity extends Activity implements View.OnClickListener {

    private DotBadge dot;
    private FigureBadge figure;
    private BadgeManager manager;
    private String tag = getClass().getSimpleName();

    private View txt1;
    private View txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        manager = (BadgeManager) findViewById(R.id.badge_layout);

        txt1 = findViewById(R.id.text_1);
        txt2 = findViewById(R.id.text_2);
        txt1.setOnClickListener(this);
        txt2.setOnClickListener(this);
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
                manager.createDotBadge(txt1, DotStyleNormal.class).show();
                break;
            case R.id.text_2:
                FigureBadge figureBadge = manager.createFigureBadge(txt2, FigureStyleNormal.class);
                figureBadge.setFigure((int) (Math.random() * 150));
                figureBadge.show();
                break;
        }
    }
}
