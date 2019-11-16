package com.norman.guides;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * https://www.jianshu.com/p/2011bcd018a7
 * ByteBuffer使用
 * 方法：
 * flip()：读取buffer中的数据，进行读写模式的切换
 * clear():重新写入数据，进行读写模式的切换
 * rewind():重新读取，进行读写模式的切换
 */
public class TestByteBuffer {
    public static String TAG = "TestByteBuffer+++:";

    /**
     * 实例化
     */
    @Test
    public void testInit() {
        // allocate
        // 从堆空间中分配一个容量大小为capacity的byte数组作为缓冲区的byte数据存储器
        System.out.println("----------Test allocate--------");
        System.out.println(TAG + "before alocate:" + Runtime.getRuntime().freeMemory());
        ByteBuffer bufferAllocate = ByteBuffer.allocate(10240000);//如果分配的内存过小，调用Runtime.getRuntime().freeMemory()大小不会变化
        System.out.println(TAG + "after alocate:" + Runtime.getRuntime().freeMemory());

        // allocateDirect
        // 是不使用JVM堆栈而是通过操作系统来创建内存块用作缓冲区，它与当前操作系统能够更好的耦合，因此能进一步提高I/O操作速度。
        // 但是分配直接缓冲区的系统开销很大，因此只有在缓冲区较大并长期存在，或者需要经常重用时，才使用这种缓冲区
        System.out.println("----------Test allocateDirect--------");
        System.out.println(TAG + "before allocateDirect:" + Runtime.getRuntime().freeMemory());
        ByteBuffer bufferAllocateDirect = ByteBuffer.allocateDirect(10240000);
        System.out.println(TAG + "after allocateDirect:" + Runtime.getRuntime().freeMemory());
        // 这个缓冲区的数据会存放在byte数组中，bytes数组或buff缓冲区任何一方中数据的改动都会影响另一方。
        // 其实ByteBuffer底层本来就有一个bytes数组负责来保存buffer缓冲区中的数据，通过allocate方法系统会帮你构造一个byte数组
        // 这部分直接用的系统内存，所以对JVM的内存没有影响
        System.out.println("----------Test wrap--------");
        System.out.println(TAG + "before bufferWrap:" + Runtime.getRuntime().freeMemory());
        byte[] bytes = new byte[10240000];
        ByteBuffer bufferWrap = ByteBuffer.wrap(bytes);
        System.out.println(TAG + "after bufferWrap:" + Runtime.getRuntime().freeMemory());

        System.out.println("----------Test wrap with offset --------");
        // 在上一个方法的基础上可以指定偏移量和长度，这个offset也就是包装后byteBuffer的position，
        // 而length呢就是limit-position的大小，从而我们可以得到limit的位置为length+position(offset)
        ByteBuffer bufferOffset = ByteBuffer.wrap(bytes, 10, 10);
        System.out.println(bufferOffset);
    }


    /**
     * ByteBuffer属性
     * capacity：分配时确定的空间大小，分配好后大小不可变
     * limit：在读的模式下，表示缓存内数据的多少，limit<=capacity;在写的模式下，表示最多能存入多少数据，此时limit=capacity;
     * position：表示读写的位置，下标从0开始。
     */
    @Test
    public void testProperty() {
        ByteBuffer buffer = ByteBuffer.allocate(16);//分配内存大小
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);

