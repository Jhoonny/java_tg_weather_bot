
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    private static Conf conf = new Conf();

    private static String APPID = conf.getAPPID();

    private static String getContent (String uri) throws IOException {
        URL url = new URL(uri);

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";

        while (in.hasNext()) { result += in.nextLine(); }

        return result;
    }

    public static String getWeather(String message, Model model) throws IOException, URISyntaxException {

        URIBuilder ub = new URIBuilder("https://api.openweathermap.org/data/2.5/weather");
        ub.addParameter("q", message);
        ub.addParameter("units", "metric");
        ub.addParameter("lang", "ro");
        ub.addParameter("APPID", APPID);

//        System.out.println(ub.toString());

        String  result = getContent(ub.toString());

        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));

        JSONObject coord = object.getJSONObject("coord");
        model.setLat(coord.getDouble("lat"));
        model.setLon(coord.getDouble("lon"));

        JSONObject sys = object.getJSONObject("sys");
        model.setCountry(sys.getString("country"));

        JSONObject main = object.getJSONObject("main");

        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));
        model.setPressure(main.getInt("pressure"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setDescription((String) obj.get("description"));
        }


        return "Localitatea: " + model.getName() + " | " + model.getCountry() + "\n" +
                "Temperatura: " + model.getTemp().longValue() + " \u2103" + "\n" +
                "Descrierea: " + model.getDescription() + "\n" +
                "Umeditatea: " + model.getHumidity() + " %" + "\n" +
                "Presiunea: " + model.getPressure() + " hpa" + "\n" +
                "UV index: " + getUV(model);
//                "https://openweathermap.org/img/w/" + model.getIcon() + ".png";

    }

    public static double getUV(Model model) throws URISyntaxException, IOException {
        URIBuilder ub = new URIBuilder("https://api.openweathermap.org/data/2.5/uvi");

        ub.addParameter("APPID", APPID);
        ub.addParameter("lat", model.getLat().toString());
        ub.addParameter("lon", model.getLon().toString());

//        System.out.println(ub.toString());

        String  result = getContent(ub.toString());

        JSONObject object = new JSONObject(result);
        double uvValue = object.getDouble("value");

        return uvValue;

    }
}
