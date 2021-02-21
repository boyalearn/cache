import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.net.URL;
import java.util.Scanner;

public class DistributionEhcacheDemo {
    public static void main(String[] args) {
        URL url = DistributionEhcacheDemo.class.getClassLoader().getResource("ehcache.xml");
        CacheManager manager = new CacheManager(url);

        Cache cache = manager.getCache("auto_cache");
        // 主机 1 的标识: GRQServer
        Element element = new Element("GRQServer-" + System.currentTimeMillis(), "GRQServer");
        // 主机 2 的标识: LSLServer
//        Element element = new Element("LSLServer-" + System.currentTimeMillis(), "LSLServer");
        cache.put(element);

        // 键盘输入流
        Scanner in = new Scanner(System.in);
        int i = 0;

        while (true) {
            String inputKey = null;
            String inputValue = null;
            Element inputElement = null;

            // 输入方法
            System.out.println();
            System.out.println("Method: ");
            String method = in.nextLine();
            switch (method) {
                case "query":
                    break;
                case "add":
                case "update":
                    System.out.print("Key: ");
                    inputKey = in.nextLine();
                    System.out.print("Value: ");
                    inputValue = in.nextLine();

                    Element outputElement = new Element(inputKey, inputValue);
                    cache.put(outputElement);
                    break;
                case "delete":
                    System.out.print("Key: ");
                    inputKey = in.nextLine();
                    cache.remove(inputKey);
                    break;
                default:
                    System.out.println("ERROR: Can't Recognize this Method \"" + method + "\"");
                    break;
            }

            // 输出缓存中所有 (Key, Value) 值
            for(Object key : cache.getKeys()) {
                Element obj = cache.get(key);
                if(obj == null) {
                    continue;
                }
                Object value = obj.getObjectValue();
                if(value == null) {
                    continue;
                }
                System.out.println(key + " : " + value);
            }
        }
    }
}
