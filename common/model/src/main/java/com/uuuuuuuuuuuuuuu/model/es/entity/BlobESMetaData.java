package com.uuuuuuuuuuuuuuu.model.es.entity;

import com.uuuuuuuuuuuuuuu.model.annotation.ESID;
import com.uuuuuuuuuuuuuuu.model.annotation.ESMetaData;
import lombok.Data;

import java.io.Serializable;

@Data
@ESMetaData(indexName = "blob", number_of_shards = 5,number_of_replicas = 0)
public class BlobESMetaData implements Serializable {
    private static final long serialVersionUID = 1L;
    @ESID
    private String id;
}
