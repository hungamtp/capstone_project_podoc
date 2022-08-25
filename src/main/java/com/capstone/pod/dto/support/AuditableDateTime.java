package com.capstone.pod.dto.support;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableDateTime {
    @JsonFormat(timezone = "Vietnam/Hanoi")
    @CreatedDate
    protected LocalDateTime createDate;
    @JsonFormat(timezone = "Vietnam/Hanoi")
    @LastModifiedDate
    protected LocalDateTime lastModifiedDate;
}

