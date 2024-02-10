package com.example.carws.model.token;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.nio.file.AccessDeniedException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

import com.example.carws.model.users.Role;
import com.example.carws.model.users.Users;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.List;

@Component
public class Token {

    private static Key secret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static long expiryDuration = 60 * 60;
    // private static

    public String generateJwt(Users user) throws Exception {

        long milliTime = System.currentTimeMillis();
        long expiryTime = milliTime + expiryDuration * 1000;

        Date issuedAt = new Date(milliTime);
        Date expiryAt = new Date(expiryTime);

        Users _user = new Users(user.getId(), user.getNom(), user.getPrenom(), user.getContact(),
                user.getDateDeNaissance(), user.getMail(), user.getRoles());
        _user.setPassword(user.getPassword());

        return Jwts.builder()
                .claim("id", user.getId())
                .claim("user", _user)
                .setSubject(user.getMail())
                .setExpiration(expiryAt)
                .signWith(secret)
                .compact();
    }


    public Claims verify(String token) throws Exception {

        try {

            return Jwts.parser()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new AccessDeniedException("Acces refuse");
        }

    }

    public Users getUser(String token) throws Exception {
        Users user = null;
        System.out.println("ato pory eh");
        Claims claim = new Token().verify(token);

        if (claim.containsKey("user")) {
            System.out.println("ouiiii");
            LinkedHashMap<String, Object> userClaims = (LinkedHashMap<String, Object>) claim.get("user");
            user = new Users();
            System.out.println("Propla no tetooo : " + (String) userClaims.get("prenom"));
            user.setId((String) userClaims.get("id"));
            user.setNom((String) userClaims.get("nom"));
            user.setPrenom((String) userClaims.get("prenom"));
            user.setPassword((String) userClaims.get("password"));
            user.setContact((String) userClaims.get("contact"));
            user.setDateDeNaissance(new java.sql.Date(Long.valueOf(userClaims.get("dateDeNaissance").toString())));
            user.setMail((String) userClaims.get("mail"));
            List<LinkedHashMap<String, Object>> rolesClaimList = (List<LinkedHashMap<String, Object>>) userClaims
                    .get("roles");
            Set<Role> roles = new HashSet<>();
            for (LinkedHashMap<String, Object> roleClaim : rolesClaimList) {
                Role role = new Role();
                role.setId((String) roleClaim.get("id"));
                role.setRole((String) roleClaim.get("role"));
                roles.add(role);
            }
            user.setRoles(roles);
            System.out.println("yesss");
            return user;
        }else{
            throw new Exception("L'utilisateur est nulle pory eh");
        }
    }

}