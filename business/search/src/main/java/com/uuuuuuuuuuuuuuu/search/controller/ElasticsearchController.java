package com.uuuuuuuuuuuuuuu.search.controller;

import com.uuuuuuuuuuuuuuu.model.es.entity.BlobESMetaData;
import com.uuuuuuuuuuuuuuu.search.service.ElasticsearchIndex;
import com.uuuuuuuuuuuuuuu.search.service.ElasticsearchTemplate;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: ElasticsearchController
 * date: 2021/1/12 15:41
 * author: juquansheng
 * version: 1.0 <br>
 */
@RestController
@RequestMapping(value = "es")
public class ElasticsearchController {

    @Autowired
    private ElasticsearchIndex elasticsearchIndex;
    @Autowired
    private ElasticsearchTemplate<BlobESMetaData,String> elasticsearchTemplate;

    public void test() throws Exception {
        BlobESMetaData blobESMetaData = new BlobESMetaData();
        blobESMetaData.setId("1");
        elasticsearchTemplate.save(blobESMetaData);
    }

}
