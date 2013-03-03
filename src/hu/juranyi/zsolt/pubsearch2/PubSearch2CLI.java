/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.juranyi.zsolt.pubsearch2;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Zsolt
 */
public class PubSearch2CLI {

    public static void main(List<String> args) {
        line('=');
        System.out.println("PubSearch 2");
        System.out.println("Version " + PubSearch2.VERSION);
        System.out.println("Command Line Interface");
        line('=');
        System.out.println("CLI is under development, please use GUI instead by running PubSearch 2");
        System.out.println("without any parameters.");
        // TODO kipróbálni, hogy "a=b", "-a=b", "--a=b" is bekerül-e így a raw-ba
        // TODO meg megnézhetjük, hogy a named-be bekerül-e és hogyan használható
        // TODO parse parameters
        // TODO print usage on fail - még ki is kell találni:) (batch, meg mi még?)
        /*
         * Paraméterek: java -jar PubSearch2.jar <MODE> <PARAMETERS>
         * MODES:
         * - crawl authors=<filename> [export [skipdb]]
         * - - más nem kell, többi beállítás (mysql, threadcounts, translev) a configban van
         * - - exportálás authoronként, csv-be, crawl-YYYYMMDD-HHMM mappába, author.csv fájlokba, authorname-ből a pontot kivenni
         * - - DE A CITEDBY-OKAT HOGY EXPORTÁLOM BELE A CSV-BE?
         * - help
         * - - usage info, leírás?
         * 
         */
    }

    private static void line(char c) {
        char[] arr = new char[75];
        Arrays.fill(arr, c);
        System.out.println(arr);
    }
}
