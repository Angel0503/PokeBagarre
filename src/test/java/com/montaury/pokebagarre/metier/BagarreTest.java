package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.erreurs.ErreurRecuperationPokemon;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.*;

class BagarreTest {
    PokeBuildApi fausseApi = Mockito.mock(PokeBuildApi.class);
    Bagarre bagarre = new Bagarre(fausseApi);
    @Test
    void nom_premier_pokemon_pas_vide() {
        //GIVEN
        String pokemon1 = "";
        String pokemon2 = "";
        //WHEN
        Throwable thrown = catchThrowable(() -> bagarre.demarrer(pokemon1, pokemon2));
        //THEN
        assertThat(thrown)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void nom_deuxieme_pokemon_pas_vide() {
        //GIVEN
        String pokemon1 = "Pikachu";
        String pokemon2 = "";
        //WHEN
        Throwable thrown = catchThrowable(() -> bagarre.demarrer(pokemon1, pokemon2));
        //THEN
        assertThat(thrown)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le second pokemon n'est pas renseigne");
    }

    @Test
    void nom_des_deux_pokemon_differents() {
        //GIVEN
        String pokemon1 = "Pokemon";
        //WHEN
        Throwable thrown = catchThrowable(() -> bagarre.demarrer(pokemon1, pokemon1));
        //THEN
        assertThat(thrown)
                .isInstanceOf(ErreurMemePokemon.class)
                .hasMessage("Impossible de faire se bagarrer un pokemon avec lui-meme");
    }

    @Test
    void pokemon1_gagne_si_attaque_superieur() {
        //GIVEN
        Mockito.when(fausseApi.recupererParNom("Pikachu"))
                .thenReturn(CompletableFuture.completedFuture(
                        new Pokemon("Pikachu", "url1", new Stats(2, 2))
                ));
        Mockito.when(fausseApi.recupererParNom("Tiplouf"))
                .thenReturn(CompletableFuture.completedFuture(
                        new Pokemon("Tiplouf", "url1", new Stats(1, 2))
                ));
        //WHEN
        CompletableFuture<Pokemon> futurVainqueur = bagarre.demarrer("Pikachu", "Tiplouf");
        //THEN
        assertThat(futurVainqueur)
                .succeedsWithin(Duration.ofSeconds(2))
                .satisfies(pokemon -> {assertThat(pokemon.getNom()).isEqualTo("Pikachu");});
    }

    @Test
    void pokemon1_gagne_si_attaque_egal_et_defense_superieur() {
        //GIVEN
        Mockito.when(fausseApi.recupererParNom("Pikachu"))
                .thenReturn(CompletableFuture.completedFuture(
                        new Pokemon("Pikachu", "url1", new Stats(2, 3))
                ));
        Mockito.when(fausseApi.recupererParNom("Tiplouf"))
                .thenReturn(CompletableFuture.completedFuture(
                        new Pokemon("Tiplouf", "url1", new Stats(2, 2))
                ));
        //WHEN
        CompletableFuture<Pokemon> futurVainqueur = bagarre.demarrer("Pikachu", "Tiplouf");
        //THEN
        assertThat(futurVainqueur)
                .succeedsWithin(Duration.ofSeconds(2))
                .satisfies(pokemon -> {assertThat(pokemon.getNom()).isEqualTo("Pikachu");});
    }
}