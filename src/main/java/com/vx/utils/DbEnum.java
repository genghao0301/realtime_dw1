package com.vx.utils;

public enum DbEnum {

    //WMS2
    WMS_CQKG ("wms_cqkg" ,""),
    WMS_FBHD ("wms_fbhd" ,"WH_FZBHD"),
    WMS_FSSS ("wms_fsss" ,"WH_FSSS"),
    WMS_GXNN ("wms_gxnn" ,"WH_NNLQ"),
    WMS_GZLX ("wms_gzlx" ,"WH_GZXT"),
    WMS_KMGX ("wms_kmgx" ,"WH_KMGX"),
    WMS_NCJM ("wms_ncjm" ,"WH_NCCB"),
    WMS_SYWC ("wms_sywc" ,"WH_SYHN"),
    WMS_SZWS ("wms_szws" ,"WH_SZWS"),
    WMS_SZYT ("wms_szyt" ,""),
    WMS_TJBC ("wms_tjbc" ,""),
    WMS_WANJI("wms_wanji","WH_SHNQ"),
    WMS_XMHC ("wms_xmhc" ,"WH_XMHC"),
    // WMS3
    WMS_BJYZ("wms_bjyz","WH_BJYZ"),
    WMS_GZPY("wms_gzpy",""),
    WMS_JNGX("wms_jngx","WH_JNGX"),
    WMS_NBBL("wms_nbbl","WH_NBBL"),
    WMS_NJVX("wms_njvx","WH_NJQX"),
    WMS_NNXZ("wms_nnxz","WH_NNXZ"),
    WMS_SHXB("wms_shxb","WH_SHXB"),
    WMS_SHXQ("wms_shxq","WH_SHXQ"),
    // WMS4
    WMS_CDLQ("wms_cdlq","WH_CDLQ"),
    WMS_CDXJ("wms_cdxj","WH_CDXJ"),
    WMS_HZDT("wms_hzdt","WH_HZDT"),
    WMS_JXPH("wms_jxph","WH_JXPH"),
    WMS_QZJJ("wms_qzjj","WH_QZJJ"),
    WMS_SHHG("wms_shhg","WH_SHHG"),
    WMS_SZWT("wms_szwt","WH_SZWT"),
    WMS_XAGX("wms_xagx","WH_XAGX"),
    // WMS5
    WMS_CSKF  ("wms_cskf", "WH_CSKF"),
    WMS_GZHP  ("wms_gzhp", "WH_GZHP"),
    WMS_HFWJ  ("wms_hfwj", ""),
    WMS_JXXZ  ("wms_jxxz", ""),
    WMS_LFJK  ("wms_lfjk", "WH_LFJK"),
    WMS_SHLG  ("wms_shlg", ""),
    WMS_SYSL  ("wms_sysl", "WH_SYYH"),
    WMS_SZHY  ("wms_szhy", "WH_SZHY"),
    WMS_TJDJG ("wms_tjdjg", "WH_TJDJG"),
    WMS_WHDXH ("wms_whdxh", "WH_WHDXH"),

    WMS_SZWT2("wms_szwt","WH_SZWT")
    ;

    String dbName;
    String wh_code;

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

    DbEnum(String dbName, String wh_code) {
        this.dbName = dbName;
        this.wh_code = wh_code;
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
}
