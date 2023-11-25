package karizma.recettecuisineback.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Recette {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @ElementCollection
    private List<String> ingredients;

    @ElementCollection
    private List<String> etapes;

    private int duree;
    private String photo;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    public Recette(String nom, List<String> ingredients, List<String> etapes, int duree) {
        this.nom = nom;
        this.ingredients = ingredients;
        this.etapes = etapes;
        this.duree = duree;
    }

    // Lombok will automatically generate getters and setters
}
