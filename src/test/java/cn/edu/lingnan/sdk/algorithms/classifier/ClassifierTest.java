package cn.edu.lingnan.sdk.algorithms.classifier;

import cn.edu.lingnan.utils.SerializableUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */
public class ClassifierTest {

    @Test
    public void classifierTrainingTest() throws IOException {
        File file = new File("D://ChnSentiCorp情感分析酒店评论");
        BayesModel model = new BayesModelImpl();
        model.train(file);
        SerializableUtils.saveCurrentState(model, "D://model");
    }

    @Test
    public void classifierPredictTest(){
        BayesModel model = SerializableUtils.getLastState(BayesModel.class, "D://model");
        Classifier classifier = new ClassifierImpl(model);
        String predict = classifier.predict("房间不错，我给满分");
        System.out.println(predict);
        predict = classifier.predict("体验不好,房间的装修太旧");
        System.out.println(predict);

    }
}
