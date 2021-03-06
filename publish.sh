#!/bin/sh

setEnvVariable () {
  EXPORT_TOKEN="\nexport $1=$2"
    if test -e ~/.bashrc; then
        echo $EXPORT_TOKEN >> ~/.bashrc
    fi
    if test -e ~/.bash_profile; then
        echo $EXPORT_TOKEN >> ~/.bash_profile
    fi
}

notifyApi () {
  PLATFORM=$1
  VERSION=$2
  BUNDLE_NAME=$3
  curl "https://api.bitmovin.com/v1/admin/analytics/releases/$PLATFORM/$VERSION" -H "X-Api-Key:$ANALYTICS_API_RELEASE_TOKEN" -H "Content-Type:application/json" \
    --data-binary "{
        \"group\": \"com.bitmovin.analytics\",
        \"repositoryUrl\":\"http://bitmovin.bintray.com/maven\",
        \"name\": \"$BUNDLE_NAME\"
      }" > /dev/null 2>&1

  if [ $? -eq 0 ]; then
    echo "Published Release $PLATFORM:$VERSION:$BUNDLE_NAME to Bitmovin API"
  else
    echo "Failed to publish release to Bitmovin API"
  fi
}


if test -e ~/.bashrc; then
  source ~/.bashrc
fi
if test -e ~/.bash_profile; then
  source ~/.bash_profile
fi

if [ -z "$ANALYTICS_GH_TOKEN" ]; then
    echo "ANALYTICS_GH_TOKEN not found in environment variables. You need to provide the Github Access Token for the user bitAnalyticsCircleCi. This can be found in LastPass."
    echo "Enter the token:"
    read ANALYTICS_GH_TOKEN
    setEnvVariable "ANALYTICS_GH_TOKEN" $ANALYTICS_GH_TOKEN
fi

if [ -z "$ANALYTICS_API_RELEASE_TOKEN" ]; then
    echo "ANALYTICS_API_RELEASE_TOKEN not found in environment variables. You need to provide the API Key for the user dhi+analytics-admin-user for https://api.bitmovin.com."
    echo "Enter the token:"
    read ANALYTICS_API_RELEASE_TOKEN
    setEnvVariable "ANALYTICS_API_RELEASE_TOKEN" $ANALYTICS_API_RELEASE_TOKEN
fi

echo "Make sure to bump the libraryVersion and versionCode in the <root>/build.gradle file, README and CHANGELOG first and merge that PR into develop."
echo "Version (without leading \"v\")":
read VERSION
git checkout develop
git pull
git checkout main
git pull
git merge develop
git tag -a v$VERSION -m "v$VERSION"
git push origin main v$VERSION

echo "Pushed \"main\" and \"$VERSION\" to repo."

curl \
  -u bitAnalyticsCircleCi:$ANALYTICS_GH_TOKEN \
  -X POST \
  -H "Accept: application/vnd.github.v3+json" \
  https://api.github.com/repos/bitmovin/bitmovin-analytics-collector-android/releases \
  -d "{\"tag_name\":\"v$VERSION\", \"name\": \"v$VERSION\", \"draft\": false}"

echo "Created release in public repo."

echo "Creating and publishing :collector project..."
./gradlew -PdevelopLocal=false :collector:clean
./gradlew -PdevelopLocal=false :collector:build
./gradlew -PdevelopLocal=false :collector:assembleRelease
./gradlew -PdevelopLocal=false :collector:artifactoryPublish
echo "Created and published :collector project."

echo "Creating and publishing :collector-bitmovin-player project..."
./gradlew -PdevelopLocal=false :collector-bitmovin-player:clean
./gradlew -PdevelopLocal=false :collector-bitmovin-player:build
./gradlew -PdevelopLocal=false :collector-bitmovin-player:assembleRelease
./gradlew -PdevelopLocal=false :collector-bitmovin-player:artifactoryPublish
echo "Created and published :collector-bitmovin-player project."

echo "Creating and publishing :collector-exoplayer project..."
./gradlew -PdevelopLocal=false :collector-exoplayer:clean
./gradlew -PdevelopLocal=false :collector-exoplayer:build
./gradlew -PdevelopLocal=false :collector-exoplayer:assembleRelease
./gradlew -PdevelopLocal=false :collector-exoplayer:artifactoryPublish
echo "Created and published :collector-exoplayer project."

file="./bitmovin.properties"

if [ -f "$file" ]
then
  echo "$file found."

  while IFS='=' read -r key value
  do
    key=$(echo $key | tr '.' '_')
    eval ${key}=\${value}
  done < "$file"
else
  echo "$file not found."
fi

echo "Distributing the artifacts to bintray..."

curl \
  -u ${artifactoryUser}:${artifactoryPassword} \
  -X POST \
  https://bitmovin.jfrog.io/artifactory/api/distribute \
  -H "Content-Type: application/json" \
  -d "{
    \"targetRepo\": \"releases\",
    \"overrideExistingFiles\": true,
    \"packagesRepoPaths\": [
      \"libs-release-local/com/bitmovin/analytics/collector/$VERSION/collector-$VERSION.pom\",
      \"libs-release-local/com/bitmovin/analytics/collector/$VERSION/collector-$VERSION.aar\",
      \"libs-release-local/com/bitmovin/analytics/collector-bitmovin-player/$VERSION/collector-bitmovin-player-$VERSION.pom\",
      \"libs-release-local/com/bitmovin/analytics/collector-bitmovin-player/$VERSION/collector-bitmovin-player-$VERSION.aar\",
      \"libs-release-local/com/bitmovin/analytics/collector-exoplayer/$VERSION/collector-exoplayer-$VERSION.pom\",
      \"libs-release-local/com/bitmovin/analytics/collector-exoplayer/$VERSION/collector-exoplayer-$VERSION.aar\"
    ]}"

echo "\nDistributed the artifacts to bintray."

notifyApi "android-bitmovin" $VERSION "collector-bitmovin-player"
notifyApi "android-exo" $VERSION "collector-exoplayer"

echo "Don't forget to update the changelog in Contentful."
open "https://app.contentful.com/spaces/blfijbdi3ei3/entries"
