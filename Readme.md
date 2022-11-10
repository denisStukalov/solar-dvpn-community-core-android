# SOLAR dVPN Community Core for Android

This project contains an application for Android, which is made as a server with a webView.  
API documentation is [here](https://github.com/solarlabsteam/solar-dvpn-community-core-docs).
iOS version of the app can be found [here](https://github.com/solarlabsteam/solar-dvpn-community-core-ios).

## Building

- Clone this repo.
- Open project in Android studio.
- Sync gradle files.
- Build.

### Pre run setup

- Set your Solar backend endpoint in the [Constants.kt](https://github.com/solarlabsteam/solar-dvpn-community-core-android/blob/da5130bab8467e949b254dee0e0501756efbf969/constants/src/main/java/ee/solarlabs/constants/Constants.kt#L11) file.
- Change the `applicationId` in the ui [build.gradle](https://github.com/solarlabsteam/solar-dvpn-community-core-android/blob/main/ui/build.gradle#L13) file to your unique id.

## UI integration
<details>
  <summary>Add your UI into the app</summary> 

- navigate to `ui/src/main/assets/www` folder
- add the root of your ui, making sure you don't forget your `index.html`.

</details>

## In-App Purchase:
<details>
  <summary>If you wish to use In-App Purchase in your application, make the following steps.</summary>

- Set up your [RevenueCat](https://www.revenuecat.com/docs/getting-started) project.
- Set your purchase API key in [Constants.kt](https://github.com/solarlabsteam/solar-dvpn-community-core-android/blob/da5130bab8467e949b254dee0e0501756efbf969/constants/src/main/java/ee/solarlabs/constants/Constants.kt#L14) file.
- Use our [Purchase API](https://github.com/solarlabsteam/solar-dvpn-community-core-docs/tree/main/api/purchases).

</details>

---

## Troubleshooting:

Do not hesitate to create an issue for our team.

