package cn.edu.lingnan.service.command;

import javafx.concurrent.Task;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/4/14.
 */
public class RootCommand extends AbstractCommand<Void> {

    /**
     * 文件读取工厂
     */
    private Tika tika = new Tika();

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Task<String> getStringFromFileTask(File file){
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return getStringFromFile(file);
            }
        };
        this.executorService.execute(task);
        return task;
    }

    private String getStringFromFile(File file) throws IOException, TikaException {
        if (file == null)
            return null;
        return tika.parseToString(file);
    }
}
