package com.petmeeting.springboot.repository;

import com.petmeeting.springboot.domain.Dog;
import com.petmeeting.springboot.dto.dog.DogSearchCondition;
import com.petmeeting.springboot.enums.AdoptionAvailability;
import com.petmeeting.springboot.enums.DogSize;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.petmeeting.springboot.domain.QDog.dog;

@Repository
@RequiredArgsConstructor
public class DogQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Dog> findByCondition(DogSearchCondition condition) {
        Integer shelterNo = condition.getShelterNo();

        if(shelterNo == null || shelterNo == 0) {
            return jpaQueryFactory.selectFrom(dog)
                    .where(dog.isDeleted.eq(false),
                            containsName(condition.getName()),
                            sameDogSize(condition.getDogSize()),
                            notContainsShelter(),
                            isAdoptable(dog.adoptionAvailability))
                    .limit((condition.getMax() == null || condition.getMax() == 0) ? 10 : condition.getMax())
                    .offset((condition.getOffset() == null || condition.getOffset() == 0) ? 0 : condition.getOffset()) // 0이 아니라 null이여야 정상작동
                    .orderBy(dog.dogNo.desc())
                    .fetch();
        }

        return jpaQueryFactory.selectFrom(dog)
                .where(dog.isDeleted.eq(false),
                        containsName(condition.getName()),
                        sameDogSize(condition.getDogSize()),
                        containsShelter(condition.getShelterNo()))
                .limit((condition.getMax() == null || condition.getMax() == 0) ? 10 : condition.getMax())
                .offset((condition.getOffset() == null || condition.getOffset() == 0) ? 0 : condition.getOffset())
                .orderBy(dog.dogNo.desc())
                .fetch();
    }

    private BooleanExpression isAdoptable(EnumPath<AdoptionAvailability> adoptionAvailability) {
        return adoptionAvailability.eq(AdoptionAvailability.ADOPT_POSSIBLE);
    }

    private BooleanExpression containsName(String name) {
        if(name == null)
            return null;

        return dog.name.contains(name)
                .or(dog.name.startsWith(name))
                .or(dog.name.endsWith(name));
    }

    private BooleanExpression sameDogSize(String size) {
        if(size == null)
            return null;

        return dog.dogSize.eq(DogSize.getSize(size));
    }

    /**
     * 보호소 번호를 함께 조회하면 입양상태와 무관하게
     * 보호소가 등록했던 모든 유기견 중 현재 조건에 맞는(이름, 사이즈) 리스트가 조회된다.
     * @param shelterNo
     * @return
     */
       private BooleanExpression containsShelter(Integer shelterNo){
        return dog.shelter.id.eq(shelterNo);
       }

    /**
     * 보호소 번호 없이 조회하면
     * 현재 조건에 맞는 리스트 중 입양가능(ADOPT_POSSIBLE)한 리스트만 조회된다.
     * @return
     */
    private BooleanExpression notContainsShelter(){
        return dog.adoptionAvailability.eq(AdoptionAvailability.ADOPT_POSSIBLE);
       }
}
