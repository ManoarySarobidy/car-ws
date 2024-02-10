insert into categorie ( nom_categorie ) values ('Camion');
insert into categorie ( nom_categorie ) values ('Plaisir');
insert into categorie ( nom_categorie ) values ('Taxi');

insert into etats (id, nom ) values (10, 'Non valider');
insert into etats (id, nom ) values (20, 'Valider');
insert into etats (id, nom ) values (30, 'Retirer');
insert into etats (id, nom ) values (40, 'vendu');

 valeur | designation
--------+-------------
     10 | Non valider
     20 | Valider
     30 | Retirer
     40 | Vendu

insert into mois values
	( 'JAN' , 'Janvier' , 1 ),
	( 'FEB' , 'Fevrier' , 2 ),
	( 'MAR' , 'Mars' 	, 3 ),
	( 'APR' , 'Avril' 	, 4 ),
	( 'MAI' , 'Mai' 	, 5 ),
	( 'JUN' , 'Juin' 	, 6 ),
	( 'JUL' , 'Juillet' , 7 ),
	( 'AUG' , 'Aout' 	, 8 ),
	( 'SEP' , 'Septembre', 9 ),
	( 'OCT' , 'Octobre' , 10 ),
	( 'NOV' , 'Novembre', 11 ),
	( 'DEC' , 'Decembre', 12 );

psql -h viaduct.proxy.rlwy.net -p 26857 -d carocaz -U postgres -W

insert into utilisateur values
	( 'USER1', 'RANDRIAHAVANA' , 'Manoary Sarobidy' , 'manoary@gmail.com', '+261341422651', 'Sarobidy!!!', '2004-05-11' );
	insert into utilisateur values
	( 'USER2', 'ANJARATIANA' , 'Layah' , 'layah@gmail.com', '+261341422651', 'Layah!!!' , '2002-06-12');

insert into annonce values
	( default, CURRENT_TIMESTAMP , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-01-05 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-01-23 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-01-27 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-04-01 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-05-10 10:00:00' , 'USER1', 2500000 );
	
insert into annonce values
	( default, '2023-01-10 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-01-10 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-01-10 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-01-10 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-05-10 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-05-10 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-05-10 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-05-10 10:00:00' , 'USER1', 2500000 );

insert into annonce values
	( default, '2023-02-10 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-02-10 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-03-10 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-02-10 10:00:00' , 'USER1', 2500000 );
insert into annonce values
	( default, '2023-07-10 10:00:00' , 'USER1', 2500000 );

db.discussions.find({$or:[{ idEnvoyeur: "eTLPHjKBKTUyMQ2UCJ0UYSQBx5g1", idReceveur: "aRU7yww5lgZ6eDev3iJ95SKgmAA3" },{ idEnvoyeur: "aRU7yww5lgZ6eDev3iJ95SKgmAA3", idReceveur: "eTLPHjKBKTUyMQ2UCJ0UYSQBx5g1" }]}).sort({ dateHeureEnvoie: 1 })

db.discussions.updateOne(
   { _id: ObjectId('65a70723152b89710aa53280') },
   { $set: { statut: 20 } }
)

db.discussions.find({ idEnvoyeur: "eTLPHjKBKTUyMQ2UCJ0UYSQBx5g1", idReceveur: "aRU7yww5lgZ6eDev3iJ95SKgmAA3", statut: 1 })

db.discussions.aggregate([
    {
        $match: {
            $or: [
                { idEnvoyeur: "RhHKS2zA2XhLXF9t3rMSitpjr8k1" },
                { idReceveur: "RhHKS2zA2XhLXF9t3rMSitpjr8k1" }
            ]
        }
    },
    { $sort: { dateHeureEnvoie: -1 } },
    {
        $group: {
            _id: {
                $cond: [
                    { $eq: ["$idEnvoyeur", "RhHKS2zA2XhLXF9t3rMSitpjr8k1"] },
                    "$idReceveur",
                    "$idEnvoyeur"
                ]
            },
            idEnvoyeur: { $first: "$idEnvoyeur" },
            dateHeureEnvoie: { $first: "$dateHeureEnvoie" },
            dernierMessage: { $first: "$message" },
            dernierStatus: { $first: "$status" },
            nombreMessagesNonLu: {
                $sum: {
                    $cond: [{ $eq: ["$status", 1] }, 1, 0]
                }
            }
        }
    },
    {
        $project: {
            _id: 1,
            idEnvoyeur: 1,
            dateHeureEnvoie: 1,
            dernierMessage: 1,
            dernierStatus: 1,
            nombreMessagesNonLu: 1,
        }
    }
]);

{ "aggregate": "discussions", 
	"pipeline": [ 
		{ "$match": { "$or": [ {"idEnvoyeur": "RhHKS2zA2XhLXF9t3rMSitpjr8k1"}, 
		{"idReceveur": "RhHKS2zA2XhLXF9t3rMSitpjr8k1"} ] } }, 
		{ "$sort": { "dateHeureEnvoie": -1 } }, 
		{ "$group": { _id: { $cond: [ { $eq: ["$idEnvoyeur", "RhHKS2zA2XhLXF9t3rMSitpjr8k1"] }, "$idReceveur", "$idEnvoyeur" ] }, idEnvoyeur: { $first: "$idEnvoyeur" }, dateHeureEnvoie: { $first: "$dateHeureEnvoie" }, dernierMessage: { $first: "$message" }, dernierStatus: { $first: "$status" }, nombreMessagesNonLu: { $sum: { $cond: [ { $eq: ["$status",  1] },  1,  0 ] } } } }
	]
}, 
{ "$project": { "_id":  1, "idEnvoyeur":  1, "dateHeureEnvoie":  1, "dernierMessage":  1, "dernierStatus":  1, "nombreMessagesNonLu":  1 } } 
