package com.petmeeting.springboot.domain;

import com.petmeeting.springboot.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@DynamicInsert
@DiscriminatorColumn(name = "MEMBER", length = 10)
public class Member extends Users {

    @Transient
    private Role userGroup = Role.ROLE_MEMBER;

    @Transient
    private Integer holdingPoint;

    @Column(name = "holding_token", nullable = false)
    @ColumnDefault("0")
    private Integer holdingToken;

    @Column(name = "adopted", nullable = false)
    @ColumnDefault("false")
    private Boolean adopted = false;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Board> boardList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<LikeDog> likeDogList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<BookmarkDog> bookmarkDogList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Adoption> adoptionList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Charge> chargeList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Donation> donationList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Chat> chatList;

    // user_no 라서 변수 Users
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Reply> replyList;


    /**
     * holdingPoint(충전금액 합계와 후원금액 합계의 차이)를 설정합니다.
     * 작업 필요
     */
    @PostLoad
    public void setHoldingPoint() {
        this.holdingPoint = 0;
    }
}
