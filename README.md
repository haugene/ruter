ruter
=====

It's almost ready to go, but how do we make it build?

Download the SDK: http://developer.android.com/sdk/index.html
Intall Android SDK, when done choose API 14(Android 4.0)

Set ANDROID_HOME path(for me: C:\Program Files (x86)\Android\android-sdk)
Add %ANDROID_HOME%\tools and %ANDROID_HOME%\platform-tools to %PATH

You also need a keystore, but we'll configure that later. For now, use the test key store from the archetype
To do this you must call: mvn clean install -Prelease -Dsign.keystore=/path/to/your/project/test-key.keystore -Dsign.alias=mykey -Dsign.storepass=testtest -Dsign.keypass=testtest

This is scripted in buildWithTestKey.bat, so just run it.

When we descide to get our own key we need to undeploy all our artifacts before we re-deploy. This is due to:
"Be aware that Android cannot re-deploy artifacts using a different key, so be sure to undeploy all artifacts before running the release."

Now it almost builds, but the integration test project will fail
The plugin tries to contact a emulated device, or a developer phone.
NEXT STEP: Emulator!

And, as always:
mvn eclipse:eclipse will generate eclipse projects

btw:
http://code.google.com/p/maven-android-plugin/wiki/GettingStarted<br />
http://developer.android.com/about/versions/android-4.0.html<br />
https://github.com/akquinet/android-archetypes