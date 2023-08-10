package com.petmeeting.springboot.domain;

import com.petmeeting.springboot.dto.user.UserUpdateReqDto;
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
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_group")
public abstract class Users {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Integer id;

    @Column(name = "user_id", unique = true, nullable = false, length = 50)
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "join_date", nullable = false)
    private Long joinDate;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Column(name = "is_activated", nullable = false)
    @ColumnDefault("true")
    private Boolean isActivated;

    @Column(name = "image_path")
    @ColumnDefault("profile1.png")
    private String imagePath;

    @PrePersist
    public void prePersist() {
        this.joinDate = joinDate == 0 ? System.currentTimeMillis() / 1000 : joinDate;
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<LikeBoard> likeBoardList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<LikeReply> likeReplyList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Inquiry> inquiryList;

    public void withdraw() {
        this.isDeleted = true;
    }

    public void updateInfo(UserUpdateReqDto updateReqDto) {
        this.name = updateReqDto.getName() == null ? this.name : updateReqDto.getName();
        this.imagePath = updateReqDto.getImagePath() == null ? this.imagePath : updateReqDto.getImagePath();
        this.phoneNumber = updateReqDto.getPhoneNumber() == null ? this.phoneNumber : updateReqDto.getPhoneNumber();
    }

    public void updateStatus(Boolean isActivated) {
        this.isActivated = isActivated;
    }
}
