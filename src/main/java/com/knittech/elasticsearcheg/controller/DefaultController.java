/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.knittech.elasticsearcheg.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Sumit
 */
@Controller
@RequestMapping("/")
public class DefaultController {

    RestHighLevelClient client;

    public DefaultController() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
    } 
    
    @PostConstruct
    public void createIndex(){
        CreateIndexRequest request = new CreateIndexRequest("book");
        try {
            CreateIndexResponse createIndexResponse = client.indices().create(request);
        } catch (IOException ex) {
            Logger.getLogger(DefaultController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @PreDestroy
    public void deleteIndex(){
        DeleteIndexRequest request = new DeleteIndexRequest("book");
        try {
            DeleteIndexResponse deleteIndexResponse = client.indices().delete(request);
        } catch (IOException ex) {
            Logger.getLogger(DefaultController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(path = "/populate", method = RequestMethod.POST)
    @ResponseBody
    public String populateSingleData() throws IOException {
        IndexRequest request = new IndexRequest("book", "grade", "1");
        request.source("name", "GK",
                "numbers", 10,
                "grade", "KG");
        IndexResponse response = client.index(request);
        return response.getResult().toString();
    }

    @RequestMapping(path = "/populatebulk", method = RequestMethod.POST)
    @ResponseBody
    public String populateBulkData() throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest("book", "grade", "2").source("name", "Symphony",
                                                                  "numbers", 10,
                                                                  "grade", "One"));
        request.add(new IndexRequest("book", "grade", "3").source("name", "Science",
                                                                  "numbers", 10,
                                                                  "grade", "Two"));
        request.add(new IndexRequest("book", "grade", "4").source("name", "Symphony",
                                                                  "numbers", 10,
                                                                  "grade", "Two"));
        BulkResponse response = client.bulk(request);
        return response.toString();
    }

    @RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getById(@PathVariable String id) throws IOException {
        GetRequest request = new GetRequest("book", "grade", id);
        GetResponse response = client.get(request);
        return response.getSourceAsString();
    }

    @RequestMapping(path = "/get", method = RequestMethod.GET)
    @ResponseBody
    public String getAll() throws IOException {
        SearchRequest request = new SearchRequest("book");
        SearchResponse response = client.search(request);
        return response.toString();
    }

    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteById(@PathVariable("id") String id) throws IOException {
        DeleteResponse response = client.delete(new DeleteRequest("book", "grade", id));
        return response.getResult().toString();
    }

    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String updateById(@PathVariable("id") String id) throws IOException {
        UpdateRequest request = new UpdateRequest("book", "grade", id).doc("section", "A7");
        UpdateResponse response = client.update(request);
        return response.getResult().toString();
    }

}
