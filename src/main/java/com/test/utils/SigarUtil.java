package com.test.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.test.entity.SigarInfo;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

public class SigarUtil {

    private static final Sigar sigar = new Sigar();
    private static final Logger logger = Logger.getLogger(SigarUtil.class.getName());

    public static void main(String[] args) {
        System.out.println("****************操作系统信息************");
        for (SigarInfo info : getOsInfo()) {
            System.out.println(info.getName() + ":" + info.getValue());
        }
        System.out.println("****************JVM虚拟机信息************");
        for (SigarInfo info : getJvmInfo()) {
            System.out.println(info.getName() + ":" + info.getValue());
        }
        System.out.println("****************CPU信息************");
        for (SigarInfo info : getCpuInfo()) {
            System.out.println(info.getName() + ":" + info.getValue());
        }
        System.out.println("****************内存信息************");
        for (SigarInfo info : getMemoryInfo()) {
            System.out.println(info.getName() + ":" + info.getValue());
        }
        System.out.println("****************文件信息************");
        for (SigarInfo info : getFileInfo()) {
            System.out.println(info.getName() + ":" + info.getValue());
        }
        System.out.println("****************网络信息************");
        for (SigarInfo info : getNetInfo()) {
            System.out.println(info.getName() + ":" + info.getValue());
        }
    }

    /**
     * 获取Sigar实例
     *
     * @return
     */
    public static final Sigar getInstance() {
        return sigar;
    }

