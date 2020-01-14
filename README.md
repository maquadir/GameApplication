# GameApplication
This is an Android Application written in Kotlin to create a guessing game that displays an image and 3 headlines. The player is rewarded for a correct match between image and headline.
The game displays every question from the json feed, in order, until the player completes all the questions.

# Application Requirements

###	Display a questions page which should show:
  -	The current total score.
  -	The image.
  -	The standfirst.
  -	The 3 possible answers.
  -	A button to read the article.
  -	A button to skip to the next question.
  
### Score is to be accumulative and is defined as:
  -	Two points for a correct answer.
  -	Minus one point for an incorrect answer.
  -	No points for skipping the question.



# Installation
Clone the repo and install the dependencies.
        
      git clone git@github.com:maquadir/GameApplication.git
      
      
# Architecture and Design
The application follows an MVVM architecture as given below

<img width="449" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/72322705-20034d80-36fb-11ea-8936-7091e6cd1a70.png">
      
# Setup
### Manifest File
- Since the app is going to fetch from json url .We have to add the following internet permissions to the manifest file.
    
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 
    
### Material Styling
- A progress bar is displayed during the async JSON read operation.
- A CardView to display the details with rounded corners and a background shadow 
- Montserrat Font styling for texts

### Setup api and repository to read from JSON URL
        val api = HeadlineApi()

### Gets reference to Headlinedao from HeadlineDatabase to construct the correct HeadlineRepository.
        headlinedao = HeadlineDatabase.getDatabase(application).getHeadlineDao()
        val repository = HeadlineRepository(api,headlinedao)

### Setup view model
        factory =
            HeadlineViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, factory).get(HeadlineViewModel::class.java)

### Get data from json reponse
        viewModel.getHeadlines()

### Invoke JSON Url using Retrofit.Builder()
We have declared a Properties API interface to invoke the JSON url using Retrofit.Builder()

         return Retrofit.Builder()
                .baseUrl(BASE_URl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(HeadlineApi::class.java)
                
### Model
A Modelcontains all the data classes, database classes, API and repository.
A Headline data class is created using "JSON to Kotlin class" plugin to map the JSON data to Kotlin. A Headline Api class to handle api requests and a repository takes care of how data will be fetched from the api.
              
              @Entity(tableName = "headline_table")
              @TypeConverters(ItemTypeConverter::class)
              data class Headline(
              
                  @ColumnInfo(name = "items")
                  val items: List<Item>,
              
                  @ColumnInfo(name = "product")
                  val product: String,
              
                  @ColumnInfo(name = "resultSize")
                  val resultSize: Int,
              
                  @ColumnInfo(name = "version")
                  val version: Int
              ){
                  @PrimaryKey(autoGenerate = true)
                  var id : Int = 0
              }

            data class Item(
            
                @ColumnInfo(name = "correctAnswerIndex")
                val correctAnswerIndex: Int,
            
                @ColumnInfo(name = "headlines")
                val headlines: List<String>,
            
                @ColumnInfo(name = "imageUrl")
                val imageUrl: String,
            
                @ColumnInfo(name = "section")
                val section: String,
            
                @ColumnInfo(name = "standFirst")
                val standFirst: String,
            
                @ColumnInfo(name = "storyUrl")
                val storyUrl: String
            )

                  
              val api = PropertiesApi()
              val repository = PropertiesRepository(api)


### Coroutines
Coroutines are a great way to write asynchronous code that is perfectly readable and maintainable. We use it to perform a job of reading data from the JSON url.

        //coroutines with a callback parameter
        fun<T: Any> ioThenMain(work: suspend (() -> T?), callback: ((T?)->Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async  rt@{
                return@rt work()
            }.await()
            callback(data)
        }


        //coroutines with a single parameter used during inserting data
        fun<T: Any> ioThenMain(work: suspend (() -> T?)) =
            CoroutineScope(Dispatchers.Main).launch {
                CoroutineScope(Dispatchers.IO).async  rt@{
                    return@rt work()
                }.await()
    
            }

        job = viewModelScope.launch {
                   Coroutines.ioThenMain(
                       { repository.getHeadlines() },
                { _headlines.value = it }
                )
                   }

### View
It is the UI part that represents the current state of information that is visible to the user.
- The View model observes any data change and fetches from JSON and adds to Room Database.

       //observe the data that is fetched from JSON and add it to Room database
        viewModel.headlines.observe(this, Observer { headlines ->

            val headline = Headline(headlines.items,headlines.product,headlines.resultSize,headlines.version)
            viewModel.insertHeadlines(headline)
            Log.i("Created","value inserted")

            //initiate data fetch from Room database
            viewModel.getHeadlinesDb()

        })
        
        
- We use Glide to display  image using data binding
      
      @BindingAdapter("image")
      fun loadImage(view: ImageView, url: String) {
          Glide.with(view)
              .load(url)
              .into(view)
      }
      
- Fetch data from Room database and perform databinding to layout

        viewModel.headlinesfetch.observe(this, Observer { headlines ->
            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE

            //display the first entry on screen
            binding.headlineItem = viewModel.headlines.value?.items?.get(0)

        })

### Data Binding
The Data Binding Library is an Android Jetpack library that allows you to bind UI components in your XML layouts to data sources in your app using a declarative format rather than programmatically.All the UIView elements in the layout are binded to views through data binding.

### Build Gradle
We declare the respective dependencies 

    def room_version = "2.2.3"
    def nav_version = "2.1.0"

    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.core:core-ktx:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'

    //Retrofit and GSON
    implementation 'com.squareup.retrofit2:retrofit:2.6.0'
    implementation 'com.squareup.retrofit2:converter-moshi:2.6.0'

    //Kotlin Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.1'
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.1"

    implementation 'com.google.android.material:material:1.0.0'

    // ViewModel and LiveData
    implementation "androidx.lifecycle:lifecycle-extensions:2.1.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0-rc03"

    //New Material Design
    implementation 'com.google.android.material:material:1.2.0-alpha03'

    //Glide
    implementation 'com.github.bumptech.glide:glide:4.10.0'

    //Room and lifecycle dependencies
    kapt "androidx.room:room-compiler:2.2.3"
    implementation "androidx.lifecycle:lifecycle-extensions:2.1.0"
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"

    // Kotlin
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    //GSON
    implementation 'com.google.code.gson:gson:2.8.6'

# Screenshots
<img width="350" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/72323672-5b9f1700-36fd-11ea-92c3-6e0e95a6a44d.png"> <img width="350" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/72323671-5b068080-36fd-11ea-91c8-9eb0bc991541.png">
<img width="350" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/72323670-5b068080-36fd-11ea-9209-ac570e62522c.png"> <img width="350" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/72323669-5b068080-36fd-11ea-9b58-2807bf97357b.png">

# Generating signed APK
From Android Studio:

- Build menu
- Generate Signed APK...

# Support
- Stack Overflow
- Google



