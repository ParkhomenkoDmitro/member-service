/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.member;

import com.parkhomenko.common.Constants;
import com.parkhomenko.common.Utils;
import com.parkhomenko.common.Utils.SuccessCreateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dmytro
 */
@RestController
@RequestMapping(value = "/members")
@Api(value = "Member Server", produces = "application/json, application/xml", 
        consumes = "application/json, application/xml")
public class MemberController {

    @Autowired
    private MemberValidator memberValidator;

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "Get list", notes = "View a list of available members", response = MemberDto[].class, 
            consumes = "application/json, application/xml", produces = "application/json, application/xml")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved", response = MemberDto[].class),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource")
    })
    @GetMapping("/get-all-list")
    public List<MemberDto> getAlMembersList() {
        return memberService.getAll();
    }

    @ApiOperation(value = "Get one", notes = "View a one member by id", response = MemberDto.class,
            consumes = "application/json, application/xml", produces = "application/json, application/xml")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved"),
        @ApiResponse(code = 400, message = "Member not found"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getOneMemberById(
            @ApiParam(required = true, name = "id", value = "ID of the member you want to get")
            @PathVariable String id) {
        MemberDto foundMember = memberService.getOneById(id);

        if (StringUtils.isEmpty(foundMember.id)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(foundMember);
    }

    @ApiOperation(value = "Update one", notes = "Update a one member by id", response = Void.class,
            consumes = "application/json, application/xml", produces = "application/json, application/xml")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated"),
        @ApiResponse(code = 400, message = "Data for update member are not valid"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource")
    })
    @PutMapping
    public ResponseEntity<Void> updateOneMemberById(
            @ApiParam(required = true, name = "member", value = "Updated member")
            @RequestBody MemberDto memberDto) {
        if (memberValidator.isAllValidOnUpdate(memberDto) == false) {
            return ResponseEntity.badRequest().build();
        }

        memberService.update(memberDto);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Crete one", notes = "Create a new member", response = SuccessCreateDto.class,
            consumes = "application/json, application/xml", produces = "application/json, application/xml")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully created"),
        @ApiResponse(code = 400, message = "Data for a new member are not valid"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource")
    })
    @PostMapping
    public ResponseEntity<SuccessCreateDto> createOneMemeber(
            @ApiParam(required = true, name = "member", value = "New member")
            @RequestBody MemberDto memberDto) {
        if (memberValidator.isAllValidOnCreate(memberDto) == false) {
            return ResponseEntity.badRequest().build();
        }

        final String memberId = memberService.create(memberDto);
        return Utils.buildCreateResponse(memberId);
    }

    @ApiOperation(value = "Delete many", notes = "Delete many members by id",
            consumes = "application/json, application/xml", produces = "application/json, application/xml")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully deleted"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource")
    })
    @DeleteMapping
    public void deleteMembersByIds(
            @ApiParam(required = true, name = "ids", value = "Array of member's id", 
                    example = "http:8080/app/members?ids=1&ids=2&ids=3")
            @RequestParam(value = Constants.LIST_ID_KEY) List<String> ids) {
        memberService.delete(ids);
    }
}
