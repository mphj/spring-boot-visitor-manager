/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sina.sina.pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.utils.interfaces.Parsable;

/**
 *
 * @author mphj
 */
public class Drugs_1 implements Parsable<JsonNode>{
    public int drug;
    public int num;
    public String visit;

    @Override
    public void parse(JsonNode content) {
        drug = content.get("drug").asInt();
        num = content.get("num").asInt();
        visit = content.get("visit").asText();
    }
}
