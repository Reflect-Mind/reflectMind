package cn.edu.lingnan.utils;

import org.json.JSONObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

/**
 * 程序引导启动类
 * @author 李田锋
 * Created by Administrator on 2018/9/14.
 */
public class Boot {

    private final static String GLOBAL_INI =
            System.getProperty("user.dir") + File.separator + "global.xml";

    private Boot(){}

    /**
     * 引导程序启动，反序列实例
     * 为桌面环境配置初始化
     * 获取当前项目名称
     * 获取一个global和config对象
     */
    static void bootAndRead() throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(Global.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        R.Owner owner = R.getOwner();

        File globalIni = new File(GLOBAL_INI);

        try {
            Global global = null;
            // 获取global对象
            global = (Global) unmarshaller.unmarshal(globalIni);
            owner.setGlobal(global);

            // 获取config对象
            Config config = SerializableUtils.getLastState(Config.class, global.getCurrentConfigPath());
            owner.setConfig(config);
        }
        catch (Exception e ) {
            // 当配置文件不存在时global和config或者无法读取时将自行实例化
            owner.setGlobal(new Global());
            owner.setConfig(new Config());
        }


    }

    /**
     * 程序运行结束时
     * 进行相关数据的保存
     * 先保存的数据有：config实例 Global配置
     *
     */
    static void shutdownAndWrite() throws JAXBException, IOException {

        Global global = R.getGlobal();
        JAXBContext context = JAXBContext.newInstance(Global.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        // 持久化global对象
        marshaller.marshal(global, new File(GLOBAL_INI));

        // 持久化config对象
        Config config = R.getConfig();
        SerializableUtils.saveCurrentState(config, global.getCurrentConfigPath());

    }
}
