#!/bin/bash

TARGET=target
SOURCE=RTI-VIPS\ Installer.app

APP=${TARGET}/${SOURCE}

rm -rf "$APP"
cp -r "$SOURCE" "$TARGET"

# Install the installation script
cp script.json "$APP/Contents/Resources/Java"

cp "target/rti-vips-installer-1.0-SNAPSHOT.jar" "target/RTI-VIPS Installer.app/Contents/Java//org/glacsweb/rti/rti-vips-installer/1.0-SNAPSHOT/rti-vips-installer-1.0-SNAPSHOT.jar"

hdiutil create -size 500m -srcfolder "target/RTI-VIPS Installer.app" -o "target/RTI-VIPS Installer.dmg"
