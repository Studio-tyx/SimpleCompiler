package tool;

/**
 * @author TYX
 * @name CharacterTools
 * @description 工具类 判断字符类型用
 * @createTime 2021/3/19 14:56
 **/
public final class CharacterTools {
    /**
     * 是否为大写字符
     *
     * @param ch 需要判断的字符
     * @return 是否为大写
     */
    public static boolean isUpper(char ch) {
        return (ch >= 'A' && ch <= 'Z');
    }

    /**
     * 是否为小写字符
     *
     * @param ch 需要判断的字符
     * @return 是否为小写
     */
    public static boolean isLower(char ch) {
        return ((ch >= 'a' && ch <= 'z'));
    }

    /**
     * 是否为空字符@
     *
     * @param ch 需要判断的字符
     * @return 是否为空字符@
     */
    public static boolean isAt(char ch) {
        return (ch == '@');
    }

    /**
     * 是否为其他符号
     *
     * @param ch 需要判断的字符
     * @return 是否为其他符号
     */
    public static boolean isPunctuation(char ch) {
        return (ch == '(') || (ch == ')') || (ch == '{') || (ch == '}') || (ch == ';') || (ch == ',') || (ch == '=');
    }
}