        // 分配时确定的空间大小，分配好后大小不可变
        int capacity = buffer.capacity();
        System.out.println(TAG + "capacity=" + capacity);
        // 表示读写的位置，下标从0开始。
        int position = buffer.position();
        System.out.println(TAG + "position=" + position);
        // 在读的模式下，表示缓存内数据的多少，limit<=capacity;在写的模式下，表示最多能存入多少数据，此时limit=capacity;
        int limit = buffer.limit();
        System.out.println(TAG + "limit=" + limit);
    }

    /**
     * Mark：标记，调用mark()来设置mark=position，再调用reset()可以让position恢复到标记的位置
     * reset():	把position设置成mark的值，相当于之前做过一个标记，现在要退回到之前标记的地方
     */
    @Test
    public void testReset() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        System.out.println(TAG + "buffer=" + buffer);
        buffer.position(5);
        buffer.mark();
        System.out.println(TAG + " mark()后 buffer=" + buffer);
        buffer.position(10);
        System.out.println(TAG +"before reset: buffer=" + buffer);
        buffer.reset();
        System.out.println(TAG +"after reset: buffer="+ buffer);
    }

    /**
     * 把position设为0，mark设为-1，不改变limit的值
     */
    @Test
    public void testRewind(){
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.position(10);
        buffer.limit(15);
        System.out.println(TAG +"before rewind: buffer=" + buffer);
        buffer.rewind();
        System.out.println(TAG +"after rewind: buffer="+ buffer);
    }
    /**
     * 把从position到limit中的内容移到0到limit-position的区域内，
     * position和limit的取值也分别变成limit-position、capacity。
     * 如果先将positon设置到limit，再compact，那么相当于clear()
     */
    @Test
    public void testCompact() throws UnsupportedEncodingException {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.put("abcd".getBytes(StandardCharsets.UTF_8));
        System.out.println(TAG +"before compact:" + buffer);
        System.out.println(new String(buffer.array()));
        buffer.flip();
        System.out.println(TAG +"after flip:" + buffer);
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println(TAG +"after three gets:" + buffer);
        System.out.println(TAG +"\t" + new String(buffer.array()));
        buffer.compact();
        System.out.println(TAG +"after compact:" + buffer);
        System.out.println(TAG +"\t" + new String(buffer.array()));
    }

    /**
     * 把相对读，从position位置读取一个byte，并将position+1，为下次读写作准备
     */
    @Test
    public void testGet() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.put((byte) 'a').put((byte) 'b').put((byte) 'c').put((byte) 'd')
                .put((byte) 'e').put((byte) 'f');
        System.out.println(TAG +"before flip()" + buffer);
        // 转换为读取模式
        buffer.flip();
        System.out.println(TAG +"before get():" + buffer);
        System.out.println((char) buffer.get());
        System.out.println(TAG +"after get():" + buffer);
        // get(index)不影响position的值
        System.out.println((char) buffer.get(2));
        System.out.println(TAG +"after get(index):" + buffer);
        byte[] dst = new byte[10];
        buffer.get(dst, 0, 2);
        System.out.println(TAG +"after get(dst, 0, 2):" + buffer);
        System.out.println(TAG +"\t dst:" + new String(dst));
        System.out.println(TAG +"buffer now is:" + buffer);
        System.out.println(TAG +"\t" + new String(buffer.array()));
    }

    /**
     * 相对写，把src中可读的部分（也就是position到limit）写入此byteBuffer
     */
    @Test
    public void testPut() {
        ByteBuffer bb = ByteBuffer.allocate(32);
        System.out.println(TAG +"before put(byte):" + bb);
        System.out.println(TAG +"after put(byte):" + bb.put((byte) 'z'));
        System.out.println(TAG +"\t" + bb.put(2, (byte) 'c'));
        // put(2,(byte) 'c')不改变position的位置
        System.out.println(TAG +"after put(2,(byte) 'c'):" + bb);
        System.out.println(TAG +"\t" + new String(bb.array()));
        // 这里的buffer是 abcdef[pos=3 lim=6 cap=32]
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.put((byte) 'a').put((byte) 'b').put((byte) 'c').put((byte) 'd')
                .put((byte) 'e').put((byte) 'f');
        buffer.get();
        buffer.get();
        buffer.get();
        bb.put(buffer);
        System.out.println(TAG +"after put(buffer):" + bb);
        System.out.println(TAG +"\t" + new String(bb.array()));
    }

}
