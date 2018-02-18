package cn.edu.lingnan.utils;

import cn.edu.lingnan.Main;
import cn.edu.lingnan.sdk.XMLParser.XMLObjectFactory;
import cn.edu.lingnan.sdk.advice.*;
import cn.edu.lingnan.sdk.overlay.CustomXMLLoader;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

//资源的代理对象
public class R {
    private R(){}

    private static Owner owner = Owner.owner;

    /**
     * 获取实际资源的拥有者
     * @return
     */
    public static Owner getOwner(){
        return owner;
    }

    /**
     * 根据给定的文件名获取相应的输入流
     * 以工程的根目录为开始目录
     * 常用于获取图片的输入流
     * @param fileName
     * @return
     */
    public static InputStream getResourceAsStream(String fileName){

        return owner.getResourceAsStream(fileName);
    }

    /**
     * 根据给定的文件名获取相应的URL
     * @param fileName
     * @return
     */
    public static URL getResourcesAsURL(String fileName){
        return owner.getResourcesAsURL(fileName);
    }

    /**
     * 根据给定的文件名获取相应的对象
     * 常用于获取fxml 中布局的根结点
     * @param fileName
     * @return
     */
    public static Object getResourcesAsComponent(String fileName){
        return owner.getResourcesAsComponent(fileName);
    }

    /**
     * 获取本工程的主要的
     * 包含最初的stage的对象的
     * 主对象,常用于设置scene graph的属主
     * @return
     */
    public static Main getApplication(){
        return owner.application;
    }

    /**
     * 根据键名获取对象
     * 常用于控制器间的临时的数据共享
     * @param key
     * @return
     */
    public static Object getParameters(String key){
        return owner.getParameters(key);
    }

    /**
     * 根据键名存放对象
     * @param key
     * @param value
     */
    public static void setParameters(String key, Object value){
        owner.setParameters(key, value);
    }

    /**
     * 获取default.xml文件名声明的对象
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T getObjectFromFactory(Class<?> clz){
        return owner.getObjectFromFactory(clz);
    }

    /**
     * 获取应用程序全局配置对象
     * @return
     */
    public static Config getConfig(){
        return owner.getConfig();
    }

   //资源拥有者
   public static class Owner{
       private Owner(){}
       private static Owner owner = new Owner();
       public static Owner getOwner(){return owner;}
       private  ClassLoader classLoader = R.class.getClassLoader();
       /**
        *
        */
       private XMLObjectFactory objectFactory = null;
       /**
        *
        */
       private HashMap<String, Object> objectHashMap = new HashMap<>();
       private Main application = null;
       public void setApplication(Main _application){
           application = _application;
       }
       public  InputStream getResourceAsStream(String fileName){

           return classLoader.getResourceAsStream(fileName);
       }
       public URL getResourcesAsURL(String fileName){
           return classLoader.getResource(fileName);
       }

       public Object getResourcesAsComponent(String fileName){
           FXMLLoader loader = new CustomXMLLoader();
           loader.setLocation(R.getResourcesAsURL(fileName));
           try {
               return loader.load();
           } catch (IOException e) {
               e.printStackTrace();
           }
           return null;
       }
       //一次性消费：超过时一定的时间限制后，自动抹除该变量
       public Object getParameters(String key){
           Object object = this.objectHashMap.get(key);
           return object;
       }
       private void setParameters(String key, Object value){
           this.objectHashMap.put(key, value);
       }

       public <T> T getObjectFromFactory(Class<?> clz){
           if (this.objectFactory == null) {
               this.objectFactory = new XMLObjectFactory("/xml/default.xml");
           }
           return this.objectFactory.getObject(clz);
       }

       /**
        * 获取应用程序的基本配置
        * @return
        */
       public Config getConfig(){
           Config config = Config.getInstance();
           return config;
       }
   }
}



