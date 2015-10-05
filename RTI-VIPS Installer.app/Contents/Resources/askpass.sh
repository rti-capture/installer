#!/bin/bash

SCRIPTDIR=`dirname "$BASH_SOURCE"`

"$SCRIPTDIR/../PlugIns/JRE/Contents/Home/bin/java" -cp "$SCRIPTDIR/../Java/org/glacsweb/rti/rti-vips-installer/1.0-SNAPSHOT/rti-vips-installer-1.0-SNAPSHOT.jar:$SCRIPTDIR/../Java/commons-io/commons-io/2.2/commons-io-2.2.jar" org.glacsweb.rti.installer.PasswordRequest
