/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sina.sina.dao;

import com.sina.sina.dao.rowmapper.OrderRowMapper;
import com.sina.sina.models.Order;
import com.sina.sina.utils.IntegerHelper;
import com.utils.list.ArrayUtils;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mphj
 */
@Repository
@Transactional
public class OrderDao extends AbstractDao {

    @Override
    public String getTableName() {
        return "order_list";
    }

    public void insert(Order cm) {
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
        getSession().save(cm);
    }

    public Order findById(int id) {
        Criteria crit = getSession().createCriteria(Order.class);
        crit.add(Restrictions.eq("id", id));
        return (Order)crit.uniqueResult();
    }

    public List<Order> findByVid(int vid) {
        Criteria crit = getSession().createCriteria(Order.class);
        crit.add(Restrictions.eq("vid", vid));
        return crit.list();
    }

    public List<Order> findForwarded(int vid) {
        Criteria crit = getSession().createCriteria(Order.class);
        crit.add(Restrictions.eq("vid", vid));
        crit.add(Restrictions.isNotNull("forwardToVid"));
        return crit.list();
    }

    public List<Order> findReceivedRequest(Integer vid) {
        Criteria crit = getSession().createCriteria(Order.class);
        crit.add(Restrictions.eq("forwardToVid", vid));
        return crit.list();
    }

    public List<Order> findNextVisit(Integer vid) {
        Criteria crit = getSession().createCriteria(Order.class);
        crit.add(Restrictions.eq("vid", vid));
        crit.add(Restrictions.isNotNull("nextSession"));
        return crit.list();
    }

    public List<Order> findByCm(int cm) {
        return jdbcTemplate.
                query(
                        "select `order_list`.* from `" + getTableName() + "`, `visitor_cm` where order_list.vid = visitor_cm.vid and visitor_cm.cid = ?",
                        new Object[]{cm},
                        new OrderRowMapper());
    }

    public List<Order> findByCmByVisitorByTime(int cm, String visitors, Timestamp from, Timestamp to) {
        int[] vidArray = null;
        if (visitors.contains(",")){
            vidArray = ArrayUtils.toIntArray(visitors.split(","));
        } else{
            vidArray = new int[]{IntegerHelper.parseInt(visitors.trim())};
        }
        return jdbcTemplate.
                query(
                        "select `order_list`.* from`" + getTableName() + "`, `visitor_cm` where order_list.vid = visitor_cm.vid and visitor_cm.cid = ? and order_list.vid in(?) and created_at between ? and ?",
                        new Object[]{cm, vidArray, from, to},
                        new OrderRowMapper());
    }
    
    public List<Order> findByCmsByVisitorByTime(String cmPhone, String cmName, String visitors, Timestamp from, Timestamp to) {
        int[] vidArray = null;
        if (cmPhone == null) cmPhone = "";
        if (cmName == null) cmName = "";
        if (visitors.isEmpty()) {
            if (from != null && to != null) {
                return jdbcTemplate.
                        query(
                                "select `order_list`.* from`" + getTableName() + "` where order_list.cm_phone like ? and order_list.cm_name like ? and order_list.created_at between ? and ?",
                                new Object[]{"%" + cmPhone + "%", "%" + cmName + "%", from, to},
                                new OrderRowMapper());
            }
            if (from == null || to == null) {
                return jdbcTemplate.
                        query(
                                "select `order_list`.* from`" + getTableName() + "` where order_list.cm_phone like ? and order_list.cm_name like ?",
                                new Object[]{"%" + cmPhone + "%", "%" + cmName + "%"},
                                new OrderRowMapper());
            }
        } else {
            if (visitors.contains(",")){
                vidArray = ArrayUtils.toIntArray(visitors.split(","));
            } else{
                vidArray = new int[]{IntegerHelper.parseInt(visitors.trim())};
            }
            if (from != null && to != null) {
                return jdbcTemplate.
                        query(
                                "select `order_list`.* from`" + getTableName() + "` where order_list.cm_phone like ? and order_list.cm_name like ?  and order_list.vid in(?) and order_list.created_at between ? and ?",
                                new Object[]{"%" + cmPhone + "%", "%" + cmName + "%", ArrayUtils.join(vidArray), from, to},
                                new OrderRowMapper());
            }
            if (from == null || to == null) {
                return jdbcTemplate.
                        query(
                                "select `order_list`.* from`" + getTableName() + "` where order_list.cm_phone like ? and order_list.cm_name like ?  and order_list.vid in(?)",
                                new Object[]{"%" + cmPhone + "%", "%" + cmName + "%", ArrayUtils.join(vidArray)},
                                new OrderRowMapper());
            }
        }
        return jdbcTemplate.
                query(
                        "select `order_list`.* from`" + getTableName() + "` where order_list.cm_phone like ? and order_list.cm_name like ?  and order_list.vid in(?) and order_list.created_at between ? and ?",
                        new Object[]{"%" + cmPhone + "%", "%" + cmName + "%", ArrayUtils.join(vidArray), from, to},
                        new OrderRowMapper());
    }

    public List<Order> receivedByCm(int cm) {
        return jdbcTemplate.
                query(
                        "select `order_list`.* from`" + getTableName() + "` where order_list.cmid = ?",
                        new Object[]{cm},
                        new OrderRowMapper());
    }

    public List<Order> findFinishedByCm(int cm) {
        return jdbcTemplate.
                query(
                        "select `order_list`.* from`" + getTableName() + "`, `visitor_cm` where order_list.vid = visitor_cm.vid and visitor_cm.cid = ? and submited = 1",
                        new Object[]{cm},
                        new OrderRowMapper());
    }

    public List<Order> findSeenByDs(int ds) {
        return jdbcTemplate.
                query(
                        "select `order_list`.* from`" + getTableName() + "` where dsid = ? and vid is null and viewed_at is not null",
                        new Object[]{ds},
                        new OrderRowMapper());
    }
    
    
    public List<Order> findByDs(int ds) {
        return jdbcTemplate.
                query(
                        "select `order_list`.* from`" + getTableName() + "` where dsid = ? and vid is null",
                        new Object[]{ds},
                        new OrderRowMapper());
    }

    public List<Order> findFinishedByDs(int ds) {
        return jdbcTemplate.
                query(
                        "select `order_list`.* from`" + getTableName() + "` where dsid = ? and vid is null and submited  = 1",
                        new Object[]{ds},
                        new OrderRowMapper());
    }

    public List<Order> findAllDsOrder() {
        return jdbcTemplate.
                query(
                        "select `order_list`.* from`" + getTableName() + "` where vid is null",
                        new OrderRowMapper());
    }

    public List<Order> findAllDsOrderByCm(int cmid) {
        return jdbcTemplate.
                query(
                        "select `order_list`.* from`" + getTableName() + "` where vid is null and from_id = ?",
                        new Object[]{cmid}, new OrderRowMapper());
    }

    public List<Order> findAll() {
        return jdbcTemplate.
                query(
                        "select `order_list`.* from`" + getTableName() + "`",
                        new OrderRowMapper());
    }


    public void update(Order order) {
        getSession().update(order);
    }
}
