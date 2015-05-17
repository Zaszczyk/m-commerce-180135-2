package mateusz.pogodynka;

import java.util.ArrayList;
import java.util.List;

public class Country {

   public void GetCity(List<String> list, String country) {

       int i = 0;

       City cities = new City();
       Continent Continents = new Continent();

       Continents.RestartList(list);

       List<String> tmp = new ArrayList<String>();

       if(country.equals("- Polska")) {
           Continents.GetCountry(list, "Europa");
           tmp = cities.Cities(0);
       }
       else if(country.equals("- Niemcy")) {
           Continents.GetCountry(list, "Europa");
           tmp = cities.Cities(1);
       }
       else if(country.equals("- USA")) {
           Continents.GetCountry(list, "Europa");
           tmp = cities.Cities(2);
       }
       else if(country.equals("- Japonia")) {

           i = list.indexOf(country);
           System.out.println("japonia: "+i);
           Continents.GetCountry(list, "Ameryka Północna");
           tmp = cities.Cities(3);
       }

       i = list.indexOf(country);
        System.out.println(i);

       if(!tmp.isEmpty()){
           for(String s : tmp) {
               System.out.println(s);
               list.add(++i, s);
           }
       }

   }
}
