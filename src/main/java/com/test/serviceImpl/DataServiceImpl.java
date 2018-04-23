package com.test.serviceImpl;

import com.test.service.DataService;
import com.test.utils.SigarUtil;
import org.hyperic.sigar.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DataServiceImpl implements DataService {

    private static final Sigar sigar = SigarUtil.getInstance();
    private static final Logger logger = Logger.getLogger(DataServiceImpl.class.getName());

    @Override
    public Map<String, Object> getServerInfo() {
        Map<String, Object> serverInfo = new HashMap<>();
        try {
            double totalCpuPerc = 0.0d;
            CpuPerc[] cpuPercs = sigar.getCpuPercList();
            for (CpuPerc cpuPerc : cpuPercs) {
                totalCpuPerc += cpuPerc.getCombined();
            }
            Mem mem = sigar.getMem();
            FileSystem fsList[] = sigar.getFileSystemList();
            long totalDiskVolume = 0L;
            long usedDiskVolume = 0L;
            for (FileSystem fs : fsList) {
                if (fs.getType() == 2) {//本地硬盘
                    FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
                    totalDiskVolume += usage.getTotal();
                    usedDiskVolume += usage.getUsed();
                }
            }
            double cpuUsage = totalCpuPerc / cpuPercs.length;
            double memUsage = (double) mem.getUsed() / mem.getTotal();
            double diskUsage = (double) usedDiskVolume / totalDiskVolume;
            serverInfo.put("cpuUsage", Math.round(cpuUsage * 100) / 100d);
            serverInfo.put("memUsage", Math.round(memUsage * 100) / 100d);
            serverInfo.put("diskUsage", Math.round(diskUsage * 100) / 100d);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return serverInfo;
    }
}
