# Readme

A demo of Market Price Handler where price are adjust by margin of 0.1%

Code are composed by 3 main classes: 

* [MarketPriceController.java](src%2Fmain%2Fjava%2Fcom%2Fscib%2Ffx%2Fmph%2Fcontroller%2FMarketPriceController.java): publish endpoint to request for prices
* [MarketPriceService.java](src%2Fmain%2Fjava%2Fcom%2Fscib%2Ffx%2Fmph%2Fservice%2FMarketPriceService.java): service to treat every new message received
* [MarketPriceDTO.java](src%2Fmain%2Fjava%2Fcom%2Fscib%2Ffx%2Fmph%2Fmodel%2FMarketPriceDTO.java) DTO to transfer last prices after margin applied


NOTE: [RandomPricerGenerator.java](src%2Fmain%2Fjava%2Fcom%2Fscib%2Ffx%2Fmph%2Fservice%2FRandomPricerGenerator.java) is a service to 
generate random prices to test functionality. 


### Swagger UI ###
Swagger UI allows anyone — be it your development team or your end consumers — to visualize and interact
with the API’s resources without having any of the implementation logic in place
```
http://localhost:35000/market-price-handler/swagger-ui/index.html
```


### Launch application ### 
you can launch via IDE

or 

executing this command into terminal: 
```shell
./mvnw spring-boot:run
```

