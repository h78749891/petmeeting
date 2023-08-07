package com.petmeeting.springboot.repository;

import com.petmeeting.springboot.domain.BookmarkDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookmarkDogRepository extends JpaRepository<BookmarkDog, Integer> {

    @Query(value = "select exists(select * from bookmark_dog where member_no = :memberNo and dog_no = :dogNo)", nativeQuery = true)
    Integer existsBookmarkDogByMemberNoAndDogNo(Integer memberNo, Integer dogNo);

    @Modifying
    @Query(value = "delete from bookmark_dog where member_no = :memberNo and dog_no = :dogNo", nativeQuery = true)
    Integer deleteBookmarkDogByMemberNoAndDogNo(Integer memberNo, Integer dogNo);

//    List<BookmarkDog> DogRepo 에 작성

}
