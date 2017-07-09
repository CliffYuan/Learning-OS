package com.xnd.os.floppy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xnd on 17-7-9.
 */
public class OS {

    private Floppy floppy=new Floppy();


    public void writeKernelBoot(String fileName){
        File file = new File(fileName);
        InputStream in = null;

        try {
            in = new FileInputStream(file);
            byte[] buf = new byte[512];
            buf[510] = 0x55;
            buf[511] = (byte) 0xaa;
            if (in.read(buf) != -1) {
                //将内核读入到磁盘第0面，第0柱面，第1个扇区
                floppy.writeFloppy(Floppy.MAGNETIC_HEAD.MAGNETIC_HEAD_0, 0, 1, buf);
            }
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }finally {
            if(in!=null){
                try {
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeBoot(String fileName){
        File file = new File(fileName);
        InputStream in = null;

        try {
            in = new FileInputStream(file);
            byte[] buf = new byte[512];
            buf[510] = 0x55;
            buf[511] = (byte) 0xaa;
            if (in.read(buf) != -1) {
                //将内核读入到磁盘第0面，第0柱面，第1个扇区
                floppy.writeFloppy(Floppy.MAGNETIC_HEAD.MAGNETIC_HEAD_0, 0, 1, buf);
            }
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }finally {
            if(in!=null){
                try {
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeKernel(String fileName,int c,int sector){
        File file = new File(fileName);
        InputStream in = null;

        try {
            in = new FileInputStream(file);
            byte[] buf = new byte[512];
            if (in.read(buf) != -1) {
                //将内核读入到磁盘第0面，第0柱面，第sector个扇区
                floppy.writeFloppy(Floppy.MAGNETIC_HEAD.MAGNETIC_HEAD_0, c, sector, buf);
            }
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }finally {
            if(in!=null){
                try {
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void makeFloppy(String osName){
        floppy.makeFloppy(osName);
    }

    public static void main(String[] args) {
        //第一版本 在同一个扇区
        OS os=new OS();
        os.writeKernelBoot("/home/xnd/workspace-os/Learning-OS/version-1/boot.bat");
        os.makeFloppy("/home/xnd/workspace-os/Learning-OS/version-1/os.img");


        //第二版本 内核加载器在第一个扇区，显示的内容在第二个扇区
        os=new OS();
        os.writeBoot("/home/xnd/workspace-os/Learning-OS/version-2/boot.bat");
        os.writeKernel("/home/xnd/workspace-os/Learning-OS/version-2/kernel.bat",1,2);
        os.makeFloppy("/home/xnd/workspace-os/Learning-OS/version-2/os-2.img");

    }
}
