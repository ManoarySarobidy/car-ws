package com.example.carws.service;

import com.example.carws.repository.UsersRepository;
import org.springframework.beans.factory.annotation.*;

import com.example.carws.model.users.Discussions;
import com.example.carws.model.users.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    UsersRepository usersRepository;
    @Autowired
    RoleService roleService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void inscription(Users users) throws Exception {
        if(users.getId() == null) {
            String idUser = this.inscriptionFireBase(users.getMail(), users.getPassword());
            users.setId(idUser);
        }
        usersRepository.save(users);
        this.ajoutRole(users.getId(), "USER");
    }

    public Users login(String id) throws Exception {
        try {
            Users user = usersRepository.findById(id).orElse(null);
            if (user == null)
                throw new Exception("Utilisateur non trouvé.");
            return user;
        } catch(Exception io) {
            throw new Exception("Utilisateur non enregistré. Veuillez s'inscrire par votre compte Google...");
        }
    }

    public Users login(Users user) throws Exception{
        Optional<Users> u = usersRepository.getUsersByEmailAndPassword( user.getMail(), user.getPassword() );
        if( !u.isPresent() ) throw new Exception("Utilisateur non trouve");
        return u.get();
    }

    public Users authentification(String email) throws Exception {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            Users user = usersRepository.findById(userRecord.getUid()).orElse(null);
            if (user == null)
                throw new Exception("Email ou mot de passe incorrecte.");
            return user;
        } catch (Exception e) {
            throw new Exception("Email ou mot de passe incorrecte.");
        }
    }

    public Users authentification(Users users) throws Exception {
        try {
            Users user = usersRepository.getUsersByEmailAndPassword(users.getMail(), users.getPassword()).orElse(null);
            if (user == null)
                throw new Exception("Email ou mot de passe incorrecte.");
            return user;
        } catch (Exception e) {
            throw new Exception("Email ou mot de passe incorrecte.");
        }
    }

    public String inscriptionFireBase(String email, String password) throws Exception {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            return userRecord.getUid();
        } catch (Exception e) {
            throw new Exception("L’utilisateur avec l’adresse e-mail fournie existe déjà!");
        }
    }

    @Transactional
    public void ajoutRole(String idUser, String idRole) throws Exception {
        String sql = "INSERT INTO roles_user (id_user, roles_id) VALUES ('" + idUser + "', '" + idRole + "')";
        System.out.println(sql);
        entityManager.createNativeQuery(sql).executeUpdate();
    }

    public List<Users> getListeUsers(String idEnvoyeur, List<Discussions> discussions) throws Exception {
        List<Users> users = new ArrayList<Users>();
        for(int i=0; i<discussions.size(); i++) {
            System.out.println("Utilisateur1: " + discussions.get(i).getId());
            Users _user = this.login(discussions.get(i).getId().toString());
            _user.setPassword("");
            if(discussions.get(i).getIdEnvoyeur().equals(idEnvoyeur)) {
                discussions.get(i).setIdEnvoyeur("true");
            } else {
                discussions.get(i).setIdEnvoyeur("false");
            }
            users.add(_user);
        }
        return users;
    }

}
