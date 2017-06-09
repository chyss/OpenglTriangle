/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chyss.opengltriangle;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.chyss.opengltriangle.shape.Cube;
import com.chyss.opengltriangle.shape.Triangle;


public class OpenglSurfaceView extends GLSurfaceView {

    Renderer render;

    public OpenglSurfaceView(Context context,int type) {
        super(context);
        init(type);
    }

    private void init(int type)
    {
        // 使用OpenGLES 2.0
        setEGLContextClientVersion(2);

        // 设置Renderer到GLSurfaceView
        switch (type)
        {
            case 1:
                render = new Triangle();
                break;
            case 2:
                render = new Cube(this);
                break;
        }
        setRenderer(render);

        // render模式为只在绘制数据发生改变时才绘制view,此设置会阻止绘制GLSurfaceView的帧，直到你调用了requestRender().
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        //以下模式会不断绘制，我的手机上大概5--6 毫秒绘制一次
        //setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
