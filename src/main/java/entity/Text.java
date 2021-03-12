package entity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TYX
 * @name Text
 * @description
 * @time 就是个提醒：行数判断边界是小于 不是小于等于
 **/


public class Text {
    private int totalLine;
    private final List<ProcessLine> content;

    /**
     * 构造函数 初始化行数为0
     */
    public Text() {
        totalLine = 0;
        content = new ArrayList<ProcessLine>();
    }

    /**
     * 读取内容
     * 将path文件中的内容读入
     *
     * @param path 文件目录
     */
    public void init(String path) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        int currentLine = 0;
        while (line != null) {
            content.add(new ProcessLine(currentLine++, line));
            line = bufferedReader.readLine();
            totalLine++;
        }
        bufferedReader.close();
        fileReader.close();
    }


    /**
     * 返回所有代码
     *
     * @return
     */
    public List<ProcessLine> getContent() {
        return content;
    }

}
