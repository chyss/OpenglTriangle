package com.chyss.opengltriangle.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import com.chyss.opengltriangle.R;
import com.chyss.opengltriangle.utils.BufferUtil;
import com.chyss.opengltriangle.utils.TextureUtil;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 正方体的绘制，增加透视投影和纹理贴图
 *
 * @author chyss 2017-06-09
 */
public class Cube extends Shape
{
    //顶点shader
    private final String vertexShaderCode =
            "attribute vec4 vPosition;"+            //顶点坐标
                    "uniform mat4 vMatrix;"+        //变换矩阵
                    "attribute vec2 aTextureCoords;"+   //纹理坐标，传入
                    "varying vec2 vTextureCoords;"+     //纹理坐标，传递到片断shader
                    "void main(){"+                         //每个shader中必须有一个main函数
                    "gl_Position = vMatrix*vPosition;"+     //坐标变换赋值
                    "vTextureCoords = aTextureCoords;"+     //传递纹理坐标
                    "}";

    //片断shader
    private final String fragmentShaderCode =
            "precision mediump float;"+                     //精度设置为mediump
                    "uniform sampler2D uTextureUnit;"+      //纹理单元
                    "varying vec2 vTextureCoords;"+         //纹理坐标，由顶点shader传递过来
                    "void main(){"+                             //每个shader中必须有一个main函数
                    "gl_FragColor = texture2D(uTextureUnit, vTextureCoords);"+ //赋值2D纹理
                    "}";

    private float[] projectMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] mVMatrix = new float[16];

    private int mMatrixHandle;
    private int mPositionHandle;
    private int mTextureHandle;
    private int mTextureCoordHandle;

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureCoordBuffer;
    private int program;
    private int texture;

    private View view;

    private int vertexShader,fragmentShader;

    private float s = 0.8f;
    //正方体顶点坐标
    private float[] coords = {
            -s,s,-s,s,s,s,s,s,-s,       //023
            -s,s,-s,-s,s,s,s,s,s,       //012
            -s,s,s,-s,-s,s,s,s,s,       //152
            s,s,s,-s,-s,s,s,-s,s,       //256
            s,s,s,s,-s,s,s,-s,-s,       //267
            s,s,s,s,-s,-s,s,s,-s,       //273
            s,s,-s,s,-s,-s,-s,-s,-s,    //374
            s,s,-s,-s,-s,-s,-s,s,-s,    //340
            -s,s,-s,-s,-s,-s,-s,s,s,    //041
            -s,s,s,-s,-s,-s,-s,-s,s,    //145
            -s,-s,s,-s,-s,-s,s,-s,s,    //546
            s,-s,s,-s,-s,-s,s,-s,-s     //647

//            -s,s,-s,    // 0
//            -s,s,s,     // 1
//            s,s,s,      // 2
//            s,s,-s,     // 3
//            -s,-s,-s,   // 4
//            -s,-s,s,    // 5
//            s,-s,s,     // 6
//            s,-s,-s,    // 7
    };

    //纹理坐标
    private float[] textureCoord = {

            0,1,1,0,1,1,
            0,1,0,0,1,0,
            0,1,0,0,1,1,
            1,1,0,0,1,0,
            0,1,0,0,1,0,
            0,1,1,0,1,1,
            0,1,0,0,1,0,
            0,1,1,0,1,1,
            0,1,0,0,1,1,
            1,1,0,0,1,0,
            0,1,0,0,1,1,
            1,1,0,0,1,0
    };

    public Cube(View view)
    {
        this.view = view;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        GLES20.glClearColor(1f,1f,1f,1.0f);
        //打开深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        //逆时针为正面
        GLES20.glFrontFace(GLES20.GL_CCW);
        //打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        //背面剪裁
        GLES20.glCullFace(GLES20.GL_BACK);

        // 初始化缓冲数据
        vertexBuffer = BufferUtil.getFloatBuffer(coords);
        textureCoordBuffer = BufferUtil.getFloatBuffer(textureCoord);

        // 编译shader代码
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);

        //加载纹理贴图
        texture = TextureUtil.loadTexture(view.getContext(), R.mipmap.ic_launcher);

        // 创建Program
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program,vertexShader);
        GLES20.glAttachShader(program,fragmentShader);
        GLES20.glLinkProgram(program);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        //设置视窗大小及位置
        GLES20.glViewport(0,0,width,height);

        float ratio = (float)width/height;
        //设置透视投影
        Matrix.frustumM(projectMatrix,0,-ratio,ratio,-1,1,3,30);
        //设置相机位置setLookAtM(float[] rm, int rmOffset,
        // float eyeX, float eyeY, float eyeZ, 摄像机的位置
        //float centerX, float centerY, float centerZ, 物体的位置
        // float upX, float upY,float upZ)  相机上方向量
        Matrix.setLookAtM(viewMatrix,0,5f,5f,10f,0f,0f,0f,0f,0f,1f);
        //计算变换矩阵
        Matrix.multiplyMM(mVMatrix,0,projectMatrix,0,viewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        //清除深度缓冲与颜色缓冲
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT|GLES20.GL_COLOR_BUFFER_BIT);

        // 添加program到OpenGL ES环境中
        GLES20.glUseProgram(program);

        //矩阵变换
        mMatrixHandle = GLES20.glGetUniformLocation(program,"vMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandle,1,false,mVMatrix,0);

        //设置纹理
        mTextureHandle = GLES20.glGetUniformLocation(program,"uTextureUnit");
        //激活纹理单元，GL_TEXTURE0代表纹理单元0，GL_TEXTURE1代表纹理单元1，以此类推。OpenGL使用纹理单元来表示被绘制的纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //绑定纹理到这个纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        //把选定的纹理单元传给片段着色器中的u_TextureUnit，
        GLES20.glUniform1i(mTextureHandle, 0);

        //顶点坐标
        mPositionHandle = GLES20.glGetAttribLocation(program,"vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,0,vertexBuffer);

        //纹理坐标
        mTextureCoordHandle = GLES20.glGetAttribLocation(program,"aTextureCoords");
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glVertexAttribPointer(mTextureCoordHandle,2,GLES20.GL_FLOAT,false,0,textureCoordBuffer);

        //绘制图形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,coords.length/3);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
