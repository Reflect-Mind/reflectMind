package cn.edu.lingnan.utils;

import cn.edu.lingnan.Main;
import cn.edu.lingnan.sdk.XMLParser.XMLObjectFactory;
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
    public static Owner getOwner(){
        return owner;
    }
    public static InputStream getResourceAsStream(String fileName){

        return owner.getResourceAsStream(fileName);
    }
    public static URL getResourcesAsURL(String fileName){
        return owner.getResourcesAsURL(fileName);
    }

    public static Object getResourcesAsComponent(String fileName){
        return owner.getResourcesAsComponent(fileName);
    }

    public static Main getApplication(){
        return owner.application;
    }

    public static Object getParameters(String key){
        return owner.getParameters(key);
    }
    public static void setParameters(String key, Object value){
        owner.setParameters(key, value);
    }

    public static <T> T getObjectFromFactory(Class<T> clz){
        return owner.getObjectFromFactory(clz);
    }

   //资源拥有者
   public static class Owner{
       private Owner(){}
       private static Owner owner = new Owner();
       public static Owner getOwner(){return owner;}
       private  ClassLoader classLoader = R.class.getClassLoader();
       private XMLObjectFactory objectFactory = null;
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

       public <T> T getObjectFromFactory(Class<T> clz){
           if (this.objectFactory == null) {
               this.objectFactory = new XMLObjectFactory("/xml/default.xml");
           }
           return this.objectFactory.getObject(clz);
       }
   }
}



