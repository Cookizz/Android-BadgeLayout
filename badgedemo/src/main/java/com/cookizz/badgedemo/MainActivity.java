package com.cookizz.badgedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cookizz.badgelib.DotBadge;
import com.cookizz.badgelib.DotStyleNormal;
import com.cookizz.badgelib.FigureBadge;
import com.cookizz.badgelib.FigureStyleNormal;
import com.cookizz.badgelib.core.BadgeManager;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final BadgeManager manager = (BadgeManager) findViewById(R.id.badge_layout);
        final FigureBadge badge = manager.createFigureBadge(R.id.badge_target, FigureStyleNormal.class);
        badge.show();
        badge.setFigure(45);

        final View target = findViewById(R.id.badge_target);
        target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                badge.setFigure((int) (Math.random() * 150));
            }
        });
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
}
