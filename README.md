# Route Finder
- Spring Boot, Maven
- [Data link](https://raw.githubusercontent.com/mledoze/countries/master/countries.json)
- The application exposes REST endpoint `/routing/{origin}/{destination}` that
returns a list of border crossings to get from origin to destination
- Single route is returned if the journey is possible
- Algorithm needs to be efficient
- If there is no land crossing, the endpoint returns ​HTTP 400
- Countries are identified by ​cca3​ field in country data
- HTTP request sample (land route from Czech Republic to Italy):
```
GET /routing/CZE/ITA HTTP/1.0 : {
    "route": ["CZE", "AUT", "ITA"] 
}
```

## Run using Maven: 
`mvn clean spring-boot:run`