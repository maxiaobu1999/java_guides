package com.norman.guides;

import java.nio.ByteBuffer;

/**
 * ByteBuffer使用
 * 属性：
 * capacity：分配时确定的空间大小，分配好后大小不可变
 * limit：在读的模式下，表示缓存内数据的多少，limit<=capacity;在写的模式下，表示最多能存入多少数据，此时limit=capacity;
 * position：表示读写的位置，下标从0开始。
 * 方法：
 * flip()：读取buffer中的数据，进行读写模式的切换
 * clear():重新写入数据，进行读写模式的切换
 * rewind():重新读取，进行读写模式的切换
 * allocate(16)：分配内存大小
 *
 */
public class GuidesByteBuffer {

    public static void main(String[] args) {

    }


    public void writeIntoBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(16);//分配内存大小
        buffer.putShort((short) 18);//存入Short类型，一个Short两个字节
        buffer.put(Utils.stringToByte("normama"));
        buffer.put((byte) 0x00);//put一个byte
        buffer.put((byte) 0x04);
        byte[] re = (byte[]) buffer.flip().array();//将所有的byte返回，链式调用。
    }

}
