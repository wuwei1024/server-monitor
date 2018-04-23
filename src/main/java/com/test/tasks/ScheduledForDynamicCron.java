package com.test.tasks;

import com.test.service.DataService;
import com.test.utils.HttpClient;
import com.test.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ScheduledForDynamicCron implements SchedulingConfigurer {

    @Autowired
    private DataService dataService;
    private static String redisUrl;
    private static String serverId;
    private static String reportRate;
    private static final String DEFAULT_CRON = "0 0/5 * * * ?";
    private String cron = "0/%s * * * * ?";

    private static final Logger logger = Logger.getLogger(ScheduledForDynamicCron.class.getName());

    static {
        Map<String, String> config = PropertiesUtil.getProperties();
        redisUrl = config.get("redisUrl");
        serverId = config.get("serverId");
        reportRate = config.get("reportRate");
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (redisUrl == null || redisUrl.isEmpty() ||
                serverId == null || serverId.isEmpty()) return;
        try {
            taskRegistrar.addTriggerTask(() -> {
                //定时查询服务器CPU/MEM/DISK信息，并上传至redis集群
                Map<String, Object> info = dataService.getServerInfo();
                sendData("10", (double) info.get("cpuUsage"));
                sendData("11", (double) info.get("memUsage"));
                sendData("13", (double) info.get("diskUsage"));
            }, (triggerContext) -> {
                // 定时任务触发，可修改定时任务的执行周期
                cron = reportRate == null || reportRate.isEmpty() ?
                        DEFAULT_CRON : String.format(cron, reportRate);
                CronTrigger trigger = new CronTrigger(cron);
                return trigger.nextExecutionTime(triggerContext);
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    /**
     * 发送数据至Redis服务器集群
     *
     * @param type
     * @param data
     */
    private void sendData(String type, double data) {
        StringBuilder jsonData = new StringBuilder("{\"serverId\":\"");
        long datetime = System.currentTimeMillis();
        try {
            jsonData.append(serverId).append("\",\"deviceId\":\"").append(type)
                    .append("\",\"type\":\"").append(type).append("\",\"event\":\"").append("data")
                    .append("\",\"data\":\"").append(data).append("\",\"datetime\":\"").append(datetime)
                    .append("\"}");
            HashMap<String, String> params = new HashMap<>();
            params.put("data", jsonData.toString());
            HttpClient.sendPost(redisUrl, params);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
    }
}
