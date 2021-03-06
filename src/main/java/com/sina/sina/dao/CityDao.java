/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sina.sina.dao;

import com.sina.sina.dao.rowmapper.CityRowMapper;
import com.sina.sina.models.City;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mphj
 */
@Repository
@Transactional
public class CityDao extends AbstractDao {

    public void insert(City city) {
        /*jdbcTemplate
                .update("insert into `" + getTableName() + "` values(?, ?, ?)",
                        city.getId(),
                        city.getName(),
                        city.getParent());*/
        getSession().save(city);
    }

    public City findById(int id) {
        return (City) jdbcTemplate
                .queryForObject(
                        "select * from `" + getTableName() + "` where id = ?",
                        new Object[]{id},
                        new CityRowMapper());
    }

    public List<City> findByParent(Integer parentId) {
        return jdbcTemplate
                .query(
                        "select * from `" + getTableName() + "` where parent = ?",
                        new Object[]{parentId},
                        new CityRowMapper());
    }

    public List<City> findWithoutParent() {
        return jdbcTemplate
                .query(
                        "select * from `" + getTableName() + "` where parent is null",
                        new CityRowMapper());
    }
    
    
    public List<City> findAll() {
        return jdbcTemplate
                .query(
                        "select * from `" + getTableName() + "`",
                        new CityRowMapper());
    }

    @Override
    public String getTableName() {
        return "city";
    }
}
