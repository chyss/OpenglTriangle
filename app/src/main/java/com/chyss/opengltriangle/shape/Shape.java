package com.chyss.opengltriangle.shape;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

/**
 * @author chyss 2017-05-05
 */

public abstract class Shape implements GLSurfaceView.Renderer
{
    public static int loadShader(int type, String shaderCode){
        //vertex shader类型(GLES20.GL_VERTEX_SHADER)或fragment shader类型(GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // 将源码添加到shader并编译它
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
