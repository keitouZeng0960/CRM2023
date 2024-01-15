package com.workspace.crm.settings.service.impl;

import com.workspace.crm.settings.mapper.DictionaryValueMapper;
import com.workspace.crm.settings.pojo.DictionaryValue;
import com.workspace.crm.settings.service.DictionaryValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictionaryValueServiceImpl implements DictionaryValueService {
    @Autowired
    DictionaryValueMapper mapper;
    @Override
    public List<DictionaryValue> queryAllDicValueByPages(int beginNo, int pageSize) {
        Map<String,Object> map=new HashMap<>();
        map.put("beginNo",beginNo);
        map.put("pageSize",pageSize);
        return mapper.selectAllValueByPage(map);
    }

    @Override
    public int queryCountOfValues() {
        return mapper.selectCountOfValues();
    }

    @Override
    public int deleteValueByIds(String[] id) {
        return mapper.deleteByIds(id);
    }

    @Override
    public int editValueById(DictionaryValue value) {
        return mapper.updateById(value);
    }

    @Override
    public int saveValue(DictionaryValue value) {
        return mapper.insertData(value);
    }

    @Override
    public DictionaryValue queryValueById(String id) {
        return mapper.selectValueById(id);
    }

    @Override
    public List<DictionaryValue> queryValueByType(String type) {
        return mapper.selectValueByType(type);
    }
}
