package com.petmeeting.springboot.repository;

import com.petmeeting.springboot.domain.Shelter;
import com.petmeeting.springboot.dto.shelter.ShelterSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.petmeeting.springboot.domain.QShelter.shelter;

@Repository
@RequiredArgsConstructor
public class ShelterQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Shelter> findByCondition(ShelterSearchCondition condition) {
        return jpaQueryFactory.selectFrom(shelter)
                .where(shelter.isDeleted.eq(false),
                        containsName(condition.getName()),
                        containsLocation(condition.getLocation()))
                .limit(condition.getMax() == null ? 10 : condition.getMax())
                .offset(condition.getOffset() == null ? 0 : condition.getOffset())
                .fetch();
    }

    private BooleanExpression containsLocation(String location) {
        if (location == null)
            return null;

        return shelter.location.contains(location)
                .or(shelter.location.startsWith(location))
                .or(shelter.location.endsWith(location));
    }

    private BooleanExpression containsName(String name) {
        if (name == null)
            return null; // 두희오빠에게 : 자바컨벤션에서 {} 생략하지 않기로했어요.

        return shelter.name.contains(name)
                .or(shelter.name.startsWith(name))
                .or(shelter.name.endsWith(name));
    }
}
