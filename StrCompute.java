package memozier;

/**
 * Title: memozier<br>
 * Description: <br>
 * Copyright: Copyright (c) 2016<br>
 *
 * @author lili 2016/5/31
 */
public class StrCompute implements Computable<String, String> {
    @Override
    public String convert(String args) {
        StringBuffer s = new StringBuffer(args);
        System.out.println("字符串" + s.toString()+"开始转换");
        return s.reverse().toString();
    }
}
