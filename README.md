# NightDogs
Android demo app using Kotlin coroutines and Moshi with Retrofit.

The uses a repo based pattern. Coroutines can provide a much cleaner implementation
for basic asynchronous operation than RxJava.

## Problem Statement ##

Our use case:  We have a client, let's call him “Willy”, 
who loves his pets (dogs and a cat), however his cat is getting a bit
old and somewhat senile. 
The cat has a tendency to spend much of the night screaming loudly 
seemingly in defiance of the laws of physics (how could so much noise 
come from such a small package).  To offset this stress during the long 
hours of coding, Willy likes to have cute pictures of cats and dogs 
presented on his phone (or old Nexus 7 tablet). Dog pictures displayed 
during the night to remind him that there are better pets around, 
and cats during the day to try to convince him that cats aren’t all 
that bad after all. Now Willy is a bit of a nutcase when it comes to time. 
(and the cat is surprisingly punctual). 
He wants the cat pictures displayed between local sunrise and sunset, 
and the dog pictures displayed during the inverse.  
Also he wants the picture updated every 5 minutes.

So, the app should:
- Query the device’s location API to determine the exact location.
- Contact an external API to determine the precise sunrise/sunset times for this location.
- Obtain a picture from random cats or dogs api.
- Display the pix.
- Update every 5 minutes.

Willy won’t always keep the app open, 
and he frequently moves from one coffee shop to another while coding. 
 He doesn’t mind if the app, when opened, displays a previously download 
 pix as long as it isn’t stale (time wise).  
 He also is sort of a data nut, so if you want to put the local 
 coordinates (latitude and longitude) and the time of the next solar 
 event on the screen w/ the picture, that’s fine.

### API’s:

Sunrise/Sunset API:
https://sunrise-sunset.org/api

Random Dog Pictures:
https://random.dog/woof.json

Random Cat Pictures
https://aws.random.cat/meow

### Third Party Libraries

- [Retrofit 2](https://square.github.io/retrofit/) - REST api access.
- [Moshi (codegen)](https://github.com/square/moshi) - JSON deserialization
- [Dagger 2](https://google.github.io/dagger/) - Dependency injection
- [Picasso](https://github.com/square/picasso) - Image loading
- [Timber](https://github.com/JakeWharton/timber) - debug logging
- [ThreeTenApb](https://github.com/JakeWharton/ThreeTenABP) - java.time compatibility for earlier Android versions
- [EasyPermissions](https://github.com/googlesamples/easypermissions/tree/master/easypermissions/src/main/java/pub/devrel/easypermissions) - Simplifies Android permission request.