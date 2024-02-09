package com.example.carws.controller.users;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.carws.model.token.Token;
import com.example.carws.model.users.Discussions;
import com.example.carws.model.users.Messagerie;
import com.example.carws.model.users.Users;
import com.example.carws.request.InscriptionRequest;
import com.example.carws.service.MessagerieService;
import com.example.carws.service.UsersService;
import com.example.carws.response.*;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    UsersService usersService;
    @Autowired
    MessagerieService messagerieService;

    @GetMapping("/inscription_valide/{date}")
    public ResponseEntity<Response> isInscriptionValid(@PathVariable("date") String date) throws Exception {
        try {
            Users users = new Users();
            users.setDateDeNaissance(date);
            Response response = new Response();
            response.addData("valide", "Inscription valide");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            System.out.println("Erreur: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response().addError("exception", exception.getMessage()));
        }
    }

    @PostMapping("inscription")
    public ResponseEntity<Response> inscription(@RequestBody InscriptionRequest users) throws Exception {
        try {
            usersService.inscription(users.toUser());
            Response response = new Response();
            response.addData("valide", "Le nouveau utilisateur a ete enregistre avec succès");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response().addError("exception", e.getMessage()));
        }
    }

    @PostMapping("login")
    public ResponseEntity<Response> login(@RequestBody Users users) throws Exception {
        try {
            users = usersService.login(users.getId());
            System.out.println("role: " + users.getRoles().size());
            String token = new Token().generateJwt(users);
            Response response = new Response();
            response.addData("token", token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            System.out.println("Erreur login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response().addError("exception", e.getMessage()));
        }
    }

    @PostMapping("authentification")
    public ResponseEntity<Response> authentification(@RequestBody InscriptionRequest users) throws Exception {
        try {
            Users u = usersService.authentification(users.toLoginUser());
            String token = new Token().generateJwt(u);
            Response response = new Response();
            response.addData("token", token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response().addError("exception", e.getMessage()));
        }
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<Response> checkTokenValid(@PathVariable("token") String token) throws Exception {
        try {
            Claims claims = new Token().verify(token);
            Response response = new Response();
            response.addData("user", claims);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            System.out.println("Erreur: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response().addError("exception", exception.getMessage()));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("messagerie")
    public ResponseEntity<Response> nouveauMessage(@RequestBody Messagerie messagerie) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String idEnvoyeur = (String)authentication.getPrincipal();
            messagerie.setIdEnvoyeur(idEnvoyeur);
            messagerieService.nouveauMessage(messagerie);
            Response response = new Response();
            response.addData("valide", "Message envoye");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            System.out.println("Erreur: " + exception.getMessage());
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response().addError("exception", exception.getMessage()));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("discussions")
    public ResponseEntity<Response> discussions(@RequestBody Messagerie messagerie) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String idEnvoyeur = (String)authentication.getPrincipal();
            List<Messagerie> discussions = messagerieService.getDiscussions(idEnvoyeur, messagerie.getIdReceveur());
            Response response = new Response();
            response.addData("discussions", discussions);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            System.out.println("Erreur: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response().addError("exception", exception.getMessage()));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("StatusVu")
    public ResponseEntity<Response> setStatusVu(@RequestBody Messagerie messagerie) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String idEnvoyeur = (String)authentication.getPrincipal();
            messagerieService.setStatus(messagerie.getIdReceveur(), idEnvoyeur, 20, 1);
            Response response = new Response();
            response.addData("status", "Modification effectue");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            System.out.println("Erreur: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response().addError("exception", exception.getMessage()));
        }
    }

    @PostMapping("authentificationAdmin")
    public ResponseEntity<Response> authentificationAdmin(@RequestBody Users users) throws Exception {
        try {
            users = usersService.authentification(users);
            if(!users.isRole("ADMIN"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response().addError("exception", "Acces non autorise."));
            String token = new Token().generateJwt(users);
            Response response = new Response();
            response.addData("token", token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response().addError("exception", e.getMessage()));
        }
    }

    @PostMapping("test")
    public void test(HttpServletRequest request) {
        // String token = this.tokenAuthenticationFilter.getBearerToken(request);
        System.out.println("user: "  + request.isUserInRole("USER"));
        System.out.println("admin: "  + request.isUserInRole("ADMIN"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = (String)authentication.getPrincipal();
        if (authentication != null && authentication.getAuthorities() != null) {
            System.out.println("Roles de l'utilisateur : " + id);
            authentication.getAuthorities().forEach(authority -> {
                System.out.println("==>" + authority.getAuthority());
            });
        } else {
            System.out.println("Aucun rôle trouvé pour cet utilisateur.");
        }
    }

    @PostMapping("/offline-auth")
    public ResponseEntity<?> offlineLogin( @RequestBody InscriptionRequest request ){
        try{
            Users users = request.toLoginUser();
            users = usersService.login(users);
            String token = new Token().generateJwt(users);
            Response response = new Response();
            response.addData("token", token);
            return ResponseEntity.ok().body(response);
        }catch(Exception e){
            return ResponseEntity.badRequest().body( e.getMessage() );
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("listeDiscussions")
    public ResponseEntity<Response> getListeDiscussions() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String idEnvoyeur = (String)authentication.getPrincipal();
            List<Discussions> discussions2 = messagerieService.getListeDiscussions(idEnvoyeur);
            List<Users> users = usersService.getListeUsers(idEnvoyeur, discussions2);
            Response response = new Response();
            response.addData("discussions", discussions2);
            response.addData("users", users);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            System.out.println("Erreur: " + exception.getMessage());
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response().addError("exception", exception.getMessage()));
        }
    }

}