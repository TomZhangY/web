package com.tom.web.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author: tom
 * @Description: 基础类
 * @Date: Created in 2:17 PM 12/17/2018
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<T extends BaseEntity> implements Serializable, Comparable<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @JsonIgnore
    @Column(name = "create_time")
    private Timestamp createTime;

    @LastModifiedDate
    @JsonIgnore
    @Column(name = "update_time")
    private Timestamp updateTime;

    @Override
    public int compareTo(T obj) {
        if (obj == null) {
            return -1;
        }
        if (this.equals(obj)) {
            return 0;
        }

        // 按照类名正序排列
        if (!obj.getClass().getName().equals(getClass().getName())) {
            return getClass().getName().hashCode() - obj.getClass().getName().hashCode();
        }

        T o = (T) obj;

        // ID为Null的对象放在前
        if (o.getId() == null) {
            if (getId() != null) {
                return 1;
            }
        }
        if (o.getId() != null) {
            if (getId() == null) {
                return -1;
            }
        }
        // 比较的对象ID都为NULL时，根据hashcode倒序排列
        if (o.getId() == null && getId() == null) {
            return o.hashCode() - hashCode();
        }

        // ID都不为NULL时，根据id倒序排列
        return o.getId().intValue() - getId().intValue();
    }
}