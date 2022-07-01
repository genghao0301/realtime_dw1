package com.vx.app.func;

import com.alibaba.fastjson.JSONObject;
import com.vx.utils.DimUtil;
import com.vx.utils.DimUtil2;
import com.vx.utils.ThreadPoolUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.curator4.org.apache.curator.utils.ThreadUtils;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @version V1.0
 * @ClassName: DimAsyncFunction
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/4/24 17:51
 */
public abstract class DimAsyncFunction<T> extends RichAsyncFunction<T, T> implements DimJoinFunction<T> {

    //声明线程池对象
    private ThreadPoolExecutor threadPoolExecutor;
    //声明属性
    private String tableName;

    public DimAsyncFunction(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        //初始化线程池
        threadPoolExecutor = ThreadPoolUtil.getInstance();
    }

    @Override
    public void asyncInvoke(T input, ResultFuture<T> resultFuture) throws Exception {
        threadPoolExecutor.submit(() -> {
            //0.获取查询条件
            String key = getKey(input);
            //1.查询维度信息
            List<JSONObject> dimInfo = null;
            try {
                dimInfo = DimUtil.getDimInfo(tableName,  key, JSONObject.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //2.关联到事实数据上
            if (dimInfo != null && dimInfo.size() > 0) {
                try {
                    join(input, dimInfo.get(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //3.继续向下游传输
            resultFuture.complete(Collections.singletonList(input));
        });
    }
}
