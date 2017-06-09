package com.chyss.opengltriangle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author chyss 2017-05-05
 */

public class OpenglSelectActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opengl_selector);

        findViewById(R.id.opengl_triangle).setOnClickListener(onClickListener);
        findViewById(R.id.opengl_cube).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.opengl_triangle:
                    stepNext(OpenglActivity.class,1);
                    break;
                case R.id.opengl_cube:
                    stepNext(OpenglActivity.class,2);
                    break;
            }
        }
    };

    private <T> void stepNext(Class<T> t,int type)
    {
        Intent intent = new Intent(this, t);
        intent.putExtra("type",type);
        startActivity(intent);
    }
}
