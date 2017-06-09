package com.chyss.opengltriangle.shape;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.chyss.opengltriangle.utils.BufferUtil;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author chyss 2017-05-23
 */
public class Triangle extends Shape
{
    //gl_Position : 输出属性-变换后的顶点的位置，用于后面的固定的裁剪等操作。所有的顶点着色器都必须写这个值。
    private static final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    //gl_FragColor : 输出的颜色用于随后的像素操作
    private static final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    //定点的缓存
    private FloatBuffer vertexBuffer;
    //OpenGL ES Program
    private int program;
    //设置三角形顶点数组
    private float coords[] = {   // 默认按逆时针方向绘制
            0.0f,  0.5f, 0.0f,
            -0.5f, 0.0f, 0.0f,
            0.5f, 0.0f, 0.0f
    };

    // 设置三角形颜色和透明度（r,g,b,a），蓝色不透明
    private float color[] = {0.0f, 0.0f, 1.0f, 1.0f};

    private int positionHandle;
    private int colorHandle;

    /**
     * 仅调用一次，用于设置view的OpenGLES环境
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        //设置清屏背景色RGBA,白色不透明
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        // 初始化顶点字节缓冲区
        vertexBuffer = BufferUtil.getFloatBuffer(coords);

        // 编译shader代码
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);

        // 创建空的OpenGL ES Program
        program = GLES20.glCreateProgram();

        // 将vertex shader添加到program
        GLES20.glAttachShader(program, vertexShader);

        // 将fragment shader添加到program
        GLES20.glAttachShader(program, fragmentShader);

        // 创建可执行的 OpenGL ES program
        GLES20.glLinkProgram(program);
    }

    /**
     * 如果view的几何形状发生变化就调用，例如当竖屏变为横屏时
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        //设置视窗大小及位置
        GLES20.glViewport(0, 0, width, height);
    }

    /**
     * 每次View被重绘时被调用
     */
    @Override
    public void onDrawFrame(GL10 gl)
    {
        //清除深度缓冲与颜色缓冲
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);

        // 添加program到OpenGL ES环境中
        GLES20.glUseProgram(program);

        // 获取指向vertex shader的成员vPosition的handle
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");

        // 启用一个指向三角形的顶点数组的handle
        GLES20.glEnableVertexAttribArray(positionHandle);

        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(positionHandle,  //变量的内存位置
                3,                      //每一个坐标的数据个数
                GLES20.GL_FLOAT,        //数据的类型
                false,                  //converted to a range from -1 to 1 for signed data, or 0 to 1 for unsigned data
                3 * 4,                  //the spacing between elements,每个顶点占用的空间
                vertexBuffer);         //the array containing the data,顶点缓冲数组

        // 获取指向fragment shader的成员vColor的handle
        colorHandle = GLES20.glGetUniformLocation(program, "vColor");

        // 上传缓冲
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // glDrawArrays 使用VetexBuffer 来绘制，顶点的顺序由vertexBuffer中的顺序指定。
        // GLES20.GL_TRIANGLES 绘制的类型为3角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, coords.length / 3);

        //glDrawElements 可以重新定义顶点的顺序，顶点的顺序由indices Buffer 指定
        //GLES20.glDrawElements(GLES20.GL_TRIANGLES,indices.length,GLES20.GL_UNSIGNED_INT,indexBuffer);

        // 禁用指向三角形的顶点数组
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
