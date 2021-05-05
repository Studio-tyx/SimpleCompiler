package tool;

import javax.swing.*;
import java.io.*;

/**
 * @author TYX
 * @name IOTools
 * @description
 * @createTime 2021/5/4 14:20
 **/
public final class IOTools {
    public static String chooseFile(){
        String res = null;
        JFileChooser chooser=new JFileChooser("D:\\Languages\\Java\\SimpleCompiler");
        int ret=chooser.showOpenDialog(null);
        if(ret==JFileChooser.APPROVE_OPTION) {
            File file=chooser.getSelectedFile();
            res=(file.getAbsolutePath());
            System.out.println((file.getAbsolutePath()));
        }
        return res;
    }

    public static String read(String url){
        String end = "";
        String str = "";
        if(url!=null) {
            File file = new File(url);
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    if (!((str = br.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                end = end + str + "\n";
            }
            //System.out.println("\nend:"+end+"\n");
        }
        return end;
    }
}
