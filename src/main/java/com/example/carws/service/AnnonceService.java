package com.example.carws.service;
import com.example.carws.repository.AnnonceFavoriesRepository;
import com.example.carws.repository.AnnonceRepository;
import com.example.carws.repository.AnnonceVenduRepository;
import com.example.carws.repository.AnnoncePhotoRepository;
import com.example.carws.repository.CaracteristiqueRepository;
import com.example.carws.repository.DetailsAnnonceRepository;
import com.example.carws.repository.HistoriqueRepository;
import com.example.carws.repository.LieuRepository;
import com.example.carws.repository.UsersRepository;
import com.example.carws.repository.ValidateAnnonceRepository;
import com.example.carws.repository.VoitureRepository;
import com.example.carws.request.SearchedElements;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import com.example.carws.model.annonce.*;
import com.example.carws.model.primaire.Caracteristique;
import com.example.carws.model.primaire.Coloriage;
import com.example.carws.model.primaire.Couleur;
import com.example.carws.model.primaire.Lieu;
import com.example.carws.model.primaire.Modele;
import com.example.carws.model.statistics.Commission;
import com.example.carws.model.users.Users;
import com.example.carws.model.voiture.Voiture;
import com.example.carws.exception.*;

@Service
public class AnnonceService{

	@Autowired
	AnnonceRepository repository;

	@Autowired
	DetailsAnnonceRepository detailsRepository;

	@Autowired
	LieuRepository lieuRepository;
	
	@Autowired
	ValidateAnnonceRepository validateRepository;

	@Autowired
	AnnonceVenduRepository venduRepository;

	@Autowired
	AnnoncePhotoRepository photoRepository;

	@Autowired
	AnnonceFavoriesRepository favorisRepository;

	@Autowired
	VoitureRepository voitureRepository;

	@Autowired
	UsersRepository userRepository;

    @Autowired
    CaracteristiqueRepository caracteristiqueRepository;

	@Autowired
	HistoriqueRepository historiqueRepository;

	@Autowired
	EntityManager entityManager;

	public List<Annonce> getAllAnnonces() throws Exception{
		return repository.findAll();
	}

	public Annonce getAnnonce( String id ) throws Exception{
		Annonce annonce = (Annonce)repository.findById( id ).orElse(null);
		if( annonce == null ){
			throw new CategorieException("L'annonce n'existe pas");
		}
		return annonce;
	}

	public List<Annonce> getAnnoncesByUser() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String idUser = (String)authentication.getPrincipal();

		Users user = userRepository.findById(idUser).orElseThrow(() -> new Exception("User not found in annonceService"));
        return repository.findByUser(user);
    }

    @Transactional
	public void saveAnnonceWithDetails( Annonce annonce, DetailsAnnonce[] details) throws Exception{

        Optional<Lieu> lieu = lieuRepository.findById(annonce.getLieu().getId());
        Voiture voiture = voitureRepository.findById(annonce.getVoiture().getId());
        
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String idUser = (String)authentication.getPrincipal();

		Users user = userRepository.findById(idUser).orElseThrow(() -> new Exception("User not found in annonceService"));
        List<AnnoncePhoto> pics = annonce.getPhotos();
        annonce.setLieu(lieu.get());
        annonce.setVoiture(voiture);
        annonce.setUser(user);
        annonce.setValeur(10); // annonce non validé par défaut

		repository.save( annonce );

		for( AnnoncePhoto photo : pics ){
			photo.setAnnonce( annonce );
			photoRepository.save(photo);
		}

		if(details != null){
			for( DetailsAnnonce detail : details ){
		        Optional<Caracteristique> caracteristique = caracteristiqueRepository.findById(detail.getCaracteristique().getId());
				detail.setAnnonce(annonce);
		        detail.setCaracteristique(caracteristique.get());

				detailsRepository.save(detail);
			}
			
		}
	}

	public Annonce updateAnnonce( Annonce annonce ) throws Exception{
		Annonce annonceUpdate;
		try{
			annonceUpdate = this.getAnnonce( annonce.getId() );
			annonceUpdate.setDate(annonce.getDate());

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String idUser = (String)authentication.getPrincipal();

			Optional<Users> user = userRepository.findById(idUser);
            annonceUpdate.setUser(user.get());

            annonceUpdate.setPrix(annonce.getPrix());
			annonceUpdate.setId(annonce.getId());
			annonceUpdate.setDescription(annonce.getDescription());
			annonceUpdate.setPrix(annonce.getPrix());
			annonceUpdate.setValeur(annonce.getValeur());
			annonceUpdate.setVoiture(annonce.getVoiture());
			annonceUpdate = repository.save(annonceUpdate);
			
		}catch(CategorieException e){
			annonceUpdate = repository.save( annonce );
		}catch (Exception e) {
			throw e;
		}
		return annonceUpdate;
	}

	@Transactional
	public void deleteAnnonce(String id) throws Exception {
		Optional<Annonce> optionalAnnonce = repository.findById(id);
		if (optionalAnnonce.isPresent()) {
			Annonce annonce = optionalAnnonce.get();
			annonce.setValeur(30);
			repository.save(annonce);
		} else {
			throw new Exception("Annonce not found with id " + id);
		}
	}

	public List<Annonce> findAllAnnoncesEnAttente() {
		return repository.findByValeur(10); 
	}

	public List<Annonce> findAllValidatedAnnonces() {
		return repository.findByValeur(20); 
	}
	  
	public List<AnnonceFavories> findAllAnnoncesFavories() {
		Users userP = new Users();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String idUser = (String)authentication.getPrincipal();
		userP.setId(idUser);
		return favorisRepository.findAllByUser(userP); 
	}

