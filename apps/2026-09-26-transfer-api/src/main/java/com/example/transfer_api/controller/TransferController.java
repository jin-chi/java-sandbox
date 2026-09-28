package com.example.transfer_api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transfer_api.dto.TransferRequestDto;
import com.example.transfer_api.dto.TransferResponseDto;
import com.example.transfer_api.service.TransferService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/transfers")
@AllArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponseDto> getTransfer(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.getTransfer(id));
    }

    @PostMapping
    public ResponseEntity<TransferResponseDto> transfer(@RequestBody @Valid  TransferRequestDto req) {
        TransferResponseDto res = transferService.transfer(req);
        return ResponseEntity.created(URI.create("/transfers/" + res.id())).body(res);
    }
}
