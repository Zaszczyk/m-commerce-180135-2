package mateusz.pogodynka;

import java.util.ArrayList;
import java.util.List;

public class Continent {

    public Continent() {

    }

    public List<String> GenerateContinents(){
        List<String> continents = new ArrayList();

        continents.add("Ameryka Północna");
        continents.add("Ameryka Południowa");
        continents.add("Afryka");
        continents.add("Europa");
        continents.add("Azja");

        return continents;
    }

    public void RestartList(List<String> list) {
        list.clear();
        list.add("Ameryka Północna");
        list.add("Ameryka Południowa");
        list.add("Afryka");
        list.add("Europa");
        list.add("Azja");
    }

    public void GetCountry(List<String> list, String continent) {

        RestartList(list);
        int i = list.indexOf(continent);

        if(continent.equals("Ameryka Północna")) {
            list.add(i+1,"- Kanada");
            list.add(i+2,"- USA");
            list.add(i+3,"- Meksyk");
        }
        else if(continent.equals("Ameryka Południowa")) {
            list.add(i+1,"- Brazylia");
            list.add(i+2,"- Argentyna");
        }
        else if(continent.equals("Afryka")) {
            list.add(i+1,"- Egipt");
        }
        else if(continent.equals("Europa")) {
            list.add(i+1,"- Polska");
            list.add(i+2,"- Niemcy");
            list.add(i+3,"- Francja");
            list.add(i+4,"- Hiszpania");
        }
        else if(continent.equals("Azja")) {
            list.add(i+1,"- Chiny");
            list.add(i+2,"- Japonia");
        }


    }


}
