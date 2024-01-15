package com.workspace.crm.settings.service;

import com.workspace.crm.settings.pojo.DictionaryValue;

import java.util.List;

public interface DictionaryValueService {
    //分页查询值
    List<DictionaryValue> queryAllDicValueByPages(int beginNo, int pageSize);

    //查询记录条数
    int queryCountOfValues();

    //删除数据
    int deleteValueByIds(String[] id);

    //更新数据
    int editValueById(DictionaryValue value);
    //创建数据
    int saveValue(DictionaryValue value);

    DictionaryValue queryValueById(String id);


    List<DictionaryValue> queryValueByType(String type);
}
