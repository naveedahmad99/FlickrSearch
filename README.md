# FlickrSearch (Kotlin)
---


The application has a very basic UI. No work has been done on the design due to shortage of 
time and a lesser creative mind. The app has the following structure.

* screenshots

* activity
    * `MainActivity` *(Base class for handling common functionality for all `Activities`)*
* adapter
    * `GenericAdapter` *(Adapter class for handling data)*
    * `CustomBindingAdapter` *(Binding Adapter class for handling ImageView src)*
* fragment
    * `BaseFragment` *(Base class for handling common functionality for all `Fragments`)*
    * `HomeFragment`
* interfaces
    * `ActivityDestroyedListener`
    * `CurrentActivityListener`
* models
    * `FlickrPhoto` *(Model for Flickr Photo Object)*
    * `SearchPhotosResponse` *(Model for API Response)*
* utils
    * `NetworkRequestUtil` *(Handled all the network operations here)*
    
## Probable Improvements for future
* Improve Image Caching Library to use local cache and avoid external storage.
* Use Serialization/Deserialization libraries to avoid writing long parser classes.
* Handle error codes with `Api` class and send meaningful callbacks.
* Improve Design to make it more fluid and follow Material Guidelines.
* Write UI and Unit Test cases. (Will need use of Third Party Libraries, hence avoided)
* Need to update Proguard for code obfuscation.
* Refactor code to follow a Clean Architecture guideline and improve usage of interfaces and dependency injection to avoid tight coupling between classes.
