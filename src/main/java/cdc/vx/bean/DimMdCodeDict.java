package cdc.vx.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 数据字典数值表
 * //@TableName md_code_dict
 */
//@TableName(value ="md_code_dict")
@Data
public class DimMdCodeDict implements Serializable {
    /**
     * 数据字典值表流水号
     */
    //@TableId(value = "md_code_dict_id", type = IdType.AUTO)
    private Long md_code_dict_id;

    /**
     * 类型代码
     */
    //@TableField(value = "type_code")
    private String type_code;

    /**
     * 
     */
    //@TableField(value = "code_value")
    private String code_value;

    /**
     * 数据字典名称
     */
    //@TableField(value = "code_name")
    private String code_name;

    /**
     * 默认是否可编辑，默认可编辑
     */
    //@TableField(value = "enabled")
    private Integer enabled;

    /**
     * 仓库ID
     */
    //@TableField(value = "warehouse_id")
    private Long warehouse_id;

    /**
     * 仓库代码
     */
    //@TableField(value = "warehouse_code")
    private String warehouse_code;

    /**
     * 语言代码
     */
    //@TableField(value = "language_code")
    private String language_code;

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

    //@TableField(exist = false)
    private static final long serialVersionUID = 1L;
}