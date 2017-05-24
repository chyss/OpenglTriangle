package com.chyss.opengltriangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * @author chyss 2017-05-22
 */

public class BufferUtil
{

    public static FloatBuffer getFloatBuffer(float[] coords)
    {
        // 初始化顶点字节缓冲区，用于存放形状的坐标,每个浮点数占用4个字节
        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
        //设置使用设备硬件的原生字节序
        bb.order(ByteOrder.nativeOrder());

        //从ByteBuffer中创建一个浮点缓冲区
        FloatBuffer floatBuffer = bb.asFloatBuffer();
        // 把坐标都添加到FloatBuffer中
        floatBuffer.put(coords);
        //设置buffer从第一个坐标开始读
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static IntBuffer getIntBuffer(int[] coords)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer intBuffer = bb.asIntBuffer();
        intBuffer.put(coords);
        intBuffer.position(0);
        return intBuffer;
    }

    public static ShortBuffer getShortBuffer(short[] coords)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 2);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer shortBuffer = bb.asShortBuffer();
        shortBuffer.put(coords);
        shortBuffer.position(0);
        return shortBuffer;
    }
}
