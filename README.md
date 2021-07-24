# survey-android

# Architecture
Follows the principles of [Clean Architecture](https://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html)

# Setup
Because of security reasons, all credentials are not exist in the codebase, you need to grab your credentials and put them into `local.properties` file following below instructions
Note: `baseUrl` inclues api version, e.g: `https://hostname/api/v1/`

## Set up credentials for production
```
apiConfigs.production.baseUrl="{API-BASE-URL}"
apiConfigs.production.clientId="{YOUR-API-CLIENT-ID}"
apiConfigs.production.clientSecret="{YOUR-API-CLIENT-SECRET}"
```
## Set up credentials for staging
```
apiConfigs.staging.baseUrl="{API-BASE-URL}"
apiConfigs.staging.clientId="{YOUR-API-CLIENT-ID}"
apiConfigs.staging.clientSecret="{YOUR-API-CLIENT-SECRET}"
```
## Set up credentials for testing
```
apiConfigs.test.baseUrl="{API-BASE-URL}"
apiConfigs.test.clientId="{YOUR-API-CLIENT-ID}"
apiConfigs.test.clientSecret="{YOUR-API-CLIENT-SECRET}"
apiConfigs.test.email="{LOGIN-EMAIL}"
apiConfigs.test.password="{LOGIN-PASSWORD}"
```

# Dependencies
- [RxJava3](https://github.com/ReactiveX/RxJava)
- [Hilt](https://dagger.dev/hilt/) dependency injection
- [Retrofit](https://square.github.io/retrofit/) 
- [OkHttp](https://square.github.io/okhttp/)
- [Gson](https://github.com/google/gson) json parser
- [Glide](https://github.com/bumptech/glide) image loader
- [RxBinding](https://github.com/JakeWharton/RxBinding)
- [Blurry](https://github.com/wasabeef/Blurry)
- [Shimmer](https://github.com/facebook/shimmer-android)
- [Mockito](https://github.com/mockito/mockito)
- [Robolectric](http://robolectric.org/getting-started/)
- [Jacoco](https://github.com/jacoco/jacoco)
- JUnit
- and many support libraries from [Android Jetpack](https://maven.google.com/web/index.html)