// 	public List<AnnonceVendus> findAllVenduAnnonces() {
// 		return venduRepository.findAll(); 
// 	}

	@Transactional
	public void saveValidateAnnonce( String id , ValidateAnnonce validateAnnonce ) throws Exception{
		Optional<Annonce> optionalAnnonce = repository.findById(id);
		if (!optionalAnnonce.isPresent() || optionalAnnonce.get().getId() == null) {
			throw new Exception("Annonce not found or invalid with id " + id + " in saveValidateAnnonce()");
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String idUser = (String)authentication.getPrincipal();
		Users user = new Users();
		user.setId(idUser);
		validateAnnonce.setUser(user);

		Annonce annonce = optionalAnnonce.get();
		annonce.setValeur(20);
		annonce = this.updateAnnonce(annonce);
		
		System.out.println("annonce : "+annonce.getId());
		System.out.println("user : "+validateAnnonce.getUser().getId());

		Optional<Users> optionnaleUser = userRepository.findById(validateAnnonce.getUser().getId());
		if (!optionnaleUser.isPresent() || optionnaleUser.get().getId() == null) {
			throw new Exception("Annonce not found or invalid with id " + id + " for user in saveValidateAnnonce()");
		}

		System.out.println(optionnaleUser.get().getId());

		validateAnnonce.setAnnonce(annonce);
		validateAnnonce.setUser(optionnaleUser.get());

		validateRepository.save(validateAnnonce);
	}

	public Integer getCommission(double prix){

		List<Commission> results = new ArrayList<>();
		String sql = "SELECT c.* FROM commission c WHERE :prix >= c.minimum AND :prix < c.maximum";

		Query query = entityManager.createNativeQuery(sql, Commission.class);
		query.setParameter("prix", prix);
		results = query.getResultList();

		return results.get(0).getCommission();
	}

	@Transactional
	public void saveAnnonceVendu( String id , AnnonceVendus annonceVendu ) throws Exception{
		Optional<Annonce> optionalAnnonce = repository.findById(id);
		if (!optionalAnnonce.isPresent() || optionalAnnonce.get().getId() == null) {
			throw new Exception("Annonce not found or invalid with id " + id + " in saveAnnonceVendu()");
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String idUser = (String)authentication.getPrincipal();
		Users user = new Users();
		user.setId(idUser);
		annonceVendu.setUser(user);

		Optional<Users> optionnaleUser = userRepository.findById(annonceVendu.getUser().getId());
		if (!optionnaleUser.isPresent() || optionnaleUser.get().getId() == null) {
			throw new Exception("Annonce not found or invalid with id " + id + " for user in saveAnnonceFavories()");
		}
		Annonce annonce = optionalAnnonce.get();
		annonce.setValeur(40);
		annonce = this.updateAnnonce(annonce);
		
		System.out.println("annonce : "+annonce.getId());

		annonceVendu.setUser(optionnaleUser.get());

		annonceVendu.setAnnonce(annonce);
		Integer commission = this.getCommission(annonceVendu.getAnnonce().getPrix());
		annonceVendu.setCommission(commission);
		venduRepository.save(annonceVendu);
	}

	@Transactional
	public void saveAnnonceFavories( String id , AnnonceFavories annonceFavories ) throws Exception{
		Optional<Annonce> optionalAnnonce = repository.findById(id);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String idUser = (String)authentication.getPrincipal();
		Users user = new Users();
		user.setId(idUser);
		annonceFavories.setUser(user);

		Optional<Users> optionnalUser = userRepository.findById(annonceFavories.getUser().getId());
		if (!optionnalUser.isPresent() || optionnalUser.get().getId() == null) {
			throw new Exception("Annonce not found or invalid with id " + id + " for user in saveAnnonceFavories()");
		}

		if (optionalAnnonce.isPresent()) {
			Annonce annonce = optionalAnnonce.get();

			annonceFavories.setAnnonce(annonce);
			annonceFavories.setUser(optionnalUser.get());
			favorisRepository.save(annonceFavories);
			
		} else {
			throw new Exception("Annonce not found with id " + id + " in saveAnnonceFavories()");
		}	  
	}
	

	@Transactional
	public void DefavoriseAnnonce( String id ) throws Exception{
		Optional<AnnonceFavories> optionalAnnonce = favorisRepository.findById(id);
		if (!optionalAnnonce.isPresent() || optionalAnnonce.get().getId() == null) {
			throw new Exception("Annonce favoris not found or invalid with id " + id + " in DefavoriseAnnonce()");
		}

		Annonce annonce = optionalAnnonce.get().getAnnonce();
		Users user = optionalAnnonce.get().getUser();

		annonce.setFavories(null);

		Historique historique = new Historique();

		System.out.println("id annonce: "+annonce.getId());

		historique.setIdAnnonce(annonce.getId());
		// historique.setAncienValeur(optionalAnnonce.get().getAnnonce().getValeur());
		historique.setDate(optionalAnnonce.get().getDatetime());
		historique.setUser(user);

		System.out.println("Testtt");

		favorisRepository.deleteById(optionalAnnonce.get().getId());

		historiqueRepository.save(historique);
	}

	@Transactional
	public List<Annonce> getAnnoncesMatch(SearchedElements elements){
		// Requête native pour la condition de couleur
		List<Annonce> results = new ArrayList<>();
		if(elements.getIdCouleur() != null){
			String sql = "SELECT v.* FROM annonce v " +
						 " INNER JOIN (SELECT id_voiture, MAX(date_application) AS max_date FROM coloriage GROUP BY id_voiture) cv ON v.id_voiture = cv.id_voiture" +
						 "	WHERE EXISTS ( SELECT 1 FROM coloriage c " +
						 "    WHERE c.id_voiture = v.id_voiture AND c.date_application = cv.max_date AND c.id_couleur = :couleur)";
	
			Query query = entityManager.createNativeQuery(sql, Annonce.class);
			query.setParameter("couleur", elements.getIdCouleur());
			results = query.getResultList();
		}
	
		// Requête Criteria API pour les autres conditions
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Annonce> criteriaQuery = criteriaBuilder.createQuery(Annonce.class);
		Root<Annonce> root = criteriaQuery.from(Annonce.class);
	
		Predicate predicate = criteriaBuilder.equal(root.get("valeur"), 20);
		Join<Annonce, Voiture> voitureJoin = root.join("voiture");
		HashMap<String, Object> conditions = elements.allConditions();
	
		for (Map.Entry<String, Object> condition : conditions.entrySet()) {
			if(condition.getValue() != null){
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(voitureJoin.get(condition.getKey()).get("id"), condition.getValue()));
			}
		}
	
		if (elements.getDescription() != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("description"), "%" + elements.getDescription() + "%"));
		}
	
		if (elements.getKilometrageMin() != -1 && elements.getKilometrageMax() > 0) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(voitureJoin.get("kilometrage"), elements.getKilometrageMin(), elements.getKilometrageMax()));
		}

		if (elements.getAnneeMin() != -1 && elements.getAnneeMax() > 0) {
			Calendar minYear = Calendar.getInstance();
			minYear.set(elements.getAnneeMin(), Calendar.JANUARY, 1);
			Calendar maxYear = Calendar.getInstance();
			maxYear.set(elements.getAnneeMax(), Calendar.DECEMBER, 31);
			Predicate yearPredicate = criteriaBuilder.between(voitureJoin.get("modele").get("annee"), minYear, maxYear);
			predicate = criteriaBuilder.and(predicate, yearPredicate);
		}			
	
		criteriaQuery.where(predicate);
		System.out.println(criteriaQuery.toString());
		TypedQuery<Annonce> typedQuery = entityManager.createQuery(criteriaQuery);
		List<Annonce> tempResults = typedQuery.getResultList();
	
		// Comparaison des résultats des deux requêtes
		// results.retainAll(tempResults);
		List<Annonce> resultatAnnonces = new ArrayList<>();
		if(!results.isEmpty() && !tempResults.isEmpty()){
			results.retainAll(tempResults);
			resultatAnnonces = results;
		} else if (!results.isEmpty() && tempResults.isEmpty()){
			resultatAnnonces = results;
		} else if (results.isEmpty() && !tempResults.isEmpty()){
			resultatAnnonces = tempResults;
		}
	
		return resultatAnnonces;
	}	

}