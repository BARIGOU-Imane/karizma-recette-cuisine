package karizma.recettecuisineback.dao;

import karizma.recettecuisineback.beans.Recette;
import karizma.recettecuisineback.beans.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RecetteDao extends JpaRepository<Recette, Long> {
    List<Recette> findByName(String name);

    List<Recette> findByIngredients(List<String> ingredients);

    List<Recette> findByDuree(int duree);
    List<Recette> findByUser(Utilisateur user);

    List<Recette> findByIngredientsContaining(String ingredient);

    List<Recette> findByEtapesContaining(String etape);

}
