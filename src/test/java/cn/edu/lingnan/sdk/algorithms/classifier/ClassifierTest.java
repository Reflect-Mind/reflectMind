package cn.edu.lingnan.sdk.algorithms.classifier;

import cn.edu.lingnan.utils.SerializableUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/3/14.
 */
public class ClassifierTest {

    @Test
    public void classifierTrainingTest() throws IOException {
        File file = new File("D://ChnSentiCorp情感分析酒店评论");
        cn.edu.lingnan.sdk.algorithms.classifier.BayesModel model = new cn.edu.lingnan.sdk.algorithms.classifier.BayesModelImpl();
        model.train(file);
        SerializableUtils.saveCurrentState(model, "D://model");
    }

    @Test
    public void classifierPredictTest(){

        cn.edu.lingnan.sdk.algorithms.classifier.BayesModel model = SerializableUtils.getLastState(cn.edu.lingnan.sdk.algorithms.classifier.BayesModel.class, "D://model");
        cn.edu.lingnan.sdk.algorithms.classifier.Classifier classifier = new cn.edu.lingnan.sdk.algorithms.classifier.ClassifierImpl(model);
        String predict = classifier.predict("房间不错，我给满分");
        System.out.println(predict);
        predict = classifier.predict("很差");
        System.out.println(predict);
    }
}
