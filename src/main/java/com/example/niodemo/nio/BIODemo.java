package com.example.niodemo.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BIODemo {

    public static void test_BIO_read() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("X:\\nio.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] b = new byte[1024];
        int pos = 0;
        try {
            fis.read(b);
            System.out.print((char)b[0]);
            System.out.print((char)b[1]);
            System.out.print((char)b[2]);
            System.out.print((char)b[3]);
            System.out.print((char)b[4]);
            System.out.print((char)b[1020]);
            System.out.print((char)b[1021]);
            System.out.print((char)b[1022]);
            System.out.print((char)b[1023]);
            fis.read(b);
            System.out.print((char)b[0]);
            System.out.print((char)b[1]);
            System.out.print((char)b[2]);
            System.out.print((char)b[3]);
            System.out.print((char)b[4]);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis!=null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void test_BIO_write() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("X:\\nio.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            File file = new File("X:\\nio_out.txt");
            if(!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = new byte[1024];
        int pos = 0;
        try {
            while((pos=fis.read(b))!=-1) {
                fos.write(b,0, pos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis!=null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void test_NIO_buffer() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println("begin=============================");
        System.out.println(buffer.capacity());
        System.out.println(buffer.limit());
        System.out.println(buffer.position());
//        System.out.println(buffer.mark());
        buffer.put("hello nio".getBytes());
        System.out.println("after put=============================");
        System.out.println(buffer.capacity());
        System.out.println(buffer.limit());
        System.out.println(buffer.position());
//        System.out.println(buffer.mark());
        buffer.flip();
        System.out.println("after flip=============================");
        System.out.println(buffer.capacity());
        System.out.println(buffer.limit());
        System.out.println(buffer.position());
//        System.out.println(buffer.mark());
//        buffer.clear();
        buffer.compact();
        System.out.println("after clear/compact=============================");
        System.out.println(buffer.capacity());
        System.out.println(buffer.limit());
        System.out.println(buffer.position());
    }

    public static void test_NIO_channel() {
        //
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("X:\\nio.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileChannel channel1 = fis.getChannel();
        //
        FileChannel channel2 = null;
        try {
            channel2 = FileChannel.open(Paths.get("X:\\nio.txt"), StandardOpenOption.READ);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void test_NIO_write() {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = FileChannel.open(Paths.get("X:\\nio.txt"), StandardOpenOption.READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out = FileChannel.open(Paths.get("X:\\nio_out.txt"),
                    StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            while(in.read(buffer)!=-1) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    使用直接缓冲区有两种方式：
//    1.缓冲区创建的时候分配的是直接缓冲区
//    2.在FileChannel上调用map()方法，将文件直接映射到内存中创建
    public static void test_NIO_DirectBuffer() {
        FileChannel in = null;
        try {
            in = FileChannel.open(Paths.get("X:\\nio.txt"), StandardOpenOption.READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileChannel out = null;
        try {
            out = FileChannel.open(Paths.get("X:\\nio_out.txt"),
                    StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        System.out.println(buffer.isDirect());
        try {
            while(in.read(buffer)!=-1) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void test_NIO_map() {
        FileChannel in = null;
        try {
            in = FileChannel.open(Paths.get("X:\\nio.txt"), StandardOpenOption.READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileChannel out = null;
        try {
            out = FileChannel.open(Paths.get("X:\\nio_out.txt"),
                    StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MappedByteBuffer inBuf = null;
        try {
            inBuf = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());
            System.out.println(inBuf.isDirect());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MappedByteBuffer outBuf = null;
        try {
            outBuf = out.map(FileChannel.MapMode.READ_WRITE, 0, in.size());
            System.out.println(inBuf.isDirect());
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] temp = new byte[inBuf.limit()];
        inBuf.get(temp);
        outBuf.put(temp);
    }

    public static void main(String[] args) {
        test_NIO_map();
    }

}
