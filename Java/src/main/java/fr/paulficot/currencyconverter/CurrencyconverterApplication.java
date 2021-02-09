package fr.paulficot.currencyconverter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Scanner;

@SpringBootApplication
public class CurrencyconverterApplication implements CommandLineRunner {

    /**
     * Methode main, appelle l'application Spring
     *
     * @param args args
     */
    public static void main(String[] args) {
        ApplicationContext appContext = SpringApplication.run(CurrencyconverterApplication.class, args);
        System.out.println("Good bye !");
        SpringApplication.exit(appContext);
    }

    /**
     * Methode run de CommandLineRunner
     *
     * @param args args
     */
    @Override
    public void run(String... args) {
        init();
        mainMenu();
    }

    /**
     * Appelle toutes les fonctions d'initialisation de la DB
     */
    public void init() {
        try {
            RateDB.initTable(RateDB.getConnexion());
            RateDB.deleteAll(RateDB.getConnexion());
            RateDB.insertRates(RateDB.getConnexion(), getCurrencyRateList());
        } catch (Exception e) {
            e.printStackTrace();
        }


    };

    /**
     * Insert directemtent avec un POJO toutes les
     * données de l'API dans un objet RetreivedRates
     *
     * @return hashmap de toutes les devises
     */
    public HashMap<String, Double> getCurrencyRateList() {
        RestTemplate restTemplate = new RestTemplate();
        String URL = "https://api.exchangeratesapi.io/latest";

        RetreivedRates retreivedRates = restTemplate
                .getForObject(URL, RetreivedRates.class);

        assert retreivedRates != null;
        return retreivedRates.getRates();
    }

    /**
     * Menu
     */
    public void mainMenu() {
       Scanner input = new Scanner(System.in);
       int answer = 0;

       while(answer != 4) {
           System.out.println(" .: Currency Converter :.");
           System.out.println("1 - Display all currencies");
           System.out.println("2 - Conversion examples");
           System.out.println("3 - Convert currencies");
           System.out.println("4 - Quit");

           answer = input.nextInt();
           System.out.println("Answer : " + answer);

           switch (answer) {
               case 1 :
                   System.out.println(" .: All currencies :. ");
                   try {
                       RateDB.selectAll(RateDB.getConnexion());
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                   break;
               case 2 :
                   System.out.println(" .: Examples :. ");
                   try {
                       RateDB.selectExample();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                   break;
               case 3 :
                   System.out.println(" .: Convert currencies :. ");
                   try {
                       RateDB.selectComparison();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                   break;
               case 4 :
                   System.out.println(" .: Quit :. ");
                   break;
               default:
                   System.out.println("Unkown value, try again");
                   System.out.println();
                   this.mainMenu();
                   break;
           }
        }
    }
}
