#!/bin/bash

PLUGIN="VaultManager"
PLUGIN_DESTINATION="${HOME}/minecraft/latest-paper/plugins"

./gradlew shadowJar

cd build/libs || exit

cp *.jar "${PLUGIN_DESTINATION}"/${PLUGIN}.jar

clean_flag='false'

while getopts 'c' flag; do
  # shellcheck disable=SC2220
  case "${flag}" in
    c) clean_flag='true' ;;
  esac
done

if [ "$clean_flag" = "true" ]; then
    echo "Cleaning ${PLUGIN} directory!"
    cd "$PLUGIN_DESTINATION" || exit
    rm -rf "${PLUGIN:?}"/
fi