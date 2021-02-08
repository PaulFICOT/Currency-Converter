package fr.paulficot.currencyconverter;

import org.hsqldb.jdbc.JDBCDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class RateDB {

    /** mode mémoire. */
    private static final String database = "jdbc:hsqldb:mem:database";

    /** utilisateur qui se connecte à la base de données. */
    private static final String userDB = "user";

    /** mot de passe pour se connecter à la base de données. */
    private static final String passwordDB = "password";

    /**
     * Execute la requete de type UPDATE passée en paramètre.
     *
     * @param requete contient la requête SQL
     * @throws SQLException Exception syntaxe SQL incorrecte
     */
    public static void executerUpdate(Connection connexion, String requete) throws SQLException {
        // executerUpdate permet d’effectuer une requête SQL de type UPDATE sur la base
        Statement statement;
        statement = connexion.createStatement();
        statement.executeUpdate(requete);
    }

    /**
     * Execute la requete de type SELECT passée en paramètre.
     *
     * @param requete contient la requête SQL
     * @throws SQLException Exception syntaxe SQL incorrete
     */
    public static ResultSet executerRequete(Connection connexion,String requete) throws SQLException {
        // executerRequete execute n’importe quelle requête, mais renvoie le résultat, ce qui sert par exemple
        // dans le cas d’une requête de type SELECT
        Statement statement;
        statement = connexion.createStatement();
        return statement.executeQuery(requete);
    }

    /**
     * Fait la connexion avec la base de données
     *
     * @return connexion à la DB
     * @throws Exception exception si la connexion a échouée
     */
    private static Connection getConnectionFromDataSource() throws Exception {
        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setURL(database);
        return dataSource.getConnection(userDB,passwordDB);
    }

    /**
     * Permet d'accéder à la connexion à la DB
     *
     * @return connexion à la DB
     * @throws Exception exception si la connexion a échouée
     */
    public static Connection getConnexion() throws Exception {
        return getConnectionFromDataSource();
    }

    /**
     * Crée la table rates dans la DB
     *
     * @param connexion connexion à la DB
     */
    public static void initTable(Connection connexion) {
        String rates = "CREATE TABLE rates ( idRate INTEGER IDENTITY PRIMARY KEY NOT NULL, name VARCHAR(256) NOT NULL, value DOUBLE NOT NULL )";
        try {
            executerUpdate(connexion, rates);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Insert dans la DB les données venant de l'API
     *
     * @param connexion connexion à la DB
     * @param rates Hashmap de devises
     */
    public static void insertRates(Connection connexion, HashMap<String, Double> rates) {
        String rateBaseQuery = "INSERT INTO rates(name, value) VALUES ('EUR', '1.000')";
        try {
            executerUpdate(connexion, rateBaseQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        rates.forEach((str, dbl) -> {
            Rate rate = new Rate(str, dbl);
            String rateQuery = "INSERT INTO rates (name, value) VALUES ('" + rate.getName() + "', '" + rate.getValue() + "')";
            try {
                executerUpdate(connexion, rateQuery);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    /**
     * Supprime toutes les lignes de la table rates
     *
     * @param connexion connexion à la DB
     */
    public static void deleteAll(Connection connexion) {
        String deleteAllQuery = "DELETE FROM rates";
        try {
            executerUpdate(connexion, deleteAllQuery);
        } catch (SQLException throwables) {
            throwables .printStackTrace();
        }
    }

    /**
     * Affiche tout le contenu de la table rates
     *
     * @param connexion connexion à la DB
     * @throws SQLException exception syntaxe SQL incorrecte
     */
    public static void selectAll(Connection connexion) throws SQLException {
        String selectAllQuery = "SELECT * FROM rates";
        ResultSet resultat = executerRequete(connexion, selectAllQuery);
        while (resultat.next()) {
            String id = resultat.getString("idRate");
            String name = resultat.getString("name");
            double value = resultat.getDouble("value");
            System.out.println(id + " | " + name + " | " + value);
        }
    }

    /**
     * Affiche le nom de toutes les devises de la table rates
     *
     * @param connexion connexion à la DB
     * @throws SQLException exception syntaxe SQL incorrecte
     */
    public static void selectAllNames(Connection connexion) throws SQLException {
        String selectAllNamesQuery = "SELECT name FROM rates";
        ResultSet resultat = executerRequete(connexion, selectAllNamesQuery);
        String name = "";
        while (resultat.next()) {
            name += resultat.getString("name");
            name += " | ";
        }
        System.out.println(" | " + name + " | ");
    }

    /**
     * Retourne l'objet rate correspondant à la devise en paramètre
     *
     * @param connexion connexion à la DB
     * @param rate Devise souhaitée
     * @return Objet rate correspondant à la devise
     * @throws SQLException exception syntaxe SQL incorrecte
     */
    public static Rate selectCurrencyByName(Connection connexion, String rate) throws SQLException {
        String selectQuery = "SELECT name, value FROM rates WHERE name = '" + rate.toUpperCase() + "'";
        ResultSet resultat = executerRequete(connexion, selectQuery);
        String name = null;
        double value = 0.0;
        while(resultat.next()) {
            name = resultat.getString("name");
            value = resultat.getDouble("value");
        }
        return new Rate(name, value);
    }

    /**
     * Affiche des exemples de conversions de devises
     *
     *
     * @throws Exception exception syntaxe incorrecte ou connexion à la DB impossible
     */
    public static void selectExample() throws Exception {
        Rate r1;
        Rate r2;

        //EUR -> USB
        r1 = selectCurrencyByName(getConnexion(), "EUR");
        r2 = selectCurrencyByName(getConnexion(), "USD");
        convertCurrencies(r1, r2);

        //PHP -> EUR
        r1 = selectCurrencyByName(getConnexion(), "PHP");
        r2 = selectCurrencyByName(getConnexion(), "EUR");
        convertCurrencies(r1, r2);

        //RUB -> GBP
        r1 = selectCurrencyByName(getConnexion(), "RUB");
        r2 = selectCurrencyByName(getConnexion(), "GBP");
        convertCurrencies(r1, r2);
    }

    /**
     * Fait la conversion de deux devises choisis par l'utilisateur
     *
     * @throws Exception exception syntaxe incorrecte ou connexion à la DB impossible
     */
    public static void selectComparison() throws Exception {
        Scanner input = new Scanner(System.in);
        String strR1;
        String strR2;
        Rate r1;
        Rate r2;

        System.out.println("Choose a first currency");
        selectAllNames(getConnexion());
        strR1 = input.next();
        r1 = selectCurrencyByName(getConnexion(), strR1);
        if(r1.getName() == null) {
            System.out.println("Currency not found, try again");
            selectComparison();
        } else {
            System.out.println("Selected : " + r1);
            System.out.println("Choose a second currency");
            selectAllNames(getConnexion());
            strR2 = input.next();
            r2 = selectCurrencyByName(getConnexion(), strR2);
            if(r2.getName() == null) {
                System.out.println("Currency not found, try again");
                selectComparison();
            } else {
                convertCurrencies(r1, r2);
            }
        }
    }

    /**
     * Converti 1 devise r1 en son montant en devise r2
     *
     * @param r1 devise n°1
     * @param r2 devise n°2
     */
    public static void convertCurrencies(Rate r1, Rate r2){
        DecimalFormat decimalFormat = new DecimalFormat("#0.000");
        System.out.println();
        System.out.println("Converting " + r1.getName() + " to " + r2.getName() + " :");
        System.out.println((1.0) + " " + r1.getName() + " = " +
                decimalFormat.format(r2.getValue()/r1.getValue()) + " " + r2.getName());
    }

}
