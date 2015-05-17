package mateusz.pogodynka;

import java.util.ArrayList;
import java.util.List;

public class City {

    public List<ArrayList<String>> ArrayCities = new ArrayList<ArrayList<String>>();

    public List<String> Cities(int i){

        ArrayList<String> tmp = new ArrayList<String>();

        if(i == 0) {             //POLSKA
            tmp.add("Lodz,PL");
            tmp.add("Warszawa,PL");
            tmp.add("Kalisz,PL");
            tmp.add("Opole,PL");
        }
        else if(i == 1){         //NIEMCY
            tmp.add("Berlin,DE");
            tmp.add("Frankfurt,DE");
        }
        else if(i == 2){        //USA
            tmp.add("New York,US");
            tmp.add("Detroit,US");
        }
        else if(i == 3){        //JAPONIA
            tmp.add("Tokyo,JP");
        }

        return tmp;

    }

    public List<String> getCities(){

        List<String> tmp = new ArrayList<String>();
        List<String> cities = new ArrayList<String>();

        for(int i = 0; i < 4; i++) {

            tmp = Cities(i);

            for(String s : tmp) {
                cities.add(s);
            }

        }

        return cities;
    }
}
