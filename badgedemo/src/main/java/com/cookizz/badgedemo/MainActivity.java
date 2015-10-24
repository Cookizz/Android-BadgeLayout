package com.cookizz.badgedemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cookizz.badgelib.DotBadge;
import com.cookizz.badgelib.DotStyleNormal;
import com.cookizz.badgelib.FigureBadge;
import com.cookizz.badgelib.FigureStyleNormal;
import com.cookizz.badgelib.core.BadgeManager;

public class MainActivity extends Activity {

    private DotBadge dot;
    private FigureBadge figure;
    private BadgeManager manager;
    private String tag = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        manager = (BadgeManager) findViewById(R.id.badge_layout);

        dot = manager.createDotBadge(R.id.text_1, DotStyleNormal.class);
        dot.show();

        final View target = findViewById(R.id.text_1);
        target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Math.random() > 0.5) {
                    showDot();
                }
                else {
                    showFigure((int) (Math.random() * 100));
                }
            }
        });
    }

    private void showDot() {
        if(dot == null || !dot.isAttached()) {
            dot = manager.createDotBadge(R.id.text_1, DotStyleNormal.class);
        }
        dot.show();
    }

    private void showFigure(int f) {
        if(figure == null || !figure.isAttached()) {
            figure = manager.createFigureBadge(R.id.text_1, FigureStyleNormal.class);
        }
        figure.setFigure(f);
        figure.show();
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
