/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sina.sina.dao;

import com.sina.sina.dao.rowmapper.OrderRowMapper;
import com.sina.sina.models.Order;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mphj
 */
@Repository
public class OrderDao extends AbstractDao{

    @Override
    public String getTableName() {
        return "order";
    }
    

    
    public int insert(Order cm) {
        /*jdbcTemplate
                .update("insert into `" + getTableName() + "` values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        cm.getId(),
                        cm.getVid(),
                        cm.getCmid(),
                        cm.getDsid(),
                        cm.getDsVisitedName(),
                        cm.getDsVisitedJob(),
                        cm.getDsVisitedPhone(),
                        cm.getDsIdea(),
                        cm.getDsPopCm(),
                        cm.getDsRival(),
                        cm.getDsIndexDr(),
                        cm.getDrVisitPlace(),
                        cm.getDrVisitPlaceName(),
                        cm.getDrSuggestion(),
                        cm.getCreatedAt(),
                        cm.getCreatedAtAp(),
                        cm.getNextSession(),
                        cm.getPrevSessionId(),
                        cm.getContent(),
                        cm.getResult(),
                        cm.getDesc(),
                        cm.getGivenDocument(),
                        cm.getNeededDocument(),
                        cm.getForwardToVid(),
                        cm.getFromId(),
                        cm.getSubmited(),
                        cm.getSubmitTime(),
                        cm.getViewedAt(),
                        cm.getUrgency());*/
        simpleJdbcInsert.setTableName(getTableName());
        Map<String, Object> map = new HashMap<>();
        map.put("id", cm.getId());
        map.put("vid", cm.getVid());
        map.put("cmid", cm.getCmid());
        map.put("dsid", cm.getDsid());
        map.put("drid", cm.getDrid());
        map.put("ds_visited_name", cm.getDsVisitedName());
        map.put("ds_visited_job", cm.getDsVisitedJob());
        map.put("ds_visited_phone", cm.getDsVisitedPhone());
        map.put("ds_idea", cm.getDsIdea());
        map.put("ds_pop_cm", cm.getDsPopCm());
        map.put("ds_rival", cm.getDsRival());
        map.put("ds_index_dr", cm.getDsIndexDr());
        map.put("dr_visit_place", cm.getDrVisitPlace());
        map.put("dr_visit_place_name", cm.getDrVisitPlaceName());
        map.put("dr_suggestion", cm.getDrSuggestion());
        map.put("created_at", cm.getCreatedAt());
        map.put("created_at_ap", cm.getCreatedAtAp());
        map.put("next_session", cm.getNextSession());
        map.put("prev_session_id", cm.getPrevSessionId());
        map.put("content", cm.getContent());
        map.put("result", cm.getResult());
        map.put("desc", cm.getDesc());
        map.put("given_document", cm.getGivenDocument());
        map.put("needed_document", cm.getNeededDocument());
        map.put("forward_to_vid", cm.getForwardToVid());
        map.put("from_id", cm.getFromId());
        map.put("submited", cm.getSubmited());
        map.put("submit_time", cm.getSubmitTime());
        map.put("viewed_at", cm.getViewedAt());
        map.put("urgency", cm.getUrgency());
        int key =  simpleJdbcInsert.executeAndReturnKey(map).intValue();
        cm.setId(key);
        return key;
    }
    
    
    public Order findById(int id) {
        return (Order) jdbcTemplate
                .queryForObject(
                        "select * from `" + getTableName() + "` where id = ?",
                        new Object[]{id},
                        new OrderRowMapper());
    }
}