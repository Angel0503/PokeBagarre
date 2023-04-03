package com.montaury.pokebagarre.ui;
import java.util.concurrent.TimeUnit;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
@ExtendWith(ApplicationExtension.class)
class PokeBagarreAppTest {
    private static final String IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1 = "#nomPokemon1";
    private static final String IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2 = "#nomPokemon2";
    private static final String IDENTIFIANT_BOUTON_BAGARRE = ".button";
    @Start
    private void start(Stage stage) {
        new PokeBagarreApp().start(stage);
    }
    @Test
    void pas_d_erreur_si_deux_noms_pokemons_existants_differents(FxRobot robot) {
        //GIVEN
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        robot.write("Pikachu");
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        robot.write("Tiplouf");
        //WHEN
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        //THEN
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(getResultatBagarre(robot)).isEqualTo("Le vainqueur est: Pikachu")
         );
    }

    @Test
    void erreur_si_deux_noms_identiques(FxRobot robot) {
        //GIVEN
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        robot.write("Tiplouf");
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        robot.write("Tiplouf");
        //WHEN
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        //THEN
        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Impossible de faire se bagarrer un pokemon avec lui-meme")
        );
    }

    @Test
    void erreur_si_nom_premier_pokemon_inexistant(FxRobot robot) {
        //GIVEN
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        robot.write("UnPokemon");
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        robot.write("Tiplouf");
        //WHEN
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        //THEN
        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Impossible de recuperer les details sur 'UnPokemon'")
        );
    }

    @Test
    void erreur_si_nom_deuxieme_pokemon_inexistant(FxRobot robot) {
        //GIVEN
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        robot.write("Tiplouf");
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        robot.write("UnPokemon");
        //WHEN
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        //THEN
        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Impossible de recuperer les details sur 'UnPokemon'")
        );
    }

    @Test
    void erreur_si_nom_premier_pokemon_non_renseigner(FxRobot robot) {
        //GIVEN
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        robot.write("Tiplouf");
        //WHEN
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        //THEN
        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Le premier pokemon n'est pas renseigne")
        );
    }

    @Test
    void erreur_si_nom_deuxieme_pokemon_non_renseigner(FxRobot robot) {
        //GIVEN
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        robot.write("Tiplouf");
        //WHEN
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        //THEN
        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Le second pokemon n'est pas renseigne")
        );
    }

    private static String getResultatBagarre(FxRobot robot) {
        return robot.lookup("#resultatBagarre").queryText().getText();
    }
    private static String getMessageErreur(FxRobot robot) {
        return robot.lookup("#resultatErreur").queryLabeled().getText();
    }
}
