package utils;

import org.osbot.Constants;
import org.osbot.rs07.script.MethodProvider;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Config extends MethodProvider {
    public List<String> getConfig() {
        File file = new File(Constants.DATA_DIR + "\\Ultimate Money Maker V3\\config.txt");

        if (!file.exists()) {
            try {
                if (file.getParentFile().mkdirs() && file.createNewFile()) {
                    int randomStartMinutes = random(1, 1440);

                    double devidedMinutes = randomStartMinutes / 60;

                    int hours = (int) Math.floor(devidedMinutes);

                    BigDecimal minutesBigDecimal = new BigDecimal(devidedMinutes - hours);

                    int minutes = (int) minutesBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();

                    String time = String.format("%d:%d", hours, minutes); // Done

                    int agressivness = random(15, 20);

                    List<String> params = new ArrayList<>();

                    params.add(time);
                    params.add(String.valueOf(agressivness));

                    try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
                        for (String line : params) {
                            br.write(line + System.lineSeparator());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<String> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                data.add(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
