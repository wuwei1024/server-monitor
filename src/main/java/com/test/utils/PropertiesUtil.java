package com.test.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 属性文件工具类
 *
 * @author Wu Wei
 * @date 2018-01-08 17:15:19
 */
public class PropertiesUtil {

    private static final Properties prop = new Properties();
    private static final String filePath = System.getProperty("user.dir") + "\\serverMonitor.properties";
    private static final Logger logger = Logger.getLogger(PropertiesUtil.class.getName());

    /**
     * 读取属性key-value
     *
     * @return
     */
    public static Map<String, String> getProperties() {
        Map<String, String> params = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            prop.load(fis);
            prop.entrySet().stream().forEach(entry ->
                    params.put((String) entry.getKey(), (String) entry.getValue())
            );
        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return params;
    }

    /**
     * 修改属性值
     *
     * @param key
     * @param value
     */
    public static void setProperties(String key, String value) {
        try (FileInputStream fis = new FileInputStream(filePath);
             FileOutputStream fos = new FileOutputStream(filePath)) {
            prop.load(fis);
            prop.setProperty(key, value);
            prop.store(fos, null);
        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }
}
