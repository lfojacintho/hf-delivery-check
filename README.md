# hf-delivery-check
A utility app to create a checklist of items that will be delivered by HelloFresh.

This app has no binding with HelloFresh products, it's just a utility app I created to
help me, as a customer, to check the items that were delivered in an easier way. 

It also helps me to plan the week by showing the items I must have at home, so I can buy any 
missing item in advance as well.

## Pre-Requisites

* First of all, you must have an account at HelloFresh and an active order for box delivery.
* You also need to have at least JRE 11 installed in you machine to run it.

## Parameters

Some parameters are available for you to configure the app according to your account and data on
HelloFresh.

This app supports external configuration according to [Spring Boot pattern](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config).
This means that you can set the parameters either by placing a file called `application.properties` in the same folder of your jar file, or you can set the parameters through environment variables.

All parameters have prefixes, depending on the type of parameter and you should set it inside the `application.properties` file like:
```properties
hf.client.baseUrl=https://www.hellofresh.de/
```

Alternatively, you can set the same parameter as environment variable. For this, you need to replace the `.` by `_` and all letters should be capitalized. For example:
```shell
export HF_CLIENT_BASEURL=https://www.hellofresh.de/
```

### Client Parameters

These parameters influence on the client to connect to HelloFresh.

Prefix: `hf.client.`

| Parameter | Description | Default Value |
|:--------- |:----------- |:------------- |
| baseUrl   | This parameter indicates the base URL of HelloFresh for you, this is the URL you use to access it. | https://www.hellofresh.de/ |
| authToken | This app doesn't support login yet. So for accessing the endpoint, you need to login in the browser into your HelloFresh account, copy the Authorization token and use it as a parameter when running the app | *no default value* |

### Product Parameters

These parameters are based on the product you have at HelloFresh. In future, when login is supported through the app you might not need to set these parameters.

Prefix: `hf.product.`

| Parameter | Description | Default Value |
|:--------- |:----------- |:------------- |
| subscription | This is the number of your subscription at HelloFresh, you can find it as a parameter when you inspect the call to the `gw/my-deliveries/menu` endpoint in Web. | *no default value* |
| sku | This is the id of the product you ordered at HelloFresh, you can find it as a parameter when you inspect the call to the `gw/my-deliveries/menu` endpoint in Web. | *no default value* |
| servings | This is the number of servings you chose when you ordered with HelloFresh, it's usually 2, 3 or 4 | *no default value* |
| country | The [Alpha-2 code from ISO 3166-1](https://en.wikipedia.org/wiki/ISO_3166-1) from the country you live. This parameter is optional. | *no default value* |
| locale | The locale code for the language you use the product | de-DE
| preference | The id of you preference of meals, for example, `light`. This parameter is optional. | *no default value* |
| postcode | The postcode where your boxes are delivered. This parameter is optional. | *no default value* |
| deliveryOption | The id of the delivery option of your boxes. This parameter is optional. | *no default value* |

## Building the App

For building the application, you will need to have at least JDK 17.
You can build it simply running:
```shell
./mvnw clean package
```

This will create the executable JAR file at `target` directory.

## Running the App

After you have the JAR file, don't forget to set the mandatory parameters (either in a properties files or as environment variable). The you can simply run:
```shell
cd target
java -jar hellofresh-delivery-check-0.0.1-SNAPSHOT.jar 
```
