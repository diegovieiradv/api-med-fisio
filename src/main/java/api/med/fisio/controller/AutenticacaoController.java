package api.med.fisio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import api.med.fisio.domain.usuario.Usuario;
import api.med.fisio.domain.usuario.UsuarioRepository;
import api.med.fisio.domain.usuario.DadosCadastroUsuario;
import api.med.fisio.infra.security.DadosTokenJWT;
import api.med.fisio.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping
@Tag(name = "Autenticacao", description = "Endpoints de autenticacao e cadastro de usuarios")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Efetuar login", description = "Autentica o usuario e retorna um token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso, token JWT retornado"),
        @ApiResponse(responseCode = "400", description = "Dados de login invalidos"),
        @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
    })
    public ResponseEntity efetuarLogin(@RequestBody @Valid api.med.fisio.domain.usuario.DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

    @PostMapping("/usuarios")
    @Operation(summary = "Cadastrar usuario", description = "Cadastra um novo usuario no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados invalidos"),
        @ApiResponse(responseCode = "409", description = "Login ja existe")
    })
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        var usuarioExistente = usuarioRepository.findByLogin(dados.login());
        if (usuarioExistente != null) {
            return ResponseEntity.badRequest().body("Login ja cadastrado");
        }

        var usuario = new Usuario();
        usuario.setLogin(dados.login());
        usuario.setSenha(passwordEncoder.encode(dados.senha()));

        usuarioRepository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
