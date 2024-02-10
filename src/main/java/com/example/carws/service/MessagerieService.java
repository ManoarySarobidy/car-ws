package com.example.carws.service;

import org.springframework.stereotype.*;

import com.example.carws.model.users.Discussions;
import com.example.carws.model.users.Messagerie;
import com.example.carws.repository.MessagerieRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.*;

@Service
public class MessagerieService {
    @Autowired
    MessagerieRepository messagingRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public void nouveauMessage(Messagerie message) throws Exception {
        System.out.println("envoyeur: " + message.getIdReceveur());
        messagingRepository.save(message);
    }

    public List<Messagerie> getDiscussions(String idEnvoyeur, String idReceveur) {
        this.setStatus(idReceveur, idEnvoyeur, 20, 1);
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("idEnvoyeur").is(idEnvoyeur).and("idReceveur").is(idReceveur),
                Criteria.where("idEnvoyeur").is(idReceveur).and("idReceveur").is(idEnvoyeur)
        );
        Query query = new Query(criteria).with(Sort.by(Sort.Direction.ASC, "dateHeureEnvoie"));
        return mongoTemplate.find(query, Messagerie.class);
    }

    public void setStatus(String idEnvoyeur, String idReceveur, int status, int avant) {
        Query query = new Query(Criteria.where("idEnvoyeur").is(idEnvoyeur).and("idReceveur").is(idReceveur).and("status").is(avant));
        Update update = new Update().set("status", status);
        mongoTemplate.updateFirst(query, update, Messagerie.class);
        System.out.println("Set status teto nandefa: " + idEnvoyeur + " et nandray: " + idReceveur);
    }

    // public List<Discussions> getListeDiscussions(String idUser) {
    //     MatchOperation matchStage = match(new Criteria().orOperator(
    //         Criteria.where("idEnvoyeur").is(idUser),
    //         Criteria.where("idReceveur").is(idUser)
    //     ));

    //     SortOperation sortStage = sort(Sort.Direction.DESC, "dateHeureEnvoie");

    //     GroupOperation groupStage = Aggregation.group()
    //         .first("idEnvoyeur").as("idEnvoyeur")
    //         .first("dateHeureEnvoie").as("dateHeureEnvoie")
    //         .first("message").as("dernierMessage")
    //         .first("status").as("dernierStatus")
    //         .sum(ConditionalOperators.when(Criteria.where("status").is(1)).then(1).otherwise(0)).as("nombreMessagesNonLu")
    //         .push(Cond.when(Criteria.where("idEnvoyeur").is(idUser))
    //             .thenValueOf("idReceveur")
    //             .otherwiseValueOf("idEnvoyeur")).as("_id");

    //     // Define project stage
    //     ProjectionOperation projectStage = project("_id", "idEnvoyeur", "dateHeureEnvoie", "dernierMessage", "dernierStatus", "nombreMessagesNonLu");

    //     // Create aggregation
    //     Aggregation aggregation = newAggregation(matchStage, sortStage, groupStage, projectStage);

    //     // Execute aggregation and map results to a list
    //     AggregationResults<Discussions> result = mongoTemplate.aggregate(aggregation, "discussions", Discussions.class);
    //     return result.getMappedResults();
    // }

    public List<Discussions> getListeDiscussions(String idUser) {
        String commandJson = "{ \"aggregate\": \"discussions\", " +
            "\"pipeline\": [ " +
                "{ \"$match\": { \"$or\": [ {\"idEnvoyeur\": \"" + idUser +"\"}, {\"idReceveur\": \"" + idUser +"\"} ] } }, " +
                "{ \"$sort\": { \"dateHeureEnvoie\": -1 } }, " +
                "{ \"$group\": { \"_id\": { \"$cond\": [ { \"$eq\": [\"$idEnvoyeur\", \"" + idUser +"\"] }, \"$idReceveur\", \"$idEnvoyeur\" ] }, " +
                    "\"idEnvoyeur\": { \"$first\": \"$idEnvoyeur\" }, " +
                    "\"dateHeureEnvoie\": { \"$first\": \"$dateHeureEnvoie\" }, " +
                    "\"dernierMessage\": { \"$first\": \"$message\" }, " +
                    "\"dernierStatus\": { \"$first\": \"$status\" }, " +
                    "\"nombreMessagesNonLu\": { \"$sum\": { \"$cond\": [ { \"$eq\": [\"$status\",   1] },   1,   0 ] } } " +
                "} }, " +
                "{ \"$project\": { \"_id\":   1, \"idEnvoyeur\":   1, \"dateHeureEnvoie\":   1, \"dernierMessage\":   1, \"dernierStatus\":   1, \"nombreMessagesNonLu\":   1 } } " +
            "], " +
            "\"cursor\": {} " +
        "}";

    
        
        System.out.println(commandJson);
        
        Document result = mongoTemplate.executeCommand(commandJson);
    
        List<Document> documents = (List<Document>) result.get("cursor", Document.class).get("firstBatch");

        List<Discussions> discussions = new ArrayList<>();
        for (Document doc : documents) {
            Discussions discussion = new Discussions();
            discussion.setId(doc.getString("_id"));
            discussion.setIdEnvoyeur(doc.getString("idEnvoyeur"));
            java.util.Date date = doc.getDate("dateHeureEnvoie");
            LocalDateTime localDateTime = Instant.ofEpochMilli(date.getTime())
                                                  .atZone(ZoneId.systemDefault())
                                                  .toLocalDateTime();
            discussion.setDateHeureEnvoie(localDateTime);
            discussion.setDernierMessage(doc.getString("dernierMessage"));
            discussion.setDernierStatus(doc.getInteger("dernierStatus"));
            discussion.setNombreMessagesNonLu(doc.getInteger("nombreMessagesNonLu"));
            discussions.add(discussion);
        }

        return discussions;
    }
    

    
}
