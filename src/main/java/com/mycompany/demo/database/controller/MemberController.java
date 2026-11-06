package com.mycompany.demo.database.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.demo.database.dto.JoinMemberRequest;
import com.mycompany.demo.database.dto.MemberResponse;
import com.mycompany.demo.database.dto.Pager;
import com.mycompany.demo.database.dto.UpdateMemberRequest;
import com.mycompany.demo.database.entity.Member;
import com.mycompany.demo.database.service.MemberService;
import com.mycompany.demo.exception.NoAccountException;

import lombok.extern.slf4j.Slf4j;


@RestController 
@RequestMapping("/database")
@Slf4j 
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/get-member")
    public MemberResponse getMember(@RequestParam("memberId") String memberId) throws SQLException {
        if(true) {
            //throw new RuntimeException("실행 예외 발생");
            //throw new SQLException("DB에서 예외 발생");
            //throw new NullPointerException("NullPointerException 발생");
            //throw new DuplicatedMemberIDException("이미 아이디가 존재함");
            //throw new NoAccountException("계좌가 존재하지 않음");
        }
        
        Member member = memberService.getMember(memberId);
        MemberResponse memberResponse = new MemberResponse(
            member.getMemberId(),
            member.getMemberName(),
            member.getEmail(),
            member.getJoinedAt()
        );
        return memberResponse;
    }

    @PostMapping("/join-member")
    public MemberResponse joinMember(@RequestBody JoinMemberRequest joinMemberRequest) {
        Member member = new Member();
        member.setMemberId(joinMemberRequest.getMemberId());
        member.setMemberName(joinMemberRequest.getMemberName());
        member.setPassword(joinMemberRequest.getPassword());
        member.setEmail(joinMemberRequest.getEmail());
        memberService.join(member);

        Member dbMember = memberService.getMember(joinMemberRequest.getMemberId());
        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setMemberId(dbMember.getMemberId());
        memberResponse.setMemberName(dbMember.getMemberName());
        memberResponse.setEmail(dbMember.getEmail());
        memberResponse.setJoinedAt(dbMember.getJoinedAt());
        return memberResponse;
    } 

    @PutMapping("/update-member")
    public Map<String, Object> updateMember(@RequestBody UpdateMemberRequest updateMemberRequest) {
        Member member = new Member();
        member.setMemberId(updateMemberRequest.getMemberId());
        member.setPassword(updateMemberRequest.getPassword());
        member.setEmail(updateMemberRequest.getEmail());      
        int rows = memberService.updateMember(member);
        if(rows == 1) {
            // 수정 성공
            Member dbMember = memberService.getMember(updateMemberRequest.getMemberId());
            MemberResponse memberResponse = new MemberResponse();
            memberResponse.setMemberId(dbMember.getMemberId());
            memberResponse.setMemberName(dbMember.getMemberName());
            memberResponse.setEmail(dbMember.getEmail());
            memberResponse.setJoinedAt(dbMember.getJoinedAt());
            Map<String, Object> map = new HashMap<>();
            map.put("status", "success");
            map.put("data", memberResponse);
            return map;
        } else {
            // 수정 실패
            Map<String, Object> map = new HashMap<>();
            map.put("status", "fail");
            return map;
        }
    }

    @DeleteMapping("/delete-member")
    public Map<String, Object> deleteMember(@RequestParam("memberId") String memberId) {
        int rows = memberService.deleteMember(memberId);
        if(rows == 1) {
            // 삭제 성공
            Map<String, Object> map = new HashMap<>();
            map.put("status", "success");
            return map;
        } else {
            // 삭제 실패
            Map<String, Object> map = new HashMap<>();
            map.put("status", "fail");
            return map;
        }
    }

    @GetMapping("/list-member")
    public Map<String, Object> listMember(@RequestParam(value="pageNo", defaultValue = "1") int pageNo) {
        Map<String, Object> where = null;
        int totalRows = memberService.pagingTotalRows(where);
        Pager pager = new Pager(10, 5, totalRows, pageNo);
        List<Member> list = memberService.listMember(pager, where);
        List<MemberResponse> listMemberResponse = new ArrayList<>();
        for(Member dbMember: list) {
            MemberResponse memberResponse = new MemberResponse();
            memberResponse.setMemberId(dbMember.getMemberId());
            memberResponse.setMemberName(dbMember.getMemberName());
            memberResponse.setEmail(dbMember.getEmail());
            memberResponse.setJoinedAt(dbMember.getJoinedAt());
            listMemberResponse.add(memberResponse);
        } 
        Map<String, Object> map = Map.of(
            "pager", pager,
            "data", listMemberResponse
        );
        return map;
    }
}
