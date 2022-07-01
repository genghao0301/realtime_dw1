package cdc.vx.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 包装明细表
 * //@TableName md_package_detail
 */
//@TableName(value ="md_package_detail")
@Data
public class DimMdPackageDetail implements Serializable {
    /**
     * 主键ID
     */
    //@TableId(value = "md_package_detail_id", type = IdType.AUTO)
    private String md_package_detail_id;

    /**
     * 仓库ID
     */
    //@TableField(value = "warehouse_id")
    private Long warehouse_id;

    /**
     * 仓库编码
     */
    //@TableField(value = "warehouse_code")
    private String warehouse_code;

    /**
     * 包装编码
     */
    //@TableField(value = "package_code")
    private String package_code;

    /**
     * 实例名称
     */
    //@TableField(value = "instance_name")
    private String instance_name;

    /**
     * 最基本计量单位标识
     */
    //@TableField(value = "is_basic_uom")
    private Integer is_basic_uom;

    /**
     * 单位
     */
    //@TableField(value = "uom")
    private String uom;

    /**
     * 单位名称
     */
    //@TableField(value = "uom_name")
    private String uom_name;

    /**
     * 外部单位
     */
    //@TableField(value = "extern_uom")
    private String extern_uom;

    /**
     * 任务切分标志
     */
    //@TableField(value = "task_split_flag")
    private Integer task_split_flag;

    /**
     * 转换系数
     */
    //@TableField(value = "ratio")
    private String ratio;

    /**
     * 高
     */
    //@TableField(value = "height")
    private BigDecimal height;

    /**
     * 长
     */
    //@TableField(value = "length")
    private BigDecimal length;

    /**
     * 宽
     */
    //@TableField(value = "width")
    private BigDecimal width;

    /**
     * 均体积
     */
    //@TableField(value = "volume")
    private BigDecimal volume;

    /**
     * 均净重
     */
    //@TableField(value = "net_weight")
    private BigDecimal net_weight;

    /**
     * 均皮重
     */
    //@TableField(value = "weight")
    private BigDecimal weight;

    /**
     * 均毛重
     */
    //@TableField(value = "gross_weight")
    private BigDecimal gross_weight;

    /**
     * 是否有效。0-无效  1-有效。
     */
    //@TableField(value = "is_active")
    private Integer is_active;

    /**
     * 备注
     */
    //@TableField(value = "remark")
    private String remark;

    /**
     * 用户自定义字段1
     */
    //@TableField(value = "user_def1")
    private String user_def1;

    /**
     * 用户自定义字段2
     */
    //@TableField(value = "user_def2")
    private String user_def2;

    /**
     * 用户自定义字段3
     */
    //@TableField(value = "user_def3")
    private String user_def3;

    /**
     * 用户自定义字段4
     */
    //@TableField(value = "user_def4")
    private String user_def4;

    /**
     * 用户自定义字段5
     */
    //@TableField(value = "user_def5")
    private String user_def5;

    /**
     * 用户自定义字段6
     */
    //@TableField(value = "user_def6")
    private String user_def6;

    /**
     * 用户自定义字段7
     */
    //@TableField(value = "user_def7")
    private String user_def7;

    /**
     * 用户自定义字段8
     */
    //@TableField(value = "user_def8")
    private String user_def8;

    /**
     * 用户自定义字段9
     */
    //@TableField(value = "user_def9")
    private BigDecimal user_def9;

    /**
     * 用户自定义字段10
     */
    //@TableField(value = "user_def10")
    private BigDecimal user_def10;

    /**
     * 创建用户ID
     */
    //@TableField(value = "created_by_user_id")
    private Long created_by_user_id;

    /**
     * 创建用户
     */
    //@TableField(value = "created_by_user")
    private String created_by_user;

    /**
     * 创建地点
     */
    //@TableField(value = "created_office")
    private String created_office;

    /**
     * 创建时间
     */
    //@TableField(value = "created_dtm_loc")
    private String created_dtm_loc;

    /**
     * 创建时区
     */
    //@TableField(value = "created_time_zone")
    private String created_time_zone;

    /**
     * 更新用户ID
     */
    //@TableField(value = "updated_by_user_id")
    private Long updated_by_user_id;

    /**
     * 更新用户
     */
    //@TableField(value = "updated_by_user")
    private String updated_by_user;

    /**
     * 更新地点
     */
    //@TableField(value = "updated_office")
    private String updated_office;

    /**
     * 更新时间
     */
    //@TableField(value = "updated_dtm_loc")
    private String updated_dtm_loc;

    /**
     * 更新时区
     */
    //@TableField(value = "updated_time_zone")
    private String updated_time_zone;

    /**
     * 记录版本
     */
    //@TableField(value = "record_version")
    private Long record_version;

    /**
     * 是否逻辑删除[0-删除, 1-正常]
     */
    //@TableField(value = "valid")
    private Integer valid;

    //@TableField(exist = false)
    private static final long serialVersionUID = 1L;
}