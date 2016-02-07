//Java 6.25 starts with an incorrect TimeZone
//This code corrects it to Europe/Istanbul

import java.util.TimeZone;

class TZone {
    static String[] TZ = TimeZone.getAvailableIDs();
    static String KEY = "user.timezone";
    static String DEF = "Europe/Istanbul";
    public static void main(String[] args) {
        System.out.println(TZ.length+" time zones");
        String old = System.getProperty(KEY);
        System.out.println("user.timezone = "+old);
        System.out.println(old+" --> "+DEF);
        System.setProperty(KEY, DEF);
        TimeZone.setDefault(TimeZone.getTimeZone(DEF));
        //for (String s : TZ) if (s.startsWith("Asia"))  //"Eu"))
          //  System.out.println(s);
    }
}
/*
Europe/Belfast
Europe/Dublin
Europe/Guernsey
Europe/Isle_of_Man
Europe/Jersey
Europe/Lisbon
Europe/London
Europe/Amsterdam
Europe/Andorra
Europe/Belgrade
Europe/Berlin
Europe/Bratislava
Europe/Brussels
Europe/Budapest
Europe/Busingen
Europe/Copenhagen
Europe/Gibraltar
Europe/Ljubljana
Europe/Luxembourg
Europe/Madrid
Europe/Malta
Europe/Monaco
Europe/Oslo
Europe/Paris
Europe/Podgorica
Europe/Prague
Europe/Rome
Europe/San_Marino
Europe/Sarajevo
Europe/Skopje
Europe/Stockholm
Europe/Tirane
Europe/Vaduz
Europe/Vatican
Europe/Vienna
Europe/Warsaw
Europe/Zagreb
Europe/Zurich
Europe/Athens
Europe/Bucharest
Europe/Chisinau
Europe/Helsinki
Europe/Istanbul
Europe/Kiev
Europe/Mariehamn
Europe/Nicosia
Europe/Riga
Europe/Simferopol
Europe/Sofia
Europe/Tallinn
Europe/Tiraspol
Europe/Uzhgorod
Europe/Vilnius
Europe/Zaporozhye
Europe/Kaliningrad
Europe/Minsk
Europe/Moscow
Europe/Samara
Europe/Volgograd
*/
