import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static Document getPages() throws IOException {
        String url = "https://www.pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;

    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");
    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Cant extract date from String!");
    }
    private static int printFourVal(Elements values, int idx) {
        int iterationCount=4;
        if (idx == 0) {
            Element valLine = values.get(3);
            boolean isMorning = valLine.text().contains("Утро");
            if (isMorning) {
                iterationCount = 3;
            }

            for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(idx + i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + " ");
                }
                System.out.println();
            }
            return iterationCount;
        } else {
            for (int i = 0; i < iterationCount; i++) {
                Element valLine = values.get(idx + i);
                for (Element td : valLine.select("td")) {
                    System.out.print(td.text() + "   ");
                }
                System.out.println();
            }
        }
        return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPages();
        //css query lng
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;
        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "   Явление   Температура   Давл   Влажность   Ветер");
            int iterationCount = printFourVal(values, index);
            index += iterationCount;
        }
    }
}
