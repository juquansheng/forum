package com.uuuuuuuuuuuuuuu.model.es.entity;

import com.uuuuuuuuuuuuuuu.model.annotation.ESID;
import com.uuuuuuuuuuuuuuu.model.annotation.ESMapping;
import com.uuuuuuuuuuuuuuu.model.annotation.ESMetaData;
import com.uuuuuuuuuuuuuuu.model.enums.DataType;
import lombok.Data;

import java.io.Serializable;

@Data
@ESMetaData(indexName = "blob", number_of_shards = 5,number_of_replicas = 0)
public class BlobESData implements Serializable {
    private static final long serialVersionUID = 1L;
    @ESID
    private String id;

    @ESMapping(datatype = DataType.text_type)
    private String title;
    private String content;
    private String desc;
    private String name;
}
