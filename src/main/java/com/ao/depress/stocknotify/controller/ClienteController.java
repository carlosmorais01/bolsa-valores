package com.ao.depress.stocknotify.controller;

import com.ao.depress.stocknotify.dto.BolsaResponse;
import com.ao.depress.stocknotify.dto.ClienteRequest;
import com.ao.depress.stocknotify.dto.ClienteResponse;
import com.ao.depress.stocknotify.dto.NotificacaoResponse;
import com.ao.depress.stocknotify.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Cadastro de clientes e consulta das notificações recebidas")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @Operation(
            summary = "Cadastra um cliente",
            description = "Cria o cliente e o inscreve automaticamente em todas as bolsas registradas, para que passe a receber notificações de mercado.")
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado")
    public ResponseEntity<ClienteResponse> cadastrar(@RequestBody ClienteRequest request) {
        var cliente = clienteService.cadastrar(request.nome(), request.premium());
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponse.de(cliente));
    }

    @GetMapping
    @Operation(summary = "Lista todos os clientes cadastrados")
    public List<ClienteResponse> listar() {
        return clienteService.listar().stream().map(ClienteResponse::de).toList();
    }

    @GetMapping("/{id}/notificacoes")
    @Operation(
            summary = "Consulta as notificações recebidas por um cliente",
            description = "Representa a notificação efetivamente 'enviada' ao cliente: cada evento de mercado que a política vigente decidiu repassar a ele.")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public List<NotificacaoResponse> notificacoes(
            @Parameter(description = "Identificador do cliente") @PathVariable UUID id) {
        return clienteService.consultarNotificacoes(id).stream().map(NotificacaoResponse::de).toList();
    }

    @GetMapping("/{id}/bolsas")
    @Operation(
            summary = "Lista as bolsas em que o cliente está inscrito",
            description = "Útil para saber o id a usar no DELETE de desinscrição, ou confirmar que uma inscrição/desinscrição teve efeito.")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public List<BolsaResponse> bolsas(
            @Parameter(description = "Identificador do cliente") @PathVariable UUID id) {
        return clienteService.listarBolsas(id).stream()
                .map(e -> new BolsaResponse(e.getValue().getId(), e.getKey()))
                .toList();
    }
}
