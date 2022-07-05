package cdc.vx.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @version V1.0
 * @ClassName: CdcConstant
 * @Description: TODO
 * @Author: xiehp
 * @Date: 2022/7/5 16:54
 */
public class CdcConstant {

    public static List<String> DMP_DIM_TABLES = Arrays.asList(
             "WAREHOUSE_CODE_MAPPING"
            ,""
    );

    public static List<String> WMS_DIM_TABLES = Arrays.asList(
             "md_sku"
            ,"md_client"
            ,"md_package_detail"
            ,"md_location"
            ,"md_code_dict"
    );

}
