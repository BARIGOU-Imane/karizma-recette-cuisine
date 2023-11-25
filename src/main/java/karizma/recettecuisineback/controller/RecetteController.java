package karizma.recettecuisineback.controller;

import karizma.recettecuisineback.beans.Recette;
import karizma.recettecuisineback.beans.Utilisateur;
import karizma.recettecuisineback.dao.RecetteDao;
import karizma.recettecuisineback.dao.UtilisateurDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.Period;
import java.util.*;

@RestController
public class RecetteController {
    @Autowired
    RecetteDao recetteDao;

    @Autowired
    UtilisateurDao utilisateurDao;

    @GetMapping({"/Allrecettes"})
    public ResponseEntity<List<Recette>> getAllRecette(@RequestParam(required = false) String name) {
        try {
            List<Recette> recettes;
            if (name == null) {
                recettes = new ArrayList<>(recetteDao.findAll());
            } else {
                recettes = new ArrayList<>(recetteDao.findByName(name));
            }

            return recettes.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(recettes, HttpStatus.OK);
        } catch (Exception var3) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/recette/{id}"})
    public ResponseEntity<Recette> getRecettesById(@PathVariable("id") long id) {
        Optional<Recette> recetteDetails = this.recetteDao.findById(id);
        return recetteDetails.isPresent() ? new ResponseEntity((Recette)recetteDetails.get(), HttpStatus.OK) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping({"/addRecette"})
    public ResponseEntity<Recette> createRecettes(@RequestBody Recette recette) {
        try {
            Recette _recette = new Recette(recette.getNom(), recette.getIngredients(), recette.getEtapes(), recette.getDuree());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Utilisateur user = (Utilisateur)this.utilisateurDao.findByUsername(username).orElseThrow(() -> {
                return new UsernameNotFoundException("Utilisateur non trouvé avec username: " + username);
            });
            _recette.setUtilisateur(user);
            Recette savedComp = (Recette)this.recetteDao.save(_recette);
            return new ResponseEntity(savedComp, HttpStatus.CREATED);
        } catch (Exception var9) {
            return new ResponseEntity((MultiValueMap)null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping({"/updateRecette/{id}"})
    public ResponseEntity<Recette> updateRecette(@PathVariable("id") long id, @RequestBody Recette recette) {
        Optional<Recette> compDetails = this.recetteDao.findById(id);
        if (compDetails.isPresent()) {
            Recette _recette = (Recette)compDetails.get();
            _recette.setNom(recette.getNom());
            _recette.setIngredients(recette.getIngredients());
            _recette.setEtapes(recette.getEtapes());
            _recette.setDuree(recette.getDuree());
            return new ResponseEntity((Recette)this.recetteDao.save(_recette), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping({"/deleteRecette/{id}"})
    public ResponseEntity<HttpStatus> deleteRecette(@PathVariable("id") long id) {
        try {
            this.recetteDao.deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception var4) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping({"/deleteAllRecette"})
    public ResponseEntity<HttpStatus> deleteAllRecettes() {
        try {
            this.recetteDao.deleteAll();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception var2) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/recette/name"})
    public ResponseEntity<List<Recette>> findByNom(@RequestParam("nom") String nom) {
        try {
            List<Recette> recettes = this.recetteDao.findByName(nom);
            return recettes.isEmpty() ? new ResponseEntity(HttpStatus.NO_CONTENT) : new ResponseEntity(recettes, HttpStatus.OK);
        } catch (Exception var3) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping({"/getAllRecetteUser"})
    public ResponseEntity<List<Recette>> findCompsByAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Utilisateur user = (Utilisateur)this.utilisateurDao.findByUsername(username).orElseThrow(() -> {
            return new UsernameNotFoundException("Utilisateur non trouvé avec username: " + username);
        });
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            List<Recette> recettes = this.recetteDao.findByUser(user);
            return recettes.isEmpty() ? new ResponseEntity(HttpStatus.NO_CONTENT) : new ResponseEntity(recettes, HttpStatus.OK);
        }
    }
}
