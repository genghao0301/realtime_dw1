package com.vx.utils;

public enum DbEnum {

    //WMS2
    WMS_CQKG ("wms_cqkg" ,"", ""),
    WMS_FBHD ("wms_fbhd" ,"WH_FZBHD", "BHD_FZ"),
    WMS_FSSS ("wms_fsss" ,"WH_FSSS", "FOS_SS"),
    WMS_GXNN ("wms_gxnn" ,"WH_NNLQ", "GXI_NN"),
    WMS_GZLX ("wms_gzlx" ,"WH_GZXT", "GZH_LX"),
    WMS_KMGX ("wms_kmgx" ,"WH_KMGX", "KUM_GX"),
    WMS_NCJM ("wms_ncjm" ,"WH_NCCB", "NCH_JM"),
    WMS_SYWC ("wms_sywc" ,"WH_SYHN", "SHY_WC"),
    WMS_SZWS ("wms_szws" ,"WH_SZWS", "SHZ_WS"),
    WMS_SZYT ("wms_szyt" ,"", ""),
    WMS_TJBC ("wms_tjbc" ,"", ""),
    WMS_WANJI("wms_wanji","WH_SHNQ", "SHA_WJ"),
    WMS_XMHC ("wms_xmhc" ,"WH_XMHC", "XIM_HC"),
    // WMS3
    WMS_BJYZ("wms_bjyz","WH_BJYZ", "BJN_YZ"),
    WMS_GZPY("wms_gzpy","", ""),
    WMS_JNGX("wms_jngx","WH_JNGX", "JIN_GX"),
    WMS_NBBL("wms_nbbl","WH_NBBL", "ZHJ_NB"),
    WMS_NJVX("wms_njvx","WH_NJQX", "NAJ_VX"),
    WMS_NNXZ("wms_nnxz","WH_NNXZ", "GXI_XZ"),
    WMS_SHXB("wms_shxb","WH_SHXB", "SHA_XB"),
    WMS_SHXQ("wms_shxq","WH_SHXQ", "SHA_XQ"),
    // WMS4
    WMS_CDLQ("wms_cdlq","WH_CDLQ", "CHD_LQ"),
    WMS_CDXJ("wms_cdxj","WH_CDXJ", "CHD_XJ"),
    WMS_HZDT("wms_hzdt","WH_HZDT", "HZH_DT"),
    WMS_JXPH("wms_jxph","WH_JXPH", "JXI_PH"),
    WMS_QZJJ("wms_qzjj","WH_QZJJ", "QZH_JJ"),
    WMS_SHHG("wms_shhg","WH_SHHG", "SHH_HG"),
    WMS_SZWT("wms_szwt","WH_SZWT", "SHZ_WT"),
    WMS_XAGX("wms_xagx","WH_XAGX", "XIA_GX"),
    // WMS5
    WMS_CSKF  ("wms_cskf", "WH_CSKF", "CHS_KF"),
    WMS_GZHP  ("wms_gzhp", "WH_GZHP", "GZH_HP"),
    WMS_HFWJ  ("wms_hfwj", "", ""),
    WMS_JXXZ  ("wms_jxxz", "", ""),
    WMS_LFJK  ("wms_lfjk", "WH_LFJK", "LAF_JK"),
    WMS_SHLG  ("wms_shlg", "", ""),
    WMS_SYSL  ("wms_sysl", "WH_SYYH", "SHY_YH"),
    WMS_SZHY  ("wms_szhy", "WH_SZHY", "SHZ_HY"),
    WMS_TJDJG ("wms_tjdjg", "WH_TJDJG", "DJG_TJ"),
    WMS_WHDXH ("wms_whdxh", "WH_WHDXH", "DXH_WH"),

    WMS_SZWT2("wms_szwt","WH_SZWT", "")
    ;

    String dbName;
    String wh_code;
    String wms_wh_code;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getWh_code() {
        return wh_code;
    }

    public void setWh_code(String wh_code) {
        this.wh_code = wh_code;
    }

    public String getWms_wh_code() {
        return wms_wh_code;
    }

    public void setWms_wh_code(String wms_wh_code) {
        this.wms_wh_code = wms_wh_code;
    }

    DbEnum(String dbName, String wh_code, String wms_wh_code) {
        this.dbName = dbName;
        this.wh_code = wh_code;
        this.wms_wh_code = wms_wh_code;
    }

    public static DbEnum getResultInfoEnumByDb(String dbName) {
        for (DbEnum resEnum : DbEnum.values()) {
            if (dbName.equals(resEnum.getDbName())) {
                return resEnum;
            }
        }
        return null;
    }

    public static String getWhCodeEnumByDb(String dbName) {
        for (DbEnum resEnum : DbEnum.values()) {
            if (dbName.equals(resEnum.getDbName())) {
                return resEnum.getWh_code();
            }
        }
        return null;
    }

    public static String getWmsWhCodeEnumByDb(String dbName) {
        for (DbEnum resEnum : DbEnum.values()) {
            if (dbName.equals(resEnum.getDbName())) {
                return resEnum.getWms_wh_code();
            }
        }
        return null;
    }
}
