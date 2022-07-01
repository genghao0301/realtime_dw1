package com.vx.app.func;

import com.vx.utils.KeyWordUtil;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

import java.util.List;

/**
 * @version V1.0
 * @ClassName: KeyWordUDTF
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/4/24 18:28
 */
public class KeyWordUDTF extends TableFunction<Row> {

    public void eval(String str) {
        //使用IK分词器对搜索的关键词进行分词处理
        List<String> list = KeyWordUtil.analyze(str);

        //遍历单词写出
        for (String word : list)
            collect(Row.of(word));
    }
}
