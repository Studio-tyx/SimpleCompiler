package entity;

import com.sun.prism.shader.Solid_TextureRGB_AlphaTest_Loader;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author TYX
 * @name MatrixGraph
 * @description
 * @time
 **/
public class MatrixGraph implements Graph<Integer> {
    private int[][] matrix;
    private int statusNumber;
    private int terminalNumber;

    public MatrixGraph() {
    }

    public MatrixGraph(int terminalNumber) {
        this.matrix = new int[101][terminalNumber];
        statusNumber=0;
        this.terminalNumber=terminalNumber;
    }
    //0-27为终结符代号 如果为-2表示无法连通 -1为未赋值

    public Integer nextStatus(Integer thisStatus,Integer weight){
        return matrix[thisStatus][weight];
    }


    //增加一条从this到next的边
    public void addEdge(Integer thisVertex, Integer nextVertex, Integer weight) {
        matrix[thisVertex][weight]=nextVertex;
        statusNumber=thisVertex;
    }

    public Integer getFirst() {
        return 0;
    }

    public void show() {
        System.out.println("MatrixGraph show:");
        for(int i=0;i<=statusNumber;i++){
            for(int j=0;j<terminalNumber;j++){
                System.out.println(i+"->"+matrix[i][j]+":"+j);
            }
        }
    }
}
