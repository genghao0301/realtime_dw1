package cdc.vx.trigger;

import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * @version V1.0
 * @ClassName: OrderTrigger
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/6/8 10:07
 */
public class InOutTrigger extends Trigger<Object, TimeWindow> {

    private static final long serivalVersionUID = 1L;
    public InOutTrigger(){}

    private static int flag = 0;

    public static int threshold = 0;

    @Override
    public TriggerResult onElement(Object element, long timestamp, TimeWindow window, TriggerContext ctx) throws Exception {
        ctx.registerEventTimeTimer(window.maxTimestamp());
        flag++;
        if(flag>=threshold){
            flag = 0;
            ctx.deleteProcessingTimeTimer(window.maxTimestamp());
            return TriggerResult.FIRE_AND_PURGE;
        }
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
        if(flag>0){
            System.out.println("到达窗口时间执行触发："+flag);
            flag = 0;
            return TriggerResult.FIRE_AND_PURGE;
        }
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
        if(time>=window.maxTimestamp()&&flag>0){
            System.out.println("到达时间窗口且有数据，触发操作！");
            flag=0;
            return TriggerResult.FIRE_AND_PURGE;
        }else if(time>=window.maxTimestamp()&&flag==0){
            //清除窗口但不触发
            return TriggerResult.PURGE;
        }
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {
        ctx.deleteProcessingTimeTimer(window.maxTimestamp());
        ctx.deleteEventTimeTimer(window.maxTimestamp());
    }


    public static InOutTrigger create(int value){
        threshold = value;
        return new InOutTrigger();
    }
}
