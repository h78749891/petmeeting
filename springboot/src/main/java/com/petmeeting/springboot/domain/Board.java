package com.petmeeting.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id @GeneratedValue
    @Column(name = "board_no")
    private Integer boardNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @Column(name = "image_path")
    private String imagePath;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<Reply> replyList;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<LikeBoard> likeBoardList;


    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "text", nullable = false)
    private String content;

    @Column(name = "created_time", nullable = false)
    private Integer createdTime;

    @Column(name = "modified_time")
    private Integer modifiedTime;

    @Column(name = "deleted_time")
    private Integer deletedTime;

    @Column(name = "view_cnt", nullable = false)
    @ColumnDefault("0")
    private Integer viewCnt;

    // null일때 0으로 default처리 되야하는데 nullable = false라서 에러나니까 이렇게 적용!
    @PrePersist
    private void prePersist(){
        this.viewCnt = viewCnt == null ? 0 : viewCnt;
    }

}
