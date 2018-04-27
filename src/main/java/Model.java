public class Model {

    private String name;
    private Double temp;
    private Double humidity;
    private int pressure;
    private String icon;
    private String description;
    private Double lon;
    private Double lat;
    private String country;

    public Double getLon() { return lon; }

    public Double getLat() { return lat; }

    public void setLat(Double lat) { this.lat = lat; }

    public void setLon(Double lon) { this.lon = lon; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Double getTemp() {
        return temp;
    }
    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getHumidity() {
        return humidity;
    }
    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public int getPressure() { return pressure; }
    public void setPressure(int pressure) { this.pressure = pressure; }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }


}