    /**
     * 1.获取操作系统信息
     *
     * @return
     */
    public static List<SigarInfo> getOsInfo() {
        List<SigarInfo> osInfoList = new ArrayList<>();
        try {
            OperatingSystem os = OperatingSystem.getInstance();
            osInfoList.add(new SigarInfo("操作系统", os.getArch()));
            osInfoList.add(new SigarInfo("操作系统CpuEndian()", os.getCpuEndian()));
            osInfoList.add(new SigarInfo("操作系统DataModel()", os.getDataModel()));
            osInfoList.add(new SigarInfo("操作系统的描述", os.getDescription()));
            osInfoList.add(new SigarInfo("操作系统的供应商", os.getVendor()));
            osInfoList.add(new SigarInfo("操作系统的供应商编号", os.getVendorCodeName()));
            osInfoList.add(new SigarInfo("操作系统的供应商名称", os.getVendorName()));
            osInfoList.add(new SigarInfo("操作系统供应商类型", os.getVendorVersion()));
            osInfoList.add(new SigarInfo("操作系统的版本号", os.getVersion()));
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return osInfoList;
    }

    /**
     * 2.获取系统信息和jvm信息
     *
     * @return
     * @throws UnknownHostException
     */
    public static List<SigarInfo> getJvmInfo() {
        List<SigarInfo> jvmInfoList = new ArrayList<>();
        try {
            Runtime r = Runtime.getRuntime();
            Properties sysProps = System.getProperties();
            InetAddress addr = InetAddress.getLocalHost();
            Map<String, String> envInfoMap = System.getenv();
            String userName = envInfoMap.get("USERNAME");// 获取用户名
            String computerName = envInfoMap.get("COMPUTERNAME");// 获取计算机名
            String userDomain = envInfoMap.get("USERDOMAIN");// 获取计算机域名
            jvmInfoList.add(new SigarInfo("用户名", userName));
            jvmInfoList.add(new SigarInfo("计算机名", computerName));
            jvmInfoList.add(new SigarInfo("计算机域名", userDomain));
            jvmInfoList.add(new SigarInfo("主机IP", addr.getHostAddress()));
            jvmInfoList.add(new SigarInfo("主机名", addr.getHostName()));
            jvmInfoList.add(new SigarInfo("JVM总内存", String.valueOf(r.totalMemory())));
            jvmInfoList.add(new SigarInfo("JVM剩余内存", String.valueOf(r.freeMemory())));
            jvmInfoList.add(new SigarInfo("JVM处理器个数", String.valueOf(r.availableProcessors())));
            jvmInfoList.add(new SigarInfo("Java的运行环境版本", sysProps.getProperty("java.version")));
            jvmInfoList.add(new SigarInfo("Java的运行环境供应商", sysProps.getProperty("java.vendor")));
            jvmInfoList.add(new SigarInfo("Java供应商的URL", sysProps.getProperty("java.vendor.url")));
            jvmInfoList.add(new SigarInfo("Java的安装路径", sysProps.getProperty("java.home")));
            jvmInfoList.add(new SigarInfo("Java的虚拟机规范版本", sysProps.getProperty("java.vm.specification.version")));
            jvmInfoList.add(new SigarInfo("Java的虚拟机规范供应商", sysProps.getProperty("java.vm.specification.vendor")));
            jvmInfoList.add(new SigarInfo("Java的虚拟机规范名称", sysProps.getProperty("java.vm.specification.name")));
            jvmInfoList.add(new SigarInfo("Java的虚拟机实现版本", sysProps.getProperty("java.vm.version")));
            jvmInfoList.add(new SigarInfo("Java的虚拟机实现供应商", sysProps.getProperty("java.vm.vendor")));
            jvmInfoList.add(new SigarInfo("Java的虚拟机实现名称", sysProps.getProperty("java.vm.name")));
            jvmInfoList.add(new SigarInfo("Java运行时环境规范版本", sysProps.getProperty("java.specification.version")));
            jvmInfoList.add(new SigarInfo("Java运行时环境规范供应商", sysProps.getProperty("java.specification.vendor")));
            jvmInfoList.add(new SigarInfo("Java的虚拟机规范名称", sysProps.getProperty("java.specification.name")));
            jvmInfoList.add(new SigarInfo("Java的类格式版本号", sysProps.getProperty("java.class.version")));
            jvmInfoList.add(new SigarInfo("Java的类路径", sysProps.getProperty("java.class.path")));
            jvmInfoList.add(new SigarInfo("加载库时搜索的路径列表", sysProps.getProperty("java.library.path")));
            jvmInfoList.add(new SigarInfo("默认的临时文件路径", sysProps.getProperty("java.io.tmpdir")));
            jvmInfoList.add(new SigarInfo("一个或多个扩展目录的路径", sysProps.getProperty("java.ext.dirs")));
            jvmInfoList.add(new SigarInfo("操作系统的名称", sysProps.getProperty("os.name")));
            jvmInfoList.add(new SigarInfo("操作系统的构架", sysProps.getProperty("os.arch")));
            jvmInfoList.add(new SigarInfo("操作系统的版本", sysProps.getProperty("os.version")));
            jvmInfoList.add(new SigarInfo("文件分隔符", sysProps.getProperty("file.separator")));
            jvmInfoList.add(new SigarInfo("路径分隔符", sysProps.getProperty("path.separator")));
            jvmInfoList.add(new SigarInfo("行分隔符", sysProps.getProperty("line.separator")));
            jvmInfoList.add(new SigarInfo("用户的账户名称", sysProps.getProperty("user.name")));
            jvmInfoList.add(new SigarInfo("用户的主目录", sysProps.getProperty("user.home")));
            jvmInfoList.add(new SigarInfo("用户的当前工作目录", sysProps.getProperty("user.dir")));
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return jvmInfoList;
    }

    /**
     * 3.获取cpu信息
     *
     * @return
     * @throws SigarException
     */
    public static List<SigarInfo> getCpuInfo() {
        List<SigarInfo> cpuInfoList = new ArrayList<>();
        try {
            CpuInfo[] cpuInfos = sigar.getCpuInfoList();
            CpuPerc[] cpuPercs = sigar.getCpuPercList();
            for (int i = 0; i < cpuInfos.length; i++) {
                CpuInfo cpuInfo = cpuInfos[i];
                cpuInfoList.add(new SigarInfo("第" + i + "个CPU信息", String.valueOf(i)));
                cpuInfoList.add(new SigarInfo("CPU的总量MHz" + i, String.valueOf(cpuInfo.getMhz())));
                cpuInfoList.add(new SigarInfo("获得CPU的供应商" + i, cpuInfo.getVendor()));
                cpuInfoList.add(new SigarInfo("获得CPU的类别" + i, cpuInfo.getModel()));
                cpuInfoList.add(new SigarInfo("缓冲存储器数量" + i, String.valueOf(cpuInfo.getCacheSize())));
            }
            for (int i = 0; i < cpuPercs.length; i++) {
                CpuPerc cpuPerc = cpuPercs[i];
                cpuInfoList.add(new SigarInfo("第" + i + "个CPU片段", String.valueOf(i)));
                cpuInfoList.add(new SigarInfo("CPU用户使用率" + i, String.valueOf(cpuPerc.getUser())));
                cpuInfoList.add(new SigarInfo("CPU系统使用率" + i, String.valueOf(cpuPerc.getSys())));
                cpuInfoList.add(new SigarInfo("CPU当前等待率" + i, String.valueOf(cpuPerc.getWait())));
                cpuInfoList.add(new SigarInfo("CPU当前错误率" + i, String.valueOf(cpuPerc.getNice())));
                cpuInfoList.add(new SigarInfo("CPU当前空闲率" + i, String.valueOf(cpuPerc.getIdle())));
                cpuInfoList.add(new SigarInfo("CPU总的使用率" + i, String.valueOf(cpuPerc.getCombined())));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return cpuInfoList;
    }

    /**
     * 4.获取内存信息
     *
     * @return
     * @throws SigarException
     */
    public static List<SigarInfo> getMemoryInfo() {
        List<SigarInfo> memoryInfoList = new ArrayList<>();
        try {
            Mem mem = sigar.getMem();
            memoryInfoList.add(new SigarInfo("内存总量", mem.getTotal() / 1024L + "K av"));
            memoryInfoList.add(new SigarInfo("当前内存使用量", mem.getUsed() / 1024L + "K used"));
            memoryInfoList.add(new SigarInfo("当前内存剩余量", mem.getFree() / 1024L + "K free"));
            Swap swap = sigar.getSwap();
            memoryInfoList.add(new SigarInfo("交换区总量", swap.getTotal() / 1024L + "K av"));
            memoryInfoList.add(new SigarInfo("当前交换区使用量", swap.getUsed() / 1024L + "K used"));
            memoryInfoList.add(new SigarInfo("当前交换区剩余量", swap.getFree() / 1024L + "K free"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return memoryInfoList;
    }

    /**
     * 5.获取文件信息
     *
     * @return
     * @throws SigarException
     */
    public static List<SigarInfo> getFileInfo() {
        List<SigarInfo> fileInfoList = new ArrayList<>();
        try {
            FileSystem fslist[] = sigar.getFileSystemList();
            for (int i = 0; i < fslist.length; i++) {
                FileSystem fs = fslist[i];
                fileInfoList.add(new SigarInfo("分区的盘符号" + i, i + ""));
                fileInfoList.add(new SigarInfo("盘符名称" + i, fs.getDevName()));
                fileInfoList.add(new SigarInfo("盘符路径" + i, fs.getDirName()));
                fileInfoList.add(new SigarInfo("盘符标志" + i, fs.getFlags() + ""));
                fileInfoList.add(new SigarInfo("盘符类型(FAT32,NTFS)" + i, fs.getSysTypeName()));
                fileInfoList.add(new SigarInfo("盘符类型名" + i, fs.getTypeName()));
                fileInfoList.add(new SigarInfo("盘符文件系统类型" + i, fs.getType() + ""));
                if (fs.getType() == 2) {//本地硬盘
                    FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
                    fileInfoList.add(new SigarInfo("文件系统总大小" + fs.getDevName(), usage.getTotal() + "KB"));
                    fileInfoList.add(new SigarInfo("文件系统剩余大小" + fs.getDevName(), usage.getFree() + "KB"));
                    fileInfoList.add(new SigarInfo("文件系统可用大小" + fs.getDevName(), usage.getAvail() + "KB"));
                    fileInfoList.add(new SigarInfo("文件系统已经使用量" + fs.getDevName(), usage.getUsed() + "KB"));
                    fileInfoList.add(new SigarInfo("文件系统资源的利用率" + fs.getDevName(), usage.getUsePercent() * 100D + "%"));
                    fileInfoList.add(new SigarInfo(fs.getDevName() + "读出", usage.getDiskReads() + ""));
                    fileInfoList.add(new SigarInfo(fs.getDevName() + "写入", usage.getDiskWrites() + ""));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return fileInfoList;
    }

    /**
     * 6.获取网络信息
     *
     * @return
     * @throws SigarException
     */
    public static List<SigarInfo> getNetInfo() {
        List<SigarInfo> netInfoList = new ArrayList<>();
        try {
            String nIfNames[] = sigar.getNetInterfaceList();
            for (int i = 0; i < nIfNames.length; i++) {
                String name = nIfNames[i];
                NetInterfaceConfig nIfConfig = sigar.getNetInterfaceConfig(name);
                netInfoList.add(new SigarInfo("网络设备名" + i, name));
                netInfoList.add(new SigarInfo("IP地址" + i, nIfConfig.getAddress()));
                netInfoList.add(new SigarInfo("子网掩码" + i, nIfConfig.getNetmask()));
                if ((nIfConfig.getFlags() & 1L) <= 0L) {
                    System.out.println("getNetInterfaceStat not exist");
                    continue;
                }
                NetInterfaceStat nIfStat = sigar.getNetInterfaceStat(name);
                netInfoList.add(new SigarInfo("接收的总包裹数" + i, nIfStat.getRxPackets() + ""));
                netInfoList.add(new SigarInfo("发送的总包裹数" + i, nIfStat.getTxPackets() + ""));
                netInfoList.add(new SigarInfo("接收到的总字节数" + i, nIfStat.getRxBytes() + ""));
                netInfoList.add(new SigarInfo("发送的总字节数" + i, nIfStat.getTxBytes() + ""));
                netInfoList.add(new SigarInfo("接收到的错误包数" + i, nIfStat.getRxErrors() + ""));
                netInfoList.add(new SigarInfo("发送数据包时的错误数" + i, nIfStat.getTxErrors() + ""));
                netInfoList.add(new SigarInfo("接收时丢弃的包数" + i, nIfStat.getRxDropped() + ""));
                netInfoList.add(new SigarInfo("发送时丢弃的包数" + i, nIfStat.getTxDropped() + ""));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return netInfoList;
    }
}
