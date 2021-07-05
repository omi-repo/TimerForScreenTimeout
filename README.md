# TimerForScreenTimeout README (Timer for Screen Timeout)

Timer for Screen Timeout is an Android app for for locking phone by counting down then locking the phone. And saved it to persistence DB (Room) to show in history view.

## Getting Started

The app is a demo app for using the latest API from Jetpack in 2021 in order to complete the app. It uses MVVM architecture.

## Images

|<img src="https://raw.githubusercontent.com/omi-repo/TimerForScreenTimeout/master/device-2021-07-05-102028.png" alt="1" width="200"/>|<img src="https://raw.githubusercontent.com/omi-repo/TimerForScreenTimeout/master/device-2021-07-05-101917.png" alt="2" width="200"/>|<img src="https://raw.githubusercontent.com/omi-repo/TimerForScreenTimeout/master/device-2021-07-05-102051.png" alt="2" width="200"/>|

## Libraries Used

 - [ConstraintLayout] - ConstraintLayout allows you to create large and complex layouts with a flat view hierarchy (no nested view groups).
 - [Navigation component] - Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app.
 - [Timber](https://github.com/JakeWharton/timber) - This is a logger with a small, extensible API which provides utility on top of Android's normal `Log`  class.
 - [Room](https://developer.android.com/training/data-storage/room) - The Room persistence library provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.
 - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel?gclid=CjwKCAjw2ZaGBhBoEiwA8pfP_kqGbdCYtNCokywuc2fxridsv0tRcUhSfNKtsfvj2iCMtFIDMLCGnxoC2HUQAvD_BwE&gclsrc=aw.ds) - The `ViewModel` class is designed to store and manage UI-related data in a lifecycle conscious way. The `ViewModel` class allows data to survive configuration changes such as screen rotations.
 - [ViewModelFactory](https://developer.android.com/codelabs/kotlin-android-training-view-model#7) - We can not create ViewModel on our own. We need ViewModelProviders utility provided by Android to create ViewModels. But ViewModelProviders can only instantiate ViewModels with no arg constructor. So if I have a ViewModel with multiple arguments, then I need to use a Factory that I can pass to ViewModelProviders to use when an instance of MyViewModel is required.
 - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - LiveData is an observable data holder class. Unlike a regular observable, LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services. This awareness ensures LiveData only updates app component observers that are in an active lifecycle state.
 - [DataBinding](https://developer.android.com/topic/libraries/data-binding) - The Data Binding Library is a support library that allows you to bind UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
 - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project. Hilt is built on top of the popular DI library Dagger to benefit from the compile-time correctness, runtime performance, scalability, and Android Studio support that Dagger provides.

## Usage

Use Android Studio, then `File > New > Project from Version Control`. Then run the project with your emulator.

License
-------

    Copyright 2021 Romi

    Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

[ConstraintLayout]:<https://developer.android.com/training/constraint-layout>
[Navigation component]:<https://developer.android.com/guide/navigation?gclid=CjwKCAjw2ZaGBhBoEiwA8pfP_tmAXdzAmmwMOrK8Rkd95ePmMqCaHVmq_6X7FftoB4i-vT0E5Wuy-xoCW4gQAvD_BwE&gclsrc=aw.ds>
