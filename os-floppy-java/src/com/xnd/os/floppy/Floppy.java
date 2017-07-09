package com.xnd.os.floppy;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xnd on 17-7-9.
 */
public class Floppy {

    enum MAGNETIC_HEAD {
        MAGNETIC_HEAD_0,
        MAGNETIC_HEAD_1
    };

    /**
     * 每个扇区512字节大小
     */
    public int SECTOR_SIZE = 512;
    /**
     * 一个盘面80个柱面/磁道
     */
    private int CYLINDER_COUNT = 80;
    /**
     * 一个磁道18个扇区
     */
    private int SECTORS_COUNT = 18;
    /**
     * 默认盘面
     */
    private MAGNETIC_HEAD magneticHead = MAGNETIC_HEAD.MAGNETIC_HEAD_0;

    private int current_cylinder = 0;

    private int current_sector = 0;

    private HashMap<Integer,ArrayList<ArrayList<byte[]>>> floppy=new HashMap<Integer, ArrayList<ArrayList<byte[]>>>();

    public Floppy() {
        initFloppy();
    }

    private void initFloppy(){
        floppy.put(MAGNETIC_HEAD.MAGNETIC_HEAD_0.ordinal(),initFloppyDisk());
        floppy.put(MAGNETIC_HEAD.MAGNETIC_HEAD_1.ordinal(),initFloppyDisk());
    }

    private ArrayList<ArrayList<byte[]>> initFloppyDisk(){
        ArrayList<ArrayList<byte[]>> floppyDisk=new ArrayList<ArrayList<byte[]>>();
        for (int i=0;i<CYLINDER_COUNT;i++){
            floppyDisk.add(initFloppyCylinder());
        }
        return floppyDisk;
    }

    private ArrayList<byte[]> initFloppyCylinder(){
        ArrayList<byte[]> floppyCylinder=new ArrayList<byte[]>();
        for (int i=0;i<SECTORS_COUNT;i++){
            byte[] sector=new byte[SECTOR_SIZE];
            floppyCylinder.add(sector);
        }
        return floppyCylinder;
    }

    /**
     * 将软盘结构数据写入到文件
     * @param head
     * @param cylinder_num
     * @param sector_num
     * @return
     */
    public byte[] readFloppy(MAGNETIC_HEAD head, int cylinder_num, int sector_num) {
        setMagneticHead(head);
        setCylinder(cylinder_num);
        setSector(sector_num);

        ArrayList<ArrayList<byte[]>> disk = floppy.get(this.magneticHead.ordinal());
        ArrayList<byte[]> cylinder = disk.get(this.current_cylinder);

        byte[] sector = cylinder.get(this.current_sector);

        return sector;
    }

    /**
     * 写数据到软盘
     * @param head
     * @param cylinder_num
     * @param sector_num
     * @param buf
     */
    public void writeFloppy(MAGNETIC_HEAD head, int cylinder_num, int sector_num, byte[] buf){
        setMagneticHead(head);
        setCylinder(cylinder_num);
        setSector(sector_num);

        //write
        ArrayList<ArrayList<byte[]>> disk = floppy.get(magneticHead.ordinal());
        disk.get(current_cylinder).set(current_sector,buf);

    }

    /**
     * 制作软盘
     * @param fileName
     */
    public void makeFloppy(String fileName) {
        DataOutputStream out=null;
        try {
            out = new DataOutputStream(new FileOutputStream(fileName));
            for (int head = 0; head <= MAGNETIC_HEAD.MAGNETIC_HEAD_1.ordinal(); head++) {
                for (int cylinder = 0; cylinder < CYLINDER_COUNT; cylinder++) {
                    for (int sector = 1; sector <= SECTORS_COUNT; sector++) {
                        byte[] buf = readFloppy(MAGNETIC_HEAD.values()[head], cylinder, sector);
                        out.write(buf);
                    }
                }
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(out!=null) {
                try {
                    out.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    public void setMagneticHead(MAGNETIC_HEAD head) {
        magneticHead = head;
    }

    public void setCylinder(int cylinder) {
        if (cylinder < 0) {
            this.current_cylinder = 0;
        }
        else if (cylinder >= 80) {
            this.current_cylinder = 79;
        }
        else {
            this.current_cylinder = cylinder;
        }
    }

    public void setSector(int sector) {
        //sector 编号从1到18
        if (sector < 0) {
            this.current_sector = 0;
        }
        else if (sector > 18) {
            this.current_sector = 18 - 1;
        }
        else {
            this.current_sector = sector - 1;
        }
    }


}
